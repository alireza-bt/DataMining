
package datamining;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author acer
 */
public class ShellFragment {

    private HashMap<String, List<Integer>> cuboids = new HashMap<>();
    private HashMap<String, List<Integer>> inverted = new HashMap<>();
    //ABCDE
    private int[][] dimensions = {{0, 1, 2, 3}, {4, 5, 6, 7}, {8, 9, 10, 11}, {12, 13, 14, 15}, {16, 17, 18, 19}};
    private int[][] ABCDimensions = {{0, 1, 2, 3}, {4, 5, 6, 7}, {8, 9, 10, 11}};
    private int[][] DEDimensions = {{12, 13, 14, 15}, {16, 17, 18, 19}};
    private String[] dims = {"A", "B", "C", "D", "E"};
    private double minSup = 900;

    public ShellFragment() {
        computeCubes();
    }

    public static <T> List<T> getCommonElements(Collection<? extends Collection<T>> collections) {

        List<T> common = new ArrayList<T>();
        if (!collections.isEmpty()) {
            Iterator<? extends Collection<T>> iterator = collections.iterator();
            common.addAll(iterator.next());
            while (iterator.hasNext()) {
                common.retainAll(iterator.next());
            }
        }
        return common;
    }

    private void computeCubes() {
        BufferedReader br = null;
        try {
			//one-time scan of file and creating 1-D inverted index
            br = new BufferedReader(new FileReader(new File("D:\\Uni\\MasterOfA.I\\Data Mining\\projects\\#2\\Dataset.txt")));
            String line;
            double aggregation = 0;
            int counter = 0;
            while ((line = br.readLine()) != null) {
                counter++;
                String[] values = line.split(" ");
//                                    List<String> temp = Arrays.stream(values).collect(Collectors.toList());
//                                    List<Integer> intTemp = temp.stream().mapToInt(Integer::valueOf).collect(supplier, accumulator, combiner);
                List<Integer> temp = Arrays.stream(values).mapToInt(Integer::valueOf).boxed().collect(Collectors.toList());
                for (int i = 0; i < dimensions.length; i++) {
                    for (int j = 0; j < dimensions[i].length; j++) {
                        if (temp.contains(dimensions[i][j])) {
                            String key = dims[i] + "" + j;
                            if (inverted != null && inverted.containsKey(key)) {
                                inverted.get(key).add(counter);
                            } else {
                                List<Integer> tempList = new ArrayList<>();
                                tempList.add(counter);
                                inverted.put(key, tempList);
                            }
                        }
                    }
                }

            }

            //remove 1-D cuboids under min sups
            Iterator<Map.Entry<String, List<Integer>>> itr = inverted.entrySet().iterator();
            while (itr.hasNext()) {
                if (itr.next().getValue().size() < 10000) {
                    itr.remove();
                }
            }

			//Materializing cuboids inside ABC fragment using min_sup = 7000
            for (int i = 0; i < 5; i++) {
                String sDim1 = (i != 4) ? "A" + i : "*";
//                sDim1 += (i == 5) ? "*" : i;
                for (int j = 0; j < 5; j++) {
                    String sDim2 = (j != 4) ? "B" + j : "*";
//                    sDim2 += (j == 5) ? "*" : j;
                    for (int k = 0; k < 5; k++) {
                        String sDim3 = (k != 4) ? "C" + k : "*";
//                        sDim3 += (k == 5) ? "*" : k;
                        String key = sDim1 + "," + sDim2 + "," + sDim3;
                        List<List<Integer>> idList = new ArrayList<>();
                        if (i < 4) {
                            idList.add(inverted.get(sDim1));
                        }
                        if (j < 4) {
                            idList.add(inverted.get(sDim2));
                        }
                        if (k < 4) {
                            idList.add(inverted.get(sDim3));
                        }
                        List<Integer> tempList = getCommonElements(idList);
                        if (tempList.size() >= 7000) {
                            cuboids.put(key, tempList);
                        }
                        System.out.println(sDim1 + " , " + sDim2 + " , " + sDim3);

                    }
                }
            }
			
			//Materializing cuboids inside DE fragment using min_sup = 7000
            for (int i = 0; i < 5; i++) {
                String sDim1 = (i != 4) ? "D" + i : "*";
                for (int k = 0; k < 5; k++) {
                    String sDim2 = (k != 4) ? "E" + k : "*";
                    String key = sDim1 + "," + sDim2;
                    List<List<Integer>> idList = new ArrayList<>();
                    if (i < 4) {
                        idList.add(inverted.get(sDim1));
                    }
                    if (k < 4) {
                        idList.add(inverted.get(sDim2));
                    }
                    List<Integer> tempList = getCommonElements(idList);
                    if (tempList.size() >= 7000) {
                        cuboids.put(key, tempList);
                    }
                    System.out.println(sDim1 + " , " + sDim2);

                }

            }
            br.close();

            System.out.println(cuboids.size());
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File("D:\\Uni\\MasterOfA.I\\Data Mining\\projects\\#2\\outputs_shell.txt")));
            for (String temp : cuboids.keySet()) {
                bw.write(temp + " -> " + cuboids.get(temp).toString());
                bw.newLine();
            }

            bw.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Cube.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(Cube.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
        }
    }

    public static void main(String[] args) {
        ShellFragment sf = new ShellFragment();
    }

}
