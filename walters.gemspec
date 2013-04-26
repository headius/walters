require File.expand_path("../lib/#{File.basename(__FILE__, '.gemspec')}/version", __FILE__)

Gem::Specification.new do |s|
  s.name = 'walters'
  s.version = Walters::VERSION
  s.author = 'Wayne Meissner'
  s.email = 'wmeissner@gmail.com'
  s.homepage = 'http://wiki.github.com/wmeissner/walters'
  s.summary = 'JRuby wrapper for Houdini'
  s.description = 'JRuby wrapper for Houdini html escaping library'
  s.files = %w(walters.gemspec LICENSE README.md Rakefile)
  s.files += Dir['lib/**/*.rb', 'xni-ext/**/*.{c,cpp,h}', '{spec,libtest}/**/*.{c,cpp,h,rb}']
  s.has_rdoc = false
  s.license = 'Apache 2.0'
  s.required_ruby_version = '>= 1.9.3'
  s.add_dependency 'rake', '>= 10.0.0'
  s.add_dependency 'xni', '>= 0.1.0'
  s.add_development_dependency 'rspec'
  s.add_development_dependency 'rubygems-tasks'
  s.extensions = ['xni-ext/Rakefile']
end
