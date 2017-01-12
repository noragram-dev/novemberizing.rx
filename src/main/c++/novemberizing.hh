#ifndef   __NOVEMBERIZING__CONSTANT__HH__
#define   __NOVEMBERIZING__CONSTANT__HH__

#include <sys/types.h>

namespace novemberizing {

enum {
    Success = 0L,
    Fail = -1L,
    Infinite = -1L,
};

namespace type {

typedef long nano;

typedef size_t size;

};

}

#endif // __NOVEMBERIZING__CONSTANT__HH__
