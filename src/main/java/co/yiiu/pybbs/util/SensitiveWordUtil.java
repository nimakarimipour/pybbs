package co.yiiu.pybbs.util;

import com.aliyuncs.utils.StringUtils;

import java.util.*;

/**
 * aaaaaaa - DFAaaaa
 * <p>
 * aaaa：https://www.jianshu.com/p/2e84eacc3cc8
 *
 * @author sam
 * @since 2017/9/4
 * <p>
 * aaaaaaaaaaa，aaaaaaaaa，aaaaa，aaaaaaaaaaaaaaa，aaaaaa，aaaaaa :)
 */
public class SensitiveWordUtil {

    /**
     * aaaaaaa
     */
    public static final int MinMatchType = 1;      //aaaaaa，a：aaaa["aa","aaa"]，aa："aaaaa"，aaaa：aa[aa]a
    public static final int MaxMatchType = 2;      //aaaaaa，a：aaaa["aa","aaa"]，aa："aaaaa"，aaaa：aa[aaa]

    /**
     * aaaaa
     */
    public static HashMap sensitiveWordMap;

    /**
     * aaaaaaa，aaDFAaaaa
     *
     * @param sensitiveWordSet aaaa
     */
    public static synchronized void init(Set<String> sensitiveWordSet) {
        initSensitiveWordMap(sensitiveWordSet);
    }

    /**
     * aaaaaaa，aaDFAaaaa
     *
     * @param sensitiveWordSet aaaa
     */
    private static void initSensitiveWordMap(Set<String> sensitiveWordSet) {
        //aaaaaaaa，aaaaaa
        sensitiveWordMap = new HashMap(sensitiveWordSet.size());
        String key;
        Map nowMap;
        Map<String, String> newWorMap;
        //aasensitiveWordSet
        Iterator<String> iterator = sensitiveWordSet.iterator();
        while (iterator.hasNext()) {
            //aaa
            key = iterator.next();
            nowMap = sensitiveWordMap;
            for (int i = 0; i < key.length(); i++) {
                //aaachara
                char keyChar = key.charAt(i);
                //aaaaaaa
                Object wordMap = nowMap.get(keyChar);
                //aaaaakey，aaaa，aaaaaaaaa
                if (wordMap != null) {
                    nowMap = (Map) wordMap;
                } else {
                    //aaaa，aaaaamap，aaaisEndaaa0，aaaaaaaaa
                    newWorMap = new HashMap<>();
                    //aaaaaa
                    newWorMap.put("isEnd", "0");
                    nowMap.put(keyChar, newWorMap);
                    nowMap = newWorMap;
                }

                if (i == key.length() - 1) {
                    //aaaa
                    nowMap.put("isEnd", "1");
                }
            }
        }
    }

    /**
     * aaaaaaaaaaaa
     *
     * @param txt       aa
     * @param matchType aaaa 1：aaaaaa，2：aaaaaa
     * @return aaaaatrue，aaaafalse
     */
    public static boolean contains(String txt, int matchType) {
        boolean flag = false;
        for (int i = 0; i < txt.length(); i++) {
            int matchFlag = checkSensitiveWord(txt, i, matchType); //aaaaaaaaaa
            if (matchFlag > 0) {    //aa0aa，aatrue
                flag = true;
            }
        }
        return flag;
    }

    /**
     * aaaaaaaaaaaa
     *
     * @param txt aa
     * @return aaaaatrue，aaaafalse
     */
    public static boolean contains(String txt) {
        return contains(txt, MaxMatchType);
    }

    /**
     * aaaaaaaaa
     *
     * @param txt       aa
     * @param matchType aaaa 1：aaaaaa，2：aaaaaa
     * @return
     */
    public static Set<String> getSensitiveWord(String txt, int matchType) {
        Set<String> sensitiveWordList = new HashSet<>();

        for (int i = 0; i < txt.length(); i++) {
            //aaaaaaaaaa
            int length = checkSensitiveWord(txt, i, matchType);
            if (length > 0) {//aa,aalista
                sensitiveWordList.add(txt.substring(i, i + length));
                i = i + length - 1;//a1aaa，aaaforaaa
            }
        }

        return sensitiveWordList;
    }

    /**
     * aaaaaaaaa
     *
     * @param txt aa
     * @return
     */
    public static Set<String> getSensitiveWord(String txt) {
        return getSensitiveWord(txt, MaxMatchType);
    }

    /**
     * aaaaaaa
     *
     * @param txt         aa
     * @param replaceChar aaaaa，aaaaaaaaaaaaa，a aa：aaaaa aaa：aaa，aaaa：*， aaaa：aa***
     * @param matchType   aaaaaaa
     * @return
     */
    public static String replaceSensitiveWord(String txt, char replaceChar, int matchType) {
        String resultTxt = txt;
        //aaaaaaaa
        Set<String> set = getSensitiveWord(txt, matchType);
        Iterator<String> iterator = set.iterator();
        String word;
        String replaceString;
        while (iterator.hasNext()) {
            word = iterator.next();
            replaceString = getReplaceChars(replaceChar, word.length());
            resultTxt = resultTxt.replaceAll(word, replaceString);
        }

        return resultTxt;
    }

    /**
     * aaaaaaa
     *
     * @param txt         aa
     * @param replaceChar aaaaa，aaaaaaaaaaaaa，a aa：aaaaa aaa：aaa，aaaa：*， aaaa：aa***
     * @return
     */
    public static String replaceSensitiveWord(String txt, char replaceChar) {
        return replaceSensitiveWord(txt, replaceChar, MaxMatchType);
    }

