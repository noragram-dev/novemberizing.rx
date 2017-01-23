#ifndef   __NOVEMBERIZING_RX__SUBSCRIBERS__HH__
#define   __NOVEMBERIZING_RX__SUBSCRIBERS__HH__

#include <iostream>

#include <novemberizing/rx/observer.hh>

namespace novemberizing { namespace rx {

class Subscribers
{
private:    template <class T>
            class __Just : public Observer<T>
            {
            protected:  virtual void onNext(const T & o){ std::cout<<o<<std::endl; }
            protected:  virtual void onError(const Throwable & e){ std::cout<<"error: "<<e.msg()<<std::endl; }
            protected:  virtual void onComplete(void){ std::cout<<"complete"<<std::endl; }
            public:     __Just(void){}
            public:     virtual ~__Just(void){}
            };
public:     template <class T> static Observer<T> * Just(void){ return new __Just<T>(); }
public:     Subscribers(void){}
public:     virtual ~Subscribers(void){}
};

} }

#endif // __NOVEMBERIZING_RX__SUBSCRIBERS__HH__
