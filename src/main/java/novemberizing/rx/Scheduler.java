package novemberizing.rx;

import novemberizing.rx.scheduler.Local;

import java.util.Collection;
import java.util.LinkedList;

/**
 *
 * @author novemberizing, novemberizing.rx@novemberizing.net
 * @since 2017. 1. 10.
 */
public class Scheduler extends Executor {
    private static int __DefaultSyncSleepMillisecond = 10;

    public static int DefaultSyncSleepMillisecond(){ return __DefaultSyncSleepMillisecond; }

    public static Scheduler Local(){ return Local.Get(); }

    public static <T> Task<T> Exec(Operator<T, ?> op, T o){ return Exec(Scheduler.Local(), op, o); }

    public static <T> Task<T> Exec(Scheduler scheduler, Operator<T, ?> op, T o){
        if(scheduler==null){
            scheduler = Scheduler.Local();
        }
        Task<T> task = new Task<>(o, op, scheduler);
        scheduler.dispatch(task);
        return task;
    }

    public static <T, U> Task<T> Exec(Task<U> previous, Operator<T, ?> op, T o){
        Scheduler scheduler = previous.scheduler();
        if(scheduler==null){
            scheduler = Scheduler.Local();
        }
        Task<T> task = new Task<>(o, op, scheduler, previous);
        scheduler.dispatch(task);
        return task;
    }

    public static <T> Task<T> Sync(Task<T> task){
        while(task!=null && !task.done()){
            try {
                Thread.sleep(Scheduler.DefaultSyncSleepMillisecond());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return task;
    }

    public static <T> Collection<Task<T>> Sync(Collection<Task<T>> tasks){
        for(Task<T> task : tasks){
            while(task!=null && !task.done()){
                try {
                    Thread.sleep(Scheduler.DefaultSyncSleepMillisecond());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return tasks;
    }

    public static <T> Collection<Task<T>> Foreach(Operator<T, ?> op, T[] items){ return Foreach(Scheduler.Local(), op, items); }

    public static <T> Collection<Task<T>> Foreach(Scheduler scheduler, Operator<T, ?> op, T[] items){
        if(scheduler==null){
            scheduler = Scheduler.Local();
        }
        LinkedList<Task<T>> tasks = new LinkedList<>();
        for(T item : items) {
            tasks.add(Exec(scheduler, op, item));
        }
        return tasks;
    }

    public static <T, U> Collection<Task<T>> Foreach(Task<U> previous, Operator<T, ?> op, T[] items){
        LinkedList<Task<T>> tasks = new LinkedList<>();
        for(T item : items) {
            tasks.add(Exec(previous, op, item));
        }
        return tasks;
    }

    public static <T> Collection<Task<T>> Foreach(Operator<T, ?> op, T o,  T... items){ return Foreach(Scheduler.Local(), op, o, items); }

    @SafeVarargs
    public static <T> Collection<Task<T>> Foreach(Scheduler scheduler, Operator<T, ?> op, T o, T... items){
        if(scheduler==null){
            scheduler = Scheduler.Local();
        }
        LinkedList<Task<T>> tasks = new LinkedList<>();
        tasks.add(Exec(scheduler, op, o));
        for(T item : items) {
            tasks.add(Exec(scheduler, op, item));
        }
        return tasks;
    }

    @SafeVarargs
    public static <T, U> Collection<Task<T>> Foreach(Task<U> previous, Operator<T, ?> op, T o, T... items){
        LinkedList<Task<T>> tasks = new LinkedList<>();
        tasks.add(Exec(previous, op, o));
        for(T item : items) {
            tasks.add(Exec(previous, op, item));
        }
        return tasks;
    }
}