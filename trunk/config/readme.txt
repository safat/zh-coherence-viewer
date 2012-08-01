application name: zh coherence viewer
license: LGPL

How To Configure user POF

* unpack zhCoherenceViewer application
* find a folder: user-files
* copy all needed jars to it
* copy all needed pof XML files
*edit zh-pof-config.xml, add next line:
    <include>name of your pof file</include>

Changelog:

0.3.8
fixed bugs:
  "select distinct" doesn't work
  other small bugs

0.3.5
added JMX cache statistic
save/ load cohQl script
query tool: show number of loaded rows
added simple user classes viewer

0.3.4
tools.list file changed with tools.xml
added simple jmx memory report
fixed some small bugs