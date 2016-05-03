package org.hpar;

import org.jsoup.parser.PartParser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * Created by sxf on 4/30/16.
 */
public class Worker {
    private ExecutorService threadPool = Executors.newCachedThreadPool();
    private String data;

    public Worker(String data) {
        this.data = data;
    }

    public void run(tag t) {
        threadPool.execute(new Job(t, data));
    }
}

class Job implements Runnable {
    tag tags;
    String data;

    public Job(tag tags, String data) {
        this.tags = tags;
        this.data = data;
    }

    @Override
    public void run() {
        tags.setElement(PartParser.parse(data, tags.pos, tags));
    }
}