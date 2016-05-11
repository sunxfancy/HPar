package org.hpar;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
    private Job mainJob;
    public Worker(String data) {
        this.data = data;
    }

    public void run(tag t) {
        Job j = new Job(t, data);
        if (t.pos == 0) mainJob = j;
        threadPool.execute(j);
    }

    public Document getAll() {
        return (Document) mainJob.tags.getElement();
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
        Element element = null;
        try{
            tags.setStatus(tag.WorkStatus.doing);
            element = PartParser.parse(data, tags.pos, tags);
        }catch(Exception e) {

        }
        tags.setElement(element);
        tags.setStatus(tag.WorkStatus.done);
    }
}