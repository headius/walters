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

import static walters.AsciiCharacters.*;
import static walters.Util.*;

public class EscapeJavaScript implements TextTransformer {
    static final TextTransformer INSTANCE = new EscapeJavaScript();
    
    static final byte[] JS_ESCAPE = {
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    };
    
    private static final long bitmap_0_63 = bitmap64(new byte[] {
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    });

    private static final long bitmap_64_127 = bitmap64(new byte[] {
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    });


    private static int scan(byte[] src, final int start, int end) {
        int i = start;
        while (i < end) {
            int c = src[i] & 0xff;
            if (((c > 127 ? 0L : c > 63 ? bitmap_64_127 : bitmap_0_63) & (1 << (c & 63))) != 0) {
                break;
            }

            i++;
        }

        return i;
    }

    public final ByteList transform(byte[] src, int off, int size) {
        final int end = off + size;
        int i;
        
        if ((i = scan(src, off, end)) >= end) {
            return null;
        }

        ByteList ob = new ByteList(HOUDINI_UNESCAPED_SIZE(size));
        ob.append(src, off, i - off);

        while (i < end) {
            int org = i;
            if ((i = scan(src, i, end)) > org) {
                ob.append(src, org, i - org);
                if (i >= end) {
                    break;
                }
            }
            
            byte ch = src[i];
            switch (ch) {
                case SLASH:
                    /*
                     * Escape only if preceded by a '<'
                     */
                    if (i > off && src[i - 1] == LT)
                        ob.append(BACKSLASH);
    
                    ob.append(ch);
                    break;

                case CR:
                    /*
                     * Escape as \n, and skip the next \n if it's there
                     */
                    if (i + 1 < end && src[i + 1] == NL) i++;

                case NL:
                    /*
                     * Escape actually as '\','n', not as '\', '\n'
                     */
                    ch = LOWER_N;

                default:
                    /*
                     * Normal escaping
                     */
                    ob.append(BACKSLASH);
                    ob.append(ch);
                    break;
            }

            i++;
        }

        return ob;
    }


}
