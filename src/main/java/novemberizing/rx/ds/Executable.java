package novemberizing.rx.ds;

/**
 *
 * @author novemberizing, novemberizing@gmail.com
 * @since 2017. 1. 12.
 */
public interface Executable {
    void execute(Executor executor);
    void execute();
    void executed();
    void completed();
}
