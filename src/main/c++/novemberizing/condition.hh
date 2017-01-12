#ifndef   __NOVEMBERIZING__CONDITION__HH__
#define   __NOVEMBERIZING__CONDITION__HH__

#include <novemberizing/sync.hh>

namespace novemberizing {

class Condition : public virtual Sync
{
public:     virtual void suspend(type::nano nano = Infinite){}
public:     virtual void resume(bool all = false){}
public:     Condition(void){}
public:     virtual ~Condition(void){}
};

}

#endif // __NOVEMBERIZING__CONDITION__HH__
