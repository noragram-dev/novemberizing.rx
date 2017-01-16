package novemberizing.rx.operators;

import novemberizing.ds.Func;
import novemberizing.ds.func.Pair;
import novemberizing.rx.Observable;
import novemberizing.rx.Operator;

import java.util.LinkedList;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 15
 */
public class If<T> extends Operator<T, T> {
    private final LinkedList<Condition<T, ?>> __conditions = new LinkedList<>();
    private Condition<T, ?> __else;

    public <U> If(Condition<T, ?> condition) {
        __else = null;
        synchronized (__conditions) {
            __conditions.addLast(condition);
        }
    }

    @Override
    protected void on(Local<T, T> task) {
        for(Condition<T, ?> condition : __conditions){
            if(condition.interest()){
                condition.exec(task.in);
                return;
            }
        }
        if(__else!=null){

        }
//        for(Condition<T, ?> condition)
    }
}
