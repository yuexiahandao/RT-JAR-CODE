/*     */ package com.sun.org.apache.xml.internal.resolver.helpers;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public class Debug
/*     */ {
/*  39 */   protected int debug = 0;
/*     */ 
/*     */   public void setDebug(int newDebug)
/*     */   {
/*  48 */     this.debug = newDebug;
/*     */   }
/*     */ 
/*     */   public int getDebug()
/*     */   {
/*  53 */     return this.debug;
/*     */   }
/*     */ 
/*     */   public void message(int level, String message)
/*     */   {
/*  68 */     if (this.debug >= level)
/*  69 */       System.out.println(message);
/*     */   }
/*     */ 
/*     */   public void message(int level, String message, String spec)
/*     */   {
/*  86 */     if (this.debug >= level)
/*  87 */       System.out.println(message + ": " + spec);
/*     */   }
/*     */ 
/*     */   public void message(int level, String message, String spec1, String spec2)
/*     */   {
/* 106 */     if (this.debug >= level) {
/* 107 */       System.out.println(message + ": " + spec1);
/* 108 */       System.out.println("\t" + spec2);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.resolver.helpers.Debug
 * JD-Core Version:    0.6.2
 */