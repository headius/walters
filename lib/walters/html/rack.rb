# This file contains code based on the EscapeUtils project.  See the file LICENSE-EscapeUtils.txt for details. 
require 'walters/html/html_safety'

module Rack
  module Utils
    include ::Walters::HtmlSafety

    alias escape_html walters_escape_html
    module_function :escape_html
  end
end
