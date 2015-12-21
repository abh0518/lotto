package net.abh0518.lotto;

import net.abh0518.lotto.data.LottoDataDao;
import net.abh0518.lotto.data.LottoStatics;
import net.abh0518.lotto.data.model.LottoResult;

import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by 1001241 on 2015. 12. 10..
 */
public class Main {
    public static void main(String args[]) throws ParseException, XPathExpressionException, IOException {
        LottoDataDao dao = new LottoDataDao();
        List<LottoResult> list = dao.getLottoResults();
        LottoStatics statics = new LottoStatics(list, false);
        System.out.println(list.get(list.size()-1).getRecurrence()  + " ~ " + list.get(0).getRecurrence() + " 회차 데이터 사용");

        System.out.println("보너스 번호를 제외한 통계로 번호 선출");
        System.out.println(Arrays.toString(statics.generateNextNumbersHighProbabilityFirst()));
        System.out.println(Arrays.toString(statics.generateNextNumbersLowProbabilityFirst()));

        LottoStatics staticsWithBonus = new LottoStatics(list, false);
        System.out.println("보너스 번호를 포함한한 통계로 번호 선출");
        System.out.println(Arrays.toString(staticsWithBonus.generateNextNumbersHighProbabilityFirst()));
        System.out.println(Arrays.toString(staticsWithBonus.generateNextNumbersLowProbabilityFirst()));
    }
}
