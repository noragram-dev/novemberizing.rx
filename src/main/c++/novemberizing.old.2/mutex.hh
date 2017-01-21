#ifndef   __NOVEMBERIZING__MUTEX__HH__
#define   __NOVEMBERIZING__MUTEX__HH__

#include <novemberizing/sync.hh>

namespace novemberizing {

class Mutex : public virtual Sync
{
protected:  pthread_mutex_t * __mutex;
protected:  pthread_mutexattr_t * __mutexattr;
public:     virtual void lock(void);
public:     virtual void unlock(void);
public:     Mutex(void);
public:     virtual ~Mutex(void);
};

}

#endif // __NOVEMBERIZING__MUTEX__HH__
