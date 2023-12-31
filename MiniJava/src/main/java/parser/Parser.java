package parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import Log.Log;
import codeGenerator.CodeGeneratorFacade;
import errorHandler.ErrorHandler;
import parser.action.Action;
import scanner.ScannerFacade;
import scanner.token.Token;

public class Parser {
    private List<Rule> rules;
    private Stack<Integer> parsStack;
    private ParseTable parseTable;

    public List<Rule> getRules() {
        return rules;
    }

    public Stack<Integer> getParsStack() {
        return parsStack;
    }

    public ParseTable getParseTable() {
        return parseTable;
    }

    public ScannerFacade getScannerFacade() {
        return scannerFacade;
    }

    public CodeGeneratorFacade getCodeGeneratorFacade() {
        return codeGeneratorFacade;
    }

    private ScannerFacade scannerFacade;
    private CodeGeneratorFacade codeGeneratorFacade;

    public Parser() {
        parsStack = new Stack<Integer>();
        codeGeneratorFacade = new CodeGeneratorFacade();
        scannerFacade = new ScannerFacade();
        parsStack.push(0);
        try {
            parseTable = new ParseTable(Files.readAllLines(Paths.get("src/main/resources/parseTable")).get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        rules = new ArrayList<Rule>();
        try {
            for (String stringRule : Files.readAllLines(Paths.get("src/main/resources/Rules"))) {
                getRules().add(new Rule(stringRule));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        cg = new CodeGenerator();
    }

    public void startParse(java.util.Scanner sc) {
//        lexicalAnalyzer = new lexicalAnalyzer(sc);
        getScannerFacade().setLexicalAnalyzer(sc);
//        Token lookAhead = lexicalAnalyzer.getNextToken();
        Token lookAhead = getScannerFacade().getNextToken();
        boolean finish = false;
        Action currentAction;
        while (!finish) {
            try {
                Log.print(/*"lookahead : "+*/ lookAhead.toString() + "\t" + getParsStack().peek());
//                Log.print("state : "+ parsStack.peek());
                currentAction = getParseTable().getActionTable(getParsStack().peek(), lookAhead);
                Log.print(currentAction.toString());
                //Log.print("");

                switch (currentAction.action) {
                    case shift:
                        getParsStack().push(currentAction.number);
                        lookAhead = getScannerFacade().getNextToken();

                        break;
                    case reduce:
                        Rule rule = getRules().get(currentAction.number);
                        for (int i = 0; i < rule.RHS.size(); i++) {
                            getParsStack().pop();
                        }

                        Log.print(/*"state : " +*/ getParsStack().peek() + "\t" + rule.LHS);
//                        Log.print("LHS : "+rule.LHS);
                        getParsStack().push(getParseTable().getGotoTable(getParsStack().peek(), rule.LHS));
                        Log.print(/*"new State : " + */getParsStack().peek() + "");
//                        Log.print("");
                        try {
                            getCodeGeneratorFacade().semanticFunction(rule.semanticAction, lookAhead);
                        } catch (Exception e) {
                            Log.print("Code Genetator Error");
                        }
                        break;
                    case accept:
                        finish = true;
                        break;
                    default:
                        break;
                }
                Log.print("");
            } catch (Exception ignored) {
                ignored.printStackTrace();
//                boolean find = false;
//                for (NonTerminal t : NonTerminal.values()) {
//                    if (parseTable.getGotoTable(parsStack.peek(), t) != -1) {
//                        find = true;
//                        parsStack.push(parseTable.getGotoTable(parsStack.peek(), t));
//                        StringBuilder tokenFollow = new StringBuilder();
//                        tokenFollow.append(String.format("|(?<%s>%s)", t.name(), t.pattern));
//                        Matcher matcher = Pattern.compile(tokenFollow.substring(1)).matcher(lookAhead.toString());
//                        while (!matcher.find()) {
//                            lookAhead = lexicalAnalyzer.getNextToken();
//                        }
//                    }
//                }
//                if (!find)
//                    parsStack.pop();
            }
        }
        if (!ErrorHandler.hasError) getCodeGeneratorFacade().getCodeGenerator().printMemory();
    }
}
