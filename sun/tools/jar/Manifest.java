/*     */ package sun.tools.jar;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ import sun.misc.BASE64Encoder;
/*     */ import sun.net.www.MessageHeader;
/*     */ 
/*     */ public class Manifest
/*     */ {
/*  50 */   private Vector entries = new Vector();
/*  51 */   private byte[] tmpbuf = new byte[512];
/*     */ 
/*  53 */   private Hashtable tableEntries = new Hashtable();
/*     */ 
/*  55 */   static final String[] hashes = { "SHA" };
/*  56 */   static final byte[] EOL = { 13, 10 };
/*     */   static final boolean debug = false;
/*     */   static final String VERSION = "1.0";
/*     */ 
/*     */   static final void debug(String paramString)
/*     */   {
/*     */   }
/*     */ 
/*     */   public Manifest()
/*     */   {
/*     */   }
/*     */ 
/*     */   public Manifest(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/*  68 */     this(new ByteArrayInputStream(paramArrayOfByte), false);
/*     */   }
/*     */ 
/*     */   public Manifest(InputStream paramInputStream) throws IOException {
/*  72 */     this(paramInputStream, true);
/*     */   }
/*     */ 
/*     */   public Manifest(InputStream paramInputStream, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/*  80 */     if (!paramInputStream.markSupported()) {
/*  81 */       paramInputStream = new BufferedInputStream(paramInputStream);
/*     */     }
/*     */     while (true)
/*     */     {
/*  85 */       paramInputStream.mark(1);
/*  86 */       if (paramInputStream.read() == -1) {
/*     */         break;
/*     */       }
/*  89 */       paramInputStream.reset();
/*  90 */       MessageHeader localMessageHeader = new MessageHeader(paramInputStream);
/*  91 */       if (paramBoolean) {
/*  92 */         doHashes(localMessageHeader);
/*     */       }
/*  94 */       addEntry(localMessageHeader);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Manifest(String[] paramArrayOfString) throws IOException
/*     */   {
/* 100 */     MessageHeader localMessageHeader = new MessageHeader();
/* 101 */     localMessageHeader.add("Manifest-Version", "1.0");
/* 102 */     String str = System.getProperty("java.version");
/* 103 */     localMessageHeader.add("Created-By", "Manifest JDK " + str);
/* 104 */     addEntry(localMessageHeader);
/* 105 */     addFiles(null, paramArrayOfString);
/*     */   }
/*     */ 
/*     */   public void addEntry(MessageHeader paramMessageHeader) {
/* 109 */     this.entries.addElement(paramMessageHeader);
/* 110 */     String str = paramMessageHeader.findValue("Name");
/* 111 */     debug("addEntry for name: " + str);
/* 112 */     if (str != null)
/* 113 */       this.tableEntries.put(str, paramMessageHeader);
/*     */   }
/*     */ 
/*     */   public MessageHeader getEntry(String paramString)
/*     */   {
/* 118 */     return (MessageHeader)this.tableEntries.get(paramString);
/*     */   }
/*     */ 
/*     */   public MessageHeader entryAt(int paramInt) {
/* 122 */     return (MessageHeader)this.entries.elementAt(paramInt);
/*     */   }
/*     */ 
/*     */   public Enumeration entries() {
/* 126 */     return this.entries.elements();
/*     */   }
/*     */ 
/*     */   public void addFiles(File paramFile, String[] paramArrayOfString) throws IOException {
/* 130 */     if (paramArrayOfString == null)
/* 131 */       return;
/* 132 */     for (int i = 0; i < paramArrayOfString.length; i++)
/*     */     {
/*     */       File localFile;
/* 134 */       if (paramFile == null)
/* 135 */         localFile = new File(paramArrayOfString[i]);
/*     */       else {
/* 137 */         localFile = new File(paramFile, paramArrayOfString[i]);
/*     */       }
/* 139 */       if (localFile.isDirectory())
/* 140 */         addFiles(localFile, localFile.list());
/*     */       else
/* 142 */         addFile(localFile);
/*     */     }
/*     */   }
/*     */ 
/*     */   private final String stdToLocal(String paramString)
/*     */   {
/* 153 */     return paramString.replace('/', File.separatorChar);
/*     */   }
/*     */ 
/*     */   private final String localToStd(String paramString) {
/* 157 */     paramString = paramString.replace(File.separatorChar, '/');
/* 158 */     if (paramString.startsWith("./"))
/* 159 */       paramString = paramString.substring(2);
/* 160 */     else if (paramString.startsWith("/"))
/* 161 */       paramString = paramString.substring(1);
/* 162 */     return paramString;
/*     */   }
/*     */ 
/*     */   public void addFile(File paramFile) throws IOException {
/* 166 */     String str = localToStd(paramFile.getPath());
/* 167 */     if (this.tableEntries.get(str) == null) {
/* 168 */       MessageHeader localMessageHeader = new MessageHeader();
/* 169 */       localMessageHeader.add("Name", str);
/* 170 */       addEntry(localMessageHeader);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void doHashes(MessageHeader paramMessageHeader) throws IOException
/*     */   {
/* 176 */     String str = paramMessageHeader.findValue("Name");
/* 177 */     if ((str == null) || (str.endsWith("/"))) {
/* 178 */       return;
/*     */     }
/*     */ 
/* 181 */     BASE64Encoder localBASE64Encoder = new BASE64Encoder();
/*     */ 
/* 184 */     for (int i = 0; i < hashes.length; i++) {
/* 185 */       FileInputStream localFileInputStream = new FileInputStream(stdToLocal(str));
/*     */       try {
/* 187 */         MessageDigest localMessageDigest = MessageDigest.getInstance(hashes[i]);
/*     */         int j;
/* 190 */         while ((j = localFileInputStream.read(this.tmpbuf, 0, this.tmpbuf.length)) != -1) {
/* 191 */           localMessageDigest.update(this.tmpbuf, 0, j);
/*     */         }
/* 193 */         paramMessageHeader.set(hashes[i] + "-Digest", localBASE64Encoder.encode(localMessageDigest.digest()));
/*     */       } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 195 */         throw new JarException("Digest algorithm " + hashes[i] + " not available.");
/*     */       }
/*     */       finally {
/* 198 */         localFileInputStream.close();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void stream(OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/*     */     PrintStream localPrintStream;
/* 208 */     if ((paramOutputStream instanceof PrintStream))
/* 209 */       localPrintStream = (PrintStream)paramOutputStream;
/*     */     else {
/* 211 */       localPrintStream = new PrintStream(paramOutputStream);
/*     */     }
/*     */ 
/* 217 */     MessageHeader localMessageHeader1 = (MessageHeader)this.entries.elementAt(0);
/*     */ 
/* 219 */     if (localMessageHeader1.findValue("Manifest-Version") == null)
/*     */     {
/* 226 */       String str = System.getProperty("java.version");
/*     */ 
/* 228 */       if (localMessageHeader1.findValue("Name") == null) {
/* 229 */         localMessageHeader1.prepend("Manifest-Version", "1.0");
/* 230 */         localMessageHeader1.add("Created-By", "Manifest JDK " + str);
/*     */       } else {
/* 232 */         localPrintStream.print("Manifest-Version: 1.0\r\nCreated-By: " + str + "\r\n\r\n");
/*     */       }
/*     */ 
/* 235 */       localPrintStream.flush();
/*     */     }
/*     */ 
/* 238 */     localMessageHeader1.print(localPrintStream);
/*     */ 
/* 240 */     for (int i = 1; i < this.entries.size(); i++) {
/* 241 */       MessageHeader localMessageHeader2 = (MessageHeader)this.entries.elementAt(i);
/* 242 */       localMessageHeader2.print(localPrintStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static boolean isManifestName(String paramString)
/*     */   {
/* 249 */     if (paramString.charAt(0) == '/') {
/* 250 */       paramString = paramString.substring(1, paramString.length());
/*     */     }
/*     */ 
/* 253 */     paramString = paramString.toUpperCase();
/*     */ 
/* 255 */     if (paramString.equals("META-INF/MANIFEST.MF")) {
/* 256 */       return true;
/*     */     }
/* 258 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.tools.jar.Manifest
 * JD-Core Version:    0.6.2
 */