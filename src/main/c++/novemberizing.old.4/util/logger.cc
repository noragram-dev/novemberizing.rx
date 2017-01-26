#ifndef   __NOVEMBERIZING_UTIL__LOGGER__CC__
#define   __NOVEMBERIZING_UTIL__LOGGER__CC__

#include <novemberizing/util/log.hh>

namespace novemberizing { namespace util {

inline void Logger::write(Log::Type type, Log::Time & current, const char * msg,type::uint32 len)
{
	__write(type, current, msg, len);
}

inline Logger::Logger(void){}
inline Logger::~Logger(void){}

} }

#endif // __NOVEMBERIZING_UTIL__LOGGER__CC__/
