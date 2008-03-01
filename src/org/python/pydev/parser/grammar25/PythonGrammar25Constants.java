/* Generated By:JJTree&JavaCC: Do not edit this line. PythonGrammar25Constants.java */
package org.python.pydev.parser.grammar25;

public interface PythonGrammar25Constants {

  int EOF = 0;
  int SPACE = 1;
  int CONTINUATION = 4;
  int NEWLINE1 = 5;
  int NEWLINE = 6;
  int NEWLINE2 = 7;
  int CRLF1 = 12;
  int DEDENT = 14;
  int INDENT = 15;
  int TRAILING_COMMENT = 16;
  int SINGLE_LINE_COMMENT = 17;
  int LPAREN = 18;
  int RPAREN = 19;
  int LBRACE = 20;
  int RBRACE = 21;
  int LBRACKET = 22;
  int RBRACKET = 23;
  int SEMICOLON = 24;
  int COMMA = 25;
  int DOT = 26;
  int COLON = 27;
  int PLUS = 28;
  int MINUS = 29;
  int MULTIPLY = 30;
  int DIVIDE = 31;
  int FLOORDIVIDE = 32;
  int POWER = 33;
  int LSHIFT = 34;
  int RSHIFT = 35;
  int MODULO = 36;
  int NOT = 37;
  int XOR = 38;
  int OR = 39;
  int AND = 40;
  int EQUAL = 41;
  int GREATER = 42;
  int LESS = 43;
  int EQEQUAL = 44;
  int EQLESS = 45;
  int EQGREATER = 46;
  int LESSGREATER = 47;
  int NOTEQUAL = 48;
  int PLUSEQ = 49;
  int MINUSEQ = 50;
  int MULTIPLYEQ = 51;
  int DIVIDEEQ = 52;
  int FLOORDIVIDEEQ = 53;
  int MODULOEQ = 54;
  int ANDEQ = 55;
  int OREQ = 56;
  int XOREQ = 57;
  int LSHIFTEQ = 58;
  int RSHIFTEQ = 59;
  int POWEREQ = 60;
  int OR_BOOL = 61;
  int AND_BOOL = 62;
  int NOT_BOOL = 63;
  int IS = 64;
  int IN = 65;
  int LAMBDA = 66;
  int IF = 67;
  int ELSE = 68;
  int ELIF = 69;
  int WHILE = 70;
  int FOR = 71;
  int TRY = 72;
  int EXCEPT = 73;
  int DEF = 74;
  int CLASS = 75;
  int FINALLY = 76;
  int PRINT = 77;
  int PASS = 78;
  int BREAK = 79;
  int CONTINUE = 80;
  int RETURN = 81;
  int YIELD = 82;
  int IMPORT = 83;
  int FROM = 84;
  int DEL = 85;
  int RAISE = 86;
  int GLOBAL = 87;
  int EXEC = 88;
  int ASSERT = 89;
  int AS = 90;
  int WITH = 91;
  int AT = 92;
  int NAME = 93;
  int LETTER = 94;
  int DECNUMBER = 95;
  int HEXNUMBER = 96;
  int OCTNUMBER = 97;
  int FLOAT = 98;
  int COMPLEX = 99;
  int EXPONENT = 100;
  int DIGIT = 101;
  int SINGLE_STRING = 110;
  int SINGLE_STRING2 = 111;
  int TRIPLE_STRING = 112;
  int TRIPLE_STRING2 = 113;
  int SINGLE_USTRING = 114;
  int SINGLE_USTRING2 = 115;
  int TRIPLE_USTRING = 116;
  int TRIPLE_USTRING2 = 117;

  int DEFAULT = 0;
  int FORCE_NEWLINE1 = 1;
  int FORCE_NEWLINE2 = 2;
  int MAYBE_FORCE_NEWLINE_IF_EOF = 3;
  int INDENTING = 4;
  int INDENTATION_UNCHANGED = 5;
  int UNREACHABLE = 6;
  int IN_STRING11 = 7;
  int IN_STRING21 = 8;
  int IN_STRING13 = 9;
  int IN_STRING23 = 10;
  int IN_USTRING11 = 11;
  int IN_USTRING21 = 12;
  int IN_USTRING13 = 13;
  int IN_USTRING23 = 14;
  int IN_STRING1NLC = 15;
  int IN_STRING2NLC = 16;
  int IN_USTRING1NLC = 17;
  int IN_USTRING2NLC = 18;

