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

0.3.10
Query:
    add template replace (CTRL+ALT+SHIFT)

0.3.9
Query:
    added print to text action (Standard query plus output)
Backup
    save/load backup config
    update UI
    Be careful backup tool has been changed and doesn't fully tested

0.3.8
fixed bugs:
  "select distinct" doesn't work
  When value of a field is a Set then only the first element is shown in the results
  other small bugs