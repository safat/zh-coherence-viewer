## Plans to 0.3.10 ##
  * Query
    * Live templates
  * JMX Report
    * new report: Quadratic
  * Restore backup
    * dramatically reduced time of restoring

## 0.3.9 ##
  * Query:
    * added print to text action (Standard query plus output)
  * Backup
    * save/load backup config
    * update UI
**_Be careful backup tool has been changed and doesn't fully tested_**

## 0.3.8 ##
fixed bugs:
  * "select distinct" doesn't work
  * When value of a field is a Set then only the first element is shown in the results
  * and other

## 0.3.7 ##
  * updated Object Explorer
  * added config manager which lets add user config panels

## 0.3.5 ##
  * added JMX cache statistic
  * save/ load cohQl script
  * query tool: show number of loaded rows
  * added simple user classes viewer

## 0.3.4 ##
  * tools.list file changed with tools.xml
  * added simple jmx memory report
  * fixed some small bugs

## 0.3.3 ##
  * hide stack trace in the Query Tool Event Log
  * forced backup and restore (use loadAll and storeAll instead load and store)
  * user can add own Object viewer
  * fixed some small bugs

## 0.3.2 ##
  * removed tab: 'console'
  * updated backup tool

## 0.3.1 ##
  * added export of table output to xls and csv files
  * added expression: 'top number'
> > sample: select top 100 `*` from 'cache'
  * fixed bugs

## 0.3 ##
  * close text-dialog and object explorer dialog by ESC
  * backup tool
  * update Object Explorer to use search

## 0.2.4 ##
  * add Highlighter for table
  * add filter for table
  * add search for table (ctrl+F)
  * search in text-view window
  * history of requests

## 0.2.2 ##
  * added Object Explorer
  * updeted logon dialog