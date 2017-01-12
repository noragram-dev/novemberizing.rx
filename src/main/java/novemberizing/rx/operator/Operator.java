package novemberizing.rx.operator;

import com.google.gson.annotations.Expose;
import novemberizing.ds.Func;
import novemberizing.rx.Scheduler;
import novemberizing.rx.Task;
import novemberizing.util.Log;

/**
 *
 * @author novemberizing, novemberizing@gmail.com
 * @since 2017. 1. 12.
 */
@SuppressWarnings({"DanglingJavadoc", "WeakerAccess"})
public abstract class Operator<T, U> implements novemberizing.rx.Operator<T, U> {
    private static final String Tag = "Operator";

    @Expose protected Operator<U, ?> __next;

    @Override
    public Task<T, U> exec(T o) {
        Log.f(Tag, this, o);
        Task<T, U> ret = declare(o);
        ret = in(ret);
        ret = on(ret);
        ret = out(ret);
        return ret;
    }

    protected Task<T, U> declare(T o){
        Log.f(Tag, this, o);
        return new Task<>(o, this);
    }

    protected Task<T, U> in(Task<T, U> task){
        Log.f(Tag, this, task);
        return task;
    }

    protected abstract Task<T, U> on(Task<T, U> task);

    protected Task<T, U> out(Task<T, U> task){
        Log.f(Tag, this, task);
        if(__next!=null){

        }
        return task;
    }

    public static <T> novemberizing.rx.Operator<T, T> Just(){ return novemberizing.rx.Operator.Just(); }

    public static <T> novemberizing.rx.Operator<T, T> Single(Func<T, T> f){ return novemberizing.rx.Operator.Single(f); }

    public static <T, U> novemberizing.rx.Operator<T, U> Op(Func<T, U> f){ return novemberizing.rx.Operator.Op(f); }

    public static void main(String[] args){
        Log.depth(3);
        Log.disable(Log.HEADER);
        novemberizing.rx.Operator<Integer, String> op = Op(o->(Integer.toString(o+1)));
        Scheduler.Exec(op, 1);
    }

//    protected novemberizing.rx.Operator<U, ?> __next;
//
//    @Override
//    public novemberizing.rx.Operator<U, ?> next(){ return __next; }
//
//    @Override
//    public <V> novemberizing.rx.Operator<U, V> next(Func<U, V> f){
//        if(__next!=null){
//            Log.e(Tag, new RuntimeException("__next!=null"));
//        }
//        novemberizing.rx.Operator<U, V> op = Operator.Op(f);
//        __next = op;
//        return op;
//    }
//
//    public static <T> novemberizing.rx.Operator<T, T> Just(){ return novemberizing.rx.Operator.Just(); }
//
//    public static <T> novemberizing.rx.Operator<T, T> Just(Func<T, T> f){ return novemberizing.rx.Operator.Just(f); }
//
//    public static <T, U> novemberizing.rx.Operator<T, U> Op(Func<T, U> f){ return novemberizing.rx.Operator.Op(f); }
//
//    public static void main(String[] args){
//        Log.i("", "= Operator() ==========================");
//        novemberizing.rx.Operator<String, Integer> op = Op(o->(Integer.parseInt(o)+10));
//        for (String s : args) {
//            Log.i("operator() >", op.call(s));
//        }
//
//        op.next(o->o+1)
//                .next(o->o+1)
//                .next(o->o+1)
//                .next(o->o+1)
//                .next(o->o+1)
//                .next(o->o+1)
//                .next(o->o+1)
//                .next(o->o+1)
//                .next(o->o+1)
//                .next(o->o+1)
//                .next(o->o+1);
//
//        for (String s : args) {
//            Log.i("operator() >", op.call(s));
//        }
//
////        op.next()
//        /**
//         * 사용자는 "call" 을 호출해서는 안된다.
//         * Op(f).
//         * Op(f).
//         * Op(f).
//         */
//    }
}
