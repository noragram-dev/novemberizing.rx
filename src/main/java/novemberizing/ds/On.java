package novemberizing.ds;

/**
 *
 * @author novemberiizng, me@novemberizing.net
 * @since 2017. 1. 20.
 */
public interface On {
    class Exec {
        public static class Empty<Z> implements novemberizing.ds.Exec  {
            private novemberizing.ds.on.Single<novemberizing.ds.on.Single<Z>> __on;
            private novemberizing.ds.on.Single<Z> __ret;

            @Override
            public void exec(){
                __on.on(__ret);
            }

            public Empty(novemberizing.ds.on.Single<novemberizing.ds.on.Single<Z>> on,novemberizing.ds.on.Single<Z> ret){
                __on = on;
                __ret = ret;
            }
        }

        public static class Single<A, Z> implements novemberizing.ds.Exec  {
            private novemberizing.ds.tuple.Single<A> __tuple;
            private novemberizing.ds.on.Pair<A, novemberizing.ds.on.Single<Z>> __on;
            private novemberizing.ds.on.Single<Z> __ret;

            @Override
            public void exec(){
                __on.on(__tuple.first, __ret);
            }

            public Single(novemberizing.ds.tuple.Single<A> tuple, novemberizing.ds.on.Pair<A, novemberizing.ds.on.Single<Z>> on,novemberizing.ds.on.Single<Z> ret){
                __tuple = tuple;
                __on = on;
                __ret = ret;
            }
        }

        public static class Pair<A, B, Z> implements novemberizing.ds.Exec  {
            private novemberizing.ds.tuple.Pair<A, B> __tuple;
            private novemberizing.ds.on.Triple<A, B, novemberizing.ds.on.Single<Z>> __on;
            private novemberizing.ds.on.Single<Z> __ret;

            @Override
            public void exec(){
                __on.on(__tuple.first, __tuple.second, __ret);
            }

            public Pair(novemberizing.ds.tuple.Pair<A, B> tuple, novemberizing.ds.on.Triple<A, B, novemberizing.ds.on.Single<Z>> on,novemberizing.ds.on.Single<Z> ret){
                __tuple = tuple;
                __on = on;
                __ret = ret;
            }
        }

        public static class Triple<A, B, C, Z> implements novemberizing.ds.Exec  {
            private novemberizing.ds.tuple.Triple<A, B, C> __tuple;
            private novemberizing.ds.on.Quadruple<A, B, C, novemberizing.ds.on.Single<Z>> __on;
            private novemberizing.ds.on.Single<Z> __ret;

            @Override
            public void exec(){
                __on.on(__tuple.first, __tuple.second, __tuple.third, __ret);
            }

            public Triple(novemberizing.ds.tuple.Triple<A, B, C> tuple, novemberizing.ds.on.Quadruple<A, B, C, novemberizing.ds.on.Single<Z>> on,novemberizing.ds.on.Single<Z> ret){
                __tuple = tuple;
                __on = on;
                __ret = ret;
            }
        }

    }
}
