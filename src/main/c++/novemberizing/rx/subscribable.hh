#ifndef   __NOVEMBERIZING_RX__SUBSCRIBABLE__HH__
#define   __NOVEMBERIZING_RX__SUBSCRIBABLE__HH__

#include <novemberizing/util/log.hh>

namespace novemberizing { namespace rx {

template <class T> class Subscription;
template <class T> class Observer;

template <class T>
class Subscribable
{
public:     virtual Subscription<T> * subscribe(Observer<T> * observer) = 0;
public:     virtual bool unsubscribe(Subscription<T> * subscription) = 0;
public:     virtual void unsubscribe(void) = 0;
public:     Subscribable(void);
public:     virtual ~Subscribable(void);
};

} }

#include <novemberizing/rx/subscribable.inline.hh>

#endif // __NOVEMBERIZING_RX__SUBSCRIBABLE__HH__
