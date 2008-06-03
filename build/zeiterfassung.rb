#!/usr/bin/ruby

require 'date'

VALID_TIME = /^\d\d:\d\d$/
TIME_FORMAT = /-/

def convert_to_hours(time)
  # time is 12:34
  unless time.match VALID_TIME
    raise "Invalid time #{time}"
  end
  
  hour, minute = time.split(":").map{|s| s.to_i }
  
  return hour + (minute.to_f / 60)
end


project_start = Date.parse("2007.09.13")

entries = []
stats_week = Hash.new(0)


STDIN.each do |line|
  begin
    if line.match /END/
      break
    end
    
    (blurb, date_s, hours_s, desc) = line.split("||").collect{|e| e.strip }

    if date_s.match /Datum/
      puts line
      next
    end
    
    date = Date.parse(date_s)
    
    # calculate week
    days_since_start = date - project_start
    week = (days_since_start / 7).ceil
    
    time = 0.0
    
    if hours_s.match TIME_FORMAT
      records = hours_s.split(/,/)
      
      records.map{|r| r.strip }.each do |record| 
        # 12:00-13:30
        times = record.split(/\s*-\s*/);
        
        if times.length != 2
          raise "Ungültiges Zeitformat #{record}"
        end
        
        from, to = times.map{|t| convert_to_hours(t) }

        time += (to - from)
      end
    else
      time += hours_s.to_f
    end
  rescue => e
    puts line
    raise e
  end
  
  stats_week[week] += time
  entries << [week, date, time, desc]
  puts line
end

puts
puts


puts 
puts "Statistik: (Update #{Date.today})"
puts


entries.each do |week, date, time, desc|
  puts "|| #{week}|| #{date} || #{time} || #{desc} ||"
end
puts

puts 
puts "|| Woche || Total ||"
stats_week.sort{|a,b| a <=> b}.each do |week, hours|
  puts "|| #{week} ||  #{hours} ||"
end


total = stats_week.inject(0) {|sum, e| sum + e.last}
puts
puts "Total: #{total}"
puts "H/Woche: #{total / stats_week.length}"