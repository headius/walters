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

package walters;

import org.jcodings.Encoding;
import org.jcodings.specific.ASCIIEncoding;
import org.jcodings.specific.USASCIIEncoding;
import org.jcodings.specific.UTF8Encoding;
import org.jruby.RubyString;
import org.jruby.anno.JRubyMethod;
import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.util.ByteList;

public final class Walters {

    private static IRubyObject transform(ThreadContext context, IRubyObject cstr, TextTransformer transformer) {
        RubyString str = cstr.convertToString();
        Encoding encoding = str.getEncoding();
        if (!UTF8Encoding.INSTANCE.equals(encoding) && !USASCIIEncoding.INSTANCE.equals(encoding) && !ASCIIEncoding.INSTANCE.equals(encoding)) {
            throw context.getRuntime().newEncodingCompatibilityError("expected UTF-8 or ASCII string");
        }
        
        ByteList src = str.getByteList();
        ByteList ob = transformer.transform(src.getUnsafeBytes(), src.begin(), src.length());
        if (ob == null) {
            return cstr;
        }
        RubyString result = RubyString.newStringNoCopy(context.getRuntime(), ob);
        result.setEncoding(str.getEncoding());
        
        return result;
    }


    @JRubyMethod(meta = true)
    public static IRubyObject escape_html(ThreadContext context, IRubyObject recv, IRubyObject cstr, IRubyObject secure) {
        return transform(context, cstr, secure.isTrue() ? EscapeHtml.SECURE : EscapeHtml.INSECURE);
    }

    @JRubyMethod(meta = true)
    public static IRubyObject escape_html(ThreadContext context, IRubyObject recv, IRubyObject cstr) {
        return transform(context, cstr, EscapeHtml.SECURE);
    }
    
    @JRubyMethod(meta = true)
    public static IRubyObject unescape_html(ThreadContext context, IRubyObject recv, IRubyObject cstr) {
        return transform(context, cstr, UnescapeHtml.INSTANCE);
    }
    
    @JRubyMethod(meta = true)
    public static IRubyObject escape_href(ThreadContext context, IRubyObject recv, IRubyObject cstr) {
        return transform(context, cstr, EscapeHref.INSTANCE);
    }

    @JRubyMethod(name = { "escape_js", "escape_javascript" }, meta = true)
    public static IRubyObject escape_js(ThreadContext context, IRubyObject recv, IRubyObject cstr) {
        return transform(context, cstr, EscapeJavaScript.INSTANCE);
    }

    @JRubyMethod(name = { "unescape_js", "unescape_javascript" }, meta = true)
    public static IRubyObject unescape_js(ThreadContext context, IRubyObject recv, IRubyObject cstr) {
        return transform(context, cstr, UnescapeJavaScript.INSTANCE);
    }

    @JRubyMethod(meta = true)
    public static IRubyObject escape_uri(ThreadContext context, IRubyObject recv, IRubyObject cstr) {
        return transform(context, cstr, EscapeURI.URI_ESCAPE);
    }
    
    @JRubyMethod(meta = true)
    public static IRubyObject escape_url(ThreadContext context, IRubyObject recv, IRubyObject cstr) {
        return transform(context, cstr, EscapeURI.URL_ESCAPE);
    }

    @JRubyMethod(meta = true)
    public static IRubyObject unescape_uri(ThreadContext context, IRubyObject recv, IRubyObject cstr) {
        return transform(context, cstr, UnescapeURI.URI_UNESCAPE);
    }

    @JRubyMethod(meta = true)
    public static IRubyObject unescape_url(ThreadContext context, IRubyObject recv, IRubyObject cstr) {
        return transform(context, cstr, UnescapeURI.URL_UNESCAPE);
    }

    @JRubyMethod(meta = true)
    public static IRubyObject escape_xml(ThreadContext context, IRubyObject recv, IRubyObject cstr) {
        return transform(context, cstr, EscapeXML.INSTANCE);
    }
    
    public static final class HtmlEscape {
        @JRubyMethod
        public static IRubyObject walters_escape_html(ThreadContext context, IRubyObject recv, IRubyObject cstr) {
            return transform(context, cstr, EscapeHtml.SECURE);
        }
    }

    public static final class HtmlUnescape {
        @JRubyMethod
        public static IRubyObject walters_unescape_html(ThreadContext context, IRubyObject recv, IRubyObject cstr) {
            return transform(context, cstr, UnescapeHtml.INSTANCE);
        }
    }
    
    public static final class JavaScriptEscape {
        @JRubyMethod
        public static IRubyObject walters_escape_javascript(ThreadContext context, IRubyObject recv, IRubyObject cstr) {
            return transform(context, cstr, EscapeJavaScript.INSTANCE);
        }
    }

    public static final class JavaScriptUnescape {
        @JRubyMethod
        public static IRubyObject walters_unescape_javascript(ThreadContext context, IRubyObject recv, IRubyObject cstr) {
            return transform(context, cstr, UnescapeJavaScript.INSTANCE);
        }
    }
}
