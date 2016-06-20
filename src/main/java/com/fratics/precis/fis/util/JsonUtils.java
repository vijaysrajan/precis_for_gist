package com.fratics.precis.fis.util;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

public class JsonUtils {

    public static void main(String [] args){

        Node root = new Node("root", 100);

        Node n = new Node("dim1", 1000);
        root.addNode(n);

        Node n1 = new Node("dim2", 1000);
        Node n2 = new Node("dim3", 1000);
        n.addNode(n1);
        n.addNode(n2);

        Node n3 = new Node("dim4", 1000);
        Node n4 = new Node("dim5", 1000);
        n1.addNode(n3);
        n1.addNode(n4);

        Node n5 = new Node("dim6", 1000);
        Node n6 = new Node("dim7", 1000);
        n2.addNode(n5);
        n2.addNode(n6);

        Node n7 = new Node("dim8", 1000);
        Node n8 = new Node("dim9", 1000);
        n7.addNode(n8);
        root.addNode(n7);

        Node n9 = new Node("dim10", 1000);
        Node n10 = new Node("dim11", 1000);

        root.addNode(n9);
        root.addNode(n10);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String json = gson.toJson(root);

        System.err.println("built json -->" + json);
        System.err.println();
        System.err.println();

        //JsonElement je = gson.toJsonTree(root);
        //je.getAsJsonObject().get("children")



    }

}
