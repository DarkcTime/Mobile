package com.example.Calls.BackEnd.Analysis;

public class Analyze {

    /**
     * count number of worlds in current str
     * @param str message
     * @return count words
     */
    public static int getCountWords(String str){
        //Начальное количество слов равно 0
        int count = 0;

        //Если ввели хотя бы одно слово, тогда считать, иначе конец программы
        if(str.length() != 0){
            count++;
            //Проверяем каждый символ, не пробел ли это
            for (int i = 0; i < str.length(); i++) {
                if(str.charAt(i) == ' '){
                    //Если пробел - увеличиваем количество слов на 1
                    count++;
                }
            }
        }

        return count;
    }
}
