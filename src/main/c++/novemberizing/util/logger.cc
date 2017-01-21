#ifndef   __NOVEMBERIZING_UTIL__LOGGER__INLINE__HH__
#define   __NOVEMBERIZING_UTIL__LOGGER__INLINE__HH__

#include <novemberizing/util/log.hh>

namespace novemberizing { namespace util {

void Logger::write(Log::Type type, Log::Time & current, const char * msg,type::uint32 len)
{
	__write(type, current, msg, len);
}

Logger::Logger(void){}
Logger::~Logger(void){}

} }

#endif // __NOVEMBERIZING_UTIL__LOGGER__INLINE__HH__
