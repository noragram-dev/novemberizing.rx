package novemberizing.rx;

import novemberizing.ds.Exec;
import novemberizing.ds.Func;
import novemberizing.ds.Task;
import novemberizing.rx.operator.Just;
import novemberizing.rx.operator.Single;

/**
 *
 * @author novemberizing, novemberizing@gmail.com
 * @since 2017. 1. 12.
 */
public interface Operator<T, U> extends Exec<T, U> {
    class Work<T> extends Command {
        protected Operator<T, ?> __op;
        protected T __in;
        protected Task<T, ?> task;
        public Work(Operator<T, ?> op, T o, Task<T, ?> task){

        }

        @Override
        public void execute() {

        }
        // op, o, task;
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
