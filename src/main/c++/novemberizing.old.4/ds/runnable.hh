#ifndef   __NOVEMBERIZING_DS__RUNNABLE__HH__
#define   __NOVEMBERIZING_DS__RUNNABLE__HH__

#include <novemberizing.hh>

#include <novemberizing/util/log.hh>

namespace novemberizing { namespace ds {

class Runnable
{
public:     virtual void run(void) = 0;
public:     Runnable(void);
public:     virtual ~Runnable(void);
};

} }

#include <novemberizing/ds/runnable.cc>

#endif // __NOVEMBERIZING_DS__RUNNABLE__HH__
