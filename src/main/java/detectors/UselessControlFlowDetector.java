package detectors;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public class UselessControlFlowDetector extends VoidVisitorAdapter <Void> {

    @Override
    public void visit(MethodDeclaration n, Void arg) {
        super.visit(n, arg);
        String methodName = n.getNameAsString();
        finder(n, methodName);
    }

    public static void finder(MethodDeclaration methodDeclaration, String methodName){
        //Lists of all 5 nodes types in spec
        List<IfStmt> ifStmtList = methodDeclaration.findAll(IfStmt.class);
        List<ForStmt> forStmtList = methodDeclaration.findAll(ForStmt.class);
        List<SwitchStmt> switchStmtList = methodDeclaration.findAll(SwitchStmt.class);
        List<WhileStmt> whileStmtList = methodDeclaration.findAll(WhileStmt.class);
        List<DoStmt> doStmtList = methodDeclaration.findAll(DoStmt.class);

        //if statements
        for(int i=0; i<ifStmtList.size(); i++){
            IfStmt ifStmt = ifStmtList.get(i);
            Statement thenStatement = ifStmt.getThenStmt();
            isEmptyChecker(methodName, thenStatement);

            /* get else statement only if it's present and if the statement is last in the list
               the else statement needs to be found only for the last element since all other
               else statements are present as latter elements besides the last one */

            if(i==ifStmtList.size()-1) {
                if (ifStmt.getElseStmt().isPresent()) {
                    Statement elseStatement = ifStmt.getElseStmt().get();
                    isEmptyChecker(methodName, elseStatement);
                }
            }
        }

        //switch statements
        for(SwitchStmt switchStmt : switchStmtList){
            NodeList<SwitchEntry> switchEntryList = switchStmt.getEntries();
            //if no entries (cases), add as breakpoint
            if(switchEntryList.isEmpty())
                Breakpoints.addUCF(methodName, switchStmt.getRange().get().begin.line,
                        switchStmt.getRange().get().end.line);
            else {

                /*get statements for each switch entry, if there are no statements (empty case)
                  OR if the last statement of the case is NOT a break
                  or return statement, add as breakpoint */

                for(SwitchEntry switchEntry : switchEntryList){
                    if(switchEntry.getStatements().isEmpty())
                        Breakpoints.addUCF(methodName, switchEntry.getRange().get().begin.line,
                            switchEntry.getRange().get().end.line);
                    else {
                        Statement finalStatement = switchEntry
                                .getStatement(switchEntry.getStatements().size() - 1);
                        System.out.println(finalStatement);
                        if (!(finalStatement.isBreakStmt()) && !(finalStatement.isReturnStmt()))
                            Breakpoints.addUCF(methodName, switchEntry.getRange().get().begin.line,
                                    switchEntry.getRange().get().end.line);
                    }
                }
            }
        }

        //for statements
        for(ForStmt forStmt : forStmtList) {
            isEmptyChecker(methodName, forStmt.getBody());
        }

        //while statements
        for(WhileStmt whileStmt : whileStmtList) {
            isEmptyChecker(methodName, whileStmt.getBody());
        }

        //dowhile statements
        for(DoStmt doStmt : doStmtList) {
            isEmptyChecker(methodName, doStmt.getBody());
        }
    }

    public static void isEmptyChecker(String methodName, Statement statement){

        /* if statement is an expression or return statement, do nothing (not ucf), else check if
           statement is empty (;) OR type cast to block statement and get list of
           statements inside the block (automatically excludes comments), if this
           list is empty, add as a breakpoint */

        if(statement.isExpressionStmt() || statement.isReturnStmt());
        else {
            if (statement.isEmptyStmt() || ((BlockStmt) statement).getStatements().isEmpty()) {
                int startLine = statement.getRange().get().begin.line;
                int endLine = statement.getRange().get().end.line;
                Breakpoints.addUCF(methodName, startLine, endLine);
            }
        }
    }
}
