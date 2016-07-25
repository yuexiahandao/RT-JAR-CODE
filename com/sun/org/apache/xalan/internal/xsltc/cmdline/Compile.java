/*    */ package com.sun.org.apache.xalan.internal.xsltc.cmdline;
/*    */ 
/*    */ import com.sun.org.apache.xalan.internal.utils.FeatureManager;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.cmdline.getopt.GetOpt;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.cmdline.getopt.GetOptsException;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.XSLTC;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
/*    */ import java.io.File;
/*    */ import java.io.PrintStream;
/*    */ import java.net.URI;
/*    */ import java.net.URL;
/*    */ import java.util.Vector;
/*    */ 
/*    */ public final class Compile
/*    */ {
/* 45 */   private static int VERSION_MAJOR = 1;
/* 46 */   private static int VERSION_MINOR = 4;
/* 47 */   private static int VERSION_DELTA = 0;
/*    */ 
/* 55 */   private static boolean _allowExit = true;
/*    */ 
/*    */   public static void printUsage()
/*    */   {
/* 59 */     System.err.println("XSLTC version " + VERSION_MAJOR + "." + VERSION_MINOR + (VERSION_DELTA > 0 ? "." + VERSION_DELTA : "") + "\n" + new ErrorMsg("COMPILE_USAGE_STR"));
/*    */ 
/* 63 */     if (_allowExit) System.exit(-1);
/*    */   }
/*    */ 
/*    */   public static void main(String[] args)
/*    */   {
/*    */     try
/*    */     {
/* 75 */       boolean inputIsURL = false;
/* 76 */       boolean useStdIn = false;
/* 77 */       boolean classNameSet = false;
/* 78 */       GetOpt getopt = new GetOpt(args, "o:d:j:p:uxhsinv");
/* 79 */       if (args.length < 1) printUsage();
/*    */ 
/* 81 */       XSLTC xsltc = new XSLTC(true, new FeatureManager());
/* 82 */       xsltc.init();
/*    */       int c;
/* 85 */       while ((c = getopt.getNextOption()) != -1)
/* 86 */         switch (c) {
/*    */         case 105:
/* 88 */           useStdIn = true;
/* 89 */           break;
/*    */         case 111:
/* 91 */           xsltc.setClassName(getopt.getOptionArg());
/* 92 */           classNameSet = true;
/* 93 */           break;
/*    */         case 100:
/* 95 */           xsltc.setDestDirectory(getopt.getOptionArg());
/* 96 */           break;
/*    */         case 112:
/* 98 */           xsltc.setPackageName(getopt.getOptionArg());
/* 99 */           break;
/*    */         case 106:
/* 101 */           xsltc.setJarFileName(getopt.getOptionArg());
/* 102 */           break;
/*    */         case 120:
/* 104 */           xsltc.setDebug(true);
/* 105 */           break;
/*    */         case 117:
/* 107 */           inputIsURL = true;
/* 108 */           break;
/*    */         case 115:
/* 110 */           _allowExit = false;
/* 111 */           break;
/*    */         case 110:
/* 113 */           xsltc.setTemplateInlining(true);
/* 114 */           break;
/*    */         case 101:
/*    */         case 102:
/*    */         case 103:
/*    */         case 104:
/*    */         case 107:
/*    */         case 108:
/*    */         case 109:
/*    */         case 113:
/*    */         case 114:
/*    */         case 116:
/*    */         case 118:
/*    */         case 119:
/*    */         default:
/* 119 */           printUsage();
/*    */         }
/*    */       boolean compileOK;
/*    */       boolean compileOK;
/* 126 */       if (useStdIn) {
/* 127 */         if (!classNameSet) {
/* 128 */           System.err.println(new ErrorMsg("COMPILE_STDIN_ERR"));
/* 129 */           if (_allowExit) System.exit(-1);
/*    */         }
/* 131 */         compileOK = xsltc.compile(System.in, xsltc.getClassName());
/*    */       }
/*    */       else
/*    */       {
/* 135 */         String[] stylesheetNames = getopt.getCmdArgs();
/* 136 */         Vector stylesheetVector = new Vector();
/* 137 */         for (int i = 0; i < stylesheetNames.length; i++) {
/* 138 */           String name = stylesheetNames[i];
/*    */           URL url;
/*    */           URL url;
/* 140 */           if (inputIsURL)
/* 141 */             url = new URL(name);
/*    */           else
/* 143 */             url = new File(name).toURI().toURL();
/* 144 */           stylesheetVector.addElement(url);
/*    */         }
/* 146 */         compileOK = xsltc.compile(stylesheetVector);
/*    */       }
/*    */ 
/* 150 */       if (compileOK) {
/* 151 */         xsltc.printWarnings();
/* 152 */         if (xsltc.getJarFileName() != null) xsltc.outputToJar();
/* 153 */         if (_allowExit) System.exit(0); 
/*    */       }
/*    */       else
/*    */       {
/* 156 */         xsltc.printWarnings();
/* 157 */         xsltc.printErrors();
/* 158 */         if (_allowExit) System.exit(-1); 
/*    */       }
/*    */     }
/*    */     catch (GetOptsException ex)
/*    */     {
/* 162 */       System.err.println(ex);
/* 163 */       printUsage();
/*    */     }
/*    */     catch (Exception e) {
/* 166 */       e.printStackTrace();
/* 167 */       if (_allowExit) System.exit(-1);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.cmdline.Compile
 * JD-Core Version:    0.6.2
 */