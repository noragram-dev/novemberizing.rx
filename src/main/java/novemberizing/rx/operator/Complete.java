package novemberizing.rx.operator;

import novemberizing.rx.Operator;
import novemberizing.rx.Task;
import novemberizing.util.Log;

import java.util.HashSet;

/**
 *
 * Completion(Op(...))
 *
 * @author novemberizing, novemberizing.rx@novemberizing.net
 * @since 2017. 1. 10.
 */
public class Complete<T> extends Operator<Task<T>, T> {
    private static final String Tag = "Complete";
    private final HashSet<Task<T>> __tasks = new HashSet<>();

    protected Task<Task<T>> in(Task<Task<T>> task, Task<T> o){
        synchronized (__tasks){
            if(!__tasks.add(o)){
                Log.e(Tag, new Throwable(), this, task, o);
            }
        }
        return task.set(o, null);
    }

    @Override
    protected Task<Task<T>> on(Task<Task<T>> task, Task<T> o) {
        if(o.done()){
            return task.set(o, o);
        }
        return null;
    }

    @Override
    protected void out(Task<Task<T>> task, Object o){
        synchronized (__tasks) {
            if(!__tasks.remove(o)){
                Log.e(Tag, new Throwable(), this, task, o);
            }
        }
        super.out(task, o);
    }
}
