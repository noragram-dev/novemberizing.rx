package novemberizing.ds;

import com.google.gson.annotations.Expose;
import novemberizing.rx.Command;
import novemberizing.util.Log;

import java.util.LinkedList;

/**
 *
 * @author novemberizing, i@novemberizing.net
 * @since 2017. 1. 13.
 */
@SuppressWarnings({"WeakerAccess", "SameParameterValue"})
public abstract class Task<T> extends Command {
    private static final String Tag = "Task";
    @Expose public final T in;
    @Expose protected boolean __done;
    @Expose protected novemberizing.ds.Task<?> __parent;
    @Expose protected LinkedList<On<Object>> __onCompleteds;
    @Expose protected Object __o;
    protected novemberizing.ds.Task<?> __child;

    public Task(T o) {
        in = o;
        __done = false;
        __parent = null;
        __child = null;

    }

    public Task(T o, novemberizing.ds.Task<?> parent) {
        in = o;
        __done = false;
        __parent = parent;
        __child = null;


        if(__parent!=null){
            __parent.__child = this;
        }
    }

    public novemberizing.ds.Task<?> parent(){ return __parent; }

    public boolean done() {
        return __done;
    }

    public void done(boolean v) {
        Log.f("", this);
        __done = v;
    }

    public void onCompleted(Object o){
        if(__onCompleteds!=null){
            for(On<Object> on : __onCompleteds){
                if(on!=null) {
                    on.on(o);
                }
            }
        }
    }

    public Task<T> on(On<Object> on){
        if(on!=null) {
            if(__onCompleteds==null){
                __onCompleteds = new LinkedList<>();
            }
            __onCompleteds.addLast(on);
            if (__done) {
                try {
                    on.on(__o);
                } catch(ClassCastException e){
                    e.printStackTrace();
                }
            }
        }
        return this;
    }

    @Override
    public void completed(){
        super.completed();
        onCompleted(__o);
    }

    public <U> void onChildCompleted(U o){
        Log.f(Tag, this, o);
        __o = o;
        __child = null;
        if(__done) {
            completed();
        } else {
            executed();
        }
    }
}
