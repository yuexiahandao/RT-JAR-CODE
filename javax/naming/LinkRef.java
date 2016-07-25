/*     */ package javax.naming;
/*     */ 
/*     */ public class LinkRef extends Reference
/*     */ {
/*  77 */   static final String linkClassName = LinkRef.class.getName();
/*     */   static final String linkAddrType = "LinkAddress";
/*     */   private static final long serialVersionUID = -5386290613498931298L;
/*     */ 
/*     */   public LinkRef(Name paramName)
/*     */   {
/*  85 */     super(linkClassName, new StringRefAddr("LinkAddress", paramName.toString()));
/*     */   }
/*     */ 
/*     */   public LinkRef(String paramString)
/*     */   {
/*  93 */     super(linkClassName, new StringRefAddr("LinkAddress", paramString));
/*     */   }
/*     */ 
/*     */   public String getLinkName()
/*     */     throws NamingException
/*     */   {
/* 104 */     if ((this.className != null) && (this.className.equals(linkClassName))) {
/* 105 */       RefAddr localRefAddr = get("LinkAddress");
/* 106 */       if ((localRefAddr != null) && ((localRefAddr instanceof StringRefAddr))) {
/* 107 */         return (String)((StringRefAddr)localRefAddr).getContent();
/*     */       }
/*     */     }
/* 110 */     throw new MalformedLinkException();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.LinkRef
 * JD-Core Version:    0.6.2
 */