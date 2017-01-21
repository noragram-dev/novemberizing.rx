#ifndef   __NOVEMBERIZING_RX__OBSERVER__HH__
#define   __NOVEMBERIZING_RX__OBSERVER__HH__

using namespace novemberizing::ds;

namespace novemberizing { namespace rx {

template <class T>
class Observer
{
private:	Subscription<T> * __front;
private:	Subscription<T> * __back;
protected:	void back(Subscription<T> * subscription);
public:		virtual void onNext(const T & o) = 0;
public:		virtual void onError(const Exception & e) = 0;
public:		virtual void onComplete(void) = 0;
public:		Observer(void){}
public:		virtual ~Observer(void){}
};

} }

#endif // __NOVEMBERIZING_RX__OBSERVER__HH__
