/*     */ package javax.naming;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class NameClassPair
/*     */   implements Serializable
/*     */ {
/*     */   private String name;
/*     */   private String className;
/*  88 */   private String fullName = null;
/*     */ 
/* 102 */   private boolean isRel = true;
/*     */   private static final long serialVersionUID = 5620776610160863339L;
/*     */ 
/*     */   public NameClassPair(String paramString1, String paramString2)
/*     */   {
/* 119 */     this.name = paramString1;
/* 120 */     this.className = paramString2;
/*     */   }
/*     */ 
/*     */   public NameClassPair(String paramString1, String paramString2, boolean paramBoolean)
/*     */   {
/* 142 */     this.name = paramString1;
/* 143 */     this.className = paramString2;
/* 144 */     this.isRel = paramBoolean;
/*     */   }
/*     */ 
/*     */   public String getClassName()
/*     */   {
/* 160 */     return this.className;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 175 */     return this.name;
/*     */   }
/*     */ 
/*     */   public void setName(String paramString)
/*     */   {
/* 186 */     this.name = paramString;
/*     */   }
/*     */ 
/*     */   public void setClassName(String paramString)
/*     */   {
/* 200 */     this.className = paramString;
/*     */   }
/*     */ 
/*     */   public boolean isRelative()
/*     */   {
/* 215 */     return this.isRel;
/*     */   }
/*     */ 
/*     */   public void setRelative(boolean paramBoolean)
/*     */   {
/* 229 */     this.isRel = paramBoolean;
/*     */   }
/*     */ 
/*     */   public String getNameInNamespace()
/*     */   {
/* 255 */     if (this.fullName == null) {
/* 256 */       throw new UnsupportedOperationException();
/*     */     }
/* 258 */     return this.fullName;
/*     */   }
/*     */ 
/*     */   public void setNameInNamespace(String paramString)
/*     */   {
/* 276 */     this.fullName = paramString;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 289 */     return (isRelative() ? "" : "(not relative)") + getName() + ": " + getClassName();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.NameClassPair
 * JD-Core Version:    0.6.2
 */