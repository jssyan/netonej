package com.syan.netonej.exception;

/**
 * @Author mmdet
 * @Date 2021-06-09 10:50
 * @Description
 */
public enum ErrorCode {
    UNDEFINED_ERROR(-1,"未知的错误"),
    SUCCESS(200,"成功"),
    BAD_PARAM(400,"错误的请求"),
    NOT_HAVA_ACCESS_OR_KEY_ERROR(403,"无权访问或密钥错误"),
    PCS_THE_KEY_NOT_FOUND(404,"选择的密钥不存在"),
    SERVER_500_ERROR(500,"服务端异常错误"),
    X509_V_ERR_UNABLE_TO_GET_ISSUER_CERT(802,"找不到该证书的签发者（CA）证书"),
    X509_V_ERR_UNABLE_TO_GET_CRL(803,"找不到和该证书相关的CRL"),
    X509_V_ERR_UNABLE_TO_DECRYPT_CERT_SIGNATURE(804,"无法解开证书里的签名"),
    X509_V_ERR_UNABLE_TO_DECRYPT_CRL_SIGNATURE(805,"无法解开CRL里的签名"),
    X509_V_ERR_UNABLE_TO_DECODE_ISSUER_PUBLIC_KEY(806,"无法得到证书里公钥信息"),
    X509_V_ERR_CERT_SIGNATURE_FAILURE(807,"证书签名无效"),
    X509_V_ERR_CRL_SIGNATURE_FAILURE(808,"证书相关CRL签名无效"),
    X509_V_ERR_CERT_NOT_YET_VALID(809,"证书未生效"),
    X509_V_ERR_CERT_HAS_EXPIRED(810,"证书失效"),
    X509_V_ERR_CRL_NOT_YET_VALID(811,"与证书相关的CRL未生效"),
    X509_V_ERR_CRL_HAS_EXPIRED(812,"与证书相关的CRL过期"),
    X509_V_ERR_ERROR_IN_CERT_NOT_BEFORE_FIELD(813,"证书生效日期格式非法"),
    X509_V_ERR_ERROR_IN_CERT_NOT_AFTER_FIELD(814,"证书失效日期格式非法"),
    X509_V_ERR_ERROR_IN_CRL_LAST_UPDATE_FIELD(815,"CRL的lastUpdate格式非法"),
    X509_V_ERR_ERROR_IN_CRL_NEXT_UPDATE_FIELD(816,"CRL的nextUpdate格式非法"),
    X509_V_ERR_OUT_OF_MEM(817,"处理时内存不足"),
    X509_V_ERR_DEPTH_ZERO_SELF_SIGNED_CERT(818,"需要验证的第一个证书是自签名证书，而却不在信任证书列表中"),
    X509_V_ERR_SELF_SIGNED_CERT_IN_CHAIN(819,"可以建立证书链，但在本地找不到根"),
    X509_V_ERR_UNABLE_TO_GET_ISSUER_CERT_LOCALLY(820,"证书链不完整"),
    X509_V_ERR_UNABLE_TO_VERIFY_LEAF_SIGNATURE(821,"证书链只有一项，却不是根"),
    X509_V_ERR_CERT_CHAIN_TOO_LONG(822,"证书链过长"),
    X509_V_ERR_CERT_REVOKED(823,"证书已被废除"),
    X509_V_ERR_INVALID_CA(824,"CA证书无效"),
    X509_V_ERR_PATH_LENGTH_EXCEEDED(825,"BasicConstraints pathlentgh过长"),
    X509_V_ERR_INVALID_PURPOSE(826,"提供的证书不能用于该请求用途"),
    X509_V_ERR_CERT_UNTRUSTED(827,"根CA证书用在该请求用途是不被信任的"),
    X509_V_ERR_CERT_REJECTED(828,"CA证书不可用作该请求用途"),
    X509_V_ERR_SUBJECT_ISSUER_MISMATCH(829,"证书颁发者名称和CA证书不一致"),
    X509_V_ERR_AKID_SKID_MISMATCH(830,"证书密钥标志和其他颁发"),
    X509_V_ERR_AKID_ISSUER_SERIAL_MISMATCH(831,"证书颁发者序号和CA证书不一致"),
    X509_V_ERR_KEYUSAGE_NO_CERTSIGN(832,"CA证书用途不包括为其他证书签名"),
    X509_V_ERR_UNABLE_TO_GET_CRL_ISSUER(833,"无法获取CRL的签发者信息"),
    X509_V_ERR_UNHANDLED_CRITICAL_EXTENSION(834,"未处理临界拓展项"),
    X509_V_ERR_KEYUSAGE_NO_CRL_SIGN(835,"密钥用法中不包含CRL签发"),
    X509_V_ERR_UNHANDLED_CRITICAL_CRL_EXTENSION(836,"未处理CRL临界拓展项"),
    X509_V_ERR_INVALID_NON_CA(837,"无效的非法CA"),
    X509_V_ERR_PROXY_PATH_LENGTH_EXCEEDED(838,"代理路径长度过长"),
    X509_V_ERR_KEYUSAGE_NO_DIGITAL_SIGNATURE(839,"证书密钥用法中未包含数字签名"),
    X509_V_ERR_PROXY_CERTIFICATES_NOT_ALLOWED(840,"代理证书未不被允许"),
    X509_V_ERR_INVALID_EXTENSION(841,"无效的拓展项"),
    X509_V_ERR_INVALID_POLICY_EXTENSION(842,"无效的策略拓展项"),
    X509_V_ERR_NO_EXPLICIT_POLICY(843,"没有明确的策略"),
    X509_V_ERR_UNNESTED_RESOURCE(844,"非巢状的资源"),
    X509_V_ERR_APPLICATION(850,"根证书被废除"),
    PKCS7_R_DECRYPTED_KEY_IS_WRONG_LENGTH(900,"PKCS7解密密钥长度错误"),
    PKCS7_R_DIGEST_FAILURE(901,"摘要失败"),
    PKCS7_R_MISSING_CERIPEND_INFO(903,"缺少CERIPEND信息"),
    PKCS7_R_OPERATION_NOT_SUPPORTED_ON_THIS_TYPE(904,"该类型的操作不支持"),
    PKCS7_R_SIGNATURE_FAILURE(905,"签名失败"),
    PKCS7_R_UNABLE_TO_FIND_CERTIFICATE(906,"未找到签名证书"),
    PKCS7_R_UNABLE_TO_FIND_MEM_BIO(907,"未找到 MEM BIO"),
    PKCS7_R_UNABLE_TO_FIND_MESSAGE_DIGEST(908,"未找到签名原文的摘要信息"),
    PKCS7_R_UNKNOWN_DIGEST_TYPE(909,"未知的摘要算法类型"),
    PKCS7_R_UNKNOWN_OPERATION(910,"未知的操作类型"),
    PKCS7_R_UNSUPPORTED_CIPHER_TYPE(911,"不支持的密码算法类型"),
    PKCS7_R_UNSUPPORTED_CONTENT_TYPE(912,"不支持的签名内容类型"),
    PKCS7_R_WRONG_CONTENT_TYPE(913,"错误的签名内容类型"),
    PKCS7_R_WRONG_PKCS7_TYPE(914,"错误的PKCS#7签名类型"),
    PKCS7_R_NO_RECIPIENT_MATCHES_CERTIFICATE(915,"无接受者的证书信息"),
    PKCS7_R_CIPHER_NOT_INITIALIZED(916,"CIPHER未初始化"),
    PKCS7_R_CERTIFICATE_VERIFY_ERROR(917,"证书验证错误"),
    PKCS7_R_CONTENT_AND_DATA_PRESENT(918,"内容和数据已存在"),
    PKCS7_R_DECRYPT_ERROR(919,"解密错误"),
    PKCS7_R_ERROR_ADDING_RECIPIENT(920,"加入接受者时错误"),
    PKCS7_R_ERROR_SETTING_CIPHER(921,"设定的加密算法错误"),
    PKCS7_R_NO_CONTENT(922,"缺少签名内容"),
    PKCS7_R_NO_SIGNATURES_ON_DATA(923,"数据没有签名"),
    PKCS7_R_PKCS7_ADD_SIGNATURE_ERROR(924,"创建数字签名错误"),
    PKCS7_R_PKCS7_DATAFINAL_ERROR(925,"签名的最后一个数据错误"),
    PKCS7_R_PKCS7_DATAFINAL(926,"签名的最后一个数据不存在"),
    PKCS7_R_PRIVATE_KEY_DOES_NOT_MATCH_CERTIFICATE(927,"私钥不匹配该证书"),
    PKCS7_R_SIGNER_CERTIFICATE_NOT_FOUND(928,"签名证书不存在"),
    PKCS7_R_SMIME_TEXT_ERROR(929,"SMIME信息错误"),
    PKCS7_R_DECODE_ERROR(930,"签名解码错误"),
    PKCS7_R_INVALID_MIME_TYPE(931,"无效的MIME类型"),
    PKCS7_R_MIME_NO_CONTENT_TYPE(932,"无MIME内容类型"),
    PKCS7_R_MIME_PARSE_ERROR(933,"MIME解析错误"),
    PKCS7_R_MIME_SIG_PARSE_ERROR(934,"MIME地理信息解析错误"),
    PKCS7_R_NO_CONTENT_TYPE(935,"无内容类型"),
    PKCS7_R_NO_MULTIPART_BODY_FAILURE(936,"无多个机构失效"),
    PKCS7_R_NO_MULTIPART_BOUNDARY(937,"多处无边界限制"),
    PKCS7_R_NO_SIG_CONTENT_TYPE(938,"无地理信息内容类型"),
    PKCS7_R_PKCS7_PARSE_ERROR(939,"解析错误"),
    PKCS7_R_PKCS7_SIG_PARSE_ERROR(940,"地理信息解析错误"),
    PKCS7_R_SIG_INVALID_MIME_TYPE(941,"无效的地理信息MIME类型"),
    PKCS7_R_NO_SIGNERS(942,"无签名信息"),
    PKCS7_R_INVALID_NULL_POINTER(943,"无效的空指针"),
    PKCS7_R_CIPHER_HAS_NO_OBJECT_IDENTIFIER(944,"加密算法缺少对象标识符"),
    PKCS7_R_PKCS7_DATASIGN(945,"数据符号错误"),
    PKCS7_R_NO_RECIPIENT_MATCHES_KEY(946,"无接受者的MATCHES KEY");

    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static String getStatusCodeMessage(int code){
        String errMsg = "";
        ErrorCode[] codes = ErrorCode.values();
        for (ErrorCode error:codes){
            if(error.getCode() == code){
               return error.getMessage();
            }
        }
        return errMsg;
    }

}
