package cn.com.syan.netonej;

import com.syan.netonej.common.dict.IdMagic;
import com.syan.netonej.http.client.PCSClient;
import com.syan.netonej.http.entity.NetoneResponse;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

import java.io.*;
import java.util.Arrays;

/**
 * @Author mmdet
 * @Date 2021-08-13 16:01
 * @Description
 */

public class APKFileSignDemo2 {
    private int versionOffset,versionLen,hashOffset,hashlen,signOffset,signLen,origanDataOffset,origanDataLen;

    static int readTagNumber(RandomAccessFile s, int tag) throws IOException {
        int tagNo = tag & 0x1f;

        //
        // with tagged object tag number is bottom 5 bits, or stored at the start of the content
        //
        if (tagNo == 0x1f)
        {
            tagNo = 0;

            int b = s.read();
            if (b < 31)
            {
                if (b < 0)
                {
                    throw new EOFException("EOF found inside tag value.");
                }
                throw new IOException("corrupted stream - high tag number < 31 found");
            }

            // X.690-0207 8.1.2.4.2
            // "c) bits 7 to 1 of the first subsequent octet shall not all be zero."
            if ((b & 0x7f) == 0)
            {
                throw new IOException("corrupted stream - invalid high tag number found");
            }

            while ((b & 0x80) != 0)
            {
                if ((tagNo >>> 24) != 0)
                {
                    throw new IOException("Tag number more than 31 bits");
                }

                tagNo |= (b & 0x7f);
                tagNo <<= 7;
                b = s.read();
                if (b < 0)
                {
                    throw new EOFException("EOF found inside tag value.");
                }
            }

            tagNo |= (b & 0x7f);
        }

        tag = 7788;

        return tagNo;
    }

    static int readLength(RandomAccessFile s, long limit) throws IOException {
        int length = s.read();
        if (0 == (length >>> 7))
        {
            // definite-length short form
            return length;
        }
        if (0x80 == length)
        {
            // indefinite-length
            return -1;
        }
        if (length < 0)
        {
            throw new EOFException("EOF found when length expected");
        }
        if (0xFF == length)
        {
            throw new IOException("invalid long form definite-length 0xFF");
        }

        int octetsCount = length & 0x7F, octetsPos = 0;

        length = 0;
        do
        {
            int octet = s.read();
            if (octet < 0)
            {
                throw new EOFException("EOF found reading length");
            }

            if ((length >>> 23) != 0)
            {
                throw new IOException("long form definite-length more than 31 bits");
            }

            length = (length << 8) + octet;
        }
        while (++octetsPos < octetsCount);

        if (length >= limit)   // after all we must have read at least 1 byte
        {
            throw new IOException("corrupted stream - out of bounds length found: " + length + " >= " + limit);
        }

        return length;
    }


    public void parse(File file){
        RandomAccessFile randomAccessFile = null;
        try{
            int offset = 0;
            randomAccessFile = new RandomAccessFile(file,"r");
            randomAccessFile.seek(1);
            int tag = randomAccessFile.read();
            System.out.println(tag);
            int tagNo  = readTagNumber(randomAccessFile,tag);
            int length = readLength(randomAccessFile, randomAccessFile.length());
            System.out.println(tag);
            System.out.println(length);
        } catch (Exception e){
            e.printStackTrace();
        }finally {

        }
    }

}
