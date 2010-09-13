#!/usr/bin/env ruby

# This will be used by some of the tools as a common interface to
# indicate which graphs to work with.
#
# The following forms are supported:
# ./tool_name -f <path_to_graphs>
# ./tool_name -f <path_to_graphs> [graph ids]
# ./tool_name -e <edges for a graph>
#
# Specific Examples:
# ./tool_name -f ../graphs/connected_graphs_7.txt
# ./tool_name -f ../graphs/connected_graphs_7.txt 3 4 5:8 12:*
# ./tool_name -f ../graphs/connected_graphs_7.txt *:15 17 19
# ./tool_name -e 0 1  0 2  0 3  1 2  1 3  2 4
#
# This file makes available the following constants and variables to the 
# tools that use it:
#   OPTIONS        - The command line options that were used in a hash
#   COMMAND_ISSUED - The command that was invoked
#   @graph_parser  - The GraphParser object that is ready to be used
#   @out           - The output stream to be used
require 'optparse'
require 'enumerator'
require 'graph_parser'

COMMAND_ISSUED = "#{$0} #{ARGV.join(' ')}"
OPTIONS        = {}

OptionParser.new do |opts|
  opts.banner  = "Usage: #{$0} -f <graph_file> [ids list (ex. 2 3:10 23:*)]\n"
  opts.banner += "Usage: #{$0} -e <list of edges (ex. 1 2  0 3  4 5)>\n"

  opts.on("-e", "--edges", "Space separated list of edges.") do
    OPTIONS[:edges] = ARGV.collect{|v| v.to_i}.enum_slice(2).to_a
  end
  
  opts.on("-f", "--infile  STRING", "The file with the graphs.") do |filepath|
    OPTIONS[:infile]      = filepath
    OPTIONS[:graph_names] = ARGV
  end
  
  opts.on("-o", "--outfile STRING", "The output file path.") do |filepath|
    OPTIONS[:outfile] = filepath
  end
end.parse!

unless OPTIONS[:infile] || OPTIONS[:edges]
  $stderr.puts "A graph source file (-f) or list of edges (-e) is required."
  exit 0
end

begin
  @graph_parser = GraphParser.new(OPTIONS[:edges],
                                  OPTIONS[:infile],
                                  OPTIONS[:graph_names])
                                  
  @out = OPTIONS[:outfile] ? File.new(OPTIONS[:outfile], 'w') : $stdout
rescue => e
  $stderr.puts e.message
  exit 0
end
