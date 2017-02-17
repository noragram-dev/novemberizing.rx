package novemberizing.rx.operators;

import novemberizing.rx.Operator;
import novemberizing.rx.Subscriber;
import novemberizing.util.Log;

import java.util.Collection;
import java.util.LinkedList;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 18.
 */
@SuppressWarnings({"WeakerAccess", "DanglingJavadoc"})
public class Block {
    private static final String Tag = "Block";

    public static class Op<T, Z> extends Operator<T, Z> {
        protected boolean __ret = false;
        protected Header<T, ?, Z> __header = null;

        public boolean ret(){ return __ret; }

        protected void ret(boolean v){ __ret = v; }

        @Override
        protected void on(Task<T, Z> task, T in) {
            __header.in(task, in);
        }

        protected Block.Header.On<T, Z> header(Block.Header.On<T, Z> on){
            __header = on;
            return on;
        }

        protected <U> Block.Header.Run<T, U, Z> header(Block.Header.Run<T, U, Z> run){
            __header = run;
            return run;
        }

        protected <U> Block.Header.Func<T, U, Z> header(Block.Header.Func<T, U, Z> func){
            __header = func;
            return func;
        }
    }

    public static <T, Z> Block.Header.On<T, Z> begin(){
        Block.Op<T, Z> block = new Block.Op<>();
        return block.header(new Block.Header.On<>(block));
    }

    public static <T, U, Z> Block.Header.Run<T, U, Z> begin(novemberizing.ds.func.Empty<U> f){
        Block.Op<T, Z> block = new Block.Op<>();
        return block.header(new Block.Header.Run<>(block,f));
    }

    public static <T, U, Z> Block.Header.Func<T, U, Z> begin(novemberizing.ds.func.Single<T, U> f){
        Block.Op<T, Z> block = new Block.Op<>();
        return block.header(new Block.Header.Func<>(block,f));
    }

    public static class Next<T, V, Z> extends Subscriber<V> {
        protected Operator.Task<T, Z> __task;
        protected Line<T, V, ?, Z> __next;
        protected final LinkedList<V> __items = new LinkedList<>();
        protected novemberizing.ds.func.Pair<T, V, Boolean> __condition;
        protected novemberizing.ds.func.Pair<T, V, V> __composer;
        protected boolean __subscribe = true;

        public Next(Operator.Task<T, Z> task, Block.Line<T, V, ?, Z> next, novemberizing.ds.func.Pair<T, V, Boolean> condition, novemberizing.ds.func.Pair<T, V, V> composer){
            __task = task;
            __next = next;
            __condition = condition;
            __composer = composer;
        }

        @Override
        public void onNext(V o) {
            synchronized (this){
                if(__condition==null || __condition.call(__task.in(), o)) {
                    __items.addLast(__composer!=null ? __composer.call(__task.in(), o) : o);
                }
            }
        }

        @Override
        public void onError(Throwable e) {
            Log.e(Tag, e);
        }

        @Override
        public void onComplete() {
            if(__next!=null){
                if(__items.size()>0){
                    __next.bulk(__task, __items);
                } else {
                    Log.d(Tag, "return void");
                    __task.complete();
                }
            } else {
                Log.d(Tag, "return void");
                __task.complete();
            }
        }

        @Override public void subscribe(boolean v){ __subscribe = v; }

        @Override public boolean subscribed(){ return __subscribe; }
    }

    public static class Complete<T, Z> extends Subscriber<Z> {
        protected Operator.Task<T, Z> __task;
        protected boolean __subscribe = true;

        public Complete(Operator.Task<T, Z> task){
            __task = task;
        }

        @Override
        public void onNext(Z o) {
            __task.next(o);
        }

        @Override
        public void onError(Throwable e) {
            __task.error(e);
        }

        @Override
        public void onComplete() {
            __task.complete();
        }

        @Override public void subscribe(boolean v){ __subscribe = v; }

