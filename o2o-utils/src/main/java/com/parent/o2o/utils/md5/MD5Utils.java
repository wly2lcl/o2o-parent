package com.parent.o2o.utils.md5;

import java.security.MessageDigest;

/**
 * 
  *************************************
  * 类名称    : MD5Utils
  * 类描述    : md5加密解密工具类
  * 修改备注:
  *************************************
 */
public class MD5Utils {
    /**
     * 生成MD5摘要
     * @param srcBytes 原byte数组。
     * @return byte数组。
     */
    public static byte[] digist(byte[] srcBytes) {
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(srcBytes);
            return md.digest();
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

	 /*** 
     * MD5加码 生成32位md5码 
     */  
    public static String string2MD5(String inStr){  
        byte[] md5Bytes = digist(inStr.getBytes());
        StringBuilder hexValue = new StringBuilder();
        for (byte md5Byte : md5Bytes) {
            int val = ((int) md5Byte) & 0xff;
            if (val < 16) hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }  
        return hexValue.toString();  
  
    }  
  
    /** 
     * 加密解密算法 执行一次加密，两次解密 
     */   
    public static String convertMD5(String inStr){  
        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++){  
            a[i] = (char) (a[i] ^ 't');  
        }  
        String s = new String(a);  
        return s;  
  
    }  
}
