package com.ottd.libs.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by hardyshi on 2017/9/15.
 */

public class  MD5 {
    public static char hexDigits[] = { // 用来将字节转换成 16 进制表示的字符
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };
    public static String getMd5(String string) {
        try {
            byte[] hash = MessageDigest.getInstance("MD5").digest(
                    string.getBytes("UTF-8"));
            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                if ((b & 0xFF) < 0x10) {
                    hex.append("0");
                }
                hex.append(Integer.toHexString(b & 0xFF));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return string;
    }

    public static String getFileMD5(File sourceFile) {
        String ret = "";
        if (sourceFile.exists() && sourceFile.length() > 0) {
            BufferedInputStream is = null;
            try {
                is = new BufferedInputStream(new FileInputStream(sourceFile));
                ret = getInputStreamMd5(is);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return ret;
    }

    public static String getInputStreamMd5(InputStream is) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");

            byte[] buffer = new byte[4196];
            int len;
            int readSize = 0;
            while ((len = is.read(buffer, 0, buffer.length)) != -1) {
                if (len > 0) {
                    md5.update(buffer, 0, len);
                    readSize += len;
                }
            }
            if (readSize == 0)
                return "";
            byte[] md5Bytes = md5.digest();
            return bytesToHexString(md5Bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
    public static String bytesToHexString(byte md5Bytes[]) {
        if (md5Bytes != null && md5Bytes.length == 16) {
            char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，所以表示成 16 进制需要 32 个字符
            int k = 0; // 表示转换结果中对应的字符位置
            for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节，转换成 16 进制字符的转换
                byte byte0 = md5Bytes[i]; // 取第 i个字节
                str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换， >>> 为逻辑右移，将符号位一起右移
                str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
            }
            return new String(str); // 换后的结果转换为字符串
        } else {
            return "";
        }
    }

}