//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.github.gumtreediff.tree;

import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;
import com.github.gumtreediff.tree.TreeUtils;
import com.github.gumtreediff.tree.hash.HashUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Map.Entry;

public abstract class AbstractTree implements ITree {
    protected int id;
    protected ITree parent;
    protected List<ITree> children;
    protected int height;
    protected int size;
    protected int depth;
    protected int hash;
    protected boolean matched;
    public String additionalLabel = "";

    public AbstractTree() {
    }

    public boolean areDescendantsMatched() {
        Iterator var1 = this.getDescendants().iterator();

        ITree c;
        do {
            if(!var1.hasNext()) {
                return true;
            }

            c = (ITree)var1.next();
        } while(c.isMatched());

        return false;
    }

    public int getChildPosition(ITree child) {
        return this.getChildren().indexOf(child);
    }

    public ITree getChild(int position) {
        return (ITree)this.getChildren().get(position);
    }

    public String getChildrenLabels() {
        StringBuffer b = new StringBuffer();
        Iterator var2 = this.getChildren().iterator();

        while(var2.hasNext()) {
            ITree child = (ITree)var2.next();
            if(!"".equals(child.getLabel())) {
                b.append(child.getLabel() + " ");
            }
        }

        return b.toString().trim();
    }

    public int getDepth() {
        return this.depth;
    }

    public List<ITree> getDescendants() {
        List trees = TreeUtils.preOrder(this);
        trees.remove(0);
        return trees;
    }

    public int getHash() {
        return this.hash;
    }

    public int getHeight() {
        return this.height;
    }

    public int getId() {
        return this.id;
    }

    public boolean hasLabel() {
        return !"".equals(this.getLabel());
    }

    public List<ITree> getLeaves() {
        ArrayList leafs = new ArrayList();
        Iterator var2 = this.getTrees().iterator();

        while(var2.hasNext()) {
            ITree t = (ITree)var2.next();
            if(t.isLeaf()) {
                leafs.add(t);
            }
        }

        return leafs;
    }

    public ITree getParent() {
        return this.parent;
    }

    public void setParent(ITree parent) {
        this.parent = parent;
    }

    public List<ITree> getParents() {
        ArrayList parents = new ArrayList();
        if(this.getParent() == null) {
            return parents;
        } else {
            parents.add(this.getParent());
            parents.addAll(this.getParent().getParents());
            return parents;
        }
    }

    public String getShortLabel() {
        String lbl = this.getLabel();
        return lbl.substring(0, Math.min(50, lbl.length()))+additionalLabel;
    }

    public int getSize() {
        return this.size;
    }

    public List<ITree> getTrees() {
        return TreeUtils.preOrder(this);
    }

    private String indent(ITree t) {
        StringBuffer b = new StringBuffer();

        for(int i = 0; i < t.getDepth(); ++i) {
            b.append("\t");
        }

        return b.toString();
    }

    public boolean isClone(ITree tree) {
        return this.getHash() != tree.getHash()?false:this.toStaticHashString().equals(tree.toStaticHashString());
    }

    public boolean isCompatible(ITree t) {
        return this.getType() == t.getType();
    }

    public boolean isLeaf() {
        return this.getChildren().size() == 0;
    }

    public boolean isMatchable(ITree t) {
        return this.isCompatible(t) && !this.isMatched() && !t.isMatched();
    }

    public boolean isMatched() {
        return this.matched;
    }

    public boolean isRoot() {
        return this.getParent() == null;
    }

    public boolean isSimilar(ITree t) {
        return !this.isCompatible(t)?false:this.getLabel().equals(t.getLabel());
    }

    public Iterable<ITree> preOrder() {
        return new Iterable() {
            public Iterator<ITree> iterator() {
                return TreeUtils.preOrderIterator(AbstractTree.this);
            }
        };
    }

    public Iterable<ITree> postOrder() {
        return new Iterable() {
            public Iterator<ITree> iterator() {
                return TreeUtils.postOrderIterator(AbstractTree.this);
            }
        };
    }

