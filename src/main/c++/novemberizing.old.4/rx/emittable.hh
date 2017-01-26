#ifndef   __NOVEMBERIZING_RX__EMITTABLE__HH__
#define   __NOVEMBERIZING_RX__EMITTABLE__HH__

#include <novemberizing.hh>

#include <novemberizing/util/log.hh>

#include <novemberizing/ds/throwable.hh>

#include <novemberizing/concurrency/sync.hh>

namespace novemberizing { namespace rx {

using namespace concurrency;
using namespace ds;

template <class T>
class Emittable : public Sync
{
protected:  inline virtual void emit(const T & o) = 0;
protected:	inline virtual void emit(const T && o) = 0;
protected:  inline virtual void error(const Throwable & e) = 0;
protected:  inline virtual void complete() = 0;
public:		Emittable(void);
public:		virtual ~Emittable(void);
};

} }

#include <novemberizing/rx/emittable.inline.hh>

#endif // __NOVEMBERIZING_RX__EMITTABLE__HH__
