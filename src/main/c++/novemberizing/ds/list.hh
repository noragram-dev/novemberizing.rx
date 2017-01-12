#ifndef   __NOVEMBERIZING_DS__LIST__HH__
#define   __NOVEMBERIZING_DS__LIST__HH__

#include <list>

#include <novemberizing/condition.hh>

namespace novemberizing { namespace ds {

template <class T, class Lock = Condition>
class List : public Lock
{
public:     virtual T front(void) = 0;
public:     virtual T back(void) = 0;
public:     virtual T pop(void) = 0;
public:     virtual type::size size(void) const = 0;
public:     virtual void front(const T & v) = 0;
public:     virtual void back(const T & v) = 0;
public:     virtual void push(const T & v) = 0;
public:     List(void){}
public:     virtual ~List(void){}
};

} }

#endif // __NOVEMBERIZING_DS__LIST__HH__
