#ifndef   __NOVEMBERIZING_RX__PLAYER__INLINE__HH__
#define   __NOVEMBERIZING_RX__PLAYER__INLINE__HH__

namespace novemberizing { namespace rx {

template <class T>
Player<T>::__Replay::__Replay(const T & o) : __o(o) {}

template <class T>
Player<T>::__Replay::~Replay(void){}

template <class T>
void Player<T>::__Replay::on(Observer<T> * observer)
{
	observer->onNext(__o);
}

template <class T>
Player<T>::__Error::__Error(const Throwable & e) : __e(e) {}

template <class T>
Player<T>::__Error::~__Error(void){}

template <class T>
void Player<T>::__Error::on(Observer<T> * observer)
{
	observer->onError(e);
}

template <class T>
Player<T>::__Complete::__Complete(void){}

template <class T>
Player<T>::__Complete::~__Complete(void){}

template <class T>
void Player<T>::__Complete::on(Observer<T> * observer)
{
	observer->onComplete();
}

template <class T>
inline void Player<T>::emit(const T & o)
{
	synchronized(&__replays,__replays.push(new __Replay<T>(o)));
}

template <class T>
inline void Player<T>::emit(const T && o)
{
	synchronized(&__replays,__replays.push(new __Replay<T>(o)));
}

template <class T>
inline void Player<T>::error(const Throwable & e)
{
	synchronized(&__replays, __replays.push(new __Error<T>(e)));
}

template <class T>
inline void Player<T>::complete()
{
	synchronized(&__replays, __replays.push(new __Complete<T>()));
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
