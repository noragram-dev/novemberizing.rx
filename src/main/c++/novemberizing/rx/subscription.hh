#ifndef   __NOVEMBERIZING_RX__SUBSCRIPTION__HH__
#define   __NOVEMBERIZING_RX__SUBSCRIPTION__HH__

#include <novemberizing/util/log.hh>

namespace novemberizing { namespace rx {

template <class T> class Observable;
template <class T> class Observer;

template <class T>
class Subscription
{
private:    Observable<T> * __observable;
private:    Observer<T> * __observer;
private:    bool __unsubscribed;
public:     Subscription(Observable<T> * observable, Observer<T> * observer);
public:     virtual ~Subscription(void);
};

} }

#include <novemberizing/rx/subscription.inline.hh>

#endif // __NOVEMBERIZING_RX__SUBSCRIPTION__HH__
