package novemberizing.rx.operators;

import novemberizing.rx.Operator;
import novemberizing.rx.Subscriber;
import novemberizing.util.Log;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 17.
 */
public class While<T, Z> extends Operator<T, Z> {
    private static final String Tag = "While";
    protected final While<T, Z> __self = this;
    protected novemberizing.ds.func.Pair<T, Z, Boolean> __condition;
    protected Block<T, Z> __block;
    protected boolean __do;


    protected void execute(Task<T, Z> task, T in){
        __block.exec(in).subscribe(new Subscriber<Z>() {
            private Z __out;
            @Override
            public void onNext(Z o) {
                synchronized (this) {
                    __out = o;
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e(Tag, e);
            }

            @Override
            public void onComplete() {
                if(__condition.call(in, __out)){
                    execute(task, in);
                } else {
                    task.complete();
                }
            }
        });
    }

    @Override
    protected void on(Task<T, Z> task, T in) {
        if(!__do) {
            if (__condition.call(in, null)) {
                execute(task, in);
            }
        } else {
            execute(task, in);
        }
    }



    public While(novemberizing.ds.func.Pair<T, Z, Boolean> condition, Block<T, Z> block){
        __condition = condition;
        __block = block;
        __do = false;
    }

    public While(Block<T, Z> block, novemberizing.ds.func.Pair<T, Z, Boolean> condition){
        __condition = condition;
        __block = block;
        __do = false;
    }
}
