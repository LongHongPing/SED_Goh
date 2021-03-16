package utils;

import java.util.ArrayList;

/** 数据处理工具类 */
public class HexUtil {
    /** 长度调整 */
    public static String lengFill(int n,int count, String str){
        String counter = Integer.toHexString(count);
        int len = counter.length();
        for(int i = 0;i < n-len;i++){
            counter = str + counter;
        }
        return counter;
    }
    /** 将十六进制数据转换为二进制 */
    public static byte[] hexToByte(String hex){
        if (hex == null || hex.trim().equals("")){
            return new byte[0];
        }
        byte[] bytes = new byte[hex.length()/2];
        for(int i = 0; i < hex.length() / 2; i++) {
            String str = hex.substring(i * 2, (i + 1) * 2);
            bytes[i] = (byte) Integer.parseInt(str, 16);
        }
        return bytes;
    }
    /** 将二进制转换为十六进制数据 */
    public static String byteToHex(byte[] byteArray) {
        if (byteArray == null){
            return null;
        }
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[byteArray.length*2];
        for (int i = 0; i < byteArray.length; i++) {
            int val = byteArray[i] & 0xFF;
            hexChars[i*2] = hexArray[val >>> 4];
            hexChars[i*2+1] = hexArray[val & 0x0F];
        }
        return new String(hexChars);
    }
    /** 将字符类型转换为整型 */
    public static int charToInt(char a){
        if(a >= '0' && a <= '9'){
            return a - '0';
        }else if(a >= 'a' && a <= 'z' ){
            return a - 'a' + 10;
        }
        return -1;
    }
    /** 将整型转换为字符类型 */
    public static int intToChar(int a) {
        if(a >= 0 && a <= 9){
            return a+'0';
        }else if(a >= 10 && a <= 15){
            return a-10+'a';
        }
        return -1;
    }
    /** 异或运算 */
    public static char[] XOR(String e,String t){
        e = e.toLowerCase();
        t = t.toLowerCase();
        int len;
        if((len = e.length()) != t.length()){
            return null;
        }
        char[] eChar = e.toCharArray();
        char[] tChar = t.toCharArray();
        char[] result = new char[len];
        for (int i = 0; i < len; i++) {
            int eInt = charToInt(eChar[i]);
            int tInt = charToInt(tChar[i]);
            int ci = eInt ^ tInt;
            result[i] = (char)intToChar(ci);
        }
        return result;
    }
    /** 将arraylist换为byte */
    public static byte[] arrayToByte(ArrayList<Byte> arrayList){
        int len = arrayList.size();
        byte[] bts = new byte[len];
        int cnt = 0;
        for (byte bt : arrayList){
            bts[cnt++] = bt;
        }
        return bts;
    }
    /** 将byte换为arraylist */
    public static ArrayList<Byte> byteToArray(byte[] bts){
        ArrayList<Byte> arrayList = new ArrayList<>();
        for(byte bt : bts){
            arrayList.add(bt);
        }
        return arrayList;
    }
}
