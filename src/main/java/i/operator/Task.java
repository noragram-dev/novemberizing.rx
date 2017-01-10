package i.operator;

import com.google.gson.annotations.Expose;
import novemberizing.util.Debug;
import novemberizing.util.Log;

/**
 *
 * @author novemberizing, i@novemberizing.net
 * @since 2017. 1. 10.
 */
public class Task<T> {
    private static final String Tag = "Task";

    @Expose protected T __in;
    @Expose protected Iteration __it;
    @Expose protected i.tuple.Single<Object> __out;
    @Expose protected Throwable __e;
    @Expose public  Local<T> v;

    public Task(T o){
        Log.f(Tag, "");
        __in = o;
        __it = Iteration.In;
        __out = null;
        __e = null;
        v = null;
    }

    public Iteration it(){ return __it; }

    public T i(){ return __in; }

    public Object o(){
        Log.f(Tag, "");
        if(__out==null){ Debug.On(new RuntimeException("")); }
        return __out!=null ? __out.first : null;
    }

    public <O> O o(Class<O> c){
        Log.f(Tag, "");
        if(__out==null){ Debug.On(new RuntimeException("")); }
        try {
            return __out!=null ? c.cast(__out.first) : null;
        } catch(ClassCastException e){
            Debug.On(e);
            return null;
        }
    }

    public Throwable e(){ return __e; }

    synchronized public boolean done(){ return __it==Iteration.Done; }

    synchronized protected void it(Iteration it){ __it = it; }

    synchronized protected void out(Object o){
        Log.f(Tag, "");
        if(__it!=Iteration.Done){
            __it = Iteration.Done;
            __out = new i.tuple.Single<>(o);

        } else {
            Debug.On(new RuntimeException(""));
        }
    }

    synchronized protected void out(){
        Log.f(Tag, "");
        if(__it!=Iteration.Done){
            __it = Iteration.Done;
        } else {
            Debug.On(new RuntimeException(""));
        }
    }

    synchronized protected void error(Throwable e){
        Log.f(Tag, "");
        if(__it!=Iteration.Done){
            __it = Iteration.Done;
            __e = e;
        } else {
            Debug.On(new RuntimeException(""));
        }
    }
}
