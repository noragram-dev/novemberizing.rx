#ifndef   __NOVEMBERIZING_CONCURRENCY__SYNC__HH__
#define   __NOVEMBERIZING_CONCURRENCY__SYNC__HH__

#include <pthread.h>

#include <novemberizing.hh>

#include <novemberizing/util/log.hh>

namespace novemberizing { namespace concurrency {

class Sync
{
protected:	pthread_mutex_t * __mutex;
protected:	pthread_mutexattr_t * __mutexattr;
public:		virtual int on(void);
public:		virtual int off(void);
public:		virtual int lock(void);
public:		virtual int unlock(void);
public:		Sync(bool on = true);
public:		virtual ~Sync(void);
};

} }

#define synchronized(sync, block)				\
	(sync)->lock();								\
	block;										\
	(sync)->unlock();							\

#include <novemberizing/concurrency/sync.cc>

#endif // __NOVEMBERIZING_CONCURRENCY__SYNC__HH__
