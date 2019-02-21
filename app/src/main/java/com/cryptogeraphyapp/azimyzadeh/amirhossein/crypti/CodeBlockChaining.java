package com.cryptogeraphyapp.azimyzadeh.amirhossein.crypti;

import android.support.annotation.NonNull;
import android.util.Log;

/**
 * @author : Amirhossein Azimyzadeh
 * @version 1.2.0
 * @since summer 2018
 *
 * @licence :
 * Copyright [2018] [Amirhossein Azimyzadeh]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 * etgfvdgc
 *
 * */

public class CodeBlockChaining {

    final static String TAG=CodeBlockChaining.class.getSimpleName();

    private int caseSize;
    private int[] KeyArray;
    private int firstBoard;
    private boolean safeCreated = true;
    private String Original = null;
    private String Encrypted = null;
    private String tempEncrypted;
    private int alphabetArrange = 0;
    private char separator;
    private String str_Board;
    private boolean firstBoardUse = false;
    private boolean firstBoardUse_DE =false;
    private boolean isFinished ;

    public boolean isFinished() {
        return isFinished;
    }

    /**
     * @param keyArray ,a key for Encrypting or Decrypting your String
     * @param firstBoard , according to the CBC algorithm it's required
     * @param separator , separator is a single char that use to split Encrypted Numbers in String
     * */
    public CodeBlockChaining(@NonNull int[] keyArray,@NonNull int firstBoard,@NonNull char separator) {
        this.caseSize = keyArray.length;
        if(this.caseSize<8){
            safeCreated=false;
        }
        if (!(keyArray.length == caseSize))
            safeCreated = false;
        else {
            this.KeyArray = keyArray;
        }
        if (firstBoard >= (Math.pow(2, caseSize)))
            safeCreated = false;
        else {
            this.firstBoard = firstBoard;
        }
        this.separator = separator;
    }
    /***
     * IT IS NECESSARY TO CHECK THIS METHOD BEFORE USE ENCRYPTING ALL and DECRYPTING ALL
     */
    public String getOriginal() {
        return Original;
    }
    public boolean getSafeCreated() {
        return safeCreated;
    }
    /**
     * BE CAREFUL :
     * @param Encrypted : it should END with Separator char
     */
    public void setEncrypted(String Encrypted) {
        this.Encrypted = Encrypted;
        this.tempEncrypted=Encrypted;
    }

