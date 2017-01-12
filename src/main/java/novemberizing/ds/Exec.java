package novemberizing.ds;


/**
 *
 * @author novemberizing, i@novemberizing.net
 * @since 2017. 1. 13.
 */
@SuppressWarnings("unused")
public interface Exec<T, U> { Task<T, U> exec(T o); }
