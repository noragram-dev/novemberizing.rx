package novemberizing.rx;

import novemberizing.util.Log;

import java.util.Collection;
import java.util.LinkedList;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 18.
 */
@SuppressWarnings("WeakerAccess")
public class Block<T, Z> extends Operator<T, Z> {


    public static class Statement<T, Z, U, V> {
        private static final String Tag = "Statement";
        protected Block<T, Z> __parent;
        protected Operator<U, V> __operator;
        protected Statement<T, Z, V, ?> __next;
        protected boolean __end = false;

        public Statement(Block<T ,Z> parent, Operator<U, V> operator){
            __operator = operator;
            __parent = parent;
        }

        public <W> Statement<T, Z, V, W> append(Operator<V, W> op){
            Statement<T, Z, V, W> next = new Statement<>(__parent, op);
            __next = next;
            return next;
        }

        public Block<T, Z> end(Operator<V, Z> ret){
            __next = new Footer<>(__parent, ret);
            return __parent;
        }

        public Block<T, Z> end(novemberizing.rx.Func<V, Z> f){
            __next = new Footer<>(__parent, Operator.Op(f));
            return __parent;
        }

        public void foreach(Operator.Task<T, Z> task, Collection<U> items){
            __operator.bulk(items).subscribe(new Subscriber<V>() {
                private LinkedList<V> __items = new LinkedList<>();
                @Override
                public void onNext(V o) {
                    Log.f(Tag, this, o);
                    __items.addLast(o);
                }

                @Override
                public void onError(Throwable e) {
                    Log.f(Tag, this, e);
                    e.printStackTrace();
                }

                @Override
                public void onComplete() {
                    Log.f(Tag, this, "complete");
                    if(__next!=null && __items.size()>0) {
                        __next.foreach(task, __items);
                        __items.clear();
                    } else {
                        //if(__end){
                            Log.e(Tag, new RuntimeException("block is not close"));
                            __items.clear();
                        //}
                        task.complete();
                    }
                }
            });
        }

        public void exec(Operator.Task<T, Z> task, U in){
            __operator.exec(in).subscribe(new Subscriber<V>() {
                private LinkedList<V> __items = new LinkedList<>();
                @Override
                public void onNext(V o) {
                    Log.f(Tag, this, o);
                    if(__end){
                        Log.i(Tag, "end");
                    }
                    __items.addLast(o);
                }

                @Override
                public void onError(Throwable e) {
                    Log.f(Tag, this, e);
                }

                @Override
                public void onComplete() {
                    Log.f(Tag, this, "complete");
                    if(__next!=null && __items.size()>0) {
                        __next.foreach(task, __items);
                        __items.clear();
                    } else {
                        // if(__end){
                            Log.e(Tag, new RuntimeException("block is not close"));
                            __items.clear();
                        // }
                        task.complete();
                        Log.f(Tag, "");
                    }
                }
            });
        }
    }

    public static class Footer<T, U, Z> extends Statement<T, Z, U, Z> {
        private static final String Tag = "Footer";

        public Footer(Block<T, Z> parent, Operator<U, Z> operator) {
            super(parent, operator);
            __end = true;
        }
        public void foreach(Operator.Task<T, Z> task, Collection<U> items){
            __operator.bulk(items).subscribe(new Subscriber<Z>() {
                private LinkedList<Z> __items = new LinkedList<>();
                @Override
                public void onNext(Z o) {
                    Log.f(Tag, this, o);
                    if(__end){
                        Log.f(Tag, "end");
                    }
                    __items.addLast(o);
                }

                @Override
                public void onError(Throwable e) {
                    Log.f(Tag, this, e);
                    e.printStackTrace();
                }

                @Override
                public void onComplete() {
                    Log.f(Tag, this, "complete");
                    if(__next!=null && __items.size()>0) {
                        __next.foreach(task, __items);
                        __items.clear();
                    } else {
                        if(__end){
                            Operator.Bulk(__parent, __items);
                            __items.clear();
                        }
                        task.complete();
                        Log.f(Tag, "");
                    }
                }
            });
        }

        public void exec(Operator.Task<T, Z> task, U in){
            __operator.exec(in).subscribe(new Subscriber<Z>() {
                private LinkedList<Z> __items = new LinkedList<>();
                @Override
                public void onNext(Z o) {
                    Log.f(Tag, this, o);
                    if(__end){
                        Log.i(Tag, "end");
                    }
                    __items.addLast(o);
                }

                @Override
                public void onError(Throwable e) {
                    Log.f(Tag, this, e);
                }

                @Override
                public void onComplete() {
                    Log.f(Tag, this, "complete");
                    if(__next!=null && __items.size()>0) {
                        __next.foreach(task, __items);
                        __items.clear();
                    } else {
                        if(__end){
                            for(Z o : __items){
                                __parent.emit(o);
                            }
                            __items.clear();
                        }
                        task.complete();
                        Log.f(Tag, "");
                    }
                }
            });
        }
    }

    public abstract static class Header<T, Z, U> {
        protected Block<T, Z> __parent;
        protected Block.Statement<T, Z, U, ?> __next;

        public abstract void in(Task<T, Z> task, T o);

        public Header(Block<T, Z> parent){
            __parent = parent;
        }

        public <W> Statement<T, Z, U, W> append(Operator<U, W> op){
            Statement<T, Z, U, W> next = new Statement<>(__parent, op);
            __next = next;
            return next;
        }

        public Block<T, Z> end(Operator<U, Z> op){
            __next = new Footer<>(__parent, op);
            return __parent;
        }

        public Block<T, Z> end(novemberizing.rx.Func<U, Z> f){
            __next = new Footer<>(__parent, Operator.Op(f));
            return __parent;
        }
    }

    public static class Call<T, Z, U> extends Header<T, Z, U> {
        private static final String Tag = "Block.Call";
        protected novemberizing.ds.ret.Empty<U> __func;

        public Call(Block<T, Z> parent, novemberizing.ds.ret.Empty<U> ret) {
            super(parent);
            __func = ret;
        }

        @Override
        public void in(Operator.Task<T, Z> task,T o) {
            Log.i(Tag, this, task, o);
            __next.exec(task, __func.ret());
        }
    }

    public static class Func<T, Z, U> extends Header<T, Z, U> {
        private static final String Tag = "Block.Func";
        protected novemberizing.rx.Func<T, U> __func;

        public Func(Block<T, Z> parent, novemberizing.rx.Func<T, U> f){
            super(parent);
            __func = f;
        }

        @Override
        public void in(Task<T, Z> task,T o) {
            Log.f(Tag, this, task, o);
            __next.exec(task, __func.call(o));
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
        __header.in(task, in);
    }

    public <U, V> Statement<T, Z, U, V> append(Operator<U, V> op){

        return null;
    }



}
