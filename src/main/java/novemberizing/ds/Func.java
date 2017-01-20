package novemberizing.ds;

/**
 *
 * @author novemberiizng, me@novemberizing.net
 * @since 2017. 1. 20.
 */
public interface Func {
    class Exec {
        public static class Empty<Z> implements novemberizing.ds.Exec {
            private novemberizing.ds.func.Empty<Z> __func;
            private novemberizing.ds.on.Single<Z> __ret;

            @Override
            public void exec(){
                __ret.on(__func.call());
            }

            public Empty(novemberizing.ds.func.Empty<Z> func, novemberizing.ds.on.Single<Z> ret){
                __func = func;
                __ret = ret;
            }
        }
        /**
         *
         * @author novemberiizng, me@novemberizing.net
         * @since 2017. 1. 20.
         */
        public static class Single<A, Z> implements novemberizing.ds.Exec {
            private novemberizing.ds.tuple.Single<A> __tuple;
            private novemberizing.ds.func.Single<A, Z> __func;
            private novemberizing.ds.on.Single<Z> __ret;

            @Override
            public void exec(){
                __ret.on(__func.call(__tuple.first));
            }

            public Single(novemberizing.ds.tuple.Single<A> tuple, novemberizing.ds.func.Single<A, Z> func, novemberizing.ds.on.Single<Z> ret){
                __tuple = tuple;
                __func = func;
                __ret = ret;
            }
        }

        public static class Pair<A, B, Z> implements novemberizing.ds.Exec {
            private novemberizing.ds.tuple.Pair<A, B> __tuple;
            private novemberizing.ds.func.Pair<A, B, Z> __func;
            private novemberizing.ds.on.Single<Z> __ret;

            @Override
            public void exec(){
                __ret.on(__func.call(__tuple.first, __tuple.second));
            }

            public Pair(novemberizing.ds.tuple.Pair<A, B> tuple, novemberizing.ds.func.Pair<A, B, Z> func, novemberizing.ds.on.Single<Z> ret){
                __tuple = tuple;
                __func = func;
                __ret = ret;
            }
        }

        public static class Triple<A, B, C, Z> implements novemberizing.ds.Exec {
            private novemberizing.ds.tuple.Triple<A, B, C> __tuple;
            private novemberizing.ds.func.Triple<A, B, C, Z> __func;
            private novemberizing.ds.on.Single<Z> __ret;

            @Override
            public void exec(){
                __ret.on(__func.call(__tuple.first, __tuple.second, __tuple.third));
            }

            public Triple(novemberizing.ds.tuple.Triple<A, B, C> tuple, novemberizing.ds.func.Triple<A, B, C, Z> func, novemberizing.ds.on.Single<Z> ret){
                __tuple = tuple;
                __func = func;
                __ret = ret;
            }
        }
    }
}
