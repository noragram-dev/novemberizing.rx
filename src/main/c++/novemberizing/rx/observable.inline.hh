#ifndef   __NOVEMBERIZING_RX__OBSERVABLE__INLINE__HH__
#define   __NOVEMBERIZING_RX__OBSERVABLE__INLINE__HH__

#include <novemberizing/rx/observable.hh>

namespace novemberizing { namespace rx {

template <class T>
Observable<T>::Observable(void)
{
    FUNCTION_START("");
    FUNCTION_END("");
}

template <class T>
Observable<T>::~Observable(void)
{
    FUNCTION_START("");
    FUNCTION_END("");
}

template <class T>
void Observable<T>::__emit(const T & item)
{
    FUNCTION_START("");
    synchronized(&__observers,{
        foreach(__observers.begin(),__observers.end(), {
            Observer<T> * observer = *it;
            if(observer!=nullptr){
                observer->onNext(item);
            }
        });
    });
    FUNCTION_END("");
}

template <class T>
void Observable<T>::__error(const Throwable & e)
{
    FUNCTION_START("");
    synchronized(&__observers,{
        foreach(__observers.begin(),__observers.end(), {
            Observer<T> * observer = *it;
            if(observer!=nullptr){
                observer->onError(e);
            }
        });
    });
    FUNCTION_END("");
}

template <class T>
void Observable<T>::__complete(void)
{
    FUNCTION_START("");
    synchronized(&__observers,{
        foreach(__observers.begin(),__observers.end(), {
            Observer<T> * observer = *it;
            if(observer!=nullptr){
                observer->onComplete();
            }
        });
    });
    FUNCTION_END("");
}

template <class T>
Subscription<T> * Observable<T>::subscribe(Observer<T> * observer)
{
    FUNCTION_START("");
    // Subscription<T> * subscription = nullptr;
    // if(observer!=nullptr)
    // {
    //     if(!observer->subscribed(this))
    //     {
    //         subscription = new Subscription<T>(this, observer);
    //         /**
    //          * @todo: check deadlock
    //          */
    //         synchronized(&__subscriptions,{
    //             synchronized(observer,{
    //                 observer->onSubscribe(this, subscription);
    //                 __replayer.on(observer);
    //                 __subscriptions.push(subscription);
    //             });
    //         });
    //     }
    //     else
    //     {
    //         CAUTION_LOG("!observer->subscribed(this)");
    //     }
    // }
    // else
    // {
    //     CAUTION_LOG("observer==nullptr");
    // }
    FUNCTION_END("");
    return nullptr;
}

template <class T>
bool Observable<T>::unsubscribe(Subscription<T> * subscription)
{
    FUNCTION_START("");

    INFORMATION_LOG("implement this");

    FUNCTION_END("");
    return false;
}

template <class T>
void Observable<T>::unsubscribe(void)
{
    FUNCTION_START("");

    INFORMATION_LOG("implement this");

    FUNCTION_END("");
}

} }

#endif // __NOVEMBERIZING_RX__OBSERVABLE__INLINE__HH__
