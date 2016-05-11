package org.hpar;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * Created by sxf on 4/30/16.
 */
public class tag {
    int pos;
    int size;
    int type;
    public int end;
    public tag next = null;

    Element element = null;

    public synchronized Element getElement() {
        if (element == null) {
            try {
                this.wait();
            } catch (InterruptedException e) {
//                e.printStackTrace();
            }
        }
        return element;
    }

    public synchronized void setElement(Element element) {
        this.element = element;
        this.notifyAll();
    }

    public tag(int begin, int end, int type) {
        this.pos = begin;
        this.size = end - begin;
        this.type = type;
    }

    static final int script_begin = 1;
    static final int script_end = 2;
    static final int comment_begin = 3;
    static final int comment_end = 4;
    static final int other_begin = 5;
    static final int other_end = 6;

    static final int self_end = 250;
}