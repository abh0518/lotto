package net.abh0518.lotto.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 1001241 on 2015. 12. 10..
 */
public class LottoDataDownloader {

    public String getLottoWinDataHtml() throws IOException {
        URL url = new URL("http://www.nlotto.co.kr/lotto645Confirm.do?method=allWinExel");

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        BufferedReader in;
        in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }
}
