#ifndef   __NOVEMBERIZING_RX__CYCLABLE__HH__
#define   __NOVEMBERIZING_RX__CYCLABLE__HH__

namespace novemberizing { namespace rx {

class Cyclable
{
public:     virtual void onecycle(void) = 0;
public:     Cyclable(void){}
public:     virtual ~Cyclable(void){}
};

} }

#endif // __NOVEMBERIZING_RX__CYCLABLE__HH__
