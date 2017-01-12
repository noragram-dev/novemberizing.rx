package novemberizing.rx;

import com.google.gson.annotations.Expose;
import novemberizing.rx.tuple.Single;
import novemberizing.util.Debug;
import novemberizing.util.Log;

/**
 *
 * @author novemberizing, novemberizing.rx@novemberizing.net
 * @since 2017. 1. 9.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Task<T> extends Command {
    private static final String Tag = "task";
    @Expose protected T __in;
    @Expose protected int __it;
    @Expose protected Operator<T, ?> __op;
    @Expose protected Single<Object> __out;
    @Expose protected Throwable __exception;
    @Expose protected Scheduler __scheduler;
    @Expose protected Task<?> __previous;
    @Expose protected boolean __done;
    @Expose public Local<T> v;

    public Task(T in, Scheduler scheduler){
        Log.f(Tag, "");
        __in = in;
        __it = Iteration.IN;
        __op = null;
        v = null;
        __out = null;
        __exception = null;
        __scheduler = scheduler;
        __previous = null;
        __done = false;
    }

    public Task(T in, Scheduler scheduler , Task<?> previous){
        Log.f(Tag, "");
        __in = in;
        __it = Iteration.IN;
        __op = null;
        v = null;
        __out = null;
        __exception = null;
        __scheduler = scheduler;
        __previous = previous;
        __done = false;
    }

    public Task(T in, Operator<T, ?> op, Scheduler scheduler){
        Log.f(Tag, "");
        __in = in;
        __it = Iteration.IN;
        __op = op;
        v = op.declare(this);
        __out = null;
        __exception = null;
        __scheduler = scheduler;
        __previous = null;
        __done = false;
    }

    public Task(T in, Operator<T, ?> op, Scheduler scheduler , Task<?> previous){
        Log.f(Tag, "");
        __in = in;
        __it = Iteration.IN;
        __op = op;
        v = op.declare(this);
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
                Log.e(Tag, new RuntimeException(""));
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
                Log.e(Tag, new RuntimeException(""));
            } else {
                __done = true;
                __out = new novemberizing.rx.tuple.Single<>(o);
                complete();
            }
        }
    }

    public void exit(){
        Task<?> current = __previous;
        while(current!=null){
            if(!current.done()) {
                current.__done = true;
                if(__op!=null){ __op.out(this, null); }
                current.complete();
            }
            current = current.__previous;
        }
    }

    public void exit(Object o){
        Task<?> current = __previous;
        while(current!=null){
            if(!current.done()) {
                current.__done = true;
                if(current.__previous!=null){ current.__out = new Single<>(o); }
                if(__op!=null){ __op.out(this, null); }
                current.complete();
            }
            current = current.__previous;
        }
    }

    public void error(Throwable e){
        Task<?> current = __previous;
        while(current!=null){
            current.__exception = e;
            if(!current.done()) {
                current.__done = true;
                if(__op!=null){ __op.out(this, null); }
                current.complete();
            }
            current = current.__previous;
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

    synchronized public Task<T> next(){
        __it++;
        return move();
    }

    synchronized public Task<T> next(Object in, Object out){
        __it++;
        set(in, out);
        return move();
    }

    synchronized public Task<T> up(){ return move(); }

    synchronized protected Task<T> up(Object in, Object out){
        set(in, out);
        return move();
    }

    synchronized public <C> Task<C> down(Operator<C, ?> op, C o){
        Scheduler scheduler = __scheduler==null ? Scheduler.Local() : __scheduler;
        Task<C> down = new Task<>(o, op, scheduler, this);
        scheduler.dispatch(down);
        return down;
    }

    private Task<T> move() {
        if(__done){
            complete();
        } else {
            executed();
        }
        return this;
    }

    synchronized public Task<T> set(Object in, Object out){
        v.set(__it, in, out);
        return this;
    }

    synchronized public int it(){ return __it; }
    synchronized public boolean done(){ return __done; }

    public Scheduler scheduler(){ return __scheduler; }

    public void cancel(){}

    @Override
    public void execute() {
        Log.f(Tag, "");
        if(__op!=null) {
            __op.exec(this);
        } else {
            complete();
        }
    }
}
