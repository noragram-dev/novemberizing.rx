package novemberizing.rx.tuple;

import com.google.gson.annotations.Expose;

/**
 *
 * @author novemberizing, novemberizing.rx@novemberizing.net
 * @since 2017. 1. 9.
 */
@SuppressWarnings("unused")
public class Pair<A, B> extends novemberizing.rx.tuple.Single<A> {
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
