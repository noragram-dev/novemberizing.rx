#include "cond.hh"

namespace novemberizing {

Cond::Cond()
{
    if(__mutex!=nullptr)
    {
        __cond = (pthread_cond_t *) ::malloc(sizeof(pthread_cond_t));
        __condattr = (pthread_condattr_t *) ::malloc(sizeof(pthread_condattr_t));

        ::pthread_condattr_init(__condattr);
        int ret = ::pthread_cond_init(__cond, __condattr);

        if(ret!=Success)
        {
            ::pthread_condattr_destroy(__condattr);
            ::free(__condattr);
            __condattr = nullptr;
            ::free(__cond);
            __cond = nullptr;
        }
    }
    else
    {
        __cond = nullptr;
        __condattr = nullptr;
    }
}

Cond::~Cond(void)
{
    if(__cond!=nullptr)
    {
        ::pthread_cond_destroy(__cond);
        ::free(__cond);
        __cond = nullptr;
    }

    if(__condattr!=nullptr)
    {
        ::pthread_condattr_destroy(__condattr);
        ::free(__condattr);
        __condattr = nullptr;
    }
}

void Cond::suspend(type::nano nano)
{
    if(__cond!=nullptr)
    {
        if(nano<=0)
        {
            int ret = ::pthread_cond_wait(__cond, __mutex);
            if(ret!=Success)
            {
                printf("fail to ::pthread_cond_wait(...) caused by %d",ret);
            }
        }
        else
        {
            struct timespec spec;
            spec.tv_sec = nano/1000000000;
            spec.tv_nsec = nano%1000000000;
            int ret = ::pthread_cond_timedwait(__cond, __mutex, &spec);
            if(ret!=Success)
            {
                printf("fail to ::pthread_cond_timedwait(...) caused by %d",ret);
            }
        }
    }
}

void Cond::resume(bool all)
{
    if(__cond!=nullptr)
    {
        if(all)
        {
            int ret = ::pthread_cond_broadcast(__cond);
            if(ret!=Success)
            {
                printf("fail to ::pthread_cond_broadcast(...) caused by %d",ret);
            }
        }
        else
        {
            int ret = ::pthread_cond_signal(__cond);
            if(ret!=Success)
            {
                printf("fail to ::pthread_cond_signal(...) caused by %d",ret);
            }
        }
    }
}

}
