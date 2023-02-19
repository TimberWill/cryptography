package com.whl.policy;

/**
 * 作者：whl
 * 日期：2023-01-08 19:06
 * 描述：
 */
public abstract class InternalNode extends TreeNode{
    private TreeNode left;
    private TreeNode right;

    public TreeNode getLeft() {
        return left;
    }

    public TreeNode getRight() {
        return right;
    }

    public void setLeft(TreeNode left) {
        this.left = left;
    }

    public void setRight(TreeNode right) {
        this.right = right;
    }
    //todo
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (!(obj instanceof InternalNode))
            return false;
        InternalNode other = (InternalNode) obj;
        if (left == null) {
            if (other.left != null)
                return false;
        } else if (!left.equals(other.left))
            return false;
        if (right == null) {
            return other.right == null;
        } else return right.equals(other.right);
    }
}