    public Iterable<ITree> breadthFirst() {
        return new Iterable() {
            public Iterator<ITree> iterator() {
                return TreeUtils.breadthFirstIterator(AbstractTree.this);
            }
        };
    }

    public int positionInParent() {
        ITree p = this.getParent();
        return p == null?-1:p.getChildren().indexOf(this);
    }

    public void refresh() {
        TreeUtils.computeSize(this);
        TreeUtils.computeDepth(this);
        TreeUtils.computeHeight(this);
        HashUtils.DEFAULT_HASH_GENERATOR.hash(this);
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void setHash(int digest) {
        this.hash = digest;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMatched(boolean matched) {
        this.matched = matched;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String toStaticHashString() {
        StringBuffer b = new StringBuffer();
        b.append("[(");
        b.append(this.toShortString());
        Iterator var2 = this.getChildren().iterator();

        while(var2.hasNext()) {
            ITree c = (ITree)var2.next();
            b.append(c.toStaticHashString());
        }

        b.append(")]");
        return b.toString();
    }

    public String toString() {
        System.err.println("This method should currently not be used (please use toShortString())");
        return this.toShortString();
    }

    public String toShortString() {
        return String.format("%d%s%s%s", new Object[]{Integer.valueOf(this.getType()), "@@", this.getLabel(), additionalLabel});
    }

    public String toTreeString() {
        StringBuffer b = new StringBuffer();
        Iterator var2 = TreeUtils.preOrder(this).iterator();

        while(var2.hasNext()) {
            ITree t = (ITree)var2.next();
            b.append(this.indent(t) + t.toShortString() + "\n");
        }

        return b.toString();
    }

    public String toPrettyString(TreeContext ctx) {
        return this.hasLabel()?ctx.getTypeLabel(this) + ": " + this.getLabel():ctx.getTypeLabel(this);
    }

    protected static class EmptyEntryIterator implements Iterator<Entry<String, Object>> {
        protected EmptyEntryIterator() {
        }

        public boolean hasNext() {
            return false;
        }

        public Entry<String, Object> next() {
            throw new NoSuchElementException();
        }
    }

    public static class FakeTree extends AbstractTree {
        public FakeTree(ITree... trees) {
            this.children = new ArrayList(trees.length);
            this.children.addAll(Arrays.asList(trees));
        }

        private RuntimeException unsupportedOperation() {
            return new UnsupportedOperationException("This method should not be called on a fake tree");
        }

        public void addChild(ITree t) {
            throw this.unsupportedOperation();
        }

        public ITree deepCopy() {
            throw this.unsupportedOperation();
        }

        public List<ITree> getChildren() {
            return this.children;
        }

        public String getLabel() {
            return "";
        }

        public int getLength() {
            return this.getEndPos() - this.getPos();
        }

        public int getPos() {
            return ((ITree)Collections.min(this.children, (t1, t2) -> {
                return t2.getPos() - t1.getPos();
            })).getPos();
        }

        public int getEndPos() {
            return ((ITree)Collections.max(this.children, (t1, t2) -> {
                return t2.getPos() - t1.getPos();
            })).getEndPos();
        }

        public int getType() {
            return -1;
        }

        public void setChildren(List<ITree> children) {
            throw this.unsupportedOperation();
        }

        public void setLabel(String label) {
            throw this.unsupportedOperation();
        }

        public void setLength(int length) {
            throw this.unsupportedOperation();
        }

        public void setParentAndUpdateChildren(ITree parent) {
            throw this.unsupportedOperation();
        }

        public void setPos(int pos) {
            throw this.unsupportedOperation();
        }

        public void setType(int type) {
            throw this.unsupportedOperation();
        }

        public String toPrettyString(TreeContext ctx) {
            return "FakeTree";
        }

        public Object getMetadata(String key) {
            return null;
        }

        public Object setMetadata(String key, Object value) {
            return null;
        }

        public Iterator<Entry<String, Object>> getMetadata() {
            return new AbstractTree.EmptyEntryIterator();
        }
    }
}
