package novemberizing.rx;

import novemberizing.util.Log;

import java.util.LinkedList;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 17.
 */
public class Subscribers {
    public static class Just<T> extends Subscriber<T> {
        private String __tag = "just";

        public String tag(){ return __tag; }

        public Just(){}

        public Just(String tag){ __tag = tag; }

        @Override
        public void onNext(T o) {
            Log.d(__tag, o);
        }

        @Override
        public void onError(Throwable e) {
            Log.e(__tag, "error", e.getMessage());
        }

        @Override
        public void onComplete() {
            Log.d(__tag, "complete");
        }
    }

    public static <T> Just<T> Just(){ return new Just<>(); }

    public static <T> Just<T> Just(String tag){ return new Just<>(tag); }


    public static class Completion<T> extends Subscriber<T> {

        private String __tag = "completion";

        private T __o;
        private Throwable __exception;
        private novemberizing.ds.on.Pair<T, Throwable> __func;

        public String tag(){ return __tag; }

        public Completion(novemberizing.ds.on.Pair<T, Throwable> f){
            __func = f;
            __exception = null;
            __o = null;
        }

        public Completion(String tag, novemberizing.ds.on.Pair<T, Throwable> f){
            __tag = tag;
            __func = f;
            __exception = null;
            __o = null;
        }

        @Override
        public void onNext(T o) {
            Log.d(__tag, o);
            __o = o;
        }

        @Override
        public void onError(Throwable e) {
            Log.e(__tag, "error", e.getMessage());
        }

        @Override
        public void onComplete() {
            Log.d(__tag, "complete");
            if(__func!=null){
                __func.on(__o, __exception);
            }
        }
    }

    public static <T> Completion<T> Completion(novemberizing.ds.on.Pair<T, Throwable> f){ return new Completion<>(f); }

    public static <T> Completion<T> Completion(String tag, novemberizing.ds.on.Pair<T, Throwable> f){ return new Completion<>(tag, f); }
}
