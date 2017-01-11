package i;

import i.operator.*;
import novemberizing.util.Debug;
import novemberizing.util.Log;

import java.util.concurrent.locks.Condition;

import static i.Iteration.IN;

/**
 *
 * @author novemberizing, i@novemberizing.net
 * @since 2017. 1. 10.
 */
public abstract class Operator<T, U> implements i.func.Single<T, U> {

    public interface Func<T, U> extends i.func.Single<T, U> {}

    public static <T> Operator<T, T> Just(){ return new Just<>(); }

    public static <T> Operator<T, T> Just(i.func.Single<T, T> f){
        if(f instanceof i.Operator){
            return (Operator<T, T>) f;
        } else if(f==null){
            Debug.On(new RuntimeException(""));
            return Just();
        }
        return new Just<T>(){
            @Override
            protected Task<T> on(Task<T> task, T o) {
                return task.set(o, f.call(o));
            }
        };
    }

    public static <T, U> Operator<T, ?> Sync(i.func.Single<T, U> f){
        if(f instanceof i.Operator){
            return (Operator<T, ?>) f;
        } else if(f!=null){
            return new Sync<T, U>(){
                @Override
                protected Task<T> on(Task<T> task, T o) {
                    return task.set(o, f.call(o));
                }
            };
        } else {
            Debug.On(new RuntimeException(""));
            return Just();
        }
    }

    public static <T, U> Operator<T, ?> Op(i.func.Single<T, U> f) {
        if (f instanceof i.Operator) {
            return (Operator<T, ?>) f;
        } else if (f != null) {
            return new Operator<T, U>() {
                @Override
                protected Task<T> on(Task<T> task, T o) {
                    return task.set(o, f.call(o));
                }
            };
        } else {
            return Just();
        }
    }

    public static <T, U> Operator<T, ?> Op(i.func.Single<T, U> f, i.func.Single<?, ?>... functions){
        Operator<T, ?> ret = Op(f);
        ret.append(functions);
        return ret;
    }

    public static <T, U> Operator<T, ?> Op(i.func.Single<?, ?>[] functions){
        Operator<T, ?> ret = (Operator<T, U>) Op(functions[0]);
        ret.append(1, functions);
        return ret;
    }

    public static <T, U> CompletionPort<T, U> CompletionPort(i.func.Single<T, U> f){
        return new CompletionPort<>(f);
    }

    public static <T, U> Block<T, U> Block(i.func.Single<?, ?>[] functions){
        return (Block<T, U>) new Block<>(Op(functions));
    }

    public static <T, U> Block<T, ?> Block(i.func.Single<T, U> f) {
        return new Block<>(Op(f));
    }

    public static <T, U> Block<T, ?> Block(i.func.Single<T, U> f, i.func.Single<?, ?>... functions){
        return new Block<>(Op(f, functions));
    }

    public static <T, U> If<T, U> If(Operator.Func<T,Boolean> condition, i.func.Single<T, U> f){
        return new If<>(condition, f);
    }

    public static <T, U> Switch<T, U> Switch(Operator.Func<T, Integer> f){
        return new Switch<>(f);
    }

    public static <T> While<T> While(Operator.Func<T, Boolean> condition, i.func.Single<T, ?> op){
        return new While<>(condition, op);
    }

    public static <T> While<T> While(i.func.Single<T, ?> op, Operator.Func<T, Boolean> condition){
        return new While<>(op, condition);
    }

    public static <T, V> For<T, V> For(Operator.Func<T, V> initializer,i.func.Pair<T, V, Boolean> condition,i.func.Pair<T, V, V> op, i.func.Single<T, ?> block){
        return new For<>(initializer, condition, op, block);
    }

    protected Operator<U, ?> __next;

    synchronized public Task<T> exec(Task<T> task) {
        Task<T> current = task;
        if(current!=null && current.it()==IN){
            current = __iterate(in(task, task.v.in));
        }
        if(current!=null && current.__it==IN+1){
            current = __iterate(on(task, task.v.in));
        }
        if(current!=null && current.__it==IN+2){
            out(task, task.v.out);
        }
        return task;
    }

    protected Task<T> __iterate(Task<T> task){
        if(task!=null) {
            task.__it++;
        }
        return task;
    }



    protected Task<T> in(Task<T> task, T o){ return task.set(o, null); }

    protected abstract Task<T> on(Task<T> task, T o);

    protected void out(Task<T> task, Object o){
        task.out(o);
        if (__next != null) {
            __next(task);
        } else {
            __up(task);
        }
    }

    protected void __next(Task<T> task){
        Log.f("", this);
        Scheduler scheduler = task.__scheduler;
        if(scheduler==null){ scheduler = Scheduler.Local(); }
        scheduler.dispatch(new Task<>((U) task.o(), __next, scheduler, task.__previous));
    }

    protected void __up(Task<T> task){
        Log.f("", this);
        if(task.__previous!=null){
            task.__previous.up(task.__previous.v.in, task.o());
        }
    }

    protected Operator<?, ?> last(){
        Operator<?, ?> ret = __next;
        while(ret!=null && ret.__next!=null){ ret = ret.__next; }
        return ret;
    }

    public Operator<?, ?> append(i.func.Single<?, ?> f) {
        Operator<?, ?> current = last();
        if(current==null){
            current = __next = (Operator<U, ?>) Op(f);
        } else {
            current = current.append(Op(f));
        }
        return current;
    }

    public Operator<?, ?> append(i.func.Single<?, ?> f, i.func.Single<?, ?>... functions) {
        Operator<?, ?> current = append(f);
        for(i.func.Single<?, ?> function : functions) {
            if(function!=null) {
                current = current.append(Op(f));
            }
        }
        return current;
    }

    public Operator<?, ?> append(i.func.Single<?, ?>[] functions) {
        Operator<?, ?> current = append(functions[0]);
        for(int i = 1; i<functions.length;i++){
            if(functions[i]!=null) {
                current = current.append(Op(functions[i]));
            }
        }
        return current;
    }

    public Operator<?, ?> append(int index, i.func.Single<?, ?>[] functions) {
        Operator<?, ?> current = append(functions[index]);
        for(int i = index + 1; i<functions.length;i++){
            if(functions[i]!=null) {
                current = current.append(Op(functions[i]));
            }
        }
        return current;
    }

    public Local<T> declare(Task<T> task){
        return new Local<>(task.i());
    }

    @Override
    public U call(T first) {
        Log.f("", this);
        Debug.On(new RuntimeException(""));
        return null;
    }
}
