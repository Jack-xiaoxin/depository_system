package com.example.depository_system.util;

public class NumberUtil {

    public static String DoubleToString(double num) {
        //保留num后的最多两位
        String str = String.format("%.2f", num);
        //去除str末尾的0
        str = str.replaceAll("0+?$", "").replaceAll("[.]$", "");
        //去除末尾多余的小数点
        if (str.indexOf(".") == -1) {
            return str;
        } else {
            return str.replaceAll("[.]$", "");
        }
    }
}
