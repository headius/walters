# This file contains code based on the EscapeUtils project.  See the file LICENSE-EscapeUtils.txt for details.
require_relative 'html_safety'

class ERB
  module Util
    include ::Walters::HtmlSafety

    alias html_escape walters_escape_html
    alias h html_escape
    module_function :h
    module_function :html_escape
  end
end