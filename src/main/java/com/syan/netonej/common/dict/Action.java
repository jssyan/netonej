package com.syan.netonej.common.dict;

/**
 * @Author mmdet
 * @Date 2021-08-13 14:37
 * @Description
 */
public class Action {

    /**
     * PCS - Action 列出可用的Key ID
     */
    public final static String PCS_ACTION_SL = "sl.svr";
    /**
     * PCS - Action 获取与Key ID对应的X.509证书
     */
    public final static String PCS_ACTION_SG = "sg.svr";
    /**
     * PCS - Action 生成PKCS#1 数字签名
     */
    public final static String PCS_ACTION_SMP1 = "smp1.svr";
    /**
     * PCS - Action 生成PKCS#7 数字签名
     */
    public final static String PCS_ACTION_SMP7 = "smp7.svr";
    /**
     * PCS - Action 私钥加密
     */
    public final static String PCS_ACTION_SPE = "spe.svr";
    /**
     * PCS - Action 私钥解密
     */
    public final static String PCS_ACTION_SPD = "spd.svr";
    /**
     * PCS - Action 公钥加密
     */
    public final static String PCS_ACTION_PPE = "ppe.svr";
    /**
     * PCS - Action 公钥解密
     */
    public final static String PCS_ACTION_PPD = "ppd.svr";
    /**
     * PCS - Action  生成XML数字签名
     */
    public final static String PCS_ACTION_SXS = "sxs.svr";
    /**
     * PCS - Action 数字信封封包
     */
    public final static String PCS_ACTION_ENVSEAL = "envseal.svr";
    /**
     * PCS - Action 数字信封解包
     */
    public final static String PCS_ACTION_ENVOPEN = "envopen.svr";

    /**
     * PCS - Action 修改密钥访问口令
     */
    public final static String PCS_ACTION_CHPWD = "chpwd.svr";

    /**
     * PCS - Action 密钥派生
     */
    public final static String PCS_ACTION_CMACKDF = "cmac.svr";




    /**
     * TSA - Action 创建时间戳
     */
    public final static String TSA_ACTION_TSAC = "tsac.svr";

    /**
     * TSA - Action 创建时间戳
     */
    public final static String TSA_ACTION_TSRS = "tsrs.svr";

    /**
     * TSA - Action 验证时间戳
     */
    public final static String TSA_ACTION_TSAV = "tsav.svr";


    /**
     * SVS - Action 签名证书验证
     */
    public final static String SVS_ACTION_VC = "vc.svr";
    /**
     * SVS - Action PKCS#1签名验证
     */
    public final static String SVS_ACTION_VP1 = "vp1.svr";
    /**
     * SVS - Action PKCS#7签名验证
     */
    public final static String SVS_ACTION_VP7 = "vp7.svr";
    /**
     * SVS - Action XML签名验证
     */
    public final static String SVS_ACTION_VX = "vx.svr";
    /**
     * SVS - Action 获取证书列表
     */
    public final static String SVS_ACTION_LISTC = "listc.svr";

    public final static String EAPI_ACTION="/eapi.php";

}
