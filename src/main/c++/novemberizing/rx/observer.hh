#ifndef   __NOVEMBERIZING_RX__OBSERVER__HH__
#define   __NOVEMBERIZING_RX__OBSERVER__HH__

#include <novemberizing/util/log.hh>

#include <novemberizing/ds/throwable.hh>
#include <novemberizing/ds/concurrent.map.hh>

#include <novemberizing/rx/subscription.hh>

namespace novemberizing { namespace rx {

using namespace ds;
using namespace concurrency;

template <class T>
class Observer : public Sync
{
private:    ConcurrentMap<Observable<T> * ,Subscription<T> * > __subscriptions;
protected:  virtual void onNext(const T & item) = 0;
protected:  virtual void onError(const Throwable & exception) = 0;
protected:  virtual void onComplete(void) = 0;
public:     virtual bool isSubscribed(Observable<T> * observable);
public:     virtual void onSubscribe(Observable<T> * observable,Subscription<T> * observer);
public:     virtual void onUnsubscribe(Observable<T> * observable,Subscription<T> * observer);
public:     Observer(void);
public:     virtual ~Observer(void);
};

} }

#include <novemberizing/rx/observer.inline.hh>

#endif // __NOVEMBERIZING_RX__OBSERVER__HH__
