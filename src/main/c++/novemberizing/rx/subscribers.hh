#ifndef   __NOVEMBERIZING_RX__SUBSCRIBERS__HH__
#define   __NOVEMBERIZING_RX__SUBSCRIBERS__HH__

#include <novemberizing/rx/observer.hh>

namespace novemberizing { namespace rx {

class Subscribers
{
public:     template <class T>
            class Just : public Observer<T>
            {
            protected:  virtual void onNext(const T & o);
            protected:  virtual void onError(const Throwable & e);
            protected:  virtual void onComplete(void);
            public:     Just(void);
            public:     virtual ~Just(void);
            };
public:     Subscribers(void);
public:     virtual ~Subscribers(void);
};

} }

#endif // __NOVEMBERIZING_RX__SUBSCRIBERS__HH__
