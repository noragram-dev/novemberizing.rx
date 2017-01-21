#include "pair.hh"


#ifdef    __NOVEMBERIZING_DS_TUPLE__PAIR__EXAMPLE__

#include <iostream>

using namespace novemberizing;

int main(int argc, char ** argv) {

    ds::tuple::Pair<int, int> pair(1, 2);

    std::cout << pair.first << ":" << pair.second << std::endl;

    return 0;
}

#endif // __NOVEMBERIZING_DS_TUPLE__PAIR__EXAMPLE__
