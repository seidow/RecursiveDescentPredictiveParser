import java.util.List;

public class Parser {
    private final List<String> tokens;
    private int current = 0;

    public Parser(List<String> tokens) {
        this.tokens = tokens;
    }

    // ------------------------- Core Parsing Methods -------------------------
    public void parse() {
        while (current < tokens.size()) {
            parseStatement();
        }
    }

    // ------------------------- Statement Parsing -------------------------
    private void parseStatement() {
    if (current >= tokens.size()) error("Unexpected end of input");
    String token = peek();
    if (isType(token)) {
        parseDeclaration();
    } else if (isIdentifier(token)) {
        parseAssignment();
    } else if (token.equals("if")) {
        parseIfStatement();
    } else if (token.equals("while")) {
        parseWhileStatement();
    } else if (token.equals("for")) {
        parseForStatement();
    } else if (token.equals("{")) {
        parseBlock();
    } else {
        if (isNumber(token)) {
            error("Expected identifier, found number '" + token + "'");
        } else if (token.equals("float") || token.equals("double")) {
            error("Invalid type '" + token + "'. Allowed: int, boolean, String, char");
        } else {
            error("Unexpected token for statement: " + token);
        }
    }
}

    // ------------------------- Declaration Parsing -------------------------
    private void parseDeclaration() {
        parseType();        // Validates and consumes the type (e.g., "int")
        parseIdentifier();   // Validates and consumes the identifier (e.g., "x")
        parseDecl();         // Handles ";", "= <expr> ;"
    }

    private void parseDecl() {
        if (match(";")) {
            consume(";");
        } else {
            consume("=");
            parseExpr();
            consume(";");
        }
    }

    // ------------------------- Type, Identifier, Number Parsing -------------------------
    /**
     * Parses and validates a type keyword (e.g., "int", "boolean").
     * Throws an error if the token is not a valid type.
     */
    private void parseType() {
        String token = consume();
        if (!isType(token)) {
            error("Expected type, found " + token);
        }
    }

    /**
     * Parses and validates an identifier (single lowercase letter).
     * Throws an error if the token is not a valid identifier.
     */
    private void parseIdentifier() {
        String token = consume();
        if (!isIdentifier(token)) {
            error("Expected identifier, found " + token);
        }
    }

    /**
     * Parses and validates a numeric literal (one or more digits).
     * Throws an error if the token is not a valid number.
     */
    private void parseNumber() {
        String token = consume();
        if (!isNumber(token)) {
            error("Expected number, found " + token);
        }
    }

    // ------------------------- Assignment Parsing -------------------------
    private void parseAssignment() {
        parseIdentifier();
        consume("=");
        parseExpr();
        consume(";");
    }

    // ------------------------- Control Structures -------------------------
    private void parseIfStatement() {
        consume("if");
        consume("(");
        parseExpr();
        consume(")");
        parseStatement();
        parseElseStatement();
    }

    private void parseElseStatement() {
        if (match("else")) {
            consume("else");
            parseStatement();
        }
    }

    private void parseWhileStatement() {
        consume("while");
        consume("(");
        parseExpr();
        consume(")");
        parseStatement();
    }

    private void parseForStatement() {
        consume("for");
        consume("(");
        parseDeclaration();
        parseExpr();         
        consume(";");
        parseExpr();         
        consume(")");
        parseStatement();
    }

    // ------------------------- Block Parsing -------------------------
    private void parseBlock() {
        consume("{");
        parseStatement();
        consume("}");
    }

    // ------------------------- Expression Parsing -------------------------
    private void parseExpr() {
        parseSimpleExpr();
        parseSimpleExprPrime();
    }

    private void parseSimpleExprPrime() {
    if (current >= tokens.size()) return;
    String token = peek();
    if (isRelop(token)) {
        parseRelop();
        parseSimpleExpr();
    } else if (!token.equals(")") && !token.equals(";") && !token.equals("}")) {
        error("Unexpected operator in expression: " + token);
    }
}

    private void parseRelop() {
        String token = consume();
        if (!isRelop(token)) {
            error("Expected relational operator, found " + token);
        }
    }

    private void parseSimpleExpr() {
        parseTerm();
        parseExprPrime();
    }

    private void parseExprPrime() {
        if (match("+") || match("-")) {
            consume();
            parseTerm();
            parseExprPrime();
        }
    }

    private void parseTerm() {
        parseFactor();
        parseTermPrime();
    }

    private void parseTermPrime() {
        if (match("*") || match("/")) {
            consume();
            parseFactor();
            parseTermPrime();
        }
    }

    private void parseFactor() {
        if (match("(")) {
            consume("(");
            parseExpr();
            consume(")");
        } else if (isIdentifier(peek())) {
            parseIdentifier();
        } else if (isNumber(peek())) {
            parseNumber();
        } else {
            error("Unexpected factor: " + peek());
        }
    }

    // ------------------------- Token Validation -------------------------
    private boolean isType(String token) {
        return token != null && (token.equals("int") || token.equals("boolean") 
                || token.equals("String") || token.equals("char"));
    }

    private boolean isIdentifier(String token) {
        return token != null && token.length() == 1 && Character.isLowerCase(token.charAt(0));
    }

    private boolean isNumber(String token) {
        if (token == null || token.isEmpty()) return false;
        for (char c : token.toCharArray()) {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }

    private boolean isRelop(String token) {
        return token != null && (token.equals("<") || token.equals(">") 
                || token.equals("<=") || token.equals(">=") || token.equals("==") || token.equals("!="));
    }

    // ------------------------- Token Consumption -------------------------
    private String peek() {
        return (current < tokens.size()) ? tokens.get(current) : null;
    }

    private boolean match(String expected) {
        return expected.equals(peek());
    }

    private void consume(String expected) {
        if (current >= tokens.size()) {
            error("Expected '" + expected + "' but reached end of input");
        }
        String token = tokens.get(current);
        if (!expected.equals(token)) {
            error("Expected '" + expected + "', found '" + token + "'");
        }
        current++;
    }

    private String consume() {
        if (current >= tokens.size()) {
            error("Unexpected end of input");
        }
        return tokens.get(current++);
    }

    // ------------------------- Error Handling -------------------------
    private void error(String message) {
        throw new RuntimeException("Syntax error: " + message);
    }
}