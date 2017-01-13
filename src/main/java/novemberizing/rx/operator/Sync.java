package novemberizing.rx.operator;

import com.google.gson.annotations.Expose;
import novemberizing.ds.Func;
import novemberizing.ds.On;
import novemberizing.rx.Scheduler;
import novemberizing.util.Log;

import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author novemberizing, novemberizing@gmail.com
 * @since 2017. 1. 12.
 */
@SuppressWarnings("StatementWithEmptyBody")
public abstract class Sync<T, U> extends Operator<T, U> {
    private static final String Tag = "Sync";

    public Sync(){}

    private Sync<T, U> __self = this;
    @Expose private final LinkedList<Task<T, U>> __tasks = new LinkedList<>();
    @Expose private Task<T, U> __current = null;

    protected Task<T, U> in(Task<T, U> task){
        Log.f(Tag + " :in", task);
        synchronized (__tasks){
            if(__current!=null){
                __tasks.addLast(task);
                task = null;
            } else {
                __current = task;
            }
        }
        return task;
    }

    protected Task<T, U> out(Task<T, U> task){
        Log.f(Tag + " :out", this, task);
        synchronized (__tasks) {
            task.done(true);
            if(__current==task){
                do {
                    if(__tasks.size()>0) {
                        __current = __tasks.pollFirst();
                    } else {
                        __current = null;
                    }
                } while(__current==null && __tasks.size()>0);
                if(__current!=null){
                    __current.it++;
                    __current.executed();
                }
            } else {
                Log.e(Tag, this, task);
            }
        }
        return task;
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
            Operator.Foreach(Run.New(100,
                    o->Log.i("sync >", o)),
                    strings);
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
            Operator.Foreach(Run.New(100,
                    o->Log.i("sync >", o)),
                    strings);
            Scheduler.Local().clear();
        }
    }

}
