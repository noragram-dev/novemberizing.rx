#ifndef   __NOVEMBERIZING_RX__EXECUTABLE__HH__
#define   __NOVEMBERIZING_RX__EXECUTABLE__HH__

namespace novemberizing { namespace rx {

class Executable
{
public:     virtual void execute(Executor * executor) = 0;
public:     Executable(void){}
public:     virtual ~Executable(void){}
};

} }

#endif // __NOVEMBERIZING_RX__EXECUTABLE__HH__
