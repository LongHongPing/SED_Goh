import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/** 索引建立类 */
public class BuildIndex {
    //常量定义
    private static Double SCALING_FACTOR = 1.5;
    private static Double PROBABILITY_FALSE = 0.01;
    private static String NONCE = "a49eca32";
    private static String STREAM_CIPHER_KEY = "abcdefghijklmnop";
    private static String ENCRYPTION_KEY = "ponmlkjihgfedcba";
    private static String PLAINTEXT = "This is a testes";

    public static void buildIndex() throws Exception{
        String dirPath = "/plain";
        String keyFilePath = "/keyfiles";
        ArrayList<ArrayList<Byte>> hashKeys = new ArrayList<>();

        FileUtil.deleteFiles(keyFilePath);
        hashKeys = EncUtil.genHashKeys(PROBABILITY_FALSE);
        FileUtil.write(hashKeys.toString(),keyFilePath,"UTF-8");
        //循环建立文件索引
        File[] files = FileUtil.getFiles(dirPath);
        for(File file : files){
            String fileName = file.getName();
            List<String> words = FileUtil.getWords(file);
            //去重
            HashSet hashSet = new HashSet(words);
            words.clear();
            words.addAll(hashSet);
            //创建陷门、码字
            BloomFilter bloomFilter = new BloomFilter();
            for(String keyWord : words){
                EncUtil.build(fileName,keyWord,hashKeys);
                EncUtil.index.add(EncUtil.codeWords);
            }
            //执行盲化
            EncUtil.blind(words.size(),file.length(),hashKeys.size());
            //写出文件,加密
            FileUtil.write(EncUtil.index.bits.toString(),keyFilePath,"UTF-8");
            for(String word : words){
                byte[] wordByte = EncUtil.aesEncrypt(ENCRYPTION_KEY,word,PLAINTEXT);
                FileUtil.write(wordByte.toString(),"encry/" + fileName,"UTF-8");
            }
        }
        System.out.println("secure index builds complete.");
    }
}
