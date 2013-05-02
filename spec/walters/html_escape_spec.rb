# encoding: UTF-8
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
#
# This file contains code based on the EscapeUtils project.  See the file LICENSE-EscapeUtils.txt for details.  
#

require File.expand_path('../../spec_helper.rb', __FILE__)

describe 'html escape' do
  it 'double quotes' do
    Walters.escape_html("<some_tag some_attr=\"some value\"/>").should == '&lt;some_tag some_attr=&quot;some value&quot;&#47;&gt;'
  end

  it 'single quotes' do
    Walters.escape_html("<some_tag some_attr='some value'/>").should == '&lt;some_tag some_attr=&#39;some value&#39;&#47;&gt;'
  end

  it 'ampersand' do
    Walters.escape_html('<b>Bourbon & Branch</b>').should == '&lt;b&gt;Bourbon &amp; Branch&lt;&#47;b&gt;'
  end

  it 'original returned if no escaping required' do
    str = 'foobar'
    Walters.escape_html(str).should equal str
  end
  
  it 'all tags escaped' do 
    Walters.escape_html('&<>"\'/').should == '&amp;&lt;&gt;&quot;&#39;&#47;'
  end

  it 'plain text followed by tag' do
    Walters.escape_html('foobar<1>').should == 'foobar&lt;1&gt;'
  end

  it 'non-utf8 input' do
    lambda { Walters.escape_html('<b>Bourbon & Branch</b>'.encode('ISO-8859-1')) }.should raise_error Encoding::CompatibilityError
  end
  
  it 'returned value has same encoding as original' do
    str = '<b>Bourbon & Branch</b>'.encode('utf-8')
    Walters.escape_html(str).encoding.should == str.encoding
  end
end