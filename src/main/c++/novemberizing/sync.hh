#ifndef   __NOVEMBERIZING__SYNC__HH__
#define   __NOVEMBERIZING__SYNC__HH__

#include <pthread.h>

#include <novemberizing.hh>

namespace novemberizing {

class Condition;

class Sync
{
protected:  pthread_mutex_t * __mutex;
protected:  pthread_mutexattr_t * __mutexattr;
public:     void lock(void);
public:     void unlock(void);
public:     Sync(void);
public:     virtual ~Sync(void);
public:     friend class Condition;
};

}

#endif // __NOVEMBERIZING__SYNC__HH__
