#ifndef   __NOVEMBERIZING_RX__OPERATOR__HH__
#define   __NOVEMBERIZING_RX__OPERATOR__HH__

namespace novemberizing { namespace rx {

template <class T,class U>
class Operator
{
protected:  void in(void) = 0;
protected:  void on(void) = 0;
protected:  void out(void) = 0;
public:     Operator(void){}
public:     virtual ~Operator(void){}
};

} }

#endif // __NOVEMBERIZING_RX__OPERATOR__HH__