        @Override public boolean subscribed(){ return __subscribe; }
    }

    public static class Line<T, U, V, Z> {
        protected Operator<U, V> __operator;
        protected Block.Op<T, Z> __parent;
        protected Line<T, V, ?, Z> __next;
        protected novemberizing.ds.func.Pair<T, V, V> __composer;
        protected novemberizing.ds.func.Pair<T, V, Boolean> __condition;
        /**
         * ret()
         * close()
         */
        public Line(Block.Op<T, Z> parent, Operator<U, V> operator){
            __parent = parent;
            __operator = operator;
            __next = null;
            __composer = null;
            __condition = null;
        }

        public Line<T, U, V, Z> composer(novemberizing.ds.func.Pair<T, V, V> composer){
            __composer = composer;
            return this;
        }

        public Line<T, U, V, Z> condition(novemberizing.ds.func.Pair<T, V, Boolean> condition){
            __condition = condition;
            return this;
        }

        public <W> Line<T, V, W, Z> next(Operator<V, W> op){
            Line<T, V, W, Z> next = new Line<>(__parent, op);
            __next = next;
            return next;
        }

        public <W> Line<T, V, W, Z> next(novemberizing.ds.func.Single<V,W> f){ return next(Operator.Op(f)); }

        public Block.Op<T, Z>  ret(){
            if(__parent.ret()){
                Log.e(Tag, new RuntimeException("__parent.ret()"));
                return __parent;
            }
            __parent.ret(true);
            return __parent;
        }

        public Block.Op<T, Z>  ret(novemberizing.ds.func.Empty<Z> f){
            return ret(new novemberizing.ds.func.Single<V, Z>(){
                @Override
                public Z call(V o) {
                    return f.call();
                }
            });
        }

        public Block.Op<T, Z>  ret(novemberizing.ds.func.Single<V,Z> f){ return ret(Operator.Op(f)); }

        public Block.Op<T, Z>  ret(Operator<V,Z> op){
            if(__parent.ret()){
                Log.e(Tag, new RuntimeException("__parent.ret()"));
                return __parent;
            }
            __parent.ret(true);
            __next = new Footer<>(__parent, op);
            return __parent;
        }

        protected void exec(Operator.Task<T, Z> task, U o){
            __operator.exec(o).subscribe(new Next<>(task, __next, __condition, __composer));
        }
        protected void bulk(Operator.Task<T, Z> task, Collection<U> items){
            __operator.bulk(items).subscribe(new Next<>(task, __next, __condition, __composer));
        }
    }

    public static class Footer<T, V, Z> extends Block.Line<T, V, Z, Z> {

        public Footer(Op<T, Z> parent, Operator<V, Z> operator) {
            super(parent, operator);
        }

        protected void exec(Operator.Task<T, Z> task, V o){
            __operator.exec(o).subscribe(new Block.Complete<>(task));
        }
        protected void bulk(Operator.Task<T, Z> task, Collection<V> items){
            __operator.bulk(items).subscribe(new Block.Complete<>(task));
        }

    }

    protected static abstract class Header<T, U, Z> {
        protected Block.Op<T, Z> __parent;
        protected Line<T, U, ?, Z> __next;
        protected novemberizing.ds.func.Pair<T, U, U> __composer;
        protected novemberizing.ds.func.Pair<T, U, Boolean> __condition;

        public Header(Op<T, Z> parent) {
            __parent = parent;
        }

        protected abstract void in(Operator.Task<T, Z> task, T in);

        public Header<T, U, Z> composer(novemberizing.ds.func.Pair<T, U, U> composer){
            __composer = composer;
            return this;
        }

        public Header<T, U, Z> condition(novemberizing.ds.func.Pair<T, U, Boolean> condition){
            __condition = condition;
            return this;
        }

        public <V> Line<T, U, V, Z> next(Operator<U, V> op){
            Line<T, U, V, Z> next = new Line<>(__parent, op);
            __next = next;
            return next;
        }

