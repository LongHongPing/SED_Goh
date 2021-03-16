import utils.BloomFilter;
import utils.EncUtil;
import utils.FileUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/** 服务器端 */
public class SearchServer {
    /** 读取索引文件 */
    public static ArrayList<Boolean> readIndexFile(String path) throws Exception{
        ArrayList<Boolean> secureIndex = new ArrayList<>();
        String content = FileUtil.read(path,"UTF-8");
        List<String> words = FileUtil.getWords(content);
        for(String word : words){
            if(word != "0"){
                secureIndex.add(true);
            }else{
                secureIndex.add(false);
            }
        }
        return secureIndex;
    }
    /** 主测试函数 */
    public static void main(String[] args){
        ServerSocket serverSocket = null;
        Socket socket = null;
        String dirPath = "/encry";

        try{
            try{
                serverSocket = new ServerSocket(4700);
                socket = serverSocket.accept();
            }catch (Exception e){
                e.printStackTrace();
            }
            //socket输入流
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //socket输出流
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
            //标准输入
           // BufferedReader bufReader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Listening on port: " + serverSocket.getLocalPort());
            String keyword = bufferedReader.readLine();
            while(!keyword.equals("bye")){
                System.out.println("Server accept keyword: " + bufferedReader.readLine());
                ArrayList<byte[]> trapDoors = new ArrayList<>();
                File[] files = FileUtil.getFiles(dirPath);
                ArrayList<String> results = new ArrayList<>();
                System.out.println("Checked the following indexes: ");
                for(File file : files){
                    String fileName = file.getName();
                    ArrayList<Boolean> index = readIndexFile(dirPath + "/" + fileName);
                    BloomFilter bloomFilter = new BloomFilter(index);
                    ArrayList<byte[]> codeWords = EncUtil.buildTrapCode(fileName,trapDoors);
                    if(bloomFilter.check(codeWords)){
                        results.add(fileName);
                    }
                }
                if(!results.isEmpty()){
                    printWriter.print("Keyword matches found: ");
                    for(String res : results){
                        printWriter.print(", " + res);
                    }
                    printWriter.println(".");
                }else{
                    printWriter.println("No matches found.");
                }
            }
            bufferedReader.close();
            printWriter.close();
            socket.close();
            serverSocket.close();
        }catch (Exception exp){
            exp.printStackTrace();
        }
    }
}
