# This file contains code based on the EscapeUtils project.  See the file LICENSE-EscapeUtils.txt for details.
require 'walters/html/html_safety'

class CGI
  extend ::Walters::HtmlSafety
  extend ::Walters::HtmlUnescape

  class << self 
    alias escapeHTML walters_escape_html
    alias unescapeHTML walters_unescape_html
  end
end
