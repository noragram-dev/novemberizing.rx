#ifndef   __NOVEMBERIZING_RX__SUBSCRIBERS__INLINE__HH__
#define   __NOVEMBERIZING_RX__SUBSCRIBERS__INLINE__HH__

namespace novemberizing { namespace rx {

Subscribers::Subscribers(void)
{
    FUNCTION_START("");
    FUNCTION_END("");
}

Subscribers::~Subscribers(void)
{
    FUNCTION_START("");
    FUNCTION_END("");
}

template <class T>
Subscribers::Just<T>::Just(void)
{
    FUNCTION_START("");
    FUNCTION_END("");
}

template <class T>
Subscribers::Just<T>::~Just(void)
{
    FUNCTION_START("");
    FUNCTION_END("");
}

template <class T>
void Subscribers::Just<T>::onNext(const T & o)
{
    FUNCTION_START("");
    FUNCTION_END("");
}

template <class T>
void Subscribers::Just<T>::onError(const Throwable & o)
{
    FUNCTION_START("");
    FUNCTION_END("");
}

template <class T>
void Subscribers::Just<T>::onComplete(void)
{
    FUNCTION_START("");
    FUNCTION_END("");
}

} }

#endif // __NOVEMBERIZING_RX__SUBSCRIBERS__INLINE__HH__
