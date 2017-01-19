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
            Log.i(__tag, o);
        }

        @Override
        public void onError(Throwable e) {
            Log.i(__tag, "error", e.getMessage());
        }

        @Override
        public void onComplete() {
            Log.i(__tag, "complete");
        }
    }

    public static <T> Just<T> Just(){ return new Just<>(); }

    public static <T> Just<T> Just(String tag){ return new Just<>(tag); }

//    public static class Block<T, Z> extends Subscriber<Z> {
//        protected LinkedList<Z> __items = new LinkedList<>();
//        protected String __tag = "block";
//        protected final Operator.Task<T, Z> __task;
//        protected final Operator<?, Z> __operator;
//
//        public String tag(){ return __tag; }
//
//        public <U> Block(Operator<U, Z> op,Operator.Task<T, Z> task){
//            __task = task;
//            __operator = op;
//        }
//
//        public <U> Block(Operator<U, Z> op,Operator.Task<T, Z> task, String tag){
//            __task = task;
//            __operator = op;
//            __tag = tag;
//        }
//
//        @Override
//        public void onNext(Z o) {
//            __task.next(o);
//            __items.addLast(o);
//        }
//
//        @Override
//        public void onError(Throwable e) {
//            __task.error(e);
//        }
//
//        @Override
//        public void onComplete() {
//            Operator.Bulk(__operator, __items);
//            __task.complete();
//        }
//    }
//
//    public static <T, Z> Subscribers.Block<T, Z> Block(Operator<?, Z> op,Operator.Task<T, Z> task){
//        return new Subscribers.Block<>(op, task);
//    }
//
//    public static <T, Z> Subscribers.Block<T, Z> Block(Operator<?, Z> op,Operator.Task<T, Z> task, String tag){
//        return new Subscribers.Block<>(op, task, tag);
//    }
}
