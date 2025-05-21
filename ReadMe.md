[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/YWz5JtMT)
P CSA Final Project - Pathfinding 

For this project students will generate a java program that can search and construct a path using a 2d array of 0s and 1s. The goal is to have the program create a list of coordinates that a robot could take through a maze of 0s and 1s by finding the digit 1 and its neighbors. The path will make at least 1 90 degree turn, meaning the path will not just go straight across the map, it will always end in an adjacent side to the starting wall.



The output list would have the coordinates of the walking path a robot could take to reach the last 1 available. 

String ArrayList Format:
ArrayList<String> answerList = new ArrayList<String>();

Arraylist Contents when you print out answerList: 

{ "A[0][10]", "A[1][10]", "A[2][10]", "A[3][10]", "A[4][10]", "A[5][10]", "A[6][0]", "A[6][1]", "A[6][2]", "A[6][3]", "A[6][4]", "A[6][5]", "A[6][6]", "A[6][7]", "A[6][8]", "A[6][9]", "A[6][10]" }

Note: A is the name of the map given, the coordinates from map A are ordered to begin at the top right A[0][10], but may start from the bottom left as well A[6][0]. 

Code Specifications and Requirements:

There is a chance there are 1s that are not connected to other 1s. 
There will be maps that may contain other characters, your program must follow only the 1s.
1s must connect to other 1s. Do not just find all 1s and add them to a list. 
Your robot cannot move diagonally. 
There will only be one true path per map. 
The path will make a 90 degree turn, beginning on one wall then exiting in an adjacent wall. 
No paths will go straight across, meaning paths will not exit on the wall opposite their beginning. 
Maps will be structured as a 2D Integer Array, a test 2d array is provided below.
Your output should be organized so that it forms a path, do not list 1s by their index order in the map array.
Your program should print the final path you create, format the output to show your coordinates are actually linear and can be followed by a robot. Example of output:
		[ 1 ,   ,   ,   ,  ]
		[ 1 ,   ,   ,   ,  ]
		[ 1 ,   ,   ,   ,  ]
		[ 1 , 1 , 1 , 1 ]

You do not have to use 1s for this purpose.

Grading Rubric:

Program Functionality 
On a scale of 1-5, programs should accept any sized 2d array, output must be formatted as a String ArrayList containing the plot coordinates one by one. Example above.

Program Organization
On a scale of 1-5 Input should be easily provided to one static variable. I should be able to change the input arraylist through copy and pasting into your program at the top of your main method. If your program cannot achieve this, you must provide a comment that denotes where this input should be pasted for testing. However, full points will only be earned by using the global static variable.  

Program Structure
On a scale of 1-5 the program is structured with methods that abstract the functionality of your algorithm, your main method should not contain more than 1 line of code. Limit the use of global variables, you should not have more than 1 static variable to represent the input Integer Arraylist. 

Printing the map
Make sure your program can print the path you are generating, do not print out the 0s or other characters. Only the 1s. Print to the console to show that your path does connect in a line that a robot would be able to follow from one side of the map to the other. 
See example from the specs and reqs above.
