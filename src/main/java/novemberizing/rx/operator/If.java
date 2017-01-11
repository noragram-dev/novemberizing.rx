package novemberizing.rx.operator;

import novemberizing.rx.Operator;
import novemberizing.rx.Task;
import novemberizing.util.Log;

import java.util.LinkedList;

/**
 *
 * @author novemberizing, novemberizing.rx@novemberizing.net
 * @since 2017. 1. 11.
 */
public class If<T, U> extends Operator<T, U> {

    protected Operator<T, ?> __else;
    protected LinkedList<novemberizing.rx.tuple.Pair<Operator.Func<T,Boolean>,Operator<T, ?>>> __conditions = new LinkedList<>();


    public <V> If(Operator.Func<T,Boolean> condition, novemberizing.rx.func.Single<T, V> f){
        __conditions.add(new novemberizing.rx.tuple.Pair<>(condition,Block(f)));
    }

    public <V> If<T, U> ElseIf(Operator.Func<T,Boolean> condition, novemberizing.rx.func.Single<T, V> f){
        if(__else==null){
            __conditions.add(new novemberizing.rx.tuple.Pair<>(condition,Block(f)));
        } else {
            Log.e("", new RuntimeException("else!=null"));
        }
        return this;
    }

    public <V> If<T, U> Else(novemberizing.rx.func.Single<T, V> f){
        if(__else==null){
            __else = Op(f);
        } else {
            Log.e("", new RuntimeException("else!=null"));
        }
        return this;
    }

    @Override
    protected Task<T> on(Task<T> task, T o) {
        for(novemberizing.rx.tuple.Pair<Operator.Func<T,Boolean>,Operator<T, ?>> condition : __conditions){
            if(condition!=null){
                if(condition.first==null || condition.first.call(o)){
                    if(condition.second==null){
                        task.set(o, null);
                        task.out(null);
                    } else {
                        task.set(o, null);
                        __iterate(task);
                        task.down(condition.second, o);
                    }
                    return null;
                }
            }
        }
        task.set(o, null);
        __iterate(task);
        if(__else!=null) {
            task.down(__else, o);
        } else {
            task.out(null);
        }
        return null;
    }
}
