import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Scanner;
class Instruction{
    String opcode="0000";
    public static String BitFix(String a,int bit){
        // 0 = 000
        String out="";
        int leadingBit = bit - a.length();
        for (int i = 0; i <leadingBit ; i++) {
            out+='0';
        }
        int index=0;
        for (int i = leadingBit; i < bit; i++) {
            out+=a.charAt(index++);
        }
        return out;
    }
}
class InvalidInstructionException extends Exception{
    public InvalidInstructionException() {
    }

    @Override
    public String toString() {
        return "Invalid Instruction.";
    }
}
class InvalidImmediateException extends Exception{
    public InvalidImmediateException() {
    }

    @Override
    public String toString() {
        return "Invalid immediate value.";
    }
}
class RType extends Instruction{
    private  String rs="000";
    private  String rt="000";
    private  String rd="000";
    private  String[] reg= {"000","000","000"};
    private  String shamt="0";

    public RType(String opcode,String rs, String rt, String rd, String shamt,int line) throws InvalidImmediateException, InvalidInstructionException {
        if(shamt.charAt(0) > '1' || shamt.charAt(0) <'0' || shamt.length()>1)
            throw  new InvalidImmediateException();
        this.opcode = opcode;
        this.reg[0] = rs;
        this.reg[1] = rt;
        this.reg[2] = rd;
        this.shamt = shamt;
    }
    public RType(String opcode,String rd, String rs, String rt) {
        this.opcode = opcode;
        this.rs = rs;
        this.rt = rt;
        this.rd = rd;
    }

    @Override
    public String toString() {
        return opcode+" "+rs +" "+ rt + " " + rd +" "+ shamt;
    }
}
class IType extends Instruction{
    private final short immediateBits=3;
    private char sign;
    private String rs="000";
    private String rt="000";
    private String immediate="0000";

    public IType(String opcode,String rt, String rs, String immediate,char sign) throws InvalidImmediateException {
        this.opcode = opcode;
        this.rs = rs;
        this.rt = rt;
        if(opcode.equals("1011") || opcode.equals("1100")){
            if(immediate.length()>2)
                throw new InvalidImmediateException();
        }
        if(sign=='1' && !opcode.equals("1001"))
            throw  new InvalidImmediateException();
        else if(immediate.length()<immediateBits)
            this.immediate = BitFix(immediate,immediateBits);
        else if(immediate.length()>immediateBits)
            throw new InvalidImmediateException();
        else
            this.immediate = immediate;
        this.sign=sign;
    }

    @Override
    public String toString() {
        return opcode+" "+rs +" "+ rt + " "+sign+immediate;
    }
}
class JType extends Instruction{
    private short targetBits = 10;
    private String target="0000000000";

    public JType(String opcode,String target) throws InvalidImmediateException {
        this.opcode = opcode;
        if(target.length()<targetBits)
            this.target = BitFix(target,targetBits);
        else if(target.length()>targetBits)
            throw new InvalidImmediateException();
        else
            this.target = target;
    }

    @Override
    public String toString() {
        return opcode+ " " + target;
    }
}
class InstructionHandling {
    public static HashMap<String, String> reg_file = new HashMap();
    public static HashMap<String, String> op_code = new HashMap();

