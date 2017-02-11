package novemberizing.ds;

/**
 *
 * @author novemberiizng, me@novemberizing.net
 * @since 2017. 1. 20.
 */
public interface On {
    class Exec {
        public static class Empty<CALLBACK> implements novemberizing.ds.Exec<CALLBACK>  {
            private novemberizing.ds.on.Single<CALLBACK> __on;

            @Override
            public  void exec(CALLBACK callback){
                __on.on(callback);
            }

            public Empty(novemberizing.ds.on.Single<CALLBACK> on){
                __on = on;
            }
        }

        public static class Single<A, CALLBACK> implements novemberizing.ds.Exec<CALLBACK>  {
            private novemberizing.ds.tuple.Single<A> __tuple;
            private novemberizing.ds.on.Pair<A, CALLBACK> __on;

            @Override
            public void exec(CALLBACK callback){
                __on.on(__tuple.first, callback);
            }

            public Single(novemberizing.ds.tuple.Single<A> tuple, novemberizing.ds.on.Pair<A, CALLBACK> on){
                __tuple = tuple;
                __on = on;
            }
        }

        public static class Pair<A, B, CALLBACK> implements novemberizing.ds.Exec<CALLBACK>  {
            private novemberizing.ds.tuple.Pair<A, B> __tuple;
            private novemberizing.ds.on.Triple<A, B, CALLBACK> __on;

            @Override
            public void exec(CALLBACK callback){
                __on.on(__tuple.first, __tuple.second, callback);
            }

            public Pair(novemberizing.ds.tuple.Pair<A, B> tuple, novemberizing.ds.on.Triple<A, B, CALLBACK> on){
                __tuple = tuple;
                __on = on;
            }
        }

        public static class Triple<A, B, C, CALLBACK> implements novemberizing.ds.Exec<CALLBACK>  {
            private novemberizing.ds.tuple.Triple<A, B, C> __tuple;
            private novemberizing.ds.on.Quadruple<A, B, C, CALLBACK> __on;

            @Override
            public void exec(CALLBACK callback){
                __on.on(__tuple.first, __tuple.second, __tuple.third, callback);
            }

            public Triple(novemberizing.ds.tuple.Triple<A, B, C> tuple, novemberizing.ds.on.Quadruple<A, B, C, CALLBACK> on){
                __tuple = tuple;
                __on = on;
            }
        }

        public static class Quadruple<A, B, C, D, CALLBACK> implements novemberizing.ds.Exec<CALLBACK>  {
            private novemberizing.ds.tuple.Quadruple<A, B, C, D> __tuple;
            private novemberizing.ds.on.Quintuple<A, B, C, D, CALLBACK> __on;

            @Override
            public void exec(CALLBACK callback){
                __on.on(__tuple.first, __tuple.second, __tuple.third, __tuple.fourth, callback);
            }

            public Quadruple(novemberizing.ds.tuple.Quadruple<A, B, C, D> tuple, novemberizing.ds.on.Quintuple<A, B, C, D, CALLBACK> on){
                __tuple = tuple;
                __on = on;
            }
        }

    }
}
