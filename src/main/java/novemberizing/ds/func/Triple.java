package novemberizing.ds.func;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 17.
 */
public interface Triple<A, B, C, Z> { Z call(A first, B second, C third); }