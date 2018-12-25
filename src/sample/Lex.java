package sample;

import javafx.fxml.FXML;
import jdk.nashorn.internal.runtime.ParserException;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class Lex {

    static int count = 0;
    static int lineNo = 1;
    static char[] inputChar;
    static char currentChar;
    static ArrayList<Token> tokens = new ArrayList<>();//创建的token序列
    static StringBuilder sb = new StringBuilder();
    static String LexError = "";

    public static ArrayList<Token> lexAnalysis(String input) {
        if(tokens.size()!=0)
            for(int i=0;i<tokens.size();i++)
                tokens.remove(i);
        inputChar = input.toCharArray();//文件内容的char[]表示
        //readChar();//等下看要不要加这句话
        currentChar=inputChar[0];
        makeTokenList();
        display();
        inputChar = null;
        count = 0;
        return tokens;
    }

    public static String readTxtFile(String filePath) {
        StringBuffer str = new StringBuffer("");
        File file = new File(filePath);
        try {
            FileReader fr = new FileReader(file);
            int ch = 0;
            while ((ch = fr.read()) != -1) {
                System.out.print((char) ch + "");
                str.append((char) ch + "");
            }
            System.out.println();
            str.append('\n');
            str.append('#');
            fr.close();
        } catch (IOException e) {
            LexError += "File reader出错\n";
            System.out.println("File reader出错");
        }
        return str.toString();

    }//读入文件，返回所有string

    public static void readChar() {
//        if(inputChar[count]!='#') {
            currentChar = inputChar[count++];
            if (currentChar == '\n') lineNo++;
//        }else{
//            System.exit(-1);
//        }
    }//currentChar指向下一位

    public static void makeTokenList() {
        readChar();
        while(true) {


            //readChar();//从char[]中读取一个字符保存到currentChar中
            if (currentChar == ';') {
                tokens.add(new Token(Token.SEMI,";", lineNo));readChar();continue;
            }else
            if(currentChar == ',') {
                tokens.add(new Token(Token.COMMA,",", lineNo));readChar();continue;
            }else
            if (currentChar == '+') {
                readChar();
                if (currentChar == '+') {
                    tokens.add(new Token(Token.PLUSPLUS,"++", lineNo));readChar();continue;}
                else if(currentChar=='='){
                    tokens.add(new Token(Token.PLUSEQUAL,"+=", lineNo));readChar();continue;}
                else {
                    tokens.add(new Token(Token.PLUS,"+", lineNo));
                    //readChar();
                    continue;}
            }else
            if (currentChar == '-') {
                readChar();
                if (currentChar == '-') {
                    tokens.add(new Token(Token.MINUSMINUS,"--", lineNo));readChar();continue;}
                else if(currentChar=='='){
                    tokens.add(new Token(Token.MINUSEQUAL,"-=", lineNo));readChar();continue;}
                else {
                    tokens.add(new Token(Token.MINUS,"-", lineNo));
                    //readChar();
                    continue;
                }
            }else
            if (currentChar == '*') {
                readChar();
                if(currentChar=='='){
                    tokens.add(new Token(Token.MULTIEQUAL,"*=", lineNo));readChar();continue;}
                else {
                    tokens.add(new Token(Token.MUL,"*", lineNo));
                    //readChar();
                    continue;
                }
            }else
            if (currentChar == '(') {
                tokens.add(new Token(Token.LPARENT, "(",lineNo));readChar();continue;
            }else
            if (currentChar == ')') {
                tokens.add(new Token(Token.RPARENT,")", lineNo));readChar();continue;
            }else
            if (currentChar == '[') {
                tokens.add(new Token(Token.LBRACKET, "[",lineNo));readChar();continue;
            }else
            if (currentChar == ']') {
                tokens.add(new Token(Token.RBRACKET, "]",lineNo));readChar();continue;
            }else
            if (currentChar == '{') {
                tokens.add(new Token(Token.LBRACE, "{",lineNo));readChar();continue;
            }else
            if (currentChar == '}') {
                tokens.add(new Token(Token.RBRACE,"}", lineNo));readChar();continue;
            }else //如果currentChar是+-*()[]{}
                if (currentChar == '/') {
                    readChar();
                    if (currentChar == '*') {
                        //tokens.add(new Token(Token.LCOM,"/*", lineNo));
                        readChar();
                        while (true) {
                            if (currentChar == '*') {
                                readChar();
                                if (currentChar == '/') {
                                    //tokens.add(new Token(Token.RCOM,"*/", lineNo));
                                    readChar();
                                    break;
                                }
                            } else {
                                readChar();
                            }
                        }
                        continue;
                    } else if (currentChar == '/') {
                        //tokens.add(new Token(Token.SCOM,"//", lineNo));
                        while (currentChar != '\n') {
                            readChar();
                        }
                        readChar();
                        continue;
                    } else if(currentChar=='=') {
                        tokens.add(new Token(Token.DIVEQUAL,"/=", lineNo));readChar();continue;
                    }else {//除号
                        tokens.add(new Token(Token.DIV,"/", lineNo));
                        //readChar();
                        continue;
                    }
                }else //如果currentChar是/   ......后期要删掉  注释不要加进token序列？
                    if (currentChar == '=') {
                        readChar();
                        if (currentChar == '=') {
                            tokens.add(new Token(Token.EQ, "==",lineNo));
                            readChar();
                            continue;
                        } else {
                            tokens.add(new Token(Token.ASSIGN,"=", lineNo));
                            //readChar();
                            continue;
                        }
                    }else
                    if (currentChar == '>') {
                        readChar();
                        if (currentChar == '=') {
                            tokens.add(new Token(Token.GET,">=", lineNo));
                            readChar();
                            continue;
                        } else {
                            tokens.add(new Token(Token.GT,">", lineNo));
                            //readChar();
                            continue;
                        }
                    }else
                    if (currentChar == '<') {
                        readChar();
                        if (currentChar == '=') {
                            tokens.add(new Token(Token.LET, "<=",lineNo));
                            readChar();
                            continue;
                        } else if (currentChar == '>') {
                            tokens.add(new Token(Token.NEQ, "<>",lineNo));
                            readChar();
                            continue;
                        } else {
                            tokens.add(new Token(Token.LT,"<", lineNo));
                            //readChar();
                            continue;
                        }
                    }else //如果currentChar是<>=
                        if (currentChar >= '0' && currentChar <= '9') {
                            boolean isReal = false;//是否小数
                            while ((currentChar >= '0' && currentChar <= '9') || currentChar == '.') {
                                if (currentChar == '.') {
                                    if (isReal) {
                                        break;
                                    } else {
                                        isReal = true;
                                    }
                                }
                                sb.append(currentChar);
                                readChar();
                            }
                            if (isReal) {
                                tokens.add(new Token(Token.LITERAL_DOUBLE, sb.toString(), lineNo));
                            } else {
                                tokens.add(new Token(Token.LITERAL_INT, sb.toString(), lineNo));
                            }
                            sb.delete(0, sb.length());
                            continue;
                        }else //如果currentChar是int or double
                            if(currentChar=='\"'){
                                readChar();
                                StringBuilder sbr=new StringBuilder();
                                try {
                                    while (currentChar != '\"') {
                                        sbr.append(currentChar + "");
                                        readChar();
                                    }
                                }catch (Exception e)
                                {
                                    System.out.println("引号不匹配");
                                }finally {
                                    tokens.add(new Token(Token.LITERAL_STRING, sbr.toString(), lineNo));
                                    sbr.delete(0, sbr.length());
                                    readChar();
                                }

                            }else //如果将要碰到字符串
                                if(currentChar>='A'&&currentChar<='Z'||currentChar>='a'&&currentChar<='z'||currentChar=='_'){
                                    if(currentChar=='i'&&inputChar[count]=='n'&&inputChar[count+1]=='t'&&inputChar[count+2]==' '){
                                        readChar();readChar();readChar();
                                        tokens.add(new Token(Token.INT,"INT", lineNo));
                                        continue;
                                    }else if(currentChar=='d'&&inputChar[count]=='o'&&inputChar[count+1]=='u'&&inputChar[count+2]=='b'&&inputChar[count+3]=='l'&&inputChar[count+4]=='e'&&inputChar[count+5]==' '){//可能越界？
                                        readChar();readChar();readChar();readChar();readChar();readChar();
                                        tokens.add(new Token(Token.DOUBLE,"DOUBLE", lineNo));
                                        continue;
                                    }else if(currentChar=='i'&&inputChar[count]=='f'&&inputChar[count+1]=='('){//可能越界？
                                        readChar();readChar();
                                        tokens.add(new Token(Token.IF,"IF", lineNo));
                                        continue;
                                    }else if(currentChar=='e'&&inputChar[count]=='l'&&inputChar[count+1]=='s'&&inputChar[count+2]=='e'&&inputChar[count+3]=='{'){//可能越界？
                                        readChar();readChar();readChar();readChar();
                                        tokens.add(new Token(Token.ELSE,"ELSE", lineNo));
                                        continue;
                                    }else if(currentChar=='v'&&inputChar[count]=='o'&&inputChar[count+1]=='i'&&inputChar[count+2]=='d'&&inputChar[count+3]==' '){//可能越界？
                                        readChar();readChar();readChar();readChar();
                                        tokens.add(new Token(Token.VOID,"VOID", lineNo));
                                        continue;
                                    }else if(currentChar=='r'&&inputChar[count]=='e'&&inputChar[count+1]=='a'&&inputChar[count+2]=='d'&&inputChar[count+3]==' '){//可能越界？
                                        readChar();readChar();readChar();readChar();
                                        tokens.add(new Token(Token.READ,"READ", lineNo));
                                        continue;
                                    }else if(currentChar=='w'&&inputChar[count]=='h'&&inputChar[count+1]=='i'&&inputChar[count+2]=='l'&&inputChar[count+3]=='e'&&inputChar[count+4]=='('){//可能越界？
                                        readChar();readChar();readChar();readChar();readChar();
                                        tokens.add(new Token(Token.WHILE,"WHILE", lineNo));
                                        continue;
                                    }else if(currentChar=='w'&&inputChar[count]=='r'&&inputChar[count+1]=='i'&&inputChar[count+2]=='t'&&inputChar[count+3]=='e'&&inputChar[count+4]==' '){//可能越界？
                                        readChar();readChar();readChar();readChar();readChar();
                                        tokens.add(new Token(Token.WRITE,"WRITE", lineNo));
                                        continue;
                                    }
                                    else if(currentChar=='s'&&inputChar[count]=='t'&&inputChar[count+1]=='r'&&inputChar[count+2]=='i'&&inputChar[count+3]=='n'&&inputChar[count+4]=='g'&&inputChar[count+5]==' '){
                                        readChar();readChar();readChar();readChar();readChar();readChar();
                                        tokens.add(new Token(Token.STRING,"STRING", lineNo));
                                        continue;
                                    }else if(currentChar=='r'&&inputChar[count]=='e'&&inputChar[count+1]=='t'&&inputChar[count+2]=='u'&&inputChar[count+3]=='r'&&inputChar[count+4]=='n'&&inputChar[count+5]==' '){
                                        readChar();readChar();readChar();readChar();readChar();readChar();
                                        tokens.add(new Token(Token.RETURN,"return", lineNo));
                                        continue;
                                    }
                                    else{
                                        while (true) {
                                            if (currentChar>='A'&&currentChar<='Z'||currentChar>='a'&&currentChar<='z'||currentChar=='_'||currentChar>='0'&&currentChar<='9') {
                                                sb.append(currentChar);
                                                readChar();
                                            }else{
                                                tokens.add(new Token(Token.ID,sb.toString(),lineNo));
                                                //readChar();
                                                sb.delete(0,sb.length());
                                                break;
                                            }
                                        }
                                    }
                                }else //如果currentChar是数字
                                    if(currentChar=='#') break;
                                    else if(currentChar==' '||currentChar=='\r'||currentChar=='\n'||currentChar=='\f'||currentChar=='\t')   readChar();
                                    else {
                                        LexError+="非法字符:"+currentChar+"\tlineNo:"+lineNo;
                                        LexError+="\n";
                                        readChar();
                                    }
        }
    }

    static String tokenResult="";
    public static void display(){
        for(Token t : tokens){
            tokenResult+=("["+t.tokenNo+"\t"+t.value+"\t"+t.lineNo+"]\n");
        }
        System.out.println(tokenResult);
    }
}