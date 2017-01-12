#ifndef   __NOVEMBERIZING__CONDITION__HH__
#define   __NOVEMBERIZING__CONDITION__HH__

#include <novemberizing.hh>
#include <novemberizing/sync.hh>

namespace novemberizing {

class Condition
{
private:    Sync * __sync;
private:    pthread_cond_t * __cond;
private:    pthread_condattr_t * __condattr;
public:     void suspend(type::nano nano = Infinite);
public:     void resume(bool all = false);
public:     Condition(Sync * sync);
public:     virtual ~Condition(void);
};

}

#endif // __NOVEMBERIZING__CONDITION__HH__
