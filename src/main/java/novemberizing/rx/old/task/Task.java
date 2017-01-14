package novemberizing.rx.old.task;


import com.google.gson.annotations.Expose;
import novemberizing.rx.old.operator.Operator;
import novemberizing.util.Log;

import static novemberizing.rx.ds.Iteration.IN;

/**
 *
 * @author novemberizing
 * @since 2017. 1. 13.
 */
public class Task<T, U> extends novemberizing.rx.ds.Task<T> {
    private static final String Tag = "novemberizing.rx.old.task.Task";

    @Expose public int it;
    @Expose public U out;
    @Expose protected novemberizing.rx.old.des.Operator<T, U> __op;

    public Task(T o, novemberizing.rx.old.des.Operator<T, U> op, novemberizing.rx.ds.Task<?> parent) {
        super(o, parent);
        it = IN;
        __op = op;
        out = null;
    }

    @Override
    synchronized public void execute() {
        Log.f(Tag, "execute", this);

        Task<T, U> ret = __op.exec(this);
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

//    @Override
//    public <U extends novemberizing.rx.ds.Task> void onChildCompleted(U o){
//        Log.f(Tag, this, o);
//        __o = o;
//        synchronized (__children){
//            __children.remove(o);
//            if(__children.size()==0){ __done = true; }
//        }
//        if(__done) {
//            completed();
//        } else {
//            executed();
//        }
//    }
    synchronized public void next() {
        it++;
        executed();
    }
}
