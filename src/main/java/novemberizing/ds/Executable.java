package novemberizing.ds;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 14
 */
@SuppressWarnings("unused")
public interface Executable {
    void execute(Executor executor);
//    void execute();
//    void executed();
//    void complete();
    boolean completed();
}
