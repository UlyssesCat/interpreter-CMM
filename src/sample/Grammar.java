package sample;

import javafx.fxml.FXML;
import jdk.nashorn.internal.runtime.ParserException;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

public class Grammar {
    static ArrayList<Token> tokenList;
    static ListIterator<Token> iterator;//tokenList的迭代器
    static LinkedList<TreeNode> treeNodeList= new LinkedList<>();//TreeNode链表
    static Token currentToken;//当前的token
    static TreeNode root = null;
    static String GraError="";
    public static LinkedList<TreeNode> graAnalysis(ArrayList<Token> tokens){
        if(treeNodeList.size()!=0)
            for(int i=0;i<treeNodeList.size();i++)
                treeNodeList.remove(i);
        tokenList=tokens;
        iterator = tokenList.listIterator();
        TreeNode node = new TreeNode(TreeNode.PROGRAM);
        TreeNode tmp=null;
        treeNodeList.add(node);
        while(iterator.hasNext()){
            tmp=parseStmt();
            node.mNext=tmp;
            node=tmp;
        }
        Show();
        root = treeNodeList.getFirst();
        return treeNodeList;
    }
//    private static TreeNode parseStmt() throws ParserException {
//        TreeNode node = new TreeNode(TreeNode.PARSE_STMT);
//        node.mLeft=parseeStmt();
//        return node;
//    }

