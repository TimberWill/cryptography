package com.whl.policy;

import it.unisa.dia.gas.jpbc.Element;
import jnr.ffi.Struct;

import java.util.*;

/**
 * 作者：whl
 * 日期：2023-01-08 15:36
 * 描述：
 */
public class AccessStructure {
    private Map<Integer,String> rho;//ρ映射
    private List<List<MatrixElement>> A;//矩阵
    private TreeNode policyTree;//树
    private int partsIndex;

    private Map<String,String> Wfi;//关键字值

    public Map<Integer, String> getRhos() {
        return rho;
    }

    public void setRho(Map<Integer, String> rho) {
        this.rho = rho;
    }

    public List<List<MatrixElement>> getA() {
        return A;
    }

    public void setA(List<List<MatrixElement>> a) {
        A = a;
    }



    public enum MatrixElement{
        MINUS_ONE,
        ZERO,
        ONE
    }

    private AccessStructure(){
        A = new ArrayList<>();
        rho = new HashMap<>();
    }


    public static void dealWfi(Map<Integer, String> rho){
        for (Map.Entry<Integer, String> entry : rho.entrySet()) {

        }
    }

    //构造访问结构（由访问策略-》生成树-》生成矩阵）
    public static AccessStructure buildFromPolicy(String policy) {
        AccessStructure aRho = new AccessStructure();

        aRho.generateTree(policy);

        aRho.generateMatrix();

        return aRho;
    }

    public String rho(int i) {
        return rho.get(i);
    }



    private void generateTree(String policy) {
        String[] policyParts;
        partsIndex = -1;

        // policy has infix notation if logic operators aren't at the beginning of string
        if (!(policy.toLowerCase().startsWith("and") || policy.toLowerCase().startsWith("or"))) {
            policy = policy.replace("(", "( ").replace(")", " )");
            policyParts = infixNotationToPolishNotation(policy.split("\\s+"));
        } else {
            policyParts = policy.split("\\s+");
        }
        policyTree = generateTree(policyParts);
    }

    private TreeNode generateTree(String[] policyParts) {
        partsIndex++;

        String policyAtIndex = policyParts[partsIndex];
        TreeNode node = generateNodeAtIndex(policyAtIndex);

        if (node instanceof InternalNode) {
            ((InternalNode) node).setLeft(generateTree(policyParts));
            ((InternalNode) node).setRight(generateTree(policyParts));
        }

        return node;
    }
    //根据policy的index生成node
    private TreeNode generateNodeAtIndex(String policyAtIndex) {
        switch (policyAtIndex) {
            case "and":
                return new AndGate();
            case "or":
                return new OrGate();
            default:
                return new Attribute(policyAtIndex);
        }
    }
    public List<Integer> getIndexesList(Collection<String> pKeys) {
        // initialize
        Queue<TreeNode> queue = new LinkedList<TreeNode>();
        queue.add(policyTree);

        while (!queue.isEmpty()) {
            TreeNode t = queue.poll();

            if (t instanceof Attribute) {
                t.setSat(pKeys.contains(t.getName()) ? 1 : -1);
            } else if (t instanceof InternalNode) {
                t.setSat(0);
                queue.add(((InternalNode) t).getLeft());
                queue.add(((InternalNode) t).getRight());
            }
        }

        // find if satisfiable
        if (!findIfSAT(policyTree))
            return null;

        // populate the list
        List<Integer> list = new LinkedList<Integer>();
        queue.add(policyTree);
        while (!queue.isEmpty()) {
            TreeNode t = queue.poll();

            if (1 == t.getSat()) {
                if (t instanceof AndGate) {
                    queue.add(((AndGate) t).getLeft());
                    queue.add(((AndGate) t).getRight());
                } else if (t instanceof OrGate) {
                    if (1 == ((OrGate) t).getLeft().getSat()) {
                        queue.add(((OrGate) t).getLeft());
                    } else if (1 == ((OrGate) t).getRight().getSat()) {
                        queue.add(((OrGate) t).getRight());
                    }
                } else if (t instanceof Attribute) {
                    list.add(((Attribute) t).getX());
                }
            }
        }

        // return
        return list;
    }

    private boolean findIfSAT(TreeNode node) {
        if (node instanceof Attribute)
            return 1 == node.getSat();
        else {
            boolean b;
            if (node instanceof AndGate) {
                b = findIfSAT(((AndGate) node).getLeft());
                b &= findIfSAT(((AndGate) node).getRight());
            } else if (node instanceof OrGate) {
                b = findIfSAT(((OrGate) node).getLeft());
                b |= findIfSAT(((OrGate) node).getRight());
            } else
                throw new IllegalArgumentException("Unknown node type");
            node.setSat(b ? 1 : -1);
            return b;
        }
    }

