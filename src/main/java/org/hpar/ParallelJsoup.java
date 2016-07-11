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
    int span = 1024*100;
    int threads = 8;
    public ParallelJsoup(String data) {
        this.data = data;
//        span = data.length() / (threads+2);
        worker = new Worker(data.toCharArray(), threads);
    }
    public ParallelJsoup(String data, int t) {
        this.data = data;
        this.threads = t;
//        span = data.length() / (threads+2);
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
        this.lastt = this.tags = lexer.tail = lexer.tags = t;
        worker.run(t);
        lexer.find();
        Element e = worker.getAll();
        if (!(e instanceof Document)) {
			System.out.println("Error: Element is not a instance of Document");
			System.out.println(worker.mainJob.tags);
            System.out.println(e);
        }
        return (Document)e;
    }
}
