package novemberizing.rx.operator;


import com.google.gson.annotations.Expose;
import novemberizing.ds.On;
import novemberizing.util.Log;

import static novemberizing.rx.operator.Iteration.IN;

/**
 *
 * @author novemberizing
 * @since 2017. 1. 13.
 */
public class Task<T, U> extends novemberizing.ds.Task<T> {
    private static final String Tag = "novemberizing.rx.operator.Task";

    @Expose public int it;
    @Expose public U out;
    @Expose protected novemberizing.rx.Operator<T, U> __op;
    @Expose protected On<Object> __onChildCompleted;

    public Task(T o, novemberizing.rx.Operator<T, U> op, novemberizing.ds.Task<?> parent) {
        super(o, parent);
        it = IN;
        __op = op;
        out = null;
    }

    public Task<T, U> setOnChildCompleted(On onChildCompleted){
        __onChildCompleted = onChildCompleted;
        return this;
    }

    @Override
    synchronized public void execute() {
        Log.f(Tag, "execute", this);

        Task<T, U> ret = __op.call(this);
        if(ret!=null && ret.done()){
            completed();
            if(__op.next()!=null) {
                try {
                    Operator.Exec(__op.next(this));
                } catch(Exception e){
                    e.printStackTrace();
                }
            } else if(__parent!=null) {
                __parent.onChildCompleted(this);
            }
        } else {
            // Log.i(Tag, "2");
        }
    }

    @Override
    public <V extends novemberizing.ds.Task> void onChildCompleted(V o){
        Log.f(Tag, this, o);
        synchronized (this) {
            __children.remove(o);
        }
        if(__onChildCompleted!=null) {

            __onChildCompleted.on(o);
        } else {
            super.onChildCompleted(o);
        }
    }
}
