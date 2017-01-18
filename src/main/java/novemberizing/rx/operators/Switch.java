package novemberizing.rx.operators;

import novemberizing.rx.Block;
import novemberizing.rx.Func;
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
public class Switch<T, Z> extends Operator<T, Z> {
    private static final String Tag = "Switch";
    protected Func<T, Integer> __hash;
    protected HashMap<Integer, Block<T, Z>> __cases = new HashMap<>();
    protected Block<T, Z> __default;
    protected final Switch<T, Z> __self = this;

    public Switch(Func<T, Integer> hash){
        __hash = hash;
    }

    public Switch<T, Z> _case(int i, Block<T, Z> block){
        if(__default!=null){
            Log.e(Tag, "__default!=null");
        } else if(__cases.get(i)!=null) {
            Log.e(Tag, "__case is exist");
        } else {
            __cases.put(i, block);
        }
        return this;
    }

    public Switch<T, Z> _default(Block<T, Z> block){
        if(__default!=null){
            Log.e(Tag, "__default!=null");
        } else {
            __default = block;
        }
        return this;
    }

    @Override
    protected void on(Task<T, Z> task, T in) {
        Integer i = __hash.call(in);
        if(i!=null){
            Block<T, Z> block = __cases.get(i);
            if(block!=null){
                block.exec(in).subscribe(new Subscriber<Z>() {
                    private LinkedList<Z> __items = new LinkedList<>();
                    @Override
                    public void onNext(Z o) {
                        __items.addLast(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(Tag, e);
                    }

                    @Override
                    public void onComplete() {
                        Operator.Bulk(__self, __items);
                        task.complete();
                    }
                });
            } else if(__default!=null) {
                __default.exec(in).subscribe(new Subscriber<Z>() {
                    private LinkedList<Z> __items = new LinkedList<>();
                    @Override
                    public void onNext(Z o) {
                        __items.addLast(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(Tag, e);
                    }

                    @Override
                    public void onComplete() {
                        Operator.Bulk(__self, __items);
                        task.complete();
                    }
                });
            } else {
                task.error(new RuntimeException("not exist callable op"));
                task.complete();
            }
        } else if(__default!=null) {
            __default.exec(in).subscribe(new Subscriber<Z>() {
                private LinkedList<Z> __items = new LinkedList<>();
                @Override
                public void onNext(Z o) {
                    __items.addLast(o);
                }

                @Override
                public void onError(Throwable e) {
                    Log.e(Tag, e);
                }

                @Override
                public void onComplete() {
                    Operator.Bulk(__self, __items);
                    task.complete();
                }
            });
        } else {
            task.error(new RuntimeException("not exist callable op"));
            task.complete();
        }
    }
}
