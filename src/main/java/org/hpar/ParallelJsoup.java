package org.hpar;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Basic Class for HPar.
 * Used for creating the parser and running the paralleled method
 * e.g.
 * String data = "<html></html>";
 * // a new paralleled Jsoup with 8 threads
 * ParallelJsoup pj = new ParallelJsoup(data, 8);
 * // parsing the HTML data
 * Document document = pj.parse();
 * Created by sxf on 5/1/16.
 */
public class ParallelJsoup {
    tag tags;
    char[] data;
    Worker worker;
    int span = 1024*3;
    int threads = 8;

    /**
     * Constructor of HPar
     * @param data the HTML string
     */
    public ParallelJsoup(char[] data) {
        this.data = data;
//        span = data.length() / (threads+2);
        worker = new Worker(data, threads);
    }

    /**
     * Constructor of HPar
     * @param data the HTML string
     * @param t the highest threads number
     */
    public ParallelJsoup(char[] data, int t) {
        this.data = data;
        this.threads = t;
//        span = data.length() / (threads+2);
        worker = new Worker(data, threads);
    }



    private tag lastt;

    /**
     * Begin parsing
     * @return Document in Jsoup, error for null
     */
    public Document parse() {
        YetAnotherLexer lexer = new YetAnotherLexer(data);
        lexer.callback = new YetAnotherLexer.Callback() {
            @Override
            public void find(tag t) {
                if (t.pos - lastt.pos >= span && t.getStatus() == tag.WorkStatus.undo) {
                    worker.run(t);
                    lastt = t;
                }
            }
        };
        tag t = new tag(0, 0, tag.other_begin);
        this.lastt = this.tags = lexer.tail = lexer.tags = t;
        worker.run(t);
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        lexer.find();
        Element e = worker.getAll();
        if (!(e instanceof Document)) {
			System.out.println("Error: Element is not a instance of Document");
			System.out.println(worker.mainJob.tags);
            System.out.println(e);
        }
        return (Document)e;
    }

    public void closeAll() {
        worker.closeAll();
    }
}
