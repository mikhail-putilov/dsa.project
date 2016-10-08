import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mputilov on 29/09/16.
 */
@Slf4j
public class MemberDeclarationContextListener extends JavaBaseListener {
    @Getter
    private List<JavaParser.MemberDeclarationContext> fields = new ArrayList<>();

    @Override
    public void exitMemberDeclaration(JavaParser.MemberDeclarationContext ctx) {
        log.debug(ctx.getText());
        fields.add(ctx);
    }
}
