walters [![Build Status](https://travis-ci.org/wmeissner/walters.png)](https://travis-ci.org/wmeissner/walters)
======

[Walters](https://github.com/wmeissner/walters) is a JRuby wrapper for the [Houdini](https://github.com/vmg/houdini) 
html escaping library 

##### Example usage

    jruby-1.7.4.dev :001 > require 'walters'
     => true
    jruby-1.7.4.dev :002 > Walters.escape_html('<html>')
     => "&lt;html&gt;" 
    

The same extension can be used from the legacy CRuby VM as well (aka MRI)

    2.0.0p0 :001 > require 'walters'
     => true 
    2.0.0p0 :002 > Walters.escape_html('<html>')
     => "&lt;html&gt;"