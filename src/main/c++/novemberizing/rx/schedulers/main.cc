#ifndef   __NOVEMBERIZING_RX_SCHEDULERS__MAIN__CC__
#define   __NOVEMBERIZING_RX_SCHEDULERS__MAIN__CC__

namespace novemberizing { namespace rx { namespace schedulers {

Main::Main(void)
{
	FUNCTION_START("");
	FUNCTION_END("");
}

Main::~Main(void)
{
	FUNCTION_START("");
	FUNCTION_END("");
}

void Main::dispatch(Task * task)
{
	FUNCTION_START("");
	if(task!=nullptr)
	{
		__q.lock();
		__q.push(task);
		__q.resume(false);
		__q.unlock();
	}
	else
	{
		DEBUG_LOG("task==nullptr");
	}
	FUNCTION_END("");
}

void Main::execute(Task * task)
{
	FUNCTION_START("");
	if(task!=nullptr)
	{
		if(__tasks.add(task))
		{
			__tasks->run();
		}
		else
		{
			ERROR_LOG("__tasks.add(task)==false");
		}
	}
	else
	{
		DEBUG_LOG("task==nullptr");
	}
	FUNCTION_END("");
}

void Main::executed(Task * task)
{
	FUNCTION_START("");
	if(task!=nullptr)
	{
		if(!__tasks.del(task))
		{
			DEBUG_LOG("__tasks.del(task)==false")
		}
		__q.lock();
		__q.push(task);
		__q.resume(false);
		__q.unlock();
	}
	else
	{
		DEBUG_LOG("task==nullptr");
	}
	FUNCTION_END("");
}

void Main::completed(Task * task)
{
	FUNCTION_START("");
	if(task!=nullptr)
	{
		if(!__tasks.del(task))
		{
			DEBUG_LOG("__tasks.del(task)==false")
		}
	}
	else
	{
		DEBUG_LOG("task==nullptr");
	}
	FUNCTION_END("");
}

void Main::onecycle(void)
{
	FUNCTION_START("");
	__cyclables.lock();
	for(ConcurrencyList<Cyclable *>::iterator it = __cyclables.begin();it!=__cyclables.end();it++)
	{
		Cyclable * cyclable = *it;
		cyclable->onecycle();
	}
	__cyclables.unlock();
	__q.lock();
	type::size limit = __q.size();
	for(type::size = i;i<limit && __q.size()>0;i++)
	{
		Task * task = __q.pop();
		__q.unlock();
		execute(task);
		__q.lock();
	}
	__q.unlock();
	FUNCTION_END("");
}

Main::Main(void)
{
	FUNCTION_START("");
	FUNCTION_END("");
}

Main::~Main(void)
{
	FUNCTION_START("");
	int remain = 0;
	do {
		__q.lock();
		while(__q.size()>0)
		{
			Task * task = __q.pop();
			__q.unlock();
			execute(task);
			__q.lock();
		}
		synchronized(&__tasks,{ remain = __tasks.size(); });
		__q.unlock();
	} while(remain>0);
	FUNCTION_END("");
}

	// class Main : public Scheduler, public Cyclable
	// {
	// private:	ConcurrencyList<Task *, Condition> __tasks;
	// private:	ConcurrencyList<Cyclable *> __cyclables;
	// public:		virtual ;
	// public:		virtual ;
	// public:		virtual ;
	// public:		virtual ;
	// public:		virtual ;
	// public:		Main(void);
	// public:		virtual ~Main(void);
	// };

} } }

#endif // __NOVEMBERIZING_RX_SCHEDULERS__MAIN__CC__
