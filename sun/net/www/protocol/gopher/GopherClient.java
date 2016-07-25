/*     */ package sun.net.www.protocol.gopher;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PipedInputStream;
/*     */ import java.io.PipedOutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URL;
/*     */ import java.security.AccessController;
/*     */ import sun.net.NetworkClient;
/*     */ import sun.net.www.MessageHeader;
/*     */ import sun.net.www.URLConnection;
/*     */ import sun.security.action.GetBooleanAction;
/*     */ import sun.security.action.GetIntegerAction;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ public class GopherClient extends NetworkClient
/*     */   implements Runnable
/*     */ {
/*     */ 
/*     */   @Deprecated
/*  70 */   public static boolean useGopherProxy = ((Boolean)AccessController.doPrivileged(new GetBooleanAction("gopherProxySet"))).booleanValue();
/*     */ 
/*     */   @Deprecated
/*  74 */   public static String gopherProxyHost = (String)AccessController.doPrivileged(new GetPropertyAction("gopherProxyHost"));
/*     */ 
/*     */   @Deprecated
/*  77 */   public static int gopherProxyPort = ((Integer)AccessController.doPrivileged(new GetIntegerAction("gopherProxyPort", 80))).intValue();
/*     */   PipedOutputStream os;
/*     */   URL u;
/*     */   int gtype;
/*     */   String gkey;
/*     */   URLConnection connection;
/*     */ 
/*  89 */   GopherClient(URLConnection paramURLConnection) { this.connection = paramURLConnection; }
/*     */ 
/*     */ 
/*     */   public static boolean getUseGopherProxy()
/*     */   {
/*  97 */     return ((Boolean)AccessController.doPrivileged(new GetBooleanAction("gopherProxySet"))).booleanValue();
/*     */   }
/*     */ 
/*     */   public static String getGopherProxyHost()
/*     */   {
/* 105 */     String str = (String)AccessController.doPrivileged(new GetPropertyAction("gopherProxyHost"));
/*     */ 
/* 107 */     if ("".equals(str)) {
/* 108 */       str = null;
/*     */     }
/* 110 */     return str;
/*     */   }
/*     */ 
/*     */   public static int getGopherProxyPort()
/*     */   {
/* 117 */     return ((Integer)AccessController.doPrivileged(new GetIntegerAction("gopherProxyPort", 80))).intValue();
/*     */   }
/*     */ 
/*     */   InputStream openStream(URL paramURL)
/*     */     throws IOException
/*     */   {
/* 124 */     this.u = paramURL;
/* 125 */     this.os = this.os;
/* 126 */     int i = 0;
/* 127 */     String str = paramURL.getFile();
/* 128 */     int j = str.length();
/* 129 */     int k = 49;
/* 130 */     while ((i < j) && ((k = str.charAt(i)) == '/'))
/* 131 */       i++;
/* 132 */     this.gtype = (k == 47 ? 49 : k);
/* 133 */     if (i < j)
/* 134 */       i++;
/* 135 */     this.gkey = str.substring(i);
/*     */ 
/* 137 */     openServer(paramURL.getHost(), paramURL.getPort() <= 0 ? 70 : paramURL.getPort());
/*     */ 
/* 139 */     MessageHeader localMessageHeader = new MessageHeader();
/*     */ 
/* 141 */     switch (this.gtype) {
/*     */     case 48:
/*     */     case 55:
/* 144 */       localMessageHeader.add("content-type", "text/plain");
/* 145 */       break;
/*     */     case 49:
/* 147 */       localMessageHeader.add("content-type", "text/html");
/* 148 */       break;
/*     */     case 73:
/*     */     case 103:
/* 151 */       localMessageHeader.add("content-type", "image/gif");
/* 152 */       break;
/*     */     default:
/* 154 */       localMessageHeader.add("content-type", "content/unknown");
/*     */     }
/*     */ 
/* 157 */     if (this.gtype != 55) {
/* 158 */       this.serverOutput.print(decodePercent(this.gkey) + "\r\n");
/* 159 */       this.serverOutput.flush();
/* 160 */     } else if ((i = this.gkey.indexOf('?')) >= 0) {
/* 161 */       this.serverOutput.print(decodePercent(this.gkey.substring(0, i) + "\t" + this.gkey.substring(i + 1) + "\r\n"));
/*     */ 
/* 163 */       this.serverOutput.flush();
/* 164 */       localMessageHeader.add("content-type", "text/html");
/*     */     } else {
/* 166 */       localMessageHeader.add("content-type", "text/html");
/*     */     }
/* 168 */     this.connection.setProperties(localMessageHeader);
/* 169 */     if (localMessageHeader.findValue("content-type") == "text/html") {
/* 170 */       this.os = new PipedOutputStream();
/* 171 */       PipedInputStream localPipedInputStream = new PipedInputStream();
/* 172 */       localPipedInputStream.connect(this.os);
/* 173 */       new Thread(this).start();
/* 174 */       return localPipedInputStream;
/*     */     }
/* 176 */     return new GopherInputStream(this, this.serverInput);
/*     */   }
/*     */ 
/*     */   private String decodePercent(String paramString)
/*     */   {
/* 181 */     if ((paramString == null) || (paramString.indexOf('%') < 0))
/* 182 */       return paramString;
/* 183 */     int i = paramString.length();
/* 184 */     char[] arrayOfChar = new char[i];
/* 185 */     int j = 0;
/* 186 */     for (int k = 0; k < i; k++) {
/* 187 */       int m = paramString.charAt(k);
/* 188 */       if ((m == 37) && (k + 2 < i)) {
/* 189 */         int n = paramString.charAt(k + 1);
/* 190 */         int i1 = paramString.charAt(k + 2);
/* 191 */         if ((48 <= n) && (n <= 57))
/* 192 */           n -= 48;
/* 193 */         else if ((97 <= n) && (n <= 102))
/* 194 */           n = n - 97 + 10;
/* 195 */         else if ((65 <= n) && (n <= 70))
/* 196 */           n = n - 65 + 10;
/*     */         else
/* 198 */           n = -1;
/* 199 */         if ((48 <= i1) && (i1 <= 57))
/* 200 */           i1 -= 48;
/* 201 */         else if ((97 <= i1) && (i1 <= 102))
/* 202 */           i1 = i1 - 97 + 10;
/* 203 */         else if ((65 <= i1) && (i1 <= 70))
/* 204 */           i1 = i1 - 65 + 10;
/*     */         else
/* 206 */           i1 = -1;
/* 207 */         if ((n >= 0) && (i1 >= 0)) {
/* 208 */           m = n << 4 | i1;
/* 209 */           k += 2;
/*     */         }
/*     */       }
/* 212 */       arrayOfChar[(j++)] = ((char)m);
/*     */     }
/* 214 */     return new String(arrayOfChar, 0, j);
/*     */   }
/*     */ 
/*     */   private String encodePercent(String paramString)
/*     */   {
/* 219 */     if (paramString == null)
/* 220 */       return paramString;
/* 221 */     int i = paramString.length();
/* 222 */     Object localObject = null;
/* 223 */     int j = 0;
/* 224 */     for (int k = 0; k < i; k++) {
/* 225 */       int m = paramString.charAt(k);
/* 226 */       if ((m <= 32) || (m == 34) || (m == 37)) {
/* 227 */         if (localObject == null)
/* 228 */           localObject = paramString.toCharArray();
/* 229 */         if (j + 3 >= localObject.length) {
/* 230 */           char[] arrayOfChar1 = new char[j + 10];
/* 231 */           System.arraycopy(localObject, 0, arrayOfChar1, 0, j);
/* 232 */           localObject = arrayOfChar1;
/*     */         }
/* 234 */         localObject[j] = 37;
/* 235 */         int n = m >> 4 & 0xF;
/* 236 */         localObject[(j + 1)] = ((char)(n < 10 ? 48 + n : 55 + n));
/* 237 */         n = m & 0xF;
/* 238 */         localObject[(j + 2)] = ((char)(n < 10 ? 48 + n : 55 + n));
/* 239 */         j += 3;
/*     */       } else {
/* 241 */         if (localObject != null) {
/* 242 */           if (j >= localObject.length) {
/* 243 */             char[] arrayOfChar2 = new char[j + 10];
/* 244 */             System.arraycopy(localObject, 0, arrayOfChar2, 0, j);
/* 245 */             localObject = arrayOfChar2;
/*     */           }
/* 247 */           localObject[j] = ((char)m);
/*     */         }
/* 249 */         j++;
/*     */       }
/*     */     }
/* 252 */     return localObject == null ? paramString : new String((char[])localObject, 0, j);
/*     */   }
/*     */ 
/*     */   public void run()
/*     */   {
/* 258 */     int i = -1;
/*     */     try
/*     */     {
/*     */       Object localObject1;
/* 260 */       if ((this.gtype == 55) && ((i = this.gkey.indexOf('?')) < 0)) {
/* 261 */         localObject1 = new PrintStream(this.os, false, encoding);
/* 262 */         ((PrintStream)localObject1).print("<html><head><title>Searchable Gopher Index</title></head>\n<body><h1>Searchable Gopher Index</h1><isindex>\n</body></html>\n");
/* 263 */       } else if ((this.gtype != 49) && (this.gtype != 55)) {
/* 264 */         localObject1 = new byte[2048];
/*     */         try
/*     */         {
/*     */           int j;
/* 267 */           while ((j = this.serverInput.read((byte[])localObject1)) >= 0)
/* 268 */             this.os.write((byte[])localObject1, 0, j);
/*     */         } catch (Exception localException) {
/*     */         }
/*     */       } else {
/* 272 */         localObject1 = new PrintStream(this.os, false, encoding);
/* 273 */         String str1 = null;
/* 274 */         if (this.gtype == 55) {
/* 275 */           str1 = "Results of searching for \"" + this.gkey.substring(i + 1) + "\" on " + this.u.getHost();
/*     */         }
/*     */         else
/* 278 */           str1 = "Gopher directory " + this.gkey + " from " + this.u.getHost();
/* 279 */         ((PrintStream)localObject1).print("<html><head><title>");
/* 280 */         ((PrintStream)localObject1).print(str1);
/* 281 */         ((PrintStream)localObject1).print("</title></head>\n<body>\n<H1>");
/* 282 */         ((PrintStream)localObject1).print(str1);
/* 283 */         ((PrintStream)localObject1).print("</h1><dl compact>\n");
/* 284 */         DataInputStream localDataInputStream = new DataInputStream(this.serverInput);
/*     */         String str2;
/* 286 */         while ((str2 = localDataInputStream.readLine()) != null) {
/* 287 */           int k = str2.length();
/* 288 */           while ((k > 0) && (str2.charAt(k - 1) <= ' '))
/* 289 */             k--;
/* 290 */           if (k > 0)
/*     */           {
/* 292 */             int m = str2.charAt(0);
/* 293 */             int n = str2.indexOf('\t');
/* 294 */             int i1 = n > 0 ? str2.indexOf('\t', n + 1) : -1;
/* 295 */             int i2 = i1 > 0 ? str2.indexOf('\t', i1 + 1) : -1;
/* 296 */             if (i2 >= 0)
/*     */             {
/* 300 */               String str3 = i2 + 1 < k ? ":" + str2.substring(i2 + 1, k) : "";
/* 301 */               String str4 = i1 + 1 < i2 ? str2.substring(i1 + 1, i2) : this.u.getHost();
/* 302 */               ((PrintStream)localObject1).print("<dt><a href=\"gopher://" + str4 + str3 + "/" + str2.substring(0, 1) + encodePercent(str2.substring(n + 1, i1)) + "\">\n");
/*     */ 
/* 304 */               ((PrintStream)localObject1).print("<img align=middle border=0 width=25 height=32 src=");
/* 305 */               switch (m) {
/*     */               default:
/* 307 */                 ((PrintStream)localObject1).print(System.getProperty("java.net.ftp.imagepath.file"));
/* 308 */                 break;
/*     */               case 48:
/* 310 */                 ((PrintStream)localObject1).print(System.getProperty("java.net.ftp.imagepath.text"));
/* 311 */                 break;
/*     */               case 49:
/* 313 */                 ((PrintStream)localObject1).print(System.getProperty("java.net.ftp.imagepath.directory"));
/* 314 */                 break;
/*     */               case 103:
/* 316 */                 ((PrintStream)localObject1).print(System.getProperty("java.net.ftp.imagepath.gif"));
/*     */               }
/*     */ 
/* 319 */               ((PrintStream)localObject1).print(".gif align=middle><dd>\n");
/* 320 */               ((PrintStream)localObject1).print(str2.substring(1, n) + "</a>\n");
/*     */             }
/*     */           }
/*     */         }
/* 322 */         ((PrintStream)localObject1).print("</dl></body>\n");
/* 323 */         ((PrintStream)localObject1).close();
/*     */       }
/*     */     }
/*     */     catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/* 327 */       throw new InternalError(encoding + " encoding not found");
/*     */     } catch (IOException localIOException2) {
/*     */     } finally {
/*     */       try {
/* 331 */         closeServer();
/* 332 */         this.os.close();
/*     */       }
/*     */       catch (IOException localIOException4)
/*     */       {
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.protocol.gopher.GopherClient
 * JD-Core Version:    0.6.2
 */