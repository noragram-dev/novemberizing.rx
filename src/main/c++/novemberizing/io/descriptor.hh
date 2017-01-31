#ifndef   __NOVEMBERIZING_IO__DESCRIPTOR__HH__
#define   __NOVEMBERIZING_IO__DESCRIPTOR__HH__

namespace novemberizing { namespace io {

class Descriptor
{
protected:  int __v;
public:     inline int v(void) const;
public:     Descriptor(void);
public:     virtual ~Descriptor(void);
};

} }

#endif // __NOVEMBERIZING_IO__DESCRIPTOR__HH__
