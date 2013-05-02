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

import java.util.Map;
import java.util.TreeMap;

import static walters.Util.*;
import static walters.AsciiCharacters.*;

public class UnescapeHtml implements TextTransformer {
    static final TextTransformer INSTANCE = new UnescapeHtml();

    public final ByteList transform(byte[] src, int off, int size) {
        ByteList ob = null;
        int i = 0, org;

        while (i < size) {
            org = i;
            while (i < size && src[off + i] != AMPER) {
                i++;
            }

            if (i > org) {
                if (org == 0) {
                    if (i >= size) {
                        return null;
                    }

                    ob = buf_grow(ob, HOUDINI_UNESCAPED_SIZE(size));
                }

                ob = buf_put(ob, src, off + org, i - org);
            }

		    /* escaping */
            if (i >= size)
                break;

            i++;
            if (ob == null) ob = new ByteList();
            i += unescape_ent(ob, src, off + i, size - i);
        }

        return ob;
    }


    static void buf_put_utf8(ByteList ob, int c) {

        if (c < 0x80) {
            ob.append((byte) c);
        
        } else if (c < 0x800) {
            ob.append(new byte[] { (byte)(192 + (c / 64)), (byte) (128 + (c % 64)) });
        
        } else if (c - 0xd800 < 0x800) {
            ob.append(QUERY);
        
        } else if (c < 0x10000) {
            ob.append(new byte[] {
                    (byte)(224 + (c / 4096)),
                    (byte)(128 + (c / 64) % 64),
                    (byte)(128 + (c % 64))
            });
        } else if (c < 0x110000) {
            ob.append(new byte[] {
                    (byte) (240 + (c / 262144)),
                    (byte) (128 + (c / 4096) % 64),
                    (byte) (128 + (c / 64) % 64),
                    (byte) (128 + (c % 64))
            });
        } else {
            ob.append(QUERY);
        }
    }

    static int unescape_ent(ByteList ob, byte[] src, int off, int size) {
        int i = 0;

        if (size > 3 && src[off + 0] == HASH) {
            int codepoint = 0;

            if (isdigit(src[off + 1])) {
                for (i = 1; i < size && isdigit(src[off + i]); ++i)
                    codepoint = (codepoint * 10) + (src[off + i] - ZERO);
            }

            else if (src[off + 1] == LOWER_X || src[off + 1] == UPPER_X) {
                for (i = 2; i < size && isxdigit(src[off + i]); ++i)
                    codepoint = (codepoint * 16) + hex2c(src[off + i]);
            }

            if (i < size && src[off + i] == SEMICOLON) {
                buf_put_utf8(ob, codepoint);
                return i + 1;
            }
        }

        else {
            

            for (i = 1; i < size; ++i) {
                if (src[off + i] == SPACE)
                    break;

                if (src[off + i] == SEMICOLON) {
                    byte[] entity = HtmlEntityTable.find(src, off, i);
                    
                    if (entity != null) {
                        ob.append(entity);
                        return i + 1;
                    }

                    break;
                }
            }
        }

        ob.append(AMPER);
        return 0;
    }

}
