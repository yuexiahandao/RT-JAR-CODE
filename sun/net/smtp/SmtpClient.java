/*     */ package sun.net.smtp;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.InetAddress;
/*     */ import java.security.AccessController;
/*     */ import sun.net.TransferProtocolClient;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ public class SmtpClient extends TransferProtocolClient
/*     */ {
/*     */   String mailhost;
/*     */   SmtpPrintStream message;
/*     */ 
/*     */   public void closeServer()
/*     */     throws IOException
/*     */   {
/*  53 */     if (serverIsOpen()) {
/*  54 */       closeMessage();
/*  55 */       issueCommand("QUIT\r\n", 221);
/*  56 */       super.closeServer();
/*     */     }
/*     */   }
/*     */ 
/*     */   void issueCommand(String paramString, int paramInt) throws IOException {
/*  61 */     sendServer(paramString);
/*     */     int i;
/*  63 */     while ((i = readServerResponse()) != paramInt)
/*  64 */       if (i != 220)
/*  65 */         throw new SmtpProtocolException(getResponseString());
/*     */   }
/*     */ 
/*     */   private void toCanonical(String paramString) throws IOException
/*     */   {
/*  70 */     if (paramString.startsWith("<"))
/*  71 */       issueCommand("rcpt to: " + paramString + "\r\n", 250);
/*     */     else
/*  73 */       issueCommand("rcpt to: <" + paramString + ">\r\n", 250);
/*     */   }
/*     */ 
/*     */   public void to(String paramString) throws IOException {
/*  77 */     int i = 0;
/*  78 */     int j = paramString.length();
/*  79 */     int k = 0;
/*  80 */     int m = 0;
/*  81 */     int n = 0;
/*  82 */     int i1 = 0;
/*  83 */     while (k < j) {
/*  84 */       int i2 = paramString.charAt(k);
/*  85 */       if (n > 0) {
/*  86 */         if (i2 == 40)
/*  87 */           n++;
/*  88 */         else if (i2 == 41)
/*  89 */           n--;
/*  90 */         if (n == 0)
/*  91 */           if (m > i)
/*  92 */             i1 = 1;
/*     */           else
/*  94 */             i = k + 1;
/*  95 */       } else if (i2 == 40) {
/*  96 */         n++;
/*  97 */       } else if (i2 == 60) {
/*  98 */         i = m = k + 1;
/*  99 */       } else if (i2 == 62) {
/* 100 */         i1 = 1;
/* 101 */       } else if (i2 == 44) {
/* 102 */         if (m > i)
/* 103 */           toCanonical(paramString.substring(i, m));
/* 104 */         i = k + 1;
/* 105 */         i1 = 0;
/*     */       }
/* 107 */       else if ((i2 > 32) && (i1 == 0)) {
/* 108 */         m = k + 1;
/* 109 */       } else if (i == k) {
/* 110 */         i++;
/*     */       }
/* 112 */       k++;
/*     */     }
/* 114 */     if (m > i)
/* 115 */       toCanonical(paramString.substring(i, m));
/*     */   }
/*     */ 
/*     */   public void from(String paramString) throws IOException {
/* 119 */     if (paramString.startsWith("<"))
/* 120 */       issueCommand("mail from: " + paramString + "\r\n", 250);
/*     */     else
/* 122 */       issueCommand("mail from: <" + paramString + ">\r\n", 250);
/*     */   }
/*     */ 
/*     */   private void openServer(String paramString) throws IOException
/*     */   {
/* 127 */     this.mailhost = paramString;
/* 128 */     openServer(this.mailhost, 25);
/* 129 */     issueCommand("helo " + InetAddress.getLocalHost().getHostName() + "\r\n", 250);
/*     */   }
/*     */ 
/*     */   public PrintStream startMessage() throws IOException {
/* 133 */     issueCommand("data\r\n", 354);
/*     */     try {
/* 135 */       this.message = new SmtpPrintStream(this.serverOutput, this);
/*     */     } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/* 137 */       throw new InternalError(encoding + " encoding not found");
/*     */     }
/* 139 */     return this.message;
/*     */   }
/*     */ 
/*     */   void closeMessage() throws IOException {
/* 143 */     if (this.message != null)
/* 144 */       this.message.close();
/*     */   }
/*     */ 
/*     */   public SmtpClient(String paramString)
/*     */     throws IOException
/*     */   {
/* 150 */     if (paramString != null)
/*     */       try {
/* 152 */         openServer(paramString);
/* 153 */         this.mailhost = paramString;
/* 154 */         return;
/*     */       }
/*     */       catch (Exception localException1)
/*     */       {
/*     */       }
/*     */     try {
/* 160 */       this.mailhost = ((String)AccessController.doPrivileged(new GetPropertyAction("mail.host")));
/*     */ 
/* 162 */       if (this.mailhost != null) {
/* 163 */         openServer(this.mailhost);
/* 164 */         return;
/*     */       }
/*     */     } catch (Exception localException2) {
/*     */     }
/*     */     try {
/* 169 */       this.mailhost = "localhost";
/* 170 */       openServer(this.mailhost);
/*     */     } catch (Exception localException3) {
/* 172 */       this.mailhost = "mailhost";
/* 173 */       openServer(this.mailhost);
/*     */     }
/*     */   }
/*     */ 
/*     */   public SmtpClient() throws IOException
/*     */   {
/* 179 */     this(null);
/*     */   }
/*     */ 
/*     */   public SmtpClient(int paramInt) throws IOException
/*     */   {
/* 184 */     setConnectTimeout(paramInt);
/*     */     try
/*     */     {
/* 187 */       this.mailhost = ((String)AccessController.doPrivileged(new GetPropertyAction("mail.host")));
/*     */ 
/* 189 */       if (this.mailhost != null) {
/* 190 */         openServer(this.mailhost);
/* 191 */         return;
/*     */       }
/*     */     } catch (Exception localException1) {
/*     */     }
/*     */     try {
/* 196 */       this.mailhost = "localhost";
/* 197 */       openServer(this.mailhost);
/*     */     } catch (Exception localException2) {
/* 199 */       this.mailhost = "mailhost";
/* 200 */       openServer(this.mailhost);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getMailHost() {
/* 205 */     return this.mailhost;
/*     */   }
/*     */ 
/*     */   String getEncoding() {
/* 209 */     return encoding;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.smtp.SmtpClient
 * JD-Core Version:    0.6.2
 */