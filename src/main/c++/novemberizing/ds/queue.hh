#ifndef   __NOVEMBERIZING_DS__QUEUE__HH__
#define   __NOVEMBERIZING_DS__QUEUE__HH__

namespace novemberizing { namespace ds {

class queue<T>
{
protected:  novemberizing::concurrency::sync * __sync;
protected:  novemberizing::concurrency::condition * __condition;
protected:  std::queue<T> __q;
public:     queue(void);
public:     virtual ~queue(void);
};

} }

#endif // __NOVEMBERIZING_DS__QUEUE__HH__
