package i.operator;

import novemberizing.util.Log;

/**
 *
 * @author novemberizing, i@novemberizing.net
 * @since 2017. 1. 10.
 */
public abstract class Operator<T, U> implements i.Operator<T, U> {
    private static final String Tag = "Operator";

    public Operator(){
        Log.f(Tag, "");
    }

    protected Task<T> __in(Task<T> task){
        Log.f(Tag, "");
        task.v = new Local<>(task.i());
        task.__it = Iteration.On;
        return __on(task);
    }

    protected abstract Task<T> __on(Task<T> task);

    protected Task<T> __out(Task<T> task){
        Log.f(Tag, "");
        task.out(task.v.out);
        return task;
    }

    private Task<T> __unknown(Task<T> task){
        Log.f(Tag, "");
        throw new RuntimeException("");
    }

    @Override
    public U call(T first) {
        Log.f(Tag, "");
        throw new RuntimeException("");
    }

    @Override
    public Task<T> in(Task<T> task) {
        Log.f(Tag, "");
        switch (task.__it){
        case In:    return __in(task);
        case On:    return __on(task);
        case Out:   return __out(task);
        default:    return __unknown(task);
        }
    }
}
