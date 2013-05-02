# This file contains code based on the EscapeUtils project.  See the file LICENSE-EscapeUtils.txt for details.
require_relative 'html_safety'

module Haml
  module Helpers
    include ::Walters::HtmlSafety

    alias html_escape walters_escape_html
  end
end