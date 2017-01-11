package i;

import i.operator.CompletionPort;
import i.operator.Just;
import i.operator.Sync;
import novemberizing.util.Debug;
import novemberizing.util.Log;

import static i.Iteration.IN;
import static i.Iteration.ON;
import static i.Iteration.OUT;

/**
 *
 * @author novemberizing, i@novemberizing.net
 * @since 2017. 1. 10.
 */
public abstract class Operator<T, U> implements i.func.Single<T, U> {

    public interface Func<T, U> extends i.func.Single<T, U> {}

    private static final String Tag = "Operator";

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
            protected Task<T> on(Task<T> task) {
                task.v.out(task.v.in = f.call(task.v.in));
                return task;
            }
        };
    }

    public static <T, U> Operator<T, ?> Sync(i.func.Single<T, U> f){
        if(f instanceof i.Operator){
            return (Operator<T, ?>) f;
        } else if(f!=null){
            return new Sync<T, U>(){
                @Override
                protected Task<T> on(Task<T> task) {
                    task.v.out(f.call(task.v.in));
                    return task;
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
                protected Task<T> on(Task<T> task) {
                    task.v.out(f.call(task.v.in));
                    return task;
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

    protected Operator<U, ?> __next;

    public Task<T> exec(Task<T> task) {
        Task<T> current = task;
        if(current!=null && current.__it==IN){
            current = in(task);
            if(current!=null) {
                current.__it = ++task.v.next;
            }
        }
        if(current!=null && current.__it==ON){
            current = on(task);
            if(current!=null) {
                current.__it = ++task.v.next;
            }
        }
        if(current!=null && current.__it==OUT){
            out(task);
        }
        return task;
    }

    protected Task<T> in(Task<T> task){
        task.v = new Local<>(task.i());
        return task;
    }

    protected abstract Task<T> on(Task<T> task);

    protected void out(Task<T> task){
        task.out(task.v.o());
        if (__next != null) {
            __next(task);
        } else {
            __up(task);
        }
    }

    protected void __next(Task<T> task){
        Log.f(Tag, "");
        Scheduler scheduler = task.__scheduler;
        if(scheduler==null){ scheduler = Scheduler.Local(); }
        scheduler.dispatch(new Task<>((U) task.o(), __next, scheduler, task.__previous));
    }

    protected void __up(Task<T> task){
        Log.f(Tag, "");
        if(task.__previous!=null){
            task.__previous.v.out(task.o());
            task.__previous.executed();
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

    @Override
    public U call(T first) {
        Log.f(Tag, "");
        Debug.On(new RuntimeException(""));
        return null;
    }
}
