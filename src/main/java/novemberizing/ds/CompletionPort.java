package novemberizing.ds;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 15
 */
public interface CompletionPort {
    void dispatch(Executable executable);
}
