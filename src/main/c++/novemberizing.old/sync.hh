#ifndef   __NOVEMBERIZING__SYNC__HH__
#define   __NOVEMBERIZING__SYNC__HH__

#include <pthread.h>
#include <stdlib.h>
#include <stdio.h>

#include <novemberizing.hh>

namespace novemberizing {

class Sync
{
public:     virtual void lock(void){}
public:     virtual void unlock(void){}
public:     Sync(void){}
public:     virtual ~Sync(void){}
};

}

#endif // __NOVEMBERIZING__SYNC__HH__
