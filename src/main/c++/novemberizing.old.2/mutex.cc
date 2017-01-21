#include "mutex.hh"

namespace novemberizing {

Mutex::Mutex(void)
{
    __mutex = (pthread_mutex_t *) ::malloc(sizeof(pthread_mutex_t));
    __mutexattr = (pthread_mutexattr_t *) ::malloc(sizeof(pthread_mutex_t));

    *__mutex = PTHREAD_MUTEX_INITIALIZER;
    ::pthread_mutexattr_init(__mutexattr);
    int ret = ::pthread_mutex_init(__mutex, __mutexattr);

    if(ret!=Success)
    {
        printf("fail to ::pthread_mutex_init(...) caused by %d\n",ret);
        ::pthread_mutexattr_destroy(__mutexattr);
        ::free(__mutexattr);
        __mutexattr = nullptr;
        ::free(__mutex);
        __mutex = nullptr;
    }
}

Mutex::~Mutex(void)
{
    if(__mutex!=nullptr)
    {
        ::pthread_mutex_destroy(__mutex);
        ::free(__mutex);
        __mutex = nullptr;
    }

    if(__mutexattr!=nullptr)
    {
        ::pthread_mutexattr_destroy(__mutexattr);
        ::free(__mutexattr);
        __mutexattr = nullptr;
    }
}

void Mutex::lock(void)
{
    if(__mutex!=nullptr)
    {
        int ret = ::pthread_mutex_lock(__mutex);
        if(ret!=Success)
        {
            printf("fail to ::pthread_mutex_lock(...) caused by %d\n",ret);
        }
    }
}

void Mutex::unlock(void)
{
    if(__mutex!=nullptr)
    {
        int ret = ::pthread_mutex_unlock(__mutex);
        if(ret!=Success)
        {
            printf("fail to ::pthread_mutex_unlock(...) caused by %d\n",ret);
        }
    }
}

}
