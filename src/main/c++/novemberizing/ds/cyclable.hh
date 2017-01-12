#ifndef   __NOVEMBERIZING_DS__CYCLABLE__HH__
#define   __NOVEMBERIZING_DS__CYCLABLE__HH__

namespace novemberizing { namespace ds {

class cyclable
{
public:     virtual void onecycle(void) = 0;
public:     cyclable(void){}
public:     virtual ~cyclable(void){}
};

} }

#endif // __NOVEMBERIZING_DS__CYCLABLE__HH__