    //生成矩阵
    private void generateMatrix() {
        int c = computeLabels(policyTree);

        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(policyTree);

        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();

            if (node instanceof InternalNode) {
                queue.add(((InternalNode) node).getLeft());
                queue.add(((InternalNode) node).getRight());
            } else {
                rho.put(A.size(), node.getName());
                ((Attribute) node).setX(A.size());
                List<MatrixElement> Ax = new ArrayList<>(c);

                for (int i = 0; i < node.getLabel().length(); i++) {
                    switch (node.getLabel().charAt(i)) {
                        case '0':
                            Ax.add(MatrixElement.ZERO);
                            break;
                        case '1':
                            Ax.add(MatrixElement.ONE);
                            break;
                        case '*':
                            Ax.add(MatrixElement.MINUS_ONE);
                            break;
                    }
                }

                while (c > Ax.size())
                    Ax.add(MatrixElement.ZERO);
                A.add(Ax);
            }
        }
    }
    //计算tree的label
    private int computeLabels(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        StringBuilder sb = new StringBuilder();
        int c = 1;

        root.setLabel("1");
        queue.add(root);

        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();

            if (node instanceof Attribute)
                continue;

            if (node instanceof OrGate) {
                ((OrGate) node).getLeft().setLabel(node.getLabel());
                queue.add(((OrGate) node).getLeft());
                ((OrGate) node).getRight().setLabel(node.getLabel());
                queue.add(((OrGate) node).getRight());
            } else if (node instanceof AndGate) {
                sb.delete(0, sb.length());

                sb.append(node.getLabel());

                while (c > sb.length())
                    sb.append('0');
                sb.append('1');
                ((AndGate) node).getLeft().setLabel(sb.toString());
                queue.add(((AndGate) node).getLeft());

                sb.delete(0, sb.length());

                while (c > sb.length())
                    sb.append('0');
                sb.append('*');

                ((AndGate) node).getRight().setLabel(sb.toString());
                queue.add(((AndGate) node).getRight());

                c++;
            }
        }

        return c;
    }

    //【列数】
    public int getL() {
        return A.get(0).size();
    }
    //【行数】
    public int getN() {
        return A.size();
    }
    //矩阵A的某一行
    public List<MatrixElement> getRow(int row) {
        return A.get(row);
    }

    //将matrix改为string
    public String getMatrixAsString() {
        StringBuilder sb = new StringBuilder(2*getN() + getL()*getN());
        for (int x = 0; x < A.size(); x++) {
            List<MatrixElement> Ax = A.get(x);
            sb.append(String.format("%s: [", rho.get(x)));
            for (MatrixElement aAx : Ax) {
                switch (aAx) {
                    case ONE:
                        sb.append("  1");
                        break;
                    case MINUS_ONE:
                        sb.append(" -1");
                        break;
                    case ZERO:
                        sb.append("  0");
                        break;
                }
            }
            sb.append("]\n");
        }
        sb.delete(sb.length() - 1, sb.length());
        return sb.toString();
    }
    //将字符串数组中字符串 反向颠倒次序
    private String[] infixNotationToPolishNotation(String[] policy) {
        Map<String, Integer> precedence = new HashMap<>();
        precedence.put("and", 2);
        precedence.put("or", 1);
        precedence.put("(", 0);

        Stack<String> rpn = new Stack<String>(); //rpn stands for Reverse Polish Notation
        Stack<String> operators = new Stack<String>();

        for (String token : policy) {
            if (token.equals("(")) {
                operators.push(token);
            } else if (token.equals(")")) {
                while (!operators.peek().equals("(")) {
                    rpn.add(operators.pop());
                }
                operators.pop();
            } else if (precedence.containsKey(token)) {
                while (!operators.empty() && precedence.get(token) <= precedence.get(operators.peek())) {
                    rpn.add(operators.pop());
                }
                operators.push(token);
            } else {
                rpn.add(token);
            }
        }
        while (!operators.isEmpty()) {
            rpn.add(operators.pop());
        }

        // reversing the result to obtain Normal Polish Notation
        List<String> polishNotation = new ArrayList<String>(rpn);
        Collections.reverse(polishNotation);
        return polishNotation.toArray(new String[] {});
    }


    public static void main(String[] args) {
//        infixNotationToPolishNotation()
        AccessStructure structure = new AccessStructure();
        String[] hah = {"A and B or sd","B and D","k or l"};
        String[] result = structure.infixNotationToPolishNotation(hah);
        for (String s : result) {
            System.out.println(s);
        }

        List<Element> list = new ArrayList<>();
//        list = {{1,2,3},{4,5,6},{7,8,9}};
    }

}
