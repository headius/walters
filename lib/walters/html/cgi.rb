# This file contains code based on the EscapeUtils project.  See the file LICENSE-EscapeUtils.txt for details.
require_relative 'html_safety'

module CGI
  extend ::Walters::HtmlSafety
  extend ::Walters::HtmlUnescape

  class << self 
    alias escapeHTML walters_escape_html
    alias unescapeHTML walters_unescape_html
  end
end