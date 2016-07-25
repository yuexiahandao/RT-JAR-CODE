/*      */ package sun.net.ftp.impl;
/*      */ 
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.BufferedOutputStream;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.Closeable;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.OutputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.net.Inet6Address;
/*      */ import java.net.InetAddress;
/*      */ import java.net.InetSocketAddress;
/*      */ import java.net.Proxy;
/*      */ import java.net.Proxy.Type;
/*      */ import java.net.ServerSocket;
/*      */ import java.net.Socket;
/*      */ import java.net.SocketAddress;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.text.DateFormat;
/*      */ import java.text.ParseException;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Calendar;
/*      */ import java.util.Date;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.TimeZone;
/*      */ import java.util.Vector;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ import javax.net.ssl.SSLException;
/*      */ import javax.net.ssl.SSLSession;
/*      */ import javax.net.ssl.SSLSocket;
/*      */ import javax.net.ssl.SSLSocketFactory;
/*      */ import sun.misc.BASE64Decoder;
/*      */ import sun.misc.BASE64Encoder;
/*      */ import sun.net.TelnetInputStream;
/*      */ import sun.net.TelnetOutputStream;
/*      */ import sun.net.ftp.FtpClient.TransferType;
/*      */ import sun.net.ftp.FtpDirEntry;
/*      */ import sun.net.ftp.FtpDirEntry.Type;
/*      */ import sun.net.ftp.FtpDirParser;
/*      */ import sun.net.ftp.FtpProtocolException;
/*      */ import sun.net.ftp.FtpReplyCode;
/*      */ import sun.util.logging.PlatformLogger;
/*      */ 
/*      */ public class FtpClient extends sun.net.ftp.FtpClient
/*      */ {
/*      */   private static int defaultSoTimeout;
/*      */   private static int defaultConnectTimeout;
/*   56 */   private static final PlatformLogger logger = PlatformLogger.getLogger("sun.net.ftp.FtpClient");
/*      */   private Proxy proxy;
/*      */   private Socket server;
/*      */   private PrintStream out;
/*      */   private InputStream in;
/*   62 */   private int readTimeout = -1;
/*   63 */   private int connectTimeout = -1;
/*      */ 
/*   66 */   private static String encoding = "ISO8859_1";
/*      */   private InetSocketAddress serverAddr;
/*   69 */   private boolean replyPending = false;
/*   70 */   private boolean loggedIn = false;
/*   71 */   private boolean useCrypto = false;
/*      */   private SSLSocketFactory sslFact;
/*      */   private Socket oldSocket;
/*   75 */   private Vector<String> serverResponse = new Vector(1);
/*      */ 
/*   77 */   private FtpReplyCode lastReplyCode = null;
/*      */   private String welcomeMsg;
/*   83 */   private final boolean passiveMode = true;
/*   84 */   private FtpClient.TransferType type = FtpClient.TransferType.BINARY;
/*   85 */   private long restartOffset = 0L;
/*   86 */   private long lastTransSize = -1L;
/*      */   private String lastFileName;
/*   91 */   private static String[] patStrings = { "([\\-ld](?:[r\\-][w\\-][x\\-]){3})\\s*\\d+ (\\w+)\\s*(\\w+)\\s*(\\d+)\\s*([A-Z][a-z][a-z]\\s*\\d+)\\s*(\\d\\d:\\d\\d)\\s*(\\p{Print}*)", "([\\-ld](?:[r\\-][w\\-][x\\-]){3})\\s*\\d+ (\\w+)\\s*(\\w+)\\s*(\\d+)\\s*([A-Z][a-z][a-z]\\s*\\d+)\\s*(\\d{4})\\s*(\\p{Print}*)", "(\\d{2}/\\d{2}/\\d{4})\\s*(\\d{2}:\\d{2}[ap])\\s*((?:[0-9,]+)|(?:<DIR>))\\s*(\\p{Graph}*)", "(\\d{2}-\\d{2}-\\d{2})\\s*(\\d{2}:\\d{2}[AP]M)\\s*((?:[0-9,]+)|(?:<DIR>))\\s*(\\p{Graph}*)" };
/*      */ 
/*  101 */   private static int[][] patternGroups = { { 7, 4, 5, 6, 0, 1, 2, 3 }, { 7, 4, 5, 0, 6, 1, 2, 3 }, { 4, 3, 1, 2, 0, 0, 0, 0 }, { 4, 3, 1, 2, 0, 0, 0, 0 } };
/*      */   private static Pattern[] patterns;
/*  109 */   private static Pattern linkp = Pattern.compile("(\\p{Print}+) \\-\\> (\\p{Print}+)$");
/*  110 */   private DateFormat df = DateFormat.getDateInstance(2, Locale.US);
/*      */ 
/*  369 */   private FtpDirParser parser = new DefaultParser(null);
/*  370 */   private FtpDirParser mlsxParser = new MLSxParser(null);
/*      */   private static Pattern transPat;
/*      */   private static Pattern epsvPat;
/*      */   private static Pattern pasvPat;
/*      */   private static String[] MDTMformats;
/*      */   private static SimpleDateFormat[] dateFormats;
/*      */ 
/*      */   private static boolean isASCIISuperset(String paramString)
/*      */     throws Exception
/*      */   {
/*  172 */     String str = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz-_.!~*'();/?:@&=+$,";
/*      */ 
/*  176 */     byte[] arrayOfByte1 = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 45, 95, 46, 33, 126, 42, 39, 40, 41, 59, 47, 63, 58, 64, 38, 61, 43, 36, 44 };
/*      */ 
/*  182 */     byte[] arrayOfByte2 = str.getBytes(paramString);
/*  183 */     return Arrays.equals(arrayOfByte2, arrayOfByte1);
/*      */   }
/*      */ 
/*      */   private void getTransferSize()
/*      */   {
/*  374 */     this.lastTransSize = -1L;
/*      */ 
/*  381 */     String str1 = getLastResponseString();
/*  382 */     if (transPat == null) {
/*  383 */       transPat = Pattern.compile("150 Opening .*\\((\\d+) bytes\\).");
/*      */     }
/*  385 */     Matcher localMatcher = transPat.matcher(str1);
/*  386 */     if (localMatcher.find()) {
/*  387 */       String str2 = localMatcher.group(1);
/*  388 */       this.lastTransSize = Long.parseLong(str2);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void getTransferName()
/*      */   {
/*  398 */     this.lastFileName = null;
/*  399 */     String str = getLastResponseString();
/*  400 */     int i = str.indexOf("unique file name:");
/*  401 */     int j = str.lastIndexOf(')');
/*  402 */     if (i >= 0) {
/*  403 */       i += 17;
/*  404 */       this.lastFileName = str.substring(i, j);
/*      */     }
/*      */   }
/*      */ 
/*      */   private int readServerResponse()
/*      */     throws IOException
/*      */   {
/*  413 */     StringBuffer localStringBuffer = new StringBuffer(32);
/*      */ 
/*  415 */     int j = -1;
/*      */ 
/*  419 */     this.serverResponse.setSize(0);
/*      */     label76: int k;
/*      */     while (true)
/*      */     {
/*      */       int i;
/*  421 */       if ((i = this.in.read()) != -1) {
/*  422 */         if ((i == 13) && 
/*  423 */           ((i = this.in.read()) != 10)) {
/*  424 */           localStringBuffer.append('\r');
/*      */         }
/*      */ 
/*  427 */         localStringBuffer.append((char)i);
/*  428 */         if (i == 10)
/*  429 */           break label76;
/*      */       }
/*      */       else {
/*  432 */         String str = localStringBuffer.toString();
/*  433 */         localStringBuffer.setLength(0);
/*  434 */         if (logger.isLoggable(300)) {
/*  435 */           logger.finest("Server [" + this.serverAddr + "] --> " + str);
/*      */         }
/*      */ 
/*  438 */         if (str.length() == 0) {
/*  439 */           k = -1;
/*      */         } else {
/*      */           try {
/*  442 */             k = Integer.parseInt(str.substring(0, 3));
/*      */           } catch (NumberFormatException localNumberFormatException) {
/*  444 */             k = -1;
/*      */           }
/*      */           catch (StringIndexOutOfBoundsException localStringIndexOutOfBoundsException) {
/*      */           }
/*  448 */           continue;
/*      */         }
/*      */ 
/*  451 */         this.serverResponse.addElement(str);
/*  452 */         if (j != -1)
/*      */         {
/*  454 */           if ((k == j) && ((str.length() < 4) || (str.charAt(3) != '-')))
/*      */           {
/*  459 */             j = -1;
/*  460 */             break;
/*      */           }
/*      */         } else { if ((str.length() < 4) || (str.charAt(3) != '-')) break;
/*  463 */           j = k;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  470 */     return k;
/*      */   }
/*      */ 
/*      */   private void sendServer(String paramString)
/*      */   {
/*  475 */     this.out.print(paramString);
/*  476 */     if (logger.isLoggable(300))
/*  477 */       logger.finest("Server [" + this.serverAddr + "] <-- " + paramString);
/*      */   }
/*      */ 
/*      */   private String getResponseString()
/*      */   {
/*  483 */     return (String)this.serverResponse.elementAt(0);
/*      */   }
/*      */ 
/*      */   private Vector<String> getResponseStrings()
/*      */   {
/*  488 */     return this.serverResponse;
/*      */   }
/*      */ 
/*      */   private boolean readReply()
/*      */     throws IOException
/*      */   {
/*  498 */     this.lastReplyCode = FtpReplyCode.find(readServerResponse());
/*      */ 
/*  500 */     if (this.lastReplyCode.isPositivePreliminary()) {
/*  501 */       this.replyPending = true;
/*  502 */       return true;
/*      */     }
/*  504 */     if ((this.lastReplyCode.isPositiveCompletion()) || (this.lastReplyCode.isPositiveIntermediate())) {
/*  505 */       if (this.lastReplyCode == FtpReplyCode.CLOSING_DATA_CONNECTION) {
/*  506 */         getTransferName();
/*      */       }
/*  508 */       return true;
/*      */     }
/*  510 */     return false;
/*      */   }
/*      */ 
/*      */   private boolean issueCommand(String paramString)
/*      */     throws IOException
/*      */   {
/*  522 */     if (!isConnected()) {
/*  523 */       throw new IllegalStateException("Not connected");
/*      */     }
/*  525 */     if (this.replyPending)
/*      */       try {
/*  527 */         completePending();
/*      */       }
/*      */       catch (FtpProtocolException localFtpProtocolException)
/*      */       {
/*      */       }
/*  532 */     sendServer(paramString + "\r\n");
/*  533 */     return readReply();
/*      */   }
/*      */ 
/*      */   private void issueCommandCheck(String paramString)
/*      */     throws FtpProtocolException, IOException
/*      */   {
/*  544 */     if (!issueCommand(paramString))
/*  545 */       throw new FtpProtocolException(paramString + ":" + getResponseString(), getLastReplyCode());
/*      */   }
/*      */ 
/*      */   private Socket openPassiveDataConnection(String paramString)
/*      */     throws FtpProtocolException, IOException
/*      */   {
/*  561 */     InetSocketAddress localInetSocketAddress = null;
/*      */     String str;
/*      */     Object localObject1;
/*      */     int i;
/*  574 */     if (issueCommand("EPSV ALL"))
/*      */     {
/*  576 */       issueCommandCheck("EPSV");
/*  577 */       str = getResponseString();
/*      */ 
/*  585 */       if (epsvPat == null) {
/*  586 */         epsvPat = Pattern.compile("^229 .* \\(\\|\\|\\|(\\d+)\\|\\)");
/*      */       }
/*  588 */       localObject1 = epsvPat.matcher(str);
/*  589 */       if (!((Matcher)localObject1).find()) {
/*  590 */         throw new FtpProtocolException("EPSV failed : " + str);
/*      */       }
/*      */ 
/*  593 */       localObject2 = ((Matcher)localObject1).group(1);
/*  594 */       i = Integer.parseInt((String)localObject2);
/*  595 */       InetAddress localInetAddress = this.server.getInetAddress();
/*  596 */       if (localInetAddress != null) {
/*  597 */         localInetSocketAddress = new InetSocketAddress(localInetAddress, i);
/*      */       }
/*      */       else
/*      */       {
/*  603 */         localInetSocketAddress = InetSocketAddress.createUnresolved(this.serverAddr.getHostName(), i);
/*      */       }
/*      */     }
/*      */     else {
/*  607 */       issueCommandCheck("PASV");
/*  608 */       str = getResponseString();
/*      */ 
/*  622 */       if (pasvPat == null) {
/*  623 */         pasvPat = Pattern.compile("227 .* \\(?(\\d{1,3},\\d{1,3},\\d{1,3},\\d{1,3}),(\\d{1,3}),(\\d{1,3})\\)?");
/*      */       }
/*  625 */       localObject1 = pasvPat.matcher(str);
/*  626 */       if (!((Matcher)localObject1).find()) {
/*  627 */         throw new FtpProtocolException("PASV failed : " + str);
/*      */       }
/*      */ 
/*  630 */       i = Integer.parseInt(((Matcher)localObject1).group(3)) + (Integer.parseInt(((Matcher)localObject1).group(2)) << 8);
/*      */ 
/*  632 */       localObject2 = ((Matcher)localObject1).group(1).replace(',', '.');
/*  633 */       localInetSocketAddress = new InetSocketAddress((String)localObject2, i);
/*      */     }
/*      */ 
/*  637 */     if (this.proxy != null) {
/*  638 */       if (this.proxy.type() == Proxy.Type.SOCKS)
/*  639 */         localObject1 = (Socket)AccessController.doPrivileged(new PrivilegedAction()
/*      */         {
/*      */           public Socket run()
/*      */           {
/*  643 */             return new Socket(FtpClient.this.proxy);
/*      */           }
/*      */         });
/*      */       else
/*  647 */         localObject1 = new Socket(Proxy.NO_PROXY);
/*      */     }
/*      */     else {
/*  650 */       localObject1 = new Socket();
/*      */     }
/*      */ 
/*  653 */     Object localObject2 = (InetAddress)AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public InetAddress run()
/*      */       {
/*  657 */         return FtpClient.this.server.getLocalAddress();
/*      */       }
/*      */     });
/*  663 */     ((Socket)localObject1).bind(new InetSocketAddress((InetAddress)localObject2, 0));
/*      */ 
/*  665 */     if (this.connectTimeout >= 0) {
/*  666 */       ((Socket)localObject1).connect(localInetSocketAddress, this.connectTimeout);
/*      */     }
/*  668 */     else if (defaultConnectTimeout > 0)
/*  669 */       ((Socket)localObject1).connect(localInetSocketAddress, defaultConnectTimeout);
/*      */     else {
/*  671 */       ((Socket)localObject1).connect(localInetSocketAddress);
/*      */     }
/*      */ 
/*  674 */     if (this.readTimeout >= 0)
/*  675 */       ((Socket)localObject1).setSoTimeout(this.readTimeout);
/*  676 */     else if (defaultSoTimeout > 0) {
/*  677 */       ((Socket)localObject1).setSoTimeout(defaultSoTimeout);
/*      */     }
/*  679 */     if (this.useCrypto) {
/*      */       try {
/*  681 */         localObject1 = this.sslFact.createSocket((Socket)localObject1, localInetSocketAddress.getHostName(), localInetSocketAddress.getPort(), true);
/*      */       } catch (Exception localException) {
/*  683 */         throw new FtpProtocolException("Can't open secure data channel: " + localException);
/*      */       }
/*      */     }
/*  686 */     if (!issueCommand(paramString)) {
/*  687 */       ((Socket)localObject1).close();
/*  688 */       if (getLastReplyCode() == FtpReplyCode.FILE_UNAVAILABLE)
/*      */       {
/*  690 */         throw new FileNotFoundException(paramString);
/*      */       }
/*  692 */       throw new FtpProtocolException(paramString + ":" + getResponseString(), getLastReplyCode());
/*      */     }
/*  694 */     return localObject1;
/*      */   }
/*      */ 
/*      */   private Socket openDataConnection(String paramString)
/*      */     throws FtpProtocolException, IOException
/*      */   {
/*      */     try
/*      */     {
/*  710 */       return openPassiveDataConnection(paramString);
/*      */     }
/*      */     catch (FtpProtocolException localFtpProtocolException)
/*      */     {
/*  714 */       Object localObject1 = localFtpProtocolException.getMessage();
/*  715 */       if ((!((String)localObject1).startsWith("PASV")) && (!((String)localObject1).startsWith("EPSV"))) {
/*  716 */         throw localFtpProtocolException;
/*      */       }
/*      */ 
/*  724 */       if ((this.proxy != null) && (this.proxy.type() == Proxy.Type.SOCKS))
/*      */       {
/*  728 */         throw new FtpProtocolException("Passive mode failed");
/*  732 */       }
/*      */ ServerSocket localServerSocket = new ServerSocket(0, 1, this.server.getLocalAddress());
/*      */       Socket localSocket;
/*      */       try {
/*  734 */         localObject1 = localServerSocket.getInetAddress();
/*  735 */         if (((InetAddress)localObject1).isAnyLocalAddress()) {
/*  736 */           localObject1 = this.server.getLocalAddress();
/*      */         }
/*      */ 
/*  745 */         String str = "EPRT |" + ((localObject1 instanceof Inet6Address) ? "2" : "1") + "|" + ((InetAddress)localObject1).getHostAddress() + "|" + localServerSocket.getLocalPort() + "|";
/*      */ 
/*  747 */         if ((!issueCommand(str)) || (!issueCommand(paramString)))
/*      */         {
/*  749 */           str = "PORT ";
/*  750 */           byte[] arrayOfByte = ((InetAddress)localObject1).getAddress();
/*      */ 
/*  753 */           for (int i = 0; i < arrayOfByte.length; i++) {
/*  754 */             str = str + (arrayOfByte[i] & 0xFF) + ",";
/*      */           }
/*      */ 
/*  758 */           str = str + (localServerSocket.getLocalPort() >>> 8 & 0xFF) + "," + (localServerSocket.getLocalPort() & 0xFF);
/*  759 */           issueCommandCheck(str);
/*  760 */           issueCommandCheck(paramString);
/*      */         }
/*      */ 
/*  764 */         if (this.connectTimeout >= 0) {
/*  765 */           localServerSocket.setSoTimeout(this.connectTimeout);
/*      */         }
/*  767 */         else if (defaultConnectTimeout > 0) {
/*  768 */           localServerSocket.setSoTimeout(defaultConnectTimeout);
/*      */         }
/*      */ 
/*  771 */         localSocket = localServerSocket.accept();
/*  772 */         if (this.readTimeout >= 0) {
/*  773 */           localSocket.setSoTimeout(this.readTimeout);
/*      */         }
/*  775 */         else if (defaultSoTimeout > 0)
/*  776 */           localSocket.setSoTimeout(defaultSoTimeout);
/*      */       }
/*      */       finally
/*      */       {
/*  780 */         localServerSocket.close();
/*      */       }
/*  782 */       if (this.useCrypto) {
/*      */         try {
/*  784 */           localSocket = this.sslFact.createSocket(localSocket, this.serverAddr.getHostName(), this.serverAddr.getPort(), true);
/*      */         } catch (Exception localException) {
/*  786 */           throw new IOException(localException.getLocalizedMessage());
/*      */         }
/*      */       }
/*  789 */       return localSocket;
/*      */     }
/*      */   }
/*      */ 
/*  793 */   private InputStream createInputStream(InputStream paramInputStream) { if (this.type == FtpClient.TransferType.ASCII) {
/*  794 */       return new TelnetInputStream(paramInputStream, false);
/*      */     }
/*  796 */     return paramInputStream; }
/*      */ 
/*      */   private OutputStream createOutputStream(OutputStream paramOutputStream)
/*      */   {
/*  800 */     if (this.type == FtpClient.TransferType.ASCII) {
/*  801 */       return new TelnetOutputStream(paramOutputStream, false);
/*      */     }
/*  803 */     return paramOutputStream;
/*      */   }
/*      */ 
/*      */   public static sun.net.ftp.FtpClient create()
/*      */   {
/*  820 */     return new FtpClient();
/*      */   }
/*      */ 
/*      */   public sun.net.ftp.FtpClient enablePassiveMode(boolean paramBoolean)
/*      */   {
/*  835 */     return this;
/*      */   }
/*      */ 
/*      */   public boolean isPassiveModeEnabled()
/*      */   {
/*  844 */     return true;
/*      */   }
/*      */ 
/*      */   public sun.net.ftp.FtpClient setConnectTimeout(int paramInt)
/*      */   {
/*  856 */     this.connectTimeout = paramInt;
/*  857 */     return this;
/*      */   }
/*      */ 
/*      */   public int getConnectTimeout()
/*      */   {
/*  867 */     return this.connectTimeout;
/*      */   }
/*      */ 
/*      */   public sun.net.ftp.FtpClient setReadTimeout(int paramInt)
/*      */   {
/*  878 */     this.readTimeout = paramInt;
/*  879 */     return this;
/*      */   }
/*      */ 
/*      */   public int getReadTimeout()
/*      */   {
/*  889 */     return this.readTimeout;
/*      */   }
/*      */ 
/*      */   public sun.net.ftp.FtpClient setProxy(Proxy paramProxy) {
/*  893 */     this.proxy = paramProxy;
/*  894 */     return this;
/*      */   }
/*      */ 
/*      */   public Proxy getProxy()
/*      */   {
/*  905 */     return this.proxy;
/*      */   }
/*      */ 
/*      */   private void tryConnect(InetSocketAddress paramInetSocketAddress, int paramInt)
/*      */     throws IOException
/*      */   {
/*  915 */     if (isConnected()) {
/*  916 */       disconnect();
/*      */     }
/*  918 */     this.server = doConnect(paramInetSocketAddress, paramInt);
/*      */     try {
/*  920 */       this.out = new PrintStream(new BufferedOutputStream(this.server.getOutputStream()), true, encoding);
/*      */     }
/*      */     catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/*  923 */       throw new InternalError(encoding + "encoding not found");
/*      */     }
/*  925 */     this.in = new BufferedInputStream(this.server.getInputStream());
/*      */   }
/*      */ 
/*      */   private Socket doConnect(InetSocketAddress paramInetSocketAddress, int paramInt)
/*      */     throws IOException
/*      */   {
/*      */     Socket localSocket;
/*  930 */     if (this.proxy != null) {
/*  931 */       if (this.proxy.type() == Proxy.Type.SOCKS)
/*  932 */         localSocket = (Socket)AccessController.doPrivileged(new PrivilegedAction()
/*      */         {
/*      */           public Socket run()
/*      */           {
/*  936 */             return new Socket(FtpClient.this.proxy);
/*      */           }
/*      */         });
/*      */       else
/*  940 */         localSocket = new Socket(Proxy.NO_PROXY);
/*      */     }
/*      */     else {
/*  943 */       localSocket = new Socket();
/*      */     }
/*      */ 
/*  949 */     if (paramInt >= 0) {
/*  950 */       localSocket.connect(paramInetSocketAddress, paramInt);
/*      */     }
/*  952 */     else if (this.connectTimeout >= 0) {
/*  953 */       localSocket.connect(paramInetSocketAddress, this.connectTimeout);
/*      */     }
/*  955 */     else if (defaultConnectTimeout > 0)
/*  956 */       localSocket.connect(paramInetSocketAddress, defaultConnectTimeout);
/*      */     else {
/*  958 */       localSocket.connect(paramInetSocketAddress);
/*      */     }
/*      */ 
/*  962 */     if (this.readTimeout >= 0)
/*  963 */       localSocket.setSoTimeout(this.readTimeout);
/*  964 */     else if (defaultSoTimeout > 0) {
/*  965 */       localSocket.setSoTimeout(defaultSoTimeout);
/*      */     }
/*  967 */     return localSocket;
/*      */   }
/*      */ 
/*      */   private void disconnect() throws IOException {
/*  971 */     if (isConnected()) {
/*  972 */       this.server.close();
/*      */     }
/*  974 */     this.server = null;
/*  975 */     this.in = null;
/*  976 */     this.out = null;
/*  977 */     this.lastTransSize = -1L;
/*  978 */     this.lastFileName = null;
/*  979 */     this.restartOffset = 0L;
/*  980 */     this.welcomeMsg = null;
/*  981 */     this.lastReplyCode = null;
/*  982 */     this.serverResponse.setSize(0);
/*      */   }
/*      */ 
/*      */   public boolean isConnected()
/*      */   {
/*  991 */     return this.server != null;
/*      */   }
/*      */ 
/*      */   public SocketAddress getServerAddress() {
/*  995 */     return this.server == null ? null : this.server.getRemoteSocketAddress();
/*      */   }
/*      */ 
/*      */   public sun.net.ftp.FtpClient connect(SocketAddress paramSocketAddress) throws FtpProtocolException, IOException {
/*  999 */     return connect(paramSocketAddress, -1);
/*      */   }
/*      */ 
/*      */   public sun.net.ftp.FtpClient connect(SocketAddress paramSocketAddress, int paramInt)
/*      */     throws FtpProtocolException, IOException
/*      */   {
/* 1009 */     if (!(paramSocketAddress instanceof InetSocketAddress)) {
/* 1010 */       throw new IllegalArgumentException("Wrong address type");
/*      */     }
/* 1012 */     this.serverAddr = ((InetSocketAddress)paramSocketAddress);
/* 1013 */     tryConnect(this.serverAddr, paramInt);
/* 1014 */     if (!readReply()) {
/* 1015 */       throw new FtpProtocolException("Welcome message: " + getResponseString(), this.lastReplyCode);
/*      */     }
/*      */ 
/* 1018 */     this.welcomeMsg = getResponseString().substring(4);
/* 1019 */     return this;
/*      */   }
/*      */ 
/*      */   private void tryLogin(String paramString, char[] paramArrayOfChar) throws FtpProtocolException, IOException {
/* 1023 */     issueCommandCheck("USER " + paramString);
/*      */ 
/* 1028 */     if ((this.lastReplyCode == FtpReplyCode.NEED_PASSWORD) && 
/* 1029 */       (paramArrayOfChar != null) && (paramArrayOfChar.length > 0))
/* 1030 */       issueCommandCheck("PASS " + String.valueOf(paramArrayOfChar));
/*      */   }
/*      */ 
/*      */   public sun.net.ftp.FtpClient login(String paramString, char[] paramArrayOfChar)
/*      */     throws FtpProtocolException, IOException
/*      */   {
/* 1044 */     if (!isConnected()) {
/* 1045 */       throw new FtpProtocolException("Not connected yet", FtpReplyCode.BAD_SEQUENCE);
/*      */     }
/* 1047 */     if ((paramString == null) || (paramString.length() == 0)) {
/* 1048 */       throw new IllegalArgumentException("User name can't be null or empty");
/*      */     }
/* 1050 */     tryLogin(paramString, paramArrayOfChar);
/*      */ 
/* 1055 */     StringBuffer localStringBuffer = new StringBuffer();
/* 1056 */     for (int i = 0; i < this.serverResponse.size(); i++) {
/* 1057 */       String str = (String)this.serverResponse.elementAt(i);
/* 1058 */       if (str != null) {
/* 1059 */         if ((str.length() >= 4) && (str.startsWith("230")))
/*      */         {
/* 1061 */           str = str.substring(4);
/*      */         }
/* 1063 */         localStringBuffer.append(str);
/*      */       }
/*      */     }
/* 1066 */     this.welcomeMsg = localStringBuffer.toString();
/* 1067 */     this.loggedIn = true;
/* 1068 */     return this;
/*      */   }
/*      */ 
/*      */   public sun.net.ftp.FtpClient login(String paramString1, char[] paramArrayOfChar, String paramString2)
/*      */     throws FtpProtocolException, IOException
/*      */   {
/* 1083 */     if (!isConnected()) {
/* 1084 */       throw new FtpProtocolException("Not connected yet", FtpReplyCode.BAD_SEQUENCE);
/*      */     }
/* 1086 */     if ((paramString1 == null) || (paramString1.length() == 0)) {
/* 1087 */       throw new IllegalArgumentException("User name can't be null or empty");
/*      */     }
/* 1089 */     tryLogin(paramString1, paramArrayOfChar);
/*      */ 
/* 1094 */     if (this.lastReplyCode == FtpReplyCode.NEED_ACCOUNT) {
/* 1095 */       issueCommandCheck("ACCT " + paramString2);
/*      */     }
/*      */ 
/* 1100 */     StringBuffer localStringBuffer = new StringBuffer();
/* 1101 */     if (this.serverResponse != null) {
/* 1102 */       for (String str : this.serverResponse) {
/* 1103 */         if (str != null) {
/* 1104 */           if ((str.length() >= 4) && (str.startsWith("230")))
/*      */           {
/* 1106 */             str = str.substring(4);
/*      */           }
/* 1108 */           localStringBuffer.append(str);
/*      */         }
/*      */       }
/*      */     }
/* 1112 */     this.welcomeMsg = localStringBuffer.toString();
/* 1113 */     this.loggedIn = true;
/* 1114 */     return this;
/*      */   }
/*      */ 
/*      */   public void close()
/*      */     throws IOException
/*      */   {
/* 1123 */     if (isConnected()) {
/* 1124 */       issueCommand("QUIT");
/* 1125 */       this.loggedIn = false;
/*      */     }
/* 1127 */     disconnect();
/*      */   }
/*      */ 
/*      */   public boolean isLoggedIn()
/*      */   {
/* 1136 */     return this.loggedIn;
/*      */   }
/*      */ 
/*      */   public sun.net.ftp.FtpClient changeDirectory(String paramString)
/*      */     throws FtpProtocolException, IOException
/*      */   {
/* 1147 */     if ((paramString == null) || ("".equals(paramString))) {
/* 1148 */       throw new IllegalArgumentException("directory can't be null or empty");
/*      */     }
/*      */ 
/* 1151 */     issueCommandCheck("CWD " + paramString);
/* 1152 */     return this;
/*      */   }
/*      */ 
/*      */   public sun.net.ftp.FtpClient changeToParentDirectory()
/*      */     throws FtpProtocolException, IOException
/*      */   {
/* 1162 */     issueCommandCheck("CDUP");
/* 1163 */     return this;
/*      */   }
/*      */ 
/*      */   public String getWorkingDirectory()
/*      */     throws FtpProtocolException, IOException
/*      */   {
/* 1175 */     issueCommandCheck("PWD");
/*      */ 
/* 1181 */     String str = getResponseString();
/* 1182 */     if (!str.startsWith("257")) {
/* 1183 */       return null;
/*      */     }
/* 1185 */     return str.substring(5, str.lastIndexOf('"'));
/*      */   }
/*      */ 
/*      */   public sun.net.ftp.FtpClient setRestartOffset(long paramLong)
/*      */   {
/* 1200 */     if (paramLong < 0L) {
/* 1201 */       throw new IllegalArgumentException("offset can't be negative");
/*      */     }
/* 1203 */     this.restartOffset = paramLong;
/* 1204 */     return this;
/*      */   }
/*      */ 
/*      */   public sun.net.ftp.FtpClient getFile(String paramString, OutputStream paramOutputStream)
/*      */     throws FtpProtocolException, IOException
/*      */   {
/* 1222 */     int i = 1500;
/*      */     Socket localSocket;
/*      */     InputStream localInputStream;
/*      */     byte[] arrayOfByte;
/*      */     int j;
/* 1223 */     if (this.restartOffset > 0L)
/*      */     {
/*      */       try {
/* 1226 */         localSocket = openDataConnection("REST " + this.restartOffset);
/*      */       } finally {
/* 1228 */         this.restartOffset = 0L;
/*      */       }
/* 1230 */       issueCommandCheck("RETR " + paramString);
/* 1231 */       getTransferSize();
/* 1232 */       localInputStream = createInputStream(localSocket.getInputStream());
/* 1233 */       arrayOfByte = new byte[i * 10];
/*      */ 
/* 1235 */       while ((j = localInputStream.read(arrayOfByte)) >= 0) {
/* 1236 */         if (j > 0) {
/* 1237 */           paramOutputStream.write(arrayOfByte, 0, j);
/*      */         }
/*      */       }
/* 1240 */       localInputStream.close();
/*      */     } else {
/* 1242 */       localSocket = openDataConnection("RETR " + paramString);
/* 1243 */       getTransferSize();
/* 1244 */       localInputStream = createInputStream(localSocket.getInputStream());
/* 1245 */       arrayOfByte = new byte[i * 10];
/*      */ 
/* 1247 */       while ((j = localInputStream.read(arrayOfByte)) >= 0) {
/* 1248 */         if (j > 0) {
/* 1249 */           paramOutputStream.write(arrayOfByte, 0, j);
/*      */         }
/*      */       }
/* 1252 */       localInputStream.close();
/*      */     }
/* 1254 */     return completePending();
/*      */   }
/*      */ 
/*      */   public InputStream getFileStream(String paramString)
/*      */     throws FtpProtocolException, IOException
/*      */   {
/* 1270 */     if (this.restartOffset > 0L) {
/*      */       try {
/* 1272 */         localSocket = openDataConnection("REST " + this.restartOffset);
/*      */       } finally {
/* 1274 */         this.restartOffset = 0L;
/*      */       }
/* 1276 */       if (localSocket == null) {
/* 1277 */         return null;
/*      */       }
/* 1279 */       issueCommandCheck("RETR " + paramString);
/* 1280 */       getTransferSize();
/* 1281 */       return createInputStream(localSocket.getInputStream());
/*      */     }
/*      */ 
/* 1284 */     Socket localSocket = openDataConnection("RETR " + paramString);
/* 1285 */     if (localSocket == null) {
/* 1286 */       return null;
/*      */     }
/* 1288 */     getTransferSize();
/* 1289 */     return createInputStream(localSocket.getInputStream());
/*      */   }
/*      */ 
/*      */   public OutputStream putFileStream(String paramString, boolean paramBoolean)
/*      */     throws FtpProtocolException, IOException
/*      */   {
/* 1320 */     String str = paramBoolean ? "STOU " : "STOR ";
/* 1321 */     Socket localSocket = openDataConnection(str + paramString);
/* 1322 */     if (localSocket == null) {
/* 1323 */       return null;
/*      */     }
/* 1325 */     boolean bool = this.type == FtpClient.TransferType.BINARY;
/* 1326 */     return new TelnetOutputStream(localSocket.getOutputStream(), bool);
/*      */   }
/*      */ 
/*      */   public sun.net.ftp.FtpClient putFile(String paramString, InputStream paramInputStream, boolean paramBoolean)
/*      */     throws FtpProtocolException, IOException
/*      */   {
/* 1348 */     String str = paramBoolean ? "STOU " : "STOR ";
/* 1349 */     int i = 1500;
/* 1350 */     if (this.type == FtpClient.TransferType.BINARY) {
/* 1351 */       Socket localSocket = openDataConnection(str + paramString);
/* 1352 */       OutputStream localOutputStream = createOutputStream(localSocket.getOutputStream());
/* 1353 */       byte[] arrayOfByte = new byte[i * 10];
/*      */       int j;
/* 1355 */       while ((j = paramInputStream.read(arrayOfByte)) >= 0) {
/* 1356 */         if (j > 0) {
/* 1357 */           localOutputStream.write(arrayOfByte, 0, j);
/*      */         }
/*      */       }
/* 1360 */       localOutputStream.close();
/*      */     }
/* 1362 */     return completePending();
/*      */   }
/*      */ 
/*      */   public sun.net.ftp.FtpClient appendFile(String paramString, InputStream paramInputStream)
/*      */     throws FtpProtocolException, IOException
/*      */   {
/* 1378 */     int i = 1500;
/* 1379 */     Socket localSocket = openDataConnection("APPE " + paramString);
/* 1380 */     OutputStream localOutputStream = createOutputStream(localSocket.getOutputStream());
/* 1381 */     byte[] arrayOfByte = new byte[i * 10];
/*      */     int j;
/* 1383 */     while ((j = paramInputStream.read(arrayOfByte)) >= 0) {
/* 1384 */       if (j > 0) {
/* 1385 */         localOutputStream.write(arrayOfByte, 0, j);
/*      */       }
/*      */     }
/* 1388 */     localOutputStream.close();
/* 1389 */     return completePending();
/*      */   }
/*      */ 
/*      */   public sun.net.ftp.FtpClient rename(String paramString1, String paramString2)
/*      */     throws FtpProtocolException, IOException
/*      */   {
/* 1400 */     issueCommandCheck("RNFR " + paramString1);
/* 1401 */     issueCommandCheck("RNTO " + paramString2);
/* 1402 */     return this;
/*      */   }
/*      */ 
/*      */   public sun.net.ftp.FtpClient deleteFile(String paramString)
/*      */     throws FtpProtocolException, IOException
/*      */   {
/* 1414 */     issueCommandCheck("DELE " + paramString);
/* 1415 */     return this;
/*      */   }
/*      */ 
/*      */   public sun.net.ftp.FtpClient makeDirectory(String paramString)
/*      */     throws FtpProtocolException, IOException
/*      */   {
/* 1427 */     issueCommandCheck("MKD " + paramString);
/* 1428 */     return this;
/*      */   }
/*      */ 
/*      */   public sun.net.ftp.FtpClient removeDirectory(String paramString)
/*      */     throws FtpProtocolException, IOException
/*      */   {
/* 1441 */     issueCommandCheck("RMD " + paramString);
/* 1442 */     return this;
/*      */   }
/*      */ 
/*      */   public sun.net.ftp.FtpClient noop()
/*      */     throws FtpProtocolException, IOException
/*      */   {
/* 1452 */     issueCommandCheck("NOOP");
/* 1453 */     return this;
/*      */   }
/*      */ 
/*      */   public String getStatus(String paramString)
/*      */     throws FtpProtocolException, IOException
/*      */   {
/* 1472 */     issueCommandCheck("STAT " + paramString);
/*      */ 
/* 1497 */     Vector localVector = getResponseStrings();
/* 1498 */     StringBuffer localStringBuffer = new StringBuffer();
/* 1499 */     for (int i = 1; i < localVector.size() - 1; i++) {
/* 1500 */       localStringBuffer.append((String)localVector.get(i));
/*      */     }
/* 1502 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   public List<String> getFeatures()
/*      */     throws FtpProtocolException, IOException
/*      */   {
/* 1534 */     ArrayList localArrayList = new ArrayList();
/* 1535 */     issueCommandCheck("FEAT");
/* 1536 */     Vector localVector = getResponseStrings();
/*      */ 
/* 1539 */     for (int i = 1; i < localVector.size() - 1; i++) {
/* 1540 */       String str = (String)localVector.get(i);
/*      */ 
/* 1542 */       localArrayList.add(str.substring(1, str.length() - 1));
/*      */     }
/* 1544 */     return localArrayList;
/*      */   }
/*      */ 
/*      */   public sun.net.ftp.FtpClient abort()
/*      */     throws FtpProtocolException, IOException
/*      */   {
/* 1555 */     issueCommandCheck("ABOR");
/*      */ 
/* 1574 */     return this;
/*      */   }
/*      */ 
/*      */   public sun.net.ftp.FtpClient completePending()
/*      */     throws FtpProtocolException, IOException
/*      */   {
/* 1611 */     while (this.replyPending) {
/* 1612 */       this.replyPending = false;
/* 1613 */       if (!readReply()) {
/* 1614 */         throw new FtpProtocolException(getLastResponseString(), this.lastReplyCode);
/*      */       }
/*      */     }
/* 1617 */     return this;
/*      */   }
/*      */ 
/*      */   public sun.net.ftp.FtpClient reInit()
/*      */     throws FtpProtocolException, IOException
/*      */   {
/* 1626 */     issueCommandCheck("REIN");
/* 1627 */     this.loggedIn = false;
/* 1628 */     if ((this.useCrypto) && 
/* 1629 */       ((this.server instanceof SSLSocket))) {
/* 1630 */       SSLSession localSSLSession = ((SSLSocket)this.server).getSession();
/* 1631 */       localSSLSession.invalidate();
/*      */ 
/* 1633 */       this.server = this.oldSocket;
/* 1634 */       this.oldSocket = null;
/*      */       try {
/* 1636 */         this.out = new PrintStream(new BufferedOutputStream(this.server.getOutputStream()), true, encoding);
/*      */       }
/*      */       catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/* 1639 */         throw new InternalError(encoding + "encoding not found");
/*      */       }
/* 1641 */       this.in = new BufferedInputStream(this.server.getInputStream());
/*      */     }
/*      */ 
/* 1644 */     this.useCrypto = false;
/* 1645 */     return this;
/*      */   }
/*      */ 
/*      */   public sun.net.ftp.FtpClient setType(FtpClient.TransferType paramTransferType)
/*      */     throws FtpProtocolException, IOException
/*      */   {
/* 1657 */     String str = "NOOP";
/*      */ 
/* 1659 */     this.type = paramTransferType;
/* 1660 */     if (paramTransferType == FtpClient.TransferType.ASCII) {
/* 1661 */       str = "TYPE A";
/*      */     }
/* 1663 */     if (paramTransferType == FtpClient.TransferType.BINARY) {
/* 1664 */       str = "TYPE I";
/*      */     }
/* 1666 */     if (paramTransferType == FtpClient.TransferType.EBCDIC) {
/* 1667 */       str = "TYPE E";
/*      */     }
/* 1669 */     issueCommandCheck(str);
/* 1670 */     return this;
/*      */   }
/*      */ 
/*      */   public InputStream list(String paramString)
/*      */     throws FtpProtocolException, IOException
/*      */   {
/* 1688 */     Socket localSocket = openDataConnection("LIST " + paramString);
/* 1689 */     if (localSocket != null) {
/* 1690 */       return createInputStream(localSocket.getInputStream());
/*      */     }
/* 1692 */     return null;
/*      */   }
/*      */ 
/*      */   public InputStream nameList(String paramString)
/*      */     throws FtpProtocolException, IOException
/*      */   {
/* 1712 */     Socket localSocket = openDataConnection("NLST " + paramString);
/* 1713 */     if (localSocket != null) {
/* 1714 */       return createInputStream(localSocket.getInputStream());
/*      */     }
/* 1716 */     return null;
/*      */   }
/*      */ 
/*      */   public long getSize(String paramString)
/*      */     throws FtpProtocolException, IOException
/*      */   {
/* 1733 */     if ((paramString == null) || (paramString.length() == 0)) {
/* 1734 */       throw new IllegalArgumentException("path can't be null or empty");
/*      */     }
/* 1736 */     issueCommandCheck("SIZE " + paramString);
/* 1737 */     if (this.lastReplyCode == FtpReplyCode.FILE_STATUS) {
/* 1738 */       String str = getResponseString();
/* 1739 */       str = str.substring(4, str.length() - 1);
/* 1740 */       return Long.parseLong(str);
/*      */     }
/* 1742 */     return -1L;
/*      */   }
/*      */ 
/*      */   public Date getLastModified(String paramString)
/*      */     throws FtpProtocolException, IOException
/*      */   {
/* 1770 */     issueCommandCheck("MDTM " + paramString);
/* 1771 */     if (this.lastReplyCode == FtpReplyCode.FILE_STATUS) {
/* 1772 */       String str = getResponseString().substring(4);
/* 1773 */       Date localDate = null;
/* 1774 */       for (SimpleDateFormat localSimpleDateFormat : dateFormats) {
/*      */         try {
/* 1776 */           localDate = localSimpleDateFormat.parse(str);
/*      */         } catch (ParseException localParseException) {
/*      */         }
/* 1779 */         if (localDate != null) {
/* 1780 */           return localDate;
/*      */         }
/*      */       }
/*      */     }
/* 1784 */     return null;
/*      */   }
/*      */ 
/*      */   public sun.net.ftp.FtpClient setDirParser(FtpDirParser paramFtpDirParser)
/*      */   {
/* 1798 */     this.parser = paramFtpDirParser;
/* 1799 */     return this;
/*      */   }
/*      */ 
/*      */   public Iterator<FtpDirEntry> listFiles(String paramString)
/*      */     throws FtpProtocolException, IOException
/*      */   {
/* 1880 */     Socket localSocket = null;
/* 1881 */     BufferedReader localBufferedReader = null;
/*      */     try {
/* 1883 */       localSocket = openDataConnection("MLSD " + paramString);
/*      */     }
/*      */     catch (FtpProtocolException localFtpProtocolException)
/*      */     {
/*      */     }
/*      */ 
/* 1889 */     if (localSocket != null) {
/* 1890 */       localBufferedReader = new BufferedReader(new InputStreamReader(localSocket.getInputStream()));
/* 1891 */       return new FtpFileIterator(this.mlsxParser, localBufferedReader);
/*      */     }
/* 1893 */     localSocket = openDataConnection("LIST " + paramString);
/* 1894 */     if (localSocket != null) {
/* 1895 */       localBufferedReader = new BufferedReader(new InputStreamReader(localSocket.getInputStream()));
/* 1896 */       return new FtpFileIterator(this.parser, localBufferedReader);
/*      */     }
/*      */ 
/* 1899 */     return null;
/*      */   }
/*      */ 
/*      */   private boolean sendSecurityData(byte[] paramArrayOfByte) throws IOException {
/* 1903 */     BASE64Encoder localBASE64Encoder = new BASE64Encoder();
/* 1904 */     String str = localBASE64Encoder.encode(paramArrayOfByte);
/* 1905 */     return issueCommand("ADAT " + str);
/*      */   }
/*      */ 
/*      */   private byte[] getSecurityData() {
/* 1909 */     String str = getLastResponseString();
/* 1910 */     if (str.substring(4, 9).equalsIgnoreCase("ADAT=")) {
/* 1911 */       BASE64Decoder localBASE64Decoder = new BASE64Decoder();
/*      */       try
/*      */       {
/* 1915 */         return localBASE64Decoder.decodeBuffer(str.substring(9, str.length() - 1));
/*      */       }
/*      */       catch (IOException localIOException) {
/*      */       }
/*      */     }
/* 1920 */     return null;
/*      */   }
/*      */ 
/*      */   public sun.net.ftp.FtpClient useKerberos()
/*      */     throws FtpProtocolException, IOException
/*      */   {
/* 1967 */     return this;
/*      */   }
/*      */ 
/*      */   public String getWelcomeMsg()
/*      */   {
/* 1977 */     return this.welcomeMsg;
/*      */   }
/*      */ 
/*      */   public FtpReplyCode getLastReplyCode()
/*      */   {
/* 1986 */     return this.lastReplyCode;
/*      */   }
/*      */ 
/*      */   public String getLastResponseString()
/*      */   {
/* 1996 */     StringBuffer localStringBuffer = new StringBuffer();
/* 1997 */     if (this.serverResponse != null) {
/* 1998 */       for (String str : this.serverResponse) {
/* 1999 */         if (str != null) {
/* 2000 */           localStringBuffer.append(str);
/*      */         }
/*      */       }
/*      */     }
/* 2004 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   public long getLastTransferSize()
/*      */   {
/* 2016 */     return this.lastTransSize;
/*      */   }
/*      */ 
/*      */   public String getLastFileName()
/*      */   {
/* 2029 */     return this.lastFileName;
/*      */   }
/*      */ 
/*      */   public sun.net.ftp.FtpClient startSecureSession()
/*      */     throws FtpProtocolException, IOException
/*      */   {
/* 2046 */     if (!isConnected()) {
/* 2047 */       throw new FtpProtocolException("Not connected yet", FtpReplyCode.BAD_SEQUENCE);
/*      */     }
/* 2049 */     if (this.sslFact == null) {
/*      */       try {
/* 2051 */         this.sslFact = ((SSLSocketFactory)SSLSocketFactory.getDefault());
/*      */       } catch (Exception localException1) {
/* 2053 */         throw new IOException(localException1.getLocalizedMessage());
/*      */       }
/*      */     }
/* 2056 */     issueCommandCheck("AUTH TLS");
/* 2057 */     Socket localSocket = null;
/*      */     try {
/* 2059 */       localSocket = this.sslFact.createSocket(this.server, this.serverAddr.getHostName(), this.serverAddr.getPort(), true);
/*      */     } catch (SSLException localSSLException) {
/*      */       try {
/* 2062 */         disconnect();
/*      */       } catch (Exception localException2) {
/*      */       }
/* 2065 */       throw localSSLException;
/*      */     }
/*      */ 
/* 2068 */     this.oldSocket = this.server;
/* 2069 */     this.server = localSocket;
/*      */     try {
/* 2071 */       this.out = new PrintStream(new BufferedOutputStream(this.server.getOutputStream()), true, encoding);
/*      */     }
/*      */     catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/* 2074 */       throw new InternalError(encoding + "encoding not found");
/*      */     }
/* 2076 */     this.in = new BufferedInputStream(this.server.getInputStream());
/*      */ 
/* 2078 */     issueCommandCheck("PBSZ 0");
/* 2079 */     issueCommandCheck("PROT P");
/* 2080 */     this.useCrypto = true;
/* 2081 */     return this;
/*      */   }
/*      */ 
/*      */   public sun.net.ftp.FtpClient endSecureSession()
/*      */     throws FtpProtocolException, IOException
/*      */   {
/* 2094 */     if (!this.useCrypto) {
/* 2095 */       return this;
/*      */     }
/*      */ 
/* 2098 */     issueCommandCheck("CCC");
/* 2099 */     issueCommandCheck("PROT C");
/* 2100 */     this.useCrypto = false;
/*      */ 
/* 2102 */     this.server = this.oldSocket;
/* 2103 */     this.oldSocket = null;
/*      */     try {
/* 2105 */       this.out = new PrintStream(new BufferedOutputStream(this.server.getOutputStream()), true, encoding);
/*      */     }
/*      */     catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/* 2108 */       throw new InternalError(encoding + "encoding not found");
/*      */     }
/* 2110 */     this.in = new BufferedInputStream(this.server.getInputStream());
/*      */ 
/* 2112 */     return this;
/*      */   }
/*      */ 
/*      */   public sun.net.ftp.FtpClient allocate(long paramLong)
/*      */     throws FtpProtocolException, IOException
/*      */   {
/* 2124 */     issueCommandCheck("ALLO " + paramLong);
/* 2125 */     return this;
/*      */   }
/*      */ 
/*      */   public sun.net.ftp.FtpClient structureMount(String paramString)
/*      */     throws FtpProtocolException, IOException
/*      */   {
/* 2139 */     issueCommandCheck("SMNT " + paramString);
/* 2140 */     return this;
/*      */   }
/*      */ 
/*      */   public String getSystem()
/*      */     throws FtpProtocolException, IOException
/*      */   {
/* 2153 */     issueCommandCheck("SYST");
/*      */ 
/* 2157 */     String str = getResponseString();
/*      */ 
/* 2159 */     return str.substring(4);
/*      */   }
/*      */ 
/*      */   public String getHelp(String paramString)
/*      */     throws FtpProtocolException, IOException
/*      */   {
/* 2173 */     issueCommandCheck("HELP " + paramString);
/*      */ 
/* 2192 */     Vector localVector = getResponseStrings();
/* 2193 */     if (localVector.size() == 1)
/*      */     {
/* 2195 */       return ((String)localVector.get(0)).substring(4);
/*      */     }
/*      */ 
/* 2199 */     StringBuffer localStringBuffer = new StringBuffer();
/* 2200 */     for (int i = 1; i < localVector.size() - 1; i++) {
/* 2201 */       localStringBuffer.append(((String)localVector.get(i)).substring(3));
/*      */     }
/* 2203 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   public sun.net.ftp.FtpClient siteCmd(String paramString)
/*      */     throws FtpProtocolException, IOException
/*      */   {
/* 2216 */     issueCommandCheck("SITE " + paramString);
/* 2217 */     return this;
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  113 */     int[] arrayOfInt = { 0, 0 };
/*  114 */     final String[] arrayOfString = { null };
/*      */ 
/*  116 */     AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public Object run()
/*      */       {
/*  120 */         this.val$vals[0] = Integer.getInteger("sun.net.client.defaultReadTimeout", 0).intValue();
/*  121 */         this.val$vals[1] = Integer.getInteger("sun.net.client.defaultConnectTimeout", 0).intValue();
/*  122 */         arrayOfString[0] = System.getProperty("file.encoding", "ISO8859_1");
/*  123 */         return null;
/*      */       }
/*      */     });
/*  126 */     if (arrayOfInt[0] == 0)
/*  127 */       defaultSoTimeout = -1;
/*      */     else {
/*  129 */       defaultSoTimeout = arrayOfInt[0];
/*      */     }
/*      */ 
/*  132 */     if (arrayOfInt[1] == 0)
/*  133 */       defaultConnectTimeout = -1;
/*      */     else {
/*  135 */       defaultConnectTimeout = arrayOfInt[1];
/*      */     }
/*      */ 
/*  138 */     encoding = arrayOfString[0];
/*      */     try {
/*  140 */       if (!isASCIISuperset(encoding))
/*  141 */         encoding = "ISO8859_1";
/*      */     }
/*      */     catch (Exception localException) {
/*  144 */       encoding = "ISO8859_1";
/*      */     }
/*      */ 
/*  147 */     patterns = new Pattern[patStrings.length];
/*  148 */     for (int j = 0; j < patStrings.length; j++) {
/*  149 */       patterns[j] = Pattern.compile(patStrings[j]);
/*      */     }
/*      */ 
/*  371 */     transPat = null;
/*      */ 
/*  548 */     epsvPat = null;
/*  549 */     pasvPat = null;
/*      */ 
/* 1744 */     MDTMformats = new String[] { "yyyyMMddHHmmss.SSS", "yyyyMMddHHmmss" };
/*      */ 
/* 1748 */     dateFormats = new SimpleDateFormat[MDTMformats.length];
/*      */ 
/* 1751 */     for (int i = 0; i < MDTMformats.length; i++) {
/* 1752 */       dateFormats[i] = new SimpleDateFormat(MDTMformats[i]);
/* 1753 */       dateFormats[i].setTimeZone(TimeZone.getTimeZone("GMT"));
/*      */     }
/*      */   }
/*      */ 
/*      */   private class DefaultParser
/*      */     implements FtpDirParser
/*      */   {
/*      */     private DefaultParser()
/*      */     {
/*      */     }
/*      */ 
/*      */     public FtpDirEntry parseLine(String paramString)
/*      */     {
/*  209 */       String str1 = null;
/*  210 */       String str2 = null;
/*  211 */       String str3 = null;
/*  212 */       String str4 = null;
/*  213 */       String str5 = null;
/*  214 */       String str6 = null;
/*  215 */       String str7 = null;
/*  216 */       boolean bool = false;
/*  217 */       Calendar localCalendar = Calendar.getInstance();
/*  218 */       int i = localCalendar.get(1);
/*      */ 
/*  220 */       Matcher localMatcher1 = null;
/*  221 */       for (int j = 0; j < FtpClient.patterns.length; j++) {
/*  222 */         localMatcher1 = FtpClient.patterns[j].matcher(paramString);
/*  223 */         if (localMatcher1.find())
/*      */         {
/*  226 */           str4 = localMatcher1.group(FtpClient.patternGroups[j][0]);
/*  227 */           str2 = localMatcher1.group(FtpClient.patternGroups[j][1]);
/*  228 */           str1 = localMatcher1.group(FtpClient.patternGroups[j][2]);
/*  229 */           if (FtpClient.patternGroups[j][4] > 0)
/*  230 */             str1 = str1 + ", " + localMatcher1.group(FtpClient.patternGroups[j][4]);
/*  231 */           else if (FtpClient.patternGroups[j][3] > 0) {
/*  232 */             str1 = str1 + ", " + String.valueOf(i);
/*      */           }
/*  234 */           if (FtpClient.patternGroups[j][3] > 0) {
/*  235 */             str3 = localMatcher1.group(FtpClient.patternGroups[j][3]);
/*      */           }
/*  237 */           if (FtpClient.patternGroups[j][5] > 0) {
/*  238 */             str5 = localMatcher1.group(FtpClient.patternGroups[j][5]);
/*  239 */             bool = str5.startsWith("d");
/*      */           }
/*  241 */           if (FtpClient.patternGroups[j][6] > 0) {
/*  242 */             str6 = localMatcher1.group(FtpClient.patternGroups[j][6]);
/*      */           }
/*  244 */           if (FtpClient.patternGroups[j][7] > 0) {
/*  245 */             str7 = localMatcher1.group(FtpClient.patternGroups[j][7]);
/*      */           }
/*      */ 
/*  248 */           if ("<DIR>".equals(str2)) {
/*  249 */             bool = true;
/*  250 */             str2 = null;
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*  255 */       if (str4 != null) {
/*      */         Date localDate;
/*      */         try {
/*  258 */           localDate = FtpClient.this.df.parse(str1);
/*      */         } catch (Exception localException) {
/*  260 */           localDate = null;
/*      */         }
/*  262 */         if ((localDate != null) && (str3 != null)) {
/*  263 */           int k = str3.indexOf(":");
/*  264 */           localCalendar.setTime(localDate);
/*  265 */           localCalendar.set(10, Integer.parseInt(str3.substring(0, k)));
/*  266 */           localCalendar.set(12, Integer.parseInt(str3.substring(k + 1)));
/*  267 */           localDate = localCalendar.getTime();
/*      */         }
/*      */ 
/*  271 */         Matcher localMatcher2 = FtpClient.linkp.matcher(str4);
/*  272 */         if (localMatcher2.find())
/*      */         {
/*  274 */           str4 = localMatcher2.group(1);
/*      */         }
/*  276 */         boolean[][] arrayOfBoolean = new boolean[3][3];
/*  277 */         for (int m = 0; m < 3; m++) {
/*  278 */           for (int n = 0; n < 3; n++) {
/*  279 */             arrayOfBoolean[m][n] = (str5.charAt(m * 3 + n) != '-' ? 1 : 0);
/*      */           }
/*      */         }
/*  282 */         FtpDirEntry localFtpDirEntry = new FtpDirEntry(str4);
/*  283 */         localFtpDirEntry.setUser(str6).setGroup(str7);
/*  284 */         localFtpDirEntry.setSize(Long.parseLong(str2)).setLastModified(localDate);
/*  285 */         localFtpDirEntry.setPermissions(arrayOfBoolean);
/*  286 */         localFtpDirEntry.setType(paramString.charAt(0) == 'l' ? FtpDirEntry.Type.LINK : bool ? FtpDirEntry.Type.DIR : FtpDirEntry.Type.FILE);
/*  287 */         return localFtpDirEntry;
/*      */       }
/*  289 */       return null;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class FtpFileIterator
/*      */     implements Iterator<FtpDirEntry>, Closeable
/*      */   {
/* 1804 */     private BufferedReader in = null;
/* 1805 */     private FtpDirEntry nextFile = null;
/* 1806 */     private FtpDirParser fparser = null;
/* 1807 */     private boolean eof = false;
/*      */ 
/*      */     public FtpFileIterator(FtpDirParser paramBufferedReader, BufferedReader arg3)
/*      */     {
/*      */       Object localObject;
/* 1810 */       this.in = localObject;
/* 1811 */       this.fparser = paramBufferedReader;
/* 1812 */       readNext();
/*      */     }
/*      */ 
/*      */     private void readNext() {
/* 1816 */       this.nextFile = null;
/* 1817 */       if (this.eof) {
/* 1818 */         return;
/*      */       }
/* 1820 */       String str = null;
/*      */       try {
/*      */         do {
/* 1823 */           str = this.in.readLine();
/* 1824 */           if (str != null) {
/* 1825 */             this.nextFile = this.fparser.parseLine(str);
/* 1826 */             if (this.nextFile != null)
/* 1827 */               return;
/*      */           }
/*      */         }
/* 1830 */         while (str != null);
/* 1831 */         this.in.close();
/*      */       } catch (IOException localIOException) {
/*      */       }
/* 1834 */       this.eof = true;
/*      */     }
/*      */ 
/*      */     public boolean hasNext() {
/* 1838 */       return this.nextFile != null;
/*      */     }
/*      */ 
/*      */     public FtpDirEntry next() {
/* 1842 */       FtpDirEntry localFtpDirEntry = this.nextFile;
/* 1843 */       readNext();
/* 1844 */       return localFtpDirEntry;
/*      */     }
/*      */ 
/*      */     public void remove() {
/* 1848 */       throw new UnsupportedOperationException("Not supported yet.");
/*      */     }
/*      */ 
/*      */     public void close() throws IOException {
/* 1852 */       if ((this.in != null) && (!this.eof)) {
/* 1853 */         this.in.close();
/*      */       }
/* 1855 */       this.eof = true;
/* 1856 */       this.nextFile = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class MLSxParser
/*      */     implements FtpDirParser
/*      */   {
/*  295 */     private SimpleDateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
/*      */ 
/*      */     private MLSxParser() {  } 
/*  298 */     public FtpDirEntry parseLine(String paramString) { String str1 = null;
/*  299 */       int i = paramString.lastIndexOf(";");
/*  300 */       if (i > 0) {
/*  301 */         str1 = paramString.substring(i + 1).trim();
/*  302 */         paramString = paramString.substring(0, i);
/*      */       } else {
/*  304 */         str1 = paramString.trim();
/*  305 */         paramString = "";
/*      */       }
/*  307 */       FtpDirEntry localFtpDirEntry = new FtpDirEntry(str1);
/*      */       Object localObject;
/*  308 */       while (!paramString.isEmpty())
/*      */       {
/*  310 */         i = paramString.indexOf(";");
/*  311 */         if (i > 0) {
/*  312 */           str2 = paramString.substring(0, i);
/*  313 */           paramString = paramString.substring(i + 1);
/*      */         } else {
/*  315 */           str2 = paramString;
/*  316 */           paramString = "";
/*      */         }
/*  318 */         i = str2.indexOf("=");
/*  319 */         if (i > 0) {
/*  320 */           localObject = str2.substring(0, i);
/*  321 */           String str3 = str2.substring(i + 1);
/*  322 */           localFtpDirEntry.addFact((String)localObject, str3);
/*      */         }
/*      */       }
/*  325 */       String str2 = localFtpDirEntry.getFact("Size");
/*  326 */       if (str2 != null) {
/*  327 */         localFtpDirEntry.setSize(Long.parseLong(str2));
/*      */       }
/*  329 */       str2 = localFtpDirEntry.getFact("Modify");
/*  330 */       if (str2 != null) {
/*  331 */         localObject = null;
/*      */         try {
/*  333 */           localObject = this.df.parse(str2);
/*      */         } catch (ParseException localParseException1) {
/*      */         }
/*  336 */         if (localObject != null) {
/*  337 */           localFtpDirEntry.setLastModified((Date)localObject);
/*      */         }
/*      */       }
/*  340 */       str2 = localFtpDirEntry.getFact("Create");
/*  341 */       if (str2 != null) {
/*  342 */         localObject = null;
/*      */         try {
/*  344 */           localObject = this.df.parse(str2);
/*      */         } catch (ParseException localParseException2) {
/*      */         }
/*  347 */         if (localObject != null) {
/*  348 */           localFtpDirEntry.setCreated((Date)localObject);
/*      */         }
/*      */       }
/*  351 */       str2 = localFtpDirEntry.getFact("Type");
/*  352 */       if (str2 != null) {
/*  353 */         if (str2.equalsIgnoreCase("file")) {
/*  354 */           localFtpDirEntry.setType(FtpDirEntry.Type.FILE);
/*      */         }
/*  356 */         if (str2.equalsIgnoreCase("dir")) {
/*  357 */           localFtpDirEntry.setType(FtpDirEntry.Type.DIR);
/*      */         }
/*  359 */         if (str2.equalsIgnoreCase("cdir")) {
/*  360 */           localFtpDirEntry.setType(FtpDirEntry.Type.CDIR);
/*      */         }
/*  362 */         if (str2.equalsIgnoreCase("pdir")) {
/*  363 */           localFtpDirEntry.setType(FtpDirEntry.Type.PDIR);
/*      */         }
/*      */       }
/*  366 */       return localFtpDirEntry;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.ftp.impl.FtpClient
 * JD-Core Version:    0.6.2
 */