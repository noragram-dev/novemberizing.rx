package novemberizing.rx.task;

import novemberizing.rx.Operator;
import novemberizing.rx.Task;

/**
 *
 * @author novemberizing, novemberizing@gmail.com
 * @since 2017. 1. 12.
 */
public class Executor implements novemberizing.ds.Executor {
    protected final novemberizing.ds.Queue<Task> __q;

    public Executor(){
        __q = new novemberizing.ds.Queue<>();
    }

    @Override
    public void onecycle() {
        while(!__q.empty()){
            Operator.Exec(__q.pop());
        }
    }
}
