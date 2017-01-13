package novemberizing.rx;

import novemberizing.ds.Func;

import novemberizing.ds.On;
import novemberizing.rx.operator.*;
import novemberizing.util.Log;

import java.util.Collection;
import java.util.LinkedList;

/**
 *
 * @author novemberizing, novemberizing@gmail.com
 * @since 2017. 1. 12.
 */
public interface Operator<T, U> extends Func<Task<T, U>, Task<T,U>> {
    String Tag = "novemberizing.rx.Operator";

    interface Func<T, U> extends novemberizing.ds.Func<T, U>{}

    static <T, U> Task<T, U> DefaultOn(Task<T, U> task,novemberizing.ds.Func<T, U> f) {
        if(f instanceof Operator){
            Operator<T, T> op = (Operator<T, T>) f;
            Operator.Exec(op.build(task.in, task));
            return null;
        } else if(f!=null){
            task.out = f.call(task.in);
        }
        return task;
    }

    Task<T, U> build(T in, novemberizing.ds.Task<?> task);

    Task<U, ?> next(Task<?, U> task);

    Operator<U, ?> next();

    <V> Operator<U, V> next(Func<U, V> f);

    static <T, U> Task<T, U> Exec(Scheduler scheduler, Task<T, U> task){
        scheduler.dispatch(task);
        return task;
    }

    static <T, U> Task<T, U> Exec(Task<T, U> task){
        return Exec(Scheduler.Self(), task);
    }

    static <T> Exec<T> Exec(Operator<T, ?> op, T o){
        Log.f(Tag, "Exec", op, o);
        return Exec(Scheduler.Self(), op, o);
    }

    static <T> Exec<T> Exec(Scheduler scheduler, Operator<T, ?> op, T o){
        Log.f(Tag, "Exec" ,op, o);
        Exec<T> task = new Exec<>(o, op);
        scheduler.dispatch(task);
        return task;
    }

    @SafeVarargs
    static <T> Collection<Exec<T>> Foreach(Operator<T, ?> op, T o, T... items){
        Log.f(Tag, "Foreach", op, o);
        return Foreach(Scheduler.Self(), op, o, items);
    }

    static <T> Collection<Exec<T>> Foreach(Operator<T, ?> op, T[] items){
        Log.f(Tag, "Foreach", op);
        return Foreach(Scheduler.Self(), op, items);
    }

    @SafeVarargs
    static <T> Collection<Exec<T>> Foreach(Scheduler scheduler, Operator<T, ?> op, T o, T... items){
        Log.f(Tag, "Foreach" ,op, o, items);
        LinkedList<Exec<T>> tasks = new LinkedList<>();
        tasks.add(Exec(op, o));
        for(T item : items){
            tasks.add(Exec(op, item));
        }
        return tasks;
    }

    static <T> Collection<Exec<T>> Foreach(Scheduler scheduler, Operator<T, ?> op, T[] items){
        Log.f(Tag, "Foreach" ,op, items);
        LinkedList<Exec<T>> tasks = new LinkedList<>();
        for(T item : items){
            tasks.add(Exec(op, item));
        }
        return tasks;
    }

    static <T> Operator<T, T> Just(){ return new Just<>(); }

    static <T> Operator<T, T> Single(novemberizing.ds.Func<T, T> f){
        return new Single<T>(){
            @Override
            protected Task<T, T> on(Task<T, T> task) {
                return DefaultOn(task, f);
            }
        };
    }

    static <T, U> Operator<T, U> Op(novemberizing.ds.Func<T, U> f){
        if(f==null){
            Log.e(Tag, "f==null");
        }
        return new novemberizing.rx.operator.Operator<T, U>(){
            @Override
            protected Task<T, U> on(Task<T, U> task) {
                return DefaultOn(task, f);
            }
        };
    }

    static <T, U> Sync<T, U> Sync(novemberizing.ds.Func<T, U> f){
        return new Sync<T, U>() {
            @Override
            protected Task<T, U> on(Task<T, U> task) {
                return DefaultOn(task, f);
            }
        };
    }
}
