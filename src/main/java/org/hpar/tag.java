package org.hpar;

import org.jsoup.nodes.Element;

/**
 *
 * Created by sxf on 4/30/16.
 */
public class tag {
    int pos;
    int size;
    int type;
    public tag match;
    public tag next = null;

    Element element = null;

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public tag(int begin, int end, int type) {
        this.pos = begin;
        this.size = end - begin;
        this.type = type;
    }


    public static final int script_begin = 1;
    public static final int script_end = 2;
    public static final int comment_begin = 3;
    public static final int comment_end = 4;
    public static final int other_begin = 5;
    public static final int other_end = 6;

    public static final int self_end = 250;
}