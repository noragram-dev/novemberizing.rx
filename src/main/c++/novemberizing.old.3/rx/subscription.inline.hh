#ifndef   __NOVEMBERIZING_RX__SUBSCRIPTION__INLINE__HH__
#define   __NOVEMBERIZING_RX__SUBSCRIPTION__INLINE__HH__

namespace novemberizing { namespace rx {

template <class T>
inline Subscription<T>::Subscription(void) : __observable(nullptr), __observer(nullptr), __previous(nullptr), __next(nullptr) {}

template <class T>
inline Subscription<T>::Subscription(Observable<T> * observable, Observer<T> * observer) : __observable(observable), __observer(observer), __next(nullptr)
{
    __previous = __observable->back();
    if (__previous != nullptr)
    {
        __previous->next(this);     /** already lock because called in subscribe */
        __observable->back(this);   /** already lock because called in subscribe */
        sychronized(__observer, {
            __observer->back(this);
        });
    }
}

inline void next(Subscription<T> * v)
{
    if(__next!=nullptr)
    {
        DEBUG_LOG("__next!=nullptr");
    }
    __next = v;
}

} }

#endif // __NOVEMBERIZING_RX__SUBSCRIPTION__INLINE__HH__
