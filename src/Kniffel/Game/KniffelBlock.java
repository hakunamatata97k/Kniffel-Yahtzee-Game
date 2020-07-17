package Kniffel.Game;

import Kniffel.Gui.GameWindow;

import javax.swing.*;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

/**
 * Java implementation of the necessary functionality of the famous game Kniffel.
 */
public class KniffelBlock {

    private final Map<String,String> internalScoreData;
    private final Die[] DICE;

    private final static Random RANDOM_GEN =new Random();//to know why i chose Random over Math.random() https://stackoverflow.com/a/738651/11618992
    private String scoreText;

    /**
     *Constructor which will initialize the created Dice and the internal data Structure.
     */
    public KniffelBlock() {
        DICE =new Die[5];
        internalScoreData =new LinkedHashMap<>();//To preserve the insertion oreder!!!:) https://docs.oracle.com/javase/6/docs/api/java/util/LinkedHashMap.html
        initScoreBoardInternalData();
        initializeDice();
    }

    public Die[] getDICE() {
        return DICE;
    }


    /**
     * To initialize the Dice to avoid null pointer exceptions.
     */
    private void initializeDice(){
        for (int i = 0; i < DICE.length; i++)
            DICE[i]=new Die();
    }

    /**
     * Method to clear the data held in the {@link Die} objects.
     */
    public void clearDiceData() {
        for (Die die : DICE){
            die.setText("");
            die.setNumEyesInDie(0);
            die.setChosen(false);
            die.changeBold();
        }
    }

    /**
     * to disable the the {@link Die} and clear the internal data held by the Kniffel.Game.Die object.
     */
    public void disableDice(){
        for (Die die : DICE){
            die.setEnabled(false);
            die.setNumEyesInDie(0);
        }
    }


    /**
     * Method to populate the Internal DS with the right order of the options needed for the game.
     */
    private void initScoreBoardInternalData(){
        final String placeHolder = "--";
        //initiate the first 6 basic options.
        for (int i =0; i<6 ; i++)
            internalScoreData.put((i+1)+"er", placeHolder);
        //for the calculations!.
        internalScoreData.put("Gesamt", placeHolder);
        internalScoreData.put("Bonus" , placeHolder);
        internalScoreData.put("Gesamt oberer Teil", placeHolder);
        //for the specials..
        internalScoreData.put("Dreierpasch", placeHolder);
        internalScoreData.put("ViererPasch", placeHolder);
        internalScoreData.put("FullHouse", placeHolder);
        internalScoreData.put("Kleine Strasse", placeHolder);
        internalScoreData.put("Grosse Strasse", placeHolder);
        internalScoreData.put("Kniffel", placeHolder);
        internalScoreData.put("Chance" , placeHolder);
        internalScoreData.put("Gesamt unterer Teil" , placeHolder);
        internalScoreData.put("Endsumme" , placeHolder);
    }

    /**
     * will handel the throw of the dice and thus giving each die a randomly generated value base from 1 to 6 inclusive.
     */
    public void shake() {
        for (Die die : DICE){
            if(!die.getIsChosen()){
                die.setNumEyesInDie(RANDOM_GEN.nextInt(6) + 1);
                die.setText(""+die.getNumEyesInDie());//to set the number inside the button
            }
        }
        sortDiceValues();//for the kleineStraße and the großeStraße
    }

    /**
     * this will sort the dice combinations based on the Tim sort provided by java.
     * @see Arrays#sort(int[])
     */
    public void sortDiceValues(){
        Arrays.sort(DICE);//this is only possible because we are implementing the Comparable and overriding the compareTo method
    }

    /**
     * @return a sum of all elements in the array.
     */
    public int chance(){
        int sumOfDiceValues=0;
        for(Die die: DICE)
            sumOfDiceValues+=die.getNumEyesInDie();

        return sumOfDiceValues;
//      due to the face that we dont know if you would allow this we took the java<8 approach..
//      return Arrays.stream(DICE).mapToInt(Kniffel.Game.Die::getNumEyesInDie).sumOfDiceValues();
    }

