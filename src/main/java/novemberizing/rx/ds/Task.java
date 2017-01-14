package novemberizing.rx.ds;

import com.google.gson.annotations.Expose;
import novemberizing.util.Log;

import java.util.HashSet;
import java.util.LinkedList;

/**
 *
 * @author novemberizing, i@novemberizing.net
 * @since 2017. 1. 13.
 */
@SuppressWarnings({"WeakerAccess", "SameParameterValue", "DanglingJavadoc"})
public abstract class Task<T> extends Command {
    private static final String Tag = "Task";

    @Expose public final T in;
    @Expose public Object out;
    @Expose protected boolean __done;

    protected Task __parent;

    protected final LinkedList<On<Object>> __onCompleteds = new LinkedList<>();
    protected final HashSet<Task> __children = new HashSet<>();

    public Task(T o) {
        Log.f(Tag, this, o);
        in = o;
        out = null;
        __done = false;

        __parent = null;
    }

    public Task(T o, novemberizing.rx.ds.Task parent) {
        Log.f(Tag, this, o, parent);
        in = o;
        out = null;
        __done = false;

        __parent = parent;

        if(__parent!=null){
            __parent.add(this);
        }
    }

    public Task parent(){
        Log.f(Tag, this);
        return __parent;
    }
    public void add(novemberizing.rx.ds.Task child){
        Log.f(Tag, this, child);
        __children.add(child);
    }

    public boolean done() {
        Log.f(Tag, this);
        return __done;
    }

    public void done(boolean v) {
        Log.f(Tag, this, v);
        __done = v;
    }

    public Task<T> addOnCompleted(On<Object> onCompleted){
        if(onCompleted!=null) {
            __onCompleteds.addLast(onCompleted);
            if (__done) {
                try {
                    onCompleted.on(out);
                } catch(ClassCastException e){
                    e.printStackTrace();
                }
            }
        }
        return this;
    }

    public void onChildCompleted(Task child){
        Log.f(Tag, this, child);
        synchronized (__children){
            __children.remove(child);
            if(__children.size()==0){ __done = true; }
        }
        /**
         * ToDo
         */
        if(__done) {
            completed();
        } else {
            executed();
        }
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

    @Override
    public void completed(){
        super.completed();
        onCompleted(out);
    }
}
