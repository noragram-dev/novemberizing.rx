#include <novemberizing/rx/observable.hh>
#include <novemberizing/rx/subscribers.hh>

using namespace novemberizing::rx;
using namespace novemberizing::util;

int main(void)
{
    Log::o.disable(Log::Flow | Log::Debug);



    /**
     *
     */
    Observable<int> observable;

    observable.subscribe(Subscribers::Just<int>());

    observable.emit(1);


    return 0;
}
