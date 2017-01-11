package novemberizing.rx.operator;

import novemberizing.rx.Operator;
import novemberizing.rx.Task;
import novemberizing.util.Log;

/**
 *
 * @author novemberizing, novemberizing.rx@novemberizing.net
 * @since 2017. 1. 10.
 */
public class Just<T> extends Operator<T, T> {
    private static final String Tag = "Just";

    public Just(){
        Log.f(Tag, "");
    }

    @Override
    protected Task<T> on(Task<T> task, T o) {
        return task.set(o, o);
    }
}
