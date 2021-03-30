//Aaryan Pagar 2597573P

package detectors;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;


public class RecursionDetector extends VoidVisitorAdapter <Void>{

    @Override
    public void visit(MethodDeclaration n, Void arg) {
        super.visit(n, arg);
        String methodName = n.getNameAsString();
        for(Statement statement : n.getBody().get().getStatements()){
            statement.removeComment();
            if (statement.toString().contains(methodName)) {
                //start and end line are same (begin) as only header of caller method required
                Breakpoints.addRecursion(methodName, n.getRange().get().begin.line,
                        n.getRange().get().begin.line);
            }
        }
    }
}
