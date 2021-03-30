//Aaryan Pagar 2597573P

package detectors;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Driver class calls uselessControlFlowDetector and recursionDetector
 * Please note that the line that initializes filePath with the jar argument is commented out due to a JVM issue on my laptop.
 * To check for correct deliverable form please refer to the pom.xml file.
 * To execute the jar file uncomment the 'args[0]' line and delete the String initialization on top of it.
 * To manually change the file path to test a particular class, replace the 'filePath' String initialization with an appropriate file path.
 */
public class Driver {

    private static File file;
    private static String filePath = "";
    private static String className;

    public static void main(String[] args){
        try{
            //please replace with your file path
            filePath = "C:\\Users\\Aaryan Pagar\\Desktop\\Calculator.java";
            //filePath = args[0];
            file = new File(filePath);
            className = file.getName().split("\\.")[0];

            runUCF();
            System.out.println("Useless Control Flows: ");
            for(String s : Breakpoints.getUselessControlFlows())
                System.out.println("className = "+className+s);

            runRecursion();
            System.out.println("Recursions: ");
            for(String s : Breakpoints.getRecursion())
                System.out.println("className = "+className+s);
        } catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Please enter a file path");
        }
    }

    public static void runUCF(){
        try{
            CompilationUnit compilationUnit = StaticJavaParser
                    .parse(new FileInputStream(file));
            VoidVisitor<?> visitor = new UselessControlFlowDetector();
            visitor.visit(compilationUnit, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void runRecursion(){
        try{
            CompilationUnit compilationUnit = StaticJavaParser
                    .parse(new FileInputStream(file));
            VoidVisitor<?> visitor = new RecursionDetector();
            visitor.visit(compilationUnit, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
