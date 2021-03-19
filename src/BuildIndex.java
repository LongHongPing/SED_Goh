import utils.BloomFilter;
import utils.EncUtil;
import utils.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/** 索引建立类 */
public class BuildIndex {
    //常量定义
  //  private static Double SCALING_FACTOR = 1.5;
    private static Double PROBABILITY_FALSE = 0.01;
  //  private static String NONCE = "a49eca32";
  //  private static String STREAM_CIPHER_KEY = "abcdefghijklmnop";
    private static String ENCRYPTION_KEY = "ponmlkjihgfedcba";
    private static String PLAINTEXT = "This is a testes";

    /** 处理关键词 */
    public static String optWord(String word){
        String str = word;
        for (int i = 0; i < 32 - word.length();i++){
            str = str + ".";
        }
        return str;
    }

    public static void main(String[] args) throws Exception{
        String dirPath = "plainFiles/";
        String keyFilePath = "keyFiles/";
        String encryPath = "encryFiles/";
        String indexPath = "indexFiles/";

        if(!new File(keyFilePath).exists()){
            FileUtil.deleteFiles(encryPath);
            FileUtil.deleteFiles(keyFilePath);
            FileUtil.deleteFiles(indexPath);
        }

        //生成密钥文件
        ArrayList<byte[]> hashKeys = EncUtil.genHashKeys(PROBABILITY_FALSE);
        FileUtil.write(hashKeys.toString(),keyFilePath + "keyfile.txt","UTF-8");
        //循环建立文件索引
        File[] files = FileUtil.getFiles(dirPath);
        int count = 1;
        for(File file : files){
            String fileName = file.getName();
            System.out.println("Building index for " + fileName);
            List<String> words = FileUtil.getWords(file);
            System.out.println("Words: " + words);
            int rawSize = words.size();
            //词语去重
            HashSet hashSet = new HashSet(words);
            words.clear();
            words.addAll(hashSet);
            System.out.println("Clear words: " + words);
            //创建陷门、码字
            //BloomFilter bloomFilter = new BloomFilter();
            for(String keyWord : words){
                EncUtil.build(fileName,keyWord,hashKeys);
                EncUtil.index.add(EncUtil.codeWords);
            }
            //执行盲化
            EncUtil.blind(words.size(),rawSize,hashKeys.size());
            //写出文件,加密
            FileUtil.write(EncUtil.index.bits.toString(),indexPath + "indexfile" + count + ".txt","UTF-8");
            for(String word : words){
                byte[] wordByte = EncUtil.aesEncrypt(ENCRYPTION_KEY,optWord(word),PLAINTEXT);
                FileUtil.write(wordByte.toString(),encryPath + fileName,"UTF-8");
            }
            count++;
        }
        System.out.println("secure index builds complete.");
    }
}