    /**
     * Method to sum all occurrences of a given number if it exists in the Dice combination.
     * @param numberToSum the number to be searched.
     * @return the sum of all occurrences of the {@code numberToSum} if the number is not found return 0.
     */
    public int sumOfNerElement(final int numberToSum) {
        if(numberToSum >6 || numberToSum <=0 )
            return -1;

        int count=0;
        for (Die die : DICE)
            if (die.getNumEyesInDie() == numberToSum)
                count++;
        return count*numberToSum;//if not found then 0*x=0 where x is 0<x<=6

//      due to the face that we dont know if you would allow this we took the java<8 approach..
//        return Arrays.stream(DICE).filter(die -> die.getNumEyesInDie() == numberToSum).mapToInt(Kniffel.Game.Die::getNumEyesInDie).sum();//its java-8.
    }

    /**
     * this will handel the internal calculation of our {@link LinkedHashMap} to calculate the final score for the fields.
     */
    public void calculateFinalSum() {
        int totalSum=0,upperBlockTotal=0;
        String key, value;
        for(Map.Entry<String, String> entry : internalScoreData.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();

            if (!value.equals("--"))
                totalSum+=Integer.parseInt(value);

            switch (key){
                case "Gesamt" :
                case "Gesamt unterer Teil":
                    internalScoreData.replace(key, String.valueOf(totalSum));
                    break;
                case "Bonus" :
                    if (totalSum>=63)
                        internalScoreData.replace(key, String.valueOf(35));
                    break;
                case "Gesamt oberer Teil" :
                    String bonusValue=internalScoreData.get("Bonus");
                    int temp=0;
                    if(!bonusValue.equals("--"))
                        temp=Integer.parseInt(bonusValue);

                    internalScoreData.replace(key, String.valueOf(totalSum+temp));
                    upperBlockTotal=totalSum+temp;
                    totalSum=0;
                    break;
                case "Endsumme" :
                    internalScoreData.replace(key, String.valueOf(totalSum+upperBlockTotal));
                    break;
            }
        }
    }

    /**
     * method to check if the current combinations of the Dice combination resemble "DreierPasch".
     * @return true if there is a number in the Dice array with the occurrences of 3.
     * @see KniffelBlock#checkComparsions
     */
    public boolean isThreeOfAKind() {
        return checkComparsions(2);
    }

    /**
     * method to check if the current combinations of the Dice combination resemble "ViererPasch".
     * @return true if there is a number in the Dice array with the occurrences of 4.
     * @see KniffelBlock#checkComparsions
     */
    public boolean isFourOfAKind() {
        return checkComparsions(3);
    }

    /**
     * method to check if the current combinations of the Dice combination resemble "Kniffel".
     * @return true if there is a number in the Dice array with the occurrences of 5.
     * @see KniffelBlock#checkComparsions
     */
    public boolean isKniffel() {
        return checkComparsions(4);
    }

    /**
     * method to check if any elemnt is to be found int the Dice combination N times where numOfComparisons is {@code N-1}.
     * @param numOfComparisons The number of comparisons Which will determine the occurrences of an element in the dice combination.
     * @return true if there were an element in the Dice combination which matches the given number of comparisons.
     *
     * @see KniffelBlock#getMostFrequentDie(int numOfComparsions)
     */
    private boolean checkComparsions(int numOfComparisons) {
        return getMostFrequentDie(numOfComparisons)!=-1;
    }


    /**
     * Method to check if there is any element in the Dice combination corresponds to the given number of comparisons.
     * @param numOfComparisons The number of comparisons Which will determine the occurrences of an element in the dice combination.
     * @return <pre>
     *          The most frequent element based on the number of comparisons,
     *          or -1 if the element is not found or if the number of {@code 5<=comparisons<=1}.
     *          </pre>
     */
    public int getMostFrequentDie(final int numOfComparisons) {
        if (numOfComparisons <= 1 || numOfComparisons >= 5)
            return -1;

        int count=numOfComparisons;
        for (int i = 0; i < DICE.length-1; i++) {
            if (DICE[i].getNumEyesInDie()== DICE[i + 1].getNumEyesInDie()) {
                count--;
                if (count == 0)
                    return DICE[i].getNumEyesInDie();//returning the number will help us avoid extra iterations while searching in the full house :)
            }else {
                count=numOfComparisons;
            }
        }
        return -1;
    }

