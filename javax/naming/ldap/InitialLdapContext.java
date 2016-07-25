/*     */ package javax.naming.ldap;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.NoInitialContextException;
/*     */ import javax.naming.NotContextException;
/*     */ import javax.naming.directory.InitialDirContext;
/*     */ 
/*     */ public class InitialLdapContext extends InitialDirContext
/*     */   implements LdapContext
/*     */ {
/*     */   private static final String BIND_CONTROLS_PROPERTY = "java.naming.ldap.control.connect";
/*     */ 
/*     */   public InitialLdapContext()
/*     */     throws NamingException
/*     */   {
/* 102 */     super(null);
/*     */   }
/*     */ 
/*     */   public InitialLdapContext(Hashtable<?, ?> paramHashtable, Control[] paramArrayOfControl)
/*     */     throws NamingException
/*     */   {
/* 135 */     super(true);
/*     */ 
/* 138 */     Hashtable localHashtable = paramHashtable == null ? new Hashtable(11) : (Hashtable)paramHashtable.clone();
/*     */ 
/* 144 */     if (paramArrayOfControl != null) {
/* 145 */       Control[] arrayOfControl = new Control[paramArrayOfControl.length];
/* 146 */       System.arraycopy(paramArrayOfControl, 0, arrayOfControl, 0, paramArrayOfControl.length);
/* 147 */       localHashtable.put("java.naming.ldap.control.connect", arrayOfControl);
/*     */     }
/*     */ 
/* 150 */     localHashtable.put("java.naming.ldap.version", "3");
/*     */ 
/* 153 */     init(localHashtable);
/*     */   }
/*     */ 
/*     */   private LdapContext getDefaultLdapInitCtx()
/*     */     throws NamingException
/*     */   {
/* 165 */     Context localContext = getDefaultInitCtx();
/*     */ 
/* 167 */     if (!(localContext instanceof LdapContext)) {
/* 168 */       if (localContext == null) {
/* 169 */         throw new NoInitialContextException();
/*     */       }
/* 171 */       throw new NotContextException("Not an instance of LdapContext");
/*     */     }
/*     */ 
/* 175 */     return (LdapContext)localContext;
/*     */   }
/*     */ 
/*     */   public ExtendedResponse extendedOperation(ExtendedRequest paramExtendedRequest)
/*     */     throws NamingException
/*     */   {
/* 183 */     return getDefaultLdapInitCtx().extendedOperation(paramExtendedRequest);
/*     */   }
/*     */ 
/*     */   public LdapContext newInstance(Control[] paramArrayOfControl) throws NamingException
/*     */   {
/* 188 */     return getDefaultLdapInitCtx().newInstance(paramArrayOfControl);
/*     */   }
/*     */ 
/*     */   public void reconnect(Control[] paramArrayOfControl) throws NamingException {
/* 192 */     getDefaultLdapInitCtx().reconnect(paramArrayOfControl);
/*     */   }
/*     */ 
/*     */   public Control[] getConnectControls() throws NamingException {
/* 196 */     return getDefaultLdapInitCtx().getConnectControls();
/*     */   }
/*     */ 
/*     */   public void setRequestControls(Control[] paramArrayOfControl) throws NamingException
/*     */   {
/* 201 */     getDefaultLdapInitCtx().setRequestControls(paramArrayOfControl);
/*     */   }
/*     */ 
/*     */   public Control[] getRequestControls() throws NamingException {
/* 205 */     return getDefaultLdapInitCtx().getRequestControls();
/*     */   }
/*     */ 
/*     */   public Control[] getResponseControls() throws NamingException {
/* 209 */     return getDefaultLdapInitCtx().getResponseControls();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.ldap.InitialLdapContext
 * JD-Core Version:    0.6.2
 */