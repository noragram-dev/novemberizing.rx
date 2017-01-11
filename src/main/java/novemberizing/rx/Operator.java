package novemberizing.rx;

import novemberizing.rx.operator.*;
import novemberizing.util.Debug;
import novemberizing.util.Log;

import static novemberizing.rx.Iteration.IN;

/**
 *
 * @author novemberizing, novemberizing.rx@novemberizing.net
 * @since 2017. 1. 10.
 */
public abstract class Operator<T, U> implements novemberizing.rx.func.Single<T, U> {

    public interface Func<T, U> extends novemberizing.rx.func.Single<T, U> {}

    public static <T> Operator<T, T> Just(){ return new Just<>(); }

    public static <T> Operator<T, T> Just(novemberizing.rx.func.Single<T, T> f){
        if(f instanceof novemberizing.rx.Operator){
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

    public static <T, U> Operator<T, ?> Sync(novemberizing.rx.func.Single<T, U> f){
        if(f instanceof novemberizing.rx.Operator){
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

    public static <T, U> Operator<T, ?> Op(novemberizing.rx.func.Single<T, U> f) {
        if (f instanceof novemberizing.rx.Operator) {
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

    public static <T, U> Operator<T, ?> Op(novemberizing.rx.func.Single<T, U> f, novemberizing.rx.func.Single<?, ?>... functions){
        Operator<T, ?> ret = Op(f);
        ret.append(functions);
        return ret;
    }

    public static <T, U> Operator<T, ?> Op(novemberizing.rx.func.Single<?, ?>[] functions){
        Operator<T, ?> ret = (Operator<T, U>) Op(functions[0]);
        ret.append(1, functions);
        return ret;
    }

    public static <T, U> CompletionPort<T, U> CompletionPort(novemberizing.rx.func.Single<T, U> f){
        return new CompletionPort<>(f);
    }

    public static <T, U> Block<T, U> Block(novemberizing.rx.func.Single<?, ?>[] functions){
        return (Block<T, U>) new Block<>(Op(functions));
    }

    public static <T, U> Block<T, ?> Block(novemberizing.rx.func.Single<T, U> f) {
        return new Block<>(Op(f));
    }

    public static <T, U> Block<T, ?> Block(novemberizing.rx.func.Single<T, U> f, novemberizing.rx.func.Single<?, ?>... functions){
        return new Block<>(Op(f, functions));
    }

    public static <T, U> If<T, U> If(Operator.Func<T,Boolean> condition, novemberizing.rx.func.Single<T, U> f){
        return new If<>(condition, f);
    }

    public static <T, U> Switch<T, U> Switch(Operator.Func<T, Integer> f){
        return new Switch<>(f);
    }

    public static <T> While<T> While(Operator.Func<T, Boolean> condition, novemberizing.rx.func.Single<T, ?> op){
        return new While<>(condition, op);
    }

    public static <T> While<T> While(novemberizing.rx.func.Single<T, ?> op, Operator.Func<T, Boolean> condition){
        return new While<>(op, condition);
    }

    public static <T, V> For<T, V> For(Operator.Func<T, V> initializer, novemberizing.rx.func.Pair<T, V, Boolean> condition, novemberizing.rx.func.Pair<T, V, V> op, novemberizing.rx.func.Single<T, ?> block){
        return new For<>(initializer, condition, op, block);
    }

    protected Operator<U, ?> __next;

    synchronized public Task<T> exec(Task<T> task) {
        Task<T> current = task;
        if(current!=null && !current.done() && current.it()==IN){
            current = __iterate(in(task, task.v.in));
        }
        if(current!=null && !current.done() && current.__it==IN+1){
            current = __iterate(on(task, task.v.in));
        }
        if(current!=null && !current.done() && current.__it==IN+2){
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

    protected void error(Task<T> task, Throwable e){
        task.error(e);
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

    public Operator<?, ?> append(novemberizing.rx.func.Single<?, ?> f) {
        Operator<?, ?> current = last();
        if(current==null){
            current = __next = (Operator<U, ?>) Op(f);
        } else {
            current = current.append(Op(f));
        }
        return current;
    }

    public Operator<?, ?> append(novemberizing.rx.func.Single<?, ?> f, novemberizing.rx.func.Single<?, ?>... functions) {
        Operator<?, ?> current = append(f);
        for(novemberizing.rx.func.Single<?, ?> function : functions) {
            if(function!=null) {
                current = current.append(Op(f));
            }
        }
        return current;
    }

    public Operator<?, ?> append(novemberizing.rx.func.Single<?, ?>[] functions) {
        Operator<?, ?> current = append(functions[0]);
        for(int i = 1; i<functions.length;i++){
            if(functions[i]!=null) {
                current = current.append(Op(functions[i]));
            }
        }
        return current;
    }

    public Operator<?, ?> append(int index, novemberizing.rx.func.Single<?, ?>[] functions) {
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
