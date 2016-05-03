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
    public static final int span = 1024;

    public ParallelJsoup(String data) {
        this.data = data;
        worker = new Worker(data);
    }

    public Document parse() {
        YetAnotherLexer lexer = new YetAnotherLexer(data);
        lexer.callback = (tag t) -> {
            worker.run(t);
        };
        tags = lexer.tags;
        lexer.find();

        return null;
    }
}
