package i.rx.func;

/**
 *
 * @author novemberizing, i@novemberizing.net
 * @since 2017. 1. 9.
 */
@SuppressWarnings("unused")
public interface Single<A, Z> extends i.rx.Func<Z> { Z call(A first); }
