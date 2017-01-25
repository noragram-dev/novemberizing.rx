y7#include <novemberizing/rx/observable.hh>
#include <novemberizing/rx/subscribers.hh>

using namespace novemberizing::rx;
using namespace novemberizing::util;

int main(void)
{
    Log::o.disable(Log::Flow | Log::Debug);

    Observable<int> observable;

    /**
     * HOW TO AUTO FREE ...
     * DELETE OPERATOR REDEFINE ....
     */
    observable.subscribe(Subscribers::Just<int>());
    /**
     *
     */
    Observer<int> * observer = Subscribers::Just<int>();

    observable.subscribe(observer);



    observable.emit(1);     /** WILL BE PROTECTED */

    delete observer;

    return 0;
}
