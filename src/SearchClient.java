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
        String readLine = null;
        String keyFilePath = "keyfiles/";

        System.out.println("=== Client ===");
        try{
            Socket socket = new Socket("127.0.0.1",4700);
            //标准输入
            BufferedReader systemIn = new BufferedReader(new InputStreamReader(System.in));
            //socket输入
            BufferedReader socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //socket输出
            PrintWriter socketOut = new PrintWriter(socket.getOutputStream());

            while(readLine != "bye"){
                //System.out.println("Client input word:" + bufferedReader.readLine());
                //发送关键字
                System.out.println("Enter a keyword (type 'bye'to close): ");
                readLine = systemIn.readLine();
                socketOut.println(readLine);
                socketOut.flush();
                //发送陷门
                ArrayList<byte[]> hashKeys = readKeys(keyFilePath);
                ArrayList<byte[]> trapDoors = EncUtil.buildTrapCode(readLine,hashKeys);
                System.out.println("trans trapdoor to server... ");
                socketOut.println(trapDoors.toString());
                socketOut.flush();
                //接收结果
                String results = socketIn.readLine();
                System.out.println(results);
            }
            systemIn.close();
            socketIn.close();
            socketOut.close();
            socket.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

