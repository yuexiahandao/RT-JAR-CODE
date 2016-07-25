/*     */ package sun.security.tools.policytool;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.security.CodeSource;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.Permission;
/*     */ import java.security.UnrecoverableKeyException;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.util.LinkedList;
/*     */ import java.util.ListIterator;
/*     */ import sun.security.provider.PolicyParser.GrantEntry;
/*     */ import sun.security.provider.PolicyParser.PermissionEntry;
/*     */ import sun.security.provider.PolicyParser.PrincipalEntry;
/*     */ 
/*     */ class PolicyEntry
/*     */ {
/*     */   private CodeSource codesource;
/*     */   private PolicyTool tool;
/*     */   private PolicyParser.GrantEntry grantEntry;
/* 746 */   private boolean testing = false;
/*     */ 
/*     */   PolicyEntry(PolicyTool paramPolicyTool, PolicyParser.GrantEntry paramGrantEntry)
/*     */     throws MalformedURLException, NoSuchMethodException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, CertificateException, IOException, NoSuchAlgorithmException, UnrecoverableKeyException
/*     */   {
/* 758 */     this.tool = paramPolicyTool;
/*     */ 
/* 760 */     URL localURL = null;
/*     */ 
/* 763 */     if (paramGrantEntry.codeBase != null)
/* 764 */       localURL = new URL(paramGrantEntry.codeBase);
/* 765 */     this.codesource = new CodeSource(localURL, (Certificate[])null);
/*     */ 
/* 768 */     if (this.testing) {
/* 769 */       System.out.println("Adding Policy Entry:");
/* 770 */       System.out.println("    CodeBase = " + localURL);
/* 771 */       System.out.println("    Signers = " + paramGrantEntry.signedBy);
/* 772 */       System.out.println("    with " + paramGrantEntry.principals.size() + " Principals");
/*     */     }
/*     */ 
/* 776 */     this.grantEntry = paramGrantEntry;
/*     */   }
/*     */ 
/*     */   CodeSource getCodeSource()
/*     */   {
/* 783 */     return this.codesource;
/*     */   }
/*     */ 
/*     */   PolicyParser.GrantEntry getGrantEntry()
/*     */   {
/* 790 */     return this.grantEntry;
/*     */   }
/*     */ 
/*     */   String headerToString()
/*     */   {
/* 798 */     String str = principalsToString();
/* 799 */     if (str.length() == 0) {
/* 800 */       return codebaseToString();
/*     */     }
/* 802 */     return codebaseToString() + ", " + str;
/*     */   }
/*     */ 
/*     */   String codebaseToString()
/*     */   {
/* 811 */     String str = new String();
/*     */ 
/* 813 */     if ((this.grantEntry.codeBase != null) && (!this.grantEntry.codeBase.equals("")))
/*     */     {
/* 815 */       str = str.concat("CodeBase \"" + this.grantEntry.codeBase + "\"");
/*     */     }
/*     */ 
/* 820 */     if ((this.grantEntry.signedBy != null) && (!this.grantEntry.signedBy.equals("")))
/*     */     {
/* 822 */       str = str.length() > 0 ? str.concat(", SignedBy \"" + this.grantEntry.signedBy + "\"") : str.concat("SignedBy \"" + this.grantEntry.signedBy + "\"");
/*     */     }
/*     */ 
/* 830 */     if (str.length() == 0)
/* 831 */       return new String("CodeBase <ALL>");
/* 832 */     return str;
/*     */   }
/*     */ 
/*     */   String principalsToString()
/*     */   {
/* 839 */     String str = "";
/* 840 */     if ((this.grantEntry.principals != null) && (!this.grantEntry.principals.isEmpty()))
/*     */     {
/* 842 */       StringBuffer localStringBuffer = new StringBuffer(200);
/* 843 */       ListIterator localListIterator = this.grantEntry.principals.listIterator();
/*     */ 
/* 845 */       while (localListIterator.hasNext()) {
/* 846 */         PolicyParser.PrincipalEntry localPrincipalEntry = (PolicyParser.PrincipalEntry)localListIterator.next();
/* 847 */         localStringBuffer.append(" Principal " + localPrincipalEntry.getDisplayClass() + " " + localPrincipalEntry.getDisplayName(true));
/*     */ 
/* 849 */         if (localListIterator.hasNext()) localStringBuffer.append(", ");
/*     */       }
/* 851 */       str = localStringBuffer.toString();
/*     */     }
/* 853 */     return str;
/*     */   }
/*     */ 
/*     */   PolicyParser.PermissionEntry toPermissionEntry(Permission paramPermission)
/*     */   {
/* 861 */     String str = null;
/*     */ 
/* 864 */     if ((paramPermission.getActions() != null) && (paramPermission.getActions().trim() != ""))
/*     */     {
/* 866 */       str = paramPermission.getActions();
/*     */     }
/* 868 */     PolicyParser.PermissionEntry localPermissionEntry = new PolicyParser.PermissionEntry(paramPermission.getClass().getName(), paramPermission.getName(), str);
/*     */ 
/* 872 */     return localPermissionEntry;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.tools.policytool.PolicyEntry
 * JD-Core Version:    0.6.2
 */