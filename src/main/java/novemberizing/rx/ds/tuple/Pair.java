package novemberizing.rx.ds.tuple;

/**
 *
 * @author novemberizing, novemberizing@gmail.com
 * @since 2017. 1. 12.
 */
public class Pair<A, B> extends Single<A> {
    public B second;

    public Pair(A first, B second){
        super(first);
        this.second = second;
    }

    public Pair(){
        super();
        this.second = null;
    }
}
