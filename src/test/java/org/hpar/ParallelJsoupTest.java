package org.hpar;

import junit.framework.TestCase;
import org.jsoup.nodes.Document;

/**
 *
 * Created by sxf on 5/11/16.
 */
public class ParallelJsoupTest extends TestCase {
    public void testParse() throws Exception {
        String data = App.readFile("src/test/extern/index.html");
        ParallelJsoup pj = new ParallelJsoup(data);
        Document document = pj.parse();
        System.out.println(document);
        assertNotNull(document);
    }

}