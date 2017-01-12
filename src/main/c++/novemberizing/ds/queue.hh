#ifndef   __NOVEMBERIZING_DS__QUEUE__HH__
#define   __NOVEMBERIZING_DS__QUEUE__HH__

namespace novemberizing { namespace ds {

template <class T>
class queue
{
private:    std::queue<T> __q;
private:    Sync * __sync;
private:    Condition * __condition;
public:     queue(void);
public:
public:     virtual ~queue(void);
};

} }

#endif // __NOVEMBERIZING_DS__QUEUE__HH__
