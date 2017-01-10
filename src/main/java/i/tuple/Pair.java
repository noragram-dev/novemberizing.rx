package i.tuple;

import com.google.gson.annotations.Expose;

/**
 *
 * @author novemberizing, i@novemberizing.net
 * @since 2017. 1. 9.
 */
@SuppressWarnings("unused")
public class Pair<A, B> extends i.tuple.Single<A> {
    @Expose public B second;
    public Pair(){
        super();
        second = null;
    }
    public Pair(A first, B second){
        super(first);
        this.second = second;
    }
}
