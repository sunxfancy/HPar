package org.hpar;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.File;
import java.io.IOException;

/**
 * for all tests
 * Created by sxf on 4/6/16.
 */
public class AllTestRunner{

    int k = 0;
    int p = 0;
    int all = 0;
    public static void main(String... args) {
        AllTestRunner test = new AllTestRunner();
        test.k = 0;
        test.loadAllFiles(args[0]);
        System.out.println("正确率： " + test.k*100/test.all + "%");
    }

    public void loadAllFiles(String filePath) {
        File f = new File(filePath);
        File[] files = f.listFiles(); // 得到f文件夹下面的所有文件。
        assert files != null;
        all = files.length;
        for(File file : files) {
            try {
                LoadOne(file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void loadOneFile(String path) throws IOException {
        String data = App.readFile(path);

        long begin = System.nanoTime();
        Document dd = Parser.parse(data, "");
        long end = System.nanoTime();
        System.out.println("normal time: " + (end - begin) / 1000000 + "ms");

        Document d = null;
        try {
            ParallelJsoup pp = new ParallelJsoup(data.toCharArray());
            d = pp.parse();
        } catch (Exception e1) {
            e1.printStackTrace();
            throw e1;
        }
        p++;
        if (!dd.hasSameValue(d)) {
            System.out.println("出现异常！");
        } else {
            k++;
        }
    }


    private void LoadOne(String path) throws Exception {
        String data = App.readFile(path);

        ParallelJsoup pj = new ParallelJsoup(data.toCharArray(), 8);
        Document document = pj.parse();
        Document d = Parser.parse(data, "");
        double time = 0, time_n = 0;

        long b, e;
        for (int i = 0; i < 100; i++) {
//        while (true) {
            b = System.nanoTime();
            document = pj.parse();
            e = System.nanoTime();
            time += e - b;
            b = System.nanoTime();
            d = Parser.parse(data, "");
            e = System.nanoTime();
            time_n += e - b;
        }

        System.out.println(path);
        System.out.println("ParallelCost: " + time / 1000000 + "ms");
        System.out.println("NormalCost: " + time_n / 1000000 + "ms");
        System.out.println("SpeedUp: " + time_n/time);
        System.out.println();

        pj.worker.printThreadSummraize();
        pj.closeAll();
        if (d.hasSameValue(document)) {
            k++;
        }
    }
}
