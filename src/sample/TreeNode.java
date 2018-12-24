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
        if(type==18) return "BLOCK";
        return null;
    }

    public static final int WRONG = -1;
    public static final int NULL = 0;
    public static final int IF_STMT = 1;
    public static final int WHILE_STMT = 2;
    public static final int READ_STMT = 3;
    public static final int WRITE_STMT = 4;
    public static final int DECLARE_VAR_STMT = 5;
    public static final int DECLARE_FUN_STMT = 12;
    public static final int ASSIGN_STMT = 6;
    public static final int EXP = 7;
    public static final int VAR = 8;
    public static final int FUN = 13;
    public static final int OP = 9;
    public static final int FACTOR = 10;
    public static final int LITERAL = 11;
    public static final int PARAM = 14;
    public static final int RETURN_STMT = 15;
    public static final int PARSE_STMT = 16;
    public static final int PROGRAM = 17;
    public static final int BLOCK = 18;
    public int type;
    public TreeNode mLeft;
    public TreeNode mMiddle;
    public TreeNode mRight;



    public int mDataType;
    public String value;
    public TreeNode mNext;

    @Override
    public String toString() {
        return "["+type+"   "+mLeft+"   "+mNext+"]";
    }
}
