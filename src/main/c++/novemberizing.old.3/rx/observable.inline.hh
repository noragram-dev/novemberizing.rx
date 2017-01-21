#ifndef   __NOVEMBERIZING_RX__OBSERVABLE__INLINE__HH__
#define   __NOVEMBERIZING_RX__OBSERVABLE__INLINE__HH__

#include <novemberizing/rx/observable.hh>
#include <novemberizing/rx/subscription.hh>
#include <novemberizing/rx/observer.hh>

namespace novemberizing { namespace rx {

template <class T>
Observable<T>::Observable(void) : __front(nullptr), __back(nullptr)
{

}

template <class T>
Observable<T>::~Observable(void)
{

}

template <class T>
void Observable<T>::publish(const T & o)
{
    synchronized(this, {
        Subscription<T> subscription = __front;
        while (subscription != nullptr)
        {
            synchronized(subscription, {
                Observer<T> * observer = subscription->observer();
                try
                {
                    observer->onNext(o);
                }
                catch (const Exception & e)
                {
                    observer->onError(e);
                }
                subscription = subscription->next();
            });
        }
    });
}

template <class T>
Observable<T> & Observable<T>::subscribe(Observer<T> * observer)
{
	if(observer!=nullptr)
	{
		/**
		 * @todo: implement observer & observable link
		 */
        synchronized(this, {
            if (__back == nullptr)
            {
                __observers++;
                __front = __back = new Subscription(this, observer);
            }
            else
            {
                synchronized(__back, {
                    __back = __back->next(new Subscription(this, observer));
                });
            }
        });
	}
	else
	{
		DEBUG_LOG("observer==nullptr");
	}
	return *this;
}

	// private:	const Observer<T> * __front;
	// private:	const Observer<T> * __back;
	// public:		virtual Observable<T> & subscribe(Observer<T> * observer);
	// public:		virtual Observable<T> & unsubscribe(Observer<T> * observer);
	// public:		Observable(void);
	// public:		virtual ~Observable(void);

} }


#endif // __NOVEMBERIZING_RX__OBSERVABLE__INLINE__HH__
