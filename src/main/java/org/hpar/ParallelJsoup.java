package org.hpar;

import org.jsoup.nodes.Document;

/**
 *
 * Created by sxf on 5/1/16.
 */
public class ParallelJsoup {
    tag tags;
    String data;
    Worker worker;
    public static final int span = 1024*50;

    public ParallelJsoup(String data) {
        this.data = data;
        worker = new Worker(data);
    }

    private tag lastt;

    public Document parse() {
        YetAnotherLexer lexer = new YetAnotherLexer(data);
        lexer.callback = (tag t) -> {
            if (t.pos - lastt.pos >= span && t.getStatus() == tag.WorkStatus.undo) {
                t.setStatus(tag.WorkStatus.doing);
                worker.run(t);
                lastt = t;
            }
        };
        tag t = new tag(0, 0, tag.other_begin);
        tags = t;
        lexer.tail = lexer.tags = t;
        lastt = t;
        t.setStatus(tag.WorkStatus.doing);
        worker.run(t);
        lexer.find();
        return worker.getAll();
    }
}
