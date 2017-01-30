#include <novemberizing/rx/observable.hh>

#include <string>

using namespace novemberizing::rx;

int main(void)
{
    Observable<std::string> * observable = Observable<std::string>::Just({"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"});
    return 0;
}
