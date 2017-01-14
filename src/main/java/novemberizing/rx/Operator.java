package novemberizing.rx;

import novemberizing.util.Log;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 14
 */
public abstract class Operator<T, U> extends Observable<Task<T, U>> {

    private static final String Tag = "Operator";

    protected Task<T, U> out(T in, U out){
        return out(new Task<>(in, out));
    }

    protected Task<T, U> out(Task<T, U> task){
        Log.f(Tag, this, task);

        next(task);

        return task;
    }

    public abstract Task<T, U> exec(T o);
}
