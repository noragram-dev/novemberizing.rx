#ifndef   __NOVEMBERIZING_DS_TUPLE__TRIPLE__INLINE__HH__
#define   __NOVEMBERIZING_DS_TUPLE__TRIPLE__INLINE__HH__

namespace novemberizing { namespace ds { namespace tuple {

template <class A, class B, class C>
Triple<A, B, C>::Triple(const A & first, const B & second, const C & third) : Pair<A, B>(first, second), third(third) {}

} } }

#endif // __NOVEMBERIZING_DS_TUPLE__TRIPLE__INLINE__HH__
