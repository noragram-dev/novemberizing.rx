#ifndef   __NOVEMBERIZING_DS_ON__QUADRUPLE__HH__
#define   __NOVEMBERIZING_DS_ON__QUADRUPLE__HH__

#include <novemberizing/ds/on.hh>

namespace novemberizing { namespace ds { namespace on {

template <class A, class B, class C, class D>
class Quadruple : public On
{
public:		virtual void operator()(const A & first, const B & second, const & C third, const & D fource) = 0;
public:		Quadruple(void){}
public:		virtual ~Quadruple(void){}
};

} } }

#endif // __NOVEMBERIZING_DS_ON__QUADRUPLE__HH__
