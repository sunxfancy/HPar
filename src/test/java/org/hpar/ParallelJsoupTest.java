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
        String data = App.readFile("src/test/extern/EdSurge - Google+.html");

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
        String data = App.readFile("src/test/extern/LangRef.html");

        ParallelJsoup pj = new ParallelJsoup(data);
        Document document = pj.parse();
        Document d = Parser.parse(data, "");
        double time=0, time_n = 0;
        pj = new ParallelJsoup(data);

        long b, e;
        for (int i = 0; i < 100; i++) {
//        while (true) {
            b = System.nanoTime();
            document = pj.parse();
            e = System.nanoTime();
            time += e-b;
            b = System.nanoTime();
            d = Parser.parse(data, "");
            e = System.nanoTime();
            time_n += e-b;
        }

        assertNotNull(document);

        System.out.println("TimeCost: "+time/time_n);
        pj.worker.printThreadSummraize();
        assertTrue(d.hasSameValue(document));
    }
}