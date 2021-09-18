import java.io.*;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Scanner;

class Instruction {
    String opcode = "0000";

    public static String BitFix(String a, int bit) {
        String out = "";
        int leadingBit = bit - a.length();
        for (int i = 0; i < leadingBit; i++) {
            out += '0';
        }
        int index = 0;
        for (int i = leadingBit; i < bit; i++) {
            out += a.charAt(index++);
        }
        return out;
    }
}

class InvalidInstructionException extends Exception {
    public InvalidInstructionException() {
    }

    @Override
    public String toString() {
        return "Invalid Instruction.";
    }
}

class InvalidRegisterNameException extends Exception {
    public InvalidRegisterNameException() {
    }

    @Override
    public String toString() {
        return "Invalid Register Name.";
    }
}

class InvalidImmediateException extends Exception {
    public InvalidImmediateException() {
    }

    @Override
    public String toString() {
        return "Invalid immediate value.";
    }
}

class RType extends Instruction {
    private String rs = "000";
    private String rt = "000";
    private String rd = "000";
    private String[] reg = {"000", "000", "000"};
    private String shamt = "0";

    public RType(String opcode, String rs, String rt, String rd, String shamt) throws InvalidImmediateException, InvalidInstructionException {
        if (shamt.charAt(0) != '1' || shamt.charAt(0) != '0' || shamt.length() > 1)
            throw new InvalidImmediateException();
        this.opcode = opcode;
        this.reg[0] = rs;
        this.reg[1] = rt;
        this.reg[2] = rd;
        this.shamt = shamt;
    }

    public RType(String opcode, String rs, String rt, String rd) throws InvalidRegisterNameException {
        this.opcode = opcode;
        this.rs = rs;
        this.rt = rt;
        this.rd = rd;
        if (rs == null || rd == null || rt == null)
            throw new InvalidRegisterNameException();
    }

    @Override
    public String toString() {
        return opcode + rs + rt + rd + shamt;
//        return opcode +" "+ rs +" "+ rt +" "+ rd +" "+ shamt;
    }
    public String HexCode(){
        NumberConversion numberConversion = new NumberConversion();
        return numberConversion.BinaryToHexaDecimal(opcode)+numberConversion.BinaryToHexaDecimal(rs)+numberConversion.BinaryToHexaDecimal(rt)+numberConversion.BinaryToHexaDecimal(rd+shamt);
    }
}

class IType extends Instruction {
    private final short immediateBits = 3;
    private char sign;
    private String rs = "000";
    private String rt = "000";
    private String immediate = "0000";

    public IType(String opcode, String rs, String rt, String immediate, char sign) throws InvalidImmediateException {
        this.opcode = opcode;
        this.rs = rs;
        this.rt = rt;
        if (opcode.equals("1011") || opcode.equals("1100")) {
            if (immediate.length() > 15)
                throw new InvalidImmediateException();
        }
        if (sign == '1' && !opcode.equals("1001"))
            throw new InvalidImmediateException();
        else if (immediate.length() < immediateBits)
            this.immediate = BitFix(immediate, immediateBits);
        else if (immediate.length() > immediateBits)
            throw new InvalidImmediateException();
        else
            this.immediate = immediate;
        if(opcode.equals("1011") || opcode.equals("1010")){
            String temp = rt;
            this.rt = this.rs;
            this.rs = temp;
        }
        this.sign = sign;
    }

    @Override
    public String toString() {
        return opcode + rs + rt + sign + immediate;
//        return opcode + " " +rs + " " +rt +" " + sign +" " + immediate;
    }
    public String HexCode(){
        NumberConversion numberConversion = new NumberConversion();
        return numberConversion.BinaryToHexaDecimal(opcode)+numberConversion.BinaryToHexaDecimal(rs)+numberConversion.BinaryToHexaDecimal(rt)+numberConversion.BinaryToHexaDecimal(sign+immediate);
    }
}

class JType extends Instruction {
    private short targetBits = 10;
    private String target = "0000000000";

    public JType(String opcode, String target) throws InvalidImmediateException {
        this.opcode = opcode;
        if (target.length() < targetBits)
            this.target = BitFix(target, targetBits);
        else if (target.length() > targetBits)
            throw new InvalidImmediateException();
        else
            this.target = target;
    }

    @Override
    public String toString() {
        return opcode + target;
//        return opcode +" "+ target;
    }
    public String HexCode(){
        NumberConversion numberConversion = new NumberConversion();
        String hex = numberConversion.BinaryToHexaDecimal(this.opcode);
        hex += numberConversion.BinaryToHexaDecimal("0"+this.target.substring(0,3));
        hex += numberConversion.BinaryToHexaDecimal("0"+this.target.substring(3,6));
        hex += numberConversion.BinaryToHexaDecimal(this.target.substring(6,10));
        return hex;
    }
}

