package utils;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.ArrayList;
/** 加密工具类 */
public class EncUtil {
    public static EncUtil encUtil;
    public static ArrayList<byte[]> trapDoors;
    public static ArrayList<byte[]> codeWords;
    public static BloomFilter index = new BloomFilter();

    /** 懒汉式单例 双重锁*/
    private EncUtil(){

    }
    public static EncUtil getInstance(){
        if(encUtil == null){
            synchronized (EncUtil.class){
                if(encUtil == null){
                    encUtil = new EncUtil();
                }
            }
        }
        return encUtil;
    }
    /** 生成流加密 */
    public static byte[] genStreamCipher(String key,String NONCE,String counter,String target){
        try {
            Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE,new SecretKeySpec(key.getBytes(),"AES"),new IvParameterSpec(HexUtil.hexToByte(NONCE+counter)));
            return cipher.doFinal(target.getBytes("UTF-8"));
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    /** 加密string类型词语 */
    public static byte[] aesEncrypt(String key,String target,String iv){
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE,new SecretKeySpec(key.getBytes(),"AES"),new IvParameterSpec(iv.getBytes("UTF-8")));
            return cipher.doFinal(target.getBytes("UTF-8"));
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    /** 加密byte类型词语 */
    public static byte[] aesEncrypt(String key,byte[] target,String iv){
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE,new SecretKeySpec(key.getBytes(),"AES"),new IvParameterSpec(iv.getBytes("UTF-8")));
            return cipher.doFinal(target);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    /** 生成随机字节数组 */
    public static byte[] genRandomBytes(int size){
        byte[] salt = new byte[size];
        SecureRandom random = new SecureRandom();
        random.setSeed(0);
        random.nextBytes(salt);
        return salt;
    }
    /** 生成密钥 */
    public static ArrayList<byte[]> genHashKeys(double fp){
        double kHash = Math.round(Math.abs(-(Math.log(fp))));
        ArrayList<byte[]> keys = new ArrayList<>();
        for(int i = 0; i <= (int)kHash;i++){
            byte[] key = genRandomBytes(16);
            //keys = utils.HexUtil.append(keys,key);
            keys.add(key);
        }
        return keys;
    }

    /** 创建HMAC */
    public static byte[] createHmac(String data,byte[] key) throws Exception{
        Mac Hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key, "HmacSHA256");
        Hmac.init(secret_key);
        return Hmac.doFinal(data.getBytes("UTF-8"));
    }
    /** 创建陷门、码字 */
    public static ArrayList<byte[]> buildTrapCode(String keyword,ArrayList<byte[]> keys)throws Exception{
        ArrayList<byte[]> trapCodes = new ArrayList<>();
        for(byte[] key : keys){
            byte[] trapCode = createHmac(keyword,key);
           // trapCodes = utils.HexUtil.append(trapCodes,trapCode);
            trapCodes.add(trapCode);
        }
        return trapCodes;
    }

    /** 建立 */
    public static void build(String fileName,String keyword,ArrayList<byte[]> keys)throws Exception{
        trapDoors = buildTrapCode(keyword,keys);
        codeWords = buildTrapCode(fileName,trapDoors);
    }
    /** 安全索引盲化 */
    public static void blind(int numKeywords,int docSize,int numKeys){
        int blindFactor = (docSize - numKeywords) * numKeys;
        ArrayList<byte[]> blinding = new ArrayList<>();
        //ArrayList<ArrayList<Byte>> blinding = new byte[cnt][];
        byte[] randomBytes = genRandomBytes(blindFactor);
      //  blinding = utils.HexUtil.append(blinding,randomBytes);
        blinding.add(randomBytes);
        index.add(blinding);
    }
}
