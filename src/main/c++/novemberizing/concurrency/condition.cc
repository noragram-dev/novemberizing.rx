#ifndef   __NOVEMBERIZING_CONCURRENCY__CONDITION__CC__
#define   __NOVEMBERIZING_CONCURRENCY__CONDITION__CC__

namespace novemberizing { namespace concurrency {

Condition::Condition(void)
{
	FUNCTION_START("");
	FUNCTION_END("");
}

Condition::~Condition(void)
{
	FUNCTION_START("");
	FUNCTION_END("");
}

void Condition::suspend(type::nano nano){}

void Condition::resume(bool all){}

} }

#endif // __NOVEMBERIZING_CONCURRENCY__CONDITION__CC__
