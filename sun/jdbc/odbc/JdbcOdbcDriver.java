/*      */ package sun.jdbc.odbc;
/*      */ 
/*      */ import java.io.PrintWriter;
/*      */ import java.security.AccessController;
/*      */ import java.sql.Connection;
/*      */ import java.sql.DriverManager;
/*      */ import java.sql.DriverPropertyInfo;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.SQLFeatureNotSupportedException;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Properties;
/*      */ import java.util.logging.Logger;
/*      */ import sun.jdbc.odbc.ee.ConnectionHandler;
/*      */ import sun.security.action.GetPropertyAction;
/*      */ 
/*      */ public class JdbcOdbcDriver extends JdbcOdbcObject
/*      */   implements JdbcOdbcDriverInterface
/*      */ {
/*      */   protected static JdbcOdbc OdbcApi;
/*      */   protected static long hEnv;
/*      */   protected static long hDbc;
/*      */   protected static Hashtable connectionList;
/*      */   protected int iTimeOut;
/*      */   protected static String nativePrefix;
/*      */   protected PrintWriter outWriter;
/* 1023 */   protected JdbcOdbcTracer tracer = new JdbcOdbcTracer();
/*      */ 
/*      */   public JdbcOdbcDriver()
/*      */   {
/*   82 */     if (connectionList == null) {
/*   83 */       connectionList = new Hashtable();
/*      */     }
/*   85 */     nativePrefix = "";
/*      */   }
/*      */ 
/*      */   protected synchronized void finalize()
/*      */   {
/*   96 */     if (OdbcApi.getTracer().isTracing()) {
/*   97 */       OdbcApi.getTracer().trace("Driver.finalize");
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  104 */       if (hDbc != 0L) {
/*  105 */         disconnect(hDbc);
/*  106 */         closeConnection(hDbc);
/*  107 */         hDbc = 0L;
/*      */       }
/*      */     }
/*      */     catch (SQLException localSQLException)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   public synchronized Connection connect(String paramString, Properties paramProperties)
/*      */     throws SQLException
/*      */   {
/*  130 */     if (this.tracer.isTracing()) {
/*  131 */       this.tracer.trace("*Driver.connect (" + paramString + ")");
/*      */     }
/*      */ 
/*  137 */     if (!acceptsURL(paramString)) {
/*  138 */       return null;
/*      */     }
/*      */ 
/*  144 */     if (hDbc != 0L) {
/*  145 */       disconnect(hDbc);
/*  146 */       closeConnection(hDbc);
/*  147 */       hDbc = 0L;
/*      */     }
/*      */ 
/*  153 */     if (!initialize()) {
/*  154 */       return null;
/*      */     }
/*      */ 
/*  160 */     JdbcOdbcConnection localJdbcOdbcConnection = new JdbcOdbcConnection(OdbcApi, hEnv, this);
/*      */     int i;
/*  164 */     if (getTimeOut() > 0) {
/*  165 */       i = getTimeOut();
/*      */     }
/*      */     else {
/*  168 */       i = DriverManager.getLoginTimeout();
/*      */     }
/*      */ 
/*  174 */     localJdbcOdbcConnection.initialize(getSubName(paramString), paramProperties, i);
/*      */ 
/*  178 */     localJdbcOdbcConnection.setURL(paramString);
/*      */ 
/*  180 */     return localJdbcOdbcConnection;
/*      */   }
/*      */ 
/*      */   public synchronized Connection EEConnect(String paramString, Properties paramProperties)
/*      */     throws SQLException
/*      */   {
/*  199 */     if (this.tracer.isTracing()) {
/*  200 */       this.tracer.trace("*Driver.connect (" + paramString + ")");
/*      */     }
/*      */ 
/*  206 */     if (!acceptsURL(paramString)) {
/*  207 */       return null;
/*      */     }
/*      */ 
/*  213 */     if (hDbc != 0L) {
/*  214 */       disconnect(hDbc);
/*  215 */       closeConnection(hDbc);
/*  216 */       hDbc = 0L;
/*      */     }
/*      */ 
/*  222 */     if (!initialize()) {
/*  223 */       return null;
/*      */     }
/*      */ 
/*  229 */     ConnectionHandler localConnectionHandler = new ConnectionHandler(OdbcApi, hEnv, this);
/*      */     int i;
/*  232 */     if (getTimeOut() > 0) {
/*  233 */       i = getTimeOut();
/*      */     }
/*      */     else {
/*  236 */       i = DriverManager.getLoginTimeout();
/*      */     }
/*      */ 
/*  242 */     localConnectionHandler.initialize(getSubName(paramString), paramProperties, i);
/*      */ 
/*  246 */     localConnectionHandler.setURL(paramString);
/*      */ 
/*  248 */     return localConnectionHandler;
/*      */   }
/*      */ 
/*      */   public int getTimeOut()
/*      */   {
/*  257 */     return this.iTimeOut;
/*      */   }
/*      */ 
/*      */   public void setTimeOut(int paramInt)
/*      */   {
/*  267 */     this.iTimeOut = paramInt;
/*      */   }
/*      */ 
/*      */   public PrintWriter getWriter()
/*      */   {
/*  276 */     return this.outWriter;
/*      */   }
/*      */ 
/*      */   public void setWriter(PrintWriter paramPrintWriter)
/*      */   {
/*  286 */     this.outWriter = paramPrintWriter;
/*  287 */     this.tracer.setWriter(this.outWriter);
/*      */   }
/*      */ 
/*      */   public boolean acceptsURL(String paramString)
/*      */     throws SQLException
/*      */   {
/*  302 */     boolean bool = false;
/*      */ 
/*  307 */     if ((knownURL(paramString)) && 
/*  308 */       (trusted())) {
/*  309 */       bool = true;
/*      */     }
/*      */ 
/*  313 */     return bool;
/*      */   }
/*      */ 
/*      */   public DriverPropertyInfo[] getPropertyInfo(String paramString, Properties paramProperties)
/*      */     throws SQLException
/*      */   {
/*  332 */     if (this.tracer.isTracing()) {
/*  333 */       this.tracer.trace("*Driver.getPropertyInfo (" + paramString + ")");
/*      */     }
/*      */ 
/*  339 */     if (!acceptsURL(paramString)) {
/*  340 */       return null;
/*      */     }
/*      */ 
/*  346 */     if (!initialize()) {
/*  347 */       return null;
/*      */     }
/*      */ 
/*  352 */     String str1 = makeConnectionString(paramProperties);
/*      */ 
/*  357 */     String str2 = "";
/*      */ 
/*  359 */     str2 = getConnectionAttributes(getSubName(paramString), str1);
/*      */ 
/*  364 */     Hashtable localHashtable = getAttributeProperties(str2);
/*      */ 
/*  366 */     DriverPropertyInfo[] arrayOfDriverPropertyInfo = new DriverPropertyInfo[localHashtable.size()];
/*      */ 
/*  371 */     for (int i = 0; i < localHashtable.size(); i++) {
/*  372 */       arrayOfDriverPropertyInfo[i] = ((DriverPropertyInfo)localHashtable.get(new Integer(i)));
/*      */     }
/*      */ 
/*  376 */     return arrayOfDriverPropertyInfo;
/*      */   }
/*      */ 
/*      */   public int getMajorVersion()
/*      */   {
/*  385 */     return 2;
/*      */   }
/*      */ 
/*      */   public int getMinorVersion()
/*      */   {
/*  394 */     return 1;
/*      */   }
/*      */ 
/*      */   public boolean jdbcCompliant()
/*      */   {
/*  406 */     return true;
/*      */   }
/*      */ 
/*      */   public Logger getParentLogger()
/*      */     throws SQLFeatureNotSupportedException
/*      */   {
/*  423 */     throw new SQLFeatureNotSupportedException();
/*      */   }
/*      */ 
/*      */   private boolean initialize()
/*      */     throws SQLException
/*      */   {
/*  438 */     boolean bool = true;
/*      */ 
/*  444 */     if (OdbcApi == null) {
/*      */       try {
/*  446 */         OdbcApi = new JdbcOdbc(this.tracer, nativePrefix);
/*  447 */         this.tracer = OdbcApi.getTracer();
/*      */ 
/*  449 */         OdbcApi.charSet = ((String)AccessController.doPrivileged(new GetPropertyAction("file.encoding")));
/*      */       }
/*      */       catch (Exception localException1)
/*      */       {
/*  453 */         if (OdbcApi.getTracer().isTracing()) {
/*  454 */           OdbcApi.getTracer().trace("Unable to load JdbcOdbc library");
/*      */         }
/*  456 */         bool = false;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  461 */     if (getWriter() != null) {
/*  462 */       OdbcApi.getTracer().setWriter(getWriter());
/*      */     }
/*      */ 
/*  467 */     if (hEnv == 0L) {
/*      */       try {
/*  469 */         hEnv = OdbcApi.SQLAllocEnv();
/*      */       }
/*      */       catch (Exception localException2) {
/*  472 */         if (OdbcApi.getTracer().isTracing()) {
/*  473 */           OdbcApi.getTracer().trace("Unable to allocate environment");
/*      */         }
/*  475 */         bool = false;
/*      */       }
/*      */     }
/*      */ 
/*  479 */     return bool;
/*      */   }
/*      */ 
/*      */   private boolean knownURL(String paramString)
/*      */   {
/*  493 */     String str = getProtocol(paramString);
/*      */ 
/*  497 */     if (!str.equalsIgnoreCase("jdbc")) {
/*  498 */       return false;
/*      */     }
/*      */ 
/*  503 */     str = getSubProtocol(paramString);
/*  504 */     if (!str.equalsIgnoreCase("odbc")) {
/*  505 */       return false;
/*      */     }
/*      */ 
/*  508 */     return true;
/*      */   }
/*      */ 
/*      */   public static String getProtocol(String paramString)
/*      */   {
/*  519 */     String str = "";
/*      */ 
/*  524 */     int i = paramString.indexOf(':');
/*      */ 
/*  526 */     if (i >= 0) {
/*  527 */       str = paramString.substring(0, i);
/*      */     }
/*  529 */     return str;
/*      */   }
/*      */ 
/*      */   public static String getSubProtocol(String paramString)
/*      */   {
/*  540 */     String str = "";
/*      */ 
/*  545 */     int i = paramString.indexOf(':');
/*      */ 
/*  547 */     if (i >= 0)
/*      */     {
/*  552 */       int j = paramString.indexOf(':', i + 1);
/*      */ 
/*  554 */       if (j >= 0) {
/*  555 */         str = paramString.substring(i + 1, j);
/*      */       }
/*      */     }
/*  558 */     return str;
/*      */   }
/*      */ 
/*      */   public static String getSubName(String paramString)
/*      */   {
/*  570 */     String str = "";
/*      */ 
/*  575 */     int i = paramString.indexOf(':');
/*      */ 
/*  577 */     if (i >= 0)
/*      */     {
/*  582 */       int j = paramString.indexOf(':', i + 1);
/*      */ 
/*  584 */       if (j >= 0)
/*      */       {
/*  588 */         str = paramString.substring(j + 1);
/*      */       }
/*      */     }
/*  591 */     return str;
/*      */   }
/*      */ 
/*      */   private boolean trusted()
/*      */   {
/*  602 */     boolean bool = false;
/*      */ 
/*  604 */     if (this.tracer.isTracing()) {
/*  605 */       this.tracer.trace("JDBC to ODBC Bridge: Checking security");
/*      */     }
/*      */ 
/*  608 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  609 */     if (localSecurityManager != null)
/*      */     {
/*      */       try
/*      */       {
/*  620 */         String str = (String)AccessController.doPrivileged(new GetPropertyAction("browser"));
/*      */ 
/*  623 */         if ((str != null) && 
/*  624 */           (str.equalsIgnoreCase("Netscape Navigator")))
/*      */         {
/*  628 */           nativePrefix = "Netscape_";
/*  629 */           return true;
/*      */         }
/*      */       }
/*      */       catch (Exception localException)
/*      */       {
/*      */       }
/*      */ 
/*      */       try
/*      */       {
/*  638 */         localSecurityManager.checkWrite("JdbcOdbcSecurityCheck");
/*  639 */         bool = true;
/*      */       }
/*      */       catch (SecurityException localSecurityException) {
/*  642 */         if (this.tracer.isTracing()) {
/*  643 */           this.tracer.trace("Security check failed: " + localSecurityException.getMessage());
/*      */         }
/*      */ 
/*  646 */         bool = false;
/*      */       }
/*      */     }
/*      */     else {
/*  650 */       if (this.tracer.isTracing()) {
/*  651 */         this.tracer.trace("No SecurityManager present, assuming trusted application/applet");
/*      */       }
/*  653 */       bool = true;
/*      */     }
/*  655 */     bool = true;
/*  656 */     return bool;
/*      */   }
/*      */ 
/*      */   public String getConnectionAttributes(String paramString1, String paramString2)
/*      */     throws SQLException
/*      */   {
/*  669 */     String str1 = "DSN=" + paramString1 + paramString2;
/*      */ 
/*  674 */     if (hDbc == 0L) {
/*  675 */       hDbc = allocConnection(hEnv);
/*      */     }
/*      */ 
/*  681 */     String str2 = OdbcApi.SQLBrowseConnect(hDbc, str1);
/*      */ 
/*  687 */     if (str2 == null) {
/*  688 */       str2 = "";
/*      */ 
/*  692 */       disconnect(hDbc);
/*  693 */       closeConnection(hDbc);
/*  694 */       hDbc = 0L;
/*      */     }
/*      */ 
/*  697 */     return str2;
/*      */   }
/*      */ 
/*      */   public Hashtable getAttributeProperties(String paramString)
/*      */   {
/*  717 */     int i = 0;
/*  718 */     int j = 0;
/*      */ 
/*  722 */     int i1 = 0;
/*      */ 
/*  732 */     Hashtable localHashtable = new Hashtable();
/*      */ 
/*  734 */     int i2 = paramString.length();
/*      */ 
/*  736 */     while (i < i2)
/*      */     {
/*  738 */       boolean bool = true;
/*  739 */       String str2 = null;
/*  740 */       String str3 = null;
/*  741 */       String[] arrayOfString = null;
/*  742 */       String str4 = null;
/*      */ 
/*  745 */       j = paramString.indexOf(";", i);
/*      */ 
/*  747 */       if (j < 0)
/*      */       {
/*  750 */         j = i2;
/*      */       }
/*      */ 
/*  753 */       String str1 = paramString.substring(i, j);
/*      */ 
/*  755 */       int k = 0;
/*  756 */       int m = str1.indexOf(":", 0);
/*  757 */       int n = str1.indexOf("=", 0);
/*      */ 
/*  762 */       if (str1.startsWith("*")) {
/*  763 */         bool = false;
/*  764 */         k++;
/*      */       }
/*      */ 
/*  769 */       if (m > 0) {
/*  770 */         str2 = str1.substring(k, m);
/*      */       }
/*      */ 
/*  775 */       if ((m > 0) && (n > 0))
/*      */       {
/*  777 */         str3 = str1.substring(m + 1, n);
/*      */       }
/*      */ 
/*  783 */       if (n > 0) {
/*  784 */         str4 = str1.substring(n + 1);
/*      */ 
/*  786 */         if (str4.equals("?")) {
/*  787 */           str4 = null;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  794 */       if ((str4 != null) && 
/*  795 */         (str4.startsWith("{"))) {
/*  796 */         arrayOfString = listToArray(str4);
/*  797 */         str4 = null;
/*      */       }
/*      */ 
/*  803 */       DriverPropertyInfo localDriverPropertyInfo = new DriverPropertyInfo(str2, str4);
/*      */ 
/*  805 */       localDriverPropertyInfo.description = str3;
/*  806 */       localDriverPropertyInfo.required = bool;
/*  807 */       localDriverPropertyInfo.choices = arrayOfString;
/*      */ 
/*  811 */       localHashtable.put(new Integer(i1), localDriverPropertyInfo);
/*  812 */       i1++;
/*      */ 
/*  815 */       i = j + 1;
/*      */     }
/*      */ 
/*  818 */     return localHashtable;
/*      */   }
/*      */ 
/*      */   protected static String makeConnectionString(Properties paramProperties)
/*      */   {
/*  829 */     String str1 = "";
/*      */ 
/*  833 */     Enumeration localEnumeration = paramProperties.propertyNames();
/*      */ 
/*  842 */     OdbcApi.charSet = paramProperties.getProperty("charSet", (String)AccessController.doPrivileged(new GetPropertyAction("file.encoding")));
/*      */ 
/*  846 */     while (localEnumeration.hasMoreElements()) {
/*  847 */       String str2 = (String)localEnumeration.nextElement();
/*  848 */       String str3 = paramProperties.getProperty(str2);
/*      */ 
/*  853 */       if (str2.equalsIgnoreCase("user")) {
/*  854 */         str2 = "UID";
/*      */       }
/*      */ 
/*  857 */       if (str2.equalsIgnoreCase("password")) {
/*  858 */         str2 = "PWD";
/*      */       }
/*      */ 
/*  864 */       if (str3 != null) {
/*  865 */         str1 = str1 + ";" + str2 + "=" + str3;
/*      */       }
/*      */     }
/*      */ 
/*  869 */     return str1;
/*      */   }
/*      */ 
/*      */   protected static String[] listToArray(String paramString)
/*      */   {
/*  882 */     String[] arrayOfString = null;
/*      */ 
/*  884 */     Hashtable localHashtable = new Hashtable();
/*  885 */     int i = 0;
/*  886 */     int j = 1;
/*  887 */     int k = 1;
/*  888 */     int m = paramString.length();
/*      */ 
/*  890 */     if (!paramString.startsWith("{")) {
/*  891 */       return null;
/*      */     }
/*      */ 
/*  894 */     if (!paramString.endsWith("}"))
/*  895 */       return null;
/*      */     String str;
/*  900 */     while (j < m)
/*      */     {
/*  903 */       k = paramString.indexOf(",", j);
/*      */ 
/*  907 */       if (k < 0) {
/*  908 */         k = m - 1;
/*      */       }
/*      */ 
/*  911 */       str = paramString.substring(j, k);
/*  912 */       localHashtable.put(new Integer(i), str);
/*  913 */       i++;
/*      */ 
/*  915 */       j = k + 1;
/*      */     }
/*      */ 
/*  920 */     arrayOfString = new String[i];
/*      */ 
/*  922 */     for (j = 0; j < i; j++) {
/*  923 */       str = (String)localHashtable.get(new Integer(j));
/*  924 */       arrayOfString[j] = str;
/*      */     }
/*      */ 
/*  927 */     return arrayOfString;
/*      */   }
/*      */ 
/*      */   public long allocConnection(long paramLong)
/*      */     throws SQLException
/*      */   {
/*  944 */     long l = 0L;
/*      */ 
/*  947 */     l = OdbcApi.SQLAllocConnect(paramLong);
/*      */ 
/*  950 */     connectionList.put(new Long(l), new Long(paramLong));
/*      */ 
/*  953 */     return l;
/*      */   }
/*      */ 
/*      */   public void closeConnection(long paramLong)
/*      */     throws SQLException
/*      */   {
/*  970 */     OdbcApi.SQLFreeConnect(paramLong);
/*      */ 
/*  973 */     Long localLong = (Long)connectionList.remove(new Long(paramLong));
/*      */ 
/*  979 */     if ((connectionList.size() == 0) && 
/*  980 */       (hEnv != 0L)) {
/*  981 */       OdbcApi.SQLFreeEnv(hEnv);
/*  982 */       hEnv = 0L;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void disconnect(long paramLong)
/*      */     throws SQLException
/*      */   {
/*  998 */     OdbcApi.SQLDisconnect(paramLong);
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*   43 */     JdbcOdbcTracer localJdbcOdbcTracer = new JdbcOdbcTracer();
/*   44 */     if (localJdbcOdbcTracer.isTracing()) {
/*   45 */       localJdbcOdbcTracer.trace("JdbcOdbcDriver class loaded");
/*      */     }
/*      */ 
/*   48 */     JdbcOdbcDriver localJdbcOdbcDriver = new JdbcOdbcDriver();
/*      */     try
/*      */     {
/*   53 */       DriverManager.registerDriver(localJdbcOdbcDriver);
/*      */     }
/*      */     catch (SQLException localSQLException) {
/*   56 */       if (localJdbcOdbcTracer.isTracing())
/*   57 */         localJdbcOdbcTracer.trace("Unable to register driver");
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.jdbc.odbc.JdbcOdbcDriver
 * JD-Core Version:    0.6.2
 */