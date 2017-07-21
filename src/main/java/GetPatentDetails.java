import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Naga on 20-07-2017.
 */
public class GetPatentDetails {
    private static Map<String, JSONArray> productsMap = new HashMap<>();
    private static Map<String, String> patentsCorpusMap = new HashMap<>();
    private static Map<String, String> patentTitleOrganization = new HashMap<>();
    public GetPatentDetails() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(new File("data/significantWords.txt")));
        String line = "";
        List<String> words = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            words.add(line);
            JSONArray array = getDetails(line.trim());
            productsMap.put(line.trim(), array);
            patentsCorpusMap.put(line.trim(), flattenJsonArrayAbstract(array));
            patentTitleOrganization.put(line.trim(), flattenJsonArrayOrganization(array));
        }
        br.close();
        BufferedWriter bw1 = new BufferedWriter(new FileWriter(new File("data/patentsCorpusAbstract.txt")));
        for (String x : patentsCorpusMap.keySet()) {
            bw1.write(patentsCorpusMap.get(x));
            bw1.write("\n");
        }
        bw1.close();
        BufferedWriter bw2 = new BufferedWriter(new FileWriter(new File("data/patentsCorpusOrganizations.txt")));
        for (String x : patentTitleOrganization.keySet()) {
            bw2.write(patentTitleOrganization.get(x));
            bw2.write("\n");
        }
        bw2.close();
    }
    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(new File("data/significantWords.txt")));
        String line = "";
        List<String> words = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            words.add(line);
            JSONArray array = getDetails(line.trim());
            productsMap.put(line.trim(), array);
            patentsCorpusMap.put(line.trim(), flattenJsonArrayAbstract(array));
            patentTitleOrganization.put(line.trim(), flattenJsonArrayOrganization(array));
        }
        br.close();
        BufferedWriter bw1 = new BufferedWriter(new FileWriter(new File("data/patentsCorpusAbstract.txt")));
        for (String x : patentsCorpusMap.keySet()) {
            bw1.write(patentsCorpusMap.get(x));
            bw1.write("\n");
        }
        bw1.close();

        BufferedWriter bw2 = new BufferedWriter(new FileWriter(new File("data/patentsCorpusOrganizations.txt")));
        for (String x : patentTitleOrganization.keySet()) {
            bw2.write(patentTitleOrganization.get(x));
            bw2.write("\n");
        }
        bw2.close();
//        JSONArray arr = getDetails("international");
        System.out.print("");
    }

    public static JSONArray getDetails(String word){
        String API_URL = "\n" +
                "http://www.patentsview.org/api/patents/query?q={\"_and\":[{\"_text_any\":{\"patent_abstract\":\""+word+"\"}}]}&f=[\"patent_abstract\",\"assignee_organization\",\"patent_title\"]&o={\"per_page\":5}";
        JSONObject jsonObject = new JSONObject(getCall(API_URL));
        JSONArray patents = jsonObject.getJSONArray("patents");
        JSONArray array = new JSONArray();
        for (int i=0; i<patents.length(); i++){
            JSONObject js = patents.getJSONObject(i);
            JSONObject out = new JSONObject();
            out.put("patent_abstract", js.get("patent_abstract"));
            out.put("patent_title", js.get("patent_title"));
            JSONArray jsr = js.getJSONArray("assignees");
            StringBuilder organizations = new StringBuilder();
            for (int j=0;j<jsr.length();j++){
                JSONObject js1 = jsr.getJSONObject(j);
                organizations.append((js1.get("assignee_organization")));
            }
            out.put("assignee_organization", organizations.toString());
            array.put(out);
        }
        return array;
    }

    public static String getCall(String str) {
        StringBuilder sb = new StringBuilder();
        ;
        try {
            URL url = new URL(str);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            System.out.println("receiving data from patentsview API ....");
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }
            conn.disconnect();
        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }
        return sb.toString();
    }

    public static String flattenJsonArrayAbstract(JSONArray array){
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<array.length(); i++){
            JSONObject js = array.getJSONObject(i);
            sb.append(js.get("patent_title") + ": ");
            sb.append(js.get("patent_abstract") + ",");
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    public static String flattenJsonArrayOrganization(JSONArray array){
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<array.length(); i++){
            JSONObject js = array.getJSONObject(i);
            sb.append("\"" +js.get("assignee_organization") +"\"" + ": ");
            sb.append(js.get("patent_title") + ",");
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }
}
