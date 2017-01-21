#ifndef   __NOVEMBERIZING_RX__OBSERVER__HH__
#define   __NOVEMBERIZING_RX__OBSERVER__HH__

#include <novemberizing/util/log.hh>

#include <novemberizing/ds/throwable.hh>

#include <novemberizing/concurrency/sync.hh>

#include <novemberizing/rx/subscription.hh>

namespace novemberizing { namespace rx {

template <class T> class Subscription;

using namespace ds;
using namespace concurrency;

template <class T>
class Observer
{
protected:  virtual void onNext(const T & o) = 0;
protected:  virtual void onError(const Throwable & e) = 0;
protected:  virtual void onComplete(void) = 0;
public:     inline Observer(void);
public:     inline virtual ~Observer(void);
public:     friend Subscription<T>;
};

} }

#include <novemberizing/rx/observer.inline.hh>

#endif // __NOVEMBERIZING_RX__OBSERVER__HH__
