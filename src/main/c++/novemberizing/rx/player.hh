#ifndef   __NOVEMBERIZING_RX__PLAYER__HH__
#define   __NOVEMBERIZING_RX__PLAYER__HH__

namespace novemberizing { namespace rx {

template <class T>
class Player : public Emittable<T>
{
private:	class __Replay : public Replay<T>
			{
			private:	T __o;
			public:		virtual void on(Observer<T> * observer);
			public:		__Replay(const T & o);
			public:		virtual ~__Replay(void);
			};
private:	class __Error : public Replay<T>
			{
			private:	Throwable __e;
			public:		virtual void on(Observer<T> * observer);
			public:		__Error(const Throwable & e);
			public:		virtual ~__Error(void);
			};
private:	class __Complete : public Replay<T>
			{
			public:		virtual void on(Observer<T> * observer);
			public:		__Complete(void);
			public:		virtual ~__Complete(void);
			};
private:	type::size __limit;
private:	ConcurrencyList<Replay<T> *> __replays;
public:		inline virtual void on(Observer<T> * observer);
protected:  inline virtual void emit(const T & o);
protected:  inline virtual void error(const Throwable & e);
protected:  inline virtual void complete();
public:		Player(type::size limit = Infinite);
public:		virtual ~Player(void);
};

} }

#endif // __NOVEMBERIZING_RX__PLAYER__HH__
