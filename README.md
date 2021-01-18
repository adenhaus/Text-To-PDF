# Text-To-PDF
> A Java program that takes input from a .txt file and produces a formatted .pdf file based on commands within the .txt file.

#### Files
In this repo you will find:
* The project folder, txttopdf
* A vscode folder which contains the settings.json file
* Other miscellaneous files I created while working on the project

#### The Program
###### The Task
To take a plain text file that contains paragraphs, some of which start contain a command, for example ".bold" and format the normal paragraphs according to these commands, then output a .pdf file.
###### The PDF
I used the iText library, version 7.1.3, to format and create the PDF document
###### Documentation
It's a Maven build and I created a Javadoc
###### Improvements
The program is fully functional and includes a system test. I still need to:
* Simplify the main algorithm which implements the core logic of the program
* Refactor, creating more methods and distributing functionality
* Write unit tests

*Version 1.0*
