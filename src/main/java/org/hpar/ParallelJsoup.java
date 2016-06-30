package org.hpar;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * Created by sxf on 5/1/16.
 */
public class ParallelJsoup {
    tag tags;
    String data;
    Worker worker;
    public static final int span = 1024*10;
    public static final int threads = 8;
    public ParallelJsoup(String data) {
        this.data = data;
        worker = new Worker(data.toCharArray(), threads);
    }

    private tag lastt;

    public Document parse() {
        YetAnotherLexer lexer = new YetAnotherLexer(data);
        lexer.callback = (tag t) -> {
            if (t.pos - lastt.pos >= span && t.getStatus() == tag.WorkStatus.undo) {
                worker.run(t);
                lastt = t;
            }
        };
        tag t = new tag(0, 0, tag.other_begin);
        tags = t;
        lexer.tail = lexer.tags = t;
        lastt = t;
        worker.run(t);
        lexer.find();
        Element e = worker.getAll();
        if (!(e instanceof Document)) {
            System.out.println(e);
        }
        return (Document)e;
    }
}
