/*     */ package javax.naming.directory;
/*     */ 
/*     */ import javax.naming.NamingException;
/*     */ 
/*     */ public class AttributeModificationException extends NamingException
/*     */ {
/*  66 */   private ModificationItem[] unexecs = null;
/*     */   private static final long serialVersionUID = 8060676069678710186L;
/*     */ 
/*     */   public AttributeModificationException(String paramString)
/*     */   {
/*  78 */     super(paramString);
/*     */   }
/*     */ 
/*     */   public AttributeModificationException()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setUnexecutedModifications(ModificationItem[] paramArrayOfModificationItem)
/*     */   {
/* 101 */     this.unexecs = paramArrayOfModificationItem;
/*     */   }
/*     */ 
/*     */   public ModificationItem[] getUnexecutedModifications()
/*     */   {
/* 116 */     return this.unexecs;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 128 */     String str = super.toString();
/* 129 */     if (this.unexecs != null) {
/* 130 */       str = str + "First unexecuted modification: " + this.unexecs[0].toString();
/*     */     }
/*     */ 
/* 133 */     return str;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.directory.AttributeModificationException
 * JD-Core Version:    0.6.2
 */