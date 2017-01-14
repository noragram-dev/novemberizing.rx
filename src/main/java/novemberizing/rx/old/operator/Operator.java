package novemberizing.rx.old.operator;

import com.google.gson.annotations.Expose;
import novemberizing.rx.ds.On;
import novemberizing.rx.ds.Tasks;
import novemberizing.rx.old.des.Scheduler;
import novemberizing.rx.old.task.Exec;
import novemberizing.rx.old.task.Task;
import novemberizing.util.Log;

import static novemberizing.rx.ds.Iteration.IN;
import static novemberizing.rx.ds.Iteration.ON;
import static novemberizing.rx.ds.Iteration.OUT;

/**
 *
 * @author novemberizing, novemberizing@gmail.com
 * @since 2017. 1. 12.
 */
@SuppressWarnings({"DanglingJavadoc", "WeakerAccess"})
public abstract class Operator<T, U> implements novemberizing.rx.old.des.Operator<T, U> {
    private static final String Tag = "novemberizing.rx.old.operator.Operator";

    @Expose protected novemberizing.rx.old.des.Operator<U, ?> __next;

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
    public U call(T o){
        Log.e(Tag, new RuntimeException(""));
        return null;
    }

    @Override
    public Task<T, U> exec(Task<T, U> task) {
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
    public Task<T, U> build(T in, novemberizing.rx.ds.Task<?> task){
        return new Task<>(in, this, task);
    }

    @Override
    public Task<U, ?> next(Task<?, U> task) {
        return __next!=null ? __next.build(task.out, task.parent()) : null;
    }

    @Override
    public novemberizing.rx.old.des.Operator<U, ?> next(){ return __next; }

    @Override
    public <V> novemberizing.rx.old.des.Operator<U, V> next(novemberizing.rx.ds.Func<U, V> f){
        if(__next!=null){
            Log.e(Tag, new RuntimeException("__next!=null"));
        }
        novemberizing.rx.old.des.Operator<U, V> op = (novemberizing.rx.old.des.Operator<U, V>) Operator.Op(f);
        __next = op;
        return op;
    }

//    @Override
//    public novemberizing.rx.old.des.Operator<?,?> next(Func<?, ?> f){
//        if(__next!=null){
//            Log.e(Tag, new RuntimeException("__next!=null"));
//        }
//        novemberizing.rx.old.des.Operator<U, ?> op = null;
//        try {
//            op = (novemberizing.rx.old.des.Operator<U, ?>) Operator.Op(f);
//        } catch(Exception e){
//            Log.e(Tag, e);
//        }
//        __next = op;
//        return op;
//    }

    public static <T, U> Task<T, U> DefaultOn(Task<T, U> task, novemberizing.rx.ds.Func<T, U> f) {
        if(f instanceof novemberizing.rx.old.des.Operator){
//            novemberizing.rx.old.des.Operator<T, T> op = (novemberizing.rx.old.des.Operator<T, T>) f;
//            novemberizing.rx.old.des.Operator.Exec(op.build(task.in, task)).on(new On<novemberizing.rx.ds.Task>() {
//                @Override
//                public void on(novemberizing.rx.ds.Task v) {
////                    Log.i(Tag, v);
////                    task.next();
//                }
//            });
//                    task.setOnChildCompleted(new On<Task<T, U>>() {
//                        @Override
//                        public void on(Task<T, U> v) {
//                            Log.i("", v);
//                        }
//                    });
            return null;
        } else if(f!=null){
            task.out = f.call(task.in);
        }
        return task;
    }

    public static <T, U> Task<T, U> Exec(Scheduler scheduler, Task<T, U> task){
        scheduler.dispatch(task);
        return task;
    }

    public static <T, U> Task<T, U> Exec(Task<T, U> task){
        return Exec(Scheduler.Self(), task);
    }

    public static <T> Exec<T> Exec(novemberizing.rx.old.des.Operator<T, ?> op, T o){
        Log.f(Tag, "Exec", op, o);
        return Exec(Scheduler.Self(), op, o);
    }

    public static <T> Exec<T> Exec(Scheduler scheduler, novemberizing.rx.old.des.Operator<T, ?> op, T o){
        Log.f(Tag, "Exec" ,op, o);
        Exec<T> task = new Exec<>(o, op);
        scheduler.dispatch(task);
        return task;
    }

    public static <T> novemberizing.rx.old.des.Operator<T, T> Just(){ return new Just<>(); }

    public static <T> novemberizing.rx.old.des.Operator<T, T> Single(novemberizing.rx.ds.Func<T, T> f){ return novemberizing.rx.old.des.Operator.Single(f); }

    public static <T, U> novemberizing.rx.old.des.Operator<T, U> Op(novemberizing.rx.ds.Func<T, U> f){ return novemberizing.rx.old.des.Operator.Op(f); }

    public static <T, U> novemberizing.rx.old.des.Operator<T, U> Op(novemberizing.rx.ds.Func<T, U> f, novemberizing.rx.ds.Func<?, ?>... functions){
        return novemberizing.rx.old.des.Operator.Op(f, functions);
    }

    static <T> novemberizing.rx.old.des.Operator<T, ?> Op(novemberizing.rx.ds.Func<?, ?>[] functions){
        return novemberizing.rx.old.des.Operator.Op(functions);
    }

    public static CompletionPort CompletionPort(){ return novemberizing.rx.old.des.Operator.CompletionPort(); }

    public static <T> If<T, ?> If(Condition<T> condition, novemberizing.rx.ds.Func<T, ?> f){ return novemberizing.rx.old.des.Operator.If(condition,f); }

    @SafeVarargs
    public static <T> Tasks Foreach(novemberizing.rx.old.des.Operator<T, ?> op, T o, T... items){
        return novemberizing.rx.old.des.Operator.Foreach(op, o, items);
    }

    public static <T> Tasks Foreach(novemberizing.rx.old.des.Operator<T, ?> op, T[] items){
        return novemberizing.rx.old.des.Operator.Foreach(op, items);
    }

    @SafeVarargs
    public static <T> Tasks Foreach(Scheduler scheduler, novemberizing.rx.old.des.Operator<T, ?> op, T o, T... items){
        return novemberizing.rx.old.des.Operator.Foreach(scheduler, op, o, items);
    }

    public static <T> Tasks Foreach(Scheduler scheduler, novemberizing.rx.old.des.Operator<T, ?> op, T[] items){
        return novemberizing.rx.old.des.Operator.Foreach(scheduler, op, items);
    }

    public static void main(String[] args){
        Log.depth(3);
        Log.disable(Log.HEADER | Log.FLOW);
        String[] strings = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

        novemberizing.rx.old.des.Operator<String,Integer> op = Op((String o)->(Integer.parseInt(o)+1));
        Foreach(op, strings);

        op.next((Integer o)->o+1)
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
