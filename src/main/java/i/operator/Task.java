package i.operator;

import com.google.gson.annotations.Expose;
import i.Command;
import i.Scheduler;
import novemberizing.util.Debug;
import novemberizing.util.Log;

/**
 *
 * @author novemberizing, i@novemberizing.net
 * @since 2017. 1. 9.
 */
public class Task<T> extends Command {
    private static final String Tag = "task";
    @Expose protected T __in;
    @Expose protected int __it;
    @Expose protected i.Operator<T, ?> __op;
    @Expose protected i.tuple.Single<Object> __out;
    @Expose protected Throwable __exception;
    @Expose protected Scheduler __scheduler;
    @Expose protected Task<?> __previous;
    @Expose public Local<T> v;

    public Task(T in, i.Operator<T, ?> op, Scheduler scheduler){
        Log.f(Tag, "");
        __in = in;
        __it = Iteration.IN;
        __op = op;
        v = null;
        __out = null;
        __exception = null;
        __scheduler = scheduler;
        __previous = null;
    }

    public Task(T in, i.Operator<T, ?> op, Scheduler scheduler , Task<?> previous){
        Log.f(Tag, "");
        __in = in;
        __it = Iteration.IN;
        __op = op;
        v = null;
        __out = null;
        __exception = null;
        __scheduler = scheduler;
        __previous = previous;
    }

    public void out(){
        Log.f(Tag, "");
        synchronized (this) {
            if (__it == Iteration.DONE) {
                Debug.On(new RuntimeException(""));
            } else {
                __it = Iteration.DONE;
                complete();
            }
        }
    }

    public void out(Object o){
        Log.f(Tag, "");
        synchronized (this) {
            if (__it == Iteration.DONE) {
                Debug.On(new RuntimeException(""));
            } else {
                __it = Iteration.DONE;
                __out = new i.tuple.Single<>(o);
                complete();
            }
        }
    }

    public void error(Throwable e){
        Log.f(Tag, "");
        synchronized (this) {
            if (__it == Iteration.DONE) {
                Debug.On(new RuntimeException(""));
            } else {
                __it = Iteration.DONE;
                __exception = e;
                complete();
            }
        }
    }

    public T i(){ return __in; }

    public Throwable e(){ return __exception; }

    public Object o(){ return __out; }

    public <U> U o(Class<U> c){
        Log.f(Tag, "");
        if(!done()) {
            try {
                return c.cast(__out.first);
            } catch (Exception e) {
                Debug.On(e);
            }
        }
        return null;
    }

    public void next(){
        Log.f(Tag, "");
        if(++__it==Iteration.DONE){
            complete();
        } else {
            executed();
        }
    }

    synchronized protected int it(){ return __it; }
    synchronized protected void it(int it){ __it = it; }
    synchronized public boolean done(){ return __it==Iteration.DONE; }

    @Override
    public void execute() {
        Log.f(Tag, "");
        __op.in(this);
    }
}