    public String getEncrypted() {
        return Encrypted;
    }
    public void setOriginal(String original) {
        Original = original;
    }
    public void encryptingAll() {
        isFinished=false;
        if (Original != null) {
            String EncryptingAll = "";
            char[] char_original = Original.toCharArray();
            for (int i = 0; i < char_original.length; i++) {
                EncryptingAll += binaryToDecimal(encryptingOneChar(char_original[i])) + separator;
            }
            isFinished=false;
            Encrypted = EncryptingAll;
        }
    }
    public String decryptingAll() {
        Log.d(TAG,"DecryptionAll method started.");
        isFinished = false;
        if(Encrypted==null) {
            isFinished=true;
            return null;
        }
        String[] numbers = encryptedNumberToArray();
        String Decrypted="";
        for(int i=0 ; i<numbers.length;i++)
        {
            Decrypted += IntToChar(decryptingOneNum(numbers,i));
        }
        isFinished =true;
        Log.d(TAG,"DecryptionAll method finished.");
        return Decrypted;
    }
    private char IntToChar(int i) {
        return (char)i;
    }
    private int decryptingOneNum(String[] number, int Index) {
        String Decrypted;
        String str_firstBoard = fixedIntCase(firstBoard);
        if(!firstBoardUse_DE){
            firstBoardUse_DE =true;
            Decrypted=XORBoard(str_firstBoard,ECBInverse(fixedIntCase(Integer.valueOf(number[Index]))));
        }else{
            Decrypted=XORBoard(fixedIntCase(Integer.valueOf(number[Index-1])),
                    ECBInverse(fixedIntCase(Integer.valueOf(number[Index]))));
        }
        return Integer.valueOf(binaryToDecimal(Decrypted));
    }
    /**
     * PRIVATE METHODS
     * ECB method for Encrypting in ECB (one  part of doing this)*/
    private String ECB(String num) {
        String EncryptedCode = "";
        char[] char_num = num.toCharArray();
        char[] char_EncryptedCode = new char[caseSize];
        for (int i = 0; i < caseSize; i++) {
            char_EncryptedCode[i] = char_num[KeyArray[i] - 1];
        }
        for (int i = 0; i < caseSize; i++)
            EncryptedCode += char_EncryptedCode[i];
        return EncryptedCode;
    }
    /**
     * @see : ECB ^
     * it's work inverse of that*/
    private String ECBInverse(String num){
        String EncryptedCode = "";
        char[] char_num = num.toCharArray();
        char[] char_EncryptedCode = new char[caseSize];
        for (int i = 0; i < caseSize; i++) {
            char_EncryptedCode[KeyArray[i] - 1] = char_num[i];
        }
        for (int i = 0; i < caseSize; i++)
            EncryptedCode += char_EncryptedCode[i];
        return EncryptedCode;
    }
    /**
     * @param num : is an integer
     * @return fixed : that is the String that converted to the fixed size (size of key)
     * for example  3 is 11 in binary if key size == 4 -> 3 -> returned "0011"*/
    private String fixedIntCase(int num) {
        String toBinary = Integer.toBinaryString(num);
        if (toBinary.length() == caseSize)
            return toBinary;
        else if (toBinary.length() < caseSize) {
            String fixed = "";
            for (int i = 0; i < caseSize - toBinary.length(); i++)
                fixed += '0';
            fixed += toBinary;
            return fixed;
        }
        return null;
    }
    /**
     * here something change that may affect on hole Program
     * @param first , the first number for XOR
     * @param second ,  the second number for XOR
     * @return the first XOR second */
    private String XORBoard(String first, String second) {
        char[] char_first = first.toCharArray();
        char[] char_second = second.toCharArray();
        String result = "";
        for (int i = 0; i < caseSize; i++) {
            result += XOR(char_first[i], char_second[i]);
        }
        return result;
    }
    private char XOR(char a, char b) {
        if (a == '0' && b == '0') {
            return '0';
        }
        if (a == '0' && b == '1') {
            return '1';
        }
        if (a == '1' && b == '0') {
            return '1';
        }
        if (a == '1' && b == '1') {
            return '0';
        }
        return 'e';
    }
    private int charToInt(char A) {
        return (int)A;
    }
    private String encryptingOneChar(char A) {
        int char_num = charToInt(A);
        String str_num = fixedIntCase(char_num);
        String str_firstBoard = fixedIntCase(firstBoard);
        String Encrypted;
        if (!firstBoardUse) {
            Encrypted = ECB(XORBoard(str_firstBoard, str_num));
            str_Board = Encrypted;
            firstBoardUse = true;
        } else {
            Encrypted = ECB(XORBoard(str_Board, str_num));
            str_Board = Encrypted;
        }
        return Encrypted;
    }
    private String binaryToDecimal(String binaryCode) {
        char[] char_binaryCode = binaryCode.toCharArray();
        int sum = 0;
        double pow;
        for (int i = 0; i < char_binaryCode.length; i++) {
            pow = Math.pow(2, i);
            sum += pow * (Integer.valueOf(String.valueOf(char_binaryCode[char_binaryCode.length - i - 1])));
        }
        return String.valueOf(sum);
    }
    private int numberOfSeparator() {
        char[] char_Encrypted = Encrypted.toCharArray();
        int numberOfSeparator=0;
        for(int i=0 ; i<char_Encrypted.length ;i++){
            if(char_Encrypted[i]==separator)
                numberOfSeparator++;
        }
        return numberOfSeparator;
    }
    private String cutFirstString(char[] char_tempEncrypted,int startIndex) {
        String cutString = "";
        for(int i=startIndex+1 ; i<char_tempEncrypted.length;i++){
            cutString+=char_tempEncrypted[i];
        }
        return cutString;
    }
    private String eachNumber() {
        char[] char_tempEncrypted = tempEncrypted.toCharArray();
        String Num="";
        int i;
        for( i=0;i<char_tempEncrypted.length;i++){
            if(char_tempEncrypted[i]==separator){
                break;
            }
            Num+=char_tempEncrypted[i];
        }
        tempEncrypted=cutFirstString(char_tempEncrypted,i);
        return Num;
    }
    /**
     * @return : String Array that contain the numbers that Encrypted*/
    private String[] encryptedNumberToArray() {
        String[] numbers = new String[numberOfSeparator()];
        for(int i=0 ; i<numbers.length ; i++){
            numbers[i]=eachNumber();
        }
        return numbers;
    }
}