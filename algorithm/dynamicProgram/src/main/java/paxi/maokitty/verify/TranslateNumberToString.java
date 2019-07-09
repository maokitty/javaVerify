package paxi.maokitty.verify;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by maokitty on 19/6/20.
 * 以12258为例
 * 在选择的时候，可以以1开始，或者12开始，然后在剩下的数字中选择
 *             Root
 *             /   \
 *            1    12
 *           /\    /\
 *          2 22  2 25
 *         /\
 *        2 25
 *        。。。。。。。。
 * 可以观察到
 * 1：有重复的部分出现
 * 2：要计算出最开始的结果，需要知道最下方是多少 //todo 待续
 */
public class TranslateNumberToString {
    private static final Logger LOG = LoggerFactory.getLogger(TranslateNumberToString.class);
    public static void main(String[] args) {
        LOG.info("total:{}",translateNum(12258));
    }
    //f(i)=f(i+1)+g(i+2)f(i+2)
    //i+1表示第i个起点 i+2  123 f(i) 表示从1开始 f(i+1)表示从2开始 f(i+2)表示从3开始

    public static int translateNum(int number){
        String snumber = String.valueOf(number);
        int length = snumber.length();
        int[] dp=new int[length];
        for (int i=length-1;i>-1;i--){
            if(i+1==length){
                dp[i]=1;
            }else{
                int v=Integer.valueOf(snumber.substring(i, i + 2));
                if (v>=10 && v<=25)
                {
                    if (i+2<length)
                    {
                        dp[i]=dp[i+1]+dp[i+2];
                    }else{
                        //自己1个，加上合并起来的
                        dp[i]=dp[i+1]+1;
                    }
                }else{
                    dp[i]=dp[i+1];
                }
            }
        }
        return dp[0];
    }
}
