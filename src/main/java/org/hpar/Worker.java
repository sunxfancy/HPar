package org.hpar;

import org.jsoup.nodes.Element;
import org.jsoup.parser.PartParser;

import java.util.concurrent.*;

/**
 *
 * Created by sxf on 4/30/16.
 */
public class Worker {
    private ThreadPoolExecutor threadPool;
    private char[] data;
    Job mainJob = null;
    public Worker(char[] data, int n) {
        this.data = data;
        this.threadPool = new ThreadPoolExecutor(n, n,
                1000L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>());
    }

    public void run(tag t) {
        Job j = new Job(t, data);
        if (t.pos == 0) mainJob = j;
        threadPool.execute(j);
    }

    public void printThreadSummraize() {
        ThreadPoolExecutor pool = threadPool;
        System.out.println("完成的任务数：" + pool.getTaskCount());
        System.out.println("峰值线程数" + pool.getLargestPoolSize());

//        for (tag t = mainJob.tags; t.pos != -1; t = t.getNext()) {
//            if (t.getStatus() != tag.WorkStatus.done) {
//                continue;
//            }
//            if (t.pos != 0)
//            System.out.println(t.pos + " - " + t.end);
//            System.out.println(String.copyValueOf(data, t.pos, t.end - t.pos));
//        }
    }


    public Element getAll() {
        return mainJob.begin_tag.getElement();
    }
}


class Job implements Runnable {
    tag begin_tag;
    tag tags;
    private char[] data;

    public Job(tag tags, char[] data) {
        this.begin_tag = this.tags = tags;
        this.data = data;
    }

    @Override
    public void run() {
        do {
            if (tags.status == tag.WorkStatus.undo){
                synchronized (tags.sync_status) {
                    if (tags.status == tag.WorkStatus.undo)
                        tags.status = tag.WorkStatus.doing;
                    else
                        return;
                }
//                System.out.println("Job run：" + tags.pos);
                Element element = null;
                try {
                    element = PartParser.parse(data, tags.pos, tags);
                } catch (Exception e) {
                    System.out.println("error");
                    e.printStackTrace();
                } finally {
//                    System.out.println("Done." + tags.pos);
                    tags.setElement(element);
                    tags.setStatus(tag.WorkStatus.done);
                }
            }
            if (tags.pos == 0 && tags.getNext().pos == -1) return;
            tags = tags.getNext();
        } while (tags !=null && tags.getStatus() != tag.WorkStatus.done
                && tags.getStatus() != tag.WorkStatus.doing);
    }
}
