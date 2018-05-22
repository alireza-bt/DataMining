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
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Eclat {

    HashMap<Integer, HashMap<String, List<Integer>>> table = new HashMap<>();
    Integer MIN_SUP = 100;

    public Eclat() {
//        BufferedWriter bw = null;
        try {

            BufferedReader br = new BufferedReader(new FileReader(new File("D:\\Uni\\MasterOfA.I\\Data Mining\\projects\\#3\\newData.txt")));
            String line = "";
            HashMap<String, List<Integer>> hash = new HashMap<>();
            int counter = 0;
            while (counter <= 6000 && (line = br.readLine()) != null) {
                counter++;
                String[] temp = Arrays.copyOf(line.split(" "), line.split(" ").length);
                for (String value : temp) {
                    if (!value.equals("") && hash.containsKey(value)) {
                        hash.get(value).add(counter);
                    } else if (!value.equals("")) {
                        List<Integer> l = new ArrayList<>();
                        l.add(counter);
                        hash.put(value, l);
                    }
                }
//                bw.newLine();
            }
            br.close();
            Iterator itr = hash.entrySet().iterator();

            while (itr.hasNext()) {
                Map.Entry<String, List<Integer>> entry = (Map.Entry<String, List<Integer>>) itr.next();
                if (entry.getValue().size() < MIN_SUP) {
                    itr.remove();
                }
            }

            table.put(1, hash);

            for (int i = 2; i <= 10; i++) {
                if (!table.get(i - 1).isEmpty()) {
                    table.put(i, combine(i - 1, 1));
                } else {
                    break;
                }
                System.out.println(i);
            }
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File("D:\\Uni\\MasterOfA.I\\Data Mining\\projects\\#3\\result.txt")));
            for (HashMap<String, List<Integer>> temp : table.values()) {
                for (Map.Entry<String, List<Integer>> entry : temp.entrySet()) {
                    bw.write(entry.getKey() + " : " + entry.getValue().size());
                    bw.newLine();
                }
            }
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(FPGrowth.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void findItemsets() {

    }

    private HashMap<String, List<Integer>> combine(int i, int j) {
        Iterator itr = table.get(i).entrySet().iterator();
        HashMap<String, List<Integer>> returnVal = new HashMap<>();
        while (itr.hasNext()) {
            Map.Entry<String, List<Integer>> entry = (Map.Entry<String, List<Integer>>) itr.next();
            String[] items = entry.getKey().split(",");
            for (String temp : table.get(j).keySet()) {
                String itemset = "";
                if (i == 1 && Integer.valueOf(temp) != Integer.valueOf(items[0])) {
                    List<Integer> intersect = table.get(j).get(temp).stream().filter(entry.getValue()::contains).collect(Collectors.toList());
                    if (intersect.size() >= MIN_SUP) {
                        if (Integer.valueOf(temp) < Integer.valueOf(items[0])) {
                            itemset = temp + "," + items[0];
                        } else {
                            itemset = items[0] + "," + temp;
                        }
                        returnVal.put(itemset, intersect);
                    }
                } else if (i > 1 && !Arrays.asList(items).contains(temp)) {
                    List<Integer> intersect = entry.getValue().stream().collect(Collectors.toList());;
                    boolean frequent = true;
                    for (int k = 0; k < items.length; k++) {
                        itemset = "";
                        String[] newArray = Arrays.copyOf(items, items.length);
                        newArray[k] = temp;
                        newArray = Arrays.asList(newArray).stream().mapToInt(Integer::valueOf).sorted().boxed().map(String::valueOf).collect(Collectors.toList()).toArray(newArray);
                        for (String x : newArray) {
                            itemset += x + ",";
                        }
                        itemset = itemset.substring(0, itemset.length() - 1);
                        if (table.get(i).get(itemset) == null) {
                            frequent = false;
                            break;
                        } else {
                            intersect = table.get(i).get(itemset).stream().filter(intersect::contains).collect(Collectors.toList());
                            if (intersect.size() < MIN_SUP) {
                                frequent = false;
                                break;
                            }
                        }

                    }
                    if (frequent) {
                        List<String> newList = Arrays.stream(items).collect(Collectors.toList());
                        newList.add(temp);
                        newList = newList.stream().mapToInt(Integer::valueOf).sorted().boxed().map(String::valueOf).collect(Collectors.toList());
                        itemset = "";
                        for (String x : newList) {
                            itemset += x + ",";
                        }
                        itemset = itemset.substring(0, itemset.length()-1);
                        returnVal.put(itemset, intersect);
                    }
                }

            }
        }
        return returnVal;
    }

    public static void main(String[] args) {
        Eclat e = new Eclat();
    }
}
