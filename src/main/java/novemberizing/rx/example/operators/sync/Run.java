package novemberizing.rx.example.operators.sync;

import novemberizing.rx.Func;
import novemberizing.rx.Operator;
import novemberizing.rx.operators.Sync;

import java.util.Random;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 17.
 */
public class Run<T, U> extends Sync<T, U> {
    private int __sleep = 100;
    private Func<T, U> __func;

    @Override
    protected void on(Task<T, U> task, T in) {
        new Thread(){
            @Override
            public void run(){
                Random r = new Random();
                try {
                    Thread.sleep(__sleep + Math.abs(r.nextInt())%__sleep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                task.next(__func.call(in));
                task.complete();
            }
        }.start();
    }

    public Run(Func<T, U> f){
        __func = f;
    }
}
