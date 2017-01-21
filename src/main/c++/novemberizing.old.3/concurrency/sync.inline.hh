#ifndef   __NOVEMBERIZING_CONCURRENCY__SYNC__INLINE__HH__
#define   __NOVEMBERIZING_CONCURRENCY__SYNC__INLINE__HH__

#define synchronized(sync, statement){			\
    void * o = sync;                            \
	o->lock();								    \
		statement;								\
	o->unlock();								\
}

#endif // __NOVEMBERIZING_CONCURRENCY__SYNC__INLINE__HH__
