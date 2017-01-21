#ifndef   __NOVEMBERIZING_RX__OBSERVABLE__HH__
#define   __NOVEMBERIZING_RX__OBSERVABLE__HH__

#include <novemberizing/concurrency/sync.hh>

using namespace novemberizing::concurrency;

namespace novemberizing { namespace rx {

template <class T>
class Observable : public Sync
{
private:	const Subscription<T> * __front;
private:	const Subscription<T> * __back;
private:    unsigned int __observers;
private:    void publish(const T & o);
protected:  Subscription<T> * back(void);
protected:  Subscription<T> * front(void);
protected:  void back(Subscription<T> * v);
public:		virtual Observable<T> & subscribe(Observer<T> * observer);
public:		virtual Observable<T> & unsubscribe(Observer<T> * observer);
public:		Observable(void);
public:		virtual ~Observable(void);
};

} }

#include <novemberizing/rx/observable.inline.hh>

#endif // __NOVEMBERIZING_RX__OBSERVABLE__HH__
