/*    */ package sun.tools.jar;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.FileReader;
/*    */ import java.io.IOException;
/*    */ import java.io.Reader;
/*    */ import java.io.StreamTokenizer;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ public class CommandLine
/*    */ {
/*    */   public static String[] parse(String[] paramArrayOfString)
/*    */     throws IOException
/*    */   {
/* 58 */     ArrayList localArrayList = new ArrayList(paramArrayOfString.length);
/* 59 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/* 60 */       String str = paramArrayOfString[i];
/* 61 */       if ((str.length() > 1) && (str.charAt(0) == '@')) {
/* 62 */         str = str.substring(1);
/* 63 */         if (str.charAt(0) == '@')
/* 64 */           localArrayList.add(str);
/*    */         else
/* 66 */           loadCmdFile(str, localArrayList);
/*    */       }
/*    */       else {
/* 69 */         localArrayList.add(str);
/*    */       }
/*    */     }
/* 72 */     return (String[])localArrayList.toArray(new String[localArrayList.size()]);
/*    */   }
/*    */ 
/*    */   private static void loadCmdFile(String paramString, List paramList)
/*    */     throws IOException
/*    */   {
/* 78 */     BufferedReader localBufferedReader = new BufferedReader(new FileReader(paramString));
/* 79 */     StreamTokenizer localStreamTokenizer = new StreamTokenizer(localBufferedReader);
/* 80 */     localStreamTokenizer.resetSyntax();
/* 81 */     localStreamTokenizer.wordChars(32, 255);
/* 82 */     localStreamTokenizer.whitespaceChars(0, 32);
/* 83 */     localStreamTokenizer.commentChar(35);
/* 84 */     localStreamTokenizer.quoteChar(34);
/* 85 */     localStreamTokenizer.quoteChar(39);
/* 86 */     while (localStreamTokenizer.nextToken() != -1) {
/* 87 */       paramList.add(localStreamTokenizer.sval);
/*    */     }
/* 89 */     localBufferedReader.close();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.tools.jar.CommandLine
 * JD-Core Version:    0.6.2
 */