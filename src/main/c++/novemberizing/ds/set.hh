#ifndef   __NOVEMBERIZING_DS__SET__HH__
#define   __NOVEMBERIZING_DS__SET__HH__

#include <novemberizing/cond.hh>

namespace novemberizing { namespace ds {

template <class T, class Lock = Sync>
class Set : public Lock
{
public:     virtual bool add(const T & v) = 0;
public:     virtual bool del(const T & v) = 0;
public:     virtual type::size size(void) const = 0;
public:     Set(void){}
public:     virtual ~Set(void){}
};

} }

#endif // __NOVEMBERIZING_DS__SET__HH__
