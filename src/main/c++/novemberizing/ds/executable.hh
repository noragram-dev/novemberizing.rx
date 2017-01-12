#ifndef   __NOVEMBERIZING_DS__EXECUTABLE__HH__
#define   __NOVEMBERIZING_DS__EXECUTABLE__HH__

namespace novemberizing { namespace ds {

class executable
{
public:     virtual void execute(void) = 0;
public:     executable(void){}
public:     virtual ~executable(void){}
};

} }

#endif // __NOVEMBERIZING_DS__EXECUTABLE__HH__
