#include "descriptor.hh"

namespace novemberizing { namespace io {

Descriptor::Map Descriptor::alives;

Descriptor::Descriptor(void)
{
    FUNCTION_START("");

    __descriptor = Invalid;
    __registered = 0x00000000;

    FUNCTION_END("");
}

Descriptor::~Descriptor(void)
{
    FUNCTION_START("");

    FUNCTION_END("");
}

} }
