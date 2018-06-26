/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crc;

/**
 *
 * @author Bolek
 */
public class HammingCode {

    private final String msg;
    private final int msgLen;
    private int parBits;

    public HammingCode(String msg) {
        this.msg = msg;
        msgLen = msg.length();
        countParBits();
    }

    private void countParBits() {
        parBits = 0;
        while (msgLen + parBits + 1 > Math.pow(2, parBits)) {
            parBits++;
        }
        System.out.println("parity bits: " +parBits);
    }

    public int[] code() {
        int transLength = msgLen + parBits, temp = 0, temp2 = 0, j = 0;
        int transMsg[] = new int[transLength + 1]; //+1 because starts with 1
        for (int i = 1; i <= transLength; i++) {
            temp2 = (int) Math.pow(2, temp);
            if (i % temp2 != 0) {
                transMsg[i] = Integer.parseInt(Character.toString(msg.charAt(j)));
                j++;
            } else {
                temp++;
            }
        }
        return transMsg;
    }

    public int[] decode(int[] transMsg) {
        for (int i = 0; i < msgLen; i++) {
            int smallStep = (int) Math.pow(2, i);
            int bigStep = smallStep * 2;
            int start = smallStep, checkPos = start;
            int transLength = transMsg.length -1;
            while (true) {
                for (int k = start; k <= start + smallStep - 1; k++) {
                    checkPos = k;
                    if (k > transLength) {
                        break;
                    }
                    transMsg[smallStep] ^= transMsg[checkPos];
                }
                if (checkPos > transLength) {
                    break;
                } else {
                    start = start + bigStep;
                }
            }
        }
        return transMsg;
    }

}
