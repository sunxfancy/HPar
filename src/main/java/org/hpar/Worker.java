package org.hpar;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.PartParser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 *
 * Created by sxf on 4/30/16.
 */
public class Worker {
    static private ExecutorService threadPool = Executors.newFixedThreadPool(8);
    private char[] data;
    private Job mainJob;
    public Worker(char[] data) {
        this.data = data;
    }

    public void run(tag t) {
        Job j = new Job(t, data);
        if (t.pos == 0) mainJob = j;
        threadPool.execute(j);
    }

    public void printThreadSummraize() {
        ThreadPoolExecutor pool = (ThreadPoolExecutor) threadPool;
        System.out.println("完成的任务数：" + pool.getTaskCount());
        System.out.println("峰值线程数" + pool.getLargestPoolSize());

        for (tag t = mainJob.tags; t != null; t = t.next) {
            if (t.getStatus() != tag.WorkStatus.done) {
                continue;
            }
            System.out.println("开始位置:"+ t.pos);
//            if (t.pos != 0)
//            System.out.println(t.element);
        }
    }


    public Document getAll() {
        return (Document) mainJob.tags.getElement();
    }
}


class Job implements Runnable {
    tag tags;
    private char[] data;

    public Job(tag tags, char[] data) {
        this.tags = tags;
        this.data = data;
    }

    @Override
    public void run() {
        do {
            if (tags.getStatus() == tag.WorkStatus.undo){
                tags.setStatus(tag.WorkStatus.doing);

//                System.out.println("Job run：" + tags.pos);
                Element element = null;
                try {
                    element = PartParser.parse(data, tags.pos, tags);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
//                    System.out.println("Done." + tags.pos);
                    tags.setElement(element);
                    tags.setStatus(tag.WorkStatus.done);
                }
            }
            if (tags.pos == 0) break;
            tags = tags.next;
        } while (tags !=null && tags.getStatus() != tag.WorkStatus.done
                && tags.getStatus() != tag.WorkStatus.doing);
    }
}