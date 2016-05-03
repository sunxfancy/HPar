package org.jsoup.parser;

import org.hpar.tag;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Created by sxf on 5/1/16.
 */
public class PartParser {

    public static Element parse(String string, int pos, tag now) {
        HtmlTreeBuilder treeBuilder = new HtmlTreeBuilder();
        return treeBuilder.parsePart(string, pos, now);
    }

}
