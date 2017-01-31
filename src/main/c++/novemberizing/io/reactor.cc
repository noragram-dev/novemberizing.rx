#include "reactor.hh"

namespace novemberizing { namespace io {

Reactor::Reactor(void)
{
    FUNCTION_START("");

    __descriptor = Invalid;
    __reserved = DefaultDescriptorSize;
    __max = Invalid;
    __events = nullptr;
    __timeout = DefaultTimeout;

    FUNCTION_END("");
}

Reactor::~Reactor(void)
{
    FUNCTION_START("");

    ::close(__descriptor);
    if(__events!=nullptr)
    {
        ::free(__events);
        __events = nullptr;
    }

    FUNCTION_END("");
}

void Reactor::onecycle(void)
{
    initialize();

    if(__descriptor>=0)
    {
        int nfds = ::epoll_wait(__descriptor, __events, __max, __timeout);
        for(int i = 0;i<nfds;i++)
        {
            Descriptor * descriptor = Descriptor::alives.get(__events[i].data.fd);
            if(descriptor!=nullptr)
            {
                if(__events[i].events & (EPOLLPRI | EPOLLHUP | EPOLLRDHUP | EPOLLERR))
                {
                    del(descriptor);
                    descriptor->close();
                    continue;
                }
                if(__events[i].events & EPOLLIN)
                {
                    descriptor->read();
                }
                if(__events[i].events & EPOLLOUT)
                {
                    descriptor->write();
                }
                if(descriptor->should(Close))
                {
                    del(descriptor);
                    descriptor->close();
                }
                else
                {

                }
            }
            else
            {
                struct epoll_event ev;
                ev.data.fd = __events[i].data.fd;
                ev.events = (EPOLLIN | EPOLLOUT | EPOLLHUP | EPOLLRDHUP | EPOLLERR | EPOLLPRI);
                if(::epoll_ctl(__descriptor, EPOLL_CTL_DEL, __events[i].data.fd, &ev)!=Success)
                {
                    WARNING_LOG("fail to ::epoll_ctl(...) caused by %d",errno);
                }
            }
        }
    }
}

void Reactor::initialize(void)
{
    FUNCTION_START("");

    if(__timeout<=0){ __timeout = DefaultTimeout; }

    if(__max!=__reserved)
    {
        __max = __reserved;
        if(__max<=0){ __max = DefaultDescriptorSize; }
        if(__events!=nullptr)
        {
            ::free(__events);
        }
        __events = (struct epoll_event *) ::malloc(__max);
    }

    if(__descriptor==Invalid)
    {
        __descriptor = ::epoll_create(__max);
        std::list<Descriptor * > removes;
        synchronized(&__descriptors, foreach(__descriptors.begin(),__descriptors.end(),{
            Descriptor * descriptor = *it;
            if(descriptor!=nullptr)
            {
                if(descriptor->alive())
                {
                    struct epoll_event ev;
                    ev.data.fd = descriptor->v();
                    ev.events = (EPOLLPRI | EPOLLERR | EPOLLHUP | EPOLLRDHUP);
                    if(descriptor->should(Read))
                    {
                        ev.events |= EPOLLIN;
                    }
                    if(descriptor->should(Write))
                    {
                        ev.events |= EPOLLOUT;
                    }
                    if(epoll_ctl(__descriptor, EPOLL_CTL_ADD, descriptor->v(), &ev)!=Success)
                    {
                        if(epoll_ctl(__descriptor, EPOLL_CTL_MOD, descriptor->v(), &ev)!=Success)
                        {
                            removes.push_back(descriptor);
                        }
                    }
                }
                else
                {
                    removes.push_back(descriptor);
                }
            }
        }));
        foreach(removes.begin(),removes.end(),{
            Descriptor * descriptor = *it;
            if(descriptor!=nullptr)
            {
                descriptor->close();

            }
            synchronized(&__descriptors, __descriptors.del(descriptor));
        });
    }

    FUNCTION_END("");
}

// private:    ConcurrentSet<Descriptor *> __descriptors;
// private:    int __descriptor;
// private:    int __max;
// private:    int __reserved;
// private:    struct epoll_event * __events;
// public:     virtual int add(Descriptor * descriptor);
// public:     virtual int del(Descriptor * descriptor);
// public:     virtual int mod(Descriptor * descriptor);
// public:     virtual void onecycle(void);
// public:     Reactor(void);
// public:     virtual ~Reactor(void);

    // public:     virtual void onecycle(void);
    // public:     Reactor(void);
    // public:     virtual ~Reactor(void);

} }
