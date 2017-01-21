#ifndef   __NOVEMBERIZING_DS__EXCEPTION__HH__
#define   __NOVEMBERIZING_DS__EXCEPTION__HH__

#include <novemberizing/ds/throwable.hh>

namespace novemberizing { namespace ds {

class Exception : public Throwable
{
public:		Exception(void);
public:		virtual ~Exception(void);
};

} }

#include <novemberizing/ds/exception.cc>

#endif // __NOVEMBERIZING_DS__EXCEPTION__HH__
