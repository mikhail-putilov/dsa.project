import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by mputilov on 29/09/16.
 */
@Slf4j
public class MemberDeclarationContextListener extends JavaBaseListener {
    @Getter
    private Set<JavaParser.MemberDeclarationContext> memberDeclarations = new HashSet<>();

    @Override
    public void exitMemberDeclaration(JavaParser.MemberDeclarationContext ctx) {
        log.debug(ctx.getText());
        memberDeclarations.add(ctx);
    }
}
