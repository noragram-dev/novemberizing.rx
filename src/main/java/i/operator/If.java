package i.operator;

import i.Operator;
import i.Task;
import novemberizing.util.Log;

import java.util.LinkedList;

/**
 *
 * @author novemberizing, i@novemberizing.net
 * @since 2017. 1. 11.
 */
public class If<T, U> extends Operator<T, U> {

    protected Operator<T, ?> __else;
    protected LinkedList<i.tuple.Pair<Operator.Func<T,Boolean>,Operator<T, ?>>> __conditions = new LinkedList<>();


    public <V> If(Operator.Func<T,Boolean> condition,i.func.Single<T, V> f){
        __conditions.add(new i.tuple.Pair<>(condition,Block(f)));
    }

    public <V> If<T, U> ElseIf(Operator.Func<T,Boolean> condition,i.func.Single<T, V> f){
        if(__else==null){
            __conditions.add(new i.tuple.Pair<>(condition,Block(f)));
        } else {
            Log.e("", new RuntimeException("else!=null"));
        }
        return this;
    }

    public <V> If<T, U> Else(i.func.Single<T, V> f){
        if(__else==null){
            __else = Op(f);
        } else {
            Log.e("", new RuntimeException("else!=null"));
        }
        return this;
    }

    @Override
    protected Task<T> on(Task<T> task, T o) {
        for(i.tuple.Pair<Operator.Func<T,Boolean>,Operator<T, ?>> condition : __conditions){
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
        task.down(__else, o);
        return null;
    }
}
