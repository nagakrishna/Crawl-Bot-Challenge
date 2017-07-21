import java.io.*;
import java.util.*;

/**
 * Created by Naga on 20-07-2017.
 */
public class FinalOutput {
    private static Map<Integer, Double> cosineValues = new HashMap<>();
    private static List<String> productCorpusList = new ArrayList<>();
    public FinalOutput()  throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(new File("data/cosineSimilarityValues.txt")));
        String line="";
        while ((line=br.readLine())!=null){
            String[] str = line.replace("[","").replace("]","").split(":");
            cosineValues.put(Integer.parseInt(str[0]), Double.parseDouble(str[1]));
        }
        Map<Integer, Double> cosineValuesSorted = sortByValues(cosineValues);
        br.close();

        BufferedReader br1 = new BufferedReader(new FileReader(new File("data/patentsCorpusOrganizations.txt")));
        while ((line=br1.readLine())!=null)
            productCorpusList.add(line.trim());
        br1.close();

        BufferedWriter bw = new BufferedWriter(new FileWriter(new File("data/output.txt")));

        System.out.println("### Products and brands bases on relevance ###");
        for (Integer integer: cosineValuesSorted.keySet()){
            System.out.println(productCorpusList.get(integer));
            bw.write(productCorpusList.get(integer) + "\n");
            bw.write("\n");
            System.out.println();
        }
        bw.close();
    }

    public static <K, V extends Comparable<V>> Map<K, V> sortByValues(final Map<K, V> map) {
        Comparator<K> valueComparator =  new Comparator<K>() {
            public int compare(K k1, K k2) {
                int compare = map.get(k2).compareTo(map.get(k1));
                if (compare == 0) return 1;
                else return compare;
            }
        };
        Map<K, V> sortedByValues = new TreeMap<K, V>(valueComparator);
        sortedByValues.putAll(map);
        return sortedByValues;
    }
}
