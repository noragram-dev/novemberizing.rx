#ifndef   __NOVEMBERIZING_DS__CONCURRENCY_LIST__INLINE__HH__
#define   __NOVEMBERIZING_DS__CONCURRENCY_LIST__INLINE__HH__

namespace novemberizing { namespace ds {

template <class T>
typename ConcurrencyList<T, Concurrency>::iterator ConcurrencyList<T>::begin(void)
{
	FUNCTION_START("");
	FUNCTION_END("");
	return __o.begin();
}

template <class T>
typename ConcurrencyList<T, Concurrency>::iterator ConcurrencyList<T>::end(void)
{
	FUNCTION_START("");
	FUNCTION_END("");
	return __o.end();
}

template <class T>
typename ConcurrencyList<T, Concurrency>::iterator ConcurrencyList<T>::erase(typename ConcurrencyList<T, Concurrency>::iterator it)
{
	FUNCTION_START("");
	FUNCTION_END("");
	return __o.erase(it);
}

template <class T>
type::size ConcurrencyList<T>::size(void) const
{
	FUNCTION_START("");
	FUNCTION_END("");
	return __o.size();
}

template <class T>
void ConcurrencyList<T>::front(const T & o)
{
	FUNCTION_START("");
	__o.push_front(o);
	FUNCTION_END("");
}

template <class T>
void ConcurrencyList<T>::front(const T && o)
{
	FUNCTION_START("");
	__o.push_front(o);
	FUNCTION_END("");
}

template <class T>
void ConcurrencyList<T>::back(const T & o)
{
	FUNCTION_START("");
	__o.push_back(o);
	FUNCTION_END("");
}

template <class T>
void ConcurrencyList<T>::back(const T && o)
{
	FUNCTION_START("");
	__o.push_back(o);
	FUNCTION_END("");
}

template <class T>
T & ConcurrencyList<T>::front(void)
{
	FUNCTION_START("");
	T ret = __o.front();
	__o.pop_front();
	FUNCTION_END("");
	return ret;
}

template <class T>
T & ConcurrencyList<T>::back(void)
{
	FUNCTION_START("");
	T ret = __o.back();
	__o.pop_back();
	FUNCTION_END("");
	return ret;
}

template <class T>
void ConcurrencyList<T>::push(const T & o)
{
	FUNCTION_START("");
	__o.push_back(o);
	FUNCTION_END("");
}

template <class T>
void ConcurrencyList<T>::push(const T && o)
{
	FUNCTION_START("");
	__o.push_back(o);
	FUNCTION_END("");
}

template <class T>
T & ConcurrencyList<T>::pop(void)
{
	FUNCTION_START("");
	T ret = __o.back();
	__o.pop_back();
	FUNCTION_END("");
	return ret;
}

template <class T>
ConcurrencyList<T>::ConcurrencyList(void)
{
	FUNCTION_START("");
	FUNCTION_END("");
}

template <class T>
ConcurrencyList<T>::~ConcurrencyList(void)
{
	FUNCTION_START("");
	FUNCTION_END("");
}

} }

#endif // __NOVEMBERIZING_DS__CONCURRENCY_LIST__INLINE__HH__
