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

    public static void main(String[] args) throws IOException {
        Lexer lexer1 = new JavaLexer(new ANTLRFileStream(TEST1_PATH));
        Lexer lexer2 = new JavaLexer(new ANTLRFileStream(TEST2_PATH));

        CommonTokenStream tokens1 = new CommonTokenStream(lexer1);
        CommonTokenStream tokens2 = new CommonTokenStream(lexer2);
        JavaParser parser1 = new JavaParser(tokens1);
        JavaParser parser2 = new JavaParser(tokens2);

        JavaParser.CompilationUnitContext ctx1 = parser1.compilationUnit();
        JavaParser.CompilationUnitContext ctx2 = parser2.compilationUnit();

        ParseTreeWalker walker = new ParseTreeWalker();
        MemberDeclarationContextListener listener1 = new MemberDeclarationContextListener();
        MemberDeclarationContextListener listener2 = new MemberDeclarationContextListener();
        walker.walk(listener1, ctx1);
        walker.walk(listener2, ctx2);
        Set<JavaParser.MemberDeclarationContext> methods1 = listener1.getMethodDeclarations();
        Set<JavaParser.MemberDeclarationContext> methods2 = listener2.getMethodDeclarations();

        System.out.println(methods1.containsAll(methods2));
        System.out.println(methods2.containsAll(methods1));
    }

}
