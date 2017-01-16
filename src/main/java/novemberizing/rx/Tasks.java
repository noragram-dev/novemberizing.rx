package novemberizing.rx;

import novemberizing.ds.CompletionPort;
import novemberizing.ds.Executable;

import java.util.Collection;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 16
 */
public class Tasks extends Task<Collection<Task>, Collection<Task>> {

    public Tasks(Collection<Task> in, Collection<Task> out) {
        super(in, out);
    }

    public void add(Task task){
        task.add(new CompletionPort() {
            @Override
            synchronized public void dispatch(Executable executable) {
                out.add(task);
                if(in.size()==out.size()){
                    __completed = true;
                }
            }
        });
    }
}
