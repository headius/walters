# This file contains code based on the EscapeUtils project.  See the file LICENSE-EscapeUtils.txt for details.
require 'walters'

module ActionView
  module Helpers
    module JavaScriptHelper
      include ::Walters::JavaScriptEscape
      alias escape_javascript walters_escape_javascript
    end
  end
end