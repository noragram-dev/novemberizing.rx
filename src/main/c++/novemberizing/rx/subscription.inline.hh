#ifndef   __NOVEMBERIZING_RX__SUBSCRIPTION__INLINE__HH__
#define   __NOVEMBERIZING_RX__SUBSCRIPTION__INLINE__HH__

namespace novemberizing { namespace rx {

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

    __unsubscribed = true;
    __observable = nullptr;
    __observer = nullptr;

    FUNCTION_END("");
}

} }

#endif // __NOVEMBERIZING_RX__SUBSCRIPTION__INLINE__HH__
