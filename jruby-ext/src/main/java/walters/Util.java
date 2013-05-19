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

import org.jruby.util.ByteList;

import java.nio.charset.Charset;

import static walters.AsciiCharacters.*;


public final class Util {
    private Util() {
    }
    public static final Charset UTF8_CHARSET = Charset.forName("UTF-8");
    static final byte[] HEX_CHARS = bytes("0123456789ABCDEF");

    public static byte[] bytes(String s) {
        return s.getBytes(UTF8_CHARSET);
    }

    public static byte[] bytes(int[] s) {
        byte[] bytes = new byte[s.length];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) s[i];
        }
        
        return bytes;
    }


    static int HOUDINI_ESCAPED_SIZE(int x) {
        return (((x) * 12) / 10);
    }

    static int HOUDINI_UNESCAPED_SIZE(int x) {
        return (x);
    }
    
    static ByteList buf_grow(ByteList buf, int size) {
        if (buf != null) {
            buf.ensure(size);
            return buf;
        } else {
            return new ByteList(size);
        }
    }
    
    static ByteList buf_put(ByteList buf, byte[] src, int idx, int size) {
        if (buf == null) buf = new ByteList();
        buf.append(src, idx, size);
        return buf;
    }
    
    static boolean isxdigit(byte c) {
        return (c >= ZERO && c <= NINE)
                || (c >= UPPER_A && c <= UPPER_F)
                || (c >= LOWER_A && c <= LOWER_F);
    }
    
    static boolean isdigit(byte c) {
        return c >= ZERO && c <= NINE;
    }

    static final byte[][] HEX_STRINGS;
    static {
        byte[][] table = new byte[256][];
        for (int ch = 0; ch < 256; ch++) {
            table[ch & 0xff]  = new byte[] { PERCENT, HEX_CHARS[(ch >> 4) & 0xF], HEX_CHARS[ch & 0xF] };
        }
        HEX_STRINGS = table;
    }
    
    static void hexEscape(ByteList ob, byte ch) {
        ob.append(HEX_STRINGS[ch & 0xff]);
    }

    static byte hex2c(int c) {
        return (byte) (((c & 0xff)| 32) % 39 - 9);
    }

    static int bitmap32(byte... table) {
        int map = 0;
        for (int i = 0; i < table.length; i++) {
            if (table[i] != 0) {
                map |= (1 << i);
            }
        }

        return map;
    }

    static long bitmap64(byte... table) {
        long map = 0;
        for (int i = 0; i < table.length; i++) {
            if (table[i] != 0) {
                map |= (1 << i);
            }
        }

        return map;
    }

}
