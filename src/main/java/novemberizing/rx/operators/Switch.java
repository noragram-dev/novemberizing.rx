package novemberizing.rx.operators;

import novemberizing.ds.func.Single;
import novemberizing.rx.Operator;
import novemberizing.rx.Subscriber;
import novemberizing.util.Log;

import java.util.HashMap;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 18.
 */
public class Switch<T, Z> extends Operator<T, Z> {
    private static final String Tag = "Switch";
    protected Single<T, Integer> __hash;
    protected HashMap<Integer, Block.Op<T, Z>> __cases = new HashMap<>();
    protected Block.Op<T, Z> __default;

    public Switch(Single<T, Integer> hash){
        __hash = hash;
    }

    public Switch<T, Z> _case(int i, Block.Op<T, Z> block){
        if(__default!=null){
            Log.e(Tag, "__default!=null");
        } else if(__cases.get(i)!=null) {
            Log.e(Tag, "__case is exist");
        } else {
            __cases.put(i, block);
        }
        return this;
    }

    public Switch<T, Z> _case(int i, novemberizing.ds.func.Single<T, Z> f){
        if(__default!=null){
            Log.e(Tag, "__default!=null");
        } else if(__cases.get(i)!=null) {
            Log.e(Tag, "__case is exist");
        } else {
            __cases.put(i, Operator.Block(f));
        }
        return this;
    }

    public Switch<T, Z> _default(Block.Op<T, Z> block){
        if(__default!=null){
            Log.e(Tag, "__default!=null");
        } else {
            __default = block;
        }
        return this;
    }


    public Switch<T, Z> _default(novemberizing.ds.func.Single<T, Z> f){
        if(__default!=null){
            Log.e(Tag, "__default!=null");
        } else {
            __default = Operator.Block(f);
        }
        return this;
    }

    protected void execute(Block.Op<T, Z> block, Task<T, Z> task, T in){
        block.exec(in).subscribe(new Subscriber<Z>() {
            @Override
            public void onNext(Z o) {
                Log.f(Tag, task, o);
                task.next(o);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(Tag, e);
            }

            @Override
            public void onComplete() {
                Log.f(Tag, task);
                task.complete();
            }
        });
    }

    @Override
    protected void on(Task<T, Z> task, T in) {
        Integer i = __hash.call(in);
        if(i!=null){
            Block.Op<T, Z> block = __cases.get(i);
            if(block!=null){
                execute(block, task, in);
            } else if(__default!=null) {
                execute(__default, task, in);
            } else {
                task.error(new RuntimeException("not exist callable op"));
                task.complete();
            }
        } else if(__default!=null) {
            execute(__default, task, in);
        } else {
            task.error(new RuntimeException("not exist callable op"));
            task.complete();
        }
    }
}
