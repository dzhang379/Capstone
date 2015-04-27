
import java.lang.Math;

public class Shape {

    public int type;

    //array of the coordinates of the squares in a shape
    public int[][] pieceShape;

    //array of shapes
    private int[][][] shapeTable = new int[][][]
        {
            { { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 } },
            { { 1, 0 }, { 1, 1 }, { 0, 1 }, { 0, 2 } },
            { { 0, 0 }, { 0, 1 }, { 1, 1 }, { 1, 2 } },
            { { 0, 0 }, { 0, 1 }, { 0, 2 }, { 0, 3 } },
            { { 0, 0 }, { 1, 0 }, { 2, 0 }, { 1, 1 } },
            { { 0, 0 }, { 1, 0 }, { 0, 1 }, { 1, 1 } },
            { { 0, 0 }, { 1, 0 }, { 1, 1 }, { 1, 2 } },
            { { 1, 0 }, { 0, 0 }, { 0, 1 }, { 0, 2 } }
        };

    /**
     * Initializes the shape based on its numerical type
     */

    public Shape(int type) 
    {
        this.type = type;
        this.pieceShape = shapeTable[type]; 
    }

    /**
     * @return  int  the minimum x-value of the shape
     */
    public int minX()
    {
        int m = pieceShape[0][0];
        for (int i=0; i < 4; i++) {
            m = Math.min(m, pieceShape[i][0]);
        }
        return m;
    }

    /**
     * @return  int  the minimum y-value of the shape
     */
    public int minY() 
    {
        int m = pieceShape[0][1];
        for (int i=0; i < 4; i++) {
            m = Math.min(m, pieceShape[i][1]);
        }
        return m;
    }

    /**
     * @return  int  the maximum x-value of the shape
     */
    public int maxX()
    {
        int m = pieceShape[0][0];
        for (int i=0; i < 4; i++) {
            m = Math.max(m, pieceShape[i][0]);
        }
        return m;
    }   

    /**
     * @return  int  the maximum y-value of the shape
     */
    public int maxY()
    {
        int m = pieceShape[0][1];
        for (int i=0; i < 4; i++) {
            m = Math.max(m, pieceShape[i][1]);
        }
        return m;
    }   

    /**
     * Rotates the shape 90 degrees to the right
     * and resets its inital reference point
     * to (0,0)
     * 
     * @return  Shape  the rotated shape
     */
    public Shape rotateRight()
    {
        Shape newShape = new Shape(this.type);
        newShape.pieceShape = this.pieceShape;

        int[][]oldPieceShape = new int[4][2];
        
        //Because shapeTable only contains the initial configurations of the shapes
        //and not all of their rotated configurations, I used oldPieceShape to 
        //contain the coordinates of shapes that had already been rotated and were 
        //thus not in shapeTable.
        
        for(int i = 0; i < 4; i++)
        {
            oldPieceShape[i][0] = this.pieceShape[i][0];
            oldPieceShape[i][1] = this.pieceShape[i][1];
        }

        //rotation
        for(int i = 0; i < 4; i++)
        {
            int newY = -newShape.pieceShape[i][0];
            newShape.pieceShape[i][0] = newShape.pieceShape[i][1];
            newShape.pieceShape[i][1] = newY;
        }

        //reseting reference point to (0, 0)
        if(newShape.minX() < 0)
        {
            int incrX = Math.abs(newShape.minX());
            for(int i = 0; i < 4; i++)
            {
                newShape.pieceShape[i][0] += incrX;
            }
        }
        if(newShape.minY() < 0)
        {
            int incrY = Math.abs(newShape.minY());
            for(int i = 0; i < 4; i++)
            {
                newShape.pieceShape[i][1] += incrY;
            }
        }

        this.pieceShape = oldPieceShape;

        return newShape;
    }

    /**
     * Accessor method for instance variable type
     * 
     * @return  int  Shape type
     */
    public int getShapeType()
    {
        return type;
    }
    
    /**
     * Accessor method for instance variable pieceShape
     * 
     * @return  int[][]  coordinates of shape's populated squares
     */
    public int[][] getShape() 
    { 
        return pieceShape;
    }
}