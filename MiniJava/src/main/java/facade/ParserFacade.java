package facade;

import codeGenerator.CodeGenerator;
import scanner.lexicalAnalyzer;
import scanner.token.Token;

public class ParserFacade {
    private lexicalAnalyzer lexicalAnalyzer;
    private CodeGenerator codeGenerator;

    public ParserFacade() {
        this.codeGenerator = new CodeGenerator();
    }

    public scanner.lexicalAnalyzer getLexicalAnalyzer() {
        return lexicalAnalyzer;
    }

    public void setLexicalAnalyzer(java.util.Scanner sc) {
        this.lexicalAnalyzer = new lexicalAnalyzer(sc);
    }

    public CodeGenerator getCodeGenerator() {
        return codeGenerator;
    }

    public Token getNextToken() {
        return getLexicalAnalyzer().getNextToken();
    }

    public void semanticFunction(int func, Token next) {
        getCodeGenerator().semanticFunction(func, next);
    }
}