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

import static walters.Util.HOUDINI_UNESCAPED_SIZE;
import static walters.Util.buf_grow;
import static walters.Util.buf_put;
import static walters.AsciiCharacters.*;

public final class UnescapeJavaScript implements TextTransformer {
    
    static final TextTransformer INSTANCE = new UnescapeJavaScript();

    public final ByteList transform(byte[] src, int off, int size) {
        int  i = 0, org, ch;
        ByteList ob = null;

        while (i < size) {
            org = i;
            while (i < size && src[off + i] != BACKSLASH)
                i++;

            if (i > org) {
                if (org == 0) {
                    if (i >= size)
                        return null;

                    ob = buf_grow(ob, HOUDINI_UNESCAPED_SIZE(size));
                }

                ob = buf_put(ob, src, off + org, i - org);
            }

		    /* escaping */
            if (i == size)
                break;

            if (ob == null) ob = new ByteList(HOUDINI_UNESCAPED_SIZE(size));
            if (++i == size) {
                ob.append(BACKSLASH);
                break;
            }

            ch = src[off + i];

            switch (ch) {
                case LOWER_N:
                    ch = NL;
			    /* pass through */

                case BACKSLASH:
                case APOS:
                case QUOTE:
                case SLASH:
                    ob.append(ch);
                    i++;
                    break;

                default:
                    ob.append(BACKSLASH);
                    break;
            }
        }

        return ob;
    }


}
