package funclang;

import funclang.AST.Program;
import funclang.parser.FuncLangLexer;
import funclang.parser.FuncLangParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Lexer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Reader implements AutoCloseable {

    private final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    @SuppressWarnings("SameReturnValue")
    protected String getProgramDirectory() {
        return "src/main/java/funclang/examples/";
    }

    public Program read() throws IOException {
        String programText = readNextProgram();
        if (programText == null) {
            return null;
        } else {
            return parse(programText);
        }
    }

    public Program parse(String programText) {
        Lexer l = getLexer(CharStreams.fromString(programText));
        FuncLangParser p = getParser(new org.antlr.v4.runtime.CommonTokenStream(l));
        return p.program().ast;
    }

    protected Lexer getLexer(CharStream s) {
        return new FuncLangLexer(s);
    }

    protected FuncLangParser getParser(org.antlr.v4.runtime.CommonTokenStream s) {
        return new FuncLangParser(s);
    }

    protected String readNextProgram() throws IOException {
        System.out.print("$ ");
        String programText = br.readLine();
        if (programText == null) {
            return null;
        } else {
            return runFile(programText);
        }
    }

    protected String runFile(String programText) throws IOException {
        if (programText.startsWith("run ")) {
            programText = readFile(getProgramDirectory() + programText.substring(4));
        }
        return programText;
    }

    private String readFile(String fileName) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            return sb.toString();
        }
    }

    @Override
    public void close() throws Exception {
        this.br.close();
    }
}
