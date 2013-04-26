#include <stdlib.h>
#include <xni.h>

extern "C" {
  #include "houdini/buffer.h"
  #include "houdini/houdini.h"
}

#include "walters.h"

XNI_EXPORT void 
walters_free_cstring(RubyEnv *rb, void *cstr)
{
    free(cstr);
}

XNI_EXPORT const char * 
walters_read_cstring(RubyEnv *rb, void *ptr)
{
    return (const char *) ptr;
}

static inline void* 
cstring(gh_buf* buf, bool copy) 
{
    if (!copy) {
        gh_buf_free(buf);
        return NULL;
    }

    if (gh_buf_oom(buf)) {
        throw xni::runtime_error("out of memory");
    }

    return gh_buf_detach(buf);
}

XNI_EXPORT void *
walters__escape_html(RubyEnv *rb, const char *cstr, unsigned int size, bool secure)
{
    gh_buf gh = GH_BUF_INIT;
    return cstring(&gh, houdini_escape_html0(&gh, (const uint8_t *) cstr, size, secure));
}

XNI_EXPORT void *
walters__unescape_html(RubyEnv *rb, const char *cstr, unsigned int size)
{
    gh_buf gh = GH_BUF_INIT;
    return cstring(&gh, houdini_unescape_html(&gh, (const uint8_t *) cstr, size));
}

XNI_EXPORT void *
walters__escape_xml(RubyEnv *rb, const char *cstr, unsigned int size)
{
    gh_buf gh = GH_BUF_INIT;
    return cstring(&gh, houdini_escape_xml(&gh, (const uint8_t *) cstr, size));
}

XNI_EXPORT void *
walters__escape_uri(RubyEnv *rb, const char *cstr, unsigned int size)
{
    gh_buf gh = GH_BUF_INIT;
    return cstring(&gh, houdini_escape_uri(&gh, (const uint8_t *) cstr, size));
}

XNI_EXPORT void *
walters__escape_url(RubyEnv *rb, const char *cstr, unsigned int size)
{
    gh_buf gh = GH_BUF_INIT;
    return cstring(&gh, houdini_escape_url(&gh, (const uint8_t *) cstr, size));
}

XNI_EXPORT void *
walters__escape_href(RubyEnv *rb, const char *cstr, unsigned int size)
{
    gh_buf gh = GH_BUF_INIT;
    return cstring(&gh, houdini_escape_href(&gh, (const uint8_t *) cstr, size));
}

XNI_EXPORT void *
walters__unescape_uri(RubyEnv *rb, const char *cstr, unsigned int size)
{
    gh_buf gh = GH_BUF_INIT;
    return cstring(&gh, houdini_unescape_uri(&gh, (const uint8_t *) cstr, size));
}

XNI_EXPORT void *
walters__unescape_url(RubyEnv *rb, const char *cstr, unsigned int size)
{
    gh_buf gh = GH_BUF_INIT;
    return cstring(&gh, houdini_unescape_url(&gh, (const uint8_t *) cstr, size));
}

XNI_EXPORT void *
walters__escape_js(RubyEnv *rb, const char *cstr, unsigned int size)
{
    gh_buf gh = GH_BUF_INIT;
    return cstring(&gh, houdini_escape_js(&gh, (const uint8_t *) cstr, size));
}

XNI_EXPORT void *
walters__unescape_js(RubyEnv *rb, const char *cstr, unsigned int size)
{
    gh_buf gh = GH_BUF_INIT;
    return cstring(&gh, houdini_unescape_js(&gh, (const uint8_t *) cstr, size));
}
