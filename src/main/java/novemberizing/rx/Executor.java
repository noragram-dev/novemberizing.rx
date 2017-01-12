package novemberizing.rx;

/**
 *
 * @author novemberizing, novemberizing@gmail.com
 * @since 2017. 1. 12.
 */
public abstract class Executor implements Cyclable {
    public abstract void executed(Executable executable);
    public abstract void completed(Executable executable);
}
