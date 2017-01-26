.SUFFIXES: .cc .hh

CXX=g++
CXXFLAGS=-Wall -std=c++0x -D_REENTRANT $(INCS) -g -ggdb
AR=ar
ARFLAGS=rcv
RANLIB=ranlib

INCS=-I. -I..
LIBS=-L.. -lnovemberizing.rx -lpthread

TARGET=noragram-cli

SRCS=$(OBJS:.o=.cc)
OBJS=  	cli.o																	\

$(TARGET): $(OBJS)
	$(CXX) $(CXXFLAGS) -o $@ $(OBJS) $(LIBS)

all: clean $(TARGET)

clean:
	rm -f $(TARGET)
	rm -f $(OBJS)
