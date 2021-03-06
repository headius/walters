#
# Copyright (C) 2013 Wayne Meissner
#
# This file is part of the Walters project (http://github.com/wmeissner/walters).
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

require 'xni'

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

  native :free_cstring, [ :pointer ], :void
  native :read_cstring, [ :pointer ], :cstring
  native :_escape_html, [ :cstring, :uint, :bool], :pointer
  native :_unescape_html, [ :cstring, :uint], :pointer
  native :_escape_xml, [ :cstring, :uint], :pointer
  native :_escape_uri, [ :cstring, :uint], :pointer
  native :_escape_url, [ :cstring, :uint], :pointer
  native :_escape_href, [ :cstring, :uint], :pointer
  native :_unescape_uri, [ :cstring, :uint], :pointer
  native :_unescape_url, [ :cstring, :uint], :pointer
  native :_escape_js, [ :cstring, :uint], :pointer
  native :_unescape_js, [ :cstring, :uint], :pointer

  class << self
    private
    def cstring(ptr, str)
      if ptr
        begin
          str = read_cstring(ptr)
        ensure
          free_cstring(ptr)
        end
      end
      str
    end
  end

  def self.escape_html(src, secure = true)
    cstring _escape_html(src, src.length, secure), src
  end

  def self.unescape_html(src)
    cstring _unescape_html(src, src.length), src
  end

  def self.escape_xml(src)
    cstring _escape_xml(src, src.length), src
  end

  def self.escape_uri(src)
    cstring _escape_uri(src, src.length), src
  end

  def self.escape_url(src)
    cstring _escape_url(src, src.length), src
  end

  def self.escape_href(src)
    cstring _escape_href(src, src.length), src
  end

  def self.unescape_uri(src)
    cstring _unescape_uri(src, src.length), src
  end

  def self.unescape_url(src)
    cstring _unescape_url(src, src.length), src
  end

  def self.escape_js(src)
    cstring _escape_js(src, src.length), src
  end

  def self.unescape_js(src)
    cstring _unescape_js(src, src.length), src
  end
end
