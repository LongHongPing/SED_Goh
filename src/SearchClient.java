import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/** 客户端 */
public class SearchClient {
    /** 读取关键字 */
    public static ArrayList<ArrayList<Byte>> readKeys(String keyFile) throws Exception{
        ArrayList<ArrayList<Byte>> hashKeys = new ArrayList<>();
        File[] files = FileUtil.getFiles(keyFile);
        for(File file : files){
            List<String> words = FileUtil.getWords(file);
            for(String word : words){
                hashKeys.add(HexUtil.byteToArray((word.getBytes("UTF-8"))));
            }
        }
        return hashKeys;
    }
    /** 主测试函数 */
    public static void main(String[] args){
        try{
            Socket socket = new Socket("127.0.0.1",4700);
            //标准输入
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(System.in));
            //socket输入
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //socket输出
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
            String line = bufReader.readLine();
            while(!line.equals("bye")){
                printWriter.println(line);
                printWriter.flush();
                System.out.println("Client:" + bufferedReader.readLine());
                Scanner scanner = new Scanner(System.in);
                System.out.println("Enter keys file path: ");
                String keyFilePath = scanner.nextLine();
                ArrayList<ArrayList<Byte>> hashKeys = readKeys(keyFilePath);
                ArrayList<ArrayList<Byte>> trapDoors = EncUtil.buildTrapCode(line,hashKeys);
                printWriter.println(trapDoors.toString());
                printWriter.flush();
                System.out.println("Enter an another keyword: ");
                line = bufReader.readLine();
            }
            printWriter.close();
            bufferedReader.close();
            socket.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

