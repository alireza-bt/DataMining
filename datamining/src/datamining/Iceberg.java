/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamining;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author acer
 */
public class MultiWayArray {

    private HashMap<String, Double> cuboids = new HashMap<>();
    //ABCDE
    private int[][] dimensions = {{0, 1, 2, 3}, {4, 5, 6, 7}, {8, 9, 10, 11}, {12, 13, 14, 15}, {16, 17, 18, 19}};
    private String[] dims = {"A", "B", "C", "D", "E"};

    public MultiWayArray() {
        readFiles();
    }

    private void readFiles() {
        BufferedReader br = null;
        try {
			//create cuboids with 0 value
            for (int dim1 = 0; dim1 < 5; dim1++) {
                //4*4 chunks in each file
                String sDim1 = String.valueOf(dim1);
                if (dim1 == 4) {
                    sDim1 = "*";
                }

                for (int dim2 = 0; dim2 < 5; dim2++) {
                    String sDim2 = String.valueOf(dim2);
                    if (dim2 == 4) {
                        sDim2 = "*";
                    }

                    for (int dim3 = 0; dim3 < 5; dim3++) {
                        String sDim3 = String.valueOf(dim3);
                        if (dim3 == 4) {
                            sDim3 = "*";
                        }

                        for (int dim4 = 0; dim4 < 5; dim4++) {
                            String sDim4 = String.valueOf(dim4);
                            if (dim4 == 4) {
                                sDim4 = "*";
                            }

                            for (int dim5 = 0; dim5 < 5; dim5++) {
                                String sDim5 = String.valueOf(dim5);
                                if (dim5 == 4) {
                                    sDim5 = "*";
                                }
                                String cuboid2 = dims[0] + sDim1 + "," + dims[1] + sDim2 + "," + dims[2] + sDim3 + "," + dims[3] + sDim4 + "," + dims[4] + sDim5;
                                cuboids.put(cuboid2, 0d);
                            }
                        }
                    }
                }
            }

			//scan data file for the number of chunks and simultaneously create the cuboids.
            for (int dim1 = 0; dim1 < 4; dim1++) {
                //4*4 chunks in each file
                for (int dim2 = 0; dim2 < 4; dim2++) {
                    for (int dim3 = 0; dim3 < 4; dim3++) {
                        for (int dim4 = 0; dim4 < 4; dim4++) {
                            for (int dim5 = 0; dim5 < 4; dim5++) {

                                br = new BufferedReader(new FileReader(new File("D:\\Uni\\MasterOfA.I\\Data Mining\\projects\\#2\\Dataset.txt")));
                                String line;
                                double aggregation = 0;
                                while ((line = br.readLine()) != null) {
                                    String[] values = line.split(" ");
//                                    List<String> temp = Arrays.stream(values).collect(Collectors.toList());
//                                    List<Integer> intTemp = temp.stream().mapToInt(Integer::valueOf).collect(supplier, accumulator, combiner);
                                    List<Integer> temp = Arrays.stream(values).mapToInt(Integer::valueOf).boxed().collect(Collectors.toList());
                                    if (temp.contains(dimensions[0][dim1]) && temp.contains(dimensions[1][dim2]) && temp.contains(dimensions[2][dim3])
                                            && temp.contains(dimensions[3][dim4]) && temp.contains(dimensions[4][dim5])) {
                                        aggregation++;
                                    }
                                }
                                for (Map.Entry<String, Double> s : cuboids.entrySet()) {
                                    if ((s.getKey().contains(dims[0] + dim1) || s.getKey().contains(dims[0] + "*"))
                                            && (s.getKey().contains(dims[1] + dim2) || s.getKey().contains(dims[1] + "*"))
                                            && (s.getKey().contains(dims[2] + dim3) || s.getKey().contains(dims[2] + "*"))
                                            && (s.getKey().contains(dims[3] + dim4) || s.getKey().contains(dims[3] + "*"))
                                            && (s.getKey().contains(dims[4] + dim5) || s.getKey().contains(dims[4] + "*"))) {
                                        cuboids.put(s.getKey(), s.getValue() + aggregation);
                                    }
                                }
                                System.out.println(dim1 + " , " + dim2 + " , " + dim3 + " , " + dim4 + " , " + dim5 + " = " + aggregation);
                                br.close();
                            }
                        }
                    }
                }
            }

            System.out.println(cuboids.size());
			//create output file using cuboids
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File("D:\\Uni\\MasterOfA.I\\Data Mining\\projects\\#2\\outputs.txt")));
            for (String temp : cuboids.keySet()) {
                bw.write(temp + " -> " + cuboids.get(temp));
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
        MultiWayArray i = new MultiWayArray();
    }
}
