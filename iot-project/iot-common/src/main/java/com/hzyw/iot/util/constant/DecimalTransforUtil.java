package com.hzyw.iot.util.constant;

public class DecimalTransforUtil {
	public static byte CARD_NUM_BIT = 8;
	
	/**
     * isBlank 
     * 
     * @param value
     * @return true: blank; false: not blank
     */
    private static boolean isBlank(String value) {
        if (value == null || "".equals(value.trim())) {
            return true;
        }
        return false;
    }
 
    /**
     * 10进制转16进制，并补齐指定位数
     * @param str
     * @param bitNum 字节长度， 16进制一个字节2位
     * @return
     */
    public static String toHexStr(String str,Integer bitNum) {
    	bitNum=bitNum*2;
        String result = "";
        String regex = "^\\d{1,}$";
        if (!isBlank(str)) {
            str = str.trim();
            if (str.matches(regex)) {
                String hexStr = Long.toHexString(Long.parseLong(str.trim())).toUpperCase();
                if (hexStr.length() < bitNum) {
                    hexStr = org.apache.commons.lang3.StringUtils.leftPad(hexStr, bitNum, '0');
                }
                result = hexStr;
            } else if (isHex(str)) {
                if (str.length() < bitNum) {
                    str = org.apache.commons.lang3.StringUtils.leftPad(str, bitNum, '0');
                }
                result = str;
            }
        }
        return result;
    }
    
    
    /**
     * 判断是否是16进制数
     * 
     * @param strHex
     * @return
     */
    public static boolean isHex(String strHex) {
        int i = 0;
        if (strHex.length() > 2) {
            if (strHex.charAt(0) == '0' && (strHex.charAt(1) == 'X' || strHex.charAt(1) == 'x')) {
                i = 2;
            }
        }
        for (; i < strHex.length(); ++i) {
            char ch = strHex.charAt(i);
            if ((ch >= '0' && ch <= '9') || (ch >= 'A' && ch <= 'F') || (ch >= 'a' && ch <= 'f'))
                continue;
            return false;
        }
        return true;
    }
 
/**
     * 计算16进制对应的数值
     * 
     * @param ch
     * @return
     * @throws Exception
     */
    private static int getHex(char ch) throws Exception {
        if (ch >= '0' && ch <= '9')
            return (int) (ch - '0');
        if (ch >= 'a' && ch <= 'f')
            return (int) (ch - 'a' + 10);
        if (ch >= 'A' && ch <= 'F')
            return (int) (ch - 'A' + 10);
        throw new Exception("error param");
    }
 
/**
     * 计算幂
     * 
     * @param nValue
     * @param nCount
     * @return
     * @throws Exception
     */
    private static long getPower(int nValue, int nCount) throws Exception {
        if (nCount < 0)
            throw new Exception("nCount can't small than 1!");
        if (nCount == 0)
            return 1;
        long nSum = 1;
        for (int i = 0; i < nCount; ++i) {
            nSum = nSum * nValue;
        }
        return nSum;
    }
 
/**
     * 16进制转10进制，对16进制数的每一位数乘以其对应的16的幂，相加。
     * @param strHex 待转换的字符串
     * @param force 是否强制按16进制转换，纯数字也可能是16进制，true则将纯数字按16进制处理
     * @return
     */
    public static long hexToLong(String strHex, boolean force) {
        long nResult = 0;
        String regex = "^\\d{1,}$";
        if (!isBlank(strHex)) {
            strHex = strHex.trim();
        } else {
            return nResult;
        }
        if (!force && strHex.matches(regex)) {
            return Long.parseLong(strHex);
        }
        if (!isHex(strHex)) {
            return nResult;
        }
        String str = strHex.toUpperCase();
        if (str.length() > 2) {
            if (str.charAt(0) == '0' && str.charAt(1) == 'X') {
                str = str.substring(2);
            }
        }
        int nLen = str.length();
        for (int i = 0; i < nLen; ++i) {
            char ch = str.charAt(nLen - i - 1);
            try {
                nResult += (getHex(ch) * getPower(16, i));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return nResult;
    }
 
/**
     * 16进制转10进制
     * @param strHex
     * @return
     */
    public static long hexToLong(String strHex) {
        return hexToLong(strHex, false);
    }


    /**
     * 16进制转2进制
     * @param hex
     * @param bitNum
     * @return
     */
    public static String hexStringToByte(String hex,int bitNum) {
        int i = Integer.parseInt(hex, 16);
        String str2 = Integer.toBinaryString(i);
        if(bitNum>0){
            int lengthNum= bitNum - str2.length();
            for (int j = 0; j <lengthNum; j++) {
                str2 = "0".concat(str2);
            }
        }
        return str2;
    }
    /**
     * 16进制转2进制
     *
     * @param hex
     * @return
     */
    public static String hexStringToByte(String hex) {
        return hexStringToByte(hex,-1);
    }

    /**
     * 2进制转10进制
     *
     * @param bytes
     * @return
     */
    public static int ByteToDecimal(String bytes) {
        return Integer.valueOf(bytes, 2);
    }
    
    /**
     * 10进制转2进制
     * @param n
     * @return
     */
    public static String Demical2Byte(int n) {
        String result = Integer.toBinaryString(n);
        return result;
    }

}
