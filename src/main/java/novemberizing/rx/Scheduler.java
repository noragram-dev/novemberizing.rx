package novemberizing.rx;

import novemberizing.ds.Task;
import novemberizing.rx.scheduler.Local;
import novemberizing.util.Log;

/**
 *
 * @author novemberizing, novemberizing@gmail.com
 * @since 2017. 1. 12.
 */
@SuppressWarnings("DanglingJavadoc")
public abstract class Scheduler implements Executor {
    private static final String Tag = "Scheduler";

    public static Scheduler Self(){ return Local(); }

    public static Scheduler Local(){ return Local.Get(); }

    public static <T, U> Task<T, U> Exec(Scheduler scheduler, Operator<T, ?> op, T o){
        Log.f(Tag, op, o);
        Task<T, U> task = new Task<>(o);
        scheduler.dispatch(new Operator.Work<>(op, o, task));
        // scheduler.dispatch(new Operator.Work.New<>(op, o, task));
        // scheduler.dispatch(new Operator.Work.New<>(op, o, task));
        return task;
    }

    public static <T, U> Task<T, U> Exec(Operator<T, ?> op, T o){
        Log.f(Tag, op, o);
        return Exec(Scheduler.Self(), op, o);
    }
}
