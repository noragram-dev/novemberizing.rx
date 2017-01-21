#ifndef   __NOVEMBERIZING_DS_ON__SINGLE__HH__
#define   __NOVEMBERIZING_DS_ON__SINGLE__HH__

#include <novemberizing/ds/on.hh>

namespace novemberizing { namespace ds { namespace on {

template <class A>
class Single : public On
{
public:		virtual void operator()(const & A first) = 0;
public:		Single(void){}
public:		virtual ~Single(void){}
};

} } }

#endif // __NOVEMBERIZING_DS_ON__SINGLE__HH__
