#ifndef   __NOVEMBERIZING_DS_TUPLE__SINGLE__HH__
#define   __NOVEMBERIZING_DS_TUPLE__SINGLE__HH__

#include <novemberizing/ds/tuple/empty.hh>

namespace novemberizing { namespace ds { namespace tuple {

template <class A>
class Single : public Empty
{
public:		A first;
public:		inline Single(void){}
public:		inline Single(const A & first);
public:		inline virtual ~Single(void){}
};

} } }

#include <novemberizing/ds/tuple/single.inline.hh>

#endif // __NOVEMBERIZING_DS_TUPLE__SINGLE__HH__
