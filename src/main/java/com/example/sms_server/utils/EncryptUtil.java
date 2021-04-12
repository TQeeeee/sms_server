package cn.com.authority.utils;

import java.security.MessageDigest;

public class EncryptUtil {
    private static final String SHA = "SHA";
    private static final String SHA1 = "SHA1";
    private static final String MD5 = "MD5";
    private static final String HMAC_SHA1 = "HmacSHA1";
    /**
     * MD5 加密
     */
    public static String getMD5Str(String str) {
        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance(MD5);

            messageDigest.reset();

            messageDigest.update(str.getBytes("UTF-8"));

        } catch (Exception e) {
            System.out.println("NoSuchAlgorithmException caught!");
            System.exit(-1);
        }

        byte[] byteArray = messageDigest.digest();

        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(
                        Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }

        return md5StrBuff.toString();
    }

}
