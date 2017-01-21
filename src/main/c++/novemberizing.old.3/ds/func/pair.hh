#ifndef   __NOVEMBERIZING_DS_FUNC__PAIR__HH__
#define   __NOVEMBERIZING_DS_FUNC__PAIR__HH__

namespace novemberizing { namespace ds { namespace func {

template <class A, class B, class Z>
class Pair
{
public:		virtual Z operator()(const & A first, const & B second) = 0;
public:		Pair(void){}
public:		virtual ~Pair(void){}
};

} } }

#endif // __NOVEMBERIZING_DS_FUNC__PAIR__HH__
