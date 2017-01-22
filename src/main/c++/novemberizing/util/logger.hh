#ifndef   __NOVEMBERIZING_UTIL__LOGGER__HH__
#define   __NOVEMBERIZING_UTIL__LOGGER__HH__

#include <novemberizing.hh>

#include <novemberizing/util/log.hh>

namespace novemberizing { namespace util {

class Log;

class Logger
{
public:		inline void write(Log::Type type, Log::Time & current, const char * msg,type::uint32 len);
protected:	virtual void __write(Log::Type type, Log::Time & current, const char * msg,type::uint32 len) = 0;
public:		inline Logger(void);
public:		inline virtual ~Logger(void);
};

} }

#include <novemberizing/util/logger.cc>

#endif // __NOVEMBERIZING_UTIL__LOGGER__HH__
