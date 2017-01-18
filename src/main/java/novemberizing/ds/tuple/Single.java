package novemberizing.ds.tuple;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 18.
 */
public class Single<A> extends Empty {
    public A first;
    public Single(){
        this.first = null;
    }
    public Single(A first){
        this.first = first;
    }
}
