#ifndef   __NOVEMBERIZING_RX__OBSERVER__HH__
#define   __NOVEMBERIZING_RX__OBSERVER__HH__

namespace novemberizing { namespace rx {

template <class T>
class Observer
{
protected:  virtual void onNext(const T & o) = 0;
protected:  virtual void onError(const Exception & e) = 0;
protected:  virtual void onComplete(void) = 0;
public:     inline Observer(void)
            {
                FUNCTION_START("");
                FUNCTION_END("");
            }
public:     inline virtual ~Observer(void)
            {
                FUNCTION_START("");
                FUNCTION_END("");
            }
public:     friend Subscription<T>;
};

} }

#include <novemberizing/rx/observer.inline.hh>

#endif // __NOVEMBERIZING_RX__OBSERVER__HH__
