#ifndef   __NOVEMBERIZING_DS__CONCURRENCY_LIST__HH__
#define   __NOVEMBERIZING_DS__CONCURRENCY_LIST__HH__

#include <novemberizing.hh>

#include <novemberizing/util/log.hh>

#include <novemberizing/concurrency/condition.hh>

namespace novemberizing { namespace ds {

using namespace concurrency;

template <class T, class Concurrency = Sync>
class ConcurrencyList : public Concurrency
{
private:	std::list<T> __o;
public:		typedef typename std::list<T>::iterator iterator;
public:		inline typename ConcurrencyList<T, Concurrency>::iterator begin(void);
public:		inline typename ConcurrencyList<T, Concurrency>::iterator end(void);
public:		inline typename ConcurrencyList<T, Concurrency>::iterator erase(typename ConcurrencyList<T, Concurrency>::iterator it);
public:		inline type::size size(void) const;
public:		inline void front(const T & o);
public:		inline void front(const T && o);
public:		inline void back(const T & o);
public:		inline void back(const T && o);
public:		inline T & front(void);
public:		inline T & back(void);
public:		inline virtual void push(const T & o);
public:		inline virtual void push(const T && o);
public:		inline T & pop(void);
public:		ConcurrencyList(void);
public:		virtual ~ConcurrencyList(void);
};

} }

#include <novemberizing/ds/concurrency.list.inline.hh>

#endif // __NOVEMBERIZING_DS__CONCURRENCY_LIST__HH__