    /**
     * aaaaaaa
     *
     * @param txt        aa
     * @param replaceStr aaaaaa，aaaaaaaaaaaaa，a aa：aaaaa aaa：aaa，aaaaa：[aa]，aaaa：aa[aa]
     * @param matchType  aaaaaaa
     * @return
     */
    public static String replaceSensitiveWord(String txt, String replaceStr, int matchType) {
        if (StringUtils.isEmpty(txt)) return null;
        String resultTxt = txt;
        //aaaaaaaa
        Set<String> set = getSensitiveWord(txt, matchType);
        Iterator<String> iterator = set.iterator();
        String word;
        while (iterator.hasNext()) {
            word = iterator.next();
            resultTxt = resultTxt.replaceAll(word, replaceStr);
        }

        return resultTxt;
    }

    /**
     * aaaaaaa
     *
     * @param txt        aa
     * @param replaceStr aaaaaa，aaaaaaaaaaaaa，a aa：aaaaa aaa：aaa，aaaaa：[aa]，aaaa：aa[aa]
     * @return
     */
    public static String replaceSensitiveWord(String txt, String replaceStr) {
        return replaceSensitiveWord(txt, replaceStr, MaxMatchType);
    }

    /**
     * aaaaaaa
     *
     * @param replaceChar
     * @param length
     * @return
     */
    private static String getReplaceChars(char replaceChar, int length) {
        String resultReplace = String.valueOf(replaceChar);
        for (int i = 1; i < length; i++) {
            resultReplace += replaceChar;
        }

        return resultReplace;
    }

    /**
     * aaaaaaaaaaaaa，aaaaaa：<br>
     *
     * @param txt
     * @param beginIndex
     * @param matchType
     * @return aaaa，aaaaaaaaaaa，aaaaa0
     */
    private static int checkSensitiveWord(String txt, int beginIndex, int matchType) {
        //aaaaaaaa：aaaaaaa1aaaa
        boolean flag = false;
        //aaaaaaaa0
        int matchFlag = 0;
        char word;
        Map nowMap = sensitiveWordMap;
        for (int i = beginIndex; i < txt.length(); i++) {
            word = txt.charAt(i);
            //aaaakey
            nowMap = (Map) nowMap.get(word);
            if (nowMap != null) {//aa，aaaaaaaaaa
                //aaaakey，aaaa+1
                matchFlag++;
                //aaaaaaaaaaa,aaaa，aaaaaaa
                if ("1".equals(nowMap.get("isEnd"))) {
                    //aaaaaatrue
                    flag = true;
                    //aaaa，aaaa,aaaaaaaaaa
                    if (MinMatchType == matchType) {
                        break;
                    }
                }
            } else {//aaa，aaaa
                break;
            }
        }
        if (matchFlag < 2 || !flag) {//aaaaaaaa1，aa
            matchFlag = 0;
        }
        return matchFlag;
    }

    //    public static void main(String[] args) {
    //
    //        Set<String> sensitiveWordSet = new HashSet<>();
    //        sensitiveWordSet.add("aaa");
    //        sensitiveWordSet.add("aa");
    //        sensitiveWordSet.add("aa");
    //        sensitiveWordSet.add("aa");
    //        sensitiveWordSet.add("aa");
    //        sensitiveWordSet.add("aa");
    //        sensitiveWordSet.add("aa");
    //        sensitiveWordSet.add("aaa");
    //        sensitiveWordSet.add("aaa");
    //        //aaaaaaa
    //        SensitiveWordUtil.init(sensitiveWordSet);
    //
    //        System.out.println("aaaaaa：" + SensitiveWordUtil.sensitiveWordMap.size());
    //        String string = "aaaaaa，aaaaaaaaaaaaaaaaa aaaaaa。"
    //                + "aaaaaaaaaaaaaaaaaaaaaaaa aaaaaaaaaaaaaaaaaaaaaaaa，aaaaaaa，"
    //                + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa aaaaaa，aaaaaaaaaa。";
    //        System.out.println("aaaaaaa：" + string.length());
    //
    //        //aaaaaaa
    //        boolean result = SensitiveWordUtil.contains(string);
    //        System.out.println(result);
    //        result = SensitiveWordUtil.contains(string, SensitiveWordUtil.MinMatchType);
    //        System.out.println(result);
    //
    //        //aaaaaaaaa
    //        Set<String> set = SensitiveWordUtil.getSensitiveWord(string);
    //        System.out.println("aaaaaaaaaaaa：" + set.size() + "。aa：" + set);
    //        set = SensitiveWordUtil.getSensitiveWord(string, SensitiveWordUtil.MinMatchType);
    //        System.out.println("aaaaaaaaaaaa：" + set.size() + "。aa：" + set);
    //
    //        //aaaaaaaaa
    //        String filterStr = SensitiveWordUtil.replaceSensitiveWord(string, '*');
    //        System.out.println(filterStr);
    //        filterStr = SensitiveWordUtil.replaceSensitiveWord(string, '*', SensitiveWordUtil.MinMatchType);
    //        System.out.println(filterStr);
    //
    //        String filterStr2 = SensitiveWordUtil.replaceSensitiveWord(string, "[*aaa*]");
    //        System.out.println(filterStr2);
    //        filterStr2 = SensitiveWordUtil.replaceSensitiveWord(string, "[*aaa*]", SensitiveWordUtil.MinMatchType);
    //        System.out.println(filterStr2);
    //    }

}
