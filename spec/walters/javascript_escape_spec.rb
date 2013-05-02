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

describe 'javascript' do
  it 'quotes and newlines' do
    Walters.escape_javascript(%(This "thing" is really\n netos\r\n\n')).should == %(This \\"thing\\" is really\\n netos\\n\\n\\')     
  end
  
  it 'backslash' do
    Walters.escape_javascript(%(backslash\\test)).should == %(backslash\\\\test) 
    
  end
  
  it 'closed html tags' do
    Walters.escape_javascript(%(keep <open>, but dont </close> tags)).should == %(keep <open>, but dont <\\/close> tags) 
  end

  it 'original returned if no escaping required' do
    str = 'foobar'
    Walters.escape_javascript(str).should equal str
  end

  it 'input must be utf8' do
    lambda { Walters.escape_javascript('dont </close> tags'.encode('ISO-8859-1')) }.should raise_error(Encoding::CompatibilityError)
  end
  
  it 'return value has same encoding as input' do
    str = "dont </close> tags"
    Walters.escape_javascript(str).encoding.should == Encoding.find('UTF-8') 
  end
end
