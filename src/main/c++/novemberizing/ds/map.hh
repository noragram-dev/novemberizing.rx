#ifndef   __NOVEMBERIZING_DS__MAP__HH__
#define   __NOVEMBERIZING_DS__MAP__HH__

#include <map>

#include <novemberizing/condition.hh>

namespace novemberizing { namespace ds {

template <class K, class V, class Lock = Sync>
class Map : public Sync
{
public:     virtual bool add(const K & key, const V & value) = 0;
public:     virtual bool del(const K & key) = 0;
public:     virtual V & get(const K & key) = 0;
public:     virtual type::size size(void) const = 0;
public:     Map(void){}
public:     virtual ~Map(void){}
};

} }

#endif // __NOVEMBERIZING_DS__MAP__HH__
