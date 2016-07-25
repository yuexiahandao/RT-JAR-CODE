/*    */ package com.sun.org.apache.regexp.internal;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public class recompile
/*    */ {
/*    */   public static void main(String[] arg)
/*    */   {
/* 71 */     RECompiler r = new RECompiler();
/*    */ 
/* 74 */     if ((arg.length <= 0) || (arg.length % 2 != 0))
/*    */     {
/* 76 */       System.out.println("Usage: recompile <patternname> <pattern>");
/* 77 */       System.exit(0);
/*    */     }
/*    */ 
/* 81 */     for (int i = 0; i < arg.length; i += 2)
/*    */     {
/*    */       try
/*    */       {
/* 86 */         String name = arg[i];
/* 87 */         String pattern = arg[(i + 1)];
/* 88 */         String instructions = name + "PatternInstructions";
/*    */ 
/* 91 */         System.out.print("\n    // Pre-compiled regular expression '" + pattern + "'\n" + "    private static char[] " + instructions + " = \n    {");
/*    */ 
/* 95 */         REProgram program = r.compile(pattern);
/*    */ 
/* 98 */         int numColumns = 7;
/*    */ 
/* 101 */         char[] p = program.getInstructions();
/* 102 */         for (int j = 0; j < p.length; j++)
/*    */         {
/* 105 */           if (j % numColumns == 0)
/*    */           {
/* 107 */             System.out.print("\n        ");
/*    */           }
/*    */ 
/* 111 */           String hex = Integer.toHexString(p[j]);
/* 112 */           while (hex.length() < 4)
/*    */           {
/* 114 */             hex = "0" + hex;
/*    */           }
/* 116 */           System.out.print("0x" + hex + ", ");
/*    */         }
/*    */ 
/* 120 */         System.out.println("\n    };");
/* 121 */         System.out.println("\n    private static RE " + name + "Pattern = new RE(new REProgram(" + instructions + "));");
/*    */       }
/*    */       catch (RESyntaxException e)
/*    */       {
/* 125 */         System.out.println("Syntax error in expression \"" + arg[i] + "\": " + e.toString());
/*    */       }
/*    */       catch (Exception e)
/*    */       {
/* 129 */         System.out.println("Unexpected exception: " + e.toString());
/*    */       }
/*    */       catch (Error e)
/*    */       {
/* 133 */         System.out.println("Internal error: " + e.toString());
/*    */       }
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.regexp.internal.recompile
 * JD-Core Version:    0.6.2
 */