package org.hpar;

import junit.framework.TestCase;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.File;
import java.io.IOException;

/**
 * for all tests
 * Created by sxf on 4/6/16.
 */
public class AllTest extends TestCase {

    int k = 0;
    int p = 0;
    int all = 0;
    public void testAll() {
        k = 0;
        loadAllFiles("src/test/extern/benchmarks");
        System.out.println("正确率： " + k*100/all + "%");
        System.out.println("崩溃率： " + (all-p)*100/all + "%");
    }

    public void loadAllFiles(String filePath) {
        File f = new File(filePath);
        File[] files = f.listFiles(); // 得到f文件夹下面的所有文件。
        assert files != null;
        all = files.length;
        for(File file : files) {
            System.out.println(file.getAbsolutePath());
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
            ParallelJsoup pp = new ParallelJsoup(data);
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

        ParallelJsoup pj = new ParallelJsoup(data, 8);
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

        assertNotNull(document);
        System.out.println("ParallelCost: " + time / 1000000 + "ms");
        System.out.println("NormalCost: " + time_n / 1000000 + "ms");
        System.out.println("SpeedUp: " + time_n/time);
        pj.worker.printThreadSummraize();
        assertTrue(d.hasSameValue(document));
    }
}
