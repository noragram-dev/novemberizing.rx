#ifndef   __NOVEMBERIZING_CONCURRENCY__SYNC__HH__
#define   __NOVEMBERIZING_CONCURRENCY__SYNC__HH__

#include <pthread.h>

namespace novemberizing { namespace concurrency {

class sync
{
protected:  pthread_mutex_t __mutex;
protected:  pthread_mutexattr_t __mutexattr;
public:     void lock(void);
public:     void unlock(void);
public:     sync(void);
public:     virtual ~sync(void):
};

} }

#endif // __NOVEMBERIZING_CONCURRENCY__SYNC__HH__
