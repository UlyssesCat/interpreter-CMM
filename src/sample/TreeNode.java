package sample;

public class TreeNode {
    public TreeNode(int val){
        type = val;
    }



    public String getString(){
        if(type==0) return "NULL";
        if(type==1) return "IF_STMT";
        if(type==2) return "WHILE_STMT";
        if(type==3) return "READ_STMT";
        if(type==4) return "WRITE_STMT";
        if(type==5) return "DECLARE_VAR_STMT";
        if(type==6) return "ASSIGN_STMT";
        if(type==7) return "EXP";
        if(type==8) return "VAR";
        if(type==9) return "OP";
        if(type==10) return "FACTOR";
        if(type==11) return "LITERAL";
        if(type==12) return "DECLARE_FUN_STMT";
        if(type==13) return "FUN";
        if(type==14) return "PARAM";
        if(type==15) return "RETURN_STMT";
        if(type==16) return "PARSE_STMT";
        if(type==17) return "PROGRAM";

        return null;
    }
    /**
     * 语句块使用链表存储,使用NULL类型的TreeNode作为头部,注意不要使用NULL的节点存储信息,仅仅使用next指向下一个TreeNode
     */
    public static final int NULL = 0;
    /**
     * if语句
     * left存放exp表达式
     * middle存放if条件正确时的TreeNode
     * right存放else的TreeNode，如果有的话
     */
    public static final int IF_STMT = 1;
    /**
     * left存储EXP
     * middle存储循环体
     */
    public static final int WHILE_STMT = 2;
    /**
     * left存储一个VAR
     */
    public static final int READ_STMT = 3;
    /**
     * left存储一个EXP
     */
    public static final int WRITE_STMT = 4;
    /**
     * 声明语句
     * left中存放VAR节点
     * 如果有赋值EXP,则存放中middle中
     */
    public static final int DECLARE_VAR_STMT = 5;

    public static final int DECLARE_FUN_STMT = 12;


    /**
     * 赋值语句
     * left存放var节点
     * middle存放exp节点
     */
    public static final int ASSIGN_STMT = 6;
    /**
     * 复合表达式
     * 复合表达式则形如left middle right
     * 此时datatype为可能为  LOGIC_EXP ADDTIVE_EXP TERM_EXP STRING_EXP
     * value==null
     */
    public static final int EXP = 7;
    /**
     * 变量
     * datatype存放变量类型Token.INT 和 REAL
     * value存放变量名
     * left:
     * 在声明语句中变量的left值代表变量长度exp,在其他的调用中变量的left代表变量索引值exp,若为null,则说明是单个的变量,不是数组
     * 不存储值
     */
    public static final int VAR = 8;

    public static final int FUN = 13;

    /**
     * 运算符
     * 在datatype中存储操作符类型
     */
    public static final int OP = 9;
    /**
     * 因子
     * 有符号datatype存储TOKEN.PLUS/MINUS,默认为Token.PLUS
     * 只会在left中存放一个TreeNode
     * 如果那个TreeNode是var,代表一个变量/数组元素
     * 如果这个TreeNode是exp,则是一个表达式因子
     * 如果是LITREAL,该LITREAL的value中存放字面值的字符形式
     * EXP为因子时,mDataType存储符号PLUS/MINUS
     */
    public static final int FACTOR = 10;
    /**
     * 字面值
     * value中存放字面值,无符号
     * datatype存放类型,在TOKEN中
     */
    public static final int LITERAL = 11;

    public static final int PARAM = 14;


    public static final int RETURN_STMT = 15;
    public static final int PARSE_STMT = 16;
    public static final int PROGRAM = 17;
    public int type;
    public TreeNode mLeft;
    public TreeNode mMiddle;
    public TreeNode mRight;


//      {@link TreeNode#getType()}为{@link TreeNode#VAR}时存储变量类型,具体定义在{@link Token}中INT / REAL?????
//      {@link TreeNode#getType()}为{@link TreeNode#OP}时存储操作符类型,具体定义在{@link Token}中 LT GT ......
//      {@link TreeNode#getType()}为{@link TreeNode#EXP}时表示复合表达式
//      {@link TreeNode#getType()}为{@link TreeNode#FACTOR}表示因子,mDataType处存储表达式的前置符号,具体定义在{@link Token}
//      中PLUS/MINUS, 默认为PLUS
//      {@link TreeNode#getType()}为{@link TreeNode#LITREAL}表示字面值,存储类型

    public int mDataType;
    /*      {@link TreeNode#getType()}为{@link TreeNode#FACTOR}时存储表达式的字符串形式的值
            {@link TreeNode#getType()}为{@link TreeNode#VAR}时存储变量名
    */
    public String value;
    /**
     * 如果是代码块中的代码,则mNext指向其后面的一条语句
     * 普通的顶级代码都是存在linkedlist中,不需要使用这个参数
     */
    public TreeNode mNext;

    @Override
    public String toString() {
        return "["+type+"   "+mLeft+"   "+mNext+"]";
    }
}
