#ifndef   __NOVEMBERIZING_DS__LINKED_SET__HH__
#define   __NOVEMBERIZING_DS__LINKED_SET__HH__

#include <set>
#include <list>
#include <algorithm>

#include <novemberizing/ds/sorted.set.hh>
#include <novemberizing/ds/linked.list.hh>

namespace novemberizing { namespace ds {

template <class T, class Lock = Sync>
class LinkedSet : public SortedSet<T, Sync>
{
protected:  LinkedList<T> __q;
public:     virtual bool add(const T & v)
            {
                bool ret;
                if(ret=__s.insert(v).second)
                {
                    __q.back(v);
                }
                return ret;
            }
public:     virtual bool del(const T & v)
            {
                bool ret;
                if(ret=(__s.erase(v)==1))
                {
                    __q.remove(v);
                }
                return ret;
            }
public:     virtual type::size size(void) const { return __s.size(); }
public:     LinkedSet(void);
public:     virtual ~LinkedSet(void);
};

} }

#endif // __NOVEMBERIZING_DS__LINKED_SET__HH__
