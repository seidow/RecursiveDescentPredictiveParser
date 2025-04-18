
import java.util.List;


public class Parser {
    private final List<String> tokens;
    private int current = 0;

    public Parser(List<String> tokens) {
        this.tokens = tokens;
    }
    
    public void parse(){
        while (current < tokens.size()) {
            parseStatement();
        }
    }
    
    private void parseStatement() {
        if (current >= tokens.size()) {
            error("Unexpected end of input");
        }
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
            error("Unexpected token for statement: " + token);
        }
    }
    
    private void parseDeclaration() {
        parseType();
        parseIdentifier();
        parseDecl();
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
    
     private void parseType() {
        String token = consume();
        if (!isType(token)) {
            error("Expected type, found " + token);
        }
    }

    private void parseAssignment() {
        parseIdentifier();
        consume("=");
        parseExpr();
        consume(";");
    }
    
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
        consume(";");
        parseExpr();
        consume(")");
        parseStatement();
    }

    private void parseBlock() {
        consume("{");
        parseStatement();
        consume("}");
    }
    
    
    private void parseExpr() {
        parseSimpleExpr();
        parseSimpleExprPrime();
    }

    private void parseSimpleExprPrime() {
        if (isRelop(peek())) {
            parseRelop();
            parseSimpleExpr();
        }
    }

    private void parseRelop() {
        String token = consume();
        if (!(token.equals("<") || token.equals(">") || token.equals("<=") || token.equals(">=") || token.equals("==") || token.equals("!="))) {
            error("Expected relop, found " + token);
        }
    }

    private void parseSimpleExpr() {
        parseTerm();
        parseExprPrime();
    }

    private void parseExprPrime() {
        if (match("+") || match("-")) {
            String op = consume();
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
            String op = consume();
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

    private void parseIdentifier() {
        String token = consume();
        if (!isIdentifier(token)) {
            error("Expected identifier, found " + token);
        }
    }
    
    private void parseNumber() {
        String token = consume();
        if (!isNumber(token)) {
            error("Expected number, found " + token);
        }
    }

    private boolean isType(String token) {
        return token != null && (token.equals("int") || token.equals("boolean") || token.equals("String") || token.equals("char"));
    }

    private boolean isIdentifier(String token) {
        return token != null && token.length() == 1 && Character.isLowerCase(token.charAt(0));
    }

    private boolean isNumber(String token) {
        return token != null && token.length() == 1 && Character.isDigit(token.charAt(0));
    }

    private boolean isRelop(String token) {
        return token != null && (token.equals("<") || token.equals(">") || token.equals("<=") || token.equals(">=") || token.equals("==") || token.equals("!="));
    }

    private String consume() {
        if (current >= tokens.size()) {
            error("Unexpected end of input");
        }
        return tokens.get(current++);
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
    
    
    private String peek() {
        if (current >= tokens.size()) {
            return null;
        }
        return tokens.get(current);
    }
    
    private void error(String message) {
        throw new RuntimeException("Syntax error: " + message);
    }
    
}
