package novemberizing.rx;

import novemberizing.util.Log;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 17.
 */
public class Subscribers {
    public static class Just<T> extends Subscriber<T> {
        private String __tag = "just";

        public Just(){}

        public Just(String tag){ __tag = tag; }

        @Override
        public void onNext(T o) {
            Log.i(__tag, o);
        }

        @Override
        public void onError(Throwable e) {
            Log.i(__tag, "error", e);
        }

        @Override
        public void onComplete() {
            Log.i(__tag, "complete");
        }
    }

    public static <T> Just<T> Just(){ return new Just<>(); }

    public static <T> Just<T> Just(String tag){ return new Just<>(tag); }
}
