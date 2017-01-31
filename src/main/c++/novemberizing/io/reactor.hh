#ifndef   __NOVEMBERIZING_IO__REACTOR__HH__
#define   __NOVEMBERIZING_IO__REACTOR__HH__

#include <novemberizing/util/log.hh>

#include <novemberizing/ds/cyclable.hh>

namespace novemberizing { namespace io {

using namespace util;
using namespace ds;

class Reactor : public Cyclable
{
public:     virtual void onecycle(void);
public:     Reactor(void);
public:     virtual ~Reactor(void);
};

} }

#endif // __NOVEMBERIZING_IO__REACTOR__HH__
