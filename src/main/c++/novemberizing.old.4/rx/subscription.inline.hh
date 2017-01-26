#ifndef   __NOVEMBERIZING_RX__SUBSCRIPTION__INLINE__HH__
#define   __NOVEMBERIZING_RX__SUBSCRIPTION__INLINE__HH__

namespace novemberizing { namespace rx {

template <class T>
inline bool Subscription<T>::unsubscribed(void) const
{
    FUNCTION_START("");
    FUNCTION_END("");
    return __unsubscribed;
}

template <class T>
inline void Subscription<T>::emit(const T & o)
{
    FUNCTION_START("");
    try
    {
        __observer->onNext(o);
    }
    catch (const Throwable & e)
    {
        __observer->onError(e);
    }
    FUNCTION_END("");
}

template <class T>
inline void Subscription<T>::emit(const T && o)
{
    FUNCTION_START("");
    try
    {
        __observer->onNext(o);
    }
    catch (const Throwable & e)
    {
        __observer->onError(e);
    }
    FUNCTION_END("");
}

template <class T>
inline void Subscription<T>::error(const Throwable & e)
{
    FUNCTION_START("");
    __observer->onError(e);
    FUNCTION_END("");
}

template <class T>
inline void Subscription<T>::complete()
{
    FUNCTION_START("");
    __observer->onComplete();
    FUNCTION_END("");
}

template <class T>
Subscription<T>::Subscription(Observable<T> * observable, Observer<T> * observer) : __observable(observable), __observer(observer)
{
    FUNCTION_START("");
    FUNCTION_END("");
}

template <class T>
Subscription<T>::~Subscription(void)
{
    FUNCTION_START("");
    __observable = nullptr;
    __observer = nullptr;
    FUNCTION_END("");
}

} }

#endif // __NOVEMBERIZING_RX__SUBSCRIPTION__INLINE__HH__
