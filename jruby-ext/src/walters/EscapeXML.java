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

import static walters.Util.*;

public class EscapeXML implements TextTransformer {
    static final TextTransformer INSTANCE = new EscapeXML();

    @Override
    public ByteList transform(byte[] src, int off, int size) {
        int i = 0;

        while (i < size && XML_LOOKUP_TABLE[src[off + i] & 0xff] == 0) {
            i++;
        }
        
        // fast path - no substitutions needed, just use original string
        if (i >= size) {
            return null;
        }

        ByteList ob = new ByteList(HOUDINI_ESCAPED_SIZE(size));
        ob.append(src, off, i);

        while (i < size) {
            int start = i, end = i;
            byte code = 0;
            
            while (i < size) {
                int b;

                b = src[off + i++] & 0xff;
                code = XML_LOOKUP_TABLE[b];

                if (code == 0) {
				    /* single character used literally */
                } else if (code >= CODE_INVALID) {
                    break; /* insert lookup code string */
                } else if (code > size - end) {
                    code = CODE_INVALID; /* truncated UTF-8 character */
                    break;
                } else {
                    int chr = b & (0xff >> code);

                    while (--code > 0) {
                        b = src[off + i++] & 0xff;
                        if ((b & 0xc0) != 0x80) {
                            code = CODE_INVALID;
                            break;
                        }
                        chr = (chr << 6) + (b & 0x3f);
                    }

                    switch (i - end) {
                        case 2:
                            if (chr < 0x80)
                                code = CODE_INVALID;
                            break;
                        case 3:
                            if (chr < 0x800 ||
                                    (chr > 0xd7ff && chr < 0xe000) ||
                                    chr > 0xfffd)
                                code = CODE_INVALID;
                            break;
                        case 4:
                            if (chr < 0x10000 || chr > 0x10ffff)
                                code = CODE_INVALID;
                            break;
                        default:
                            break;
                    }
                    if (code == CODE_INVALID)
                        break;
                }
                end = i;
            }

            if (end > start)
                ob.append(src, off + start, end - start);

		    /* escaping */
            if (end >= size)
                break;
    
            ob.append(LOOKUP_CODES[code]);
        }

        return ob;
    }

    static final byte[][] LOOKUP_CODES = {
        bytes(""), /* reserved: use literal single character */
        bytes(""), /* unused */
        bytes(""), /* reserved: 2 character UTF-8 */
        bytes(""), /* reserved: 3 character UTF-8 */
        bytes(""), /* reserved: 4 character UTF-8 */
        bytes("?"), /* invalid UTF-8 character */
        bytes("&quot;"),
        bytes("&amp;"),
        bytes("&apos;"),
        bytes("&lt;"),
        bytes("&gt;")
    };


    static final byte CODE_INVALID = 5;

    static final byte[] XML_LOOKUP_TABLE = {
	    /* ASCII: 0xxxxxxx */
        5, 5, 5, 5, 5, 5, 5, 5, 5, 0, 0, 5, 5, 0, 5, 5,
        5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5,
        0, 0, 6, 0, 0, 0, 7, 8, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 0, 10, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,

	    /* Invalid UTF-8 char start: 10xxxxxx */
        5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5,
        5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5,
        5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5,
        5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5,

	    /* Multibyte UTF-8 */

	    /* 2 bytes: 110xxxxx */
        2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
        2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,

	    /* 3 bytes: 1110xxxx */
        3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,

	    /* 4 bytes: 11110xxx */
        4, 4, 4, 4, 4, 4, 4, 4,

	    /* Invalid UTF-8: 11111xxx */
        5, 5, 5, 5, 5, 5, 5, 5,
    };


}
