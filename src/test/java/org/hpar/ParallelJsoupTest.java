package org.hpar;

import junit.framework.TestCase;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.FileWriter;

/**
 *
 * Created by sxf on 5/11/16.
 */
public class ParallelJsoupTest extends TestCase {
    public void testOne() throws Exception {
        String data = App.readFile("src/test/extern/websites/1.html");

        ParallelJsoup pj = new ParallelJsoup(data.toCharArray());
        Document document = pj.parse();
        Document d = Parser.parse(data, "");
        FileWriter writer=new FileWriter("src/test/extern/d1.html");
        writer.write(document.toString());
        writer.close();
        FileWriter writer2=new FileWriter("src/test/extern/d2.html");
        writer2.write(d.toString());
        writer2.close();
        pj.worker.printThreadSummraize();
        assertTrue(d.hasSameValue(document));
    }

    public void testPerformance() throws Exception {
        String data = App.readFile("src/test/extern/files/10.html");
        for (int t = 1; t <= 16; ++t) {
            System.out.println("\n线程数"+t);
            ParallelJsoup pj = new ParallelJsoup(data.toCharArray(), t);
            Document document = pj.parse();
            Document d = Parser.parse(data, "");
            double time = 0, time_n = 0;

            long b, e;
            for (int i = 0; i < 10; i++) {
                b = System.nanoTime();
                document = pj.parse();
                e = System.nanoTime();
                time += e - b;
                d = Parser.parse(data, "");
                b = System.nanoTime();
                time_n += b - e;
            }

            assertNotNull(document);
            System.out.println("ParallelCost: " + time / 1000000 + "ms");
            System.out.println("NormalCost: " + time_n / 1000000 + "ms");
            System.out.println("SpeedUp: " + time_n/time);
            pj.worker.printThreadSummraize();
            assertTrue(d.hasSameValue(document));
        }
    }


//    public void testPerformance2() throws Exception {
//
//        for (int t = 1; t <= 10; ++t) {
//            String data = App.readFile("src/test/extern/files/"+t+".html");
//            System.out.println("\n文件长度："+t+"M");
//            ParallelJsoup pj = new ParallelJsoup(data);
//            Document document = pj.parse();
//            Document d = Parser.parse(data, "");
//            double time = 0, time_n = 0;
//
//            long b, e;
//            for (int i = 0; i < 100; i++) {
//                b = System.nanoTime();
//                document = pj.parse();
//                e = System.nanoTime();
//                time += e - b;
//                b = System.nanoTime();
//                d = Parser.parse(data, "");
//                e = System.nanoTime();
//                time_n += e - b;
//            }
//
//            assertNotNull(document);
//            System.out.println("ParallelCost: " + time / 1000000 + "ms");
//            System.out.println("NormalCost: " + time_n / 1000000 + "ms");
//            System.out.println("SpeedUp: " + time_n/time);
//            pj.worker.printThreadSummraize();
//            assertTrue(d.hasSameValue(document));
//        }
//    }

}
