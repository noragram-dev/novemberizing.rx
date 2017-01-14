package novemberizing.rx.ds.tuple;

/**
 *
 * @author novemberizing, novemberizing@gmail.com
 * @since 2017. 1. 12.
 */
public class Single<A> extends Empty {
    public A first;

    public Single(){
        first = null;
    }

    public Single(A first){
        this.first = first;
    }
}
