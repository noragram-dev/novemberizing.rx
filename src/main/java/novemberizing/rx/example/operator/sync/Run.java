package novemberizing.rx.example.operator.sync;

import novemberizing.ds.Func;
import novemberizing.rx.operators.Sync;

import java.util.Random;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 14
 */
public class Run<T, Z> extends Sync<T, Z> {
    private Func<T, Z> __func;
    private long __second = 100;

    public Run(Func<T, Z> f){
        __func = f;
    }

    @Override
    protected void on(Local<T, Z> task) {
        new Thread(){
            @Override
            public void run(){
                Random r = new Random();
                try {
                    Thread.sleep(__second + Math.abs(r.nextInt())%500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                task.done(__func.call(task.in));
            }
        }.start();
    }
}