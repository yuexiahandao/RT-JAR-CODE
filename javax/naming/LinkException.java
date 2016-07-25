/*     */ package javax.naming;
/*     */ 
/*     */ public class LinkException extends NamingException
/*     */ {
/*     */   protected Name linkResolvedName;
/*     */   protected Object linkResolvedObj;
/*     */   protected Name linkRemainingName;
/*     */   protected String linkExplanation;
/*     */   private static final long serialVersionUID = -7967662604076777712L;
/*     */ 
/*     */   public LinkException(String paramString)
/*     */   {
/* 121 */     super(paramString);
/* 122 */     this.linkResolvedName = null;
/* 123 */     this.linkResolvedObj = null;
/* 124 */     this.linkRemainingName = null;
/* 125 */     this.linkExplanation = null;
/*     */   }
/*     */ 
/*     */   public LinkException()
/*     */   {
/* 134 */     this.linkResolvedName = null;
/* 135 */     this.linkResolvedObj = null;
/* 136 */     this.linkRemainingName = null;
/* 137 */     this.linkExplanation = null;
/*     */   }
/*     */ 
/*     */   public Name getLinkResolvedName()
/*     */   {
/* 151 */     return this.linkResolvedName;
/*     */   }
/*     */ 
/*     */   public Name getLinkRemainingName()
/*     */   {
/* 162 */     return this.linkRemainingName;
/*     */   }
/*     */ 
/*     */   public Object getLinkResolvedObj()
/*     */   {
/* 175 */     return this.linkResolvedObj;
/*     */   }
/*     */ 
/*     */   public String getLinkExplanation()
/*     */   {
/* 189 */     return this.linkExplanation;
/*     */   }
/*     */ 
/*     */   public void setLinkExplanation(String paramString)
/*     */   {
/* 201 */     this.linkExplanation = paramString;
/*     */   }
/*     */ 
/*     */   public void setLinkResolvedName(Name paramName)
/*     */   {
/* 223 */     if (paramName != null)
/* 224 */       this.linkResolvedName = ((Name)paramName.clone());
/*     */     else
/* 226 */       this.linkResolvedName = null;
/*     */   }
/*     */ 
/*     */   public void setLinkRemainingName(Name paramName)
/*     */   {
/* 248 */     if (paramName != null)
/* 249 */       this.linkRemainingName = ((Name)paramName.clone());
/*     */     else
/* 251 */       this.linkRemainingName = null;
/*     */   }
/*     */ 
/*     */   public void setLinkResolvedObj(Object paramObject)
/*     */   {
/* 262 */     this.linkResolvedObj = paramObject;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 274 */     return super.toString() + "; Link Remaining Name: '" + this.linkRemainingName + "'";
/*     */   }
/*     */ 
/*     */   public String toString(boolean paramBoolean)
/*     */   {
/* 293 */     if ((!paramBoolean) || (this.linkResolvedObj == null)) {
/* 294 */       return toString();
/*     */     }
/* 296 */     return toString() + "; Link Resolved Object: " + this.linkResolvedObj;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.LinkException
 * JD-Core Version:    0.6.2
 */