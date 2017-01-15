package novemberizing.rx.observables;

import java.util.HashMap;

import novemberizing.ds.tuple.Pair;
import novemberizing.rx.Operator;
import novemberizing.util.Log;

/**
 * PAIR<KEY,VALUE> MAP PAIR<KEY, VALUE>
 *     .find(...)
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 16.
 */

public class Map<K, V> extends Operator<Pair<K, V>, Pair<K, V>> {
    private static final String Tag = "Map";

    private final java.util.Map<K, V> __map;

    public Map(){
        __map = new HashMap<>();
    }

    @Override
    protected void on(Local<Pair<K, V>, Pair<K, V>> local) {
        if(local==null || local.in==null){
            Log.e(Tag, new RuntimeException("(local==null || local.in==null)==true"));
        } else {
            synchronized (__map) {
                __map.put(local.in.first, local.in.second);
            }
        }
    }

    public V find(K key){
        V v;
        synchronized (__map){
            v = __map.get(key);
        }
        return v;
    }

    public V del(K key){
        V v;
        synchronized (__map){
            v = __map.remove(key);
        }
        return v;
    }

    public int size(){ return __map.size(); }

    public void clear(){ __map.clear(); }

    public Map(java.util.Map<K, V> map){
        __map = map;
    }
}
