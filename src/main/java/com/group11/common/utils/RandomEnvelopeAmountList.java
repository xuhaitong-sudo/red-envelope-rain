package com.group11.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 带上下限的红包分配算法（原文所有的 int 都改成了 long）
 * 见 https://www.cnblogs.com/hanganglin/p/6496422.html
 *
 * @author Xu Haitong
 * @since 2021/11/7 11:10
 */
public class RandomEnvelopeAmountList {
    /**
     * 生成红包一次分配结果
     *
     * @param totalBonus 总红包量
     * @param totalNum   总份数
     * @param rdMin      单个红包金额下限
     * @param rdMax      单个红包金额上限
     * @return total 个红包金额的 List
     */
    public static List<Long> createAmountList(Long totalBonus, Long totalNum, Long rdMin, Long rdMax) {
        Long sendedBonus = 0L;
        Long sendedNum = 0L;
        List<Long> bonusList = new ArrayList<>();
        while (sendedNum < totalNum) {
            Long bonus = randomBonusWithSpecifyBound(totalBonus, totalNum, sendedBonus, sendedNum, rdMin, rdMax);
            bonusList.add(bonus);
            sendedNum++;
            sendedBonus += bonus;
        }
        return bonusList;
    }

    /**
     * 返回一次抽奖在指定中奖概率下是否中奖
     *
     * @param rate 中奖概率
     * @return
     */
    private static boolean canReward(double rate) {
        return Math.random() <= rate;
    }

    /**
     * 返回min~max区间内随机数，含min和max
     *
     * @param min
     * @param max
     * @return
     */
    private static long getRandomVal(long min, long max) {
        return ThreadLocalRandom.current().nextLong(min, max + 1);
    }

    /**
     * 带概率偏向的随机算法，概率偏向subMin~subMax区间
     * 返回boundMin~boundMax区间内随机数（含boundMin和boundMax），同时可以指定子区间subMin~subMax的优先概率
     * 例：传入参数(10, 50, 20, 30, 0.8)，则随机结果有80%概率从20~30中随机返回，有20%概率从10~50中随机返回
     *
     * @param boundMin 边界
     * @param boundMax
     * @param subMin
     * @param subMax
     * @param subRate
     * @return
     */
    private static long getRandomValWithSpecifySubRate(long boundMin, long boundMax, long subMin, long subMax, double subRate) {
        if (canReward(subRate)) {
            return getRandomVal(subMin, subMax);
        }
        return getRandomVal(boundMin, boundMax);
    }

    /**
     * 随机分配第n个红包
     *
     * @param totalBonus  总红包量
     * @param totalNum    总份数
     * @param sendedBonus 已发送红包量
     * @param sendedNum   已发送份数
     * @param rdMin       随机下限
     * @param rdMax       随机上限
     * @return
     */
    private static Long randomBonusWithSpecifyBound(Long totalBonus, Long totalNum, Long sendedBonus,
                                                    Long sendedNum, Long rdMin, Long rdMax) {
        Long avg = totalBonus / totalNum;  // 平均值
        Long leftLen = avg - rdMin;
        Long rightLen = rdMax - avg;
        Long boundMin = 0L, boundMax = 0L;

        // 大范围设置小概率
        if (leftLen.equals(rightLen)) {
            boundMin = Math.max((totalBonus - sendedBonus - (totalNum - sendedNum - 1) * rdMax), rdMin);
            boundMax = Math.min((totalBonus - sendedBonus - (totalNum - sendedNum - 1) * rdMin), rdMax);
        } else if (rightLen.compareTo(leftLen) > 0) {
            // 上限偏离
            double bigRate = leftLen / (double) (leftLen + rightLen);
            Long standardRdMax = avg + leftLen;  // 右侧对称上限点
            Long _rdMax = canReward(bigRate) ? rdMax : standardRdMax;
            boundMin = Math.max((totalBonus - sendedBonus - (totalNum - sendedNum - 1) * standardRdMax), rdMin);
            boundMax = Math.min((totalBonus - sendedBonus - (totalNum - sendedNum - 1) * rdMin), _rdMax);
        } else {
            // 下限偏离
            double smallRate = rightLen / (double) (leftLen + rightLen);
            Long standardRdMin = avg - rightLen;  // 左侧对称下限点
            Long _rdMin = canReward(smallRate) ? rdMin : standardRdMin;
            boundMin = Math.max((totalBonus - sendedBonus - (totalNum - sendedNum - 1) * rdMax), _rdMin);
            boundMax = Math.min((totalBonus - sendedBonus - (totalNum - sendedNum - 1) * standardRdMin), rdMax);
        }

        // 已发平均值偏移修正-动态比例
        if (boundMin.equals(boundMax)) {
            return getRandomVal(boundMin, boundMax);
        }
        double currAvg = sendedNum == 0 ? (double) avg : (sendedBonus / (double) sendedNum);  // 当前已发平均值
        double middle = (boundMin + boundMax) / 2.0;
        Long subMin = boundMin, subMax = boundMax;
        // 期望值
        double exp = avg - (currAvg - avg) * sendedNum / (double) (totalNum - sendedNum);
        if (middle > exp) {
            subMax = (Long) Math.round((boundMin + exp) / 2.0);
        } else {
            subMin = (Long) Math.round((exp + boundMax) / 2.0);
        }
        Long expBound = (boundMin + boundMax) / 2;
        Long expSub = (subMin + subMax) / 2;
        double subRate = (exp - expBound) / (double) (expSub - expBound);
        return getRandomValWithSpecifySubRate(boundMin, boundMax, subMin, subMax, subRate);
    }

}
