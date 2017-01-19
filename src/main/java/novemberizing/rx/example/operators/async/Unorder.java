package novemberizing.rx.example.operators.async;

import novemberizing.ds.func.Single;
import novemberizing.rx.Operator;

import java.util.Random;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 17.
 */
public class Unorder<T, U> extends Operator<T, U> {
    private int __sleep = 100;
    private Single<T, U> __func;

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

    public Unorder(Single<T, U> f){
        __func = f;
    }
}
