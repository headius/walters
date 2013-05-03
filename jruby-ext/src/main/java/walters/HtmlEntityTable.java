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

import java.util.HashMap;
import java.util.Map;

import static walters.Util.bytes;

public class HtmlEntityTable {
    private static final class Entity {
        private final byte[] bytes;
        private final int off, len;

        private Entity(byte[] bytes, int off, int len) {
            this.bytes = bytes;
            this.off = off;
            this.len = len;
        }
        
        private Entity(String str) {
            this.bytes = bytes(str);
            this.off = 0;
            this.len = bytes.length;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Entity entity = (Entity) o;

            if (len != entity.len) return false;
            for (int i = 0; i < len; i++) {
                if (bytes[off + i] != entity.bytes[entity.off + i]) {
                    return false;
                }
            }

            return true;
        }

        @Override
        public int hashCode() {
            int result = 1;
            for (int i = 0; i < len; i++) {
                result = 31 * result + bytes[off + i];
            }
            result = 31 * result + len;
            return result;
        }
    }
    
    public static final byte[] find(byte[] src, int off, int len) {
        return entities.get(new Entity(src, off, len));
    }
    
    private static final void addEntity(Map<Entity, byte[]>m, String e, int... utf8) {
        m.put(new Entity(e), bytes(utf8));
    }
    
