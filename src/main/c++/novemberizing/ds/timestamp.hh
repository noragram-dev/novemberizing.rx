#ifndef   __NOVEMBERIZING_DS__TIMESTAMP__HH__
#define   __NOVEMBERIZING_DS__TIMESTAMP__HH__

namespace novemberizing { namespace ds {

class timestamp
{
private:    type::second __second;
private:    type::nano __nano;
public:     inline type::second second(void) const { return __second; }
public:     inline type::nano nano(void) const { return __nano; }

public:     timestamp(void);
public:     virtual ~timestamp(void);
};

} }

#endif // __NOVEMBERIZING_DS__TIMESTAMP__HH__
