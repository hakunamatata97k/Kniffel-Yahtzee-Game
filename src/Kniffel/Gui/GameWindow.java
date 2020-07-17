package Kniffel.Gui;

import Kniffel.Game.Die;
import Kniffel.Game.KniffelBlock;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * This class is the Gui representation of the Famous game Kniffel.
 * The Program needs java 8 to run.
 * @author Kazem.
 */
public class GameWindow extends JFrame implements ActionListener {

	//game relevant variables
	private KniffelBlock gameBoard;
	private boolean preventLockOnStart;
	private final static int MAX_THROWS=3, MAX_TRIES = 13;//will be used in game logik
	private int currentThrow=0, currentTries=0, scoreToBeUpdated=0;
	private JRadioButton lastClickedButton;

	//Gui relevant variables
	private JButton werfen, auszahlen, eintragen;//we left it in german because its more informative to the reader that this refers to the button name.
	private JTextArea scoreTextArea;
	private final JPanel CONTAINER;
	private final JLabel subTitle;
	private JRadioButton [] allChoices;
	private ButtonGroup choicesGroup;

	/**
	 * constructor which will handel the initializing of the Java swing components and other necessary functionalities of the program.
	 */
	public GameWindow(){
		setTitle("Kniffel Spiel");
		setLayout(new BorderLayout());//THIS IS FOR THE FRAME

		subTitle =new JLabel();
		getContentPane().add(subTitle,BorderLayout.PAGE_START);

		CONTAINER =new JPanel();
		CONTAINER.setLayout(new BorderLayout());

		initGameOptionsButton();
		updateGreeting();
		createDiceButtons();
		createScoreBoard();
		createGameOptionsGroup();
		createGameButtons();
		addListeners();
		add(CONTAINER);
		pack();//ITs optional..
		setProperties();
	}

	/**
	 * update the message that the user would get when the gui program runs.
	 * the message states the amount of the available tries and the total throw count.
	 */
	private void updateGreeting(){
		String greetingMessage=String.format("Sie haben noch %s Wuerfe !. %125s", (MAX_THROWS-currentThrow),"Sie Haben noch " + (MAX_TRIES -currentTries) +" Versuche");
		subTitle.setText(greetingMessage);
	}

	/**
	 * identify the game end message which would notify the user that the game is finished.
	 */
	private void setEndGameMessage(){
		String endGameMessage=String.format("Game ist Nun Beendet. %98s", "Starten Sie das Programm erneut an um ein neues Spiel zu erzeugen!");
		subTitle.setText(endGameMessage);
	}

	/**
	 * Initiate the Gui JRadio buttons which resemble the 13 game choices.
	 * @see JRadioButton
	 */
	private void initGameOptionsButton() {
		allChoices = new JRadioButton[13];

		for (int i = 0; i < allChoices.length; i++)
			if (i<6)//For the basics
				allChoices[i]=new JRadioButton((i+1)+"er");
		//For the specials!.
		allChoices[6]=new JRadioButton("Dreierpasch");
		allChoices[7]=new JRadioButton("ViererPasch");
		allChoices[8]=new JRadioButton("FullHouse");
		allChoices[9]=new JRadioButton("Kleine Strasse");
		allChoices[10]=new JRadioButton("Grosse Strasse");
		allChoices[11]=new JRadioButton("Kniffel");
		allChoices[12]=new JRadioButton("Chance");
	}

	/**
	 * adds listeners for the swing components to be able to get a specific functionality started when a certain button is clicked.
	 */
	private void addListeners(){
		for (JRadioButton oneChoice : allChoices) oneChoice.addActionListener(this);
		auszahlen.addActionListener(this);
		eintragen.addActionListener(this);
		werfen.addActionListener(this);
	}

	/**
	 * adds the buttons to the choices to the {@link ButtonGroup } to make it only possible to select one button at a time.
	 * The method will add the single buttons to a {@link JPanel} to give it certain layout and place on the screen.
	 */
	private void createGameOptionsGroup(){
		JPanel choicesPanel = new JPanel();
		choicesPanel.setLayout(new GridLayout(13,1));

		//As ButtonGroup is not a component, you cannot add your ButtonGroup to a JPanel.
		choicesGroup = new ButtonGroup();
		for (JRadioButton oneChoice : allChoices) {
			choicesGroup.add(oneChoice);
			choicesPanel.add(oneChoice);
		}
		CONTAINER.add(choicesPanel, BorderLayout.WEST);
	}

	/**
	 * this will initialize the big clickable buttons and add them at the top of the window shown when starting the program.
	 */
	private void createDiceButtons(){
		gameBoard=new KniffelBlock();
		JPanel diceList = new JPanel();
		diceList.setLayout(new GridLayout(1,6));

		for (Die die : gameBoard.getDICE()) {
			die.addActionListener(this);
			die.setPreferredSize(new Dimension(50, 50));//SOMEHOW WORKED ONLY WITH THIS
			diceList.add(die);
		}

		werfen =new JButton("werfen");
		diceList.add(werfen);
		CONTAINER.add(diceList, BorderLayout.PAGE_START);
	}

