package novemberizing.rx.operators;

import novemberizing.rx.Operator;
import novemberizing.rx.Subscribers;
import novemberizing.rx.Task;
import novemberizing.util.Log;

import java.util.LinkedList;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 14
 */
@SuppressWarnings("DanglingJavadoc")
public class Sync<T, U> extends Operator<T, U> {
    private static final String Tag = "Sync";

    private final LinkedList<Task<T, U>> __tasks = new LinkedList<>();
    private Task<T, U> __current;

    private Operator<T, U> __op;

    public Sync(Operator<T, U> op){
        __op = op;
        __op.subscribe(new Subscribers.Task<T, U>() {
            @Override
            public void onNext(Task<T, U> o) {
                if(__current!=null){
                    if(o!=null) {
                        synchronized (__tasks) {
                            __current.out = o.out;
                            out(__current);
                            if(__tasks.size()>0){
                                __current = __tasks.pollFirst();
                                __op.exec(__current.in);
                            } else {
                                __current = null;
                            }
                        }
                    } else {
                        Log.e(Tag, new RuntimeException("o==null"));
                    }
                } else {
                    Log.e(Tag, new RuntimeException("__current==null"));
                }
            }
        });
    }

    @Override
    public Task<T, U> on(Task<T, U> task) {
        synchronized (__tasks){
            if(__current!=null){
                __tasks.addLast(task);
            } else {
                __current = task;
                __op.exec(task.in);
            }
        }
        return task;
    }
}
