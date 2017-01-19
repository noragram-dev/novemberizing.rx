package novemberizing.rx.operators;

import novemberizing.ds.tuple.Pair;
import novemberizing.rx.Operator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import static novemberizing.ds.Constant.Infinite;

/**
 *
 * @author novemberizing, me@novemberizing.net
 * @since 2017. 1. 19
 */
public class Map<K, V> extends Operator<novemberizing.ds.tuple.Pair<K, V>, novemberizing.ds.tuple.Pair<K, V>> {

    private final java.util.Map<K, V> __map;

    public Map(){
        __map = new HashMap<>();
        replay(Infinite);
    }

    public Map(java.util.Map<K, V> map){
        if(map!=null) {
            __map = map;
        } else {
            __map = new HashMap<>();
        }
        replay(Infinite);
    }

    @Override
    protected void on(Task<Pair<K, V>, Pair<K, V>> task, Pair<K, V> in) {
        synchronized (__map){
            __map.put(in.first, in.second);
        }
        task.complete();
    }

    public Set<java.util.Map.Entry<K, V>> entries(){
        Set<java.util.Map.Entry<K, V>> ret;
        synchronized (__map){
            ret = __map.entrySet();
        }
        return ret;
    }

    public Set<K> keys(){
        Set<K> ret;
        synchronized (__map){
            ret = __map.keySet();
        }
        return ret;
    }

    public Collection<V> values(){
        Collection<V> ret;
        synchronized (__map){
            ret = __map.values();
        }
        return ret;
    }

    public V get(K key){
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

    public void clear(){
        synchronized (__map){
            __map.clear();
        }
    }

}
