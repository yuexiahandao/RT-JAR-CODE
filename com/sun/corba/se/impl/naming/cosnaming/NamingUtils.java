/*     */ package com.sun.corba.se.impl.naming.cosnaming;
/*     */ 
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import org.omg.CosNaming.NameComponent;
/*     */ 
/*     */ public class NamingUtils
/*     */ {
/*  40 */   public static boolean debug = false;
/*     */   public static PrintStream debugStream;
/*     */   public static PrintStream errStream;
/*     */ 
/*     */   public static void dprint(String paramString)
/*     */   {
/*  47 */     if ((debug) && (debugStream != null))
/*  48 */       debugStream.println(paramString);
/*     */   }
/*     */ 
/*     */   public static void errprint(String paramString)
/*     */   {
/*  56 */     if (errStream != null)
/*  57 */       errStream.println(paramString);
/*     */     else
/*  59 */       System.err.println(paramString);
/*     */   }
/*     */ 
/*     */   public static void printException(Exception paramException)
/*     */   {
/*  67 */     if (errStream != null)
/*  68 */       paramException.printStackTrace(errStream);
/*     */     else
/*  70 */       paramException.printStackTrace();
/*     */   }
/*     */ 
/*     */   public static void makeDebugStream(File paramFile)
/*     */     throws IOException
/*     */   {
/*  81 */     FileOutputStream localFileOutputStream = new FileOutputStream(paramFile);
/*     */ 
/*  83 */     DataOutputStream localDataOutputStream = new DataOutputStream(localFileOutputStream);
/*     */ 
/*  85 */     debugStream = new PrintStream(localDataOutputStream);
/*     */ 
/*  88 */     debugStream.println("Debug Stream Enabled.");
/*     */   }
/*     */ 
/*     */   public static void makeErrStream(File paramFile)
/*     */     throws IOException
/*     */   {
/*  98 */     if (debug)
/*     */     {
/* 100 */       FileOutputStream localFileOutputStream = new FileOutputStream(paramFile);
/*     */ 
/* 102 */       DataOutputStream localDataOutputStream = new DataOutputStream(localFileOutputStream);
/*     */ 
/* 104 */       errStream = new PrintStream(localDataOutputStream);
/* 105 */       dprint("Error stream setup completed.");
/*     */     }
/*     */   }
/*     */ 
/*     */   static String getDirectoryStructuredName(NameComponent[] paramArrayOfNameComponent)
/*     */   {
/* 116 */     StringBuffer localStringBuffer = new StringBuffer("/");
/* 117 */     for (int i = 0; i < paramArrayOfNameComponent.length; i++) {
/* 118 */       localStringBuffer.append(paramArrayOfNameComponent[i].id + "." + paramArrayOfNameComponent[i].kind);
/*     */     }
/* 120 */     return localStringBuffer.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.naming.cosnaming.NamingUtils
 * JD-Core Version:    0.6.2
 */