    static final Map<Entity, byte[]> entities;
    static {
        Map<Entity, byte[]> m = new HashMap<Entity, byte[]>();
        addEntity(m, "quot", 0x22);
        addEntity(m, "amp", 0x26);
        addEntity(m, "apos", 0x27);
        addEntity(m, "lt", 0x3C);
        addEntity(m, "gt", 0x3E);
        addEntity(m, "nbsp", 0xC2, 0xA0);
        addEntity(m, "iexcl", 0xC2, 0xA1);
        addEntity(m, "cent", 0xC2, 0xA2);
        addEntity(m, "pound", 0xC2, 0xA3);
        addEntity(m, "curren", 0xC2, 0xA4);
        addEntity(m, "yen", 0xC2, 0xA5);
        addEntity(m, "brvbar", 0xC2, 0xA6);
        addEntity(m, "sect", 0xC2, 0xA7);
        addEntity(m, "uml", 0xC2, 0xA8);
        addEntity(m, "copy", 0xC2, 0xA9);
        addEntity(m, "ordf", 0xC2, 0xAA);
        addEntity(m, "laquo", 0xC2, 0xAB);
        addEntity(m, "not", 0xC2, 0xAC);
        addEntity(m, "shy", 0xC2, 0xAD);
        addEntity(m, "reg", 0xC2, 0xAE);
        addEntity(m, "macr", 0xC2, 0xAF);
        addEntity(m, "deg", 0xC2, 0xB0);
        addEntity(m, "plusmn", 0xC2, 0xB1);
        addEntity(m, "sup2", 0xC2, 0xB2);
        addEntity(m, "sup3", 0xC2, 0xB3);
        addEntity(m, "acute", 0xC2, 0xB4);
        addEntity(m, "micro", 0xC2, 0xB5);
        addEntity(m, "para", 0xC2, 0xB6);
        addEntity(m, "middot", 0xC2, 0xB7);
        addEntity(m, "cedil", 0xC2, 0xB8);
        addEntity(m, "sup1", 0xC2, 0xB9);
        addEntity(m, "ordm", 0xC2, 0xBA);
        addEntity(m, "raquo", 0xC2, 0xBB);
        addEntity(m, "frac14", 0xC2, 0xBC);
        addEntity(m, "frac12", 0xC2, 0xBD);
        addEntity(m, "frac34", 0xC2, 0xBE);
        addEntity(m, "iquest", 0xC2, 0xBF);
        addEntity(m, "Agrave", 0xC3, 0x80);
        addEntity(m, "Aacute", 0xC3, 0x81);
        addEntity(m, "Acirc", 0xC3, 0x82);
        addEntity(m, "Atilde", 0xC3, 0x83);
        addEntity(m, "Auml", 0xC3, 0x84);
        addEntity(m, "Aring", 0xC3, 0x85);
        addEntity(m, "AElig", 0xC3, 0x86);
        addEntity(m, "Ccedil", 0xC3, 0x87);
        addEntity(m, "Egrave", 0xC3, 0x88);
        addEntity(m, "Eacute", 0xC3, 0x89);
        addEntity(m, "Ecirc", 0xC3, 0x8A);
        addEntity(m, "Euml", 0xC3, 0x8B);
        addEntity(m, "Igrave", 0xC3, 0x8C);
        addEntity(m, "Iacute", 0xC3, 0x8D);
        addEntity(m, "Icirc", 0xC3, 0x8E);
        addEntity(m, "Iuml", 0xC3, 0x8F);
        addEntity(m, "ETH", 0xC3, 0x90);
        addEntity(m, "Ntilde", 0xC3, 0x91);
        addEntity(m, "Ograve", 0xC3, 0x92);
        addEntity(m, "Oacute", 0xC3, 0x93);
        addEntity(m, "Ocirc", 0xC3, 0x94);
        addEntity(m, "Otilde", 0xC3, 0x95);
        addEntity(m, "Ouml", 0xC3, 0x96);
        addEntity(m, "times", 0xC3, 0x97);
        addEntity(m, "Oslash", 0xC3, 0x98);
        addEntity(m, "Ugrave", 0xC3, 0x99);
        addEntity(m, "Uacute", 0xC3, 0x9A);
        addEntity(m, "Ucirc", 0xC3, 0x9B);
        addEntity(m, "Uuml", 0xC3, 0x9C);
        addEntity(m, "Yacute", 0xC3, 0x9D);
        addEntity(m, "THORN", 0xC3, 0x9E);
        addEntity(m, "szlig", 0xC3, 0x9F);
        addEntity(m, "agrave", 0xC3, 0xA0);
        addEntity(m, "aacute", 0xC3, 0xA1);
        addEntity(m, "acirc", 0xC3, 0xA2);
        addEntity(m, "atilde", 0xC3, 0xA3);
        addEntity(m, "auml", 0xC3, 0xA4);
        addEntity(m, "aring", 0xC3, 0xA5);
        addEntity(m, "aelig", 0xC3, 0xA6);
        addEntity(m, "ccedil", 0xC3, 0xA7);
        addEntity(m, "egrave", 0xC3, 0xA8);
        addEntity(m, "eacute", 0xC3, 0xA9);
        addEntity(m, "ecirc", 0xC3, 0xAA);
        addEntity(m, "euml", 0xC3, 0xAB);
        addEntity(m, "igrave", 0xC3, 0xAC);
        addEntity(m, "iacute", 0xC3, 0xAD);
        addEntity(m, "icirc", 0xC3, 0xAE);
        addEntity(m, "iuml", 0xC3, 0xAF);
        addEntity(m, "eth", 0xC3, 0xB0);
        addEntity(m, "ntilde", 0xC3, 0xB1);
        addEntity(m, "ograve", 0xC3, 0xB2);
        addEntity(m, "oacute", 0xC3, 0xB3);
        addEntity(m, "ocirc", 0xC3, 0xB4);
        addEntity(m, "otilde", 0xC3, 0xB5);
        addEntity(m, "ouml", 0xC3, 0xB6);
        addEntity(m, "divide", 0xC3, 0xB7);
        addEntity(m, "oslash", 0xC3, 0xB8);
        addEntity(m, "ugrave", 0xC3, 0xB9);
        addEntity(m, "uacute", 0xC3, 0xBA);
        addEntity(m, "ucirc", 0xC3, 0xBB);
        addEntity(m, "uuml", 0xC3, 0xBC);
        addEntity(m, "yacute", 0xC3, 0xBD);
        addEntity(m, "thorn", 0xC3, 0xBE);
        addEntity(m, "yuml", 0xC3, 0xBF);
        addEntity(m, "OElig", 0xC5, 0x92);
        addEntity(m, "oelig", 0xC5, 0x93);
        addEntity(m, "Scaron", 0xC5, 0xA0);
        addEntity(m, "scaron", 0xC5, 0xA1);
        addEntity(m, "Yuml", 0xC5, 0xB8);
        addEntity(m, "fnof", 0xC6, 0x92);
        addEntity(m, "circ", 0xCB, 0x86);
        addEntity(m, "tilde", 0xCB, 0x9C);
        addEntity(m, "Alpha", 0xCE, 0x91);
        addEntity(m, "Beta", 0xCE, 0x92);
        addEntity(m, "Gamma", 0xCE, 0x93);
        addEntity(m, "Delta", 0xCE, 0x94);
        addEntity(m, "Epsilon", 0xCE, 0x95);
        addEntity(m, "Zeta", 0xCE, 0x96);
        addEntity(m, "Eta", 0xCE, 0x97);
        addEntity(m, "Theta", 0xCE, 0x98);
        addEntity(m, "Iota", 0xCE, 0x99);
        addEntity(m, "Kappa", 0xCE, 0x9A);
        addEntity(m, "Lambda", 0xCE, 0x9B);
        addEntity(m, "Mu", 0xCE, 0x9C);
        addEntity(m, "Nu", 0xCE, 0x9D);
        addEntity(m, "Xi", 0xCE, 0x9E);
        addEntity(m, "Omicron", 0xCE, 0x9F);
        addEntity(m, "Pi", 0xCE, 0xA0);
        addEntity(m, "Rho", 0xCE, 0xA1);
        addEntity(m, "Sigma", 0xCE, 0xA3);
        addEntity(m, "Tau", 0xCE, 0xA4);
        addEntity(m, "Upsilon", 0xCE, 0xA5);
        addEntity(m, "Phi", 0xCE, 0xA6);
        addEntity(m, "Chi", 0xCE, 0xA7);
        addEntity(m, "Psi", 0xCE, 0xA8);
        addEntity(m, "Omega", 0xCE, 0xA9);
        addEntity(m, "alpha", 0xCE, 0xB1);
        addEntity(m, "beta", 0xCE, 0xB2);
        addEntity(m, "gamma", 0xCE, 0xB3);
        addEntity(m, "delta", 0xCE, 0xB4);
        addEntity(m, "epsilon", 0xCE, 0xB5);
        addEntity(m, "zeta", 0xCE, 0xB6);
        addEntity(m, "eta", 0xCE, 0xB7);
        addEntity(m, "theta", 0xCE, 0xB8);
        addEntity(m, "iota", 0xCE, 0xB9);
        addEntity(m, "kappa", 0xCE, 0xBA);
        addEntity(m, "lambda", 0xCE, 0xBB);
        addEntity(m, "mu", 0xCE, 0xBC);
        addEntity(m, "nu", 0xCE, 0xBD);
        addEntity(m, "xi", 0xCE, 0xBE);
        addEntity(m, "omicron", 0xCE, 0xBF);
        addEntity(m, "pi", 0xCF, 0x80);
        addEntity(m, "rho", 0xCF, 0x81);
        addEntity(m, "sigmaf", 0xCF, 0x82);
        addEntity(m, "sigma", 0xCF, 0x83);
        addEntity(m, "tau", 0xCF, 0x84);
        addEntity(m, "upsilon", 0xCF, 0x85);
        addEntity(m, "phi", 0xCF, 0x86);
        addEntity(m, "chi", 0xCF, 0x87);
        addEntity(m, "psi", 0xCF, 0x88);
        addEntity(m, "omega", 0xCF, 0x89);
        addEntity(m, "thetasym", 0xCF, 0x91);
        addEntity(m, "piv", 0xCF, 0x96);
        addEntity(m, "ensp", 0xE2, 0x80, 0x82);
        addEntity(m, "emsp", 0xE2, 0x80, 0x83);
        addEntity(m, "thinsp", 0xE2, 0x80, 0x89);
        addEntity(m, "zwnj", 0xE2, 0x80, 0x8C);
        addEntity(m, "zwj", 0xE2, 0x80, 0x8D);
        addEntity(m, "lrm", 0xE2, 0x80, 0x8E);
        addEntity(m, "rlm", 0xE2, 0x80, 0x8F);
        addEntity(m, "ndash", 0xE2, 0x80, 0x93);
        addEntity(m, "mdash", 0xE2, 0x80, 0x94);
        addEntity(m, "lsquo", 0xE2, 0x80, 0x98);
        addEntity(m, "rsquo", 0xE2, 0x80, 0x99);
        addEntity(m, "sbquo", 0xE2, 0x80, 0x9A);
        addEntity(m, "ldquo", 0xE2, 0x80, 0x9C);
        addEntity(m, "rdquo", 0xE2, 0x80, 0x9D);
        addEntity(m, "bdquo", 0xE2, 0x80, 0x9E);
        addEntity(m, "dagger", 0xE2, 0x80, 0xA0);
        addEntity(m, "Dagger", 0xE2, 0x80, 0xA1);
        addEntity(m, "bull", 0xE2, 0x80, 0xA2);
        addEntity(m, "hellip", 0xE2, 0x80, 0xA6);
        addEntity(m, "permil", 0xE2, 0x80, 0xB0);
        addEntity(m, "prime", 0xE2, 0x80, 0xB2);
        addEntity(m, "Prime", 0xE2, 0x80, 0xB3);
        addEntity(m, "lsaquo", 0xE2, 0x80, 0xB9);
        addEntity(m, "rsaquo", 0xE2, 0x80, 0xBA);
        addEntity(m, "oline", 0xE2, 0x80, 0xBE);
        addEntity(m, "frasl", 0xE2, 0x81, 0x84);
        addEntity(m, "euro", 0xE2, 0x82, 0xAC);
        addEntity(m, "image", 0xE2, 0x84, 0x91);
        addEntity(m, "weierp", 0xE2, 0x84, 0x98);
        addEntity(m, "real", 0xE2, 0x84, 0x9C);
        addEntity(m, "trade", 0xE2, 0x84, 0xA2);
        addEntity(m, "alefsym", 0xE2, 0x84, 0xB5);
        addEntity(m, "larr", 0xE2, 0x86, 0x90);
        addEntity(m, "uarr", 0xE2, 0x86, 0x91);
        addEntity(m, "rarr", 0xE2, 0x86, 0x92);
        addEntity(m, "darr", 0xE2, 0x86, 0x93);
        addEntity(m, "harr", 0xE2, 0x86, 0x94);
        addEntity(m, "crarr", 0xE2, 0x86, 0xB5);
        addEntity(m, "lArr", 0xE2, 0x87, 0x90);
        addEntity(m, "uArr", 0xE2, 0x87, 0x91);
        addEntity(m, "rArr", 0xE2, 0x87, 0x92);
        addEntity(m, "dArr", 0xE2, 0x87, 0x93);
        addEntity(m, "hArr", 0xE2, 0x87, 0x94);
        addEntity(m, "forall", 0xE2, 0x88, 0x80);
        addEntity(m, "part", 0xE2, 0x88, 0x82);
        addEntity(m, "exist", 0xE2, 0x88, 0x83);
        addEntity(m, "empty", 0xE2, 0x88, 0x85);
        addEntity(m, "nabla", 0xE2, 0x88, 0x87);
        addEntity(m, "isin", 0xE2, 0x88, 0x88);
        addEntity(m, "notin", 0xE2, 0x88, 0x89);
        addEntity(m, "ni", 0xE2, 0x88, 0x8B);
        addEntity(m, "prod", 0xE2, 0x88, 0x8F);
        addEntity(m, "sum", 0xE2, 0x88, 0x91);
        addEntity(m, "minus", 0xE2, 0x88, 0x92);
        addEntity(m, "lowast", 0xE2, 0x88, 0x97);
        addEntity(m, "radic", 0xE2, 0x88, 0x9A);
        addEntity(m, "prop", 0xE2, 0x88, 0x9D);
        addEntity(m, "infin", 0xE2, 0x88, 0x9E);
        addEntity(m, "ang", 0xE2, 0x88, 0xA0);
        addEntity(m, "and", 0xE2, 0x88, 0xA7);
        addEntity(m, "or", 0xE2, 0x88, 0xA8);
        addEntity(m, "cap", 0xE2, 0x88, 0xA9);
        addEntity(m, "cup", 0xE2, 0x88, 0xAA);
        addEntity(m, "int", 0xE2, 0x88, 0xAB);
        addEntity(m, "there4", 0xE2, 0x88, 0xB4);
        addEntity(m, "sim", 0xE2, 0x88, 0xBC);
        addEntity(m, "cong", 0xE2, 0x89, 0x85);
        addEntity(m, "asymp", 0xE2, 0x89, 0x88);
        addEntity(m, "ne", 0xE2, 0x89, 0xA0);
        addEntity(m, "equiv", 0xE2, 0x89, 0xA1);
        addEntity(m, "le", 0xE2, 0x89, 0xA4);
        addEntity(m, "ge", 0xE2, 0x89, 0xA5);
        addEntity(m, "sub", 0xE2, 0x8A, 0x82);
        addEntity(m, "sup", 0xE2, 0x8A, 0x83);
        addEntity(m, "nsub", 0xE2, 0x8A, 0x84);
        addEntity(m, "sube", 0xE2, 0x8A, 0x86);
        addEntity(m, "supe", 0xE2, 0x8A, 0x87);
        addEntity(m, "oplus", 0xE2, 0x8A, 0x95);
        addEntity(m, "otimes", 0xE2, 0x8A, 0x97);
        addEntity(m, "perp", 0xE2, 0x8A, 0xA5);
        addEntity(m, "sdot", 0xE2, 0x8B, 0x85);
        addEntity(m, "lceil", 0xE2, 0x8C, 0x88);
        addEntity(m, "rceil", 0xE2, 0x8C, 0x89);
        addEntity(m, "lfloor", 0xE2, 0x8C, 0x8A);
        addEntity(m, "rfloor", 0xE2, 0x8C, 0x8B);
        addEntity(m, "lang", 0xE2, 0x9F, 0xA8);
        addEntity(m, "rang", 0xE2, 0x9F, 0xA9);
        addEntity(m, "loz", 0xE2, 0x97, 0x8A);
        addEntity(m, "spades", 0xE2, 0x99, 0xA0);
        addEntity(m, "clubs", 0xE2, 0x99, 0xA3);
        addEntity(m, "hearts", 0xE2, 0x99, 0xA5);
        addEntity(m, "diams", 0xE2, 0x99, 0xA6);
        
        entities = new HashMap<Entity, byte[]>(m);
    }
}
