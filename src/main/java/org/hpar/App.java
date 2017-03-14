package org.hpar;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.IOException;
import java.io.*;
/**
 * The App class
 * Used for console working
 */
public class App
{
    public static void main(String[] args) {
        String data = "";
        try {
            data = readFile(args[0]);
        } catch(IOException e) {
            e.printStackTrace();
        }

        ParallelJsoup pp = new ParallelJsoup(data.toCharArray());
        Document dd = pp.parse();
	    System.out.println(dd);
    }

    public static String readFile(String fileName) throws IOException {
        StringBuffer buffer = new StringBuffer();
        InputStream is = new FileInputStream(fileName);
        String line; // 用来保存每行读取的内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        line = reader.readLine(); // 读取第一行
        while (line != null) { // 如果 line 为空说明读完了
            buffer.append(line); // 将读到的内容添加到 buffer 中
            buffer.append("\n"); // 添加换行符
            line = reader.readLine(); // 读取下一行
        }
        reader.close();
        is.close();
        return buffer.toString();
    }
}
