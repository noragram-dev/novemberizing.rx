package novemberizing.rx.on;

import novemberizing.rx.On;

/**
 *
 * @author novemberizing, novemberizing.rx@novemberizing.net
 * @since 2017. 1. 9.
 */
@SuppressWarnings("unused")
public interface Pair<A, B> extends On { void on(A first, B second); }