#ifndef   __NOVEMBERIZING_DS__COND__HH__
#define   __NOVEMBERIZING_DS__COND__HH__

#include <novemberizing/mutex.hh>
#include <novemberizing/condition.hh>

namespace novemberizing {

class Cond : public Mutex, public Condition
{
private:    pthread_cond_t * __cond;
private:    pthread_condattr_t * __condattr;
public:     void suspend(type::nano nano = Infinite);
public:     void resume(bool all = false);
public:     Cond(void);
public:     virtual ~Cond(void);
};

}

#endif // __NOVEMBERIZING_DS__COND__HH__
