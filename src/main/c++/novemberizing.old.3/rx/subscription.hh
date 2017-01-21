#ifndef   __NOVEMBERIZING_RX__SUBSCRIPTION__HH__
#define   __NOVEMBERIZING_RX__SUBSCRIPTION__HH__

#include <novemberizing/concurrency/sync.hh>

using namespace novemberizing::util;

namespace novemberizing { namespace rx {

template <class T>
class Subscription : public Sync
{
private:    Observable<T> * __observable;
private:    Observer<T> * __observer;
private:    Subscription<T> * __previous;
private:    Subscription<T> * __next;
public:     Observer<T> * observer(void);
public:     Subscription<T> * next(void);
public:		inline void next(Subscription<T> * v);
public:		inline Subscription(void);
public:     inline Subscription(Observable<T> * observable, Observer<T> * observer);
public:		virtual ~Subscription(void);
};

} }

#include <novemberizing/rx/subscription.inline.hh>

#endif // __NOVEMBERIZING_RX__SUBSCRIPTION__HH__
