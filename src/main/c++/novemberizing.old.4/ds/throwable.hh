#ifndef   __NOVEMBERIZING_DS__THROWABLE__HH__
#define   __NOVEMBERIZING_DS__THROWABLE__HH__

#include <novemberizing.hh>

#include <novemberizing/util/log.hh>

namespace novemberizing { namespace ds {

class Throwable
{
private:    std::string __msg;
public:     const std::string & msg(void) const { return __msg; }
public:		Throwable(void);
public:		virtual ~Throwable(void);
};

} }

#include <novemberizing/ds/throwable.cc>

#endif // __NOVEMBERIZING_DS__THROWABLE__HH__
