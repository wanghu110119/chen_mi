package com.mht.modules.swust.entity;

/**
 * @author com.mhout.wzw
 * @version 1.0.0
 * @ClassName:
 * @Description:
 * @date
 **/
//二叉树节点
public class BinaryTreeNode {
    private int data;
    private BinaryTreeNode left;
    private BinaryTreeNode right;

    public BinaryTreeNode() {}

    public BinaryTreeNode(int data, BinaryTreeNode left, BinaryTreeNode right) {
        super();
        this.data = data;
        this.left = left;
        this.right = right;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public BinaryTreeNode getLeft() {
        return left;
    }

    public void setLeft(BinaryTreeNode left) {
        this.left = left;
    }

    public BinaryTreeNode getRight() {
        return right;
    }

    public void setRight(BinaryTreeNode right) {
        this.right = right;
    }
}
