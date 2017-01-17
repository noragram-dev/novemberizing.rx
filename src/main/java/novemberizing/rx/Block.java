package novemberizing.rx;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 18.
 */
@SuppressWarnings("WeakerAccess")
public class Block<T, Z> extends Operator<T, Z> {

    public static class Statement<T, Z, U, V> {
        protected Block<T, Z> __parent;
        protected Operator<U, V> __operator;
        protected Operator<V, ?> __next;

        public Statement(Block<T ,Z> parent, Operator<U, V> operator){
            __operator = operator;
            __parent = parent;
        }

        public <W> Statement<T, Z, V, W> append(Operator<V, W> op){
            Operator<V, W> next = __operator.next(op);
            __next = next;
//            __next.subscribe(new Subscriber<?>() {
//                @Override
//                public void onNext(Object o) {
//
//                }
//
//                @Override
//                public void onError(Throwable e) {
//
//                }
//
//                @Override
//                public void onComplete() {
//
//                }
//            });
            return new Statement<>(__parent, next);
        }

        public Block<T, Z> end(Operator<V, Z> ret){
            __next = __operator.next(ret);
            return __parent;
        }

        public Block<T, Z> end(){
            __next = null;
            return __parent;
        }
    }

    public abstract static class Header<T, Z, U> {
        protected Block<T, Z> __parent;
        protected Operator<U, ?> __next;

        public abstract void in(T o);

        public Header(Block<T, Z> parent){
            __parent = parent;
        }

        public <W> Statement<T, Z, U, W> append(Operator<U, W> op){
            __next = op;
            return new Statement<>(__parent, op);
        }

        public Block<T, Z> end(Operator<U, Z> op){
            __next = op;
            return __parent;
        }

        public Block<T, Z> end(){
            __next = null;
            return __parent;
        }
    }

    public static class Call<T, Z, U> extends Header<T, Z, U> {
        protected novemberizing.ds.ret.Empty<U> __func;

        public Call(Block<T, Z> parent, novemberizing.ds.ret.Empty<U> ret) {
            super(parent);
            __func = ret;
        }

        @Override
        public void in(T o) {
            __next.exec(__func.ret());
        }
    }

    public static class Func<T, Z, U> extends Header<T, Z, U> {
        protected novemberizing.rx.Func<T, U> __func;

        public Func(Block<T, Z> parent, novemberizing.rx.Func<T, U> f){
            super(parent);
            __func = f;
        }

        @Override
        public void in(T o) {
            __next.exec(__func.call(o));
        }
    }

    protected Header<T, Z, ?> __header;

    public Block(){
        __header = null;
    }

    public Header<T, Z, T> start(){
        if(__header==null) {
            Header<T, Z, T> header = new Block.Func<>(this, new novemberizing.rx.Func<T, T>() {
                @Override
                public T call(T in) {
                    return in;
                }
            });
            __header = header;
            return header;
        } else {
            throw new RuntimeException("__header!=null");
        }
    }


    public <U> Header<T, Z, U> start(novemberizing.rx.Func<T, U> f){
        Header<T, Z, U> header = new Block.Func<>(this, f);
        __header = header;
        return header;
    }

    public <U> Header<T, Z, U> start(novemberizing.ds.ret.Empty<U> ret){
        Header<T, Z, U> header = new Block.Call<>(this, ret);
        __header = header;
        return header;
    }

    @Override
    protected void on(Task<T, Z> task, T in) {
        __header.in(in);
    }

    public <U, V> Statement<T, Z, U, V> append(Operator<U, V> op){

        return null;
    }


    public static void main(String[] args){
        Operator<String, Integer> stoi = Operator.Op(new novemberizing.rx.Func<String, Integer>() {
            @Override
            public Integer call(String o) {
                return Integer.parseInt(o);
            }
        });

        Operator<Integer, String> itos = Operator.Op(new novemberizing.rx.Func<Integer, String>() {
            @Override
            public String call(Integer o) {
                return Integer.toString(o);
            }
        });
        {
            Block<String, String> block = new Block<>();
            block.start().append(stoi).append(itos).append(stoi).end(itos);
        }

        {
            Block<String, String> block = new Block<>();
            block.start().append(stoi).append(itos).append(stoi).end();
        }
    }

}
