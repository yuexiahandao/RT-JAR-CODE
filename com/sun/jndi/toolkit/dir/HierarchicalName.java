/*     */ package com.sun.jndi.toolkit.dir;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Properties;
/*     */ import javax.naming.CompoundName;
/*     */ import javax.naming.InvalidNameException;
/*     */ import javax.naming.Name;
/*     */ 
/*     */ final class HierarchicalName extends CompoundName
/*     */ {
/* 871 */   private int hashValue = -1;
/*     */   private static final long serialVersionUID = -6717336834584573168L;
/*     */ 
/*     */   HierarchicalName()
/*     */   {
/* 875 */     super(new Enumeration() {
/* 876 */       public boolean hasMoreElements() { return false; } 
/* 877 */       public Object nextElement() { throw new NoSuchElementException(); }
/*     */ 
/*     */     }
/*     */     , HierarchicalNameParser.mySyntax);
/*     */   }
/*     */ 
/*     */   HierarchicalName(Enumeration paramEnumeration, Properties paramProperties)
/*     */   {
/* 883 */     super(paramEnumeration, paramProperties);
/*     */   }
/*     */ 
/*     */   HierarchicalName(String paramString, Properties paramProperties) throws InvalidNameException {
/* 887 */     super(paramString, paramProperties);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 892 */     if (this.hashValue == -1)
/*     */     {
/* 894 */       String str = toString().toUpperCase();
/* 895 */       int i = str.length();
/* 896 */       int j = 0;
/* 897 */       char[] arrayOfChar = new char[i];
/*     */ 
/* 899 */       str.getChars(0, i, arrayOfChar, 0);
/*     */ 
/* 901 */       for (int k = i; k > 0; k--) {
/* 902 */         this.hashValue = (this.hashValue * 37 + arrayOfChar[(j++)]);
/*     */       }
/*     */     }
/*     */ 
/* 906 */     return this.hashValue;
/*     */   }
/*     */ 
/*     */   public Name getPrefix(int paramInt) {
/* 910 */     Enumeration localEnumeration = super.getPrefix(paramInt).getAll();
/* 911 */     return new HierarchicalName(localEnumeration, this.mySyntax);
/*     */   }
/*     */ 
/*     */   public Name getSuffix(int paramInt) {
/* 915 */     Enumeration localEnumeration = super.getSuffix(paramInt).getAll();
/* 916 */     return new HierarchicalName(localEnumeration, this.mySyntax);
/*     */   }
/*     */ 
/*     */   public Object clone() {
/* 920 */     return new HierarchicalName(getAll(), this.mySyntax);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.toolkit.dir.HierarchicalName
 * JD-Core Version:    0.6.2
 */