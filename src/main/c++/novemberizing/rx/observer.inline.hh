#ifndef   __NOVEMBERIZING_RX__OBSERVER__INLINE__HH__
#define   __NOVEMBERIZING_RX__OBSERVER__INLINE__HH__

#include <novemberizing/rx/observer.hh>

namespace novemberizing { namespace rx {

template <class T>
Observer<T>::Observer(void)
{
    FUNCTION_START("");
    FUNCTION_END("");
}

template <class T>
Observer<T>::~Observer(void)
{
    FUNCTION_START("");
    FUNCTION_END("");
}

template <class T>
bool Observer<T>::isSubscribed(Observable<T> * observable)
{
    FUNCTION_START("");
    FUNCTION_END("");
    return observable!=nullptr && __subscriptions.exist(observable);
}

template <class T>
void Observer<T>::onSubscribe(Observable<T> * observable,Subscription<T> * subscription)
{
    FUNCTION_START("");
    synchronized(&__subscriptions,__subscriptions.put(observable, subscription));
    FUNCTION_END("");
}

template <class T>
void Observer<T>::onUnsubscribe(Observable<T> * observable,Subscription<T> * subscription)
{
    FUNCTION_START("");
    synchronized(&__subscriptions,if(__subscriptions.del(observable)==subscription){
        /**
         * @todo: delete subscription logic ... 
         */
    });
    FUNCTION_END("");
}

} }

#endif // __NOVEMBERIZING_RX__OBSERVER__INLINE__HH__
