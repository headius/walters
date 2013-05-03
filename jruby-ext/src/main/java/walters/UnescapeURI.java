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

public class UnescapeURI implements TextTransformer {
    private final boolean isUrl;
    
    static final TextTransformer URI_UNESCAPE = new UnescapeURI(false);
    static final TextTransformer URL_UNESCAPE = new UnescapeURI(true);

    public UnescapeURI(boolean url) {
        isUrl = url;
    }

    @Override
    public ByteList transform(byte[] src, int off, int size) {
        ByteList ob = null;
        int  i = 0, org;

        while (i < size) {
            org = i;
            while (i < size && src[off + i] != PERCENT)
                i++;

            if (i > org) {
                if (org == 0) {
                    if (i >= size && !isUrl)
                        return null;

                    ob = buf_grow(ob, HOUDINI_UNESCAPED_SIZE(size));
                }

                ob = buf_put(ob, src, off + org, i - org);
            }

		    /* escaping */
            if (i >= size)
                break;

            i++;
            if (ob == null) ob = new ByteList(HOUDINI_UNESCAPED_SIZE(size));

            if (i + 1 < size && isxdigit(src[off + i]) && isxdigit(src[off + i + 1])) {
                ob.append((byte) ((hex2c(src[off + i]) << 4) + hex2c(src[off + i + 1])));
                i += 2;
            } else {
                ob.append(PERCENT);
            }
        }

        if (isUrl) {
            // Convert '+' in the output buffer to spaces
            byte[] dst = ob.getUnsafeBytes();
            int idx = ob.begin();
            int end = ob.begin() + ob.length();
            while (idx < end) {
                if (dst[idx] == PLUS) dst[idx] = SPACE;
            }
        }

        return ob;
    }

}
