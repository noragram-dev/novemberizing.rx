#ifndef   __NOVEMBERIZING_DS__SORTED_MAP__HH__
#define   __NOVEMBERIZING_DS__SORTED_MAP__HH__

#include <map>

#include <novemberizing/ds/map.hh>

namespace novemberizing { namespace ds {

template <class K, class V, class Lock = Sync>
class SortedMap : public Map<K, V, Lock>
{
protected:  std::map<K, V> __m;
public:     bool add(const K & key, const V & value) { return __m.insert(std::pair<K,V>(key,value)).second; }
public:     bool del(const K & key){ return __m.erase(key)==1; }
public:     V & get(const K & key){ return __m.find(key); }
public:     type::size size(void) const { return __m.size(); }
public:     SortedMap(void){}
public:     virtual ~SortedMap(void){}
};

} }

#endif // __NOVEMBERIZING_DS__SORTED_MAP__HH__
