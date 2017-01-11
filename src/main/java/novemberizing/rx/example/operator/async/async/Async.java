package novemberizing.rx.example.operator.async.async;

import i.Operator;
import i.Task;
import novemberizing.util.Log;

import java.util.Random;

public class Async<T, U> extends Operator<T, U> {
    public static <T, U> Async<T, U> New(int second, Func<T, U> f){
        return new Async<>(second, f);
    }

    private int __second = 1000;
    private Func<T, U> __func;

    private Async(int second,Func<T, U> f){
        __second = second;
        __func = f;
    }

    @Override
    protected Task<T> in(Task<T> task, T o) {
        Log.f("", this, task, o);
        new Thread(() -> {
            Random r = new Random();
            try {
                Thread.sleep(__second + ((Math.abs(r.nextInt(4096))) % 100) * 10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            task.next(o, __func!=null ? __func.call(o) : null);
        }).start();
        return null;
    }
}