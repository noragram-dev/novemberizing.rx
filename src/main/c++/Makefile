.SUFFIXES: .cc .hh

CXX=g++
CXXFLAGS=-Wall -std=c++17 -D_REENTRANT $(INCS) -g -ggdb
AR=ar
ARFLAGS=rcv
RANLIB=ranlib

INCS=-I.
LIBS=-L. -lnovemberzing.rx

TARGET=libnovemberizing.rx.a

SRCS=$(OBJS:.o=.cc)
OBJS=   ./novemberizing.o                                                       \
		./novemberizing/util/log.o												\
		./novemberizing/util/logger.o											\
		./novemberizing/ds/cancellable.o										\
		./novemberizing/ds/runnable.o											\
		./novemberizing/ds/throwable.o											\
		./novemberizing/concurrency/sync.o										\
		./novemberizing/concurrency/condition.o									\
		./novemberizing/concurrency/thread.o									\
		./novemberizing/ds/concurrent.list.o									\
		./novemberizing/ds/concurrent.set.o										\
		./novemberizing/ds/concurrent.map.o										\
		./novemberizing/ds/concurrent.linked.set.o								\
		./novemberizing/ds/executor.o											\
		./novemberizing/ds/executable.o											\
		./novemberizing/rx/scheduler.o											\
		./novemberizing/rx/schedulers/main.o									\
		./novemberizing/rx/schedulers/local.o									\
		./novemberizing/rx/emittable.o											\
		./novemberizing/rx/subscribable.o										\
		./novemberizing/rx/observable.o											\
		./novemberizing/rx/observer.o											\
		./novemberizing/rx/play.o												\
		./novemberizing/rx/replayer.o											\

$(TARGET): $(OBJS)
	$(AR) $(ARFLAGS) $@ $(OBJS)

all: clean $(TARGET)

clean:
	rm -f $(TARGET)
	rm -f $(OBJS)
