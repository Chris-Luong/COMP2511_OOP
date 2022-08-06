package unsw.calculator.model.tree;

/*
 * Abstract superclass of both tree elements (simple, binary)
 */
public interface TreeNode {

    public String getLabel();
  
    public abstract void infixPrint();
}