package com.syan.netonej.common;

import org.spongycastle.util.Arrays;

import java.io.*;

/**
 * <p>
 * This class provides...
 * </p>
 *
 * @author Iceberg on $Date:2015/4/18 12:53
 * @version $Revision $Date:2015/4/18 12:53
 * @since 1.0
 */
public class FileByteArrayReader {

    private static byte[] readFully(InputStream is, int length, boolean readAll)
            throws IOException {
        byte[] output = {};
        if (length == -1) length = Integer.MAX_VALUE;
        int pos = 0;
        while (pos < length) {
            int bytesToRead;
            if (pos >= output.length) { // Only expand when there's no room
                bytesToRead = Math.min(length - pos, output.length + 1024);
                if (output.length < pos + bytesToRead) {
                    output = Arrays.copyOf(output, pos + bytesToRead);
                }
            } else {
                bytesToRead = output.length - pos;
            }
            int cc = is.read(output, pos, bytesToRead);
            if (cc < 0) {
                if (readAll && length != Integer.MAX_VALUE) {
                    throw new EOFException("Detect premature EOF");
                } else {
                    if (output.length != pos) {
                        output = Arrays.copyOf(output, pos);
                    }
                    break;
                }
            }
            pos += cc;
        }
        return output;
    }

    public static byte[] read(String fileName) throws IOException {
        InputStream ins = new FileInputStream(fileName);

        byte[] data = readFully(ins, ins.available(), true);
        ins.close();

        return data;
    }
}