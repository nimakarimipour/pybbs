package co.yiiu.pybbs.util;

import org.springframework.util.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tomoya.
 * Copyright (c) 2018, All Rights Reserved.
 * https://atjiu.github.io
 */
public class StringUtil {

    private StringUtil() {
    }

    // aaaaa
    public static final String MOBILEREGEX = "^1[0-9]{10}";
    // emailaa
    public static final String EMAILREGEX = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";
    // urlaa
    public static final String URLREGEX = "^((https|http)?:\\/\\/)[^\\s]+";
    // aaaaa
    public static final String USERNAMEREGEX = "[a-z0-9A-Z]{2,16}";
    // aaaa
    public static final String PASSWORDREGEX = "[a-z0-9A-Z]{6,32}";
    // aaaaaaaaaaaaaa
    private static final char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c',
            'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
            'z'};
    // aaaaaaaaaaaaaa
    private static final char[] digits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    public static final Random random = new Random();

    public static boolean check(String text, String regex) {
        if (StringUtils.isEmpty(text)) {
            return false;
        } else {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(text);
            return matcher.matches();
        }
    }

    /**
     * aaaaaaaaaa
     *
     * @param length
     * @return
     */
    public static String randomString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int loop = 0; loop < length; ++loop) {
            sb.append(hexDigits[random.nextInt(hexDigits.length)]);
        }
        return sb.toString();
    }

    /**
     * aaaaaaaaa
     *
     * @param length
     * @return
     */
    public static String randomNumber(int length) {
        StringBuilder sb = new StringBuilder();
        for (int loop = 0; loop < length; ++loop) {
            sb.append(digits[random.nextInt(digits.length)]);
        }
        return sb.toString();
    }

    // aaaauuid
    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * aaaaaaaaccessToken
     */
    public static boolean isUUID(String accessToken) {
        if (StringUtils.isEmpty(accessToken)) {
            return false;
        } else {
            try {
                // noinspection ResultOfMethodCallIgnored
                UUID.fromString(accessToken);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    // aaaurlaaaaaamap
    // paramsaa：a=1&b=2&c=3
    // aa：{a: 1, b: 2, c: 3}
    public static Map<String, Object> formatParams(String params) {
        if (StringUtils.isEmpty(params)) return null;
        Map<String, Object> map = new HashMap<>();
        for (String s : params.split("&")) {
            String[] ss = s.split("=");
            map.put(ss[0], ss[1]);
        }
        return map;
    }

    // aaaaaataaaa
    public static List<String> fetchAtUser(String content) {
        if (StringUtils.isEmpty(content)) return Collections.emptyList();
        // aa ``` ``` aaaaa
        content = content.replaceAll("```([\\s\\S]*)```", "");
        // aa ` ` aaaaa
        content = content.replaceAll("`([\\s\\S]*)`", "");
        // aa@aaa
        String atRegex = "@[a-z0-9-_]+\\b?";
        List<String> atUsers = new ArrayList<>();
        Pattern regex = Pattern.compile(atRegex);
        Matcher regexMatcher = regex.matcher(content);
        while (regexMatcher.find()) {
            atUsers.add(regexMatcher.group());
        }
        return atUsers;
    }

    // aaaaaaaaaaaaa
    public static Set<String> removeEmpty(String[] strs) {
        if (strs == null || strs.length == 0) return Collections.emptySet();
        Set<String> set = new HashSet<>();
        for (String str : strs) {
            if (!StringUtils.isEmpty(str)) {
                set.add(str);
            }
        }
        return set;
    }

    /**
     * aaaaaaaunicodea
     *
     * @param string
     * @return
     * @author shuai.ding
     */
    public static String string2Unicode(String string) {
        if (StringUtils.isEmpty(string)) {
            return null;
        }
        char[] bytes = string.toCharArray();
        StringBuilder unicode = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            char c = bytes[i];
            // aaASCIIaaaaaa，aaaa
            if (c >= 0 && c <= 127) {
                unicode.append(c);
                continue;
            }
            String hexString = Integer.toHexString(bytes[i]);
            unicode.append("\\u");
            // aaaaaaa0aa
            if (hexString.length() < 4) {
                unicode.append("0000".substring(hexString.length(), 4));
            }
            unicode.append(hexString);
        }
        return unicode.toString();
    }

    /**
     * \s+aaaaaaaaa,aaaaaaaaaaa
     * \pP aaaaa p a property aaa，aa Unicode aa，aa Unicode aaaaaaa。
     * aa P aa Unicode aaaaaaaaaaa：aaaa。
     * aaaaa
     * L：aa；
     * M：aaaa（aaaaaaaa）；
     * Z：aaa（aaaa、aaa）；
     * S：aa（aaaaaa、aaaaa）；
     * N：aa（aaaaaaa、aaaaa）；
     * C：aaaa
     *
     * @param str
     * @return
     */
    public static String removeSpecialChar(String str) {
        String regEx = "\\pP|\\pS|\\s+";
        str = Pattern.compile(regEx).matcher(str).replaceAll("").trim();
        return str;
    }

}
