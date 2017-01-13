package novemberizing.rx;

import novemberizing.ds.Func;

import novemberizing.rx.operator.Exec;
import novemberizing.rx.operator.Just;
import novemberizing.rx.operator.Single;
import novemberizing.rx.operator.Task;
import novemberizing.util.Log;

/**
 *
 * @author novemberizing, novemberizing@gmail.com
 * @since 2017. 1. 12.
 */
public interface Operator<T, U> extends Func<Task<T, U>, Task<T,U>> {
    String Tag = "novemberizing.rx.Operator";

    Task<T, U> build(T in, novemberizing.ds.Task<?> task);

    Task<U, ?> next(Task<?, U> task);
    Task<T, ?> down(Task<T, ?> task);

    Operator<U, ?> next();
    Operator<T, ?> down();

    <V> Operator<U, V> next(Func<U, V> f);
    <V> Operator<T, V> down(Func<T, V> f);

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

    static <T> Operator<T, T> Just(){ return new Just<>(); }

    static <T> Operator<T, T> Single(Func<T, T> f){
        return new Single<T>(){
            @Override
            protected Task<T, T> on(Task<T, T> task) {
                task.out = (f!=null ? f.call(task.in) : task.in);
                return task;
            }
        };
    }

    static <T, U> Operator<T, U> Op(Func<T, U> f){
        if(f==null){
            Log.e(Tag, "f==null");
        }
        return new novemberizing.rx.operator.Operator<T, U>(){
            @Override
            protected Task<T, U> on(Task<T, U> task) {
                if(f!=null){
                    task.out = f.call(task.in);
                }
                return task;
            }
        };
    }
}
