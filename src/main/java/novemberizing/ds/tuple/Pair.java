package novemberizing.ds.tuple;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 18.
 */
public class Pair<A, B> extends Single<A> {
    public B second;
    public Pair(){
        this.second = null;
    }
    public Pair(A first, B second){
        super(first);
        this.second = second;
    }
}