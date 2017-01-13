package novemberizing.rx.operator;

import novemberizing.ds.On;
import novemberizing.rx.Scheduler;
import novemberizing.rx.task.Task;
import novemberizing.util.Log;

import java.util.HashSet;
import java.util.Random;

/**
 *
 * @author novemberizing, novemberizing@gmail.com
 * @since 2017. 1. 12.
 */
@SuppressWarnings("DanglingJavadoc")
public class CompletionPort extends Operator<novemberizing.ds.Task, novemberizing.ds.Task> {
    private static final String Tag = "CompletionPort";

    private final HashSet<novemberizing.ds.Task> __tasks = new HashSet<>();
    @Override
    protected Task<novemberizing.ds.Task, novemberizing.ds.Task> on(Task<novemberizing.ds.Task, novemberizing.ds.Task> task) {
        boolean ret;
        synchronized (__tasks){
            Log.e(Tag, "");
            /**
             * check thread safety
             */
            if(!(ret = task.in.done())) {
                __tasks.add(task.in);
                task.in.on(new On<novemberizing.ds.Task>() {
                    @Override
                    public void on(novemberizing.ds.Task v) {
                        synchronized (__tasks) {
                            Log.e(Tag, "");
                            __tasks.remove(v);
                            task.it++;
                            task.executed();
                        }
                    }
                });
            }
        }
        return ret ? task : null;
    }

    public static class Example {
        public static class Run<T, U> extends Sync<T, U>  {
            public static <T, U> Run<T, U> New(int second, Operator.Func<T, U> f){
                return new Run<>(second, f);
            }

            private Operator.Func<T, U> __func;
            private int __second = 100;

            private Run(int second, Operator.Func<T, U> f){
                __second = second;
                __func = f;
            }

            @Override
            protected Task<T, U> on(Task<T, U> task) {
                Random r = new Random();
                Thread thread = new Thread(() -> {
                    try {
                        Thread.sleep(__second + (Math.abs(r.nextInt()) % 10 + 10) * 10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    task.out = __func!=null ? __func.call(task.in) : null;
                    task.it++;
                    task.executed();
//                    out(task);
                });
                thread.start();
                return null;
            }
        }

        public static void main(String[] args){
            Log.depth(3);
            Log.disable(Log.FLOW);
            String[] strings = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
            Operator.Exec(CompletionPort(),Operator.Foreach(Run.New(100,
                    o->Log.i("sync >", o)),
                    strings)).on(o->Log.i("completion.port >", o));
            Scheduler.Local().clear();
        }
    }

    public static class Async {
        public static class Run<T, U> extends Operator<T, U>  {
            public static <T, U> Run<T, U> New(int second, Operator.Func<T, U> f){
                return new Run<>(second, f);
            }

            private Operator.Func<T, U> __func;
            private int __second = 100;

            private Run(int second, Operator.Func<T, U> f){
                __second = second;
                __func = f;
            }

            @Override
            protected Task<T, U> on(Task<T, U> task) {
                Random r = new Random();
                Thread thread = new Thread(() -> {
                    try {
                        Thread.sleep(__second + (Math.abs(r.nextInt()) % 10 + 10) * 10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    task.out = __func!=null ? __func.call(task.in) : null;
                    task.it++;
                    task.executed();
//                    out(task);
                });
                thread.start();
                return null;
            }
        }

        public static void main(String[] args){
            Log.depth(3);
            Log.disable(Log.FLOW);
            String[] strings = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
            Operator.Exec(CompletionPort(),Operator.Foreach(Run.New(100,
                    o->Log.i("sync >", o)),
                    strings)).on(o->Log.i("completion.port >", o));
            Scheduler.Local().clear();
        }
    }
}
