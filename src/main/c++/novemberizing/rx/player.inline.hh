#ifndef   __NOVEMBERIZING_RX__PLAYER__INLINE__HH__
#define   __NOVEMBERIZING_RX__PLAYER__INLINE__HH__

namespace novemberizing { namespace rx {

template <class T>
Player<T>::Replay::Replay(const T & o) : __o(o) {}

template <class T>
Player<T>::Replay::~Replay(void){}

template <class T>
void Player<T>::Replay::on(Observer<T> * observer)
{
	observer->onNext(__o);
}

template <class T>
Player<T>::Replay::List::List(std::initializer_list<T> items) : __items(items) {}

template <class T>
Player<T>::Replay::List::~List(void){}

template <class T>
void Player<T>::Replay::List::on(Observer<T> * observer)
{
	for(std::list<T>::iterator it = __items.begin();it!=__items.end();it++)
	{
		observer->onNext(*it);
	}
}

template <class T>
Player<T>::Replay::Error::Error(const Throwable & e) : __e(e) {}

template <class T>
Player<T>::Replay::Error::~Error(void){}

template <class T>
void Player<T>::Replay::Error::on(Observer<T> * observer)
{
	observer->onError(e);
}

template <class T>
Player<T>::Replay::Complete::Complete(void){}

template <class T>
Player<T>::Replay::Complete::~Complete(void){}

template <class T>
void Player<T>::Replay::Complete::on(Observer<T> * observer)
{
	observer->onComplete();
}

template <class T>
inline void Player<T>::emit(const T & o)
{
	synchronized(&__replays,__replays.push(new Replay<T>(o)));
}

template <class T>
inline void Player<T>::emit(const T && o)
{
	synchronized(&__replays,__replays.push(new Replay<T>(o)));
}

template <class T>
inline void Player<T>::error(const Throwable & e)
{
	synchronized(&__replays, __replays.push(new Replay::Error<T>(e)));
}

template <class T>
inline void Player<T>::complete()
{
	synchronized(&__replays, __replays.push(new Replay::Complete<T>()));
}

template <class T>
Player<T>::Player(type::size limit) : __limit(limit) {}

template <class T>
Player<T>::~Player(void)
{
	for(ConcurrencyList<Replay<T> *>::iterator it = __replays.begin();it!=__replays.end();it = __replays.erase(it))
	{
		Replay<T> * replay = *it;
		if(replay) { delete replay; }
	}
}

} }

#endif // __NOVEMBERIZING_RX__PLAYER__INLINE__HH__
