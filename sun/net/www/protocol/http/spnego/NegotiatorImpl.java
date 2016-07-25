/*     */ package sun.net.www.protocol.http.spnego;
/*     */ 
/*     */ import com.sun.security.jgss.ExtendedGSSContext;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import org.ietf.jgss.GSSContext;
/*     */ import org.ietf.jgss.GSSException;
/*     */ import org.ietf.jgss.GSSName;
/*     */ import org.ietf.jgss.Oid;
/*     */ import sun.net.www.protocol.http.HttpCallerInfo;
/*     */ import sun.net.www.protocol.http.Negotiator;
/*     */ import sun.security.action.GetBooleanAction;
/*     */ import sun.security.jgss.GSSManagerImpl;
/*     */ import sun.security.jgss.GSSUtil;
/*     */ import sun.security.jgss.HttpCaller;
/*     */ 
/*     */ public class NegotiatorImpl extends Negotiator
/*     */ {
/*  52 */   private static final boolean DEBUG = ((Boolean)AccessController.doPrivileged(new GetBooleanAction("sun.security.krb5.debug"))).booleanValue();
/*     */   private GSSContext context;
/*     */   private byte[] oneToken;
/*     */ 
/*     */   private void init(HttpCallerInfo paramHttpCallerInfo)
/*     */     throws GSSException
/*     */   {
/*     */     Oid localOid;
/*  70 */     if (paramHttpCallerInfo.scheme.equalsIgnoreCase("Kerberos"))
/*     */     {
/*  72 */       localOid = GSSUtil.GSS_KRB5_MECH_OID;
/*     */     } else {
/*  74 */       localObject = (String)AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public String run() {
/*  77 */           return System.getProperty("http.auth.preference", "spnego");
/*     */         }
/*     */       });
/*  82 */       if (((String)localObject).equalsIgnoreCase("kerberos")) {
/*  83 */         localOid = GSSUtil.GSS_KRB5_MECH_OID;
/*     */       }
/*     */       else {
/*  86 */         localOid = GSSUtil.GSS_SPNEGO_MECH_OID;
/*     */       }
/*     */     }
/*     */ 
/*  90 */     Object localObject = new GSSManagerImpl(new HttpCaller(paramHttpCallerInfo));
/*     */ 
/*  95 */     String str = "HTTP@" + paramHttpCallerInfo.host.toLowerCase();
/*     */ 
/*  97 */     GSSName localGSSName = ((GSSManagerImpl)localObject).createName(str, GSSName.NT_HOSTBASED_SERVICE);
/*     */ 
/*  99 */     this.context = ((GSSManagerImpl)localObject).createContext(localGSSName, localOid, null, 0);
/*     */ 
/* 105 */     if ((this.context instanceof ExtendedGSSContext)) {
/* 106 */       ((ExtendedGSSContext)this.context).requestDelegPolicy(true);
/*     */     }
/* 108 */     this.oneToken = this.context.initSecContext(new byte[0], 0, 0);
/*     */   }
/*     */ 
/*     */   public NegotiatorImpl(HttpCallerInfo paramHttpCallerInfo)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 117 */       init(paramHttpCallerInfo);
/*     */     } catch (GSSException localGSSException) {
/* 119 */       if (DEBUG) {
/* 120 */         System.out.println("Negotiate support not initiated, will fallback to other scheme if allowed. Reason:");
/*     */ 
/* 122 */         localGSSException.printStackTrace();
/*     */       }
/* 124 */       IOException localIOException = new IOException("Negotiate support not initiated");
/* 125 */       localIOException.initCause(localGSSException);
/* 126 */       throw localIOException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public byte[] firstToken()
/*     */   {
/* 136 */     return this.oneToken;
/*     */   }
/*     */ 
/*     */   public byte[] nextToken(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 148 */       return this.context.initSecContext(paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */     } catch (GSSException localGSSException) {
/* 150 */       if (DEBUG) {
/* 151 */         System.out.println("Negotiate support cannot continue. Reason:");
/* 152 */         localGSSException.printStackTrace();
/*     */       }
/* 154 */       IOException localIOException = new IOException("Negotiate support cannot continue");
/* 155 */       localIOException.initCause(localGSSException);
/* 156 */       throw localIOException;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.protocol.http.spnego.NegotiatorImpl
 * JD-Core Version:    0.6.2
 */