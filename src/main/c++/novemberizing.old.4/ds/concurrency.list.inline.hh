#ifndef   __NOVEMBERIZING_DS__CONCURRENCY_LIST__INLINE__HH__
#define   __NOVEMBERIZING_DS__CONCURRENCY_LIST__INLINE__HH__

namespace novemberizing { namespace ds {

template <class T, class Concurrency>
typename ConcurrencyList<T, Concurrency>::iterator ConcurrencyList<T, Concurrency>::begin(void)
{
	FUNCTION_START("");
	FUNCTION_END("");
	return __o.begin();
}

template <class T, class Concurrency>
typename ConcurrencyList<T, Concurrency>::iterator ConcurrencyList<T, Concurrency>::end(void)
{
	FUNCTION_START("");
	FUNCTION_END("");
	return __o.end();
}

template <class T, class Concurrency>
typename ConcurrencyList<T, Concurrency>::iterator ConcurrencyList<T, Concurrency>::erase(typename ConcurrencyList<T, Concurrency>::iterator it)
{
	FUNCTION_START("");
	FUNCTION_END("");
	return __o.erase(it);
}

template <class T, class Concurrency>
type::size ConcurrencyList<T, Concurrency>::size(void) const
{
	FUNCTION_START("");
	FUNCTION_END("");
	return __o.size();
}

template <class T, class Concurrency>
void ConcurrencyList<T, Concurrency>::front(const T & o)
{
	FUNCTION_START("");
	__o.push_front(o);
	FUNCTION_END("");
}

template <class T, class Concurrency>
void ConcurrencyList<T, Concurrency>::front(const T && o)
{
	FUNCTION_START("");
	__o.push_front(o);
	FUNCTION_END("");
}

template <class T, class Concurrency>
void ConcurrencyList<T, Concurrency>::back(const T & o)
{
	FUNCTION_START("");
	__o.push_back(o);
	FUNCTION_END("");
}

template <class T, class Concurrency>
void ConcurrencyList<T, Concurrency>::back(const T && o)
{
	FUNCTION_START("");
	__o.push_back(o);
	FUNCTION_END("");
}

template <class T, class Concurrency>
T & ConcurrencyList<T, Concurrency>::front(void)
{
	FUNCTION_START("");
	T ret = __o.front();
	__o.pop_front();
	FUNCTION_END("");
	return ret;
}

template <class T, class Concurrency>
T & ConcurrencyList<T, Concurrency>::back(void)
{
	FUNCTION_START("");
	T ret = __o.back();
	__o.pop_back();
	FUNCTION_END("");
	return ret;
}

template <class T, class Concurrency>
void ConcurrencyList<T, Concurrency>::push(const T & o)
{
	FUNCTION_START("");
	__o.push_back(o);
	FUNCTION_END("");
}

template <class T, class Concurrency>
void ConcurrencyList<T, Concurrency>::push(const T && o)
{
	FUNCTION_START("");
	__o.push_back(o);
	FUNCTION_END("");
}

template <class T, class Concurrency>
T & ConcurrencyList<T, Concurrency>::pop(void)
{
	FUNCTION_START("");
	T ret = __o.back();
	__o.pop_back();
	FUNCTION_END("");
	return ret;
}

template <class T, class Concurrency>
ConcurrencyList<T, Concurrency>::ConcurrencyList(void)
{
	FUNCTION_START("");
	FUNCTION_END("");
}

template <class T, class Concurrency>
ConcurrencyList<T, Concurrency>::~ConcurrencyList(void)
{
	FUNCTION_START("");
	FUNCTION_END("");
}

} }

#endif // __NOVEMBERIZING_DS__CONCURRENCY_LIST__INLINE__HH__
