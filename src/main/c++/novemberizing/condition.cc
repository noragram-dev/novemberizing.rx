#include "condition.hh"

namespace novemberizing {

Condition::Condition(Sync * sync)
{
    if(sync!=nullptr && sync->__mutex!=nullptr)
    {
        __cond = (pthread_cond_t) ::malloc(sizeof(pthread_cond_t));
        __condattr = (pthread_condattr_t) ::malloc(sizeof(pthread_condattr_t));

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

Condition::~Condition(void)
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

}