import java.util.Random;
import java.util.Arrays;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {

    //dimensions of board and tetromino squares
    
    final int SquareSide = 30;
    final int BoardHeight = 20;
    final int BoardWidth = 10;

    Timer timer;
    JLabel statusbar;

    //determines if game is running
    boolean isRunning = false;
    
    int curX = 0;
    int curY = 0;
    int score = 0;

    Random r = new Random();

    //current piece falling
    Shape curPiece;

    //array holding the coordinates in the board
    //holds 0 if coordinate is not filled by a square
    //holds an int corresponding to the shape the square
    //belongs to if filled
    static int [][] squareCoordinates = new int[10][20];

    /**
     * Constructor for the Board
     * Starts the Timer at an interval of 400 milliseconds
     * 
     * @param  Tetris  object which holds the Board
     */

    public Board(Tetris tetris) {
        setFocusable(true);    
        timer = new Timer(400, this);
        timer.start();

        this.setBackground(Color.WHITE);
        statusbar =  tetris.getStatusBar();
        addKeyListener(new TAdapter());
        clearBoard();  
    }

    /**
     * Action to be performed at every tick of the Timer.
     * curPiece descends one line and any full lines are removed.
     */
    public void actionPerformed(ActionEvent e) 
    {
        oneLineDown();
        repaint();
        removeFullLines();  
    }

    /**
     * Starts the game with a running timer, a clear Board, an initial piece
     */
    public void start()
    {
        isRunning = true;
        clearBoard();

        newPiece();
        timer.start();
    }

    /**
     * Overrides the paint method of the superclass.
     * Scans the 3D array squareCoordinates for populated coordinates
     * and draws the corresponding squares.
     * 
     * Rows of squareCoordinates are the x-Values
     * Cols of squareCoordinates are the y-Values
     * 
     * squareCordinates[x][y] holds the type of the shape it belongs to,
     * which determines its color, and is set to 0 if it is unpopulated
     * 
     * @param Graphics g
     */
    public void paint(Graphics g)
    { 
        super.paint(g);
        for (int i = 0; i < BoardWidth; i++) {
            for (int j = 0; j < BoardHeight; j++) {
                if (squareCoordinates[i][j] != 0)
                {
                    drawSquare(g, SquareSide*i, SquareSide*j, squareCoordinates[i][j]);
                }
            }
        }
        if(isRunning)
        {
            statusbar.setText(String.valueOf(score));
        }
        else
        {
            statusbar.setText("Game Over! Score: " + String.valueOf(score));
        }
    }

    /**
     * Hard Drop. Continues incrementing Y until it can no longer be incremented
     * Creates new piece
     */
    private void dropDown()
    {
        int newY = curY;
        while (newY > 0) 
        {
            if (!tryMove(curPiece, curX, newY, curX, newY + 1))
            {
                break;
            }
            newY++;
        }             
        newPiece();
    }

    /**
     * Soft Drop. Creates a new piece if curPiece reaches the bottom.
     */
    private void oneLineDown()
    {
        if (!tryMove(curPiece, curX, curY, curX, curY + 1))
        {
            newPiece();         
        }
        else
        {
            tryMove(curPiece, curX, curY, curX, curY);
        }
    }

    /**
     * Clears the Board of all squares.
     */
    private void clearBoard()
    {
        for (int i = 0; i < BoardWidth; i++)
        {
            for(int j = 0; j < BoardHeight; j++)
            {
                squareCoordinates[i][j] = 0;
            }
        }
    }

    /**
     * Creates a new piece if possible.
     * If not, ends the game.
     */
    private void newPiece()
    {
        curPiece = new Shape(r.nextInt(7) + 1);
        curX = 5;
        curY = 0;

        if (!tryMove(curPiece, 0, 0, curX, curY)) {
            timer.stop();
            curPiece = new Shape(0);
            clearPiece(curPiece, curX, curY);
            clearBoard();
            isRunning = false;
            repaint();
        }    
        tryMove(curPiece, curX, curY, curX, curY);
    }

    /**
     * Determines if the given rotation is possible
     *
     *@param  Shape  the piece to be rotated
     *@param  int  the piece's configuration post-rotation
     *@param  int  the piece's current X-value
     *@param  int  the peice's current Y-value
     *@param  int  what the piece's X-value post-move
     *@param  int  what the piece's Y-value post-move
     *
     *
     *@return  boolean  states whether the given rotation is possible
     *@post repopulates squareCoordinates if successful and repaints
     */
    private boolean tryRotation (Shape curPiece, Shape rotatedPiece, int prevX, int prevY, int newX, int newY)
    {

        for(int i = 0; i < 4; i++)
        {
            int oldX = curPiece.getShape()[i][0] + prevX;
            int oldY = curPiece.getShape()[i][1] + prevY;

            squareCoordinates[oldX][oldY] = 0;
        }

        if (rotatedPiece.minX() + newX < 0 || rotatedPiece.maxX() + newX > 9 ||
        rotatedPiece.minY() + newY < 0 || rotatedPiece.maxY() + newY > 18)                
        {
            for(int i = 0; i < 4; i++)
            {
                int oldX = curPiece.getShape()[i][0] + prevX;
                int oldY = curPiece.getShape()[i][1] + prevY;

                squareCoordinates[oldX][oldY] = curPiece.getShapeType();
            }
            return false;
        }

        for(int j = 0; j < 4; j++)
        {
            int testX = rotatedPiece.getShape()[j][0] + newX;
            int testY = rotatedPiece.getShape()[j][1] + newY;
            if(squareCoordinates[testX][testY] != 0)
            {
                for(int i = 0; i < 4; i++)
                {
                    int oldX = curPiece.getShape()[i][0] + prevX;
                    int oldY = curPiece.getShape()[i][1] + prevY;

                    squareCoordinates[oldX][oldY] = curPiece.getShapeType();
                }
                return false;
            }
        }

        for(int i = 0; i < 4; i++)
        {
            int squareX = rotatedPiece.getShape()[i][0] + newX;
            int squareY = rotatedPiece.getShape()[i][1] + newY;

            squareCoordinates[squareX][squareY] = rotatedPiece.getShapeType();
        }

        this.curPiece = rotatedPiece;

        curX = newX;
        curY = newY;

        repaint();
        return true;
    }

    /**
     * Determines if the given move is possible
     *
     *@param  Shape  the piece to be moved
     *@param  int  the piece's current X-value
     *@param  int  the peice's current Y-value
     *@param  int  what the piece's X-value post-move
     *@param  int  what the piece's Y-value post-move
     *
     *
     *@return  boolean  states whether the given move is possible
     *@post repopulates squareCoordinates if successful and repaints
     */
    private boolean tryMove (Shape curPiece, int prevX, int prevY, int newX, int newY)
    {
        
        //A brute-force solution to threading. Further explanation in README
        for(int i = 0; i < 4; i++)
        {
            int oldX = curPiece.getShape()[i][0] + prevX;
            int oldY = curPiece.getShape()[i][1] + prevY;

            squareCoordinates[oldX][oldY] = 0;
        }

        //Checks boundaries
        if (curPiece.minX() + newX < 0 || curPiece.maxX() + newX > BoardWidth - 1 ||
        curPiece.minY() + newY < 0 || curPiece.maxY() + newY > BoardHeight - 2)                
        {
            for(int i = 0; i < 4; i++)
            {
                int oldX = curPiece.getShape()[i][0] + prevX;
                int oldY = curPiece.getShape()[i][1] + prevY;

                squareCoordinates[oldX][oldY] = curPiece.getShapeType();
            }
            return false;
        }

        //Checks for intersection
        for(int j = 0; j < 4; j++)
        {
            int testX = curPiece.getShape()[j][0] + newX;
            int testY = curPiece.getShape()[j][1] + newY;
            if(squareCoordinates[testX][testY] != 0)
            {
                for(int i = 0; i < 4; i++)
                {
                    int oldX = curPiece.getShape()[i][0] + prevX;
                    int oldY = curPiece.getShape()[i][1] + prevY;

                    squareCoordinates[oldX][oldY] = curPiece.getShapeType();
                }
                return false;
            }
        }
        for(int i = 0; i < 4; i++)
        {
            int squareX = curPiece.getShape()[i][0] + newX;
            int squareY = curPiece.getShape()[i][1] + newY;

            squareCoordinates[squareX][squareY] = curPiece.getShapeType();
        }

        this.curPiece = curPiece;

        curX = newX;
        curY = newY;

        repaint();
        return true;
    }

    /**
     * Removes all full lines
     * 
     *@post squareCoordinates is repopulated with the full lines taken out
     */
    private void removeFullLines()
    {
        boolean lineIsFull;

        clearPiece(curPiece, curX, curY);

        for (int i = BoardHeight - 1; i>=0; i--)
        {
            lineIsFull = true;
            for (int j = 0; j < BoardWidth; j++)
            {
                if (squareCoordinates[j][i] == 0)
                {
                    lineIsFull = false;
                }
            }

            if (lineIsFull)
            {
                score += 1;
                for (int k = i; k > 1; k--) 
                {
                    for (int l = 0; l < BoardWidth; l++)
                    {
                        squareCoordinates[l][k] = squareCoordinates[l][k-1];
                    }
                }
            }
        }

        for(int i = 0; i < 4; i++)
        {
            squareCoordinates[curPiece.getShape()[i][0] + curX][curPiece.getShape()[i][1] + curY] = curPiece.getShapeType();
        }

        repaint();
    }

    /**
     * Paints a square at a given coordinate if that coordinate is populated
     *
     *@param  Graphics  g
     *@param  int  x-value of the square to be checked
     *@param  int  y-value of the square to be checked
     *@param  int  The square's color. Equals 0 if unpopulated
     *
     *
     *@return  boolean  states whether the square was populated/painted
     *@post the appropriate square is painted with the appropriate color
     */
    private boolean drawSquare(Graphics g, int x, int y, int color)
    {
        Color colors[] = { new Color(255, 255, 255), new Color(204, 102, 102), 
                new Color(102, 204, 102), new Color(102, 102, 204), 
                new Color(204, 204, 102), new Color(204, 102, 204), 
                new Color(102, 204, 204), new Color(218, 170, 0),
                new Color(220, 220, 220)
            };

        if(color == 0)
        {   
            return false;
        }

        Color curColor = colors[color];
        g.setColor(curColor);
        g.fillRect(x + 1, y + 1, SquareSide - 2, SquareSide - 2);

        g.setColor(colors[0]);
        g.drawLine(x, y + SquareSide - 1, x, y);
        g.drawLine(x, y, x + SquareSide - 1, y);

        g.setColor(colors[8]);
        g.drawLine(x + 1, y + SquareSide - 1,
            x + SquareSide - 1, y + SquareSide - 1);
        g.drawLine(x + SquareSide - 1, y + SquareSide - 1,
            x + SquareSide - 1, y + 1);         

        return true;
    }

    /**
     * Removes the current piece while leaving the current position unchanged
     *
     *@param  Shape  curPiece
     *@param  int  x-value of the piece
     *@param  int  y-value of the piece
     *
     *
     *@post removes the current piece without changing curX and curY
     */
    private void clearPiece(Shape curPiece, int X, int Y)
    {
        for(int i = 0; i < 4; i++)
        {
            int clearX = curPiece.getShape()[i][0] + X;
            int clearY = curPiece.getShape()[i][1] + Y;
            squareCoordinates[clearX][clearY] = 0;
        }
    }

    /**
     * Attaches actions to KeyEvents
     */
    class TAdapter extends KeyAdapter {
        public void keyPressed(KeyEvent e) {

            if (!isRunning) 
            {  
                return;
            }

            int keycode = e.getKeyCode();

            switch (keycode) {
                case KeyEvent.VK_LEFT:
                tryMove(curPiece, curX, curY, curX - 1, curY);
                break;
                case KeyEvent.VK_RIGHT:
                tryMove(curPiece, curX, curY, curX + 1, curY);
                break;
                case KeyEvent.VK_DOWN:
                oneLineDown();
                break;
                case KeyEvent.VK_UP:
                tryRotation(curPiece, curPiece.rotateRight(), curX, curY, curX, curY);
                break;
                case KeyEvent.VK_SPACE:
                dropDown();
                break;
            }

        }
    }
}