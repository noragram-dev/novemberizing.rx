#ifndef   __NOVEMBERIZING_DS_ON__EMPTY__HH__
#define   __NOVEMBERIZING_DS_ON__EMPTY__HH__

#include <novemberizing/ds/on.hh>

namespace novemberizing { namespace ds { namespace on {

class Empty : public On
{
public:		virtual void operator()(void) = 0;
public:		Empty(void){}
public:		virtual ~Empty(void){}
};

} } }

#endif // __NOVEMBERIZING_DS_ON__EMPTY__HH__
