package novemberizing.rx;

/**
 *
 * @author novemberizing, novemberizing.rx@novemberizing.net
 * @since 2017. 1. 10.
 */
public abstract class Command implements Executable {
    protected Executor __executor;

    public abstract void execute();

    @Override
    public void execute(Executor executor) {
        __executor = executor;
        execute();
    }

    public void complete(){
        __executor.completed(this);
    }

    public void executed(){
        __executor.executed(this);
    }
}
