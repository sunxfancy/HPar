package org.hpar;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * Created by sxf on 4/30/16.
 */
public class tag {
    public int pos;
    public int size;
    public int type;
    public int end;
    private tag _next = null;

    private final Object sync_next = new Object();
    private final Object sync_status = new Object();
    private final Object sync_element = new Object();

    public tag getNext() {
        synchronized (sync_next) {
            if (_next == null && pos != -1) {
                try {
                    sync_next.wait();
                } catch (InterruptedException e) {
//                e.printStackTrace();
                }
            }
        }
        return _next;
    }

    public void setNext(tag t) {
        if (t == null) return;
        synchronized (sync_next) {
            _next = t;
            sync_next.notifyAll();
        }
    }

    public enum WorkStatus {
        undo, doing, done, jump
    }

    public WorkStatus status = WorkStatus.undo;
    private Element element = null;

    public void setStatus(WorkStatus status) {
        synchronized (sync_status) {
            this.status = status;
        }
    }

    public WorkStatus getStatus() {
        synchronized (sync_status) {
            return this.status;
        }
    }

    public Element getElement() {
        synchronized (sync_element) {
            if (element == null && pos != -1) {
                try {
                    sync_element.wait();
                } catch (InterruptedException e) {
    //                e.printStackTrace();
                }
            }
        }
        return element;
    }

    public void setElement(Element element) {
        synchronized (sync_element) {
            this.element = element;
            sync_element.notifyAll();
        }
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