#ifndef   __NOVEMBERIZING_RX__SUBSCRIPTION_LIST__INLINE__HH__
#define   __NOVEMBERIZING_RX__SUBSCRIPTION_LIST__INLINE__HH__

#include <novemberizing/rx/subscription.list.hh>

namespace novemberizing { namespace rx {

template <class T>
inline typename SubscriptionList<T>::iterator SubscriptionList<T>::begin(void)
{
    FUNCTION_START("");
    FUNCTION_END("");
    return __o.begin();
}

template <class T>
inline typename SubscriptionList<T>::iterator SubscriptionList<T>::end(void)
{
    FUNCTION_START("");
    FUNCTION_END("");
    return __o.end();
}

template <class T>
inline typename SubscriptionList<T>::iterator SubscriptionList<T>::erase(typename SubscriptionList<T>::iterator it)
{
    FUNCTION_START("");
    FUNCTION_END("");
    return __o.erase(it);
}

template <class T>
inline void SubscriptionList<T>::back(Subscription<T> * subscription)
{
    FUNCTION_START("");
    __o.push_back(subscription);
    FUNCTION_END("");
}

template <class T>
inline SubscriptionList<T>::SubscriptionList(void)
{
    FUNCTION_START("");
    FUNCTION_END("");
}

template <class T>
inline SubscriptionList<T>::~SubscriptionList(void)
{
    FUNCTION_START("");
    FUNCTION_END("");
}

} }

#endif // __NOVEMBERIZING_RX__SUBSCRIPTION_LIST__INLINE__HH__
