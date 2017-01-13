package novemberizing.ds;

import com.google.gson.annotations.Expose;
import novemberizing.util.Log;

import java.util.HashSet;
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
    protected novemberizing.ds.Task<?> __parent;
    @Expose protected LinkedList<On<Object>> __onCompleteds;
    @Expose protected novemberizing.ds.Task<?> __o;
    protected final HashSet<Task<?>> __children = new HashSet<>();

    public Task(T o) {
        in = o;
        __done = false;
        __parent = null;

    }

    public Task(T o, novemberizing.ds.Task<?> parent) {
        in = o;
        __done = false;
        __parent = parent;

        if(__parent!=null){
            synchronized (__parent.__children) {
                __parent.__children.add(this);
            }
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

    public <U extends Task> void onChildCompleted(U o){
        Log.f(Tag, this, o);
        __o = o;
        synchronized (__children){
            __children.remove(o);
            if(__children.size()==0){ __done = true; }
        }
        if(__done) {
            completed();
        } else {
            executed();
        }
    }
}