    private static TreeNode parseStmt() throws ParserException {
        switch (getNextTokenType()) {
            case Token.IF: return parseIfStmt();
            case Token.WHILE: return parseWhileStmt();
            case Token.READ: return parseReadStmt();
            case Token.WRITE: return parseWriteStmt();
            case Token.INT:
            case Token.DOUBLE:
            case Token.STRING:
            case Token.VOID: return parseDeclareStmt();//变量声明+函数声明
            case Token.LBRACE: return parseStmtBlock();
            case Token.ID: return parseAssignStmt();
            case Token.RETURN: return parseReturnStmt();
            default:
                GraError+=("line " + getNextTokenLineNo() + " : expected token");
                if(iterator.hasNext())  currentToken = iterator.next();
                GraError+="\n";
                return new TreeNode(TreeNode.WRONG);
        }
    }//stmt-block
    private static TreeNode parseReturnStmt() throws ParserException {
        TreeNode node = new TreeNode(TreeNode.RETURN_STMT);
        consumeNextToken(Token.RETURN);//消耗一个return
        node.mLeft=parseExp();//exp
        consumeNextToken(Token.SEMI);
        return node;
    }
    private static TreeNode parseIfStmt() throws ParserException {
        TreeNode node = new TreeNode(TreeNode.IF_STMT);
        consumeNextToken(Token.IF);//消耗一个if
        consumeNextToken(Token.LPARENT);//消耗一个左括号
        node.mLeft=parseExp();//exp
        consumeNextToken(Token.RPARENT);
        node.mMiddle=parseStmt();//stmt-block
        if (getNextTokenType() == Token.ELSE) {
            consumeNextToken(Token.ELSE);
            node.mRight=parseStmt();//stmt-block
        }
        return node;
    }
    private static TreeNode parseWhileStmt() throws ParserException {
        TreeNode node = new TreeNode(TreeNode.WHILE_STMT);
        consumeNextToken(Token.WHILE);//消耗一个while
        consumeNextToken(Token.LPARENT);//消耗一个左括号
        node.mLeft=parseExp();//exp
        consumeNextToken(Token.RPARENT);
        node.mMiddle=parseStmt();//stmt-block
        return node;
    }
    private static TreeNode parseReadStmt() throws ParserException {
        TreeNode node = new TreeNode(TreeNode.READ_STMT);
        consumeNextToken(Token.READ);
        node.mLeft=variableName();
        consumeNextToken(Token.SEMI);
        return node;
    }
    private static TreeNode parseWriteStmt() throws ParserException {
        TreeNode node = new TreeNode(TreeNode.WRITE_STMT);
        consumeNextToken(Token.WRITE);
        node.mLeft=parseExp();
        consumeNextToken(Token.SEMI);
        return node;
    }
    private static TreeNode parseDeclareStmt() throws ParserException{
        if(checkNextTokenType(Token.INT, Token.DOUBLE, Token.STRING, Token.VOID)){//暂时先不分开int double string 和 void吧
            if(iterator.hasNext())  currentToken = iterator.next();
            iterator.next();//foo
            Token tmp = iterator.next();//函数or变量
            iterator.previous();iterator.previous();//iterator.previous();//少移动一个
            if(tmp.tokenNo==Token.LPARENT){//函数声明
                return parseDeclareFunStmt();
            }else {//变量声明
                return parseDeclareVarStmt();
            }
        }else{
            while(iterator.hasNext()){
                currentToken = iterator.next();
                if(currentToken.tokenNo==Token.RBRACE||currentToken.tokenNo==Token.RPARENT||currentToken.tokenNo==Token.SEMI) {
                    break;
                }
            }
            GraError+=("line " + getNextTokenLineNo() + " : next token should be variable type");
            GraError+="\n";
            return  new TreeNode(TreeNode.WRONG);
            //throw new ParserException("line " + getNextTokenLineNo() + " : next token should be variable type");
        }

    }
    private static TreeNode parseDeclareFunStmt() throws ParserException{//函数声明
        TreeNode node = new TreeNode(TreeNode.DECLARE_FUN_STMT);
        TreeNode varNode = new TreeNode(TreeNode.FUN);//存储返回值类型和函数名
        //if(checkNextTokenType(Token.INT, Token.DOUBLE, Token.STRING, Token.VOID)){
        if(currentToken.tokenNo==Token.INT||currentToken.tokenNo==Token.DOUBLE||currentToken.tokenNo==Token.STRING||currentToken.tokenNo==Token.VOID){
            int type = currentToken.tokenNo;
            if(type==Token.INT){
                varNode.mDataType=Token.INT;
            }else if(type==Token.DOUBLE){
                varNode.mDataType=Token.DOUBLE;
            }else if(type==Token.STRING){
                varNode.mDataType= Token.STRING;
            }else{
                varNode.mDataType= Token.VOID;
            }
        }else{
            while(iterator.hasNext()){
                currentToken = iterator.next();
                if(currentToken.tokenNo==Token.RBRACE||currentToken.tokenNo==Token.RPARENT||currentToken.tokenNo==Token.SEMI) {
                    break;
                }
            }
            GraError+=("line " + getNextTokenLineNo() + " : next token should be variable type");
            GraError+="\n";
            return  new TreeNode(TreeNode.WRONG);
        }
        if(checkNextTokenType(Token.ID)){
            if(iterator.hasNext())  currentToken = iterator.next();
            varNode.value=currentToken.value;
        }else {
            while(iterator.hasNext()){
                currentToken = iterator.next();
                if(currentToken.tokenNo==Token.RBRACE||currentToken.tokenNo==Token.RPARENT||currentToken.tokenNo==Token.SEMI) {
                    break;
                }
            }
            GraError+=("line " + getNextTokenLineNo() + " : next token should be ID");
            GraError+="\n";
            return  new TreeNode(TreeNode.WRONG);
        }
        node.mLeft=varNode;
        consumeNextToken(Token.LPARENT);
        node.mMiddle=parseParams();//有参数 会新建节点 否则无
        consumeNextToken(Token.RPARENT);
        node.mRight=parseStmtBlock();

        return node;
    }
    private static TreeNode parseParams() throws ParserException{
        if(checkNextTokenType(Token.RPARENT))   return new TreeNode(TreeNode.NULL);
        else if(checkNextTokenType(Token.INT, Token.DOUBLE, Token.STRING)){

            TreeNode node = new TreeNode(TreeNode.NULL);
            TreeNode header = node;
            TreeNode temp= null;

            while(getNextTokenType()!=Token.RPARENT){
                temp=parseParam();
                node.mNext=temp;
                node=temp;
                if(getNextTokenType()==Token.COMMA)
                    consumeNextToken(Token.COMMA);
            }
            return header;

        }else{
            while(iterator.hasNext()){
                currentToken = iterator.next();
                if(currentToken.tokenNo==Token.RBRACE||currentToken.tokenNo==Token.RPARENT||currentToken.tokenNo==Token.SEMI) {
                    break;
                }
            }
            GraError+=("line " + getNextTokenLineNo() + " : wrong params");
            GraError+="\n";
            return  new TreeNode(TreeNode.WRONG);
            //throw new ParserException("line " + getNextTokenLineNo() + " : wrong params");
        }
    }
    private static TreeNode parseParam() throws ParserException{
        TreeNode node = new TreeNode(TreeNode.PARAM);

        if(checkNextTokenType(Token.INT, Token.DOUBLE, Token.STRING)){
            if(iterator.hasNext())  currentToken = iterator.next();
            int type = currentToken.tokenNo;
            if(type==Token.INT){
                node.mDataType=Token.INT;
            }else if(type==Token.DOUBLE){
                node.mDataType=Token.DOUBLE;
            }else if(type==Token.STRING){
                node.mDataType= Token.STRING;
            }else{
                node.mDataType= Token.VOID;
            }
        }else{
            while(iterator.hasNext()){
                currentToken = iterator.next();
                if(currentToken.tokenNo==Token.RBRACE||currentToken.tokenNo==Token.RPARENT||currentToken.tokenNo==Token.SEMI) {
                    break;
                }
            }
            GraError+=("line " + getNextTokenLineNo() + " : next token should be variable type");
            GraError+="\n";
            return  new TreeNode(TreeNode.WRONG);
            //throw new ParserException("line " + getNextTokenLineNo() + " : next token should be variable type");
        }

        if(checkNextTokenType(Token.ID)){
            if(iterator.hasNext())  currentToken = iterator.next();
            node.value=currentToken.value;
        }else {
            while(iterator.hasNext()){
                currentToken = iterator.next();
                if(currentToken.tokenNo==Token.RBRACE||currentToken.tokenNo==Token.RPARENT||currentToken.tokenNo==Token.SEMI) {
                    break;
                }
            }
            GraError+=("line " + getNextTokenLineNo() + " : next token should be ID");
            GraError+="\n";
            return  new TreeNode(TreeNode.WRONG);
            //throw new ParserException("line " + getNextTokenLineNo() + " : next token should be ID");
        }

        return node;
    }
    private static TreeNode parseDeclareVarStmt() throws ParserException {
        TreeNode node = new TreeNode(TreeNode.DECLARE_VAR_STMT);
        TreeNode varNode = new TreeNode(TreeNode.VAR);//存储变量名和类型
        if(currentToken.tokenNo==Token.INT||currentToken.tokenNo==Token.DOUBLE||currentToken.tokenNo==Token.STRING){
            if(iterator.hasNext())  currentToken = iterator.next();
            int type = currentToken.tokenNo;
            currentToken = iterator.previous();
            if(type==Token.INT){
                varNode.mDataType=Token.INT;
            }else if(type==Token.DOUBLE){
                varNode.mDataType=Token.DOUBLE;
            }else{
                varNode.mDataType= Token.STRING;
            }
        }else{
            while(iterator.hasNext()){
                currentToken = iterator.next();
                if(currentToken.tokenNo==Token.RBRACE||currentToken.tokenNo==Token.RPARENT||currentToken.tokenNo==Token.SEMI) {
                    break;
                }
            }
            GraError+=("line " + getNextTokenLineNo() + " : next token should be variable type");
            GraError+="\n";
            return  new TreeNode(TreeNode.WRONG);
            //throw new ParserException("line " + getNextTokenLineNo() + " : next token should be variable type");
        }
        if(checkNextTokenType(Token.ID)){
            if(iterator.hasNext())  currentToken = iterator.next();
            varNode.value=currentToken.value;
        }else {
            while(iterator.hasNext()){
                currentToken = iterator.next();
                if(currentToken.tokenNo==Token.RBRACE||currentToken.tokenNo==Token.RPARENT||currentToken.tokenNo==Token.SEMI) {
                    break;
                }
            }
            GraError+=("line " + getNextTokenLineNo() + " : next token should be ID");
            GraError+="\n";
            return  new TreeNode(TreeNode.WRONG);
            //throw new ParserException("line " + getNextTokenLineNo() + " : next token should be ID");
        }
        if (getNextTokenType() == Token.ASSIGN) {//单个元素可以声明并赋值
            consumeNextToken(Token.ASSIGN);
            node.mMiddle=parseExp();
        } else if (getNextTokenType() == Token.LBRACKET) {//数组元素要先声明后赋值
            consumeNextToken(Token.LBRACKET);
            varNode.mLeft=parseExp();
            consumeNextToken(Token.RBRACKET);
        }else{}
        consumeNextToken(Token.SEMI);
        node.mLeft=varNode;
        return node;
    }
    private static TreeNode parseStmtBlock() throws ParserException {
        TreeNode node = new TreeNode(TreeNode.NULL);
        TreeNode header = node;
        TreeNode temp= null;
        consumeNextToken(Token.LBRACE);
        while(getNextTokenType()!=Token.RBRACE){
            temp=parseStmt();
            node.mNext=temp;
            node=temp;
        }
        consumeNextToken(Token.RBRACE);
        return header;
    }
    private static TreeNode parseAssignStmt() throws ParserException {
        TreeNode node = new TreeNode(TreeNode.ASSIGN_STMT);
        node.mLeft=variableName();
        consumeNextToken(Token.ASSIGN);
        node.mMiddle=parseExp();
        consumeNextToken(Token.SEMI);

        return node;
    }
    private static TreeNode parseExp() throws ParserException {
        TreeNode node = new TreeNode(TreeNode.EXP);
        node.mDataType=Token.LOGIC_EXP;
        TreeNode leftNode = addtiveExp();//exp  or  exp <> exp2

        if(checkNextTokenType(Token.EQ, Token.NEQ, Token.GT, Token.GET, Token.LT, Token.LET)){
            node.mLeft=leftNode;
            node.mMiddle=logicalOp();
            node.mRight=addtiveExp();
            return node;
        }else{
            return leftNode;
        }

    }
    private static TreeNode addtiveExp() throws ParserException {
        TreeNode node = new TreeNode(TreeNode.EXP);
        node.mDataType=Token.ADDTIVE_EXP;
        TreeNode leftNode = term();

        if (checkNextTokenType(Token.PLUS,Token.MINUS)) {
            node.mLeft=leftNode;
            node.mMiddle=addtiveOp();
            node.mRight=addtiveExp();
            return node;
        } else {
            return leftNode;
        }
    }
    private static TreeNode term() throws ParserException {
        TreeNode node = new TreeNode(TreeNode.EXP);
        node.mDataType=Token.TERM_EXP;
        TreeNode leftNode = factor();

        if (checkNextTokenType(Token.MUL, Token.DIV)) {
            node.mLeft=leftNode;
            node.mMiddle=multiplyOp();
            node.mRight=term();
            return node;
        } else {
            return leftNode;
        }
    }
    private static TreeNode factor() throws ParserException {
        if (iterator.hasNext()) {
            TreeNode expNode = new TreeNode(TreeNode.FACTOR);
            switch (getNextTokenType()) {
                case Token.LPARENT://(exp)
                    consumeNextToken(Token.LPARENT);
                    expNode = parseExp();
                    consumeNextToken(Token.RPARENT);
                    break;
                case Token.LITERAL_INT:
                case Token.LITERAL_DOUBLE:
                    expNode.mLeft=literal();
                    break;
                case Token.MINUS://+a
                    expNode.mDataType=Token.MINUS;
                    if(iterator.hasNext())  currentToken = iterator.next();
                    expNode.mLeft=term();
                    break;
                case Token.PLUS://-a
                    expNode.mDataType=Token.PLUS;
                    if(iterator.hasNext())  currentToken = iterator.next();
                    expNode.mLeft=term();
                    break;
                default:// [a]+b
                    //返回的不是expNode
                    return variableName();
            }
            return expNode;
        }
        while(iterator.hasNext()){
            currentToken = iterator.next();
            if(currentToken.tokenNo==Token.RBRACE||currentToken.tokenNo==Token.RPARENT||currentToken.tokenNo==Token.SEMI) {
                break;
            }
        }
        GraError+=("line " + getNextTokenLineNo() + " : next token should be factor");
        GraError+="\n";
        return  new TreeNode(TreeNode.WRONG);
        //throw new ParserException("line " + getNextTokenLineNo() + " : next token should be factor");
    }
    private static TreeNode literal() throws ParserException {//实际值节点
        if (iterator.hasNext()) {
            currentToken = iterator.next();
            int type = currentToken.tokenNo;
            TreeNode node = new TreeNode(TreeNode.LITERAL);
            node.mDataType=type;
            node.value=currentToken.value;
            if (type == Token.LITERAL_INT || type == Token.LITERAL_DOUBLE|| type == Token.LITERAL_STRING) {
                return node;
            } else {
                // continue execute until throw
            }
        }
        while(iterator.hasNext()){
            currentToken = iterator.next();
            if(currentToken.tokenNo==Token.RBRACE||currentToken.tokenNo==Token.RPARENT||currentToken.tokenNo==Token.SEMI) {
                break;
            }
        }
        GraError+=("line " + getNextTokenLineNo() + " : next token should be literal value");
        GraError+="\n";
        return  new TreeNode(TreeNode.WRONG);
        //throw new ParserException("line " + getNextTokenLineNo() + " : next token should be literal value");
    }
    private static TreeNode logicalOp() throws ParserException {//== <> >= <= > < 逻辑运算符
        if (iterator.hasNext()) {
            currentToken = iterator.next();
            int type = currentToken.tokenNo;
            if (type == Token.EQ
                    || type == Token.GET
                    || type == Token.GT
                    || type == Token.LET
                    || type == Token.LT
                    || type == Token.NEQ) {
                TreeNode node = new TreeNode(TreeNode.OP);
                node.mDataType=type;
                return node;
            }
        }
        while(iterator.hasNext()){
            currentToken = iterator.next();
            if(currentToken.tokenNo==Token.RBRACE||currentToken.tokenNo==Token.RPARENT||currentToken.tokenNo==Token.SEMI) {
                break;
            }
        }
        GraError+=("line " + getNextTokenLineNo() + " : next token should be logical operator");
        GraError+="\n";
        return  new TreeNode(TreeNode.WRONG);
        //throw new ParserException("line " + getNextTokenLineNo() + " : next token should be logical operator");
    }
    private static TreeNode addtiveOp() throws ParserException {//+ -
        if (iterator.hasNext()) {
            currentToken = iterator.next();
            int type = currentToken.tokenNo;
            if (type == Token.PLUS || type == Token.MINUS) {
                TreeNode node = new TreeNode(TreeNode.OP);
                node.mDataType=type;
                return node;
            }
        }
        while(iterator.hasNext()){
            currentToken = iterator.next();
            if(currentToken.tokenNo==Token.RBRACE||currentToken.tokenNo==Token.RPARENT||currentToken.tokenNo==Token.SEMI) {
                break;
            }
        }
        GraError+=("line " + getNextTokenLineNo() + " : next token should be addtive operator");
        GraError+="\n";
        return  new TreeNode(TreeNode.WRONG);
        //throw new ParserException("line " + getNextTokenLineNo() + " : next token should be addtive operator");
    }
    private static TreeNode multiplyOp() throws ParserException {
        if (iterator.hasNext()) {
            currentToken = iterator.next();
            int type = currentToken.tokenNo;
            if (type == Token.MUL || type == Token.DIV) {
                TreeNode node = new TreeNode(TreeNode.OP);
                node.mDataType=type;
                return node;
            }
        }
        while(iterator.hasNext()){
            currentToken = iterator.next();
            if(currentToken.tokenNo==Token.RBRACE||currentToken.tokenNo==Token.RPARENT||currentToken.tokenNo==Token.SEMI) {
                break;
            }
        }
        GraError+=("line " + getNextTokenLineNo() + " : next token should be multiple operator");
        GraError+="\n";
        return  new TreeNode(TreeNode.WRONG);
        //throw new ParserException("line " + getNextTokenLineNo() + " : next token should be multiple operator");
    }
    private static TreeNode variableName() throws ParserException {
        TreeNode node = new TreeNode(TreeNode.VAR);
        if (checkNextTokenType(Token.ID)) {
            if(iterator.hasNext())  currentToken = iterator.next();
            node.value=currentToken.value;
        } else {
            while(iterator.hasNext()){
                currentToken = iterator.next();
                if(currentToken.tokenNo==Token.RBRACE||currentToken.tokenNo==Token.RPARENT||currentToken.tokenNo==Token.SEMI) {
                    break;
                }
            }
            GraError+=("line " + getNextTokenLineNo() + " : next token should be ID");
            GraError+="\n";
            return  new TreeNode(TreeNode.WRONG);
            // throw new ParserException("line " + getNextTokenLineNo() + " : next token should be ID");
        }
        if (getNextTokenType() == Token.LBRACKET) {//a[0]
            consumeNextToken(Token.LBRACKET);
            node.mLeft=parseExp();
            consumeNextToken(Token.RBRACKET);
        }
        //TODO：if   函数调用    a+foo()   foo可以作为factor   《》   如果返回是个string  是语义分析的内容....
        return node;
    }
    private static int getNextTokenType() {
        if (iterator.hasNext()) {
            int type = iterator.next().tokenNo;
            iterator.previous();
            return type;
        }
        return 0;
    }//获取下一个的tokenNo，如果没有（结束了）返回0
    private static int getNextTokenLineNo() {
        if (iterator.hasNext()) {
            int type = iterator.next().lineNo;
            iterator.previous();
            return type;
        }
        return 0;
    }//获取下一个的lineNo，如果没有（结束了）返回0
    private static void consumeNextToken(int ...type) throws ParserException {
        if (iterator.hasNext()) {
            currentToken = iterator.next();
            /*if (currentToken.tokenNo == type) {
                return;
            }*/
            for (int i : type) {
                if(currentToken.tokenNo==i)
                    return;
            }
        }
        while(iterator.hasNext()){
            currentToken = iterator.next();
            if(currentToken.tokenNo==Token.RBRACE||currentToken.tokenNo==Token.RPARENT||currentToken.tokenNo==Token.SEMI) {
                break;
            }
        }
        GraError+=("line " + currentToken.lineNo + " : next token should be -> operator");
        GraError+="\n";
        //throw new ParserException("line " + currentToken.lineNo + " : next token should be -> operator");
    }
    private static void consumeNextToken(int type) throws ParserException {
        if (iterator.hasNext()) {
            currentToken = iterator.next();
            if (currentToken.tokenNo == type) {
                return;
            }
        }
        //consumeNextToken();
        while(iterator.hasNext()){
            currentToken = iterator.next();
            if(currentToken.tokenNo==Token.RBRACE||currentToken.tokenNo==Token.RPARENT||currentToken.tokenNo==Token.SEMI) {
                break;
            }
        }
        GraError+=("line " + getNextTokenLineNo() + " : next token should be -> " + new Token(type, 0));
        GraError+="\n";
        //throw new ParserException("line " + getNextTokenLineNo() + " : next token should be -> " + new Token(type, 0));
    }//消耗一个tokenNO期望的token
    private static boolean checkNextTokenType(int ... type) {
        if (iterator.hasNext()) {
            int nextType = iterator.next().tokenNo;
            iterator.previous();
            for (int each : type) {
                if (nextType == each) {
                    return true;
                }
            }
        }
        return false;
    }
    private static void Show() {
        System.out.println("treeNodeList:");
        for(TreeNode t : treeNodeList){
            System.out.println(t.toString());
        }
    }

}
