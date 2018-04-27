package top.ornobug.music163.util;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UnicodeUtil {

    public static void main(String[] args) throws Exception {
        byte[] testbytes = {105, 111, 115, -30, -102, -67, 32, 36, -18, -128, -104, 32, 36, -16, -97, -113, -128, 32, 36, -18, -112, -86};
        String tmpstr = new String(testbytes, "utf-8");
//        System.out.println(URLEncoder.encode(tmpstr, "utf-8"));
        System.out.println(filterEmoji(tmpstr));
//

        System.out.println("containsEmoji2: " + containsEmoji("tetete11什么66789@#￥*（&*）*%"));
        System.out.println(containsEmoji(tmpstr));


    }


    /**
     * 检测是否有emoji字符
     *
     * @param source
     * @return 一旦含有就抛出
     */
    public static boolean containsEmoji(String source) {
        if (StringUtil.isEmpty(source)) {
            return false;
        }
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isNotEmojiCharacter(codePoint)) {
                //判断到了这里表明，确认有表情字符
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否为非Emoji字符
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isNotEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) ||
                (codePoint == 0x9) ||
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }

    /**
     * 过滤emoji 或者 其他非文字类型的字符
     *
     * @param source
     * @return
     */
    public static String filterEmoji(String source) {
        if (StringUtil.isEmpty(source)) {
            return source;
        }
        if (!containsEmoji(source)) {
            return source;//如果不包含，直接返回
        }
        StringBuilder buf = new StringBuilder();
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (isNotEmojiCharacter(codePoint)) {
                buf.append(codePoint);
            }
        }

        return buf.toString().trim();
    }

    /**
     * unicode转中文
     *
     * @param str
     * @return
     * @author yutao
     * @date 2017年1月24日上午10:33:25
     */
    public static String unicodeToString(String str) {

        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            str = str.replace(matcher.group(1), ch + "");
        }
        return str;
    }

}
