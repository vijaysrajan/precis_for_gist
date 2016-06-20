package com.fratics.precis.fis.util;

import java.util.Vector;

public class Node {
    Node(){}

    Node(String dim, double value) {
        this.dim = dim;
        this.value = value;
    }

    public void addNode(Node node){
        children.add(node);
    }

    public String dim;
    public double value;
    public Vector<Node> children = new Vector<Node>();

}
