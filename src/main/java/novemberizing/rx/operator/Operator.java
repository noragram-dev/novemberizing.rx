package novemberizing.rx.operator;

import com.google.gson.annotations.Expose;
import novemberizing.ds.Func;
import novemberizing.ds.Tasks;
import novemberizing.rx.Scheduler;
import novemberizing.util.Log;

import java.util.Collection;
import java.util.LinkedList;

import static novemberizing.rx.operator.Iteration.IN;
import static novemberizing.rx.operator.Iteration.ON;
import static novemberizing.rx.operator.Iteration.OUT;

/**
 *
 * @author novemberizing, novemberizing@gmail.com
 * @since 2017. 1. 12.
 */
@SuppressWarnings({"DanglingJavadoc", "WeakerAccess"})
public abstract class Operator<T, U> implements novemberizing.rx.Operator<T, U> {
    private static final String Tag = "novemberizing.rx.operator.Operator";

    @Expose protected novemberizing.rx.Operator<U, ?> __next;

    protected Task<T, U> in(Task<T, U> task){
        Log.f(Tag, this, task);
        return task;
    }

    protected abstract Task<T, U> on(Task<T, U> task);

    protected Task<T, U> out(Task<T, U> task){
        Log.f(Tag, this, task);
        task.done(true);
        return task;
    }

    @Override
    public Task<T, U> call(Task<T, U> task) {
        if(task.it==IN){
            if(in(task)!=null){
                task.it = IN + 1;
            }
        }
        if(task.it==ON){
            if(on(task)!=null){
                task.it = ON + 1;
            }
        }
        if(task.it==OUT){
            if(out(task)!=null){
                task.it = OUT + 1;
            }
        }
        return task;
    }

    @Override
    public Task<T, U> build(T in, novemberizing.ds.Task<?> task){
        return new Task<>(in, this, task);
    }

    @Override
    public Task<U, ?> next(Task<?, U> task) {
        return __next!=null ? __next.build(task.out, task.parent()) : null;
    }

    @Override
    public novemberizing.rx.Operator<U, ?> next(){ return __next; }

    @Override
    public <V> novemberizing.rx.Operator<U, V> next(Func<U, V> f){
        if(__next!=null){
            Log.e(Tag, new RuntimeException("__next!=null"));
        }
        novemberizing.rx.Operator<U, V> op = Operator.Op(f);
        __next = op;
        return op;
    }

    public static <T, U> Task<T, U> Exec(Scheduler scheduler, Task<T, U> task){
        scheduler.dispatch(task);
        return task;
    }

    public static <T, U> Task<T, U> Exec(Task<T, U> task){
        return Exec(Scheduler.Self(), task);
    }

    public static <T> Exec<T> Exec(novemberizing.rx.Operator<T, ?> op, T o){
        Log.f(Tag, "Exec", op, o);
        return Exec(Scheduler.Self(), op, o);
    }

    public static <T> Exec<T> Exec(Scheduler scheduler, novemberizing.rx.Operator<T, ?> op, T o){
        Log.f(Tag, "Exec" ,op, o);
        Exec<T> task = new Exec<>(o, op);
        scheduler.dispatch(task);
        return task;
    }

    public static <T> novemberizing.rx.Operator<T, T> Just(){ return new Just<>(); }

    public static <T> novemberizing.rx.Operator<T, T> Single(Func<T, T> f){ return novemberizing.rx.Operator.Single(f); }

    public static <T, U> novemberizing.rx.Operator<T, U> Op(Func<T, U> f){ return novemberizing.rx.Operator.Op(f); }

    @SafeVarargs
    public static <T> Tasks Foreach(novemberizing.rx.Operator<T, ?> op, T o, T... items){
        return novemberizing.rx.Operator.Foreach(op, o, items);
    }

    public static <T> Tasks Foreach(novemberizing.rx.Operator<T, ?> op, T[] items){
        return novemberizing.rx.Operator.Foreach(op, items);
    }

    @SafeVarargs
    public static <T> Tasks Foreach(Scheduler scheduler, novemberizing.rx.Operator<T, ?> op, T o, T... items){
        return novemberizing.rx.Operator.Foreach(scheduler, op, o, items);
    }

    public static <T> Tasks Foreach(Scheduler scheduler, novemberizing.rx.Operator<T, ?> op, T[] items){
        return novemberizing.rx.Operator.Foreach(scheduler, op, items);
    }

    public static void main(String[] args){
        Log.depth(3);
        Log.disable(Log.HEADER | Log.FLOW);
        String[] strings = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

        novemberizing.rx.Operator<String, Integer> op = Op(o->(Integer.parseInt(o)+1));
        Foreach(op, strings);

        op.next(o->o+1)
                .next(o->o+1)
                .next(o->o+1)
                .next(o->o+1)
                .next(o->o+1)
                .next(o->o+1)
                .next(o->o+1)
                .next(o->o+1)
                .next(o->o+1)
                .next(o->o+1);

//        Exec(op, 1).on(o->Log.i("",o));
    }

}
