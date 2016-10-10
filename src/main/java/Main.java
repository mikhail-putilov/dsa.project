import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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
        Set<JavaParser.MemberDeclarationContext> fields1 = listener1.getFields();
        Set<JavaParser.MemberDeclarationContext> fields2 = listener2.getFields();

        System.out.println(fields1.containsAll(fields2));
        System.out.println(fields2.containsAll(fields1));
    }

}
