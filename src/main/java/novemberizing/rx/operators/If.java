package novemberizing.rx.operators;

import novemberizing.rx.Func;
import novemberizing.rx.Operator;
import novemberizing.rx.Subscriber;
import novemberizing.util.Log;

import java.util.LinkedList;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 17.
 */
public class If<T, Z> extends Operator<T, Z> {
    private static final String Tag = "If";

    private final If<T, Z> __self = this;
    private Block<T, Z> __else;

    protected LinkedList<novemberizing.ds.tuple.Pair<Func<T, Boolean>,Block<T, Z>>> __conditions = new LinkedList<>();

    public If(Func<T, Boolean> condition, Block<T, Z> block){
        __conditions.addLast(new novemberizing.ds.tuple.Pair<>(condition, block));
    }

    public If<T, Z> _elseif(Func<T, Boolean> condition, Block<T, Z> block){
        if(__else!=null) {
            Log.e(Tag, "__else!=null");
        } else {
            __conditions.addLast(new novemberizing.ds.tuple.Pair<>(condition, block));
        }
        return this;
    }

    public If<T, Z> _else(Block<T, Z> block){
        if(__else!=null){
            Log.e(Tag, "__else!=null");
        } else {
            __else = block;
        }
        return this;
    }

    @Override
    protected void on(Task<T, Z> task, T in) {
        for(novemberizing.ds.tuple.Pair<Func<T, Boolean>,Block<T, Z>> condition : __conditions){
            if(condition.first.call(in)){
                condition.second.exec(in).subscribe(new Subscriber<Z>() {
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
                return;
            }
        }
        if(__else!=null){
            __else.exec(in).subscribe(new Subscriber<Z>() {
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
            Log.e(Tag, "__not ...");
            task.complete();
        }
    }
}
