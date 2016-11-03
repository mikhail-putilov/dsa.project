package ru.innopolis.mputilov.dsap;

import lombok.extern.slf4j.Slf4j;
import org.antlr.JavaLexer;
import org.antlr.JavaParser;
import ru.innopolis.mputilov.dsap.MemberDeclarationContextListener;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.IOException;
import java.util.*;

/**
 * Created by mputilov on 07.09.16.
 */
@Slf4j
public class Main {

    public static final String TEST1_PATH = "src/main/java/ru/innopolis/mputilov/dsap/Test1.java";
    public static final String TEST2_PATH = "src/main/java/ru/innopolis/mputilov/dsap/Test2.java";
    public static final String EMPTY_PATH = "src/main/java/ru/innopolis/mputilov/dsap/Empty.java";

    public static void main(String[] args) throws IOException {
        Lexer lexer1 = new JavaLexer(new ANTLRFileStream(TEST1_PATH));
        Lexer lexer2 = new JavaLexer(new ANTLRFileStream(TEST2_PATH));
        Lexer lexer3 = new JavaLexer(new ANTLRFileStream(EMPTY_PATH));

        CommonTokenStream tokens1 = new CommonTokenStream(lexer1);
        CommonTokenStream tokens2 = new CommonTokenStream(lexer2);
        CommonTokenStream tokens3 = new CommonTokenStream(lexer3);

        JavaParser parser1 = new JavaParser(tokens1);
        JavaParser parser2 = new JavaParser(tokens2);
        JavaParser parser3 = new JavaParser(tokens3);

        JavaParser.CompilationUnitContext ctx1 = parser1.compilationUnit();
        JavaParser.CompilationUnitContext ctx2 = parser2.compilationUnit();
        JavaParser.CompilationUnitContext ctx3 = parser3.compilationUnit();

        ParseTreeWalker walker = new ParseTreeWalker();
        ImportsContextListener listener1 = new ImportsContextListener();
        ImportsContextListener listener2 = new ImportsContextListener();
        walker.walk(listener1, ctx1);
        walker.walk(listener2, ctx2);
        Set<JavaParser.ImportDeclarationContext> imports1 = listener1.getImportDeclarations();
        Set<JavaParser.ImportDeclarationContext> imports2 = listener2.getImportDeclarations();

        System.out.println(imports1.containsAll(imports2));
        System.out.println(imports2.containsAll(imports1));

        ctx3.importDeclaration().clear();
        HashSet<JavaParser.ImportDeclarationContext> union = new HashSet<>();
        union.addAll(imports1);
        union.addAll(imports2);

        ctx3.importDeclaration().addAll(union);
        System.out.println();
    }

}
