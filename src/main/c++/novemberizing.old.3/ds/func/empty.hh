#ifndef   __NOVEMBERIZING_DS_FUNC__EMPTY__HH__
#define   __NOVEMBERIZING_DS_FUNC__EMPTY__HH__

#include <novemberizing/ds/func.hh>

namespace novemberizing { namespace ds { namespace func {

template <class Z>
class Empty : public Func<Z>
{
public:		virtual Z operator()(void) = 0;
public:		Empty(void){}
public:		virtual ~Empty(void){}
};

} } }

#endif // __NOVEMBERIZING_DS_FUNC__EMPTY__HH__
