#ifndef   __NOVEMBERIZING_DS__CONCURRENCY_SET__HH__
#define   __NOVEMBERIZING_DS__CONCURRENCY_SET__HH__

namespace novemberizing { namespace ds {

template <class T, class Concurrency = Sync>
class ConcurrencySet : public Concurrency
{
private:	std::set<T> __o;
public:		typedef typename std::set<T>::iterator iterator;
public:		typename ConcurrencySet<T, Concurrency>::iterator begin(void);
public:		typename ConcurrencySet<T, Concurrency>::iterator end(void);
public:		typename ConcurrencySet<T, Concurrency>::iterator erase(typename ConcurrencySet<T, Concurrency>::iterator it);
public:		inline bool add(const T & o);
public:		inline bool add(const T && o);
public:		inline bool del(const T & o);
public:		inline bool del(const T && o);
public:		inline bool exist(const T & o) const;
public:		inline bool exist(const T && o) const;
public:		inline type::size size(void) const;
public:		ConcurrencySet(void);
public:		virtual ~ConcurrencySet(void);
};

} }

#endif // __NOVEMBERIZING_DS__CONCURRENCY_SET__HH__
