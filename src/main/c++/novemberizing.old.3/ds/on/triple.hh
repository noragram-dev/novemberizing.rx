#ifndef   __NOVEMBERIZING_DS_ON__TRIPLE__HH__
#define   __NOVEMBERIZING_DS_ON__TRIPLE__HH__

#include <novemberizing/ds/on.hh>

namespace novemberizing { namespace ds { namespace on {

template <class A, class B, class C>
class Triple : public On
{
public:		virtual void operator()(const & A first, const & B second, const & C third) = 0;
public:		Triple(void){}
public:		virtual ~Triple(void){}
};

} } }

#endif // __NOVEMBERIZING_DS_ON__TRIPLE__HH__
