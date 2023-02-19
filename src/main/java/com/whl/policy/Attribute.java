package com.whl.policy;

/**
 * 作者：whl
 * 日期：2023-01-08 18:57
 * 描述：
 */
public class Attribute extends TreeNode{
    private String name;
    private int x;

    public Attribute(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
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
        if (!(obj instanceof Attribute))
            return false;
        Attribute other = (Attribute) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return x == other.x;
    }
}