    public static void main(String args[]) throws FileNotFoundException, InvalidImmediateException {
        valueInsertionTo_reg_file_and_opCode();
        PrintStream fileStream = new PrintStream("Output.txt"); // Creates a FileOutputStream
        System.setOut(fileStream);  // all system.out sends data to filestream
        Scanner in = new Scanner(new File("Input.txt")); // getting inputs from Input.txt file using scanner class
        while(in.hasNextLine()){            //reading line by line
            String ins = in.nextLine();
            System.out.println(ins);
            String opcode = ins.substring(0, ins.indexOf(' '));
            ins = ins.substring(ins.indexOf(' '), ins.length()).replaceAll(" ", "");
            String[] regArray = ins.split(",");
            if (op_code.containsKey(opcode)) {
                opcode = op_code.get(opcode);
                if (Integer.parseInt(opcode,2) < 9) {           // our r-type instructions are to 1000 binary
                    String reg1 = reg_file.get(regArray[0]);
                    String reg2 = reg_file.get(regArray[1]);
                    String reg3 = reg_file.get(regArray[2]);
                    System.out.println(new RType(opcode, reg1, reg2, reg3));
                } else if (Integer.parseInt(opcode, 2) < 14) {   // our i-type instructions are to 1101 binary
                    String reg1 = reg_file.get(regArray[0]);
                    if (regArray.length == 3) {
                        String immediate;
                        String reg2 = reg_file.get(regArray[1]);
                        char sign='0';
                        if(opcode.equals("1101")) {
                            immediate = new NumberConversion().HexaToBinary(regArray[2]);
                        }
                        else{
                            int value = Integer.parseInt(regArray[2]);
                            if(value<0)
                                sign='1';
                            immediate = new NumberConversion().DecimalToBinary(value);
                        }
                        try{
                            System.out.println(new IType(opcode,reg1,reg2,immediate,sign));
                        }
                        catch (InvalidImmediateException e){
                            System.out.println(e);
                        }
                    }
                    else {
                        try{
                        String arr[] = regArray[1].split("\\(");
                        int value = Integer.parseInt(arr[0]);
                        char sign='0';
                        if(value<0)
                            sign='1';
                        String immediate = new NumberConversion().DecimalToBinary(Integer.parseInt(arr[0]));
                        String reg2 = reg_file.get(arr[1].substring(0,arr[1].length()-1));

                            System.out.println(new IType(opcode,reg1,reg2,immediate,sign));
                        }
                        catch (InvalidImmediateException e ){
                            System.out.println(e);
                        }
                    }
                }
                // rest are j-type
                else {
                    String target = new NumberConversion().HexaToBinary(regArray[0]);
                    try{
                        System.out.println(new JType(opcode,target));
                    }
                    catch (InvalidImmediateException e){
                        System.out.println(e);
                    }
                }
            }
            else
                try {
                    throw new InvalidInstructionException();
                } catch (InvalidInstructionException e) {
                    System.out.println(e);
                }
        }
    }

    public static void valueInsertionTo_reg_file_and_opCode() {
        //declaring register file
        reg_file.put("$zero", "000");
        reg_file.put("$intr", "001");
        reg_file.put("$outr", "010");
        reg_file.put("$s0", "011");
        reg_file.put("$s1", "100");
        reg_file.put("$t0", "101");
        reg_file.put("$t1", "110");
        reg_file.put("$t2", "111");

        //op-code declaring
        op_code.put("and", "0000");
        op_code.put("or", "0001");
        op_code.put("add", "0010");
        op_code.put("sub", "0011");
        op_code.put("nor", "0100");
        op_code.put("nand", "0101");
        op_code.put("in", "0110");
        op_code.put("out", "0111");
        op_code.put("slt", "1000");
        op_code.put("addi", "1001");
        op_code.put("sll", "1010");
        op_code.put("lw", "1011");
        op_code.put("sw", "1011");
        op_code.put("beq", "1101");
        op_code.put("j", "1110");
    }


}
class NumberConversion{
    public NumberConversion(){
    }
    //Converts Binary String to Decimal Integer value  using java built-in Integer class's method
    public int BinaryToDecimal(String Binary){
        return Integer.parseInt(Binary, 2);
    }
    //Converts Decimal Integer Value to Binary String value  using java built-in Integer class's method
    public String  DecimalToBinary(int decimal){
        if(decimal < 0)
            return Integer.toBinaryString(~decimal+1);
        return Integer.toBinaryString(decimal);
    }
    //Converts Binary String to Hexadecimal String value  using java built-in Integer class's method
    public String BinaryToHexaDecimal(String Binary){
        int decimal = BinaryToDecimal(Binary);
        return Integer.toHexString(decimal);
    }
    //Converts Hexadecimal to Binary String value
    public String HexaToBinary(String Hex){
        return new BigInteger(Hex, 16).toString(2);
    }
}
