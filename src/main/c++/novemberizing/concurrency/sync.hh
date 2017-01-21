#ifndef   __NOVEMBERIZING_CONCURRENCY__SYNC__HH__
#define   __NOVEMBERIZING_CONCURRENCY__SYNC__HH__

#include <novemberizing/util/log.hh>

namespace novemberizing { namespace concurrency {

class Sync
{
public:		virtual void lock(void);
public:		virtual void unlock(void);
public:		Sync(void);
public:		virtual ~Sync(void);
};

} }

#define synchronized(sync, block)				\
	sync->lock();								\
	block;										\
	sync->unlock();

#include <novemberizing/concurrency/sync.cc>

#endif // __NOVEMBERIZING_CONCURRENCY__SYNC__HH__
