package scanner;

import scanner.token.Token;

public class ScannerFacade {
    private lexicalAnalyzer lexicalAnalyzer;

    public scanner.lexicalAnalyzer getLexicalAnalyzer() {
        return lexicalAnalyzer;
    }

    public void setLexicalAnalyzer(java.util.Scanner sc) {
        this.lexicalAnalyzer = new lexicalAnalyzer(sc);
    }

    public Token getNextToken() {
        return getLexicalAnalyzer().getNextToken();
    }
}
