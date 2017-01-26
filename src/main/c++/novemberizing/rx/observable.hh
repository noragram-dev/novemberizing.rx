#ifndef   __NOVEMBERIZING_RX__OBSERVABLE__HH__
#define   __NOVEMBERIZING_RX__OBSERVABLE__HH__

#include <novemberizing/ds.hh>

#include <novemberizing/ds/concurrent.list.hh>

#include <novemberizing/rx/emittable.hh>
#include <novemberizing/rx/subscribable.hh>
#include <novemberizing/rx/replayer.hh>

namespace novemberizing { namespace rx {

using namespace ds;

template <class T>
class Observable : public Emittable<T>, public Subscribable<T>
{
private:    ConcurrentList<Subscription<T> * > __subscriptions;
private:    Replayer<T> __replayer;
private:    void __emit(const T & item);
private:    void __error(const Throwable & e);
private:    void __complete(void);
public:     virtual Subscription<T> * subscribe(Observer<T> * observer);
public:     virtual bool unsubscribe(Subscription<T> * subscription);
public:     virtual void unsubscribe(void);
public:     Observable(void);
public:     virtual ~Observable(void);
};

} }

#include <novemberizing/rx/observable.inline.hh>

#endif // __NOVEMBERIZING_RX__OBSERVABLE__HH__
