package novemberizing.ds;

import novemberizing.ds.tuple.Triple;
import novemberizing.ds.tuple.Empty;
import novemberizing.ds.tuple.Pair;
import novemberizing.ds.tuple.Single;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 18.
 */
public interface Tuple {
    static Empty New(){ return new Empty(); }
    static <A> Single<A> New(A first){ return new Single<>(first); }
    static <A, B> Pair<A, B> New(A first, B second){ return new Pair<>(first, second); }
    static <A, B, C> Triple<A, B, C> New(A first, B second, C third){ return new Triple<>(first, second, third); }

    static Empty Empty(){ return new Empty(); }

    static <A> Single<A> Single(){ return new Single<>(); }
    static <A> Single<A> Single(A first){ return new Single<>(first); }

    static <A, B> Pair<A, B> Pair(){ return new Pair<>(); }
    static <A, B> Pair<A, B> Pair(A first, B second){ return new Pair<>(first, second); }

    static <A, B, C> Triple<A, B, C> Triple(){ return new Triple<>(); }
    static <A, B, C> Triple<A, B, C> Triple(A first, B second, C third){ return new Triple<>(first, second, third); }
}
