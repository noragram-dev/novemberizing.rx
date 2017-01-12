package novemberizing.rx.operator;

import novemberizing.ds.Task;
import novemberizing.rx.Scheduler;
import novemberizing.util.Log;

/**
 *
 * @author novemberizing, novemberizing@gmail.com
 * @since 2017. 1. 12.
 */
public class Just<T> extends Single<T> {
    private static final String Tag = "Just";
    @Override
    protected Task<T, T> on(Task<T, T> task) {
        Log.f(Tag, this, task);
        task.out = task.in;
        return task;
    }

    public static void main(String[] args){
        Log.depth(3);
        Log.disable(Log.HEADER);
        Scheduler.Exec(Just(), "1");
    }
}
