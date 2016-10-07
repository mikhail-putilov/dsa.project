import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by mputilov on 29/09/16.
 */
@Slf4j
public class TestJavaBaseListenerImpl extends JavaBaseListener {
    @Getter
    private List<JavaParser.FieldDeclarationContext> fields = new ArrayList<>();

    @Override
    public void exitFieldDeclaration(JavaParser.FieldDeclarationContext ctx) {
        fields.add(ctx);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestJavaBaseListenerImpl listener = (TestJavaBaseListenerImpl) o;
        return Objects.equals(fields, listener.fields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fields);
    }
}
