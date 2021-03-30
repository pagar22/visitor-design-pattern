//Aaryan Pagar 2597573P

package detectors;

import java.util.ArrayList;
import java.util.List;

public class Breakpoints {
    protected static List<String> uselessControlFlows = new ArrayList<>();
    protected static List<String> recursionStatements = new ArrayList<>();

    public static void addUCF(String methodName, int startLine, int endLine){
        String concatenate = ", methodName = "+methodName
                +", startline = "+startLine+", endline = "+endLine;
        uselessControlFlows.add(concatenate);
    }
    public static List<String> getUselessControlFlows(){
        return uselessControlFlows;
    }

    public static void addRecursion(String methodName, int startLine, int endLine){
        String concatenate = ", methodName = "+methodName
                +", startline = "+startLine+", endline = "+endLine;
        recursionStatements.add(concatenate);
    }
    public static List<String> getRecursion(){return recursionStatements;}
}
