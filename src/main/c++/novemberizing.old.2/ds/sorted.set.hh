#ifndef   __NOVEMBERIZING_DS__SORTED_SET__HH__
#define   __NOVEMBERIZING_DS__SORTED_SET__HH__

#include <set>

#include <novemberizing/ds/set.hh>

namespace novemberizing { namespace ds {

template <class T, class Lock = Sync>
class SortedSet : public Set<T, Lock>
{
public:  std::set<T> __s;
public:     bool add(const T & v) { return __s.insert(v).second; }
public:     bool del(const T & v) { return __s.erase(v)==1; }
public:     type::size size(void) const { return __s.size(); }
public:     SortedSet(void){}
public:     virtual ~SortedSet(void){}
};

} }

#endif // __NOVEMBERIZING_DS__SORTED_SET__HH__
