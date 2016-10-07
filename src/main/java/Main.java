import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * Created by mputilov on 07.09.16.
 */
@Slf4j
public class Main {

    public static final String TEST1_PATH = "src/main/java/ru/innopolis/mputilov/dsap/Test1.java";
    public static final String TEST2_PATH = "src/main/java/ru/innopolis/mputilov/dsap/Test1.java";

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
        TestJavaBaseListenerImpl listener1 = new TestJavaBaseListenerImpl();
        TestJavaBaseListenerImpl listener2 = new TestJavaBaseListenerImpl();
        walker.walk(listener1, ctx1);
        walker.walk(listener2, ctx2);
        List<JavaParser.FieldDeclarationContext> fields = listener1.getFields();

    }

    boolean equals(JavaParser.FieldDeclarationContext ctx1, JavaParser.FieldDeclarationContext ctx2) {
        if (ctx1.getChildCount() != ctx2.getChildCount()) {
            return false;
        }
        for (int i = 0; i < ctx1.getChildCount(); i++) {
            boolean equals = Objects.equals(ctx1.getChild(i), ctx2.getChild(i));
            if (!equals) {
                return false;
            }
        }
        return true;
    }
}
