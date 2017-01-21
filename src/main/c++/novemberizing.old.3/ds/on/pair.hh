#ifndef   __NOVEMBERIZING_DS_ON__PAIR__HH__
#define   __NOVEMBERIZING_DS_ON__PAIR__HH__

#include <novemberizing/ds/on.hh>

namespace novemberizing { namespace ds { namespace on {

template <class A, class B>
class Pair : public On
{
public:		virtual void operator()(const A & first, const B & second) = 0;
public:		Pair(void){}
public:		virtual ~Pair(void){}
};

} } }

#endif // __NOVEMBERIZING_DS_ON__PAIR__HH__
