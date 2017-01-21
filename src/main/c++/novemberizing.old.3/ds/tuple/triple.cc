#include "triple.hh"

#ifdef    __NOVEMBERIZING_DS_TUPLE__TRIPLE__EXAMPLE__

#include <iostream>

using namespace novemberizing;

int main(int argc, char ** argv) {

    ds::tuple::Triple<int, int, int> triple(1, 2, 3);

    std::cout << triple.first << ":" << triple.second << ":" << triple.third <<std::endl;

    return 0;
}

#endif // __NOVEMBERIZING_DS_TUPLE__TRIPLE__EXAMPLE__
