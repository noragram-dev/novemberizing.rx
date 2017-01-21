#ifndef   __NOVEMBERIZING_RX__OBSERVABLE__HH__
#define   __NOVEMBERIZING_RX__OBSERVABLE__HH__

namespace novemberizing { namespace rx {

template <class T>
class Observable : public Emittable<T>
{
public:     inline static void pub(Observable<T> & observable, const T & o);
public:     inline static void pub(Observable<T> & observable, const T && o);
private:    Subscriptions<T> __subscriptions;
protected:  inline virtual void emit(const T & o);
protected:  inline virtual void error(const Exception & e);
protected:  inline virtual void complete();
public:     inline virtual Subscription<T> * subscribe(Observer<T> * observer);
public:     inline Observable(void);
public:     inline virtual ~Observable(void);
public:     friend Subscription<T>;
};

} }

#include <novemberizing/rx/observable.inline.hh>

#endif // __NOVEMBERIZING_RX__OBSERVABLE__HH__
