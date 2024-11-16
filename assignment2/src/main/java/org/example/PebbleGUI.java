package org.example;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static com.sun.java.accessibility.util.AWTEventMonitor.addKeyListener;

/**
 * The graphical user interface for the pebble game.
 */
public class PebbleGUI {
    private PebbleModel model;

    private Player CurrPlayer;
    private int TURNS;

    private int selRow = -1;
    private int selCol = -1;
    private JFrame frame;
    private JPanel gridPanel;
    private JPanel northPanel;
    private JPanel southPanel;
    private JTextArea display;
    private JLabel scoreLabel;
    private JButton[][] buttons;

    /**
     * Initializes and sets up the game GUI with a grid of buttons, allowing players to move pebbles
     * and tracking turns until one player wins or the game ends in a draw.
     */
    public PebbleGUI() {
        int Dim = chooseGridSize();
        model = new PebbleModel(Dim);
        TURNS = Dim * 5;
        CurrPlayer = model.GetP1();
        frame = new JFrame("Pebble");
        frame.setSize(600,600);
        /*
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());*/
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                int response = JOptionPane.showConfirmDialog(frame,
                        "Are you sure you want to exit?", "Confirm Exit",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        buttons = new JButton[Dim][Dim];
        gridPanel = new JPanel(new GridLayout(Dim, Dim));
        for (int i =0; i<Dim; i++) {
            for (int j=0; j<Dim; j++) {
                String pebble = model.GetPebbles()[i][j];
                JButton button = new JButton();
                if(pebble.equals("White")){
                    button.setText("White");
                    //button.setBackground(Color.white);
                    //button.setForeground(Color.black);
                }
                if(pebble.equals("Black")){
                    button.setText("Black");
                    //button.setBackground(Color.black);
                    //button.setForeground(Color.black);
                }
                if(pebble.equals(" ")){
                    button.setText(" ");
                    //button.setBackground(Color.lightGray);
                    //button.setForeground(Color.black);
                }
                //button.setEnabled(false);
                final int row = i;
                final int col = j;
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        selCol =col;
                        selRow = row;
                        System.out.println("Selected " + selRow + " " + selCol);
                        System.out.println("Player: "+ CurrPlayer.getName());

                        frame.requestFocus();
                    }
                });
                /*button.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if((selRow != -1 || selCol != -1) && CurrPlayer.getName().equals(model.GetPebbles()[selRow][selCol])) {
                            if(TURNS >0){
                                TURNS--;
                                switch (e.getKeyCode()) {
                                    case KeyEvent.VK_UP:
                                        movePebble(selRow , selCol, -1,0);
                                        break;
                                    case KeyEvent.VK_DOWN:
                                        movePebble(selRow , selCol ,1,0);
                                        break;
                                    case KeyEvent.VK_LEFT:
                                        movePebble(selRow, selCol, 0,-1);
                                        break;
                                    case KeyEvent.VK_RIGHT:
                                        movePebble(selRow, selCol, 0,1);
                                        break;
                                }
                                checkEnd();
                                CurrPlayer = CurrPlayer == model.GetP1() ? model.GetP2() : model.GetP1();
                                updateScore();
                            }
                        }
                    }
                });*/
                gridPanel.add(button);
                buttons[i][j] = button;
            }

        }
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if((selRow != -1 || selCol != -1) && CurrPlayer.getName().equals(model.GetPebbles()[selRow][selCol])) {
                    if(TURNS >0){
                        TURNS--;
                        switch (e.getKeyCode()) {
                            case KeyEvent.VK_UP:
                                movePebble(selRow , selCol, -1,0);
                                break;
                            case KeyEvent.VK_DOWN:
                                movePebble(selRow , selCol ,1,0);
                                break;
                            case KeyEvent.VK_LEFT:
                                movePebble(selRow, selCol, 0,-1);
                                break;
                            case KeyEvent.VK_RIGHT:
                                movePebble(selRow, selCol, 0,1);
                                break;
                        }
                        checkEnd();
                        CurrPlayer = CurrPlayer == model.GetP1() ? model.GetP2() : model.GetP1();
                        updateScore();
                    }
                }
            }
        });
        display = new JTextArea("Good Luck!");
        display.setEditable(false);
        display.setFocusable(false);
        display.setLineWrap(true);
        display.setWrapStyleWord(true);
        northPanel = new JPanel();
        northPanel.add(display);

        southPanel = new JPanel();
        scoreLabel = new JLabel(model.GetP1().getName() +": " + model.GetP1().getNumPebbles() + " " +model.GetP2().getName() +": " + model.GetP2().getNumPebbles()+ " Turns: " + TURNS + " Current Player: " + CurrPlayer.getName());
        southPanel.add(scoreLabel);
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> {
            String[] options = {"3x3", "4x4", "6x6"};
            int choice = JOptionPane.showOptionDialog(
                    frame,
                    "Choose a new grid size:",
                    "Reset Game",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            int newDim = switch (choice) {
                case 0 -> 3;
                case 1 -> 4;
                case 2 -> 6;
                default -> buttons.length;  // Default to current dimension if no choice is made
            };

            reset(newDim);  // Reset with chosen or current dimension
        });

        southPanel.add(resetButton);
        frame.setVisible(true);
        frame.add(northPanel, BorderLayout.NORTH);
        frame.add(gridPanel, BorderLayout.CENTER);
        frame.add(southPanel, BorderLayout.SOUTH);
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Shows a game-over dialog with options to exit or restart the game.
     *
     * @param message The message to display in the dialog.
     */
    public void showEnd(String message) {
        String[] options = {"Exit", "Restart"};
        int choice = JOptionPane.showOptionDialog(
                frame,
                message,
                "Game Over",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );
        switch (choice) {
            case 0 -> System.exit(0);
            case 1 -> reset(buttons.length);
        }
    }


    /**
     * Checks if the game has ended and shows the result
     */
    public void checkEnd() {
        if (TURNS == 0) {
            // Handle end-game condition based on scores
            int p1Pebbles = model.GetP1().getNumPebbles();
            int p2Pebbles = model.GetP2().getNumPebbles();

            if (p1Pebbles == p2Pebbles) {
                showEnd("It's a draw!");
            } else if (p1Pebbles > p2Pebbles) {
                showEnd( model.GetP1().getName()+" won!");
            } else {
                showEnd(model.GetP2().getName()+" won!");
            }
        } else if (model.GetP1().getNumPebbles() == 0) {
            showEnd( model.GetP2().getName()+" won!");
        } else if (model.GetP2().getNumPebbles() == 0) {
            showEnd(model.GetP1().getName()+" won!");
        }
    }




    /**
     * Moves a pebble from one position to another in a specified direction if the move is valid.
     *
     * @param x       the current row of the pebble
     * @param y       the current column of the pebble
     * @param xChange the row change for the move (e.g., -1 for up, 1 for down)
     * @param yChange the column change for the move (e.g., -1 for left, 1 for right)
     */

    public void movePebble(int x, int y, int xChange, int yChange) {
        if(TURNS >= 0){
            //TURNS--;
            int newX = x + xChange;
            int newY = y + yChange;
            // Check if the move is out of bounds
            if (newX < 0 || newY < 0 || newX >= buttons.length || newY >= buttons[0].length) {
                String color = buttons[x][y].getText();
                buttons[x][y].setText(" ");
                model.GetPebbles()[x][y] = " ";
                selRow = selCol = -1;  // Deselect the pebble after out-of-bounds move
                switch(color){
                    case "White":
                        model.GetP1().setNumPebbles(model.GetP1().getNumPebbles() - 1);
                        break;
                    case "Black":
                        model.GetP2().setNumPebbles(model.GetP2().getNumPebbles() - 1);
                        break;
                }

                //updateScore();
                return;
            }
            if (buttons[newX][newY].getText().equals(" ")) {
                buttons[newX][newY].setText(buttons[x][y].getText());
                model.GetPebbles()[newX][newY] = model.GetPebbles()[x][y];
                buttons[x][y].setText(" ");
                model.GetPebbles()[x][y] = " ";
            } else if (buttons[newX][newY].getText().equals("White") || buttons[newX][newY].getText().equals("Black")) {
                // Move the blocking pebble in the same direction recursively
                movePebble(newX, newY, xChange, yChange);
                // Place the original pebble in the vacated spot of the blocking pebble
                buttons[newX][newY].setText(buttons[x][y].getText());
                model.GetPebbles()[newX][newY] = model.GetPebbles()[x][y];
                buttons[x][y].setText(" ");
                model.GetPebbles()[x][y] = " ";
            }
            //updateGrid();
            //CurrPlayer = CurrPlayer == model.GetP1() ? model.GetP2() : model.GetP1();
            //updateScore();
            updateGrid();
            if (TURNS == 0) System.out.println("No more turns are left");
            //updateScore();
            if(model.GetP1().getNumPebbles() == 0) {
                showEnd(model.GetP2().getName()+" won!");
                /*
                System.out.println( model.GetP2().getName() +" WON !");
                TURNS =-1;
                String[] options2 = {"Exit", "Restart"};
                int choice = JOptionPane.showOptionDialog(
                        frame,
                        "Congratulations! ",
                        "Player 2 won the game !",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        options2,
                        options2[0]
                );
                switch(choice) {
                    case 0:
                        System.exit(0);
                    case 1:
                        reset(buttons.length);
                    default:
                        reset(buttons.length);
                };*/
            }
            if (model.GetP2().getNumPebbles() == 0) {
                showEnd(model.GetP1().getName()+" won!");
                /*
                System.out.println(model.GetP1().getName() + " WON !");
                TURNS =-1;
                String[] options2 = {"Exit", "Reset"};
                int choice = JOptionPane.showOptionDialog(
                        frame,
                        "Congratulations! ",
                        "Player 1 won the game !",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        options2,
                        options2[0]
                );
                switch(choice) {
                    case 0:
                        System.exit(0);
                    case 1:
                        reset(buttons.length);
                    default:
                        reset(buttons.length);
                }*/
            }
        }else if (model.GetP1().getNumPebbles() == model.GetP2().getNumPebbles() ) {
            showEnd("It's a draw!");
            //updateScore();
            updateGrid();
           String[] options = {"Exit", "Restart"};
           int choice = JOptionPane.showOptionDialog(
                   frame,
                   "There's no more turns left, game ended in a draw",
                   "Draw",
                   JOptionPane.DEFAULT_OPTION,
                   JOptionPane.INFORMATION_MESSAGE,
                   null,
                   options,
                   options[0]
           );
           switch(choice) {
               case 0 -> System.exit(0);
               case 1 -> reset(buttons.length);
           }
        }else if ( model.GetP1().getNumPebbles() >= model.GetP2().getNumPebbles()) {
            showEnd( model.GetP1().getName()+" won!");
            /*
            //updateScore();
            updateGrid();
            String[] options = {"Exit", "Restart"};
            int choice = JOptionPane.showOptionDialog(
                    frame,
                    "There's no more turns left, Player 1 won !",
                    "Player 1 won",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]
            );
            switch(choice) {
                case 0 -> System.exit(0);
                case 1 -> reset(buttons.length);
            }*/
        }
        else if (model.GetP1().getNumPebbles() <= model.GetP2().getNumPebbles() ) {
            showEnd( model.GetP2().getName()+" won!");
            /*
            //updateScore();
            updateGrid();
            String[] options = {"Exit", "Restart"};
            int choice = JOptionPane.showOptionDialog(
                    frame,
                    "There's no more turns left, Player 2 won !",
                    "Player 2 won",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]
            );
            switch(choice) {
                case 0 -> System.exit(0);
                case 1 -> reset(buttons.length);
            }*/
        }
    }

    /**
     * Updates the text and color of each button on the grid to reflect the current game state.
     */
    public void updateGrid() {
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                String pebble = model.GetPebbles()[i][j];
                buttons[i][j].setText(pebble.equals(" ") ? " " : pebble);
                if (pebble.equals("White")) {
                    buttons[i][j].setForeground(Color.black);
                } else if (pebble.equals("Black")) {
                    buttons[i][j].setForeground(Color.black);
                }
            }
        }
    }


    /**
     * Updates the score display to show the current player's score and the number of remaining turns.
     */
    public void updateScore() {
        scoreLabel.setText(model.GetP1().getName() +": " + model.GetP1().getNumPebbles() + " " +model.GetP2().getName() +": " + model.GetP2().getNumPebbles()+ " Turns: " + TURNS + " Current Player: " + CurrPlayer.getName());
    }

    /**
     * Prompts the user to select a grid size for the game.
     *
     * @return the chosen grid size dimension as an integer
     */
    public int chooseGridSize() {
        String[] options = {"3x3", "4x4", "6x6"};
        int response = JOptionPane.showOptionDialog(
                null,
                "Choose grid size:",
                "Grid Size Selection",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0] // Default selection
        );
        switch(response) {
            case 0: return 3; // 3x3
            case 1: return 4; // 4x4
            case 2: return 6; // 5x5
            default: return 3; // Default to 3x3 if closed
        }
    }

    /**
     * Resets the game with a new grid of the specified dimension, clearing the board and updating the GUI.
     *
     * @param newDim the dimension of the new grid to be initialized
     */
    public void reset(int newDim) {
        // Create a new model with the chosen dimensions
        model = new PebbleModel(newDim);
        CurrPlayer = model.GetP1();
        selRow = -1;
        selCol = -1;
        TURNS = newDim *5;

        // Clear existing buttons from gridPanel and recreate with new dimensions
        gridPanel.removeAll();
        gridPanel.setLayout(new GridLayout(newDim, newDim));
        buttons = new JButton[newDim][newDim];

        // Create new buttons for the grid and add to gridPanel
        for (int i = 0; i < newDim; i++) {
            for (int j = 0; j < newDim; j++) {
                JButton button = new JButton();
                button.setText(" ");
                final int row = i;
                final int col = j;
                button.addActionListener(e -> {
                    selRow = row;
                    selCol = col;
                    frame.requestFocus();
                });
                button.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (selRow != -1 && selCol != -1 && CurrPlayer.getName().equals(model.GetPebbles()[selRow][selCol])) {
                            switch (e.getKeyCode()) {
                                case KeyEvent.VK_UP -> movePebble(selRow, selCol, -1, 0);
                                case KeyEvent.VK_DOWN -> movePebble(selRow, selCol, 1, 0);
                                case KeyEvent.VK_LEFT -> movePebble(selRow, selCol, 0, -1);
                                case KeyEvent.VK_RIGHT -> movePebble(selRow, selCol, 0, 1);
                            }
                        }
                    }
                });
                buttons[i][j] = button;
                gridPanel.add(button);
            }
        }

        updateGrid();
        updateScore();

        // Refresh frame to display new grid
        frame.revalidate();
        frame.repaint();
        display.setText("Good Luck!");
    }
}