    /**
     * This method will check if the current combination resemble  full house and its optimized to avoid extra iteration by using the {@link KniffelBlock#getMostFrequentDie(int)}
     * @return true if the current combination of the Dice resemble full house.
     */
    public boolean isFullHouse() {
        int frequent= getMostFrequentDie(2);//5

        if(frequent==-1)
            return false;

        for (int i = 0; i < DICE.length-1; i++) {
            if (DICE[i].getNumEyesInDie()!=frequent
                    && DICE[i].getNumEyesInDie()== DICE[i + 1].getNumEyesInDie())
            {
                    return true;
            }
        }
        return false;
    }

    /**
     * This method will check if the current combination represent "kleine Strasse".
     * @return true if the current combination represent "kleine Strasse".
     * @see KniffelBlock#checkSequence(int)
     */
    public boolean istkleineStrasse() {
        return checkSequence(3);
    }

    /**
     * This method will check if the current combination represent "grosse Strasse".
     * @return true if the current combination represent "grosse Strasse".
     * @see KniffelBlock#checkSequence(int)
     */
    public boolean istGrosseStrasse() {
        return checkSequence(4);
    }

    /**
     * O(n) -> solution to check if sequence of length {@code lengthOfSequence} exists in the array.
     * solution can be optimized by checking if the desired lenOfseq is bigger than the half of the array which is most of the time true,
     * then we can check if the counter has been reset after the half of the group then break and return false.
     *
     * @param lengthOfSequence  its the desired length of sequence where a sequence can be 2<len<5.
     * @return true when
     */
    public boolean checkSequence(int lengthOfSequence) {//4
        if(lengthOfSequence > 5 || lengthOfSequence<=2)
            return false;

        int count=lengthOfSequence;

        for (int i = 0; i < DICE.length-1; i++){
            //12356
            if (DICE[i].getNumEyesInDie() != DICE[i+1].getNumEyesInDie()){
                if (DICE[i].getNumEyesInDie()+1 == DICE[i + 1].getNumEyesInDie()){
                    count--;
                    if (count==0)
                        return true;
                }else//if seq is broken reset the count to the original value.
                    count=lengthOfSequence;
            }
        }
        return false;
    }

    /**
     * @param numberToSearch number to be searched in the current Dice combination.
     * @return true when the the number is found else false.
     */
    public boolean checkPresenceOF(final int numberToSearch){
        if(numberToSearch <= 0 || numberToSearch>6)
            return false;

        for (Die die : DICE)
            if (die.getNumEyesInDie() == numberToSearch)
                return true;
        return false;

//      due to the face that we dont know if you would allow this we took the java<8 approach.
//      return Arrays.stream(DICE).anyMatch(die -> die.getNumEyesInDie()==numberToSearch);
    }

    /**
     * @return a String which consists of the data to be updated on the gui to be shown to the user.
     */
    public String makeScoreBoardData(){
        scoreText="";
        internalScoreData.forEach((key, value) -> scoreText +=String.format("%-20s%20s %n", key,value));
        return scoreText;
    }

    /**
     * @param lastClickedButton the last clicked button to which shall be updated in the internal data.
     * @param scoreToBeUpdated the score calculated by the {@code checkChoiceValidity()} in  {@link GameWindow}
     * @return true if the score has been updated successfully.
     */
    public boolean updateInternalScoreData (JRadioButton lastClickedButton, int scoreToBeUpdated) {
        String key=lastClickedButton.getText();

        if (key.length()==0 || scoreToBeUpdated<=0)
            return false;

        String oldValue= internalScoreData.get(key);//<"6er","28">

        if(!oldValue.equals("--")){
            //Actually, valueOf() uses parseInt internally. The difference is parseInt returns an int primitive while valueOf returns an Integer object.
            int oldPlusNew = Integer.parseInt(oldValue)+scoreToBeUpdated;
            internalScoreData.replace(key, oldValue, String.valueOf(oldPlusNew));
        }else
            internalScoreData.replace(key, oldValue , String.valueOf(scoreToBeUpdated) );
        return true;
    }

    @Override
    public String toString() {
        return Arrays.toString(DICE);
    }
}