package sample;

public class Token {
    public int tokenNo;
    public String value="";
    public int lineNo;



    public Token(int tokenNo){
        this.tokenNo = tokenNo;
    }
    public Token(int tokenNo,int lineNo){
        this.tokenNo = tokenNo;
        this.lineNo = lineNo;
    }
    public Token(int tokenNo,String value,int lineNo){
        this.tokenNo = tokenNo;
        this.value = value;
        this.lineNo = lineNo;
    }
    public static final int START = 0;
    /** if */
    public static final int IF = 1;
    /** else */
    public static final int ELSE = 2;
    /** while */
    public static final int WHILE = 3;
    /** read */
    public static final int READ = 4;
    /** write */
    public static final int WRITE = 5;
    /** int */
    public static final int INT = 6;
    /** double */
    public static final int DOUBLE = 7;
    /** string */
    public static final int STRING = 8;
    /** + */
    public static final int PLUS = 9;
    /** - */
    public static final int MINUS = 10;
    /** * */
    public static final int MUL = 11;
    /** / */
    public static final int DIV = 12;
    /** = */
    public static final int ASSIGN = 13;
    /** < */
    public static final int LT = 14;
    /** == */
    public static final int EQ = 15;
    /** <> */
    public static final int NEQ = 16;
    /** ( */
    public static final int LPARENT = 17;
    /** ) */
    public static final int RPARENT = 18;
    /** ; */
    public static final int SEMI = 19;
    /** { */
    public static final int LBRACE = 20;
    /** } */
    public static final int RBRACE = 21;
    //    /** /* */
    public static final int LCOM = 22;
    //    /** *\/ */
    public static final int RCOM = 23;
    //    /** // */
    public static final int SCOM = 24;
    /** [ */
    public static final int LBRACKET = 25;
    /** ] */
    public static final int RBRACKET = 26;
    /** <= */
    public static final int LET = 27;
    /** > */
    public static final int GT = 28;
    /** >= */
    public static final int GET = 29;
    /** 标识符,由数字,字母或下划线组成,第一个字符不能是数字 */
    public static final int ID = 30;
    /** int型字面值 */
    public static final int LITERAL_INT = 31;
    /** double型字面值 */
    public static final int LITERAL_DOUBLE = 32;
    /** string型字面值 */
    public static final int LITERAL_STRING = 33;
    /** 逻辑表达式 */
    public static final int LOGIC_EXP = 34;
    /** 多项式 */
    public static final int ADDTIVE_EXP = 35;
    /** 项 */
    public static final int TERM_EXP = 36;
    /** String式 */
    public static final int STRING_EXP = 37;

    public static final int RETURN = 38;
    public static final int VOID = 39;
    public static final int PLUSPLUS = 40;
    public static final int PLUSEQUAL = 41;
    public static final int MINUSMINUS = 42;
    public static final int MINUSEQUAL = 43;
    public static final int MULTIEQUAL = 44;
    public static final int DIVEQUAL = 45;//grammar暂时不支持++，--好了

    public static final int COMMA = 46;
}