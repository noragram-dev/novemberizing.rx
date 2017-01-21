#ifndef   __NOVEMBERIZING_RX__SCHEDULER__HH__
#define   __NOVEMBERIZING_RX__SCHEDULER__HH__

#include <novemberizing.hh>

#include <novemberizing/util/log.hh>

#include <novemberizing/rx/task.hh>

namespace novemberizing { namespace rx {

class Scheduler
{
public:		virtual void dispatch(Task * task) = 0;
public:		virtual void execute(Task * task) = 0;
public:		virtual void executed(Task * task) = 0;
public:		virtual void completed(Task * task) = 0;
public:		Scheduler(void);
public:		virtual ~Scheduler(void);
};

} }

#include <novemberizing/rx/scheduler.inline.hh>

#endif // __NOVEMBERIZING_RX__SCHEDULER__HH__
