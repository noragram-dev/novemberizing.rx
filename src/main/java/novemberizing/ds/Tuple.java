package novemberizing.ds;

//import novemberizing.ds.tuple.Triple;
//import novemberizing.ds.tuple.Empty;
//import novemberizing.ds.tuple.Pair;
//import novemberizing.ds.tuple.Single;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 18.
 */
public interface Tuple {
    /**
     * Error:Tuple.java:17-18: Static method novemberizing.ds.tuple.Triple New(java.lang.Object first, java.lang.Object second, java.lang.Object third) not supported in Android API level less than 24
     Error:Tuple.java:15-16: Static method novemberizing.ds.tuple.Single New(java.lang.Object first) not supported in Android API level less than 24
     Error:Tuple.java:19-20: Static method novemberizing.ds.tuple.Empty Empty() not supported in Android API level less than 24
     Error:Tuple.java:24-25: Static method novemberizing.ds.tuple.Pair Pair() not supported in Android API level less than 24
     Error:Tuple.java:27-28: Static method novemberizing.ds.tuple.Triple Triple() not supported in Android API level less than 24
     Error:Tuple.java:14-15: Static method novemberizing.ds.tuple.Empty New() not supported in Android API level less than 24
     Error:com.android.jack.JackAbortException
     Error:Execution failed for task ':noragram:transformJackWithJackForArmv7Debug'.
     > com.android.build.api.transform.TransformException: com.android.jack.api.v01.CompilationException
     */


//    static Empty New(){ return new Empty(); }
//    static <A> Single<A> New(A first){ return new Single<>(first); }
//    static <A, B> Pair<A, B> New(A first, B second){ return new Pair<>(first, second); }
//    static <A, B, C> Triple<A, B, C> New(A first, B second, C third){ return new Triple<>(first, second, third); }
//
//    static Empty Empty(){ return new Empty(); }
//
//    static <A> Single<A> Single(){ return new Single<>(); }
//    static <A> Single<A> Single(A first){ return new Single<>(first); }
//
//    static <A, B> Pair<A, B> Pair(){ return new Pair<>(); }
//    static <A, B> Pair<A, B> Pair(A first, B second){ return new Pair<>(first, second); }
//
//    static <A, B, C> Triple<A, B, C> Triple(){ return new Triple<>(); }
//    static <A, B, C> Triple<A, B, C> Triple(A first, B second, C third){ return new Triple<>(first, second, third); }
}
