package novemberizing.rx.operators;

import novemberizing.rx.Observer;
import novemberizing.rx.Operator;
import novemberizing.util.Log;

import java.util.LinkedList;

import static novemberizing.ds.Constant.Infinite;

/**
 *
 *
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 14
 */
public class Replayer<T> extends Operator<T, T> {

    private static final String Tag = "Replayer";

    private interface Play<T> {
        void on(Observer<T> observer);
        void on(Operator<T, ?> operator);

    }

    private static class Replay<T> implements Play<T> {
        private T __item;

        public T v(){ return __item; }

        @Override
        public void on(Observer<T> observer){
            observer.onNext(__item);
        }

        @Override
        public void on(Operator<T, ?> operator){
            operator.exec(__item);
        }

        public Replay(T o){
            __item = o;
        }
    }

    private static class Error<T> implements Play<T> {
        private Throwable __exception;

        public Throwable e(){ return __exception; }


        @Override
        public void on(Observer<T> observer){
            observer.onError(__exception);
        }

        @Override
        public void on(Operator<T, ?> operator){
            Log.i(Tag, "how to");
        }

        public Error(Throwable e){
            __exception = e;
        }
    }

    public static class Complete<T> implements Play<T> {
        @Override
        public void on(Observer<T> observer){
            observer.onComplete();
        }

        @Override
        public void on(Operator<T, ?> operator){
            Log.i(Tag, "how to");
        }

        public Complete(){

        }
    }

    private final LinkedList<Play<T>> __replays = new LinkedList<>();
    private int __limit;

    @Override
    protected void on(Local<T, T> task) {
        synchronized (__replays){
            __replays.add(new Replay<>(task.in));
            if (__limit > 0 && __limit < __replays.size()) {
                __replays.pollFirst();
            }
        }
        task.done(task.out = task.in);
    }

    protected void onSubscribed(Operator<T, ?> operator){
        synchronized (__replays) {
            for (Play<T> play : __replays) {
                play.on(operator);
            }
        }
    }

    protected void onSubscribed(Observer<T> observer){
        for(Play<T> play : __replays){
            play.on(observer);
        }
    }

    public Replayer(){
        __limit = Infinite;
    }

    public Replayer(int limit){
        __limit = limit;
        if(__limit<=0){
            __limit = Infinite;
        }
    }
}
