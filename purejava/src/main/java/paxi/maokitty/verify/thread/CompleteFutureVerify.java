package paxi.maokitty.verify.thread;

import paxi.maokitty.verify.service.TimeCostService;
import paxi.maokitty.verify.service.impl.FixedTimeCostService;
import paxi.maokitty.verify.service.impl.RandomTomeCostService;
import paxi.maokitty.verify.service.impl.TimeOutTimeCostService;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Created by maokitty on 20/11/8.
 */
public class CompleteFutureVerify {

    public static void main(String[] args) {

//        completeAsync();
//        completeHandleException();
//        multiCompleteFuture();
//        multiCompleteFutureWithThread();
//        multiTaskAsyncAndSync();
//        multiTaskCombine();
//        multiTaskAccept();
        multiCompleteFutureThreadCompareForAccept();
    }



    private static void completeAsync() {
        TimeCostService timeCostService = new FixedTimeCostService();
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(()->timeCostService.delay("async test ",1000));
        try {
            String result = completableFuture.get(2, TimeUnit.SECONDS);
            System.out.println(result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    private static void completeHandleException(){

        TimeCostService timeCostService = new TimeOutTimeCostService();
        //异常会被抛出来
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> timeCostService.delay("async test", 1000))
                .exceptionally((t) -> "fail"); //异常抛出，会执行这里非方法，没有异常，就会返回正常的值

        try {
            String result = completableFuture.get(2, TimeUnit.SECONDS);
            System.out.println(result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            System.out.println("execution exception");
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }

    //全部完成才响应
    private static void multiCompleteFuture(){
        TimeCostService timeCostService = new FixedTimeCostService();

        List<CompletableFuture<String>> completableFutures = Arrays.asList("1", "2", "3").stream()
                .map(index -> CompletableFuture.supplyAsync(() -> timeCostService.delay("test" + index, 1000)))
                .collect(Collectors.toList());
        //join 等待所有的执行完
        List<String> results = completableFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
        results.stream().forEach(System.out::println);
    }

    private static void multiCompleteFutureWithThread(){

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 2, 10000, TimeUnit.SECONDS
                , new LinkedBlockingQueue<>(), new MyThreadFactory("my thread pool factory"));
        TimeCostService timeCostService = new FixedTimeCostService();
        List<CompletableFuture<String>> completableFutures = Arrays.asList("1", "2", "3").stream()
                .map(index -> CompletableFuture.supplyAsync(() -> timeCostService.delay("test" + index, 1000),threadPoolExecutor))
                .collect(Collectors.toList());
        List<String> results = completableFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
        results.stream().forEach(System.out::println);
        threadPoolExecutor.shutdown();
    }

    //方法存在依赖,接收上一个异常任务的结果
    private static void multiTaskAsyncAndSync(){
        TimeCostService fixed = new FixedTimeCostService();
        TimeCostService rand = new RandomTomeCostService();

        List<CompletableFuture<String>> results = Arrays.asList("1", "2", "3").stream().map(index ->
                CompletableFuture.supplyAsync(() -> fixed.delay("fixed" + index, 1000)) //1 异步执行
        ).map(future -> future.thenApply((fixedResult -> //同步执行
                fixedResult.substring(0, 5)
        ))).map(future -> future.thenCompose( //3 异步执行另外一个任务,与1使用相同的线程池
                        preResult -> CompletableFuture.supplyAsync(() -> rand.delay("random " + preResult, 1000)))
        ).collect(Collectors.toList());
        List<String> myList = results.stream().map(CompletableFuture::join).collect(Collectors.toList());
        myList.forEach(System.out::println);

    }

    //方法不存在依赖，但是需要组合2者结果
    private static void multiTaskCombine(){
        TimeCostService fixed = new FixedTimeCostService();
        TimeCostService rand = new RandomTomeCostService();

        List<CompletableFuture<String>> futures = Arrays.asList("1", "2", "3").stream().map(index->
             CompletableFuture.supplyAsync(() -> fixed.delay("fixed"+index , 1000))
                    .thenCombine(CompletableFuture.supplyAsync(() -> rand.delay("random" + index, 1000)), (r, f) -> r + " : " + f)
        ).collect(Collectors.toList());

        List<String> myList = futures.stream().map(CompletableFuture::join).collect(Collectors.toList());
        myList.forEach(System.out::println);

    }

    //每完成1个任务就响应
    private static void multiTaskAccept(){
        TimeCostService rand = new FixedTimeCostService();

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 2, 10000, TimeUnit.SECONDS
                , new LinkedBlockingQueue<>(), new MyThreadFactory("my thread pool factory"));

        CompletableFuture[] completableFutures = Arrays.asList("1", "2", "3").stream().map(index ->
                        CompletableFuture.supplyAsync(() -> rand.delay("random" + index, 1000), threadPoolExecutor)
        ).map(future -> future.thenAccept(System.out::println)).toArray(size -> new CompletableFuture[size]);

        CompletableFuture.allOf(completableFutures).join();
        threadPoolExecutor.shutdown();
    }

    private static void multiCompleteFutureThreadCompareForAccept(){
        TimeCostService timeCostService = new FixedTimeCostService();

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 2, 10000, TimeUnit.SECONDS
                , new LinkedBlockingQueue<>(), new MyThreadFactory("my thread pool factory"));

        List<CompletableFuture<String>> completableFutures = Arrays.asList("1", "2", "3").stream()
                .map(index -> CompletableFuture.supplyAsync(() -> timeCostService.delay("test" + index, 1000),threadPoolExecutor))
                .collect(Collectors.toList());
        //join 等待所有的执行完
        List<String> results = completableFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
        results.stream().forEach(System.out::println);
    }


    public static class  MyThreadFactory implements ThreadFactory{
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        MyThreadFactory(String namePrefix) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            this.namePrefix = namePrefix +"-"+
                    poolNumber.getAndIncrement() +
                    "-thread-";
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon())
                t.setDaemon(true);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }


}


