#ifndef   __NOVEMBERIZING_DS_TUPLE__TRIPLE__HH__
#define   __NOVEMBERIZING_DS_TUPLE__TRIPLE__HH__

#include <novemberizing/ds/tuple/pair.hh>

namespace novemberizing { namespace ds { namespace tuple {

template <class A, class B, class C>
class Triple : public Pair<A, B>
{
public:		C third;
public:		inline Triple(void){}
public:		inline Triple(const A & first, const B & second, const C & third);
public:		inline virtual ~Triple(void){}
};

} } }

#include <novemberizing/ds/tuple/triple.inline.hh>

#endif // __NOVEMBERIZING_DS_TUPLE__TRIPLE__HH__
