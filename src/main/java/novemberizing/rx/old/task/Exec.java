package novemberizing.rx.old.task;

import com.google.gson.annotations.Expose;
import novemberizing.rx.old.des.Operator;
import novemberizing.util.Log;

/**
 *
 * @author novemberizing, novemberizing@gmail.com
 * @since 2017. 1. 12.
 */
@SuppressWarnings("WeakerAccess")
public class Exec<T> extends novemberizing.rx.ds.Task<T> {
    private static final String Tag = "novemberizing.rx.old.task.Task";
    @Expose protected Operator<T, ?> __op;

    public Exec(T o, Operator<T, ?> op) {
        super(o);
        __op = op;
    }

    @Override
    public void execute() {
        Log.f(Tag, "execute", this);
        __executor.dispatch(__op.build(in, this));
    }
}
