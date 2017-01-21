#ifndef   __NOVEMBERIZING_DS_TUPLE__PAIR__INLINE__HH__
#define   __NOVEMBERIZING_DS_TUPLE__PAIR__INLINE__HH__

namespace novemberizing { namespace ds { namespace tuple {

template <class A, class B>
Pair<A, B>::Pair(const A & first,const B & second) : Single<A>(first), second(second){}

} } }

#endif // __NOVEMBERIZING_DS_TUPLE__PAIR__INLINE__HH__
