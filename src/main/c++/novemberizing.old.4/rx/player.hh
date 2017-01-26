#ifndef   __NOVEMBERIZING_RX__PLAYER__HH__
#define   __NOVEMBERIZING_RX__PLAYER__HH__

#include <novemberizing.hh>

#include <novemberizing/util/log.hh>

#include <novemberizing/ds/concurrency.list.hh>

#include <novemberizing/rx/play.hh>

namespace novemberizing { namespace rx {

template <class T>
class Player : public Emittable<T>
{
private:	class Replay : public Play<T>
			{
			public:		class List : public Play<T>
						{
						private:	std::list<T> __items;
						public:		virtual void on(Observer<T> * observer);
						public:		List(std::initializer_list<T> list);
						public:		virtual ~List(void);
						};
			public:		class Error : public Play<T>
						{
						private:	Throwable __e;
						public:		virtual void on(Observer<T> * observer);
						public:		Error(const Throwable & e);
						public:		virtual ~Error(void);
						};
			public:		class Complete : public Play<T>
						{
						public:		virtual void on(Observer<T> * observer);
						public:		Complete(void);
						public:		virtual ~Complete(void);
						};
			private:	T __item;
			public:		virtual void on(Observer<T> * observer);
			public:		Replay(const T & item);
			public:		virtual ~Replay(void);
			};
private:	type::size __limit;
private:	ConcurrencyList<Play<T> *> __replays;
public:		inline virtual void on(Observer<T> * observer);
protected:  inline virtual void emit(const T & o);
protected:	inline virtual void emit(const T && o);
protected:  inline virtual void error(const Throwable & e);
protected:  inline virtual void complete();
public:		inline Player(type::size limit = Infinite);
public:		inline virtual ~Player(void);

};

} }

#include <novemberizing/rx/player.inline.hh>

#endif // __NOVEMBERIZING_RX__PLAYER__HH__
