/*
 * Copyright (C) 2013 Wayne Meissner
 *
 * This file is part of the Walters project (http://github.com/wmeissner/walters).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#include <stdlib.h>
#include <xni.h>

extern "C" {
  #include "houdini/buffer.h"
  #include "houdini/houdini.h"
}

#include "walters_xni.h"

struct Walters_Buffer {
    gh_buf gh;
};


XNI_EXPORT int 
walters_sizeof_buffer(void)
{
    return sizeof(struct Walters_Buffer);
}

XNI_EXPORT void 
walters_buffer_initialize(RubyEnv *rb, struct Walters_Buffer *buf, unsigned int size)
{
    gh_buf_init(&buf->gh, (size_t) size);
}

XNI_EXPORT void 
walters_buffer_finalize(RubyEnv *rb, struct Walters_Buffer *buf)
{
    gh_buf_free(&buf->gh);
}

XNI_EXPORT const char * 
walters_buffer_cstring(RubyEnv *rb, struct Walters_Buffer *buf)
{
    return gh_buf_cstr(&buf->gh);
}

static inline bool check_oom(struct Walters_Buffer *buf, int retval)
{
    if (gh_buf_oom(&buf->gh)) {
        throw xni::runtime_error("out of memory");
    }

    return retval != 0;
}

XNI_EXPORT bool 
walters_buffer_escape_html(RubyEnv *rb, struct Walters_Buffer *buf, const char *cstr, unsigned int size)
{
    return check_oom(buf, houdini_escape_html(&buf->gh, (const uint8_t *) cstr, size));
}

XNI_EXPORT bool 
walters_buffer_escape_html0(RubyEnv *rb, struct Walters_Buffer *buf, const char *cstr, unsigned int size, bool secure)
{
    return check_oom(buf, houdini_escape_html0(&buf->gh, (const uint8_t *) cstr, size, secure));
}

XNI_EXPORT bool 
walters_buffer_unescape_html(RubyEnv *rb, struct Walters_Buffer *buf, const char *cstr, unsigned int size)
{
    return check_oom(buf, houdini_unescape_html(&buf->gh, (const uint8_t *) cstr, size));
}

XNI_EXPORT bool 
walters_buffer_escape_xml(RubyEnv *rb, struct Walters_Buffer *buf, const char *cstr, unsigned int size)
{
    return check_oom(buf, houdini_escape_xml(&buf->gh, (const uint8_t *) cstr, size));
}

XNI_EXPORT bool 
walters_buffer_escape_uri(RubyEnv *rb, struct Walters_Buffer *buf, const char *cstr, unsigned int size)
{
    return check_oom(buf, houdini_escape_uri(&buf->gh, (const uint8_t *) cstr, size));
}

XNI_EXPORT bool 
walters_buffer_escape_url(RubyEnv *rb, struct Walters_Buffer *buf, const char *cstr, unsigned int size)
{
    return check_oom(buf, houdini_escape_url(&buf->gh, (const uint8_t *) cstr, size));
}

XNI_EXPORT bool 
walters_buffer_escape_href(RubyEnv *rb, struct Walters_Buffer *buf, const char *cstr, unsigned int size)
{
    return check_oom(buf, houdini_escape_href(&buf->gh, (const uint8_t *) cstr, size));
}

XNI_EXPORT bool 
walters_buffer_unescape_uri(RubyEnv *rb, struct Walters_Buffer *buf, const char *cstr, unsigned int size)
{
    return check_oom(buf, houdini_unescape_uri(&buf->gh, (const uint8_t *) cstr, size));
}

XNI_EXPORT bool 
walters_buffer_unescape_url(RubyEnv *rb, struct Walters_Buffer *buf, const char *cstr, unsigned int size)
{
    return check_oom(buf, houdini_unescape_url(&buf->gh, (const uint8_t *) cstr, size));
}

XNI_EXPORT bool 
walters_buffer_escape_js(RubyEnv *rb, struct Walters_Buffer *buf, const char *cstr, unsigned int size)
{
    return check_oom(buf, houdini_escape_js(&buf->gh, (const uint8_t *) cstr, size));
}

XNI_EXPORT bool 
walters_buffer_unescape_js(RubyEnv *rb, struct Walters_Buffer *buf, const char *cstr, unsigned int size)
{
    return check_oom(buf, houdini_unescape_js(&buf->gh, (const uint8_t *) cstr, size));
}
