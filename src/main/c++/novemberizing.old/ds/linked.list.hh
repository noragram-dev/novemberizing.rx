#ifndef   __NOVEMBERIZING_DS__LINKED_LIST__HH__
#define   __NOVEMBERIZING_DS__LINKED_LIST__HH__

#include <list>

#include <novemberizing/ds/list.hh>

namespace novemberizing { namespace ds {

template <class T, class Lock = Condition>
class LinkedList : public List<T, Lock>
{
protected:    std::list<T> __q;
public:     T front(void){ return __q.front(); }
public:     T back(void){ return __q.back(); }
public:     T pop(void)
            {
                T v = __q.front();
                __q.pop_front();
                return v;
            }
public:     type::size size(void) const { return __q.size(); }
public:     void front(const T & v){ __q.push_front(v); }
public:     void back(const T & v){ __q.push_back(v); }
public:     void push(const T & v){ __q.push_back(v); }
public:     LinkedList(void){}
public:     virtual ~LinkedList(void){}
};

} }

#endif // __NOVEMBERIZING_DS__LINKED_LIST__HH__
