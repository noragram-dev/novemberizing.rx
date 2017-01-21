#ifndef   __NOVEMBERIZING_CONCURRENCY__CONDITION__HH__
#define   __NOVEMBERIZING_CONCURRENCY__CONDITION__HH__

#include <novemberizing.hh>

#include <novemberizing/util/log.hh>

#include <novemberizing/concurrency/sync.hh>

namespace novemberizing { namespace concurrency {

class Condition : public Sync
{
public:		virtual void suspend(type::nano nano);
public:		virtual void resume(bool all);
public:		Condition(void);
public:		virtual ~Condition(void);
};

} }

#include <novemberizing/concurrency/condition.cc>

#endif // __NOVEMBERIZING_CONCURRENCY__CONDITION__HH__
