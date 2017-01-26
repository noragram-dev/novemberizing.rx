#ifndef   __NOVEMBERIZING_DS__CONCURRENT_MAP__HH__
#define   __NOVEMBERIZING_DS__CONCURRENT_MAP__HH__

#include <map>

#include <novemberizing/concurrency/sync.hh>

namespace novemberizing { namespace ds {

template <class K, class V, class Concurrent = concurrency::Sync>
class ConcurrentMap : public Concurrent
{
public:     typedef typename std::map<K,V>::iterator iterator;
private:    std::map<K, V> __map;
public:     inline iterator begin(void);
public:     inline iterator end(void);
public:     inline iterator erase(iterator it);
public:     inline V & put(const K & key, const V & value);
public:     inline V & put(const K && key, const V & value);
public:     inline V & put(const K & key, const V && value);
public:     inline V & put(const K && key, const V && value);
public:     inline V & get(const K & key);
public:     inline V & get(const K && key);
public:     inline bool exist(const K & key) const;
public:     inline bool exist(const K && key) const;
public:     inline V & del(const K & key);
public:     inline V & del(const K && key);
public:     inline bool empty(void) const;
public:     inline type::size size(void) const;
public:     inline void clear(void);
public:     ConcurrentMap(void);
public:     virtual ~ConcurrentMap(void);
};

} }

#include <novemberizing/ds/concurrent.map.inline.hh>

#endif // __NOVEMBERIZING_DS__CONCURRENT_MAP__HH__
