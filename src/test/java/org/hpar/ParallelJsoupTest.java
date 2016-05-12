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
        String data = App.readFile("src/test/extern/index.html");

        ParallelJsoup pj = new ParallelJsoup(data);
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

    public void testParse() throws Exception {
        String data = App.readFile("src/test/extern/index.html");

        ParallelJsoup pj = new ParallelJsoup(data);
        Document document = pj.parse();
        Document d = Parser.parse(data, "");


        long b = System.nanoTime();
        for (int i = 0; i < 10; i++) {
            pj = new ParallelJsoup(data);
            document = pj.parse();
        }
        long e = System.nanoTime();

        assertNotNull(document);
        double time = e - b;

        b = System.nanoTime();
        for (int i = 0; i < 10; i++) {
            d = Parser.parse(data, "");
        }
        e = System.nanoTime();
        double time_n = e - b;

        assertTrue(d.hasSameValue(document));
        System.out.println("TimeCost: "+time/time_n);
    }

}