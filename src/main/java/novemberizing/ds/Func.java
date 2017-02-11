package novemberizing.ds;

/**
 *
 * @author novemberiizng, me@novemberizing.net
 * @since 2017. 1. 20.
 */
public interface Func {
    class Exec {
        public static class Empty<Z, CALLBACK extends novemberizing.ds.on.Single<Z>> implements novemberizing.ds.Exec<CALLBACK> {
            private novemberizing.ds.func.Empty<Z> __func;

            @Override
            public void exec(CALLBACK callback){
                callback.on(__func.call());
            }

            public Empty(novemberizing.ds.func.Empty<Z> func){
                __func = func;
            }
        }
        /**
         *
         * @author novemberiizng, me@novemberizing.net
         * @since 2017. 1. 20.
         */
        public static class Single<A, Z, CALLBACK extends novemberizing.ds.on.Single<Z>> implements novemberizing.ds.Exec<CALLBACK> {
            private novemberizing.ds.tuple.Single<A> __tuple;
            private novemberizing.ds.func.Single<A, Z> __func;

            @Override
            public void exec(CALLBACK callback){
                callback.on(__func.call(__tuple.first));
            }

            public Single(novemberizing.ds.tuple.Single<A> tuple, novemberizing.ds.func.Single<A, Z> func){
                __tuple = tuple;
                __func = func;
            }
        }

        public static class Pair<A, B, Z, CALLBACK extends novemberizing.ds.on.Single<Z>> implements novemberizing.ds.Exec<CALLBACK> {
            private novemberizing.ds.tuple.Pair<A, B> __tuple;
            private novemberizing.ds.func.Pair<A, B, Z> __func;

            @Override
            public void exec(CALLBACK callback){
                callback.on(__func.call(__tuple.first, __tuple.second));
            }

            public Pair(novemberizing.ds.tuple.Pair<A, B> tuple, novemberizing.ds.func.Pair<A, B, Z> func){
                __tuple = tuple;
                __func = func;
            }
        }

        public static class Triple<A, B, C, Z, CALLBACK extends novemberizing.ds.on.Single<Z>> implements novemberizing.ds.Exec<CALLBACK> {
            private novemberizing.ds.tuple.Triple<A, B, C> __tuple;
            private novemberizing.ds.func.Triple<A, B, C, Z> __func;

            @Override
            public void exec(CALLBACK callback){
                callback.on(__func.call(__tuple.first, __tuple.second, __tuple.third));
            }

            public Triple(novemberizing.ds.tuple.Triple<A, B, C> tuple, novemberizing.ds.func.Triple<A, B, C, Z> func){
                __tuple = tuple;
                __func = func;
            }
        }

        public static class Quadruple<A, B, C, D, Z, CALLBACK extends novemberizing.ds.on.Single<Z>> implements novemberizing.ds.Exec<CALLBACK> {
            private novemberizing.ds.tuple.Quadruple<A, B, C, D> __tuple;
            private novemberizing.ds.func.Quadruple<A, B, C, D, Z> __func;

            @Override
            public void exec(CALLBACK callback){
                callback.on(__func.call(__tuple.first, __tuple.second, __tuple.third, __tuple.fourth));
            }

            public Quadruple(novemberizing.ds.tuple.Quadruple<A, B, C, D> tuple, novemberizing.ds.func.Quadruple<A, B, C, D, Z> func){
                __tuple = tuple;
                __func = func;
            }
        }
    }
}
