#ifndef   __NOVEMBERIZING_CONCURRENCY__CONDITION__HH__
#define   __NOVEMBERIZING_CONCURRENCY__CONDITION__HH__

#include <pthread.h>

#include <novemberizing.hh>

#include <novemberizing/util/log.hh>

#include <novemberizing/concurrency/sync.hh>

namespace novemberizing { namespace concurrency {

class Condition : public Sync
{
private:    pthread_cond_t * __cond;
private:    pthread_condattr_t * __condattr;
public:		virtual int on(void);
public:		virtual int off(void);
public:		virtual int suspend(type::nano nano = Infinite);
public:		virtual int resume(bool all = false);
public:		Condition(bool create = true);
public:		virtual ~Condition(void);
};

} }

#include <novemberizing/concurrency/condition.cc>

#endif // __NOVEMBERIZING_CONCURRENCY__CONDITION__HH__
