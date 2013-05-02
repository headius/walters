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

import org.jruby.Ruby;
import org.jruby.RubyModule;

import java.io.IOException;

/**
 *
 */
public class WaltersLibrary implements org.jruby.runtime.load.Library {
    @Override
    public void load(Ruby runtime, boolean wrap) throws IOException {
        RubyModule walters = runtime.getOrCreateModule("Walters");
        walters.defineAnnotatedMethods(Walters.class);
        
        // Create some helper modules that can be included for monkey patching
        RubyModule htmlEscape = walters.defineModuleUnder("HtmlEscape");
        htmlEscape.defineAnnotatedMethods(Walters.HtmlEscape.class);
        RubyModule htmlUnescape = walters.defineModuleUnder("HtmlUnescape");
        htmlUnescape.defineAnnotatedMethods(Walters.HtmlUnescape.class);
        
        RubyModule javaScriptEscape = walters.defineModuleUnder("JavaScriptEscape");
        javaScriptEscape.defineAnnotatedMethods(Walters.JavaScriptEscape.class);

        RubyModule javaScriptUnescape = walters.defineModuleUnder("JavaScriptUnescape");
        javaScriptUnescape.defineAnnotatedMethods(Walters.JavaScriptUnescape.class);

    }
}
