require 'rake/clean'
require 'rubygems/package_task'
require 'rubygems/tasks'


def gem_spec
  @gem_spec ||= Gem::Specification.load('walters.gemspec')
end

TEST_DEPS = []
if defined?(JRUBY_VERSION)
  require 'ant'
  jar_file = 'lib/walters/walters_ext.jar'
  CLEAN.include jar_file
  TEST_DEPS << jar_file

  gem_spec.files << jar_file

  directory 'pkg/classes'
  CLEAN.include 'pkg/classes'

  desc 'Compile the JRuby extension'
  task :compile => FileList['pkg/classes', 'jruby-ext/src/main/java/**/*.java'] do |t|
    ant.javac :srcdir => 'jruby-ext/src/main/java', :destdir => t.prerequisites.first,
              :source => '1.6', :target => '1.6', :debug => true, :includeantruntime => false,
              :classpath => '${java.class.path}:${sun.boot.class.path}'
  end

  desc 'Build the jar'
  file jar_file => :compile do |t|
    ant.jar :basedir => 'pkg/classes', :destfile => t.name, :includes => '**/*.class'
  end

  task :jar => jar_file
end

Gem::Tasks.new do |t|
  t.scm.tag.format = '%s'
end

task :specs do
  sh %{#{Gem.ruby} -w -Ilib -S rspec spec}
end

namespace 'java' do
  java_gem_spec = Gem::Specification.new do |s|
    s.name = gem_spec.name
    s.version = gem_spec.version
    s.author = gem_spec.author
    s.email = gem_spec.email
    s.homepage = gem_spec.homepage
    s.summary = gem_spec.summary
    s.description = gem_spec.description
    s.files = %w(LICENSE LICENSE-EscapeUtils.txt LICENSE-houdini.txt README.md Rakefile)
    s.files << Dir['{lib,spec,benchmark}/**/*.rb']
    s.files << 'lib/walters/walters_ext.jar'
    s.has_rdoc = false
    s.license = gem_spec.license
    s.platform = 'java'
  end

  Gem::PackageTask.new(java_gem_spec) do |pkg|
    pkg.need_zip = true
    pkg.need_tar = true
    pkg.package_dir = 'pkg'
  end
  
  task :gem => 'lib/walters/walters_ext.jar'
end

task 'gem:java' => 'java:gem'
