import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Naga on 20-07-2017.
 */
public class GetCorpus {
    private static Map<String, JSONArray>  productsMap = new HashMap<>();
    private static Map<String, String> productCorpusMap = new HashMap<>();
    public GetCorpus(){

        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("data/significantWords.txt")));
            String line = "";
            List<String> words = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                words.add(line);
                JSONArray array = GetProducts.getProducts(line.trim());
                productsMap.put(line.trim(), array);
                productCorpusMap.put(line.trim(), flattenJsonArray(array));
            }
            br.close();
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File("data/productsCorpus.txt")));

            for (String x : productCorpusMap.keySet()) {
                bw.write(productCorpusMap.get(x));
                bw.write("\n");
            }
            bw.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("### Fetched product data using Walmart Developer API ###");
    }
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(new File("data/significantWords.txt")));
        String line="";
        List<String> words = new ArrayList<>();
        while ((line=br.readLine())!=null){
            words.add(line);
            JSONArray array = GetProducts.getProducts(line.trim());
            productsMap.put(line.trim(), array);
            productCorpusMap.put(line.trim(), flattenJsonArray(array));
        }
        br.close();
        BufferedWriter bw = new BufferedWriter(new FileWriter(new File("data/productsCorpus.txt")));

        for (String x: productCorpusMap.keySet()){
            bw.write(productCorpusMap.get(x));
            bw.write("\n");
        }
        bw.close();
    }

    public static String flattenJsonArray(JSONArray array){
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<array.length(); i++){
            JSONObject js = array.getJSONObject(i);
            sb.append(js.get("brandName") + ": ");
            sb.append(js.get("name") + ",");
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }
}
