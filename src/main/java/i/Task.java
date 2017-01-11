package i;

import com.google.gson.annotations.Expose;
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
    @Expose protected boolean __done;

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
        __done = false;
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
        __done = false;
    }

    public void out(){
        Log.f(Tag, "");
        synchronized (this) {
            if (__done) {
                Debug.On(new RuntimeException(""));
            } else {
                __done = true;
                complete();
            }
        }
    }

    public void out(Object o){
        Log.f(Tag, "");
        synchronized (this) {
            if (__done) {
                Debug.On(new RuntimeException(""));
            } else {
                __done = true;
                __out = new i.tuple.Single<>(o);
                complete();
            }
        }
    }

    public void error(Throwable e){
        Log.f(Tag, "");
        synchronized (this) {
            if (__done) {
                Debug.On(new RuntimeException(""));
            } else {
                __done = true;
                __exception = e;
                complete();
            }
        }
    }

    public T i(){ return __in; }

    public Throwable e(){ return __exception; }

    public Object o(){
        Log.f(Tag, "");
        if (done()) {
            if(__out!=null) {
                return __out.first;
            }
        } else {
            Debug.On(new RuntimeException(""));
        }
        return null;
    }

    public <U> U o(Class<U> c){
        Log.f(Tag, "");
        if(done()) {
            try {
                if(__out!=null) {
                    return c.cast(__out.first);
                }
            } catch (Exception e) {
                Debug.On(e);
            }
        } else {
            Debug.On(new RuntimeException(""));
        }
        return null;
    }

    synchronized public void next(){
        synchronized (this){ __it = (v!=null ? ++v.next : __it); }
        move();
    }

    private static void move() {
    }

    synchronized protected int it(){ return __it; }
    synchronized public boolean done(){ return __done; }

    public Scheduler scheduler(){ return __scheduler; }

    @Override
    public void execute() {
        Log.f(Tag, "");
        __op.exec(this);
    }
}
