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

    public static class Task<T, U> extends Subscriber<novemberizing.rx.Task<T, U>> {
        private String __tag;

        public Task(){ __tag = "Task"; }

        public Task(String tag){ __tag = tag; }

        @Override
        public void onNext(novemberizing.rx.Task<T, U> o) {
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

    public static <T, U> Task<T, U> Task(){ return new Task<>(); }
    public static <T, U> Task<T, U> Task(String tag){ return new Task<>(tag); }
}
