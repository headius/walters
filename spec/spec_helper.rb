# encoding: UTF-8
if defined?(JRUBY_VERSION)
  system "cd #{File.expand_path('../..', __FILE__)} && ruby -S rake jar"
else
  system "cd #{File.expand_path('../../xni-ext', __FILE__)} && ruby -S rake"
end

require 'walters'