/*     */ package sun.net.www.protocol.http;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import sun.net.www.HeaderParser;
/*     */ import sun.net.www.MessageHeader;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ public class AuthenticationHeader
/*     */ {
/*     */   MessageHeader rsp;
/*     */   HeaderParser preferred;
/*     */   String preferred_r;
/*     */   private final HttpCallerInfo hci;
/*  88 */   boolean dontUseNegotiate = false;
/*  89 */   static String authPref = null;
/*     */   String hdrname;
/*     */   HashMap schemes;
/*     */ 
/*     */   public String toString()
/*     */   {
/*  92 */     return "AuthenticationHeader: prefer " + this.preferred_r;
/*     */   }
/*     */ 
/*     */   public AuthenticationHeader(String paramString, MessageHeader paramMessageHeader, HttpCallerInfo paramHttpCallerInfo, boolean paramBoolean)
/*     */   {
/* 121 */     this.hci = paramHttpCallerInfo;
/* 122 */     this.dontUseNegotiate = paramBoolean;
/* 123 */     this.rsp = paramMessageHeader;
/* 124 */     this.hdrname = paramString;
/* 125 */     this.schemes = new HashMap();
/* 126 */     parse();
/*     */   }
/*     */ 
/*     */   public HttpCallerInfo getHttpCallerInfo() {
/* 130 */     return this.hci;
/*     */   }
/*     */ 
/*     */   private void parse()
/*     */   {
/* 147 */     Iterator localIterator1 = this.rsp.multiValueIterator(this.hdrname);
/*     */     Object localObject2;
/* 148 */     while (localIterator1.hasNext()) {
/* 149 */       localObject1 = (String)localIterator1.next();
/* 150 */       localObject2 = new HeaderParser((String)localObject1);
/* 151 */       Iterator localIterator2 = ((HeaderParser)localObject2).keys();
/*     */ 
/* 153 */       int i = 0;
/*     */       HeaderParser localHeaderParser;
/*     */       String str;
/* 153 */       for (int j = -1; localIterator2.hasNext(); i++) {
/* 154 */         localIterator2.next();
/* 155 */         if (((HeaderParser)localObject2).findValue(i) == null) {
/* 156 */           if (j != -1) {
/* 157 */             localHeaderParser = ((HeaderParser)localObject2).subsequence(j, i);
/* 158 */             str = localHeaderParser.findKey(0);
/* 159 */             this.schemes.put(str, new SchemeMapValue(localHeaderParser, (String)localObject1));
/*     */           }
/* 161 */           j = i;
/*     */         }
/*     */       }
/* 164 */       if (i > j) {
/* 165 */         localHeaderParser = ((HeaderParser)localObject2).subsequence(j, i);
/* 166 */         str = localHeaderParser.findKey(0);
/* 167 */         this.schemes.put(str, new SchemeMapValue(localHeaderParser, (String)localObject1));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 174 */     Object localObject1 = null;
/* 175 */     if ((authPref == null) || ((localObject1 = (SchemeMapValue)this.schemes.get(authPref)) == null))
/*     */     {
/* 177 */       if ((localObject1 == null) && (!this.dontUseNegotiate)) {
/* 178 */         localObject2 = (SchemeMapValue)this.schemes.get("negotiate");
/* 179 */         if (localObject2 != null) {
/* 180 */           if ((this.hci == null) || (!NegotiateAuthentication.isSupported(new HttpCallerInfo(this.hci, "Negotiate")))) {
/* 181 */             localObject2 = null;
/*     */           }
/* 183 */           localObject1 = localObject2;
/*     */         }
/*     */       }
/*     */ 
/* 187 */       if ((localObject1 == null) && (!this.dontUseNegotiate)) {
/* 188 */         localObject2 = (SchemeMapValue)this.schemes.get("kerberos");
/* 189 */         if (localObject2 != null)
/*     */         {
/* 200 */           if ((this.hci == null) || (!NegotiateAuthentication.isSupported(new HttpCallerInfo(this.hci, "Kerberos")))) {
/* 201 */             localObject2 = null;
/*     */           }
/* 203 */           localObject1 = localObject2;
/*     */         }
/*     */       }
/*     */ 
/* 207 */       if ((localObject1 == null) && 
/* 208 */         ((localObject1 = (SchemeMapValue)this.schemes.get("digest")) == null) && 
/* 209 */         ((localObject1 = (SchemeMapValue)this.schemes.get("ntlm")) == null)) {
/* 210 */         localObject1 = (SchemeMapValue)this.schemes.get("basic");
/*     */       }
/*     */ 
/*     */     }
/* 215 */     else if ((this.dontUseNegotiate) && (authPref.equals("negotiate"))) {
/* 216 */       localObject1 = null;
/*     */     }
/*     */ 
/* 220 */     if (localObject1 != null) {
/* 221 */       this.preferred = ((SchemeMapValue)localObject1).parser;
/* 222 */       this.preferred_r = ((SchemeMapValue)localObject1).raw;
/*     */     }
/*     */   }
/*     */ 
/*     */   public HeaderParser headerParser()
/*     */   {
/* 232 */     return this.preferred;
/*     */   }
/*     */ 
/*     */   public String scheme()
/*     */   {
/* 239 */     if (this.preferred != null) {
/* 240 */       return this.preferred.findKey(0);
/*     */     }
/* 242 */     return null;
/*     */   }
/*     */ 
/*     */   public String raw()
/*     */   {
/* 249 */     return this.preferred_r;
/*     */   }
/*     */ 
/*     */   public boolean isPresent()
/*     */   {
/* 256 */     return this.preferred != null;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  96 */     authPref = (String)AccessController.doPrivileged(new GetPropertyAction("http.auth.preference"));
/*     */ 
/* 105 */     if (authPref != null) {
/* 106 */       authPref = authPref.toLowerCase();
/* 107 */       if ((authPref.equals("spnego")) || (authPref.equals("kerberos")))
/* 108 */         authPref = "negotiate";
/*     */     }
/*     */   }
/*     */ 
/*     */   static class SchemeMapValue
/*     */   {
/*     */     String raw;
/*     */     HeaderParser parser;
/*     */ 
/*     */     SchemeMapValue(HeaderParser paramHeaderParser, String paramString)
/*     */     {
/* 134 */       this.raw = paramString; this.parser = paramHeaderParser;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.protocol.http.AuthenticationHeader
 * JD-Core Version:    0.6.2
 */