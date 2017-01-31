package qixyqix.com.erpguide;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by QiXiang on 31/01/2017.
 */

public class DatamallAPIHandler {
    public JSONObject callAPI(){
        URL url = null;
        JSONObject jsonObject = null;
        try {
            url = new URL("http://datamall2.mytransport.sg/ltaodataservice/ERPRates");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("AccountKey", "moaukQySQWWjgTk1hpXoMA==");

            String response = conn.getResponseMessage();

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output = "";
            String line = "";
            while ((line = br.readLine()) != null) {
                output += line;
            }

            jsonObject = new JSONObject(output);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
