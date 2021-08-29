
public class Main {
    public static void main(String args[]) {

    }
}
class InstructionHandling{
    String opcode="0000";
    String rs="0000";
    String rt="0000";
    String shamt="00";
    int immediate=0;
    String instruction="add $t0,$t1";
    InstructionHandling(){};
    InstructionHandling(String instruction){
        this.instruction = instruction;
    }
    public void operation(){
        opcode = instruction.split(" ")[0];
    }
    public void reg1(){
        this.rs = instruction.split(" ")[1];
        rs = rs.substring(0,rs.indexOf(','));
    }
    public void reg2(){
        String temp = instruction.split(" ")[2];
        temp = temp.substring(0,rs.indexOf(','));
        try{
            
        }
        catch (Exception e){

        }
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
    public String  DecimalToBinary(int Decimal){
        return Integer.toBinaryString(Decimal);
    }
    //Converts Binary String to Hexadecimal String value  using java built-in Integer class's method
    public String BinaryToHexaDecimal(String Binary){
        int decimal = BinaryToDecimal(Binary);
        return Integer.toHexString(decimal);
    }
}