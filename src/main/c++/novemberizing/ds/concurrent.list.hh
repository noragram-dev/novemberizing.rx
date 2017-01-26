#ifndef   __NOVEMBERIZING_DS__CONCURRENT__LIST__HH__
#define   __NOVEMBERIZING_DS__CONCURRENT__LIST__HH__

#include <list>

#include <novemberizing/concurrency/condition.hh>

namespace novemberizing { namespace ds {

template <class T, class Concurrent = concurrency::Condition>
class ConcurrentList : public Concurrent
{
public:     typedef typename std::list<T>::iterator iterator;
private:    std::list<T> __list;
public:     inline iterator begin(void);
public:     inline iterator end(void);
public:     inline iterator erase(iterator it);
public:     inline void front(const T & v);
public:     inline void front(const T && v);
public:     inline void back(const T & v);
public:     inline void back(const T && v);
public:     inline T front(void);
public:     inline T back(void);
public:     inline void push(const T & v);
public:     inline void push(const T && v);
public:     inline T pop(void);
public:     inline bool empty(void) const;
public:     inline type::size size(void) const;
public:     inline void clear(void);
public:     inline ConcurrentList(void);
public:     inline virtual ~ConcurrentList(void);
};

} }

#include <novemberizing/ds/concurrent.list.inline.hh>

#endif // __NOVEMBERIZING_DS__CONCURRENT__LIST__HH__
