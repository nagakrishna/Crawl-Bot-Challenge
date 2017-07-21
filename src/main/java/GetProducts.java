import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Naga on 19-07-2017.
 */
public class GetProducts {

    private static final String API_KEY1="ng5d9j8jzqdtdxrgp5r5pqw3";
    private static final String API_KEY2 = "wazcrsqhzcgr9ja7gccnmsq4";
    private static final String API_KEY3="7hrxdwurjedt8mqh3spft65t";
//    private static String productsUrl

    public static void main(String[] args){
        JSONArray jsonArray = getProducts("phone");
        System.out.print(jsonArray.length());
    }
    public static JSONArray getProducts(String query){
        String url = "http://api.walmartlabs.com/v1/search?apiKey=" + API_KEY2 + "&query=" + query +"&facet=on";
        JSONObject jsonObject = new JSONObject(getCall(url));
        JSONArray items = jsonObject.getJSONArray("items");
        JSONArray array = new JSONArray();
        for (int i=0; i<items.length();i++){
            JSONObject js = items.getJSONObject(i);
            JSONObject out = new JSONObject();
            out.put("itemId", js.get("itemId"));
            out.put("name", js.get("name"));
            array.put(out);
        }


        //for each item id get the brand name

        JSONObject finalObj = new JSONObject();
        JSONArray finalArr = new JSONArray();
        int min= (array.length()>2)?2:array.length();
        for (int i=0;i<min; i++){
            JSONObject temp = new JSONObject();
            String itemId = String.valueOf(array.getJSONObject(i).get("itemId"));
            String name = String.valueOf(array.getJSONObject(i).get("name"));
            String brandUrl = "http://api.walmartlabs.com/v1/items/"+itemId+"?apiKey="+API_KEY3+"&format=json";
            JSONObject jsonObject1 = new JSONObject(getCall(brandUrl));
            String brandName="";
            if(jsonObject1.has("brandName"))
                 brandName = jsonObject1.getString("brandName");
            else
                brandName = "NO BRAND";
            temp.put("brandName", brandName);
            temp.put("name", name);
            finalArr.put(temp);
        }
        return finalArr;
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
            System.out.println("receiving data from Walmart API ....");
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
}

