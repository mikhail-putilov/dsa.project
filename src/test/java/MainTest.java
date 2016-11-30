import com.github.gumtreediff.actions.ActionGenerator;
import com.github.gumtreediff.actions.model.*;
import com.github.gumtreediff.client.Run;
import com.github.gumtreediff.gen.Generators;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.AbstractTree;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by mputilov on 06/11/16.
 */
public class MainTest {
    Set<ITree> modified = new HashSet<>();

    @Test
    public void name() throws Exception {
        Run.initGenerators();

        String basePath = "src/main/resources/Base.java";
        String aPath = "src/main/resources/A.java";
        String bPath = "src/main/resources/B.java";

        TreeContext baseTreeCtx = Generators.getInstance().getTree(basePath);
        ITree baseTree = baseTreeCtx.getRoot();
        ITree aTree = Generators.getInstance().getTree(aPath).getRoot();
        ITree bTree = Generators.getInstance().getTree(bPath).getRoot();

        Matcher baseToBMatcher = Matchers.getInstance().getMatcher(baseTree, bTree); // retrieve the default matcher
        Matcher baseToAMatcher = Matchers.getInstance().getMatcher(baseTree, aTree); // retrieve the default matcher

        baseToBMatcher.match();
        ActionGenerator baseToB_actionGeneration = new ActionGenerator(baseTree, bTree, baseToBMatcher.getMappings());
        baseToB_actionGeneration.generate();
        List<Action> baseToB_actions = baseToB_actionGeneration.getActions();


        for (ITree t : baseTree.postOrder()) {
            t.setMatched(false);
        }
        baseToAMatcher.match();
        ActionGenerator baseToA_actionGeneration = new ActionGenerator(baseTree, aTree, baseToAMatcher.getMappings());
        baseToA_actionGeneration.generate();
        List<Action> baseToA_actions = baseToA_actionGeneration.getActions();

        patch(baseToA_actions, baseTree, baseTreeCtx);
        patch(baseToB_actions, baseTree, baseTreeCtx);
        System.out.println(baseTree.toTreeString());
//        System.out.println(baseTree.toTreeString());
//        actions.forEach(action -> {
//            if (action instanceof Insert) {
//                Insert action1 = (Insert) action;
//                System.out.println("INS " + action1.getNode().toShortString());
//            }
//            System.out.println(action.toString());
//        });
//        Optional<Action> insert = actions.stream()
//                .filter(action -> action instanceof Insert)
//                .findFirst();
//        if (insert.isPresent()) {
//            Insert action = (Insert) insert.get();
//            ITree parent = action.getParent();
//            parent.
//            List<ITree> parents = action.getParent().getParents();
//            parents.forEach((x) -> {
//                System.out.println("parent:");
//                System.out.println(x.toTreeString());
//            });
//        }
//        System.out.println("1_1 to 2");
//        m.getMappings().forEach(mapping -> {
//            System.out.println(mapping.first.toShortString() + " => " + mapping.second.toShortString());
//        });
//        System.out.println("1_1 to 1_2");
//        m_1_1_2.getMappings().forEach(mapping -> {
//            System.out.println(mapping.first.toShortString() + " => " + mapping.second.toShortString());
//        });
    }

    private void patch(List<Action> baseToA_actions, ITree baseTree, TreeContext baseTreeCtx) {
        for (Action a : baseToA_actions) {
            ITree node1 = a.getNode();
            if (modified.contains(node1)) {
                System.err.println("UNRESOLVABLE CONFLICT CHANGES!");
            }
            modified.add(node1);
            if (a instanceof Insert) {
                Insert action = ((Insert) a);
                ITree node = action.getNode();
                int i = node.getParent().getChildren().indexOf(node);
                if (i > 0) {
                    ITree leftCtx = node.getParent().getChild(i - 1);
                    ITree destctx = action.getParent().getChild(action.getPosition() - 1);
                    boolean isGoodLeft = checkContext(leftCtx, destctx);
                    boolean isGoodRight = true;
                    if (i < node.getParent().getChildren().size() - 1) {
                        ITree rightCtx = node.getParent().getChild(i + 1);
                        ITree rightdestctx = action.getParent().getChild(action.getPosition() + 1);
                        isGoodRight = checkContext(rightCtx, rightdestctx);
                    }
                    if (isGoodLeft && isGoodRight) {
                        ((AbstractTree)node).additionalLabel =  "v";
                        System.err.println("RESOLVABLE CHANGES (marked as v)");
                    } else {
                        ((AbstractTree)node).additionalLabel =  "*";
                        System.err.println("CONFLICT CHANGES WITH BEST KNOWN EFFORT (marked as *!)");
                    }
                }
                node.getChildren().clear();
                action.getParent().getChildren().add(action.getPosition(), node);
            } else if (a instanceof Update) {
                Update action = ((Update) a);
                action.getNode().setLabel(action.getValue());
            } else if (a instanceof Move) {
                Move action = ((Move) a);
                ITree node = action.getNode();
//                node.setParent(action.getParent());

                boolean removed = action.getNode().getParent().getChildren().remove(action.getNode());
                action.getParent().getChildren().add(action.getPosition(), action.getNode());
            } else if (a instanceof Delete) {
                Delete action = ((Delete) a);
                action.getNode().getParent().getChildren().remove(action.getNode());
            } else throw new RuntimeException("No such action: " + a);
        }
    }

    private boolean checkContext(ITree srcCtx, ITree destCtx) {
        return srcCtx.getId() != destCtx.getId();
    }
}