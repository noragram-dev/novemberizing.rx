package novemberizing.rx.old.des;

import novemberizing.rx.ds.On;
import novemberizing.rx.ds.Tasks;

import novemberizing.rx.old.operator.Just;
import novemberizing.rx.old.operator.Sync;
import novemberizing.rx.old.operator.Single;
import novemberizing.rx.old.operator.CompletionPort;
import novemberizing.rx.old.operator.If;
import novemberizing.rx.old.task.Exec;
import novemberizing.rx.old.task.Task;
import novemberizing.util.Log;

import java.util.LinkedList;

/**
 *
 * @author novemberizing, novemberizing@gmail.com
 * @since 2017. 1. 12.
 */
public interface Operator<T, U> extends novemberizing.rx.ds.Func<T, U>, novemberizing.rx.ds.Exec<Task<T, U>, Task<T, U>> {
    String Tag = "novemberizing.rx.old.des.Operator";

    interface Func<T, U> extends novemberizing.rx.ds.Func<T, U>{}
    interface Condition<T> extends novemberizing.rx.ds.Func<T, Boolean>{}

    static <T, U> Task<T, U> DefaultOn(Task<T, U> task, novemberizing.rx.ds.Func<T, U> f) {
        if(f instanceof Operator){
            Operator<T, T> op = (Operator<T, T>) f;
//            Operator.Exec(op.build(task.in, task)).on(new On<novemberizing.rx.ds.Task>() {
//                @Override
//                public void on(novemberizing.rx.ds.Task v) {
//                    Log.i(Tag, v);
//                    task.next();
//                }
//            });
            return null;
        } else if(f!=null){
            task.out = f.call(task.in);
        }
        return task;
    }

    Task<T, U> build(T in, novemberizing.rx.ds.Task<?> task);

    Task<U, ?> next(Task<?, U> task);

    Operator<U, ?> next();

    <V> Operator<U, V> next(novemberizing.rx.ds.Func<U, V> f);

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
    static <T> Tasks Foreach(Operator<T, ?> op, T o, T... items){
        Log.f(Tag, "Foreach", op, o);
        return Foreach(Scheduler.Self(), op, o, items);
    }

    static <T> Tasks Foreach(Operator<T, ?> op, T[] items){
        Log.f(Tag, "Foreach", op);
        return Foreach(Scheduler.Self(), op, items);
    }

    @SafeVarargs
    static <T> Tasks Foreach(Scheduler scheduler, Operator<T, ?> op, T o, T... items){
        Log.f(Tag, "Foreach" ,op, o, items);
        LinkedList<novemberizing.rx.ds.Task> collection = new LinkedList<>();
        collection.add(Exec(op, o));
        for(T item : items){
            collection.add(Exec(op, item));
        }
        return new Tasks(collection);
    }

    static <T> Tasks Foreach(Scheduler scheduler, Operator<T, ?> op, T[] items){
        Log.f(Tag, "Foreach" ,op, items);
        LinkedList<novemberizing.rx.ds.Task> collection = new LinkedList<>();
        for(T item : items){
            collection.add(Exec(op, item));
        }
        return new Tasks(collection);
    }

    static <T> Operator<T, T> Just(){ return new Just<>(); }

    static <T> Operator<T, T> Single(novemberizing.rx.ds.Func<T, T> f){
        return new Single<T>(){
            @Override
            protected Task<T, T> on(Task<T, T> task) {
                return DefaultOn(task, f);
            }
        };
    }

    static <T, U> Operator<T, U> Op(novemberizing.rx.ds.Func<T, U> f){
        if(f==null){
            Log.e(Tag, "f==null");
        }
        return new novemberizing.rx.old.operator.Operator<T, U>(){
            @Override
            protected Task<T, U> on(Task<T, U> task) {
                return DefaultOn(task, f);
            }
        };
    }

    static <T, U> Operator<T, U> Op(novemberizing.rx.ds.Func<T, U> f, novemberizing.rx.ds.Func<?, ?>... functions){
        Operator<T, U> op = Op(f);
        Operator<?, ?> operator = op;
        for(novemberizing.rx.ds.Func<?, ?> function : functions){
            try {
                operator = operator.next((novemberizing.rx.ds.Func) function);
            } catch(Exception e){
                e.printStackTrace();
            }
        }
        return op;
    }

    static <T> Operator<T, ?> Op(novemberizing.rx.ds.Func<?, ?>[] functions){
        Operator<T, ?> op = null;
        if(functions.length>0) {
            try {
                op = (Operator<T, ?>) Op((novemberizing.rx.ds.Func) functions[0]);
                Operator<?, ?> operator = op;
                for (int i = 1;i<functions.length;i++) {
                    try {
                        operator = operator.next((novemberizing.rx.ds.Func) functions[i]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        } else {
            Log.e(Tag, new RuntimeException(""));
        }
        return op;
    }


    static <T, U> Sync<T, U> Sync(novemberizing.rx.ds.Func<T, U> f){
        return new Sync<T, U>() {
            @Override
            protected Task<T, U> on(Task<T, U> task) {
                return DefaultOn(task, f);
            }
        };
    }

    static CompletionPort CompletionPort(){
        return new CompletionPort();
    }

    static <T> If<T, ?> If(Condition<T> condition, novemberizing.rx.ds.Func<T, ?> f){
        return new If<>(condition, f);
    }
}