	/**
	 * Creates a Text area in which the scoring of the game would be shown, the area is not selectable nor clickable.
	 */
	private void createScoreBoard(){
		//YES PANEL FOR ONE COMPONENT BECAUSE THIS WILL GIVE MARGIN ON THE TOP AND SIDES WITHOUT USING A SETBORDER(NEW EMPTYBORDER(10,10,10,10))!.
		JPanel scorePanel = new JPanel();

		scoreTextArea = new JTextArea(50,40);
		scoreTextArea.setEditable(false);
		scoreTextArea.setHighlighter(null);//make it non selectable!
		//we need to specify the font in order for the text formatting to take effect.
		scoreTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));//without it the string formatting would not working!.

		scoreTextArea.setText(gameBoard.makeScoreBoardData());//for the initial start....
		scorePanel.add(scoreTextArea);

		CONTAINER.add(scorePanel,BorderLayout.EAST);
	}

	/**
	 * creates the basic game controlling buttons and specify their location on the shown window.
	 */
	private void createGameButtons(){
		eintragen=new JButton("Eintragen");
		eintragen.setEnabled(false);
		auszahlen =new JButton(" Gewinn Auszahlen");
		auszahlen.setEnabled(false);
		CONTAINER.add(eintragen,BorderLayout.PAGE_END);
		add(auszahlen,BorderLayout.PAGE_END);
	}

	/**
	 *	method to declare some properties for the program.
	 *	<pre>
	 *	   - make the shown Window not resizable
	 *	   - give the shown Window default size.
	 *	   - place the window in the center of the screen {@link GraphicsEnvironment#getCenterPoint}.
	 *	   - set the action on closure.
	 *	   - make the window visible.
	 *	</pre>
	 */
	private void setProperties(){
		setResizable(false);
		setSize(800,500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	/**
	 *	Start point of the program.
	 */
	public static void main(String[] args) {
//		in case of performance problems please uncomment!
//		java.awt.EventQueue.invokeLater(Gui.GameWindow::new);//this is equal to lambda its called class reference!
		new GameWindow();
	}

	/**
	 * Invoked when an action occurs.
	 * @param event the event to be processed
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource()== werfen)
			werfenController();


		for (Die die : gameBoard.getDICE())
			if(event.getSource()== die ){
				//to prevent the user from locking the button without clicking Werfen btn which will result of values not being visible.
				if (preventLockOnStart){
					die.toggle();
					die.changeBold();
				}
			}

		
		for (JRadioButton oneChoice : allChoices)
			if (event.getSource()==oneChoice) {
				lastClickedButton =oneChoice;
				scoreToBeUpdated = checkChoiceValidity(lastClickedButton);
			}


		if (event.getSource()==eintragen)
			eintragenController();

		if (event.getSource()==auszahlen){
			lockComponents();
			gameBoard.calculateFinalSum();
			String scoreBoardData=gameBoard.makeScoreBoardData();
			scoreTextArea.setText(scoreBoardData);
			setEndGameMessage();
			auszahlen.setEnabled(false);
		}
	}

	/**
	 * Defines the logic on which the {@linkplain GameWindow#werfen} btn should react through the game.
	 */
	private void werfenController(){
		preventLockOnStart=true;
		if(currentThrow<MAX_THROWS){
			gameBoard.shake();
			eintragen.setEnabled(true);
			currentThrow++;
		}
		if(currentThrow == MAX_THROWS)
			werfen.setEnabled(false);//werfen.disable is depreciated and replaced by setEnable since 1.1

		updateGreeting();
	}

	/**
	 * Defines the logic on which the {@linkplain GameWindow#eintragen} btn should react through the game.
	 */
	private void eintragenController(){
		if(null != lastClickedButton) {
			updateScoreBoardText();
			lastClickedButton.setEnabled(false);
			werfen.setEnabled(true);
			scoreToBeUpdated=0;
			currentThrow=0;
			currentTries++;
			lastClickedButton =null;
			gameBoard.clearDiceData();
			choicesGroup.clearSelection();
			eintragen.setEnabled(false);
			updateGreeting();
			if (currentTries == MAX_TRIES){
				lockComponents();
				currentTries=0;
				auszahlen.setEnabled(true);
			}
		}
	}

	/**
	 * created to disable the functionalities or clear the saved data of program relative components.
	 */
	private void lockComponents(){
		werfen.setEnabled(false);
		eintragen.setEnabled(false);
		gameBoard.clearDiceData();
		gameBoard.disableDice();
		choicesGroup.clearSelection();

		for (JRadioButton oneChoice : allChoices)
			oneChoice.setEnabled(false);
	}

	/**
	 * to update the score of the user on the {@linkplain GameWindow#scoreTextArea}.
	 */
	private void updateScoreBoardText(){
		if(gameBoard.updateInternalScoreData(lastClickedButton, scoreToBeUpdated))
			scoreTextArea.setText(gameBoard.makeScoreBoardData());
	}


	/**
	 * controls and checks if a certain selection of the game relative options is valid.
	 * calculates the score to be given to the user based on one's selection.
	 *
	 * @param selectedButton the choice made by the user.
	 * @return Score to be updated based on the user choice.
	 */
	private int checkChoiceValidity(JRadioButton selectedButton) {
		String selectedButtonText=selectedButton.getText();

		int score=-1;
		switch (selectedButtonText){
			case "Dreierpasch" :
				if(gameBoard.isThreeOfAKind())
					score=gameBoard.chance();
				break;
			case "ViererPasch" :
				if(gameBoard.isFourOfAKind())
					score=gameBoard.chance();
				break;
			case "FullHouse" :
				if(gameBoard.isFullHouse())
					score=25;
				break;
			case "Kleine Strasse" :
				if(gameBoard.istkleineStrasse())
					score=30;
				break;
			case "Grosse Strasse" :
				if(gameBoard.istGrosseStrasse())
					score=40;
				break;
			case "Kniffel" :
				if(gameBoard.isKniffel())
					score=50;
				break;
			case "Chance" :
				score=gameBoard.chance();
				break;
			default://verwaltet 1er bis 6er
				for (int i = 0; i < 6; i++)//0-5
					if( (allChoices[i].getText()).equals(selectedButtonText))
						if (gameBoard.checkPresenceOF(i+1))
							score=gameBoard.sumOfNerElement(i+1);
				break;
		}
		return score;
	}
}