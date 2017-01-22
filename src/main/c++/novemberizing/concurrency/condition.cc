#ifndef   __NOVEMBERIZING_CONCURRENCY__CONDITION__CC__
#define   __NOVEMBERIZING_CONCURRENCY__CONDITION__CC__\

#include <novemberizing/concurrency/condition.hh>

namespace novemberizing { namespace concurrency {

Condition::Condition(bool create) : Sync(false) ,__cond(nullptr), __condattr(nullptr)
{
	FUNCTION_START("");

	if(create){ on(); }

	FUNCTION_END("");
}

Condition::~Condition(void)
{
	FUNCTION_START("");
	FUNCTION_END("");
}

int Condition::on(void)
{
	FUNCTION_START("");
	int ret = Success;
	if((ret=Sync::on())==Success)
	{
		__condattr = (pthread_condattr_t *) ::calloc(sizeof(pthread_condattr_t), 1);
		::pthread_condattr_init(__condattr);

		__cond = (pthread_cond_t *) ::malloc(sizeof(pthread_cond_t));
		*__cond = PTHREAD_COND_INITIALIZER;
		if((ret = ::pthread_cond_init(__cond, __condattr))!=Success)
		{
			ERROR_LOG("fail to ::pthread_cond_init(...) caused by %d",ret);

			Sync::off();

			::pthread_condattr_destroy(__condattr);
			::free(__condattr);
			__condattr = nullptr;

			::free(__cond);
			__cond = nullptr;
		}
		else
		{
			DEBUG_LOG("succeed to ::pthread_cond_init(...)");
		}
	}
	else
	{
		ERROR_LOG("fail to Sync::on()");
	}
	FUNCTION_END("");
	return ret==Success ? Success : Fail;
}

int Condition::off(void)
{
	FUNCTION_START("");

	int ret = Success;

	Sync::off();

	if(__cond!=nullptr)
	{
		::pthread_cond_destroy(__cond);
		::free(__cond);
		__cond = nullptr;

		::pthread_condattr_destroy(__condattr);
		::free(__condattr);
		__condattr = nullptr;
	}
	else
	{
		NOTICE_LOG("__cond==nullptr");
	}

	FUNCTION_END("");
	return ret==Success ? Success : Fail;
}

int Condition::suspend(type::nano nano)
{
	FUNCTION_START("");
	int ret = Fail;
	if(__cond!=nullptr)
	{
		if(nano==Infinite)
		{
			if((ret==pthread_cond_wait(__cond, __mutex))!=Success)
			{
				ERROR_LOG("fail to ::pthread_cond_wait(...) caused by %d",ret);
			}
			else
			{
				DEBUG_LOG("succeed to ::pthread_cond_wait(...)");
			}
		}
		else
		{
			struct timespec spec;
			::clock_gettime(CLOCK_REALTIME, &spec);

			spec.tv_nsec += nano%1000000000L;
			spec.tv_sec += (nano + spec.tv_nsec)/1000000000L;

			if((ret=pthread_cond_timedwait(__cond, __mutex, &spec))!=Success)
			{
				ERROR_LOG("fail to ::pthread_cond_timedwait(...) caused by %d",ret);
			}
			else
			{
				DEBUG_LOG("succeed to ::pthread_cond_timedwait(...)");
			}
		}
	}
	else
	{
		NOTICE_LOG("__cond==nullptr");
	}
	FUNCTION_END("");
	return ret==Success ? Success : Fail;
}

int Condition::resume(bool all)
{
	FUNCTION_START("");
	int ret = Fail;
	if(__cond!=nullptr)
	{
		if(all)
		{
			if((ret = ::pthread_cond_broadcast(__cond))!=Success)
			{
				ERROR_LOG("fail to ::pthread_cond_broadcast(...) caused by %d",ret);
			}
			else
			{
				DEBUG_LOG("succeed to ::pthread_cond_broadcast(...)");
			}
		}
		else
		{
			if((ret = ::pthread_cond_signal(__cond))!=Success)
			{
				ERROR_LOG("fail to ::pthread_cond_signal(...) caused by %d",ret);
			}
			else
			{
				DEBUG_LOG("succeed to ::pthread_cond_signal(...)");
			}
		}
	}
	else
	{
		NOTICE_LOG("__cond==nullptr");
	}
	FUNCTION_END("");
	return ret==Success ? Success : Fail;
}


} }

#ifdef    __NOVEMBERIZING_CONCURRENCY__CONDITION__CC__EXAMPLE__

#include <iostream>

using namespace novemberizing::concurrency;
using namespace std;

int main(void)
{
	Condition condition;
	synchronized(&condition, {
		condition.suspend(5000000000L);
		// condition.suspend();
		cout<<"hello world"<<endl;
	});
	return 0;
}

#endif // __NOVEMBERIZING_CONCURRENCY__CONDITION__CC__EXAMPLE__

#endif // __NOVEMBERIZING_CONCURRENCY__CONDITION__CC__
