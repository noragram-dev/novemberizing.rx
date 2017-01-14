package novemberizing.rx.old.operator;

import novemberizing.rx.ds.On;
import novemberizing.rx.ds.tuple.Pair;
import novemberizing.rx.old.des.Scheduler;
import novemberizing.rx.old.task.Task;
import novemberizing.util.Log;

import java.util.LinkedList;

/**
 *
 * @author novemberizing, novemberizing@gmail.com
 * @since 2017. 1. 12.
 */
@SuppressWarnings("DanglingJavadoc")
public class If<T, U> extends Operator<T, U> {
    private static final String Tag = "If";

    private LinkedList<Pair<Condition<T>, Operator<T, ?>>> __conditions = new LinkedList<>();
    private Operator<T, ?> __else;

    public If(Condition<T> condition, novemberizing.rx.ds.Func<T, ?> f){
        __conditions.addLast(new Pair<>(condition, (Operator<T, ?>) Op(f)));
        __else = null;
    }

    public If<T, ?> ElseIf(Condition<T> condition, novemberizing.rx.ds.Func<T, ?> f){
        if(__else!=null){
            Log.e(Tag, new RuntimeException("__else!=null"));
        } else {
            __conditions.addLast(new Pair<>(condition, (Operator<T, ?>) Op(f)));

        }
        return this;
    }

    public If<T, ?> Else(novemberizing.rx.ds.Func<T, ?> f){
        if(__else!=null){
            Log.e(Tag, new RuntimeException("__else!=null"));
        } else {
            __else = (Operator<T, U>) Op(f);

        }
        return this;
    }

    @Override
    protected Task<T, U> on(Task<T, U> task) {
        for(Pair<Condition<T>, Operator<T, ?>> condition : __conditions){
            if(condition.first.call(task.in)){
//                Exec(condition.second.build(task.in, task)).on(new On<novemberizing.rx.ds.Task>() {
//                    @Override
//                    public void on(novemberizing.rx.ds.Task v) {
//                        Log.i(Tag, v);
//                        task.next();
//                    }
//                });
                return null;
            }
        }
        if(__else!=null){
//            Exec(__else.build(task.in, task)).on(new On<novemberizing.rx.ds.Task>() {
//                @Override
//                public void on(novemberizing.rx.ds.Task v) {
//                    Log.i(Tag, v);
//                    task.next();
//                }
//            });
        }
        return null;
    }

    private static int count = 0;
    public static void main(String[] args) {
        //Log.disable();
//        Log.depth(3);
//        String[] strings = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
//        Operator.Foreach(Op((String o)->(Integer.parseInt(o)),
//                If(o->o>6,(Integer o)->"9")
//                        .ElseIf(o->o>3,o->"6")
//                        .ElseIf(o->o>0,o->"3")
//                        .Else(o->"0")
//
//                ),strings).on(o->Log.i(Tag, o));
//        Scheduler.Local().clear();
//        Operator.Foreach(
//                Op((String o)->(Integer.parseInt(o))).next
//                        next(If((Integer o)->o>6,o->9))
//
//
//                , args);
    }
}
