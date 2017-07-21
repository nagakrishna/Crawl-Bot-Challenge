import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Naga on 20-07-2017.
 */
public class CompleteFlow {
    public static void main(String[] args) throws IOException {

        /*
        HTML File Parsing using JSOUP - extracted only the Field of the Invention, Summary, and Detailed Description.
        Saved filed to data/input/filteredInput.txt file
         */
        HTMLParsing("data/input.txt");

        //NLPs Tokenization, Lemmatization using Stanford NLP and significant words using Spark's CountVectorizer
        new PreProcess("data/input", "data/stopwords.txt", 2900000 ).start();

        //Getting products and brands for the significant words using Walmart Developer API
        new GetCorpus();

        //For cosine similarity making the single file having both input filtered text and products corpus details
        ClubDocs();

        /*
        To find the list of relevant products and companies - using consine simlarity of products data for each significant word.
        Dot product of each words products list with input document filtered text gives the relevant the results are.
        Used python scikit learn cosine similarity
        It takes 'data/finalDoc.txt' as input and emits cosine similarity values to 'cosineSimilarityValues.txt'
        Python code is located at Python/cosineSimilarity.py
        */
        CosineSimilarity();

        /*
        Sorted the cosine similarity values to find the most relevant products
         */
        new FinalOutput();
    }

    public static void HTMLParsing(String filePath){
        try {
            Map<String, String> map = new HashMap<>();
            File input = new File(filePath);
            Document doc = Jsoup.parse(input, "UTF-8", "http://example.com/");
            List<Elements> data = new ArrayList<>();
            Elements headings = doc.select( "heading");
            for (Element heading : headings) {
//                if (heading.tagName().toLowerCase().contains("heading"))
//                    continue; //we've reached a strong element that isn't actually a chapter
                StringBuilder sb = new StringBuilder();
                Element next = heading.nextElementSibling();
                while ((next != null)) {
                    if ((next.tag().toString().toLowerCase().contains("heading")) || (next.toString().contains("</description-of-drawings>")) || (next.toString().contains("</description>"))){
                        map.put(heading.ownText(), sb.toString());
                        sb.setLength(0);
                        break;
                    }
                    sb.append(next.text());
                    next = next.nextElementSibling();
                }
                if (!map.containsKey(heading.ownText())){
                    map.put(heading.ownText(), sb.toString());
                    sb.setLength(0);
                }
            }

            BufferedWriter bw = new BufferedWriter(new FileWriter(new File("data/input/filteredInput.txt")));
            for (String x: map.keySet()){
                //eliminating unwanted stuff
                if (!x.toLowerCase().contains("background") && !x.toLowerCase().contains("drawings"))
                    bw.write(x  + ": " + map.get(x)+" ");
            }
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("### Extracted the required text from HTML file ###");
        System.out.println();
    }

    public static void ClubDocs() throws IOException {
        BufferedReader br1 = new BufferedReader(new FileReader(new File("data/input/filteredInput.txt")));
        BufferedReader br2 = new BufferedReader(new FileReader(new File("data/productsCorpus.txt")));
        String line="";
        StringBuilder sb = new StringBuilder();
        while ((line=br1.readLine())!=null){
            sb.append(line);
        }
        sb.append("\n");
        while ((line=br2.readLine())!=null)
            sb.append(line).append("\n");
        br1.close();
        br2.close();
        BufferedWriter bw = new BufferedWriter(new FileWriter(new File("data/finalDoc.txt")));
        bw.write(sb.toString());
        bw.close();

        System.out.println("### File ready for cosine similarity computation ###");
        System.out.println();
    }

    public static void CosineSimilarity(){
        try {
            Process p = Runtime.getRuntime().exec(
                    "python Python\\cosineSimilarity.py ");
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    p.getInputStream()));
            System.out.println(in.readLine());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("### Computed cosine similarity values using scikit learn library ###");
        System.out.println();
    }
}
