package novemberizing.rx.example.operator.async.sync;

import i.Operator;
import i.Task;

import static i.Iteration.OUT;

/**
 *
 * @author novemberizing, i@novemberizing.net
 * @since 2017. 1. 9.
 */
public class Sync<T, U> extends i.operator.Sync<T, U>  {
    public static <T, U> Sync<T, U> New(int second, Operator.Func<T, U> f){
        return new Sync<>(second, f);
    }

    private int __second = 1000;
    private Operator.Func<T, U> __func;

    private Sync(int second, Operator.Func<T, U> f){
        __second = second;
        __func = f;
    }

    @Override
    protected Task<T> on(Task<T> task) {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(__second);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            task.v.out(__func!=null ? __func.call(task.v.in) : null);
            task.next();
        });
        thread.start();
        return null;
    }
}