package i;

import i.operator.Task;
import i.scheduler.Local;

import java.util.Collection;
import java.util.LinkedList;

/**
 *
 * @author novemberizing, i@novemberizing.net
 * @since 2017. 1. 10.
 */
public class Scheduler extends Executor {

    public static Scheduler Local(){ return Local.Get(); }

    public static <T> Task<T> Exec(Operator<T, ?> op, T o){
        Scheduler scheduler = Scheduler.Local();
        Task<T> task = new Task<>(o, op, scheduler);
        scheduler.dispatch(task);
        return task;
    }

    public static <T> Collection<Task<T>> Foreach(Operator<T, ?> op, T[] items){
        Scheduler scheduler = Scheduler.Local();
        LinkedList<Task<T>> tasks = new LinkedList<>();
        for(T item : items) {
            Task<T> task = new Task<>(item, op, scheduler);
            tasks.addLast(task);
            scheduler.dispatch(task);
        }
        return tasks;
    }
}
