package com.whl.policy;

/**
 * 作者：whl
 * 日期：2023-01-08 15:48
 * 描述：
 */
public abstract class TreeNode {
    private String label;

    private int sat;

//    public boolean isAccessControlSatisfied(final String[] attributes){
//        if (!this.isLeafNode) {
//            int satisfiedChildNumber = 0;
//            for (TreeNode childNode : this.childNodes) {
//                if (childNode.isAccessControlSatisfied(attributes)) {
//                    satisfiedChildNumber++;
//                }
//            }
//            return (satisfiedChildNumber >= this.t);
//        } else {
//            for (String eachAttribute : attributes) {
//                if (this.attribute.equals(eachAttribute)) {
//                    return true;
//                }
//            }
//            return false;
//        }
//    }
    abstract String getName();

    public String getLabel() {
        return label;
    }

    public int getSat() {
        return sat;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setSat(int sat) {
        this.sat = sat;
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
        if (obj == null)
            return false;
        if (!(obj instanceof TreeNode))
            return false;
        TreeNode other = (TreeNode) obj;
        if (label == null) {
            if (other.label != null)
                return false;
        } else if (!label.equals(other.label))
            return false;
        return sat == other.sat;
    }
}
