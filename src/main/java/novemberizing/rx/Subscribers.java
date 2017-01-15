package novemberizing.rx;

import novemberizing.util.Log;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 14
 */
@SuppressWarnings("unused")
public class Subscribers {


    public static class Just<T> extends Subscriber<T> {
        private String __tag;

        public Just(){ __tag = "Just"; }

        public Just(String tag){ __tag = tag; }

        @Override
        public void onNext(T o) {
            Log.i(__tag, o);
        }

        @Override
        public void onComplete() {
            Log.i(__tag, "complete");
        }

        @Override
        public void onError(Throwable e) {
            Log.e(__tag, e);
        }
    }

    public static <T> Just<T> Just(){ return new Just<>(); }
    public static <T> Just<T> Just(String tag){ return new Just<>(tag); }

    public static class Task<T, Z> extends Subscriber<Operator.Local<T, Z>> {
        private String __tag;

        public Task(){ __tag = "Task"; }

        public Task(String tag){ __tag = tag; }

        @Override
        public void onNext(Operator.Local<T, Z> o) {
            Log.i(__tag, o);
        }

        @Override
        public void onComplete() {
            Log.i(__tag, "complete");
        }

        @Override
        public void onError(Throwable e) {
            Log.e(__tag, e);
        }
    }

    public static <T, Z> Task<T, Z> Task(){ return new Task<>(); }
    public static <T, Z> Task<T, Z> Task(String tag){ return new Task<>(tag); }
}
