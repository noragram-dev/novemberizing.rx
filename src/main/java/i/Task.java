package i;

import com.google.gson.annotations.Expose;
import novemberizing.util.Debug;
import novemberizing.util.Log;

import static i.Iteration.IN;

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
        __it = IN;
        __op = op;
        v = __op.declare(this);
        __out = null;
        __exception = null;
        __scheduler = scheduler;
        __previous = null;
    }

    public Task(T in, i.Operator<T, ?> op, Scheduler scheduler , Task<?> previous){
        Log.f(Tag, "");
        __in = in;
        __it = IN;
        __op = op;
        v = __op.declare(this);
        __out = null;
        __exception = null;
        __scheduler = scheduler;
        __previous = previous;
    }

    public void out(){
        Log.f(Tag, "");
        synchronized (this) {
            if (__it == __op.done()) {
                Log.e("" ,new RuntimeException(""));
            } else {
                __it = __op.done();
                complete();
            }
        }
    }

    public void out(Object o){
        Log.f(Tag, "");
        synchronized (this) {
            if (__it == __op.done()) {
                Log.e("" ,new RuntimeException(""));
            } else {
                __it = __op.done();
                __out = new i.tuple.Single<>(o);
                complete();
            }
        }
    }

    public void error(Throwable e){
        Log.f(Tag, "");
        synchronized (this) {
            if (__it == __op.done()) {
                Log.e("" ,new RuntimeException(""));
            } else {
                __it = __op.done();
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
        __it++;
        move();
    }

    synchronized public void next(Object in, Object out){
        __it++;
        v.set(__it, in, out);
        move();
    }

    synchronized public void back(){
        __it--;
        if(__it< IN){ __it = IN; }
        move();
    }

    synchronized public void back(Object in, Object out){
        __it--;
        if(__it< IN){ __it = IN; }
        v.set(__it, in, out);
        move();
    }

    synchronized public void up(){
        if(__previous!=null){
            __previous.executed();
        }
    }

    synchronized public void up(Object in, Object out){
        if(__previous!=null){
            __previous.v.set(__previous.it(), in, out);
            __previous.executed();
        }
    }

    public Task<T> set(Object in, Object out){
        v.set(__it, in, out);
        return this;
    }

    private void move(){ executed(); }

    synchronized public int it(){ return __it; }
    synchronized public void it(int it){ __it = it; }
    synchronized public boolean done(){ return __it==__op.done(); }

    public Scheduler scheduler(){ return __scheduler; }

    @Override
    public void execute() {
        Log.f("", this);
        __op.exec(this);
    }
}