  String[] tokenImage = {
    "<EOF>",
    "\" \"",
    "\"\\t\"",
    "\"\\f\"",
    "<CONTINUATION>",
    "<NEWLINE1>",
    "<NEWLINE>",
    "<NEWLINE2>",
    "\"\"",
    "\"\\t\"",
    "\" \"",
    "\"\\f\"",
    "<CRLF1>",
    "\"\"",
    "\"\"",
    "\"<INDENT>\"",
    "<TRAILING_COMMENT>",
    "<SINGLE_LINE_COMMENT>",
    "\"(\"",
    "\")\"",
    "\"{\"",
    "\"}\"",
    "\"[\"",
    "\"]\"",
    "\";\"",
    "\",\"",
    "\".\"",
    "\":\"",
    "\"+\"",
    "\"-\"",
    "\"*\"",
    "\"/\"",
    "\"//\"",
    "\"**\"",
    "\"<<\"",
    "\">>\"",
    "\"%\"",
    "\"~\"",
    "\"^\"",
    "\"|\"",
    "\"&\"",
    "\"=\"",
    "\">\"",
    "\"<\"",
    "\"==\"",
    "\"<=\"",
    "\">=\"",
    "\"<>\"",
    "\"!=\"",
    "\"+=\"",
    "\"-=\"",
    "\"*=\"",
    "\"/=\"",
    "\"//=\"",
    "\"%=\"",
    "\"&=\"",
    "\"|=\"",
    "\"^=\"",
    "\"<<=\"",
    "\">>=\"",
    "\"**=\"",
    "\"or\"",
    "\"and\"",
    "\"not\"",
    "\"is\"",
    "\"in\"",
    "\"lambda\"",
    "\"if\"",
    "\"else\"",
    "\"elif\"",
    "\"while\"",
    "\"for\"",
    "\"try\"",
    "\"except\"",
    "\"def\"",
    "\"class\"",
    "\"finally\"",
    "\"print\"",
    "\"pass\"",
    "\"break\"",
    "\"continue\"",
    "\"return\"",
    "\"yield\"",
    "\"import\"",
    "\"from\"",
    "\"del\"",
    "\"raise\"",
    "\"global\"",
    "\"exec\"",
    "\"assert\"",
    "\"as\"",
    "\"with\"",
    "\"@\"",
    "<NAME>",
    "<LETTER>",
    "<DECNUMBER>",
    "<HEXNUMBER>",
    "<OCTNUMBER>",
    "<FLOAT>",
    "<COMPLEX>",
    "<EXPONENT>",
    "<DIGIT>",
    "<token of kind 102>",
    "<token of kind 103>",
    "<token of kind 104>",
    "<token of kind 105>",
    "<token of kind 106>",
    "<token of kind 107>",
    "<token of kind 108>",
    "<token of kind 109>",
    "\"\\\'\"",
    "\"\\\"\"",
    "\"\\\'\\\'\\\'\"",
    "\"\\\"\\\"\\\"\"",
    "\"\\\'\"",
    "\"\\\"\"",
    "\"\\\'\\\'\\\'\"",
    "\"\\\"\\\"\\\"\"",
    "\"\\\\\\r\\n\"",
    "<token of kind 119>",
    "\"\\\\\\r\\n\"",
    "<token of kind 121>",
    "\"\\\\\\r\\n\"",
    "<token of kind 123>",
    "\"\\\\\\r\\n\"",
    "<token of kind 125>",
    "\"\"",
    "\"\"",
    "\"\"",
    "\"\"",
    "<token of kind 130>",
    "<token of kind 131>",
    "\"\\r\\n\"",
    "\"\\n\"",
    "\"\\r\"",
    "<token of kind 135>",
    "<token of kind 136>",
    "\"`\"",
  };

}
