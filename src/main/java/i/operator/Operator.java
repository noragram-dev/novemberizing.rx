package i.operator;

import i.Scheduler;
import novemberizing.util.Debug;
import novemberizing.util.Log;

import static i.operator.Iteration.IN;
import static i.operator.Iteration.ON;
import static i.operator.Iteration.OUT;

/**
 *
 * @author novemberizing, i@novemberizing.net
 * @since 2017. 1. 9.
 */
public abstract class Operator<T, U> implements i.Operator<T, U> {
    private static final String Tag = "Operator";

    protected Operator<U, ?> __next;

    protected Task<T> __in(Task<T> task){
        Log.f(Tag, "");
        if(task!=null) {
            task.__op = this;
            task.v = new Local<>(task.i());
            task.__it = Iteration.ON;
            return __on(task);
        }
        return null;
    }

    protected abstract Task<T> __on(Task<T> task);

    protected Task<T> __out(Task<T> task){
        Log.f(Tag, "");
        task.out(task.v.out);
        if(__next!=null){
            __next(task);
        } else {
            __up(task);
        }
        return task;
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
            task.__previous.executed();
        }
    }

    private Task<T> __unknown(Task<T> task){
        Log.f(Tag, "");
        Debug.On(new RuntimeException(""));
        return task;
    }

    @Override
    public U call(T first) {
        Log.f(Tag, "");
        Debug.On(new RuntimeException(""));
        return null;
    }

    @Override
    public Task<T> in(Task<T> task) {
        Log.f(Tag, "");
        switch(task.it()){
        case IN:    return __in(task);
        case ON:    return __on(task);
        case OUT:   return __out(task);
        default:    return __unknown(task);
        }
    }
}
