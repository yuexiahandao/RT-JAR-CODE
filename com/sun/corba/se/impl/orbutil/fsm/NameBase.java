/*    */ package com.sun.corba.se.impl.orbutil.fsm;
/*    */ 
/*    */ import com.sun.corba.se.spi.orbutil.fsm.Action;
/*    */ import com.sun.corba.se.spi.orbutil.fsm.Guard;
/*    */ import com.sun.corba.se.spi.orbutil.fsm.Input;
/*    */ import com.sun.corba.se.spi.orbutil.fsm.State;
/*    */ import java.util.StringTokenizer;
/*    */ 
/*    */ public class NameBase
/*    */ {
/*    */   private String name;
/*    */   private String toStringName;
/*    */ 
/*    */   private String getClassName()
/*    */   {
/* 42 */     String str1 = getClass().getName();
/* 43 */     StringTokenizer localStringTokenizer = new StringTokenizer(str1, ".");
/* 44 */     String str2 = localStringTokenizer.nextToken();
/* 45 */     while (localStringTokenizer.hasMoreTokens())
/* 46 */       str2 = localStringTokenizer.nextToken();
/* 47 */     return str2;
/*    */   }
/*    */ 
/*    */   private String getPreferredClassName()
/*    */   {
/* 52 */     if ((this instanceof Action))
/* 53 */       return "Action";
/* 54 */     if ((this instanceof State))
/* 55 */       return "State";
/* 56 */     if ((this instanceof Guard))
/* 57 */       return "Guard";
/* 58 */     if ((this instanceof Input))
/* 59 */       return "Input";
/* 60 */     return getClassName();
/*    */   }
/*    */ 
/*    */   public NameBase(String paramString)
/*    */   {
/* 65 */     this.name = paramString;
/* 66 */     this.toStringName = (getPreferredClassName() + "[" + paramString + "]");
/*    */   }
/*    */ 
/*    */   public String getName()
/*    */   {
/* 71 */     return this.name;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 75 */     return this.toStringName;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.orbutil.fsm.NameBase
 * JD-Core Version:    0.6.2
 */