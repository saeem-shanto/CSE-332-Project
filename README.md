# CSE-332-Project 
ISA (Part 1)
 

Title : ISA Design         Type : Document

Consider yourself as a computer architect and you are employed in a vendor company. The company told you that they are going to design a new 14 bit single-cycle CPU that has separate Data and Instruction Memory. The ISA should be general purpose enough to be able to run provided general programs

Input/Output Operations

It should also be able to connect with the display unit (ex: seven segment display) to display result or any data and a keyboard or something similar to get input from the user. you should make any necessary arrangements (extra instruction or special register or any other) to accommodate this external communication into your ISA design. For example, you might consider including dedicated instructions like IN & OUT to perform the input/output operations.

Design requirements

As an ISA designer, your job is to propose a detailed design of the ISA. Few of the issues probably you would have to address are given below:

1) How many operands?

2) Types of operand? (Register based?? Memory-based? Mixed?)

3) How many operations? why?

4) Types of operations? (Arithmetic,logical,branch type?? How many from each category? Draw a table with list of instructions, instruction type, their opcode, functionality (if any)

5) How many formats would you choose? Draw the formats along with field name and number of bits in each field

6) list of registers? Draw a register table. (with register name and values)

7) Addressing Modes


Bechmark Programs

You have to design your ISA focusing on the following three categories of programs

a) Simple arithmetic & logic operations

b) Programs that require checking conditions

c) Loop type of programs



Guideline 
Your assignment will be evaluated according to the following criteria:     
1. Ability to execute the provided benchmark programs, and other general purpose programs?  
2. How much is it different from existing ISAs such as MIPS?
3. How long does it take to run benchmark programs on your processor?
You must answer those with your reasoning. While you are deciding on the above issues, you might consider some sample high-level program that can be run on this CPU using your ISA. Say, during the decisions about the types of operation to include, you can think about the type of high-level language program it will be able to execute. The design might vary from one group to other and there might be multiple possible solutions. You will be scored based on your clear reasoning.


Note: You can check all the previous semester's work to enhance your designing ideas.


 
Assembler (Part 2)                                                

Title : Assembler         Type : Software (Platform : any)
It is difficult and error-prone to manually write machine code. The problem can be addressed by writing an assembler, which can automatically generate a machine code from an assembly file. In this project,  you should write an assembler for your ISA. The assembler reads a program written using assembly language in a text file, then translates it into binary code and generates output file(.txt) containing machine code. The generated output files will later be useful to run a program when you will develop your actual CPU. 

Language:

You can use any high level language. Some demo codes are provided in previous semester' project list. You are strongly advised to use them to save your time. You might need to modify the existing functions/classes to fit in your need. Both Java and C++ based source codes are available here.

ISA:

You should focus on your own ISA that you designed in HW1.

I/O fromat:

the input code will be written in a text file in assembly format following your ISA. There will be one instruction per line. The output will be generated in Hexadecimal format instead of binary. this will be helpful for us to later transfer this code into the RAM block of logisim circuit.

Documentation : You must prepare your own documentation. A sample documentation is available for you here

Demonstration : live through google classroom




Datapath (Part 3)                                                 

Title: Datapath Type: Software (Logisim)
In this part of the project, you have to design a Datapath of your proposed 16-bit architecture. Datapath must have all the necessary components. The components must be adequately connected. It is mandatory to design the control unit at this phase. You can provide manual input to all the control lines if you cant manage the to design control units. But the datapath must be fully operational for all individual instructions proposed.

There will be individual viva on this date.


Processor Testing (Part 4)                                                 

Title: Datapath Type: Software (Logisim)
In this part of the project, you have to demo that your processor is able to solve particular types of high-level problem sets. 3 programming problems will be given that must be solved and demonstrated in 45 min time.

Programs will be of the following type 

a) Simple arithmetic & logic operations

b) Programs that require checking conditions

c) Loop type of programs
