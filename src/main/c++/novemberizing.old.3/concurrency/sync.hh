#ifndef   __NOVEMBERIZING_CONCURRENCY__SYNC__HH__
#define   __NOVEMBERIZING_CONCURRENCY__SYNC__HH__

namespace novemberizing { namespace concurrency {

class Sync
{
public:		virtual void lock(void){}
public:		virtual void unlock(void){}
public:		Sync(void) {}
public:		virtual ~Sync(void) {}
};

} }

#include <novemberizing/concurrency/sync.inline.hh>

#endif // __NOVEMBERIZING_CONCURRENCY__SYNC__HH__
