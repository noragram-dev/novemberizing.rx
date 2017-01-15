package novemberizing.ds.tuple;

import com.google.gson.annotations.Expose;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 16.
 */

public class Single<A> extends Empty {
    @Expose public A first;

    public Single(){
        this.first = null;
    }

    public Single(A first){
        this.first = first;
    }
}
