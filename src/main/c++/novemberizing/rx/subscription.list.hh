#ifndef   __NOVEMBERIZING_RX__SUBSCRIPTION_LIST__HH__
#define   __NOVEMBERIZING_RX__SUBSCRIPTION_LIST__HH__

#include <list>

#include <novemberizing.hh>

#include <novemberizing/util/log.hh>

#include <novemberizing/concurrency/sync.hh>

#include <novemberizing/rx/subscription.hh>

namespace novemberizing { namespace rx {

using namespace concurrency;

template <class T>
class SubscriptionList : public Sync
{
public:     typedef typename std::list<Subscription<T> *>::iterator iterator;
private:    std::list<Subscription<T> *> __o;
public:     inline typename SubscriptionList<T>::iterator begin(void);
public:     inline typename SubscriptionList<T>::iterator end(void);
public:     inline typename SubscriptionList<T>::iterator erase(typename SubscriptionList<T>::iterator it);
public:		inline void front(Subscription<T> * subscription);
public:     inline void back(Subscription<T> * subscription);
public:     inline SubscriptionList(void);
public:     inline virtual ~SubscriptionList(void);
};

} }

#include <novemberizing/rx/subscription.list.inline.hh>

#endif // __NOVEMBERIZING_RX__SUBSCRIPTION_LIST__HH__
