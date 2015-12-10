package net.abh0518.lotto.data;

import net.abh0518.lotto.data.model.LottoResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by 1001241 on 2015. 12. 10..
 */
public class LottoStatics {

    int recurCount;
    int[] selectedCount;
    double[] selectedProbability;
    boolean includeBonusNumber;
    Random random;

    /**
     *
     * @param lottoResult
     * @param includeBonusNumber 통계에 보너스 넘버를 포함시킬지 결정
     */
    public LottoStatics(List<LottoResult> lottoResult, boolean includeBonusNumber) {
        recurCount = lottoResult.size();
        selectedCount = new int[46];
        selectedProbability = new double[46];
        this.includeBonusNumber = includeBonusNumber;
        random = new Random(new Date().getTime());

        for(int i = 0 ; i < 46 ; i++){
            selectedCount[i] = 0;
            selectedProbability[i] = 0;
        }

        for(int i = 0 ; i < lottoResult.size() ; i++){
            LottoResult lotto = lottoResult.get(i);
            int[] numbers = lotto.getNumbers();
            int numberSize = includeBonusNumber ? numbers.length : numbers.length - 1;
            for(int n = 0; n < numberSize ; n++){
                int number = numbers[n];
                selectedCount[number]++;
            }
        }

        int totalSelectCount = includeBonusNumber? recurCount * 7 : recurCount * 6;
        for(int i = 1 ; i < selectedProbability.length ; i++){
            //횟수별 뽑힐 확률을 계산한다.
            //한 회차당 두번 뽑힐 수 없으므로 자신이 뽑힌 회차의 이후 선별 횟수는 제거되어야 할거 같지만
            //그렇게 계산하려면 너무 복잡해 지므로 그냥 totalSelectCount로 퉁친다.
            selectedProbability[i] = ((double)selectedCount[i])/totalSelectCount;
        }
    }

    /**
     * 뽑힌 횟수가 많은 번호를 우선으로 선출
     * @return
     */
    public int[] generateNextNumbersHighProbabilityFirst(){
        return generateNextNumbers(true);
    }

    /**
     * 뽑힌 횟수가 낮은 번호를 우선으로 선출
     * @return
     */
    public int[] generateNextNumbersLowProbabilityFirst(){
        return generateNextNumbers(false);
    }

    private int[] generateNextNumbers(boolean highProbabilityFirst){
        List<Integer> numberPool = new ArrayList<Integer>();
        for(int i = 1 ; i < 46 ; i++){
            numberPool.add(i);
        }

        int electCount = includeBonusNumber? 7 : 6;
        int[] electedNumbers = new int[electCount];

        //6번의 번호 선출 페이즈를 돈다 (보너스 번호 포함이면 7번의 페이즈)
        for(int elect = 0 ; elect < electCount ; elect++){
            //페이즈 별로 번호 풀을 초기화 한다.
            List<Integer> phaseNumberPool = new ArrayList<Integer>();
            phaseNumberPool.addAll(numberPool);

            //페이즈 별 로또 번호 풀에 하나의 번호만 남을때 까지 선택되지 않은 순으로 번호를 쳐낸다.
            while(phaseNumberPool.size() != 1){
                for(int nIndex = 0 ; nIndex < phaseNumberPool.size() ; nIndex++ ) {
                    int number = phaseNumberPool.get(nIndex);

                    //현재 선택된 번호가 지금까지 뽑힌 확률 (낮은 순이면 역수를 취한다.)
                    double probability = highProbabilityFirst? selectedProbability[number] : 1/(1+selectedProbability[number]);

                    //현재 번호가 다음에도 뽑힐 확률을 가져온다.
                    double selectProbability = random.nextDouble();

                    if(probability < selectProbability){
                        //현재 번호가 뽑히지 않았다면 페이즈 풀에서 제거
                        phaseNumberPool.remove(nIndex);
                        nIndex--;
                    }
                }

                //생존한 번호가 없으면 해당 페이즈는 처음부터 다시!
                if(phaseNumberPool.size() == 0){
                    phaseNumberPool.addAll(numberPool);
                }
            }

            //현재 페이즈 풀에 남은 마지막 하나가 뽑힌 번호다.
            Integer selectedNumber = phaseNumberPool.get(0);
            electedNumbers[elect] = selectedNumber;

            //뽑힌 번호는 번호 풀에서 제거
            numberPool.remove(selectedNumber);
        }
        return electedNumbers;
    }
}
