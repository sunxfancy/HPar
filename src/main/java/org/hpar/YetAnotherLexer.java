package org.hpar;

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
        for (tag t = tags; t!=null; t=t.next) {
            System.out.println("Match: "+String.copyValueOf(data, t.pos, t.size));
            System.out.println("from: "+t.pos+" - "+(t.pos+t.size));
        }
    }

    private void addTail(tag t) {
        if (tail != null) tail.next = t;
        tail = t;
        if (tags == null) tags = tail;
    }

    private void findOtherBegin(tag t) {
        addTail(t);
        if (callback != null)
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
                        if (findScriptClose(first))
                            tag_open = false;
                        else
                            findOtherBegin(new tag(first, pos, tag.other_end));
                    } else if (data[pos] == '!' && pos+2 < data_end
                            && data[pos+1]=='-' && data[pos+2]=='-') {
                        pos += 3;
                        addTail(new tag(first, pos, tag.comment_begin));
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
                        addTail(new tag(pos, pos+3, tag.comment_end));
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
        return tags;
    }

    private static char[] script = "ript".toCharArray();
    private static char[] style = "yle".toCharArray();


    private boolean findSome() {
        boolean ans = true;
        if (!(pos < data_end && data[pos] == 's')) ans = false;
        else {
            ++pos;
            if (data[pos] == 'c') {
                ++pos;
                for (char c : script) {
                    if (pos < data_end && data[pos] == c) ++pos;
                    else ans = false;
                }
            } else if (data[pos] == 't') {
            ++pos;
                for (char c : style) {
                    if (pos < data_end && data[pos] == c) ++pos;
                    else ans = false;
                }
            } else ans = false;
        }
        return ans;
    }

    private boolean findScript(int begin) {
        skipSpace();
        boolean ans = findSome();

        while (pos < data_end && data[pos] != '>') ++pos;
        int end = ++pos;

        if (data[pos-1] == '/') {
            addTail(new tag(begin, end, tag.self_end));
            return false;
        }
        if (!ans) {
            addTail(new tag(begin, end, tag.other_begin));
            return false;
        }
        addTail(new tag(begin, end, tag.script_begin));
        return true;
    }


    private boolean findScriptClose(int begin) {
        skipSpace();
        boolean ans = findSome();
        skipSpace();
        if (pos < data_end && data[pos] == '>') {
            int end = ++pos;
            addTail(new tag(begin, end, tag.script_end));
            return ans;
        }
        while (pos < data_end && data[pos] != '>') ++pos;
        ++pos;
        return false;
    }

}


