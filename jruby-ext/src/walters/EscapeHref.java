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
 * 
 * This file contains code based on the Houdini project.  See the file LICENSE-houdini.txt for details. 
 */

package walters;

import org.jruby.util.ByteList;

import static org.yecht.BytecodeNodeHandler.bytes;
import static walters.AsciiCharacters.*;
import static walters.Util.*;

public final class EscapeHref implements TextTransformer {

    static final TextTransformer INSTANCE = new EscapeHref(); 
    
    /*
     * The following characters will not be escaped:
     *
     *		-_.+!*'(),%#@?=;:/,+&$ alphanum
     *
     * Note that this character set is the addition of:
     *
     *	- The characters which are safe to be in an URL
     *	- The characters which are *not* safe to be in
     *	an URL because they are RESERVED characters.
     *
     * We asume (lazily) that any RESERVED char that
     * appears inside an URL is actually meant to
     * have its native function (i.e. as an URL 
     * component/separator) and hence needs no escaping.
     *
     * There are two exceptions: the chacters & (amp)
     * and ' (single quote) do not appear in the table.
     * They are meant to appear in the URL as components,
     * yet they require special HTML-entity escaping
     * to generate valid HTML markup.
     *
     * All other characters will be escaped to %XX.
     *
     */
    static final byte[] HREF_SAFE = {
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 1, 0, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1,
            0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    };

    static final byte[] AMPER_ESCAPE = bytes("&amp;");
    static final byte[] APOS_ESCAPE = bytes("&#x27;");
    
    public ByteList transform(byte[] src, int off, int size) {
        ByteList ob = null;
        int  i = 0, org;

        while (i < size) {
            org = i;
            while (i < size && HREF_SAFE[src[off + i] & 0xff] != 0)
                i++;

            if (i >= size && org == 0) {
                return null;
            }

            if (ob == null) ob = new ByteList(HOUDINI_ESCAPED_SIZE(size));
            if (i > org) ob.append(src, off + org, i - org);
            
		    /* escaping */
            if (i >= size)
                break;

            switch (src[off + i]) {
                /* amp appears all the time in URLs, but needs
                 * HTML-entity escaping to be inside an href */
                case AMPER:
                    ob.append(AMPER_ESCAPE);
                    break;

                /* the single quote is a valid URL character
                 * according to the standard; it needs HTML
                 * entity escaping too */
                case APOS:
                    ob.append(APOS_ESCAPE);
                    break;
		
		        /* every other character goes with a %XX escaping */
                default:
                    hexEscape(ob, src[off + i]);
                    break;
            }

            i++;
        }

        return ob;
    }

}
