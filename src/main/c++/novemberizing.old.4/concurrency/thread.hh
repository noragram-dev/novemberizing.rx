#ifndef   __NOVEMBERIZING_CONCURRENCY__THREAD__HH__
#define   __NOVEMBERIZING_CONCURRENCY__THREAD__HH__

namespace novemberizing { namespace concurrency {

class Thread : public Runnable
{
private:    pthread_t * __thread;
private:    pthread_attr_t * __threadattr;
public:     virtual void run(void);
public:     Thread(void);
public:     virtual ~Thread(void);
};

} }

#endif // __NOVEMBERIZING_CONCURRENCY__THREAD__HH__
