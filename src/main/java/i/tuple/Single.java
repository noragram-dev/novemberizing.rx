package i.tuple;

import com.google.gson.annotations.Expose;

/**
 *
 * @author novemberizing, i@novemberizing.net
 * @since 2017. 1. 9.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Single<A> extends i.tuple.Empty {
    @Expose public A first;
    public Single(){ this.first = null; }
    public Single(A first){ this.first = first; }
}
