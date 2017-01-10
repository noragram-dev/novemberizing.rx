package i.run;

import i.Run;

/**
 *
 * @author novemberizing, i@novemberizing.net
 * @since 2017. 1. 9.
 */
@SuppressWarnings("unused")
public interface Triple<A, B, C> extends Run { void run(A first, B second, C third); }
