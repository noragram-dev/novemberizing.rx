#ifndef   __NOVEMBERIZING_DS_FUNC__TRIPLE__HH__
#define   __NOVEMBERIZING_DS_FUNC__TRIPLE__HH__

namespace novemberizing { namespace ds { namespace func {

template <class A, class B, class C, class Z>
class Triple
{
public:		virtual Z operator()(const & A first, const & B second, const & C third) = 0;
public:		Triple(void){}
public:		virtual ~Triple(void){}
};

} } }

#endif // __NOVEMBERIZING_DS_FUNC__TRIPLE__HH__
