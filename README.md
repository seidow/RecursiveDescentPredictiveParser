# Mini Java Recursive Descent Parser

A recursive descent predictive parser for a subset of Java-like syntax, built according to a specified context-free grammar (CFG). This parser validates the structure of input programs based on declarative statements, control flow constructs, and expressions.

---

## Features
- **Declarations**: Variables of types `int`, `boolean`, `String`, and `char`.
- **Assignments**: Assign values to variables (e.g., `x = 5;`).
- **Control Structures**: 
  - `if`-`else` statements.
  - `while` loops.
  - `for` loops (non-standard syntax as per the CFG).
- **Blocks**: Code blocks enclosed in `{ }`.
- **Expressions**: Arithmetic and relational operations with support for parentheses.

---

## Grammar Overview
The parser adheres to the following CFG:
```ebnf
<statement> → <declaration> | <assignment> | <if-statement> | <while-statement> | <for-statement> | <block>
<declaration> → <type> <identifier> <decl>
<decl> → ";" | "=" <expr> ";"
<assignment> → <identifier> "=" <expr> ";"
<if-statement> → "if" "(" <expr> ")" <statement> <else_statement>
<else_statement> → "else" <statement> | ε
<while-statement> → "while" "(" <expr> ")" <statement>
<for-statement> → "for" "(" <declaration> <expr> ";" <expr> ";" <expr> ")" <statement>
<block> → "{" <statement> "}"
<expr> → <simple_expr> <simple_expr_prime>
<simple_expr_prime> → <relop> <simple_expr> | ε
<relop> → "<" | ">" | "<=" | ">=" | "==" | "!="
<simple_expr> → <term> <expr_prime>
<expr_prime> → "+" <term> <expr_prime> | "-" <term> <expr_prime> | ε
<term> → <factor> <term_prime>
<term_prime> → "*" <factor> <term_prime> | "/" <factor> <term_prime> | ε
<factor> → "(" <expr> ")" | <identifier> | <number>
<type> → "int" | "boolean" | "String" | "char"
<identifier> → [a-z] (single lowercase letter)
<number> → [0-9]+ (one or more digits)
