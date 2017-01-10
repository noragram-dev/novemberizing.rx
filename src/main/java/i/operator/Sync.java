package i.operator;

import novemberizing.util.Debug;
import novemberizing.util.Log;

import java.util.LinkedList;

/**
 *
 * @author novemberizing, i@novemberizing.net
 * @since 2017. 1. 9.
 */
public class Sync<T, U> extends Operator<T, U> {
    private static final String Tag = "Sync";

    protected Func<T, U> __func;
    protected LinkedList<Task<T>> __tasks = new LinkedList<>();
    protected Task<T> __current;

    public Sync(){
        Log.f(Tag, "");
        __func = null;
        __current = null;
    }

    public Sync(Func<T, U> f){
        Log.f(Tag, "");
        __func = f;
        __current = null;
    }

    synchronized boolean running(){ return __current!=null; }

    @Override
    protected Task<T> __in(Task<T> task){
        Log.f(Tag, "");
        if(task!=null) {
            task.__op = this;
            task.v = new Local<>(task.i());
            task.__it = Iteration.ON;
            if (!running()) {
                synchronized (this) {
                    __current = task;
                }
                return __on(task);
            } else {
                synchronized (this) {
                    __tasks.addLast(task);
                }
                return task;
            }
        }
        return null;
    }

    @Override
    protected Task<T> __on(Task<T> task) {
        Log.f(Tag, "");
        task.v.out = call(task.v.in);
        task.__it = Iteration.OUT;
        return __out(task);
    }

    @Override
    protected Task<T> __out(Task<T> task){
        Log.f(Tag, "");
        task.out(task.v.out);
        if(__next!=null){
            __next(task);
        } else {
            __up(task);
        }
        synchronized (this){
            __current = __tasks.size()>0 ? __tasks.pollFirst() : null;
            if(__current!=null) {
                Log.i(Tag, "");
                __current.executed();
            }
        }
        return task;
    }

    @Override
    public U call(T first) {
        Log.f(Tag, "");
        if(__func==null){ Debug.On(new RuntimeException("")); }
        return __func!=null ? __func.call(first) : null;
    }
}
