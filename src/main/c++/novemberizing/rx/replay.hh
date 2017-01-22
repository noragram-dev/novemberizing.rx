#ifndef   __NOVEMBERIZING_RX__REPLAY__HH__
#define   __NOVEMBERIZING_RX__REPLAY__HH__

namespace novemberizing { namespace rx {

template <class T>
class Replay<T>
{
public:		virtual void on(Observer<T> * observer) = 0;
public:		Replay(void);
public:		virtual ~Replay(void);
};

} }

#include <novemberizing/rx/replay.inline.hh>

#endif // __NOVEMBERIZING_RX__REPLAY__HH__
