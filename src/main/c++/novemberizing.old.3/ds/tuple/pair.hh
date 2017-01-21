#ifndef   __NOVEMBERIZING_DS_TUPLE__PAIR__HH__
#define   __NOVEMBERIZING_DS_TUPLE__PAIR__HH__

#include <novemberizing/ds/tuple/single.hh>

namespace novemberizing { namespace ds { namespace tuple {

template <class A, class B>
class Pair : public Single<A>
{
public:		B second;
public:		Pair(void){}
public:		Pair(const A & first,const B & second);
public:		virtual ~Pair(void){}
};

} } }

#include <novemberizing/ds/tuple/pair.inline.hh>

#endif // __NOVEMBERIZING_DS_TUPLE__PAIR__HH__
