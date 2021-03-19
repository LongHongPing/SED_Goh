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
        String dirPath = "indexFiles/";
        String keyword = null;

        System.out.println("=== Server ===");
        try{
            try{
                serverSocket = new ServerSocket(4700);
                socket = serverSocket.accept();
            }catch (Exception e){
                e.printStackTrace();
            }
            System.out.println("Listening on port: " + serverSocket.getLocalPort());

            //socket输入流
            BufferedReader socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //socket输出流
            PrintWriter socketOut = new PrintWriter(socket.getOutputStream());

            while(keyword != "bye"){
                keyword = socketIn.readLine();
                System.out.println("Server accept keyword: " + keyword);
                //处理陷门
                String trapDoor = socketIn.readLine();
                System.out.println("Server accept trapdoor: " + trapDoor);
                ArrayList<byte[]> trapDoors = new ArrayList<>();
                File[] files = FileUtil.getFiles(dirPath);
                ArrayList<String> results = new ArrayList<>();

                //查找文件
                System.out.println("Checked the following indexes: ");
                for(File file : files){
                    String fileName = file.getName();
                    ArrayList<Boolean> index = readIndexFile(dirPath + fileName);
                    System.out.println("Search in " + fileName);
                    BloomFilter bloomFilter = new BloomFilter(index);
                    ArrayList<byte[]> codeWords = EncUtil.buildTrapCode(fileName,trapDoors);
                    if(bloomFilter.check(codeWords)){
                        results.add(fileName);
                    }
                }
                if(!results.isEmpty()){
                    socketOut.print("Keyword matches found: ");
                    for(String res : results){
                        socketOut.print(res + " ");
                    }
                }else{
                    socketOut.println("No matches found.");
                }
                socketOut.flush();
            }
            socketIn.close();
            socketOut.close();
            socket.close();
            serverSocket.close();
        }catch (Exception exp){
            exp.printStackTrace();
        }
    }
}
