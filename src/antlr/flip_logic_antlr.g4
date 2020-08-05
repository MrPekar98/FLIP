grammar flip_logic_antlr;

prog
  : setup stage* EOF
  ;

setup
  : Setup Lbrace dcl* Rbrace
  ;

dcl
  : numberType? id (assign numberLiteral)? Semicolon // Common prefixes
  | cardType? id (assign cardLiteral)? Semicolon // Common prefixes
  ;

suitLiteral
  : spadesSuit
  | clubsSuit
  | heartsSuit
  | diamondsSuit
  ;

spadesSuit
  : 'spades'
  ;

clubsSuit
  : 'clubs'
  ;

heartsSuit
  : 'hearts'
  ;

diamondsSuit
  : 'diamonds'
  ;

//arrayLiteral
//  :
//  ;

playersDcl
  : Players assign Digits Semicolon
  ;

tableDcl
  : Locations assign numberLiteral Semicolon
  ;

stage
  : Stage numberLiteral Lbrace option* Rbrace
  ;

option
  : modifier? Option Lparen params? Rparen Lbrace Rbrace
  ;

params
  : type id ',' params
  | type id
  ;

expr0
  : expr1 assign expr0
  | expr1 Semicolon
  ;

expr1
  : expr2 plus expr5
  | expr2 minus expr5
  | expr2
  ;

expr2
  : expr3 multiply expr2
  | expr3 divide expr2
  | expr3 mod expr2
  | expr3
  ;

expr3
  : Lparen expr0 Rparen
  | id
  | numberLiteral
  ;

type : playerType
     | numberType
     | cardType
     | rankType
     | suitType
     ;

playerType
  : 'Player'
  ;

numberType
  : 'Number'
  ;

cardType
  : 'Card'
  ;

rankType
  : 'Rank'
  ;

suitType
  : 'Suit'
  ;

id
  : Id
  ;

modifier
  : mandatoryModifier
  | onceModifier
  ;

cardLiteral
  : Digits 'of' suitLiteral // Common prefix
  ;

numberLiteral
  : Digits
  ;

mandatoryModifier
  : 'mandatory'
  ;
onceModifier
  : 'once'
  ;

assign
  : '='
  ;

plus
  : '+'
  ;

minus
  : '-'
  ;

multiply
  : '*'
  ;

divide
  : '/'
  ;

mod
  : '%'
  ;

BlockComment : '/*' .*? '*/' -> skip;
Setup : 'Setup';
Stage : 'Stage';
Lparen : '(';
Lbrace : '{';
Rbrace : '}';
Rparen : ')';
Digits : '0'|[1-9][0-9]*;
Option : 'option';
Semicolon : ';';
Ws : [ \t\n\r]+ -> skip;

Id : [a-zA-Z$_][a-zA-Z$_0-9]*;