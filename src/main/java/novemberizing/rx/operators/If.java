package novemberizing.rx.operators;

import novemberizing.ds.func.Single;
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
    private Block.Op<T, Z> __else;

    protected LinkedList<novemberizing.ds.tuple.Pair<Single<T, Boolean>,Block.Op<T, Z>>> __conditions = new LinkedList<>();

    public If(Single<T, Boolean> condition, Block.Op<T, Z> block){
        __conditions.addLast(new novemberizing.ds.tuple.Pair<>(condition, block));
    }

    public If(Single<T, Boolean> condition, novemberizing.ds.func.Single<T, Z> f){
        __conditions.addLast(new novemberizing.ds.tuple.Pair<>(condition, Operator.Block(f)));
    }

    public If(Single<T, Boolean> condition, Operator<T, Z> op){
        __conditions.addLast(new novemberizing.ds.tuple.Pair<>(condition, Operator.Block(op)));
    }

    public If<T, Z> _elseif(Single<T, Boolean> condition, Block.Op<T, Z> block){
        if(__else!=null) {
            Log.e(Tag, "__else!=null");
        } else {
            __conditions.addLast(new novemberizing.ds.tuple.Pair<>(condition, block));
        }
        return this;
    }

    public If<T, Z> _elseif(Single<T, Boolean> condition, novemberizing.ds.func.Single<T, Z> f){
        if(__else!=null) {
            Log.e(Tag, "__else!=null");
        } else {
            __conditions.addLast(new novemberizing.ds.tuple.Pair<>(condition, Operator.Block(f)));
        }
        return this;
    }

    public If<T, Z> _elseif(Single<T, Boolean> condition, Operator<T, Z> op){
        if(__else!=null) {
            Log.e(Tag, "__else!=null");
        } else {
            __conditions.addLast(new novemberizing.ds.tuple.Pair<>(condition, Operator.Block(op)));
        }
        return this;
    }

    public If<T, Z> _else(Block.Op<T, Z> block){
        if(__else!=null){
            Log.e(Tag, "__else!=null");
        } else {
            __else = block;
        }
        return this;
    }

    public If<T, Z> _else(novemberizing.ds.func.Single<T, Z> f){
        if(__else!=null){
            Log.e(Tag, "__else!=null");
        } else {
            __else = Operator.Block(f);
        }
        return this;
    }

    public If<T, Z> _else(Operator<T, Z> op){
        if(__else!=null){
            Log.e(Tag, "__else!=null");
        } else {
            __else = Operator.Block(op);
        }
        return this;
    }

    public void execute(Block.Op<T, Z> block, Task<T, Z> task, T in){
        block.exec(in).subscribe(new Subscriber<Z>() {
            protected boolean __subscribe = true;
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

            @Override public void subscribe(boolean v){ __subscribe = v; }

            @Override public boolean subscribed(){ return __subscribe; }
        });
    }

    @Override
    protected void on(Task<T, Z> task, T in) {
        for(novemberizing.ds.tuple.Pair<Single<T, Boolean>,Block.Op<T, Z>> condition : __conditions){
            if(condition.first.call(in)){
                execute(condition.second, task, in);
                return;
            }
        }
        if(__else!=null){
            execute(__else, task, in);
        } else {
            Log.e(Tag, "no block");
            task.complete();
        }
    }
}
