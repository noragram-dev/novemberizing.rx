package novemberizing.rx.example.operator.async.sync;

import i.Operator;
import i.Task;

import java.util.Random;

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
    protected Task<T> on(Task<T> task, T o) {
        Random r = new Random();
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(__second + (Math.abs(r.nextInt()) % 50 + 10) * 10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            task.next(o, __func!=null ? __func.call(o) : null);
        });
        thread.start();
        return null;
    }
}