        public <V> Line<T, U, V, Z> next(novemberizing.ds.func.Single<U, V> f){ return next(Operator.Op(f)); }

        public Block.Op<T, Z>  ret(){
            if(__parent.ret()){
                Log.e(Tag, new RuntimeException("__parent.ret()"));
                return __parent;
            }
            __parent.ret(true);
            return __parent;
        }

        public Block.Op<T, Z>  ret(novemberizing.ds.func.Empty<Z> f){
            return ret(new novemberizing.ds.func.Single<U, Z>(){
                @Override
                public Z call(U o) {
                    return f.call();
                }
            });
        }

        public Block.Op<T, Z>  ret(novemberizing.ds.func.Single<U ,Z> f){ return ret(Operator.Op(f)); }

        public Block.Op<T, Z>  ret(Operator<U ,Z> op){
            if(__parent.ret()){
                Log.e(Tag, new RuntimeException("__parent.ret()"));
                return __parent;
            }
            __parent.ret(true);
            __next = new Footer<>(__parent, op);
            return __parent;
        }

        public static class Func<T, U, Z> extends Header<T, U, Z> {
            private novemberizing.ds.func.Single<T, U> __func;

            public Func(Op<T, Z> parent, novemberizing.ds.func.Single<T, U> f) {
                super(parent);
                __func = f;
            }

            @Override
            protected void in(Operator.Task<T, Z> task, T in) {
                __next.exec(task, __func.call(in));
            }
        }

        public static class Run<T, U, Z> extends Header<T, U, Z> {
            private novemberizing.ds.func.Empty<U> __func;

            public Run(Op<T, Z> parent, novemberizing.ds.func.Empty<U> f) {
                super(parent);
                __func = f;
            }

            @Override
            protected void in(Operator.Task<T, Z> task, T in) {
                __next.exec(task, __func.call());
            }
        }

        public static class On<T, Z> extends Header<T, T, Z> {

            public On(Op<T, Z> parent) {
                super(parent);
            }

