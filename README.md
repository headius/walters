walters [![Build Status](https://travis-ci.org/wmeissner/walters.png)](https://travis-ci.org/wmeissner/walters)
======

[Walters](https://github.com/wmeissner/walters) is a fast HTML (and href, uri, xml, javascript) escaping library for JRuby.

It is optimised for the non-escaping case - i.e. where the input string contains no characters requiring escaping.

### Example usage

    jruby-1.7.4.dev :001 > require 'walters'
     => true
    jruby-1.7.4.dev :002 > Walters.escape_html('<html>')
     => "&lt;html&gt;" 
    

The same extension can also be used from the legacy CRuby VM for compatibility.

    2.0.0p0 :001 > require 'walters'
     => true 
    2.0.0p0 :002 > Walters.escape_html('<html>')
     => "&lt;html&gt;"
     
### Benchmarks
Escaping 1000 bytes of text requiring escaping 1000000 times under jruby-1.7.4:

                                user     system      total        real
    Rack::Utils.escape_html    89.980000   0.230000  90.210000 ( 90.272000)
    Haml::Helpers.html_escape  50.420000   0.170000  50.590000 ( 51.147000)
    ERB::Util.html_escape      44.650000   0.130000  44.780000 ( 45.518000)
    CGI.escapeHTML             36.230000   0.090000  36.320000 ( 36.358000)
    String#gsub                35.490000   0.090000  35.580000 ( 35.587000)
    Walters.escape_html        10.090000   0.030000  10.120000 ( 10.126000)

Escaping 1000 bytes of html-free text 1000000 times under jruby-1.7.4:

                               user     system      total        real
    Rack::Utils.escape_html     3.530000   0.010000   3.540000 (  3.601000)
    Haml::Helpers.html_escape   4.140000   0.010000   4.150000 (  4.196000)
    ERB::Util.html_escape      11.710000   0.030000  11.740000 ( 11.876000)
    CGI.escapeHTML              3.440000   0.010000   3.450000 (  3.548000)
    String#gsub                 3.470000   0.000000   3.470000 (  3.531000)
    Walters.escape_html         0.660000   0.010000   0.670000 (  0.663000)

### Punching ducks
There are monkey-patching shims supplied that replace common html escaping functions with calls to the Walters escape_html function.

e.g. to patch Rack::Utils.escape_html to use Walters.escape_html:

    require 'walters/html/rack'

### Credits
The escaping routines are based on C code from the [Houdini](https://github.com/vmg/houdini) project, and the specs, benchmarks and monkey patches come from the [escape_utils](https://github.com/brianmario/escape_utils) project
