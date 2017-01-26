#ifndef   __NOVEMBERIZING_CONCURRENCY__SYNC__CC__
#define   __NOVEMBERIZING_CONCURRENCY__SYNC__CC__

#include <novemberizing/concurrency/sync.hh>

namespace novemberizing { namespace concurrency {

/**
 * @fn			Sync::Sync(void)
 * @brief
 */
Sync::Sync(bool create) : __mutex(nullptr), __mutexattr(nullptr)
{
	FUNCTION_START("");

	if(create){ on(); }

	FUNCTION_END("");
}

Sync::~Sync(void)
{
	FUNCTION_START("");

	off();

	FUNCTION_END("");
}

int Sync::on(void)
{
	FUNCTION_START("");
	int ret = Success;
	if(__mutex==nullptr)
	{
		__mutexattr = (pthread_mutexattr_t *) ::calloc(sizeof(pthread_mutexattr_t), 1);
		::pthread_mutexattr_init(__mutexattr);

		__mutex = (pthread_mutex_t *) ::malloc(sizeof(pthread_mutex_t));
		*__mutex = PTHREAD_MUTEX_INITIALIZER;
		if((ret = ::pthread_mutex_init(__mutex, __mutexattr))!=Success)
		{
			ERROR_LOG("fail to ::pthread_mutex_init(...) caused by %d", ret);

			::free(__mutex);
			__mutex = nullptr;

			::pthread_mutexattr_destroy(__mutexattr);
			::free(__mutexattr);
			__mutexattr = nullptr;
		}
		else
		{
			DEBUG_LOG("succeed to ::pthread_mutex_init(...)");
		}
	}
	else
	{
		NOTICE_LOG("__mutex!=nullptr");
	}
	FUNCTION_END("");
	return ret==Success ? Success : Fail;
}

int Sync::off(void)
{
	FUNCTION_START("");
	int ret = Success;
	if(__mutex!=nullptr)
	{
		::pthread_mutex_destroy(__mutex);
		::free(__mutex);
		__mutex = nullptr;

		::pthread_mutexattr_destroy(__mutexattr);
		::free(__mutexattr);
		__mutexattr = nullptr;

		DEBUG_LOG("succeed to ::pthread_mutex_destroy(...)");
	}
	else
	{
		NOTICE_LOG("__mutex==nullptr");
	}
	FUNCTION_END("");
	return ret==Success ? Success : Fail;
}

int Sync::lock(void)
{
	FUNCTION_START("");
	int ret = Fail;
	if(__mutex!=nullptr)
	{
		if((ret=::pthread_mutex_lock(__mutex))!=Success)
		{
			ERROR_LOG("fail to ::pthread_mutex_lock(...) caused by %d", ret);
		}
		else
		{
			DEBUG_LOG("succeed to ::pthread_mutex_lock(...)");
		}
	}
	else
	{
		NOTICE_LOG("__mutex==nullptr");
	}
	FUNCTION_END("");
	return ret==Success ? Success : Fail;
}

int Sync::unlock(void)
{
	FUNCTION_START("");
	int ret = Fail;
	if(__mutex!=nullptr)
	{
		if((ret=::pthread_mutex_unlock(__mutex))!=Success)
		{
			ERROR_LOG("fail to ::pthread_mutex_unlock(...) caused by %d", ret);
		}
		else
		{
			DEBUG_LOG("succeed to ::pthread_mutex_unlock(...)");
		}
	}
	else
	{
		NOTICE_LOG("__mutex==nullptr");
	}
	FUNCTION_END("");
	return ret==Success ? Success : Fail;
}

} }

#ifdef    __NOVEMBERIZING_CONCURRENCY__SYNC__CC__EXAMPLE__

#include <iostream>

using namespace novemberizing::concurrency;
using namespace std;

int main(void)
{
	Sync sync;
	synchronized(&sync, cout<<"hello world"<<endl; );
	return 0;
}

#endif // __NOVEMBERIZING_CONCURRENCY__SYNC__CC__EXAMPLE__

#endif // __NOVEMBERIZING_CONCURRENCY__SYNC__CC__
