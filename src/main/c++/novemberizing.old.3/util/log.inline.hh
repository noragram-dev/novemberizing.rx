#ifndef   __NOVEMBERIZING_UTIL__LOG__INLINE__HH__
#define   __NOVEMBERIZING_UTIL__LOG__INLINE__HH__

#include <stdio.h>

namespace novemberizing { namespace util {



} }

#define FUNCTION_START(format,...)	printf(format, ##__VA_ARGS__)

#define FUNCTION_END(format, ...)	printf(format, ##__VA_ARGS__)

#define ERROR_LOG(format, ...)	printf(format, ##__VA_ARGS__)

#define WARNING_LOG(format, ...)	printf(format, ##__VA_ARGS__)

#define CAUTION_LOG(format, ...)	printf(format, ##__VA_ARGS__)

#define NOTICE_LOG(format, ...)	printf(format, ##__VA_ARGS__)

#define INFORMATION_LOG(format, ...)	printf(format, ##__VA_ARGS__)

#define FLOW_LOG(format, ...)	printf(format, ##__VA_ARGS__)

#define DEBUG_LOG(format, ...)	printf(format, ##__VA_ARGS__)

#define VERBOSE_LOG(format, ...)	printf(format, ##__VA_ARGS__)


#endif // __NOVEMBERIZING_UTIL__LOG__INLINE__HH__
