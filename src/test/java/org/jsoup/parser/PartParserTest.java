package org.jsoup.parser;

import junit.framework.TestCase;
import org.hpar.App;
import org.hpar.tag;
import org.jsoup.nodes.Element;

/**
 * Created by sxf on 5/11/16.
 */
public class PartParserTest extends TestCase {
    public void testParse() throws Exception {
        String data = App.readFile("src/test/extern/index.html");
        Element d = PartParser.parse(data, 0, new tag(0, 0, 5));
        assertNotNull(d);
    }

}