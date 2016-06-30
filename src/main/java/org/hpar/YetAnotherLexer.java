package org.hpar;

import org.jsoup.nodes.Element;

/**
 *
 * Created by sxf on 4/27/16.
 */
public class YetAnotherLexer {
    private char[] data;
    private int pos;
    private int data_begin, data_end;

    public interface Callback {
        void find(tag t);
    }
    public Callback callback;

    tag tags = null;
    tag tail = null;

    public YetAnotherLexer(String data) {
        this.data = data.toCharArray();
        this.data_begin = 0;
        this.data_end = data.length();
        reset();
    }

    public YetAnotherLexer(String data, int data_begin, int data_end) {
        this.data = data.toCharArray();
        this.data_begin = data_begin;
        this.data_end = data_end;
        reset();
    }

    public void reset() {
        pos = data_begin;
    }

    private void skipSpace() {
        while (pos < data_end) {
            if (!Character.isWhitespace(data[pos]))
                return;
            ++pos;
        }
    }

    private void skipQuote() {
        char quote = data[pos];
        ++pos;
        while (pos < data_end) {
            if (data[pos] == quote) {
                ++pos;
                return;
            }
            if (data[pos] == '\\')
                ++pos;
            ++pos;
        }
    }

    public void print() {
        for (tag t = tags; t.pos != -1; t=t.getNext()) {
            System.out.println("Match: "+String.copyValueOf(data, t.pos, t.size));
            System.out.println("from: "+t.pos+" - "+(t.pos+t.size));
        }
    }

    private void addTail(tag t) {
        if (tail != null) tail.setNext(t);
        tail = t;
        if (tags == null) tags = tail;
        if (callback != null && t.pos != -1)
            callback.find(t);
    }


    public tag find() {
        reset();
        boolean tag_open = false;
        boolean comment_open = false;

        while (pos < data_end) {
            switch (data[pos]) {
                case '<': {
                    if (comment_open) { ++pos; break; }
                    int first = pos++;
                    if (data[pos] == '/') {
                        pos++;
                        if (last_match != null && findScriptClose(first))
                            tag_open = false;
                    } else if (data[pos] == '!' && pos+2 < data_end
                            && data[pos+1]=='-' && data[pos+2]=='-') {
                        pos += 3;
                        comment_open = true;
                    } else {
                        if (findScript(first))
                            tag_open = true;
                    }
                } break;
                case '-': {
                    if (tag_open) { ++pos; break; }
                    if (pos+2 < data_end
                            && data[pos+1]=='-' && data[pos+2]=='>') {
                        pos += 2;
                        comment_open = false;
                    }
                    ++pos;
                } break;
                case '\'':
                case '\"': {
                    if (comment_open||!tag_open) { ++pos; break; }
                    skipQuote();
                } break;
                default:
                    ++pos;
            }
        }
        tag end = new tag(-1, -1, tag.other_end);
        end.setStatus(tag.WorkStatus.done);
        tail.setNext(end);
        return tags;
    }

    private static char[] script = "cript".toCharArray();
    private static char[] style = "tyle".toCharArray();
    private static char[] div = "div".toCharArray();
    private static char[] noscript = "noscript".toCharArray();
    private static char[] textarea = "textarea".toCharArray();

    private static char[] last_match = null;

    private boolean findTag(char[] str) {
        for (char c : str) {
            if (pos < data_end && data[pos] == c) ++pos;
            else return false;
        }
        if (str != div) last_match = str;
        return true;
    }

    private boolean findSome() {
        if (pos >= data_end) return false;
        if (data[pos] == 's') {
            ++pos;
            return findTag(script) || findTag(style);
        } else {
            return findTag(div) || findTag(noscript) || findTag(textarea);
        }
    }

    private boolean findScript(int begin) {
        skipSpace();
        boolean ans = findSome();

        while (pos < data_end && data[pos] != '>') ++pos;
        int end = ++pos;

        if (data[pos-1] == '/') {
//            addTail(new tag(begin, end, tag.self_end));
            return false;
        }
        if (!ans) {
//            addTail(new tag(begin, end, tag.other_begin));
            return false;
        }
        addTail(new tag(begin, end, tag.script_begin));
        return true;
    }


    private boolean findScriptClose(int begin) {
        skipSpace();
        boolean ans = findTag(last_match);
        last_match = null;
        skipSpace();
        if (pos < data_end && data[pos] == '>') {
            int end = ++pos;
//            addTail(new tag(begin, end, tag.script_end));
            return ans;
        }
        while (pos < data_end && data[pos] != '>') ++pos;
        ++pos;
        return false;
    }

}


