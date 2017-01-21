#include "single.hh"

#ifdef    __NOVEMBERIZING_DS_TUPLE__SINGLE__EXAMPLE__

#include <iostream>

using namespace novemberizing;

int main(int argc, char ** argv) {

    ds::tuple::Single<int> single(1);

    std::cout << single.first << std::endl;

    return 0;
}

#endif // __NOVEMBERIZING_DS_TUPLE__SINGLE__EXAMPLE__
