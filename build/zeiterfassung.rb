#!/usr/bin/ruby

require 'date'

VALID_TIME = /^\d?\d:\d\d$/
VALID_EVENT = /^(-?\d+(?:\.\d+)?)h\s*(.*)$/
VALID_SPAN = /^(\d?\d:\d\d\s*-\s*\d\d:\d\d)\s*(.*)$/

TIME_FORMAT = /-/


def convert_to_hours(time)
  # time is 12:34
  unless time.match VALID_TIME
    raise "Invalid time #{time}"
  end
  
  hour, minute = time.split(":").map{|s| s.to_i }
  
  return hour + (minute.to_f / 60)
end

def get_time_span(time) 
  # 12:00-13:30
  times = time.split(/\s*-\s*/);
  
  if times.length != 2
    raise "Ungültiges Zeitformat #{record}"
  end
  
  from, to = times.map{|t| convert_to_hours(t) }

  return to - from
end


project_start = Date.parse("2007.09.13")

entries = []
stats_week = Hash.new(0)


STDIN.each do |line|
  begin
    if line.match /END/
      break
    end
    
    (week_s, weekday_s, times) = line.split("|").collect{|e| e.strip }

    #date = Date.parse(date_s)
    
    week = week_s.to_i
    
    today = 0.0
    
    comment, hours = nil, nil
    times.split(",").collect{|x| x.strip}.each do |time| 
      if event = VALID_EVENT.match(time)
        comment = event[2]
        hours = event[1].to_f
      elsif span_match = VALID_SPAN.match(time)
        span = span_match[1]
        comment = span_match[2]
        hours = get_time_span(span)
      else
        raise "ups: #{time.inspect}"
      end
      
      if hours.nil?
        raise "oups, hours is nil?"
      end
      
      if comment.match(/:/)
        raise "colon in comment: #{comment.inspect}"
      end
      #puts " - #{comment} = #{hours}"
      today += hours
    end
    
    puts today
 
  rescue => e
    puts line
    raise e
  end
  
  stats_week[week] += today
  entries << [week, week.to_s + " " +   weekday_s, today, comment]
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