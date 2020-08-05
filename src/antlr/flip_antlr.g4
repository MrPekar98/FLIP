grammar flip_antlr;

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

action
  : Action id Lparen params? Rparen thing* End id
  ;

event
  : Event keyDown Lparen params? Rparen thing* End keyDown
  | Event cardClicked Lparen params? Rparen thing* End cardClicked
  | Event pileClicked Lparen params? Rparen thing* End pileClicked
  ;

function
  : Function id Lparen params? Rparen returnType? thing* End id
  ;

returnType
  : Returns type
  ;

thing
  : dcl
  | stmt
  | expr0
  ;

dcl
  : type id assign expr0
  | type id Semicolon
  ;

stmt
  : if
  | while
  ;

while
  : While Lparen expr0 Rparen thing* End While
  ;

if
  : If Lparen expr0 Rparen thing* else? End If
  ;

else
  : Else If Lparen expr0 Rparen thing* else? End Else
  | Else thing* End else
  ;

expr0
  : expr1 assign expr0
  | expr1 Semicolon
  ;

expr1
  : expr2 or expr1
  | expr2
  ;

expr2
  : expr3 and expr2
  | expr3
  ;

expr3
  : expr4 eq expr3
  | expr4 ne expr3
  | expr4
  ;

expr4
  : expr5 gt expr4
  | expr5 lt expr4
  | expr5 le expr4
  | expr5 ge expr4
  | expr5
  ;

expr5
  : expr6 plus expr5
  | expr6 minus expr5
  | expr6
  ;

expr6
  : expr7 multiply expr6
  | expr7 divide expr6
  | expr7 mod expr6
  | expr7
  ;

expr7
  : not expr7
  | expr8
  ;

expr8
  : Lparen expr0 Rparen
  | id
  | intLiteral
  | booleanLiteral
  | floatLiteral
  | stringLiteral
  | call
  ;

call    // TODO: Missing support for a.b().
        // TODO: Missing support for a(2).
  : id Lparen expr0? (Comma expr0)* Rparen
  ;

params
  : param Comma params
  | param
  ;

param
  : type id
  ;

id : Id;

type
  : intType
  | floatType
  | stackType
  | cardType
  | booleanType
  | stringType
  ;

booleanType : 'boolean';
cardType    : 'card';
stackType   : 'stack';
floatType   : 'float';
intType     : 'int';
stringType  : 'string';

stringLiteral : StringLiteral;
StringLiteral : UnterminatedStringLiteral '"';
UnterminatedStringLiteral : '"' (~["\\\r\n] | '\\' (. | EOF))*;
intLiteral     : Digit+;
floatLiteral   : Digit+'.'Digit+;
booleanLiteral : 'true' | 'false';

Digit : [0-9];

//Opperators
assign   : '=';
plus     : '+';
minus    : '-';
divide   : '/';
multiply : '*';
mod      : '%';

eq  : 'eq' | '==';
ne  : 'ne' | '!=';
lt  : 'lt' | '<';
le  : 'le' | '<=';
gt  : 'gt' | '>';
ge  : 'ge' | '>=';
not : 'not' | 'NOT' | '!';
and : 'and' | 'AND' | '&&';
or  : 'or' | 'OR' | '||';

keyDown : 'KeyDown';
pileClicked : 'PileClicked';
cardClicked : 'CardClicked';

Setup    : 'Setup';
Action   : 'Action';
Event    : 'Event';
Function : 'Function';
While    : 'while';
If       : 'if';
Else     : 'else';
End      : 'end';
Returns  : 'returns';
Void     : 'void';
Lparen   : '(';
Rparen   : ')';
Semicolon : ';';
Comma     : ',';

Ws : [ \t\n\r]+ -> skip;

Id : [a-zA-Z$_][a-zA-Z$_0-9]*;