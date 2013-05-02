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

public class EscapeHtml implements TextTransformer {
    private final byte[][] entityTable;
    private final long bitmap;

    static final byte[][] HTML_ESCAPES = {
            bytes(""),
            bytes("&quot;"),
            bytes("&amp;"),
            bytes("&#39;"),
            bytes("&#47;"),
            bytes("&lt;"),
            bytes("&gt;")
    };
   
    public EscapeHtml(byte[] table) {
        this.bitmap = Util.bitmap64(table);
        
        byte[][] entityTable = new byte[table.length][];
        for (int i = 0; i < table.length; i++) {
            entityTable[i] = HTML_ESCAPES[table[i]];
        }

        this.entityTable = entityTable;
    }

    /**
     * According to the OWASP rules:
     *
     * & --> &amp;
     * < --> &lt;
     * > --> &gt;
     * " --> &quot;
     * ' --> &#x27;     &apos; is not recommended
     * / --> &#x2F;     forward slash is included as it helps end an HTML entity
     *
     */

    // Truncated HTML escape table - just lookup values <= 63
    private static final byte[] ESCAPE_TABLE = {
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 1, 0, 0, 0, 2, 3, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 6, 0
    };

    private static final byte[] SECURE_ESCAPE_TABLE = {
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 1, 0, 0, 0, 2, 3, 0, 0, 0, 0, 0, 0, 0, 4,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 6, 0
    };

    public static final TextTransformer SECURE = new EscapeHtml(SECURE_ESCAPE_TABLE);
    public static final TextTransformer INSECURE = new EscapeHtml(ESCAPE_TABLE);

    private static int scan(byte[] src, int start, final int end, final long bitmap) {
        int i = start, c;
        while (i < end && ((c = src[i]) > 63 || (bitmap & (1 << (c & 63))) == 0)) {
            i++;
        }

        return i;
    }
    
    public final ByteList transform(byte[] src, final int off, final int size) {
        final int end = off + size;
        int i;
        
        if ((i = scan(src, off, end, bitmap)) >= end) {
            return null;
        }

        ByteList ob = new ByteList(HOUDINI_ESCAPED_SIZE(size));
        ob.append(src, off, i - off);
        
        while (i < end) {
            int org = i;
            if ((i = scan(src, i, end, bitmap)) > org) {
                ob.append(src, org, i - org);
                if (i >= end) { 
                    break;
                }
            }

            ob.append(entityTable[src[i] & 63]);
            i++;
        }

        return ob;
    }

}
