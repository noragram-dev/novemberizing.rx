package novemberizing.rx.example.operator.async;

import i.Scheduler;
import i.operator.Operator;
import i.operator.Task;
import novemberizing.util.Debug;
import novemberizing.util.Log;

import java.util.Collection;

/**
 *
 * @author novemberizing, i@novemberizing.net
 * @since 2017. 1. 9.
 */
public class Example {
    public static class Async<T, U> extends Operator<T, U> {
        public static <T, U> Async<T, U> New(int second, Func<T, U> f){
            return new Async<>(second, f);
        }

        private int __second = 1000;
        private Func<T, U> __func;

        private Async(int second,Func<T, U> f){
            __second = second;
            __func = f;
        }

        @Override
        protected Task<T> __on(Task<T> task) {
            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(__second);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                task.v.out = call(task.v.in);
                task.next();
            });
            thread.start();

            return task;
        }

        @Override
        public U call(T first) {
            if(__func==null){ Debug.On(new RuntimeException("")); }
            return __func!=null ? __func.call(first) : null;
        }
    }

    public static class Sync<T, U> extends i.operator.Sync<T, U> {
        public static <T, U> Sync<T, U> New(int second, Func<T, U> f){
            return new Sync<>(second, f);
        }

        private int __second = 1000;

        private Sync(int second,Func<T, U> f){
            super(f);
            __second = second;
        }

        @Override
        protected Task<T> __on(Task<T> task) {
            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(__second);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                task.v.out = call(task.v.in);
                task.next();
            });
            thread.start();

            return task;
        }

    }
    public static void main(String[] args){
//        Log.disable(Log.HEADER);
        Log.depth(3);

        Collection<Task<String>> tasks;

//        tasks = Scheduler.Foreach(Async.New(1000, o -> Log.i("async(async(f)) >", o)), args);
//
//        for(Task<String> o : tasks){
//            while(!o.done()){
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }

        tasks = Scheduler.Foreach(Sync.New(1000, o -> Log.i("async(sync(f)) >", o)), args);

        for(Task<String> o : tasks){
            while(!o.done()){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}