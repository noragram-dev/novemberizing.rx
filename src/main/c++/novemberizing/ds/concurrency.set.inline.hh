#ifndef   __NOVEMBERIZING_DS__CONCURRENCY_SET__INLINE__HH__
#define   __NOVEMBERIZING_DS__CONCURRENCY_SET__INLINE__HH__

namespace novemberizing { namespace ds {

template <class T>
typename ConcurrencySet<T, Concurrency>::iterator ConcurrencySet<T>::begin(void)
{
	FUNCTION_START("");
	FUNCTION_END("");
	return __o.begin();
}

template <class T>
typename ConcurrencySet<T, Concurrency>::iterator ConcurrencySet<T>::end(void)
{
	FUNCTION_START("");
	FUNCTION_END("");
	return __o.end();
}

template <class T>
typename ConcurrencySet<T, Concurrency>::iterator ConcurrencySet<T>::erase(typename ConcurrencySet<T, Concurrency>::iterator it)
{
	__o.erase(it++);
	return it;
}

template <class T>
inline bool ConcurrencySet<T>::add(const T & o)
{
	FUNCTION_START("");
	std::pair<typename std::set<T>::iterator, bool> inserted = __o.insert(o);
	FUNCTION_END("");
	return inserted.second;
}

template <class T>
inline bool ConcurrencySet<T>::add(const T && o)
{
	FUNCTION_START("");
	std::pair<typename std::set<T>::iterator, bool> inserted = __o.insert(o);
	FUNCTION_END("");
	return inserted.second;
}

template <class T>
inline bool ConcurrencySet<T>::del(const T & o)
{
	FUNCTION_START("");
	bool ret = __o.erase(o)==1;
	FUNCTION_END("");
	return ret;
}

template <class T>
inline bool ConcurrencySet<T>::del(const T && o)
{
	FUNCTION_START("");
	bool ret = __o.erase(o)==1;
	FUNCTION_END("");
	return ret;
}

template <class T>
inline bool ConcurrencySet<T>::exist(const T & o)
{
	FUNCTION_START("");
	bool ret = __o.find(o)==__o.end();
	FUNCTION_END("");
	return ret;
}

template <class T>
inline bool ConcurrencySet<T>::exist(const T && o)
{
	FUNCTION_START("");
	bool ret = __o.find(o)==__o.end();
	FUNCTION_END("");
	return ret;
}

template <class T>
inline type::size ConcurrencySet<T>::size(void) const
{
	FUNCTION_START("");
	FUNCTION_END("");
	return __o.size();
}

template <class T>
ConcurrencySet<T>::ConcurrencySet(void)
{
	FUNCTION_START("");
	FUNCTION_END("");
}

template <class T>
ConcurrencySet<T>::~ConcurrencySet(void)
{
	FUNCTION_START("");
	FUNCTION_END("");
}

} }

#endif // __NOVEMBERIZING_DS__CONCURRENCY_SET__INLINE__HH__
