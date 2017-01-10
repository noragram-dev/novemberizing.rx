package i.func;

import i.Func;

/**
 *
 * @author novemberizing, i@novemberizing.net
 * @since 2017. 1. 9.
 */
@SuppressWarnings("unused")
public interface Pair<A, B, Z> extends Func<Z> { Z call(A first, B second); }
