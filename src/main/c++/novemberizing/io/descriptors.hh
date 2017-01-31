#ifndef   __NOVEMBERIZING_IO__DESCRIPTORS__HH__
#define   __NOVEMBERIZING_IO__DESCRIPTORS__HH__

#include <vector>
#include <functional>

#include <novemberizing/concurrency/sync.hh>

namespace novemberizing { namespace io {

using namespace concurrency;

class Descriptor;

class Descriptors : public Sync
{
private:    std::vector<Descriptor *> __map;
public:     Descriptor * get(int v);
public:     void set(Descriptor * descriptor,std::function<void(Descriptor *)> f = nullptr);
public:     void del(int v,std::function<void(Descriptor *)> f = nullptr);
public:     void del(Descriptor * descriptor);
public:     Descriptors(type::size reserved = 1024);
public:     virtual ~Descriptors(void);
};

} }

#endif // __NOVEMBERIZING_IO__DESCRIPTORS__HH__
