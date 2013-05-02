# This file contains code based on the EscapeUtils project.  See the file LICENSE-EscapeUtils.txt for details. 
require 'walters'

module Walters
  module HtmlSafety
    if ''.respond_to? :html_safe?
      def walters_escape_html(s)
        if s.html_safe?
          s.to_s.html_safe
        else
          Walters.escape_html(s).html_safe
        end
      end
    else
      include ::Walters::HtmlEscape
    end
  end

end