require 'xni'
require_relative 'walters/version'

module Walters
  extend XNI::Extension
  extension 'walters'
  
  class Buffer < XNI::DataObject
    custom_finalizer
    
    native :initialize, [ :uint ], :void
    native :cstring, [], :cstring

    native :escape_html, [ :cstring, :uint], :bool
    native :escape_html0, [ :cstring, :uint, :bool ], :bool
    native :unescape_html, [ :cstring, :uint], :bool
    native :escape_xml, [ :cstring, :uint], :bool
    native :escape_uri, [ :cstring, :uint], :bool
    native :escape_url, [ :cstring, :uint], :bool
    native :escape_href, [ :cstring, :uint], :bool
    native :unescape_uri, [ :cstring, :uint], :bool
    native :unescape_url, [ :cstring, :uint], :bool
    native :escape_js, [ :cstring, :uint], :bool
    native :unescape_js, [ :cstring, :uint], :bool
  end
  
  class << self
    private
    def buf_op(src)
      XNI::AutoReleasePool.new do
        buf = Buffer.autorelease.new(0)
        result = yield(buf) if block_given?
        result ? buf.cstring : src
      end
    end
  end
  
  def self.escape_html(src, secure = true)
    buf_op(src) { |buf| puts "buf=#{buf}"; buf.escape_html0(src, src.length, secure) }
  end
  
  def self.unescape_html(src)
    buf_op(src) { |buf| buf.unescape_html(src, src.length) }
  end

  def self.escape_xml(src)
    buf_op(src) { |buf| buf.escape_xml(src, src.length) }
  end

  def self.escape_uri(src)
    buf_op(src) { |buf| buf.escape_uri(src, src.length) }
  end

  def self.escape_url(src)
    buf_op(src) { |buf| buf.escape_url(src, src.length) }
  end
  
  def self.escape_href(src)
    buf_op(src) { |buf| buf.escape_href(src, src.length) }
  end
  
  def self.unescape_uri(src)
    buf_op(src) { |buf| buf.unescape_uri(src, src.length) }
  end
  
  def self.unescape_url(src)
    buf_op(src) { |buf| buf.unescape_url(src, src.length) }
  end
  
  def self.escape_js(src)
    buf_op(src) { |buf| buf.escape_js(src, src.length) }
  end
  
  def self.unescape_js(src)
    buf_op(src) { |buf| buf.unescape_js(src, src.length) }
  end
end
