package com.fs.mongo;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
/**
 * Created by fangsheng on 2017/8/27.
 *
 * @cnstonefang@gmail.com
 */
public class ThreadUtil {

        private static ThreadUtil threadUtil = new ThreadUtil();

        private static final int SIZE_CORE_POOL = 3;

        private static final int SIZE_MAX_POOL = 10;

        private static final int TIME_KEEP_ALIVE = 5000;

        private static final int SIZE_WORK_QUEUE = 65535;

        private static final int PERIOD_TASK_QOS = 1000;
        // 任务缓冲队列
        private final Queue<Runnable>taskQueue = new LinkedList<Runnable>();
        /*
         * 线程忙加入队列中
         */
        private final RejectedExecutionHandler handler = new MyRejectedExecutionHandler();
        /*
         * 线程池
         */
        private final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(SIZE_CORE_POOL,
                SIZE_MAX_POOL,
                TIME_KEEP_ALIVE, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(SIZE_WORK_QUEUE),
                handler);

        private ThreadUtil() {
        }
        /*
         * 线程池单例创建方法
         */
        public static ThreadUtil newInstance() {
            return threadUtil;
        }

        /*
         * 将缓冲队列中的任务重新加载到线程池
         */
        private final Runnable accessBufferThread = new Runnable() {
            public void run() {
                if (hasMoreAcquire()) {
                    threadPool.execute(taskQueue.poll());
                }
            }
        };

        /*
         * 创建一个调度线程池
         */
        private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        /*
         * 通过调度线程周期性的执行缓冲队列中任务
         */
        protected final ScheduledFuture<?> mTaskHandler = scheduler.scheduleAtFixedRate(
                accessBufferThread, 0,
                PERIOD_TASK_QOS, TimeUnit.MILLISECONDS);

        public void prepare() {
            if (threadPool.isShutdown() && !threadPool.prestartCoreThread()) {
                @SuppressWarnings("unused") final int startThread =
                        threadPool.prestartAllCoreThreads();
            }
        }

        /*
         * 消息队列检查方法
         */
        private boolean hasMoreAcquire() {
            return !taskQueue.isEmpty();
        }

        /*
         * 向线程池中添加任务方法
         */
        public void addExecuteTask(final Runnable task) {
            if (task != null) {
                threadPool.execute(task);
            }
        }

        protected boolean isTaskEnd() {
            if (threadPool.getActiveCount() == 0) {
                return true;
            } else {
                return false;
            }
        }

        public void shutdown() {
            System.out.println("shut down");
            taskQueue.clear();
            threadPool.shutdown();
        }

    private class MyRejectedExecutionHandler implements RejectedExecutionHandler {
        public void rejectedExecution(final Runnable task, final ThreadPoolExecutor executor) {
            taskQueue.offer(task);
        }
    }
}
