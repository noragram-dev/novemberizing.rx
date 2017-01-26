#include <novemberizing/rx/scheduler.hh>

using namespace novemberizing::rx;
using namespace novemberizing::util;

int main(void)
{
    Log::o.disable(Log::Flow | Log::Debug);
    Scheduler * scheduler = Scheduler::Main();

    Observable<int> * observable = Observable<int>.just(1);
    delete observable;

    return scheduler->run();
}
