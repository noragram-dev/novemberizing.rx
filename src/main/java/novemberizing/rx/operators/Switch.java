package novemberizing.rx.operators;

import novemberizing.ds.func.Single;
import novemberizing.rx.Operator;
import novemberizing.rx.Subscriber;
import novemberizing.util.Log;

import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 18.
 */
public abstract class Switch<T, Z> extends Operator<T, Z> {
//    private static final String Tag = "Switch";
//    protected Single<T, Integer> __hash;
//    protected HashMap<Integer, Block<T, Z>> __cases = new HashMap<>();
//    protected Block<T, Z> __default;
//
//    public Switch(Single<T, Integer> hash){
//        __hash = hash;
//    }
//
//    public Switch<T, Z> _case(int i, Block<T, Z> block){
//        if(__default!=null){
//            Log.e(Tag, "__default!=null");
//        } else if(__cases.get(i)!=null) {
//            Log.e(Tag, "__case is exist");
//        } else {
//            __cases.put(i, block);
//        }
//        return this;
//    }
//
//    public Switch<T, Z> _default(Block<T, Z> block){
//        if(__default!=null){
//            Log.e(Tag, "__default!=null");
//        } else {
//            __default = block;
//        }
//        return this;
//    }
//
//    protected void execute(Block<T, Z> block, Task<T, Z> task, T in){
//        block.exec(in).subscribe(new Subscriber<Z>() {
//            @Override
//            public void onNext(Z o) {
//                Log.i(Tag, task, o);
//                task.next(o);
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.e(Tag, e);
//            }
//
//            @Override
//            public void onComplete() {
//                Log.i(Tag, task);
//                task.complete();
//            }
//        });
//    }
//
//    @Override
//    protected void on(Task<T, Z> task, T in) {
//        Integer i = __hash.call(in);
//        if(i!=null){
//            Block<T, Z> block = __cases.get(i);
//            if(block!=null){
//                execute(block, task, in);
//            } else if(__default!=null) {
//                __default.exec(in).subscribe(new Subscriber<Z>() {
//                    @Override
//                    public void onNext(Z o) {
//                        task.next(o);
//                        Log.i(Tag, task, o);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.e(Tag, e);
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        Log.i(Tag, task);
//                        task.complete();
//                    }
//                });
//            } else {
//                task.error(new RuntimeException("not exist callable op"));
//                task.complete();
//            }
//        } else if(__default!=null) {
//            __default.exec(in).subscribe(new Subscriber<Z>() {
//                @Override
//                public void onNext(Z o) {
//                    task.next(o);
//                    Log.i(Tag, task, o);
//                }
//
//                @Override
//                public void onError(Throwable e) {
//                    Log.e(Tag, e);
//                }
//
//                @Override
//                public void onComplete() {
//                    task.complete();
//                    Log.i(Tag, task);
//                }
//            });
//        } else {
//            task.error(new RuntimeException("not exist callable op"));
//            task.complete();
//        }
//    }
}
