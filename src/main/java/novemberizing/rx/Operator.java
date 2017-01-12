package novemberizing.rx;

/**
 *
 * @author novemberizing, novemberizing@gmail.com
 * @since 2017. 1. 12.
 */
public class Operator<T, U> {

    public static class Task {

    }

    public static int Exec(Task task){
        Operator op = task.op();



//        op.exec(task);
        return 0;
    }

//    public Task exec(Task task){
//        return task;
//    }

//    protected Operator<U, ?> __next;
//
//    public Task exec(Task task){
//
//    }
//
//    public static void main(String[] args){
//        Task task = new Task();
//        Operator<?,?> current = new Operator<>();
//        do {
//            task = current.exec(task);
//            if(task.completed()){
//                current = current.__next;
//            } else {
//                // new task
//            }
//        } while(true);
//    }
//
//    /**
//     *
//     * @author novemberizing, novemberizing@gmail.com
//     * @since 2017. 1. 12.
//     */
//    public static class Task {
//    }
}
