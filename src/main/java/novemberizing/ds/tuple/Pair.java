package novemberizing.ds.tuple;

import com.google.gson.annotations.Expose;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 16.
 */

public class Pair<A, B> extends Single<A> {
    @Expose public B second;

    public Pair(){
        super();
        this.second = null;
    }

    public Pair(A first,B second){
        super(first);
        this.second = second;
    }
}
