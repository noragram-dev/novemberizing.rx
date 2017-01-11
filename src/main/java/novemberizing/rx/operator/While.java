package novemberizing.rx.operator;

import novemberizing.rx.Operator;
import novemberizing.rx.Task;

/**
 *
 * @author novemberizing, novemberizing.rx@novemberizing.net
 * @since 2017. 1. 11.
 */
public class While<T> extends Operator<T, T> {

    protected Operator.Func<T, Boolean> __condition;
    protected Operator<T, ?> __op;
    protected boolean __do;

    public While(Operator.Func<T, Boolean> condition, novemberizing.rx.func.Single<T, ?> op){
        __condition = condition;
        __op = Op(op);
        __do = false;
    }

    public While(novemberizing.rx.func.Single<T, ?> op, Operator.Func<T, Boolean> condition){
        __condition = condition;
        __op = Op(op);
        __do = true;
    }

    @Override
    protected Task<T> on(Task<T> task, T o){
        if(__do){
            task.set(o, o);
            __iterate(task);
            task.down(__op, task.v.in);
            return null;
        }
        return task.set(o, o);
    }

    @Override
    protected void out(Task<T> task, Object o) {
//        task.set(task.v.out, task.v.out);
        if(__condition.call((T) o)){
            task.down(__op, (T) o);
        } else {
            super.out(task, o);
        }
    }
}
