package novemberizing.rx.operator;

import com.google.gson.annotations.Expose;
import novemberizing.util.Log;

/**
 *
 * @author novemberizing, novemberizing@gmail.com
 * @since 2017. 1. 12.
 */
@SuppressWarnings("WeakerAccess")
public class Exec<T> extends novemberizing.ds.Task<T> {
    private static final String Tag = "novemberizing.rx.operator.Task";
    @Expose protected novemberizing.rx.Operator<T, ?> __op;

    public Exec(T o, novemberizing.rx.Operator<T, ?> op) {
        super(o);
        __op = op;
    }

    @Override
    public void execute() {
        Log.f(Tag, "execute", this);
        __executor.dispatch(__child = __op.build(in, this));
    }

    @Override
    public <U> void onChildCompleted(U o){
        Log.f(Tag, this, o);
        done(true);
        super.onChildCompleted(o);
    }
}
