package codeGenerator;

import scanner.token.Token;

public class CodeGeneratorFacade {
    private CodeGenerator codeGenerator;

    public CodeGeneratorFacade() {
        this.codeGenerator = new CodeGenerator();
    }

    public CodeGenerator getCodeGenerator() {
        return codeGenerator;
    }

    public void semanticFunction(int func, Token next) {
        getCodeGenerator().semanticFunction(func, next);
    }
}
