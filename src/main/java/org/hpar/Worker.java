package org.hpar;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * Created by sxf on 4/30/16.
 */
public class Worker {
    private ExecutorService threadPool = Executors.newCachedThreadPool();


    public void run(tag t) {
        threadPool.execute(new Job(t));
    }
}

class Job implements Runnable {
    public Job(tag tags) {
        this.tags = tags;
    }

    tag tags;
    @Override
    public void run() {

    }
}