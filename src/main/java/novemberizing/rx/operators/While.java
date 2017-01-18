package novemberizing.rx.operators;

import novemberizing.rx.Block;
import novemberizing.rx.Operator;
import novemberizing.rx.Subscriber;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 17.
 */
public class While<T, Z> extends Operator<T, Z> {
    protected final While<T, Z> __self = this;
    @Override
    protected void on(Task<T, Z> task, T in) {
        if(!__do) {
            if (__condition.call(in, null)) {
                __block.exec(in).subscribe(new Subscriber<Z>() {
                    private Z __out;
                    @Override
                    public void onNext(Z o) {
                        if(__condition.call(in,o)){
                            __self.re(task);
                            __out = o;
                        } else {
                            task.complete();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        task.error(e);
                    }

                    @Override
                    public void onComplete() {
                        Operator.Emit(__self, __out);
                        task.complete();
                    }
                });
            }
        } else {
            __block.exec(in).subscribe(new Subscriber<Z>() {
                private Z __out;
                @Override
                public void onNext(Z o) {
                    if(__condition.call(in,o)){
                        __self.re(task);
                        __out = o;
                    } else {
                        task.complete();
                    }
                }

                @Override
                public void onError(Throwable e) {
                    task.error(e);
                }

                @Override
                public void onComplete() {
                    Operator.Emit(__self, __out);
                    task.complete();
                }
            });
        }
    }

    protected novemberizing.ds.func.Pair<T, Z, Boolean> __condition;
    protected Block<T, Z> __block;
    protected boolean __do;

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
