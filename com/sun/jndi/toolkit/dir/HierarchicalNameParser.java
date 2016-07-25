/*     */ package com.sun.jndi.toolkit.dir;
/*     */ 
/*     */ import java.util.Properties;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.NameParser;
/*     */ import javax.naming.NamingException;
/*     */ 
/*     */ final class HierarchicalNameParser
/*     */   implements NameParser
/*     */ {
/* 928 */   static final Properties mySyntax = new Properties();
/*     */ 
/*     */   public Name parse(String paramString)
/*     */     throws NamingException
/*     */   {
/* 941 */     return new HierarchicalName(paramString, mySyntax);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 930 */     mySyntax.put("jndi.syntax.direction", "left_to_right");
/* 931 */     mySyntax.put("jndi.syntax.separator", "/");
/* 932 */     mySyntax.put("jndi.syntax.ignorecase", "true");
/* 933 */     mySyntax.put("jndi.syntax.escape", "\\");
/* 934 */     mySyntax.put("jndi.syntax.beginquote", "\"");
/*     */ 
/* 937 */     mySyntax.put("jndi.syntax.trimblanks", "false");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.toolkit.dir.HierarchicalNameParser
 * JD-Core Version:    0.6.2
 */