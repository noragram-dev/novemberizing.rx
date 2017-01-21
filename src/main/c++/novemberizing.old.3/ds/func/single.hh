#ifndef   __NOVEMBERIZING_DS_FUNC__SINGLE__HH__
#define   __NOVEMBERIZING_DS_FUNC__SINGLE__HH__

#include <novemberizing/ds/func.hh>

namespace novemberizing { namespace ds { namespace func {

template <class A, class Z>
class Single : public Func<Z>
{
public:		virtual Z operator()(const & A first) = 0;
public:		Single(void){}
public:		virtual ~Single(void){}
}

} } }

#endif // __NOVEMBERIZING_DS_FUNC__SINGLE__HH__
