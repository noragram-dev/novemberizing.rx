#ifndef   __NOVEMBERIZING_RX__EMITTABLE__HH__
#define   __NOVEMBERIZING_RX__EMITTABLE__HH__

#include <novemberizing/util/log.hh>

#include <novemberizing/concurrency/sync.hh>

namespace novemberizing { namespace rx {

using namespace concurrency;

template <class T>
class Emittable
{
public:		Emittable(void);
public:		virtual ~Emittable(void);
};

} }

#include <novemberizing/rx/emittable.inline.hh>

#endif // __NOVEMBERIZING_RX__EMITTABLE__HH__
