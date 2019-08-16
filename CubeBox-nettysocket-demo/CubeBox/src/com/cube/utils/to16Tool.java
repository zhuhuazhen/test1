package com.cube.utils;

 


 



import java.io.BufferedInputStream;  
import java.io.BufferedReader;  
import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileOutputStream;  
import java.io.FileWriter;  
import java.io.IOException;  
import java.io.InputStream;  
import java.io.InputStreamReader;  
import java.io.PrintWriter;  

/** 
 * 图片转成十六进制 
 * @author Administrator 
 * 
 */  
public class to16Tool {  
    public static void main(String[] args) throws Exception {  
    try{   
        StringBuffer sb = new StringBuffer();   
        FileInputStream fis = new FileInputStream("F:/dd.amr");   
        BufferedInputStream bis = new BufferedInputStream(fis);   
        java.io.ByteArrayOutputStream bos=new java.io.ByteArrayOutputStream();  

        byte[] buff=new byte[1024];  
        int len=0;  
        while((len=fis.read(buff))!=-1){  
            bos.write(buff,0,len);  
        }  
        //得到图片的字节数组  
        byte[] result=bos.toByteArray();  
        System.out.println("=十六进制111==="+result.toString());  
        System.out.println("=十六进制222==="+byte2HexStr(result));  
        //字节数组转成十六进制  
        String str=byte2HexStr(result);  
        System.out.println("=str==="+str);  
        /* 
         * 将十六进制串保存到txt文件中 
         */  
        PrintWriter   pw   =   new   PrintWriter(new   FileWriter("f://today.txt"));     
           pw.println(str);     
           pw.close();  
        }catch(IOException e){   
        }   

}  
    /* 
     *  实现字节数组向十六进制的转换方法一 
     */  
    public static String byte2HexStr(byte[] b) {  
        String hs="";  
        String stmp="";  
        for (int n=0;n<b.length;n++) {  
            stmp=(Integer.toHexString(b[n] & 0XFF));  
            if (stmp.length()==1) hs=hs+"0"+stmp;  
            else hs=hs+stmp;  
        }  
        return hs.toUpperCase();  
    }  

    private static byte uniteBytes(String src0, String src1) {  
        byte b0 = Byte.decode("0x" + src0).byteValue();  
        b0 = (byte) (b0 << 4);  
        byte b1 = Byte.decode("0x" + src1).byteValue();  
        byte ret = (byte) (b0 | b1);  
        return ret;  
    }  
    /* 
     *实现字节数组向十六进制的转换的方法二 
     */  
    public static String bytesToHexString(byte[] src){  

        StringBuilder stringBuilder = new StringBuilder("");  
        if (src == null || src.length <= 0) {  
            return null;  
        }  
        for (int i = 0; i < src.length; i++) {  
            int v = src[i] & 0xFF;  
            String hv = Integer.toHexString(v);  
            if (hv.length() < 2) {  
                stringBuilder.append(0);  
            }  
            stringBuilder.append(hv);  
        }  
        return stringBuilder.toString();  

    }  

}