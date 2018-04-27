package top.ornobug.music163.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {


    /**
     * 是否空字符串
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {

        if (null == str || "".equals(str) || "null".equals(str)
                || "undefined".equals(str) || "N/A".equals(str) || str.length() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 获取非空字符串
     *
     * @param str
     * @param expect
     * @return
     */
    public static String getSafeVal(String str, String expect) {
        if (isEmpty(str)) {
            return expect;
        }
        return str;
    }

    /*
    * 判断是否为整数
    * @param str 传入的字符串
    * @return 是整数返回true,否则返回false
    */
    public static boolean isInteger(String str) {
        try {
            Integer.valueOf(str);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    /*
    * 判断是否为浮点数，包括double和float
    * @param str 传入的字符串
    * @return 是浮点数返回true,否则返回false
    */
    public static boolean isDouble(String str) {
        try {
            Double.valueOf(str);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static boolean isNum(String str) {
        return isDouble(str) || isInteger(str);
    }

    // java 判断字符串里是否包含中文字符
    public static boolean isContainChinese( String str ) {
        Pattern p = Pattern.compile( "[\u4e00-\u9fa5]" );
        Matcher m = p.matcher(str);

        if ( m.find() ) {
            return true;
        }

        return false;
    }


    public static void main(String[] args) {
        System.out.println(isNum("3.33"));
    }

}

