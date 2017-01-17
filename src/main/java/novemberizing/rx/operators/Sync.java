package novemberizing.rx.operators;

import novemberizing.rx.Operator;
import novemberizing.rx.Subscriber;
import novemberizing.util.Log;

import java.util.LinkedList;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 17.
 */
public abstract class Sync<T, U> extends Operator<T, U> {
    private static final String Tag = "Sync";
    private final LinkedList<Operator.Task<T, U>> __tasks = new LinkedList<>();
    private Operator.Task<T, U> __now;

    @Override
    protected void in(Operator.Next<?, U> next, Operator.Task<T, U> task){
        synchronized (__tasks){
            if(__now==null){
                __now = task;
                on(task, task.in());
            } else {
                __tasks.add(task);
            }
        }
    }

    private void __next(Operator.Next<?, U> next){
        synchronized (__tasks){
            __now = null;
            while(__tasks.size()>0){
                __now = __tasks.pollFirst();
                if(__now!=null){
                    break;
                }
            }
        }
        if(__now!=null){
            on(__now, __now.in());
        }
    }
}
