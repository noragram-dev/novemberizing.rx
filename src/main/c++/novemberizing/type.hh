#ifndef   __NOVEMBERIZING__TYPE__HH__
#define   __NOVEMBERIZING__TYPE__HH__

#include <stdint.h>
#include <pthread.h>
#include <time.h>
#include <stdarg.h>

namespace novemberizing { namespace type {

typedef int8_t          int8;
typedef uint8_t         uint8;
typedef int16_t         int16;
typedef uint16_t        uint16;
typedef int32_t         int32;
typedef uint32_t        uint32;
typedef int64_t         int64;
typedef uint64_t        uint64;

typedef unsigned char   byte;

typedef pthread_t       thread;

typedef time_t          second;
typedef long            nano;

typedef size_t          size;

typedef va_list         args;

typedef int             descriptor;

} }

#endif // __NOVEMBERIZING__TYPE__HH__
