package i.operator;

import i.Operator;
import i.Task;
import novemberizing.util.Log;

/**
 *
 * @author novemberizing, i@novemberizing.net
 * @since 2017. 1. 10.
 */
public class Just<T> extends Operator<T, T> {
    private static final String Tag = "Just";

    public Just(){
        Log.f(Tag, "");
    }

    @Override
    protected Task<T> in(Task<T> task, T o) {
        return task.set(o, o);
    }
}
