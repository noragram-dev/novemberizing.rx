#ifndef   __NOVEMBERIZING_RX__OBSERVABLE__INLINE__HH__
#define   __NOVEMBERIZING_RX__OBSERVABLE__INLINE__HH__

namespace novemberizing { namespace rx {

template <class T>
inline void Observable<T>::pub(Observable<T> & observable, const T & o)
{
    FUNCTION_START("");
    observable.emit(o);
    FUNCTION_END("");
}

template <class T>
inline void Observable<T>::pub(Observable<T> & observable, const T && o)
{
    FUNCTION_START("");
    observable.emit(o);
    FUNCTION_END("");
}

template <class T>
inline void Observable<T>::emit(const T & o)
{
    FUNCTION_START("");
    for (SubscriptionList<T>::iterator it = __subscriptionList.begin(); it != __subscriptionList.end();)
    {
        Subscription<T> * subscription = *it;
        if (subscription != nullptr)
        {
            synchronized(subscription, {
                if (!subscription->unsubscribed())
                {
                    subscription->emit(o);
                    it++;
                }
                else
                {
                    it = __subscriptionList.erase(it);
                }
            });
        }
        else
        {
            it = __subscriptionList.erase(it);
        }
    }
    FUNCTION_END("");
}

template <class T>
inline void Observable<T>::error(const Throwable & e)
{
    FUNCTION_START("");
    for (SubscriptionList<T>::iterator it = __subscriptionList.begin(); it != __subscriptionList.end();)
    {
        Subscription<T> * subscription = *it;
        if (subscription != nullptr)
        {
            synchronized(subscription, {
                if (!subscription->unsubscribed())
                {
                    subscription->error(e);
                    it++;
                }
                else
                {
                    it = __subscriptionList.erase(it);
                }
            });
        }
        else
        {
            it = __subscriptionList.erase(it);
        }
    }
    FUNCTION_END("");
}

template <class T>
inline void Observable<T>::complete()
{
    FUNCTION_START("");
    for (SubscriptionList<T>::iterator it = __subscriptionList.begin(); it != __subscriptionList.end();)
    {
        Subscription<T> * subscription = *it;
        if (subscription != nullptr)
        {
            synchronized(subscription, {
                if (!subscription->unsubscribed())
                {
                    subscription->complete();
                    it++;
                }
                else
                {
                    it = __subscriptionList.erase(it);
                }
            });
        }
        else
        {
            it = __subscriptionList.erase(it);
        }
    }
    FUNCTION_END("");
}

template <class T>
inline Subscription<T> * Observable<T>::subscribe(Observer<T> * observer)
{
    FUNCTION_START("");
    Subscription<T> * subscription = new Subscription<T>(this, observer);
    __subscriptionList.back(subscription);
    FUNCTION_END("");
    return subscription;
}

template <class T>
inline Observable<T>::Observable(void)
{
    FUNCTION_START("");
    FUNCTION_END("");
}

template <class T>
inline Observable<T>::~Observable(void)
{
    FUNCTION_START("");
    FUNCTION_END("");
}

} }

#endif // __NOVEMBERIZING_RX__OBSERVABLE__INLINE__HH__
