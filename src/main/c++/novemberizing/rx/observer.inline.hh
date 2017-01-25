#ifndef   __NOVEMBERIZING_RX__OBSERVER__INLINE__HH__
#define   __NOVEMBERIZING_RX__OBSERVER__INLINE__HH__

namespace novemberizing { namespace rx {

template <class T>
inline Observer<T>::Observer(void)
{
    FUNCTION_START("");
    FUNCTION_END("");
}

template <class T>
inline Observer<T>::~Observer(void)
{
    FUNCTION_START("");
    FUNCTION_END("");
}

template <class T>
inline void Observer<T>::operator delete(void * p)
{
    FUNCTION_START("");
    INFORMATION_LOG("");
    Observer<T> * observer = static_cast<Observer<T> *>(p);
    if(observer!=nullptr)
    {
        ::operator delete(observer);
    }
    else
    {
        CAUTION_LOG("static_cast<Observer<T> *>(p)==nullptr");
    }
    FUNCTION_END("");
}

} }

#endif // __NOVEMBERIZING_RX__OBSERVER__INLINE__HH__
