#ifndef   __NOVEMBERIZING_RX_SCHEDULERS__MAIN__HH__
#define   __NOVEMBERIZING_RX_SCHEDULERS__MAIN__HH__

#include <novemberizing.hh>

#include <novemberizing/util/log.hh>
#include <novemberizing/util/concurrency.list.hh>

#include <novemberizing/ds/cyclable.hh>

#include <novemberizing/rx/scheduler.hh>
#include <novemberizing/rx/task.hh>

namespace novemberizing { namespace rx { namespace schedulers {

class Main : public Scheduler, public Cyclable
{
private:	ConcurrencySet<Task *> __tasks;
private:	ConcurrencyList<Task *, Condition> __q;
private:	ConcurrencyList<Cyclable *> __cyclables;
public:		virtual void dispatch(Task * task);		/** check visibility */
public:		virtual void execute(Task * task);		/** check visibility */
public:		virtual void executed(Task * task);		/** check visibility */
public:		virtual void completed(Task * task);	/** check visibility */
public:		virtual void onecycle(void);			/** check visibility */
public:		Main(void);
public:		virtual ~Main(void);
};

} } }

#endif // __NOVEMBERIZING_RX_SCHEDULERS__MAIN__HH__