            @Override
            protected void in(Operator.Task<T, Z> task, T in) {
                __next.exec(task, in);
            }
        }

//        public static class Call<T, U, V, Z> extends Header<T, U, V, Z> {
//            private novemberizing.ds.func.Empty<U> __func;
//
//            public Call(Op<T, Z> parent, novemberizing.ds.func.Empty<U> f, Operator<U, V> operator) {
//                super(parent, operator);
//                __func = f;
//            }
//
//            @Override
//            protected void in(Operator.Task<T, Z> task, T in) {
//                __operator.exec(__func.call()).subscribe(new Next<>(task, __next, __condition, __composer));
//            }
//        }
//
//        public static class Func<T, U, V, Z> extends Header<T, U, V, Z> {
//            private novemberizing.ds.func.Single<T, U> __func;
//
//            public Func(Op<T, Z> parent, novemberizing.ds.func.Single<T, U> f, Operator<U, V> operator) {
//                super(parent, operator);
//                __func = f;
//            }
//
//            @Override
//            protected void in(Operator.Task<T, Z> task, T in) {
//                __operator.exec(__func.call(in)).subscribe(new Next<>(task, __next, __condition, __composer));
//            }
//        }
    }



//    public static class Header {
//        public static class Line<T, Z> {
//            public void exec(Operator.Task<T, Z> task, T in){
//
//            }
//        }
//
//        public static class On<T, Z> extends Header.Line<T, Z> {
//
//        }
//    }
//
//    public static class Op<T, Z> extends Operator<T, Z> {
//        protected Header.Line<T, Z> __header;
//
//        @Override
//        protected void on(Operator.Task<T, Z> task, T in) {
//            __header.exec(task, in);
//        }
//    }
//
//    public static class On<T, Z> extends Op<T, Z> {
//
//        public Header.Line<T, Z> begin(){
//            return null;
//        }
//    }
//
//    public static <T, U, Z> Block.On<T, Z> On(novemberizing.ds.func.Single<T, U> f){
//        /**
//         * T U
//         */
//    }
//
//    public static class void main(String[] args){
//        Block.On
////        Block.On()
//    }

//    public static class Func<T, Z> extends Operator<T, Z> {
//
//    }



//    public static class Statement<T, Z, U, V> {
//        private static final String Tag = "Statement";
//        protected Block<T, Z> __parent;
//        protected Operator<U, V> __operator;
//        protected Statement<T, Z, V, ?> __next;
//        protected boolean __end = false;
//
//        public Statement(Block<T ,Z> parent, Operator<U, V> operator){
//            __operator = operator;
//            __parent = parent;
//        }
//
//        public <W> Statement<T, Z, V, W> append(Operator<V, W> op){
//            Statement<T, Z, V, W> next = new Statement<>(__parent, op);
//            __next = next;
//            return next;
//        }
//
//        public <W> Statement<T, Z, V, W> append(novemberizing.rx.Func<V, W> f){
//            Statement<T, Z, V, W> next = new Statement<>(__parent, Operator.Op(f));
//            __next = next;
//            return next;
//        }
//
//        public Block<T, Z> end(Operator<V, Z> ret){
//            __next = new Footer<>(__parent, ret);
//            return __parent;
//        }
//
//        public Block<T, Z> end(novemberizing.rx.Func<V, Z> f){
//            __next = new Footer<>(__parent, Operator.Op(f));
//            return __parent;
//        }
//
//        public void foreach(Operator.Task<T, Z> task, Collection<U> items){
//            __operator.bulk(items).subscribe(new Subscriber<V>() {
//                private LinkedList<V> __items = new LinkedList<>();
//                @Override
//                public void onNext(V o) {
//                    Log.f(Tag, this, o);
//                    __items.addLast(o);
//                }
//
//                @Override
//                public void onError(Throwable e) {
//                    Log.f(Tag, this, e);
//                    e.printStackTrace();
//                }
//
//                @Override
//                public void onComplete() {
//                    Log.f(Tag, this, "complete");
//                    if(__next!=null && __items.size()>0) {
//                        __next.foreach(task, __items);
//                        __items.clear();
//                    } else {
//                        Log.e(Tag, new RuntimeException("block is not close"));
//                        __items.clear();
//                        task.complete();
//                    }
//                }
//            });
//        }
//
//        public void exec(Operator.Task<T, Z> task, U in){
//            __operator.exec(in).subscribe(new Subscriber<V>() {
//                private LinkedList<V> __items = new LinkedList<>();
//                @Override
//                public void onNext(V o) {
//                    Log.f(Tag, this, o);
//                    __items.addLast(o);
//                }
//
//                @Override
//                public void onError(Throwable e) {
//                    Log.f(Tag, this, e);
//                }
//
//                @Override
//                public void onComplete() {
//                    Log.f(Tag, this, "complete");
//                    if(__next!=null && __items.size()>0) {
//                        __next.foreach(task, __items);
//                        __items.clear();
//                    } else {
//                        Log.e(Tag, new RuntimeException("block is not close"));
//                        __items.clear();
//                        task.complete();
//                    }
//                }
//            });
//        }
//    }
//
//    public static class Footer<T, U, Z> extends Statement<T, Z, U, Z> {
//        private static final String Tag = "Footer";
//
//        public Footer(Block<T, Z> parent, Operator<U, Z> operator) {
//            super(parent, operator);
//            __end = true;
//        }
//        public void foreach(Operator.Task<T, Z> task, Collection<U> items){
//            __operator.bulk(items).subscribe(new Subscriber<Z>() {
//                @Override
//                public void onNext(Z o) {
//                    Log.f(Tag, this, o);
//                    task.next(o);
//                }
//
//                @Override
//                public void onError(Throwable e) {
//                    Log.f(Tag, this, e);
//                    e.printStackTrace();
//                }
//
//                @Override
//                public void onComplete() {
//                    Log.f(Tag, this, "complete");
////                    Operator.Bulk(__parent, __items);
////                    __items.clear();
//                    task.complete();
//                }
//            });
//        }
//
//        public void exec(Operator.Task<T, Z> task, U in){
//            __operator.exec(in).subscribe(new Subscriber<Z>() {
//                @Override
//                public void onNext(Z o) {
//                    Log.f(Tag, this, o);
//                    task.next(o);
//                }
//
//                @Override
//                public void onError(Throwable e) {
//                    Log.f(Tag, this, e);
//                }
//
//                @Override
//                public void onComplete() {
//                    Log.f(Tag, this, "complete");
//                    task.complete();
//                }
//            });
//        }
//    }
//
//    public abstract static class Header<T, Z, U> {
//        protected Block<T, Z> __parent;
//        protected Block.Statement<T, Z, U, ?> __next;
//
//        public abstract void in(Task<T, Z> task, T o);
//
//        public Header(Block<T, Z> parent){
//            __parent = parent;
//        }
//
//        public <W> Statement<T, Z, U, W> append(Operator<U, W> op){
//            Statement<T, Z, U, W> next = new Statement<>(__parent, op);
//            __next = next;
//            return next;
//        }
//
//        public <W> Statement<T, Z, U, W> append(novemberizing.rx.Func<U, W> f){
//            Statement<T, Z, U, W> next = new Statement<>(__parent, Operator.Op(f));
//            __next = next;
//            return next;
//        }
//
//        public Block<T, Z> end(Operator<U, Z> op){
//            __next = new Footer<>(__parent, op);
//            return __parent;
//        }
//
//        public Block<T, Z> end(novemberizing.rx.Func<U, Z> f){
//            __next = new Footer<>(__parent, Operator.Op(f));
//            return __parent;
//        }
//    }
//
//    public static class Call<T, Z, U> extends Header<T, Z, U> {
//        private static final String Tag = "Block.Call";
//        protected novemberizing.ds.ret.Empty<U> __func;
//
//        public Call(Block<T, Z> parent, novemberizing.ds.ret.Empty<U> ret) {
//            super(parent);
//            __func = ret;
//        }
//
//        @Override
//        public void in(Operator.Task<T, Z> task,T o) {
//            Log.i(Tag, this, task, o);
//            __next.exec(task, __func.ret());
//        }
//    }
//
//    public static class Func<T, Z, U> extends Header<T, Z, U> {
//        private static final String Tag = "Block.Func";
//        protected novemberizing.rx.Func<T, U> __func;
//
//        public Func(Block<T, Z> parent, novemberizing.rx.Func<T, U> f){
//            super(parent);
//            __func = f;
//        }
//
//        @Override
//        public void in(Task<T, Z> task,T o) {
//            Log.f(Tag, this, task, o);
//            __next.exec(task, __func.call(o));
//        }
//    }
//
//    protected Header<T, Z, ?> __header;
//
//    public Block(){
//        __header = null;
//    }
//
//    public Header<T, Z, T> start(){
//        if(__header==null) {
//            Header<T, Z, T> header = new Block.Func<>(this, new novemberizing.rx.Func<T, T>() {
//                @Override
//                public T call(T in) {
//                    return in;
//                }
//            });
//            __header = header;
//            return header;
//        } else {
//            throw new RuntimeException("__header!=null");
//        }
//    }
//
//
//    public <U> Header<T, Z, U> start(novemberizing.rx.Func<T, U> f){
//        Header<T, Z, U> header = new Block.Func<>(this, f);
//        __header = header;
//        return header;
//    }
//
//    public <U> Header<T, Z, U> start(novemberizing.ds.ret.Empty<U> ret){
//        Header<T, Z, U> header = new Block.Call<>(this, ret);
//        __header = header;
//        return header;
//    }
//
//    @Override
//    protected void on(Task<T, Z> task, T in) {
//        __header.in(task, in);
//    }
}
