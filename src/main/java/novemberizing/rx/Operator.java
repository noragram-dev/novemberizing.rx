package novemberizing.rx;

import com.google.gson.annotations.Expose;
import novemberizing.ds.Exec;
import novemberizing.ds.Func;

import novemberizing.rx.operator.Just;
import novemberizing.rx.operator.Single;

/**
 *
 * @author novemberizing, novemberizing@gmail.com
 * @since 2017. 1. 12.
 */
public interface Operator<T, U> extends Exec<T, Task<T,U>> {


    class Exec<T, U> extends Command {
        @Expose protected Operator<T, U> __op;
        @Expose protected Task<T, ?> __task;

        public Exec(Operator<T, U> op, Task<T, ?> task){
            __op = op;
        }

        @Override
        public void execute() {
            Task<T, U> task = __op.exec(__task.in);
            if (task.done()) {
                /**
                 * next execute
                 */
            } else {
                /**
                 * wait to finish task ...
                 */
                // __task.child(task);
            }
        }
    }

    static <T> Operator<T, T> Just(){ return new Just<>(); }

    static <T> Operator<T, T> Single(Func<T, T> f){
        return new Single<T>(){
            @Override
            protected Task<T, T> on(Task<T, T> task) {
                task.out = (f!=null ? f.call(task.in) : task.in);
                return task;
            }
        };
    }

    static <T, U> Operator<T, U> Op(Func<T, U> f){
        return new novemberizing.rx.operator.Operator<T, U>(){
            @Override
            protected Task<T, U> on(Task<T, U> task) {
                if(f!=null){
                    task.out = f.call(task.in);
                }
                return task;
            }
        };
    }


}
