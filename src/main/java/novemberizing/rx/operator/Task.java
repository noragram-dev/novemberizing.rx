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
    @Expose protected On<Object> __onCompleted;

    public Task(T o, novemberizing.rx.Operator<T, U> op, novemberizing.ds.Task<?> parent) {
        super(o, parent);
        it = IN;
        __op = op;
        out = null;
    }

    public Task<T, U> setOnCompleted(On<Object> onCompleted){
        __onCompleted = onCompleted;
        return this;
    }

    @Override
    public void execute() {
        Log.f(Tag, "execute", this);

        Task<T, U> ret = __op.call(this);
        if(ret!=null && ret.done()){
            completed();
            if(__op.down()!=null) {
                __executor.dispatch(__op.down(this));
            } else if(__op.next()!=null) {
                __executor.dispatch(__op.next(this));
            } else if(__parent!=null) {
                __parent.onChildCompleted(ret.out);
            }
        } else {
            Log.i(Tag, "2");
        }
    }

    @Override
    public <V> void onChildCompleted(V o){
        Log.f(Tag, this, o);
        done(true);
        if(__onCompleted!=null){
            __onCompleted.on(o);
        }
        super.onChildCompleted(o);
    }
}
