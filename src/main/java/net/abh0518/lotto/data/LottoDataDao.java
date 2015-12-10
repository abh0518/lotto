package net.abh0518.lotto.data;


import net.abh0518.lotto.data.model.LottoResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 1001241 on 2015. 12. 10..
 */
public class LottoDataDao {

    List<LottoResult> originData = new ArrayList<LottoResult>();

    public LottoDataDao() throws IOException, XPathExpressionException, ParseException {
        LottoDataDownloader downloader = new LottoDataDownloader();
        String html = downloader.getLottoWinDataHtml();

        Document document = Jsoup.parse(html);
        Elements eles = document.body().getElementsByTag("table");
        eles = eles.get(1).getElementsByTag("tr");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");

        int size = eles.size();
        for(int i = 2 ; i < size ; i++ ){
            Element tr = eles.get(i);
            Elements tds = tr.getElementsByTag("td");

            int tdSize = tds.size();
            int indexAdder = 0;
            if(tdSize == 20){
                indexAdder = 1;
            }

            int recurrenceIndex = 0 + indexAdder;
            int dateIndex = 1 + indexAdder;
            int winnerCountIndex = 2 + indexAdder;
            int winMoneyIndex = 3 + indexAdder;

            int firstIndex = 12 + indexAdder;
            int secondIndex = 13 + indexAdder;
            int thirdIndex = 14 + indexAdder;
            int fourthIndex = 15 + indexAdder;
            int fifthIndex = 16 + indexAdder;
            int sixthIndex = 17 + indexAdder;
            int bonusIndex = 18 + indexAdder;

            LottoResult winNumber = new LottoResult();
            winNumber.setRecurrence(Integer.parseInt(tds.get(recurrenceIndex).text()));
            winNumber.setDate(dateFormat.parse(tds.get(dateIndex).text()));
            winNumber.setWinnerCount(Integer.parseInt(tds.get(winnerCountIndex).text()));
            winNumber.setWinMoney(tds.get(winMoneyIndex).text());

            int[] numbers = { Integer.parseInt(tds.get(firstIndex).text())
                    ,Integer.parseInt(tds.get(secondIndex).text())
                    ,Integer.parseInt(tds.get(thirdIndex).text())
                    ,Integer.parseInt(tds.get(fourthIndex).text())
                    ,Integer.parseInt(tds.get(fifthIndex).text())
                    ,Integer.parseInt(tds.get(sixthIndex).text())
                    ,Integer.parseInt(tds.get(bonusIndex).text())};
            winNumber.setNumbers(numbers);

            originData.add(winNumber);
        }
    }

    public LottoResult getLottoResult(int recurrence){
        for(int i = 0 ; i < originData.size() ; i++){
            LottoResult lotto = originData.get(i);
            if(lotto.getRecurrence() == recurrence){
                return lotto;
            }
        }
        return null;
    }

    public List<LottoResult> getLottoResults(int startRecurrence, int endRecurrence){
        List<LottoResult> result = new ArrayList<LottoResult>();
        for(int i = 0 ; i < originData.size() ; i++){
            LottoResult lotto = originData.get(i);
            if(lotto.getRecurrence() >= startRecurrence
                    && lotto.getRecurrence() <= endRecurrence){
                result.add(lotto);
            }
        }
        return result;
    }

    public List<LottoResult> getLottoResults(Date startDate, Date endDate){
        List<LottoResult> result = new ArrayList<LottoResult>();
        for(int i = 0 ; i < originData.size() ; i++){
            LottoResult lotto = originData.get(i);
            long date = lotto.getDate().getTime();
            if(date >= startDate.getTime()
                    && date <= endDate.getTime()){
                result.add(lotto);
            }
        }
        return result;
    }

    public List<LottoResult> getLottoResults(){
        return originData;
    }

}
