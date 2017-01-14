package novemberizing.rx.example.operators.async;

import novemberizing.ds.Func;
import novemberizing.rx.Operator;
import novemberizing.rx.Task;

import java.util.Random;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 14
 */
@SuppressWarnings("AnonymousHasLambdaAlternative")
public class Run<T, U> extends Operator<T, U> {
    private static long DefaultSleepSec = 100L;
    private Func<T, U> __func;

    @Override
    public Task<T, U> exec(T o) {
        Task<T, U> task = new Task<>(o);
        new Thread(){
            @Override
            public void run(){
                Random r = new Random();
                try {
                    Thread.sleep(DefaultSleepSec + Math.abs(r.nextInt())%100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                task.out = __func.call(task.in);
                out(task);
            }
        }.start();
        return task;
    }

    public Run(Func<T, U> f){
        __func = f;
    }
}
