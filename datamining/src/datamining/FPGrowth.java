/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamining;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author acer
 */
public class FPGrowth {

    HashMap<String, Integer> count = new HashMap<>();
    FPNode root = new FPNode();
    BufferedWriter bw;

    public static void main(String[] args) {
        FPGrowth fp = new FPGrowth();
    }

    public FPGrowth() {
//        preProcess();
        FPTree();
    }

    //this method sorts items within a transaction by frequency
    private List<String> sortTransactionItems(String[] transaction) {
        return Arrays.stream(transaction).distinct().sorted((t1, t2) -> count.get(t1) - count.get(t2)).collect(Collectors.toList());
    }

    private void FPTree() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("D:\\Uni\\MasterOfA.I\\Data Mining\\projects\\#3\\newData.txt")));
            String line = "";
            int counter = 0;
            while (counter++ < 10000 && (line = br.readLine()) != null) {
                String[] temp = Arrays.copyOf(line.split(" "), line.split(" ").length - 1);
                for (String value : temp) {
                    if (!value.equals("") && count.containsKey(value)) {
                        count.put(value, count.get(value) + 1);
                    } else if (!value.equals("")) {
                        count.put(value, 1);
                    }
                }
//                bw.newLine();
            }
            br.close();

            br = new BufferedReader(new FileReader(new File("D:\\Uni\\MasterOfA.I\\Data Mining\\projects\\#3\\newData.txt")));
            line = "";
            counter = 0;
            while (counter++ < 10000 && (line = br.readLine()) != null) {
                String[] temp = Arrays.copyOf(line.split(" "), line.split(" ").length - 1);
                List<String> sorted = sortTransactionItems(temp);
                buildTree(sorted);
//                bw.newLine();
                System.out.println(counter);
            }
            br.close();
            bw = new BufferedWriter(new FileWriter(new File("D:\\Uni\\MasterOfA.I\\Data Mining\\projects\\#3\\fpTree.txt")));
            printTree(root, 0);
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(FPGrowth.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void preProcess() {
        BufferedWriter bw = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("D:\\Uni\\MasterOfA.I\\Data Mining\\projects\\#3\\Dataset.txt")));
            bw = new BufferedWriter(new FileWriter(new File("D:\\Uni\\MasterOfA.I\\Data Mining\\projects\\#3\\newData.txt")));
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] temp = line.split(" ");
                List<String> out = new ArrayList<>();
                for (String value : temp) {
                    if (Integer.valueOf(value) < 10) {
                        bw.write(value + " ");
                    }
                }
                bw.newLine();
            }
            br.close();
        } catch (IOException ex) {
            Logger.getLogger(FPGrowth.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bw.close();
            } catch (IOException ex) {
                Logger.getLogger(FPGrowth.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void buildTree(List<String> temp) {
        FPNode copy = new FPNode();
        copy = root;
        for (String value : temp) {
            FPNode child = new FPNode();
            if ((child = copy.getChildWithID(Integer.valueOf(value))) != null) {
                child.counter++;
            } else {
                child = new FPNode();
                child.counter = 1;
                child.itemID = Integer.valueOf(value);
                copy.children.add(child);
            }
            copy = child;
        }
    }

    private void printTree(FPNode node, int level) throws IOException {
        for (int i = 0; i < level; i++) {
//            System.out.print("\t");
            bw.write("\t");
        }
        bw.write(String.format("(%d) : %d", node.itemID, node.counter));
        bw.newLine();
        for (FPNode temp : node.children) {
            printTree(temp, level + 1);
        }
    }
}

class FPNode {

    int itemID = -1;  // item id
    int counter = 1;  // frequency counter

    // the parent node of that node or null if it is the root
    FPNode parent = null;
    // the child nodes of that node
    List<FPNode> children = new ArrayList<>();

    FPNode() {

    }

    /**
     * Return the immediate child of this node having a given ID. If there is no
     * such child, return null;
     */
    FPNode getChildWithID(int id) {
        // for each child node
        for (FPNode child : children) {
            // if the id is the one that we are looking for
            if (child.itemID == id) {
                // return that node
                return child;
            }
        }
        // if not found, return null
        return null;
    }

    /**
     * Method for getting a string representation of this tree
     */
    public String toString(String indent) {
        StringBuilder output = new StringBuilder();
        output.append("" + itemID);
        output.append(" (count=" + counter);
        output.append(")\n");
        String newIndent = indent + "   ";
        for (FPNode child : children) {
            output.append(newIndent + child.toString(newIndent));
        }
        return output.toString();
    }

    public String toString() {
        return "" + itemID;
    }
}
