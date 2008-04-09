#!/usr/bin/ruby
# Copyright (c) 2007-2008 Pascal Hobus, Reto Schüttel, Robin Stocker

# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
# 
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
# 
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.

require "find"
require 'fileutils'

class Lizenzierer  
  LICENSE = "/*" +                                                                          "\n" +
            " * Copyright (C) 2008  Reto Schuettel, Robin Stocker" +                   "\n" +
            " *" +                                                                          "\n" +
            " * IFS Institute for Software, HSR Rapperswil, Switzerland" +                  "\n" +
            " *" +                                                                          "\n" +
            " */"  +                                                                        "\n" + 
            "" +                                                                            "\n"
            
            
  def initialize(pfad)
    @pfad = pfad
  end
  
  def lizenziere() 
    # Gehe den Verzeichnisbaum durch
    Find.find(@pfad) do |datei|
      next unless datei =~ /\.java$/    # wendet den RegExp an (=~)
      
      content = File.read(datei)
      if content.match(/Dennis Hunziker, Ueli Kistler/) 
        raise "Dennis hunziker in #{datei}"
      elsif content.match(/Reto Sch(ü|ue)ttel, Robin Stocker/)
        copyright = content.find{|line| line.match /Copyright/ }
        case copyright
        when " * Copyright (C) 2007  Reto Schuettel, Robin Stocker\n"
          n = content.sub(" * Copyright (C) 2007  Reto Schuettel, Robin Stocker",
                          " * Copyright (C) 2007-2008  Reto Schuettel, Robin Stocker")
          
          temp_write(datei, n)
        when " * Copyright (C) 2007-2008  Reto Schuettel, Robin Stocker\n"
        when " * Copyright (C) 2008  Reto Schuettel, Robin Stocker\n"
        else
          raise copyright.inspect
        end


        # if  !content.match(/Reto Sch(ü|ue)ttel, Robin Stocker/)
        #   puts "uns #{datei}"
        #   
        #   n = content.sub(/ \* Copyright \(C\) 2006, 2007  Dennis Hunziker, Ueli Kistler\s*\n/,
        #                   " * Copyright (C) 2006, 2007  Dennis Hunziker, Ueli Kistler\n" + 
        #                   " * Copyright (C) 2007  Reto Schuettel, Robin Stocker\n")
        #   
        #   temp_write(datei, n)
        #   
        # end
      else
        
        if content.match /Copyright.*IBM Corporation/
          raise "IBM: #{datei}"
        else
          puts "Installing license into #{datei}"
          temp_write(datei, LICENSE + content)
        end
      end
    end
  end
  
  def temp_write(file, content)
    tmp_file = file + ".tmp"

    if File.exist?(tmp_file)
      raise "Uuups, temp file already existed: #{tmp_file}"
    end
      
    File.open(tmp_file, "w") do |fh|
      fh.puts content
    end
    
    FileUtils.mv tmp_file, file
  end
end

lizenzierer = Lizenzierer.new("src");
lizenzierer.lizenziere

# nice :)
