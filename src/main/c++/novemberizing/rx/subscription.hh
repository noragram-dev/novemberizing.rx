#ifndef   __NOVEMBERIZING_RX__SUBSCRIPTION__HH__
#define   __NOVEMBERIZING_RX__SUBSCRIPTION__HH__

#include <novemberizing/util/log.hh>

#include <novemberizing/ds/throwable.hh>

#include <novemberizing/concurrency/sync.hh>

#include <novemberizing/rx/observable.hh>
#include <novemberizing/rx/observer.hh>

namespace novemberizing { namespace rx {

template <class T> class Observable;
template <class T> class Observer;

using namespace ds;
using namespace concurrency;

template <class T>
class Subscription : public Emittable<T>, public Sync
{
public:     friend Observable<T>;
private:    Observable<T> * __observable;
private:    Observer<T> * __observer;
private:    bool __unsubscribed;
public:     inline bool unsubscribed(void) const;
protected:  inline virtual void emit(const T & o);
protected:  inline virtual void error(const Throwable & e);
protected:  inline virtual void complete();
protected:  inline Subscription(Observable<T> * observable, Observer<T> * observer);
protected:  inline virtual ~Subscription(void);
};

} }

#include <novemberizing/rx/subscription.inline.hh>

#endif // __NOVEMBERIZING_RX__SUBSCRIPTION__HH__
