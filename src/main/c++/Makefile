.SUFFIXES: .cc .hh

CXX=g++
CXXFLAGS=-Wall -std=c++0x -D_REENTRANT $(INCS) -g -ggdb
AR=ar
ARFLAGS=rcv
RANLIB=ranlib

INCS=-I.
LIBS=-L. -lnovemberzing.rx

TARGET=libnovemberizing.rx.a

SRCS=$(OBJS:.o=.cc)
OBJS=   ./novemberizing.o                                                               \
        ./novemberizing/sync.o                                                          \
        ./novemberizing/condition.o                                                     \

$(TARGET): $(OBJS)
	$(AR) $(ARFLAGS) $@ $(OBJS)

all: clean $(TARGET)

clean:
	rm -f $(TARGET)
	rm -f $(OBJS)
