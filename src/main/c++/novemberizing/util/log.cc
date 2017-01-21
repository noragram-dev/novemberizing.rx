#ifndef   __NOVEMBERIZING_UTIL__LOG__INLINE__HH__
#define   __NOVEMBERIZING_UTIL__LOG__INLINE__HH__

#include <novemberizing/util/log.hh>
#include <novemberizing/util/logger.hh>

namespace novemberizing { namespace util {

Log Log::o;

static const char * toString(Log::Type type)
{
	switch(type)
	{
	case Log::ERROR:		return "error";
	case Log::WARNING:		return "warning";
	case Log::CAUTION:		return "caution";
	case Log::NOTICE:		return "notice";
	case Log::INFORMATION:	return "information";
	case Log::FLOW:			return "flow";
	case Log::DEBUG:		return "debug";
	case Log::VERBOSE:		return "verbose";
	default:				return "unknown";
	}
	return "unknown";
}

Log::Log(void)
{
    __types = 0xFFFFFFFF;
}

Log::~Log(void)
{
	for(std::list<Logger *>::iterator it = __loggers.begin();it!=__loggers.end();it = __loggers.erase(it))
	{
		Logger * logger = *it;
		delete logger;
	}
}

void Log::write(Log::Type type, const char * file, type::uint32 line, const char * function, const char * format, ...)
{
	if(__types & type)
	{
		const type::uint32 size = 4096;
		char str[size];
		Log::Time current;
		int n = snprintf(	str,
							size,
							"%04u/%02u/%02u %02u:%02u:%02u.%09lu %s:%d %s %lu %s > ",
							current.year(),
							current.month(),
							current.day(),
							current.hour(),
							current.minute(),
							current.second(),
							current.nano(),
							file,
							line,
							function,
							::pthread_self(),
							toString(type));
		va_list ap;
		va_start(ap, format);
		n+=vsnprintf(&str[n],size-n-1,format,ap);
		va_end(ap);
		str[n] = '\n';
        str[n+1] = 0;
		if(__loggers.size()>0)
		{
			for(std::list<Logger *>::iterator it = __loggers.begin();it!=__loggers.end();it++)
			{
				Logger * logger = *it;
				logger->write(type, current, str, n);
			}
		}
		else
		{
			printf("%s",str);
		}
	}
}

Log & Log::add(Logger * logger)
{
	if(logger!=nullptr)
	{
		if(std::find(__loggers.begin(),__loggers.end(),logger)!=__loggers.end())
		{
			__loggers.push_back(logger);
		}
	}
	return *this;
}

Log & Log::del(Logger * logger)
{
	if(logger!=nullptr)
	{
		__loggers.erase(std::find(__loggers.begin(),__loggers.end(),logger));
	}
	return *this;
}

Log::Time::Time(void)
{
#ifdef WIN32
    __year = 0;
    __month = 0;
    __day = 0;
    __hour = 0;
    __minute = 0;
    __second = 0;
    __nano = 0;
    __timestamp = 0;
#else
	struct timespec spec;
	if(::clock_gettime(CLOCK_REALTIME, &spec)==Success)
	{
		__nano = spec.tv_nsec;
		__timestamp = spec.tv_sec;
		struct tm tm;
		localtime_r(&__timestamp, &tm);
		__year = 1900 + tm.tm_year;
		__month = 1 + tm.tm_mon;
		__day = tm.tm_mday;
		__hour = tm.tm_hour;
		__minute = tm.tm_min;
		__second = tm.tm_sec;
	}
	else
	{
		printf("fail to ::clock_gettime(...) caused by %d\n",errno);
	}
#endif
}

Log::Time::~Time(void) {}

} }

#endif // __NOVEMBERIZING_UTIL__LOG__INLINE__HH__
