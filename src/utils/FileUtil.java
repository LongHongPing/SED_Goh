package utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 文件处理工具类 */
public class FileUtil {
    /** 从文件中读取 */
    public static String read(String path, String charset) throws IOException {
        File file = new File(path);
        FileInputStream fileInputStream = new FileInputStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream,charset);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        String content = "";
        String temp;
        while((temp = reader.readLine()) != null) {
            content += temp;
        }
        return content;
    }
    /** 向文件写入结果 */
    public static void write(String content,String path, String charset) throws IOException {
        File file = new File(path);
        if(!file.exists()){
            file.createNewFile();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(file,true);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, charset);
        outputStreamWriter.append(content);
        outputStreamWriter.close();
    }
    /** 获取路径下所有文件 */
    public static File[] getFiles(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        return files;
    }
    /** 获取明文中的单个词语 */
    public static List<String> getWords(File file) throws Exception{
        String filePath = file.getPath();
        String content = read(filePath,"UTF-8");
        return getWords(content);
    }
    public static List<String> getWords(String content) throws Exception{
        String pattern = "(\\w[\\w']*\\w|\\w)";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(content);
        List<String> words = new ArrayList<>();
        while (matcher.find()){
            words.add(matcher.group().toLowerCase());
        }
        return words;
    }
    /** 获取密文中的单个词语 */
    public static String[] getCipherWords(String content) {
        int count = content.length() / 64;
        String[] encWords = new String[count];
        for(int i = 0; i < count; i++) {
            String temp = "";
            for(int j = i * 64; j < i * 64 + 64; j++) {
                temp += content.charAt(j);
            }
            encWords[i] = temp;
        }
        return encWords;
    }
    /** 删除文件 */
    public static void deleteFiles(String dir){
        File[] files = getFiles(dir);
        for (File file : files) {
            file.delete();
        }
    }
}
