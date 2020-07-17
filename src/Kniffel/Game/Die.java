package Kniffel.Game;

import javax.swing.*;
import java.awt.*;

/**
 * this class represent the Dice to be thrown.
 * Each Kniffel.Game.Die itself is a button which can be clicked and can holds a value.
 */
public class Die extends JButton implements Comparable<Die> {

    private int numEyesInDie;
    private boolean isChosen;
    private final Font ORIGINAL_FONT =getFont();

    /**
     * sets the number to be held on the dice and shown to the user.
     * @param numEyesInDie the randomly generated number by the {@link KniffelBlock#shake()} to be set as the displayed number.
     */
    public void setNumEyesInDie(int numEyesInDie) {
        this.numEyesInDie = numEyesInDie;
    }

    /**
     * @return the number displayed on the dice.
     */
    public int getNumEyesInDie() {
        return numEyesInDie;
    }

    /**
     * to toggle the button on and off.
     */
    public void toggle(){//is called in the action listener!.
        isChosen=!isChosen;
    }

    /**
     * getter for the button isChosen.
     * @return current state of the boolean {@code IsChosen}
     */
    public boolean getIsChosen() {
        return isChosen;
    }

    /**
     * setter for the {@code isChosen}.
     * @param chosen value to be set.
     */
    public void setChosen(boolean chosen) {
        isChosen = chosen;
    }

    /**
     * Method to toggle the font of the button between bold and normal.
     */
    public void changeBold() {
        if(isChosen) {
            setFont(new Font("Sansserif", Font.BOLD,50));
        }
        else {
            setFont(ORIGINAL_FONT);
        }
    }

    @Override
    public String toString() {
        return ""+ numEyesInDie;
    }

    @Override
    public int compareTo(Die x) {
        return getNumEyesInDie() - x.getNumEyesInDie();
    }//will help in sorting!!!
}
