#include "main.hh"

namespace novemberizing { namespace rx { namespace schedulers {

Main * Main::__singleton = nullptr;

/**
 * @todo        thread safety
 */
Main * Main::Get(void)
{
    if(__singleton==nullptr){
        __singleton = new Main();
    }
    return __singleton;
}

Main::Main(void)
{
    FUNCTION_START("");

    on();

    FUNCTION_END("");
}

Main::~Main(void)
{
    FUNCTION_START("");
    type::size remain = 0;
    do {
        __q.lock();
        while(__q.size()>0){
            __q.pop([this](Executable * executable){
                if(executable!=nullptr)
                {
                    __q.unlock();
                    synchronized(&__executables, __executables.add(executable));
                    executable->execute(this);
                    __q.lock();
                }
                else
                {
                    NOTICE_LOG("executable==nullptr");
                }
            });
        }
        synchronized(&__executables, remain = __executables.size());
        __q.unlock();
    } while(remain>0);
    FUNCTION_END("");
}

int Main::run(void)
{
    lock();
    while(!cancel()){
        unlock();
        __q.lock();
        type::size current = __q.size();
        for(type::size i = 0;i<current && __q.size()>0;i++)
        {
            __q.pop([this](Executable * executable){
                if(executable!=nullptr)
                {
                    __q.unlock();
                    synchronized(&__executables, __executables.add(executable));
                    executable->execute(this);
                    __q.lock();
                }
                else
                {
                    NOTICE_LOG("executable==nullptr");
                }
            });
        }
        __q.unlock();
        for(ConcurrentList<Cyclable *>::iterator it = __cyclables.begin();it!=__cyclables.end();it++)
        {
            Cyclable * cyclable = *it;
            cyclable->onecycle();
        }
        lock();
    }
    unlock();
    return Success;
}

void Main::dispatch(Executable * executable)
{
    FUNCTION_START("");
    synchronized(&__q,{
        if(executable!=nullptr){ __q.push(executable); }
        __q.resume();
    });
    FUNCTION_END("");
}

void Main::dispatch(std::initializer_list<Executable *> executables)
{
    FUNCTION_START("");
    synchronized(&__q,{
        for(std::initializer_list<Executable *>::iterator it = executables.begin();executables.end();it++)
        {
            if(*it!=nullptr){ __q.push(*it); }
        }
        __q.resume(true);
    });
    FUNCTION_END("");
}

void Main::completed(Executable * executable)
{
    FUNCTION_START("");
    if(executable!=nullptr)
    {
        synchronized(&__executables, __executables.del(executable));
    }
    FUNCTION_END("");
}

void Main::executed(Executable * executable)
{
    FUNCTION_START("");
    if(executable!=nullptr)
    {
        synchronized(&__executables, __executables.del(executable));
        dispatch(executable);
    }
    FUNCTION_END("");
}

} } }
