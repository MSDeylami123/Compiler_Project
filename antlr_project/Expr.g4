grammar Expr;

// =====================
// Parser rules
// =====================

prog
    : (externalDecl | stat)* EOF
    ;

externalDecl
    : functionDecl
    | varDecl
    ;

functionDecl
    : type ID LPAREN paramList? RPAREN block
    ;

paramList
    : param (COMMA param)*
    ;

param
    : type ID
    ;

stat
    : varDecl
    | assignStat
    | ifStat
    | whileStat
    | forStat
    | returnStat
    | exprStat
    | block
    ;

type
    : INT_TYPE
    | FLOAT_TYPE
    | CHAR_TYPE
    | VOID_TYPE
    ;

varDecl
    : type ID (ASSIGN expr)? SEMI
    ;

assignStat
    : ID ASSIGN expr SEMI
    ;

returnStat
    : RETURN expr? SEMI
    ;

ifStat
    : IF LPAREN expr RPAREN stat (ELSE stat)?
    ;

whileStat
    : WHILE LPAREN expr RPAREN stat
    ;

forUpdate
    : ID ASSIGN expr
    ;

forStat
    : FOR LPAREN (varDecl | assignStat | SEMI)
          expr? SEMI
          forUpdate?
      RPAREN stat
    ;

block
    : LBRACE stat* RBRACE
    ;

exprStat
    : expr SEMI
    ;

// =====================
// Expressions (C-like)
// =====================

expr
    : assignmentExpr
    ;

assignmentExpr
    : logicalOrExpr
    | ID ASSIGN assignmentExpr
    ;

logicalOrExpr
    : logicalAndExpr (OR logicalAndExpr)*
    ;

logicalAndExpr
    : equalityExpr (AND equalityExpr)*
    ;

equalityExpr
    : relationalExpr ((EQ | NEQ) relationalExpr)*
    ;

relationalExpr
    : additiveExpr ((GT | LT | GE | LE) additiveExpr)*
    ;

additiveExpr
    : multiplicativeExpr ((PLUS | MINUS) multiplicativeExpr)*
    ;

multiplicativeExpr
    : unaryExpr ((STAR | DIV) unaryExpr)*
    ;

unaryExpr
    : (MINUS | NOT) unaryExpr
    | postfixExpr
    ;

postfixExpr
    : primaryExpr (LPAREN argumentList? RPAREN)*
    ;

primaryExpr
    : ID
    | NUMBER
    | FLOAT
    | CHAR_LITERAL
    | LPAREN expr RPAREN
    ;

argumentList
    : expr (COMMA expr)*
    ;

// =====================
// Lexer rules
// =====================

// Keywords
IF      : 'if';
ELSE    : 'else';
WHILE   : 'while';
FOR     : 'for';
RETURN  : 'return';

// Types
INT_TYPE   : 'int';
FLOAT_TYPE : 'float';
CHAR_TYPE  : 'char';
VOID_TYPE  : 'void';

// Operators
ASSIGN  : '=';
AND     : '&&';
OR      : '||';
EQ      : '==';
NEQ     : '!=';
GT      : '>';
LT      : '<';
GE      : '>=';
LE      : '<=';
PLUS    : '+';
MINUS   : '-';
STAR    : '*';
DIV     : '/';
NOT     : '!';

// Delimiters
SEMI    : ';';
LPAREN  : '(';
RPAREN  : ')';
LBRACE  : '{';
RBRACE  : '}';
COMMA   : ',' ;

// Literals
ID          : [a-zA-Z_][a-zA-Z_0-9]* ;
FLOAT       : [0-9]+ '.' [0-9]+ ;
NUMBER      : [0-9]+ ;
CHAR_LITERAL: '\'' . '\'' ;

// Misc
COMMENT : '#' ~[\r\n]* -> skip ;
WS      : [ \t\r\n]+ -> skip ;
