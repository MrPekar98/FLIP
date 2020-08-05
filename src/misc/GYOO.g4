grammar GYOO;
program   : 'begin' statement+ 'end';

prog
  : setup method* EOF
  ;

setup
  : Setup thing* End Setup
  ;

method
  : action
  | event
  | function
  ;

Id     : [a-z]+ ;
NUMBER : [0-9]+ ;
WS     : [ \n\t]+ -> skip;