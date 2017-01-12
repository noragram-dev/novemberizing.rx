package novemberizing.rx.func;

import novemberizing.rx.Func;

/**
 *
 * @author novemberizing, novemberizing.rx@novemberizing.net
 * @since 2017. 1. 9.
 */
@SuppressWarnings("unused")
public interface Single<A, Z> extends Func<Z> { Z call(A first); }