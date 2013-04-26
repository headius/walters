#include <stdlib.h>
#include <xni.h>

extern "C" {
  #include "houdini/buffer.h"
  #include "houdini/houdini.h"
}

#include "walters_xni.h"

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
houdini(int (*fn)(gh_buf*, const uint8_t *, size_t size), const char* cstr, unsigned int size)
{
    gh_buf gh = GH_BUF_INIT;
    
    if (!(*fn)(&gh, (const uint8_t *) cstr, size)) {
        gh_buf_free(&gh);
        return NULL;
    }

    if (gh_buf_oom(&gh)) {
        throw xni::runtime_error("out of memory");
    }

    return gh_buf_detach(&gh);
}

XNI_EXPORT void *
walters__escape_html(RubyEnv *rb, const char *cstr, unsigned int size, bool secure)
{
    gh_buf gh = GH_BUF_INIT;
    
    if (houdini_escape_html0(&gh, (const uint8_t *) cstr, size, secure)) {
        return gh_buf_detach(&gh);
    }

    gh_buf_free(&gh);

    return NULL;
}

XNI_EXPORT void *
walters__unescape_html(RubyEnv *rb, const char *cstr, unsigned int size)
{
    return houdini(houdini_unescape_html, cstr, size);
}

XNI_EXPORT void *
walters__escape_xml(RubyEnv *rb, const char *cstr, unsigned int size)
{
    return houdini(houdini_escape_xml, cstr, size);
}

XNI_EXPORT void *
walters__escape_uri(RubyEnv *rb, const char *cstr, unsigned int size)
{
    return houdini(houdini_escape_uri, cstr, size);
}

XNI_EXPORT void *
walters__escape_url(RubyEnv *rb, const char *cstr, unsigned int size)
{
    return houdini(houdini_escape_url, cstr, size);
}

XNI_EXPORT void *
walters__escape_href(RubyEnv *rb, const char *cstr, unsigned int size)
{
    return houdini(houdini_escape_href, cstr, size);
}

XNI_EXPORT void *
walters__unescape_uri(RubyEnv *rb, const char *cstr, unsigned int size)
{
    return houdini(houdini_unescape_uri, cstr, size);
}

XNI_EXPORT void *
walters__unescape_url(RubyEnv *rb, const char *cstr, unsigned int size)
{
    return houdini(houdini_unescape_url, cstr, size);
}

XNI_EXPORT void *
walters__escape_js(RubyEnv *rb, const char *cstr, unsigned int size)
{
    return houdini(houdini_escape_js, cstr, size);
}

XNI_EXPORT void *
walters__unescape_js(RubyEnv *rb, const char *cstr, unsigned int size)
{
    return houdini(houdini_unescape_js, cstr, size);
}
