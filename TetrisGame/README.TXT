------------------------------------------------------------------------
This is the project README file. Here, you should describe your project.
Tell the reader (someone who does not know anything about this project)
all he/she needs to know. The comments should usually include at least:
------------------------------------------------------------------------

PROJECT TITLE: Tetris
PURPOSE OF PROJECT: Simulates Tetris Game
VERSION or DATE: 4/26/2015
HOW TO START THIS PROJECT: Run main method of Tetris class
AUTHORS: Davis Zhang
USER INSTRUCTIONS: Arrow Keys to move pieces, up arrow to rotate,
down for soft drop, spacebar for hard drop

Overview: I initially wanted to create a Tetris Battle game, but writing Tetris
itself took me way longer than I expected. I had originally hoped to use a 
tutorial to help me, but that turned out to be a waste of time. Although
this program is very loosely based on the code from 
http://zetcode.com/tutorials/javagamestutorial/tetris/, I have majorly
edited or rewritten all of the methods, added some of my own, and changed 
the system for manipulating the tetrominoes.

Explanation of concepts: Writing this game required nested arrays, which also
went hand in hand with nested loops. Object orient design was also important
because of the multiple classes necessary to write this program. Additional
research was needed draw and animate the graphics of the game.

Further explanation of methods:

While I think most of the methods are pretty straightforward, the tryMove,
removeFullLines, and tryRotation methods contain an awkward workaround 
to a problem I was unable to fix more elegantly. Because I did not know how
to program with multiple threads, the automatic execution of the oneLineDown
method interfered with tryMove, removeFullLines, and tryRotation. My solution
was to temporarily remove curPiece while those methods ran, then put curPiece
back after they were finished.