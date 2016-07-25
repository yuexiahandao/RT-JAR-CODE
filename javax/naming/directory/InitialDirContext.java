/*     */ package javax.naming.directory;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.InitialContext;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.NamingEnumeration;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.NoInitialContextException;
/*     */ import javax.naming.NotContextException;
/*     */ 
/*     */ public class InitialDirContext extends InitialContext
/*     */   implements DirContext
/*     */ {
/*     */   protected InitialDirContext(boolean paramBoolean)
/*     */     throws NamingException
/*     */   {
/*  66 */     super(paramBoolean);
/*     */   }
/*     */ 
/*     */   public InitialDirContext()
/*     */     throws NamingException
/*     */   {
/*     */   }
/*     */ 
/*     */   public InitialDirContext(Hashtable<?, ?> paramHashtable)
/*     */     throws NamingException
/*     */   {
/* 101 */     super(paramHashtable);
/*     */   }
/*     */ 
/*     */   private DirContext getURLOrDefaultInitDirCtx(String paramString) throws NamingException
/*     */   {
/* 106 */     Context localContext = getURLOrDefaultInitCtx(paramString);
/* 107 */     if (!(localContext instanceof DirContext)) {
/* 108 */       if (localContext == null) {
/* 109 */         throw new NoInitialContextException();
/*     */       }
/* 111 */       throw new NotContextException("Not an instance of DirContext");
/*     */     }
/*     */ 
/* 115 */     return (DirContext)localContext;
/*     */   }
/*     */ 
/*     */   private DirContext getURLOrDefaultInitDirCtx(Name paramName) throws NamingException
/*     */   {
/* 120 */     Context localContext = getURLOrDefaultInitCtx(paramName);
/* 121 */     if (!(localContext instanceof DirContext)) {
/* 122 */       if (localContext == null) {
/* 123 */         throw new NoInitialContextException();
/*     */       }
/* 125 */       throw new NotContextException("Not an instance of DirContext");
/*     */     }
/*     */ 
/* 129 */     return (DirContext)localContext;
/*     */   }
/*     */ 
/*     */   public Attributes getAttributes(String paramString)
/*     */     throws NamingException
/*     */   {
/* 137 */     return getAttributes(paramString, null);
/*     */   }
/*     */ 
/*     */   public Attributes getAttributes(String paramString, String[] paramArrayOfString) throws NamingException
/*     */   {
/* 142 */     return getURLOrDefaultInitDirCtx(paramString).getAttributes(paramString, paramArrayOfString);
/*     */   }
/*     */ 
/*     */   public Attributes getAttributes(Name paramName) throws NamingException
/*     */   {
/* 147 */     return getAttributes(paramName, null);
/*     */   }
/*     */ 
/*     */   public Attributes getAttributes(Name paramName, String[] paramArrayOfString) throws NamingException
/*     */   {
/* 152 */     return getURLOrDefaultInitDirCtx(paramName).getAttributes(paramName, paramArrayOfString);
/*     */   }
/*     */ 
/*     */   public void modifyAttributes(String paramString, int paramInt, Attributes paramAttributes) throws NamingException
/*     */   {
/* 157 */     getURLOrDefaultInitDirCtx(paramString).modifyAttributes(paramString, paramInt, paramAttributes);
/*     */   }
/*     */ 
/*     */   public void modifyAttributes(Name paramName, int paramInt, Attributes paramAttributes) throws NamingException
/*     */   {
/* 162 */     getURLOrDefaultInitDirCtx(paramName).modifyAttributes(paramName, paramInt, paramAttributes);
/*     */   }
/*     */ 
/*     */   public void modifyAttributes(String paramString, ModificationItem[] paramArrayOfModificationItem) throws NamingException
/*     */   {
/* 167 */     getURLOrDefaultInitDirCtx(paramString).modifyAttributes(paramString, paramArrayOfModificationItem);
/*     */   }
/*     */ 
/*     */   public void modifyAttributes(Name paramName, ModificationItem[] paramArrayOfModificationItem) throws NamingException
/*     */   {
/* 172 */     getURLOrDefaultInitDirCtx(paramName).modifyAttributes(paramName, paramArrayOfModificationItem);
/*     */   }
/*     */ 
/*     */   public void bind(String paramString, Object paramObject, Attributes paramAttributes) throws NamingException
/*     */   {
/* 177 */     getURLOrDefaultInitDirCtx(paramString).bind(paramString, paramObject, paramAttributes);
/*     */   }
/*     */ 
/*     */   public void bind(Name paramName, Object paramObject, Attributes paramAttributes) throws NamingException
/*     */   {
/* 182 */     getURLOrDefaultInitDirCtx(paramName).bind(paramName, paramObject, paramAttributes);
/*     */   }
/*     */ 
/*     */   public void rebind(String paramString, Object paramObject, Attributes paramAttributes) throws NamingException
/*     */   {
/* 187 */     getURLOrDefaultInitDirCtx(paramString).rebind(paramString, paramObject, paramAttributes);
/*     */   }
/*     */ 
/*     */   public void rebind(Name paramName, Object paramObject, Attributes paramAttributes) throws NamingException
/*     */   {
/* 192 */     getURLOrDefaultInitDirCtx(paramName).rebind(paramName, paramObject, paramAttributes);
/*     */   }
/*     */ 
/*     */   public DirContext createSubcontext(String paramString, Attributes paramAttributes) throws NamingException
/*     */   {
/* 197 */     return getURLOrDefaultInitDirCtx(paramString).createSubcontext(paramString, paramAttributes);
/*     */   }
/*     */ 
/*     */   public DirContext createSubcontext(Name paramName, Attributes paramAttributes) throws NamingException
/*     */   {
/* 202 */     return getURLOrDefaultInitDirCtx(paramName).createSubcontext(paramName, paramAttributes);
/*     */   }
/*     */ 
/*     */   public DirContext getSchema(String paramString) throws NamingException {
/* 206 */     return getURLOrDefaultInitDirCtx(paramString).getSchema(paramString);
/*     */   }
/*     */ 
/*     */   public DirContext getSchema(Name paramName) throws NamingException {
/* 210 */     return getURLOrDefaultInitDirCtx(paramName).getSchema(paramName);
/*     */   }
/*     */ 
/*     */   public DirContext getSchemaClassDefinition(String paramString) throws NamingException
/*     */   {
/* 215 */     return getURLOrDefaultInitDirCtx(paramString).getSchemaClassDefinition(paramString);
/*     */   }
/*     */ 
/*     */   public DirContext getSchemaClassDefinition(Name paramName) throws NamingException
/*     */   {
/* 220 */     return getURLOrDefaultInitDirCtx(paramName).getSchemaClassDefinition(paramName);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration<SearchResult> search(String paramString, Attributes paramAttributes)
/*     */     throws NamingException
/*     */   {
/* 229 */     return getURLOrDefaultInitDirCtx(paramString).search(paramString, paramAttributes);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration<SearchResult> search(Name paramName, Attributes paramAttributes)
/*     */     throws NamingException
/*     */   {
/* 236 */     return getURLOrDefaultInitDirCtx(paramName).search(paramName, paramAttributes);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration<SearchResult> search(String paramString, Attributes paramAttributes, String[] paramArrayOfString)
/*     */     throws NamingException
/*     */   {
/* 245 */     return getURLOrDefaultInitDirCtx(paramString).search(paramString, paramAttributes, paramArrayOfString);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration<SearchResult> search(Name paramName, Attributes paramAttributes, String[] paramArrayOfString)
/*     */     throws NamingException
/*     */   {
/* 256 */     return getURLOrDefaultInitDirCtx(paramName).search(paramName, paramAttributes, paramArrayOfString);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration<SearchResult> search(String paramString1, String paramString2, SearchControls paramSearchControls)
/*     */     throws NamingException
/*     */   {
/* 267 */     return getURLOrDefaultInitDirCtx(paramString1).search(paramString1, paramString2, paramSearchControls);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration<SearchResult> search(Name paramName, String paramString, SearchControls paramSearchControls)
/*     */     throws NamingException
/*     */   {
/* 276 */     return getURLOrDefaultInitDirCtx(paramName).search(paramName, paramString, paramSearchControls);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration<SearchResult> search(String paramString1, String paramString2, Object[] paramArrayOfObject, SearchControls paramSearchControls)
/*     */     throws NamingException
/*     */   {
/* 286 */     return getURLOrDefaultInitDirCtx(paramString1).search(paramString1, paramString2, paramArrayOfObject, paramSearchControls);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration<SearchResult> search(Name paramName, String paramString, Object[] paramArrayOfObject, SearchControls paramSearchControls)
/*     */     throws NamingException
/*     */   {
/* 297 */     return getURLOrDefaultInitDirCtx(paramName).search(paramName, paramString, paramArrayOfObject, paramSearchControls);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.directory.InitialDirContext
 * JD-Core Version:    0.6.2
 */