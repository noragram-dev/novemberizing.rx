#ifndef   __NOVEMBERIZING_RX__OBSERVABLE__HH__
#define   __NOVEMBERIZING_RX__OBSERVABLE__HH__

#include <initializer_list>

#include <novemberizing.hh>

#include <novemberizing/util/log.hh>

#include <novemberizing/ds/throwable.hh>

#include <novemberizing/concurrency/sync.hh>

#include <novemberizing/rx/emittable.hh>
#include <novemberizing/rx/observer.hh>
#include <novemberizing/rx/subscription.hh>
#include <novemberizing/rx/subscription.list.hh>
#include <novemberizing/rx/player.hh>

namespace novemberizing { namespace rx {

using namespace ds;
using namespace concurrency;

template <class T> class SubscriptionList;
template <class T> class Subscription;
template <class T> class Observer;

template <class T>
class Observable : public Emittable<T>
{
public:		inline static Observable<T> just(const T & o);
public:		inline static Observable<T> just(const T && o);
public:		inline static Observable<T> just(std::initializer_list<T> items);
private:    SubscriptionList<T> __subscriptionList;
private:	Player<T> __replayer;
private:	type::size __emits;
protected:  inline virtual void emit(const T & o);
protected:  inline virtual void emit(const T && o);
protected:  inline virtual void error(const Throwable & e);
protected:  inline virtual void complete(void);
public:     inline virtual Subscription<T> * subscribe(Observer<T> * observer);
public:		inline Observable<T> & operator=(const Observable & observable);
public:     inline Observable(void);
public:		inline Observable(const Observable<T> & observable);
public:     inline virtual ~Observable(void);
public:     friend Subscription<T>;
};

} }

#include <novemberizing/rx/observable.inline.hh>


#endif // __NOVEMBERIZING_RX__OBSERVABLE__HH__
