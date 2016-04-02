package com.abcdabcd987.compiler2016.FrontEnd;

import com.abcdabcd987.compiler2016.AST.Program;
import com.abcdabcd987.compiler2016.Parser.MillLexer;
import com.abcdabcd987.compiler2016.Parser.MillParser;
import com.abcdabcd987.compiler2016.Symbol.GlobalSymbolTable;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by abcdabcd987 on 2016-04-02.
 */
@RunWith(Parameterized.class)
public class SemanticCheckerTest {
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Collection<Object[]> params = new ArrayList<>();
        for (File f : new File("testcase/semantic/").listFiles()) {
            if (f.isFile() && f.getName().endsWith(".mill")) {
                params.add(new Object[] { "testcase/semantic/" + f.getName() });
            }
        }
        return params;
    }

    private String filename;

    public SemanticCheckerTest(String filename) {
        this.filename = filename;
    }

    @Test
    public void testAST() throws IOException {
        System.out.println(filename);

        InputStream is = new FileInputStream(filename);
        ANTLRInputStream input = new ANTLRInputStream(is);
        MillLexer lexer = new MillLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MillParser parser = new MillParser(tokens);
        parser.setErrorHandler(new BailErrorStrategy());

        ParseTree tree = parser.program();
        ParseTreeWalker walker = new ParseTreeWalker();
        ASTBuilder astBuilder = new ASTBuilder();
        walker.walk(astBuilder, tree);
        Program program = astBuilder.getProgram();
        ASTPrintVisitor printer = new ASTPrintVisitor();

        CompilationError ce = new CompilationError();
        GlobalSymbolTable sym = new GlobalSymbolTable();
        StructSymbolScanner structSymbolScanner = new StructSymbolScanner(sym, ce);
        program.accept(structSymbolScanner);
        StructFunctionDeclarator structFunctionDeclarator = new StructFunctionDeclarator(sym, ce);
        program.accept(structFunctionDeclarator);
        SemanticChecker semanticChecker = new SemanticChecker(sym, ce);
        program.accept(semanticChecker);
        program.accept(printer);
    }
}
