/*     */ package javax.naming.spi;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ import javax.naming.CannotProceedException;
/*     */ import javax.naming.CompositeName;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.NamingEnumeration;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.directory.Attributes;
/*     */ import javax.naming.directory.DirContext;
/*     */ import javax.naming.directory.ModificationItem;
/*     */ import javax.naming.directory.SearchControls;
/*     */ 
/*     */ class ContinuationDirContext extends ContinuationContext
/*     */   implements DirContext
/*     */ {
/*     */   ContinuationDirContext(CannotProceedException paramCannotProceedException, Hashtable paramHashtable)
/*     */   {
/*  54 */     super(paramCannotProceedException, paramHashtable);
/*     */   }
/*     */ 
/*     */   protected DirContextNamePair getTargetContext(Name paramName)
/*     */     throws NamingException
/*     */   {
/*  60 */     if (this.cpe.getResolvedObj() == null) {
/*  61 */       throw ((NamingException)this.cpe.fillInStackTrace());
/*     */     }
/*  63 */     Context localContext = NamingManager.getContext(this.cpe.getResolvedObj(), this.cpe.getAltName(), this.cpe.getAltNameCtx(), this.env);
/*     */ 
/*  67 */     if (localContext == null) {
/*  68 */       throw ((NamingException)this.cpe.fillInStackTrace());
/*     */     }
/*  70 */     if ((localContext instanceof DirContext)) {
/*  71 */       return new DirContextNamePair((DirContext)localContext, paramName);
/*     */     }
/*  73 */     if ((localContext instanceof Resolver)) {
/*  74 */       localObject = (Resolver)localContext;
/*  75 */       ResolveResult localResolveResult = ((Resolver)localObject).resolveToClass(paramName, DirContext.class);
/*     */ 
/*  78 */       DirContext localDirContext = (DirContext)localResolveResult.getResolvedObj();
/*  79 */       return new DirContextNamePair(localDirContext, localResolveResult.getRemainingName());
/*     */     }
/*     */ 
/*  84 */     Object localObject = localContext.lookup(paramName);
/*  85 */     if ((localObject instanceof DirContext)) {
/*  86 */       return new DirContextNamePair((DirContext)localObject, new CompositeName());
/*     */     }
/*     */ 
/*  90 */     throw ((NamingException)this.cpe.fillInStackTrace());
/*     */   }
/*     */ 
/*     */   protected DirContextStringPair getTargetContext(String paramString)
/*     */     throws NamingException
/*     */   {
/*  96 */     if (this.cpe.getResolvedObj() == null) {
/*  97 */       throw ((NamingException)this.cpe.fillInStackTrace());
/*     */     }
/*  99 */     Context localContext = NamingManager.getContext(this.cpe.getResolvedObj(), this.cpe.getAltName(), this.cpe.getAltNameCtx(), this.env);
/*     */ 
/* 104 */     if ((localContext instanceof DirContext)) {
/* 105 */       return new DirContextStringPair((DirContext)localContext, paramString);
/*     */     }
/* 107 */     if ((localContext instanceof Resolver)) {
/* 108 */       localObject = (Resolver)localContext;
/* 109 */       ResolveResult localResolveResult = ((Resolver)localObject).resolveToClass(paramString, DirContext.class);
/*     */ 
/* 112 */       DirContext localDirContext = (DirContext)localResolveResult.getResolvedObj();
/* 113 */       Name localName = localResolveResult.getRemainingName();
/* 114 */       String str = localName != null ? localName.toString() : "";
/* 115 */       return new DirContextStringPair(localDirContext, str);
/*     */     }
/*     */ 
/* 120 */     Object localObject = localContext.lookup(paramString);
/* 121 */     if ((localObject instanceof DirContext)) {
/* 122 */       return new DirContextStringPair((DirContext)localObject, "");
/*     */     }
/*     */ 
/* 125 */     throw ((NamingException)this.cpe.fillInStackTrace());
/*     */   }
/*     */ 
/*     */   public Attributes getAttributes(String paramString) throws NamingException {
/* 129 */     DirContextStringPair localDirContextStringPair = getTargetContext(paramString);
/* 130 */     return localDirContextStringPair.getDirContext().getAttributes(localDirContextStringPair.getString());
/*     */   }
/*     */ 
/*     */   public Attributes getAttributes(String paramString, String[] paramArrayOfString) throws NamingException
/*     */   {
/* 135 */     DirContextStringPair localDirContextStringPair = getTargetContext(paramString);
/* 136 */     return localDirContextStringPair.getDirContext().getAttributes(localDirContextStringPair.getString(), paramArrayOfString);
/*     */   }
/*     */ 
/*     */   public Attributes getAttributes(Name paramName) throws NamingException {
/* 140 */     DirContextNamePair localDirContextNamePair = getTargetContext(paramName);
/* 141 */     return localDirContextNamePair.getDirContext().getAttributes(localDirContextNamePair.getName());
/*     */   }
/*     */ 
/*     */   public Attributes getAttributes(Name paramName, String[] paramArrayOfString) throws NamingException
/*     */   {
/* 146 */     DirContextNamePair localDirContextNamePair = getTargetContext(paramName);
/* 147 */     return localDirContextNamePair.getDirContext().getAttributes(localDirContextNamePair.getName(), paramArrayOfString);
/*     */   }
/*     */ 
/*     */   public void modifyAttributes(Name paramName, int paramInt, Attributes paramAttributes) throws NamingException
/*     */   {
/* 152 */     DirContextNamePair localDirContextNamePair = getTargetContext(paramName);
/* 153 */     localDirContextNamePair.getDirContext().modifyAttributes(localDirContextNamePair.getName(), paramInt, paramAttributes);
/*     */   }
/*     */ 
/*     */   public void modifyAttributes(String paramString, int paramInt, Attributes paramAttributes) throws NamingException {
/* 157 */     DirContextStringPair localDirContextStringPair = getTargetContext(paramString);
/* 158 */     localDirContextStringPair.getDirContext().modifyAttributes(localDirContextStringPair.getString(), paramInt, paramAttributes);
/*     */   }
/*     */ 
/*     */   public void modifyAttributes(Name paramName, ModificationItem[] paramArrayOfModificationItem) throws NamingException
/*     */   {
/* 163 */     DirContextNamePair localDirContextNamePair = getTargetContext(paramName);
/* 164 */     localDirContextNamePair.getDirContext().modifyAttributes(localDirContextNamePair.getName(), paramArrayOfModificationItem);
/*     */   }
/*     */ 
/*     */   public void modifyAttributes(String paramString, ModificationItem[] paramArrayOfModificationItem) throws NamingException {
/* 168 */     DirContextStringPair localDirContextStringPair = getTargetContext(paramString);
/* 169 */     localDirContextStringPair.getDirContext().modifyAttributes(localDirContextStringPair.getString(), paramArrayOfModificationItem);
/*     */   }
/*     */ 
/*     */   public void bind(Name paramName, Object paramObject, Attributes paramAttributes) throws NamingException
/*     */   {
/* 174 */     DirContextNamePair localDirContextNamePair = getTargetContext(paramName);
/* 175 */     localDirContextNamePair.getDirContext().bind(localDirContextNamePair.getName(), paramObject, paramAttributes);
/*     */   }
/*     */ 
/*     */   public void bind(String paramString, Object paramObject, Attributes paramAttributes) throws NamingException {
/* 179 */     DirContextStringPair localDirContextStringPair = getTargetContext(paramString);
/* 180 */     localDirContextStringPair.getDirContext().bind(localDirContextStringPair.getString(), paramObject, paramAttributes);
/*     */   }
/*     */ 
/*     */   public void rebind(Name paramName, Object paramObject, Attributes paramAttributes) throws NamingException
/*     */   {
/* 185 */     DirContextNamePair localDirContextNamePair = getTargetContext(paramName);
/* 186 */     localDirContextNamePair.getDirContext().rebind(localDirContextNamePair.getName(), paramObject, paramAttributes);
/*     */   }
/*     */ 
/*     */   public void rebind(String paramString, Object paramObject, Attributes paramAttributes) throws NamingException {
/* 190 */     DirContextStringPair localDirContextStringPair = getTargetContext(paramString);
/* 191 */     localDirContextStringPair.getDirContext().rebind(localDirContextStringPair.getString(), paramObject, paramAttributes);
/*     */   }
/*     */ 
/*     */   public DirContext createSubcontext(Name paramName, Attributes paramAttributes) throws NamingException
/*     */   {
/* 196 */     DirContextNamePair localDirContextNamePair = getTargetContext(paramName);
/* 197 */     return localDirContextNamePair.getDirContext().createSubcontext(localDirContextNamePair.getName(), paramAttributes);
/*     */   }
/*     */ 
/*     */   public DirContext createSubcontext(String paramString, Attributes paramAttributes) throws NamingException
/*     */   {
/* 202 */     DirContextStringPair localDirContextStringPair = getTargetContext(paramString);
/* 203 */     return localDirContextStringPair.getDirContext().createSubcontext(localDirContextStringPair.getString(), paramAttributes);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration search(Name paramName, Attributes paramAttributes, String[] paramArrayOfString)
/*     */     throws NamingException
/*     */   {
/* 211 */     DirContextNamePair localDirContextNamePair = getTargetContext(paramName);
/* 212 */     return localDirContextNamePair.getDirContext().search(localDirContextNamePair.getName(), paramAttributes, paramArrayOfString);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration search(String paramString, Attributes paramAttributes, String[] paramArrayOfString)
/*     */     throws NamingException
/*     */   {
/* 220 */     DirContextStringPair localDirContextStringPair = getTargetContext(paramString);
/* 221 */     return localDirContextStringPair.getDirContext().search(localDirContextStringPair.getString(), paramAttributes, paramArrayOfString);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration search(Name paramName, Attributes paramAttributes)
/*     */     throws NamingException
/*     */   {
/* 229 */     DirContextNamePair localDirContextNamePair = getTargetContext(paramName);
/* 230 */     return localDirContextNamePair.getDirContext().search(localDirContextNamePair.getName(), paramAttributes);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration search(String paramString, Attributes paramAttributes) throws NamingException
/*     */   {
/* 235 */     DirContextStringPair localDirContextStringPair = getTargetContext(paramString);
/* 236 */     return localDirContextStringPair.getDirContext().search(localDirContextStringPair.getString(), paramAttributes);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration search(Name paramName, String paramString, SearchControls paramSearchControls)
/*     */     throws NamingException
/*     */   {
/* 244 */     DirContextNamePair localDirContextNamePair = getTargetContext(paramName);
/* 245 */     return localDirContextNamePair.getDirContext().search(localDirContextNamePair.getName(), paramString, paramSearchControls);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration search(String paramString1, String paramString2, SearchControls paramSearchControls)
/*     */     throws NamingException
/*     */   {
/* 252 */     DirContextStringPair localDirContextStringPair = getTargetContext(paramString1);
/* 253 */     return localDirContextStringPair.getDirContext().search(localDirContextStringPair.getString(), paramString2, paramSearchControls);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration search(Name paramName, String paramString, Object[] paramArrayOfObject, SearchControls paramSearchControls)
/*     */     throws NamingException
/*     */   {
/* 261 */     DirContextNamePair localDirContextNamePair = getTargetContext(paramName);
/* 262 */     return localDirContextNamePair.getDirContext().search(localDirContextNamePair.getName(), paramString, paramArrayOfObject, paramSearchControls);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration search(String paramString1, String paramString2, Object[] paramArrayOfObject, SearchControls paramSearchControls)
/*     */     throws NamingException
/*     */   {
/* 271 */     DirContextStringPair localDirContextStringPair = getTargetContext(paramString1);
/* 272 */     return localDirContextStringPair.getDirContext().search(localDirContextStringPair.getString(), paramString2, paramArrayOfObject, paramSearchControls);
/*     */   }
/*     */ 
/*     */   public DirContext getSchema(String paramString) throws NamingException
/*     */   {
/* 277 */     DirContextStringPair localDirContextStringPair = getTargetContext(paramString);
/* 278 */     return localDirContextStringPair.getDirContext().getSchema(localDirContextStringPair.getString());
/*     */   }
/*     */ 
/*     */   public DirContext getSchema(Name paramName) throws NamingException {
/* 282 */     DirContextNamePair localDirContextNamePair = getTargetContext(paramName);
/* 283 */     return localDirContextNamePair.getDirContext().getSchema(localDirContextNamePair.getName());
/*     */   }
/*     */ 
/*     */   public DirContext getSchemaClassDefinition(String paramString) throws NamingException
/*     */   {
/* 288 */     DirContextStringPair localDirContextStringPair = getTargetContext(paramString);
/* 289 */     return localDirContextStringPair.getDirContext().getSchemaClassDefinition(localDirContextStringPair.getString());
/*     */   }
/*     */ 
/*     */   public DirContext getSchemaClassDefinition(Name paramName) throws NamingException
/*     */   {
/* 294 */     DirContextNamePair localDirContextNamePair = getTargetContext(paramName);
/* 295 */     return localDirContextNamePair.getDirContext().getSchemaClassDefinition(localDirContextNamePair.getName());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.spi.ContinuationDirContext
 * JD-Core Version:    0.6.2
 */