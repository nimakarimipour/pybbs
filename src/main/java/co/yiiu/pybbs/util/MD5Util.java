package co.yiiu.pybbs.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://atjiu.github.io
 */
public class MD5Util {

    private static Logger log = LoggerFactory.getLogger(MD5Util.class);

    private final static char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
            'f'};
    private static MessageDigest messagedigest = null;

    static {
        try {
            messagedigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            log.error("{}aaaaa，MessageDigestaaaMD5Util, errorMessage: {}", MD5Util.class.getName(), e.getMessage());
        }
    }

    private MD5Util() {
    }

    /**
     * aa：aaaaMD5.aaaaaMD5(aa+{aa})
     *
     * @param password aa
     * @param salt     aa
     * @return String
     * @author aaa
     * @date 2014a06a24a
     */
    public static String getMD5StringWithSalt(String password, String salt) {
        if (password == null) {
            throw new IllegalArgumentException("passwordaaanull");
        }
        if (salt.equals("") || salt.length() == 0) {
            throw new IllegalArgumentException("saltaaaa");
        }
        if ((salt.lastIndexOf('{') != -1) || (salt.lastIndexOf('}') != -1)) {
            throw new IllegalArgumentException("saltaaaaa { aa }");
        }
        return getMD5String(password + '{' + salt + '}');
    }

    /**
     * aa：aaaaamd5a。
     *
     * @param file aa。
     * @return String
     * @throws IOException aaaaIOaaa。
     * @author aaa
     * @date 2014a06a24a
     */
    public static String getFileMD5String(File file) throws IOException {
        FileInputStream in = new FileInputStream(file);
        FileChannel ch = in.getChannel();
        MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
        messagedigest.update(byteBuffer);
        return bufferToHex(messagedigest.digest());
    }

    /**
     * aa：aaaaaaaaMD5a。
     *
     * @param str aaa
     * @return String
     * @author aaa
     * @date 2014a06a24a
     */
    public static String getMD5String(String str) {
        return getMD5String(str.getBytes());
    }

    private static String getMD5String(byte[] bytes) {
        messagedigest.update(bytes);
        return bufferToHex(messagedigest.digest());
    }

    private static String bufferToHex(byte[] bytes) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte[] bytes, int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & 0xf0) >> 4];
        char c1 = hexDigits[bt & 0xf];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

    /*
     * aaaaa
     */
    public static String hexdigest(byte[] paramArrayOfByte) {
        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(paramArrayOfByte);
            byte[] arrayOfByte = localMessageDigest.digest();
            char[] arrayOfChar = new char[32];
            int i = 0;
            int j = 0;
            for (; ; ) {
                if (i >= 16) {
                    return new String(arrayOfChar);
                }
                int k = arrayOfByte[i];
                int m = j + 1;
                arrayOfChar[j] = hexDigits[(0xF & k >>> 4)];
                j = m + 1;
                arrayOfChar[m] = hexDigits[(k & 0xF)];
                i++;
            }

        } catch (Exception localException) {
            return null;
        }
    }
}
