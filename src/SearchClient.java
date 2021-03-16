import utils.EncUtil;
import utils.FileUtil;
import utils.HexUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/** 客户端 */
public class SearchClient {
    /** 读取密钥 */
    public static ArrayList<byte[]> readKeys(String keyFile) throws Exception{
        ArrayList<byte[]> hashKeys = new ArrayList<>();
        File[] files = FileUtil.getFiles(keyFile);
        for(File file : files){
            List<String> words = FileUtil.getWords(file);
            for(String word : words){
                hashKeys.add((word.getBytes("UTF-8")));
            }
        }
        return hashKeys;
    }
    /** 主测试函数 */
    public static void main(String[] args){
        try{
            Socket socket = new Socket("127.0.0.1",4700);
            System.out.println("Enter an keyword: ");
            //标准输入
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(System.in));
            //socket输入
           // BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //socket输出
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
            String line = bufReader.readLine();
            while(!line.equals("bye")){
                //System.out.println("Client input word:" + bufferedReader.readLine());
                String keyFilePath = "/keyfiles";
                ArrayList<byte[]> hashKeys = readKeys(keyFilePath);
                ArrayList<byte[]> trapDoors = EncUtil.buildTrapCode(line,hashKeys);
                System.out.println("trans trapdoor to server... ");
                printWriter.println(trapDoors.toString());
                printWriter.flush();
                System.out.println("Enter an another keyword (key 'bye'to close): ");
                line = bufReader.readLine();
            }
            printWriter.close();
           // bufferedReader.close();
            socket.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

