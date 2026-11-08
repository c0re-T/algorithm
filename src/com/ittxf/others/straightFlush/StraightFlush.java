package com.ittxf.others.straightFlush;

import java.util.Arrays;

public class StraightFlush {
    public static void main(String[] args) {
        // 案例一：A 作最大（皇家同花顺）
        // 输入：红桃10, 红桃J, 红桃Q, 红桃K, 红桃A
        Card[] case1 = {
                new Card(Suit.HEART, 10),
                new Card(Suit.HEART, 11),
                new Card(Suit.HEART, 12),
                new Card(Suit.HEART, 13),
                new Card(Suit.HEART, 1)
        };
        System.out.println("案例一（10-J-Q-K-A）结果: " + new Solution().isStraightFlush(case1));
        // 预期输出: true


        // 案例二：A 作最小（最小同花顺）
        // 输入：黑桃A, 黑桃2, 黑桃3, 黑桃4, 黑桃5
        Card[] case2 = {
                new Card(Suit.SPADE, 1),
                new Card(Suit.SPADE, 2),
                new Card(Suit.SPADE, 3),
                new Card(Suit.SPADE, 4),
                new Card(Suit.SPADE, 5)
        };
        System.out.println("案例二（A-2-3-4-5）结果: " + new Solution().isStraightFlush(case2));
        // 预期输出: true


        // 案例三（反例）：跨圈不合法的同花顺
        // 输入：梅花Q, 梅花K, 梅花A, 梅花2, 梅花3
        Card[] case3 = {
                new Card(Suit.CLUB, 12),
                new Card(Suit.CLUB, 13),
                new Card(Suit.CLUB, 1),
                new Card(Suit.CLUB, 2),
                new Card(Suit.CLUB, 3)
        };
        System.out.println("案例三（Q-K-A-2-3）结果: " + new Solution().isStraightFlush(case3));
        // 预期输出: false
    }
}

// 同花顺
class Solution {
    public boolean isStraightFlush(Card[] cards) {
        if (cards == null || cards.length != 5) return false;

        // 1. 判同花：所有牌的花色必须一样
        Suit suit = cards[0].getSuit();
        for (Card card : cards) {
            if (card.getSuit() != suit) return false;
        }

        // 2. 判顺子：所有牌的点数必须连续
        Arrays.sort(cards, (c1, c2) -> c1.getRank() - c2.getRank());

        // 专门处理 10, J, Q, K, A 的情况
        if (cards[0].getRank() == 1 && cards[1].getRank() == 10 && cards[2].getRank() == 11 && cards[3].getRank() == 12 && cards[4].getRank() == 13) return true;

        // 正常情况：相邻两张牌点数差必须为 1
        for (int i = 1; i < cards.length; i++) {
            if (cards[i].getRank() - cards[i - 1].getRank() != 1) return false;
        }

        return true;
    }
}

// 花色
enum Suit {
    CLUB, DIAMOND, HEART, SPADE
}

// 扑克牌类
class Card {
    private Suit suit;
    private int rank;

    public Card(Suit suit, int rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public int getRank() {
        return rank;
    }

    @Override
    public String toString() {
        return suit + " " + rank;
    }
}