public class Main {
    public static HashMap<String, String> reg_file = new HashMap();
    public static HashMap<String, String> op_code = new HashMap();

    public static void main(String args[]) {
        valueInsertionTo_reg_file_and_opCode();
        File machineCodeText = new File("outputMachineCode.txt");
        File hexCodeText = new File("outputHexCode.txt");
        try {
            FileWriter fwMachineCode = new FileWriter(machineCodeText);
            FileWriter fwHexCode = new FileWriter(hexCodeText);
            fwHexCode.write("v2.0 raw\n");
//            PrintStream fileStream = new PrintStream("Output.txt"); // Creates a FileOutputStream
//            System.setOut(fileStream);  // all system.out sends data to filestream
            Scanner in = new Scanner(new File("Input.txt")); // getting inputs from Input.txt file using scanner class
            while (in.hasNextLine()) {            //reading line by line
                String ins = in.nextLine();
                String temp = ins;
                try {
                    String opcode = ins.substring(0, ins.indexOf(' '));
                    if (op_code.containsKey(opcode) && opcode.equals("j")) {
                        ins = ins.substring(ins.indexOf(' '), ins.length()).trim();
                        try {
                            opcode = op_code.get(opcode);
                            String target = new NumberConversion().HexaToBinary(ins);
                            JType code = new JType(opcode, target);
                            fwMachineCode.write(code+"\n");
                            fwHexCode.write(code.HexCode()+"\n");
                        } catch (NumberFormatException e) {
                            System.out.print(temp+ " : ");
                            System.out.println("Invalid target.");
                            e.printStackTrace();
                        } finally {
                            continue;
                        }
                    } else if (op_code.containsKey(opcode) && opcode.equals("in")) {
                        ins = ins.substring(ins.indexOf(' '), ins.length()).trim();
                        try {
                            if (reg_file.containsKey(ins)) {
                                RType code = new RType(op_code.get(opcode), "000", "000", reg_file.get(ins));
                                fwMachineCode.write(code+"\n");
                                fwHexCode.write(code.HexCode()+"\n");
                            } else
                                throw new InvalidRegisterNameException();
                        } catch (InvalidRegisterNameException e) {
                            System.out.print(temp+ " : ");
                            System.out.println(e);
                            e.printStackTrace();
                        } finally {
                            continue;
                        }
                    } else if (op_code.containsKey(opcode) && opcode.equals("out")) {
                        ins = ins.substring(ins.indexOf(' '), ins.length()).trim();
                        try {
                            if (reg_file.containsKey(ins)) {
                                RType code = new RType(op_code.get(opcode), "000", reg_file.get(ins), "000");

                                fwMachineCode.write(code+"\n");
                                fwHexCode.write(code.HexCode()+"\n");
                            } else
                                throw new InvalidRegisterNameException();
                        } catch (InvalidRegisterNameException e) {
                            System.out.print(temp+ " : ");
                            System.out.println(e);
                            e.printStackTrace();
                        } finally {
                            continue;
                        }
                    }
                    ins = ins.substring(ins.indexOf(' '), ins.length()).replaceAll("\\s+ ", "");
                    String[] regArray = ins.split(",");
                    if (op_code.containsKey(opcode)) {
                        opcode = op_code.get(opcode);
                        if (Integer.parseInt(opcode, 2) < 9) {           // our r-type instructions are to 1000 binary
                            try {
                                for (int i = 0; i <3 ; i++) {
                                    regArray[i] = regArray[i].trim();
                                    if(!reg_file.containsKey(regArray[i]))
                                        throw new InvalidRegisterNameException();
                                }
                                String reg1 = reg_file.get(regArray[0]);
                                String reg2 = reg_file.get(regArray[1]);
                                String reg3 = reg_file.get(regArray[2]);
                                RType code = new RType(opcode, reg1, reg2, reg3);
                                fwMachineCode.write(code+"\n");
                                fwHexCode.write(code.HexCode()+"\n");
                            } catch (Exception e) {
                                System.out.print(temp+ " : ");
                                System.out.println(e);
                                e.printStackTrace();
                            }
                        } else if (Integer.parseInt(opcode, 2) < 14) {   // our i-type instructions are to 1101 binary
                            regArray[0] = regArray[0].trim();
                            if(!reg_file.containsKey(regArray[0]))
                                throw  new InvalidRegisterNameException();
                            String reg1 = reg_file.get(regArray[0]);
                            if (regArray.length == 3) {
                                String immediate;
                                regArray[1]= regArray[1].trim();
                                if(!reg_file.containsKey(regArray[1]))
                                    throw  new InvalidRegisterNameException();
                                String reg2 = reg_file.get(regArray[1]);
                                char sign = '0';
                                if (opcode.equals("1101")) {
                                    immediate = new NumberConversion().HexaToBinary(regArray[2]);
                                } else {
                                    int value = Integer.parseInt(regArray[2].trim());
                                    if (value < 0) {
                                        sign = '1';
                                    }
                                    immediate = new NumberConversion().DecimalToBinary(value);
                                }
                                try {
                                    IType code = new IType(opcode, reg1, reg2, immediate, sign);
                                    fwMachineCode.write(code+"\n");
                                    fwHexCode.write(code.HexCode()+"\n");
                                } catch (InvalidImmediateException e) {
                                    System.out.print(temp+ " : ");
                                    System.out.println(e);
                                    e.printStackTrace();
                                }
                            } else {
                                try {
                                    String arr[] = regArray[1].split("\\(");
                                    int value = Integer.parseInt(arr[0].trim());
                                    char sign = '0';
                                    if (value < 0) {
                                        sign = '1';
                                    }
                                    String immediate = new NumberConversion().DecimalToBinary(Integer.parseInt(arr[0].trim()));
                                    String reg2 = reg_file.get(arr[1].substring(0, arr[1].length() - 1));
                                    if (!reg_file.containsKey(arr[1].substring(0, arr[1].length() - 1)))
                                        throw new InvalidInstructionException();
                                    IType code = new IType(opcode, reg1, reg2, immediate, sign);
                                    fwMachineCode.write(code+"\n");
                                    fwHexCode.write(code.HexCode()+"\n");
                                }
                                catch (Exception e) {
                                    System.out.print(temp+ " : ");
                                    System.out.println(e);
                                    e.printStackTrace();
                                }
                            }
                        }
                        // rest are j-type
                        else {
                            System.out.print(temp+ " : ");
                            System.out.println("Invalid Instruction");
                        }
                    } else
                        try {
                            throw new InvalidInstructionException();
                        } catch (InvalidInstructionException e) {
                            System.out.print(temp+ " : ");
                            System.out.println(e);
                            e.printStackTrace();
                        }
                } catch (Exception e) {
                    System.out.print(temp+ " : ");
                    System.out.println("Invalid Instruction");
                    e.printStackTrace();
                }
            }
            fwMachineCode.close();
            fwHexCode.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void valueInsertionTo_reg_file_and_opCode() {
        //declaring register file
        reg_file.put("$zero", "000");
        reg_file.put("$inpr", "001");
        reg_file.put("$outr", "010");
        reg_file.put("$shanto", "011");
        reg_file.put("$sheam", "100");
        reg_file.put("$t0", "101");
        reg_file.put("$tanzil", "110");
        reg_file.put("$tahsin", "111");

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
        op_code.put("sw", "1100");
        op_code.put("beq", "1101");
        op_code.put("j", "1110");

        reg_file.put(" $zero", "000");
        reg_file.put(" $inpr", "001");
        reg_file.put(" $outr", "010");
        reg_file.put(" $shanto", "011");
        reg_file.put(" $sheam", "100");
        reg_file.put(" $t0", "101");
        reg_file.put(" $tanzil", "110");
        reg_file.put(" $tahsin", "111");

        //op-code declaring
        op_code.put(" and", "0000");
        op_code.put(" or", "0001");
        op_code.put(" add", "0010");
        op_code.put(" sub", "0011");
        op_code.put(" nor", "0100");
        op_code.put(" nand", "0101");
        op_code.put(" in", "0110");
        op_code.put(" out", "0111");
        op_code.put(" slt", "1000");
        op_code.put(" addi", "1001");
        op_code.put(" sll", "1010");
        op_code.put(" lw", "1011");
        op_code.put(" sw", "1100");
        op_code.put(" beq", "1101");
        op_code.put(" j", "1110");
    }
}

class NumberConversion {
    public NumberConversion() {
    }

    //Converts Binary String to Decimal Integer value  using java built-in Integer class's method
    public int BinaryToDecimal(String Binary) {
        return Integer.parseInt(Binary, 2);
    }

    //Converts Decimal Integer Value to Binary String value  using java built-in Integer class's method
    public String DecimalToBinary(int decimal) {
        if (decimal < 0)
            return Integer.toBinaryString(~decimal + 1);
        return Integer.toBinaryString(decimal);
    }

    //Converts Binary String to Hexadecimal String value  using java built-in Integer class's method
    public String BinaryToHexaDecimal(String Binary) {
        int decimal = BinaryToDecimal(Binary);
        return Integer.toHexString(decimal);
    }

    //Converts Hexadecimal to Binary String value
    public String HexaToBinary(String Hex) {
        return new BigInteger(Hex, 16).toString(2);
    }
}
