package org.jsoup.parser;

import org.jsoup.nodes.Document;

/**
 * Created by sxf on 5/1/16.
 */
public class PartParser {

    public Document parse(String string, int pos) {
        HtmlTreeBuilder treeBuilder = new HtmlTreeBuilder();
        return treeBuilder.parsePart(string, pos);
    }

}
