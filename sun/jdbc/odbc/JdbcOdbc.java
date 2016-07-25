/*      */ package sun.jdbc.odbc;
/*      */ 
/*      */ import java.io.PrintStream;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.math.BigDecimal;
/*      */ import java.security.AccessController;
/*      */ import java.sql.DataTruncation;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.SQLWarning;
/*      */ import java.sql.Time;
/*      */ import java.sql.Timestamp;
/*      */ import java.util.Calendar;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import sun.security.action.LoadLibraryAction;
/*      */ 
/*      */ public class JdbcOdbc extends JdbcOdbcObject
/*      */ {
/*      */   public static final int MajorVersion = 2;
/*      */   public static final int MinorVersion = 1;
/*      */   public String charSet;
/*      */   public String odbcDriverName;
/*      */   private static Map hstmtMap;
/* 7173 */   public JdbcOdbcTracer tracer = new JdbcOdbcTracer();
/*      */ 
/*      */   public static void addHstmt(long paramLong1, long paramLong2)
/*      */   {
/*   34 */     hstmtMap.put(new Long(paramLong1), new Long(paramLong2));
/*      */   }
/*      */ 
/*      */   JdbcOdbc(JdbcOdbcTracer paramJdbcOdbcTracer, String paramString)
/*      */     throws SQLException
/*      */   {
/*   50 */     this.tracer = paramJdbcOdbcTracer;
/*      */     try
/*      */     {
/*   54 */       if (paramJdbcOdbcTracer.isTracing()) {
/*   55 */         java.util.Date localDate = new java.util.Date();
/*      */ 
/*   57 */         String str = "";
/*   58 */         int i = 1;
/*      */ 
/*   63 */         if (i < 1000) str = str + "0";
/*   64 */         if (i < 100) str = str + "0";
/*   65 */         if (i < 10) str = str + "0";
/*   66 */         str = str + "" + i;
/*      */ 
/*   68 */         paramJdbcOdbcTracer.trace("JDBC to ODBC Bridge 2." + str);
/*   69 */         paramJdbcOdbcTracer.trace("Current Date/Time: " + localDate.toString());
/*   70 */         paramJdbcOdbcTracer.trace("Loading " + paramString + "JdbcOdbc library");
/*      */       }
/*   72 */       AccessController.doPrivileged(new LoadLibraryAction(paramString + "JdbcOdbc"));
/*      */ 
/*   76 */       if (hstmtMap == null) {
/*   77 */         hstmtMap = Collections.synchronizedMap(new HashMap());
/*      */       }
/*      */     }
/*      */     catch (UnsatisfiedLinkError localUnsatisfiedLinkError)
/*      */     {
/*   82 */       if (paramJdbcOdbcTracer.isTracing()) {
/*   83 */         paramJdbcOdbcTracer.trace("Unable to load " + paramString + "JdbcOdbc library");
/*      */       }
/*      */ 
/*   86 */       throw new SQLException("Unable to load " + paramString + "JdbcOdbc library");
/*      */     }
/*      */   }
/*      */ 
/*      */   public long SQLAllocConnect(long paramLong)
/*      */     throws SQLException
/*      */   {
/*  104 */     long l = 0L;
/*      */ 
/*  107 */     if (this.tracer.isTracing()) {
/*  108 */       this.tracer.trace("Allocating Connection handle (SQLAllocConnect)");
/*      */     }
/*      */ 
/*  111 */     byte[] arrayOfByte = new byte[1];
/*  112 */     l = allocConnect(paramLong, arrayOfByte);
/*      */ 
/*  114 */     if (arrayOfByte[0] != 0)
/*      */     {
/*  117 */       standardError((short)arrayOfByte[0], paramLong, 0L, 0L);
/*      */     }
/*  122 */     else if (this.tracer.isTracing()) {
/*  123 */       this.tracer.trace("hDbc=" + l);
/*      */     }
/*      */ 
/*  126 */     return l;
/*      */   }
/*      */ 
/*      */   public long SQLAllocEnv()
/*      */     throws SQLException
/*      */   {
/*  136 */     long l = 0L;
/*      */ 
/*  139 */     if (this.tracer.isTracing()) {
/*  140 */       this.tracer.trace("Allocating Environment handle (SQLAllocEnv)");
/*      */     }
/*      */ 
/*  143 */     byte[] arrayOfByte = new byte[1];
/*  144 */     l = allocEnv(arrayOfByte);
/*      */ 
/*  146 */     if (arrayOfByte[0] != 0) {
/*  147 */       throwGenericSQLException();
/*      */     }
/*  150 */     else if (this.tracer.isTracing()) {
/*  151 */       this.tracer.trace("hEnv=" + l);
/*      */     }
/*      */ 
/*  154 */     return l;
/*      */   }
/*      */ 
/*      */   public long SQLAllocStmt(long paramLong)
/*      */     throws SQLException
/*      */   {
/*  165 */     long l = 0L;
/*      */ 
/*  168 */     if (this.tracer.isTracing()) {
/*  169 */       this.tracer.trace("Allocating Statement Handle (SQLAllocStmt), hDbc=" + paramLong);
/*      */     }
/*      */ 
/*  172 */     byte[] arrayOfByte = new byte[1];
/*  173 */     l = allocStmt(paramLong, arrayOfByte);
/*      */ 
/*  175 */     if (arrayOfByte[0] != 0)
/*      */     {
/*  178 */       standardError((short)arrayOfByte[0], 0L, paramLong, 0L);
/*      */     }
/*  183 */     else if (this.tracer.isTracing()) {
/*  184 */       this.tracer.trace("hStmt=" + l);
/*      */     }
/*      */ 
/*  187 */     addHstmt(l, paramLong);
/*  188 */     return l;
/*      */   }
/*      */ 
/*      */   public void SQLBindColAtExec(long paramLong, int paramInt1, int paramInt2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, long[] paramArrayOfLong)
/*      */     throws SQLException
/*      */   {
/*  206 */     if (this.tracer.isTracing()) {
/*  207 */       this.tracer.trace("Binding Column DATA_AT_EXEC (SQLBindCol), hStmt=" + paramLong + ", icol=" + paramInt1 + ", SQLtype=" + paramInt2);
/*      */     }
/*      */ 
/*  210 */     byte[] arrayOfByte = new byte[1];
/*      */ 
/*  212 */     bindColAtExec(paramLong, paramInt1, paramInt2, paramArrayOfByte1, paramArrayOfByte2, paramArrayOfLong, arrayOfByte);
/*      */ 
/*  214 */     if (arrayOfByte[0] != 0)
/*      */     {
/*  217 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLBindColBinary(long paramLong, int paramInt1, Object[] paramArrayOfObject, byte[] paramArrayOfByte1, int paramInt2, byte[] paramArrayOfByte2, long[] paramArrayOfLong)
/*      */     throws SQLException
/*      */   {
/*  240 */     if (this.tracer.isTracing()) {
/*  241 */       this.tracer.trace("Bind column binary (SQLBindColBinary), hStmt=" + paramLong + ", icol=" + paramInt1);
/*      */     }
/*      */ 
/*  244 */     byte[] arrayOfByte = new byte[1];
/*      */ 
/*  246 */     bindColBinary(paramLong, paramInt1, paramArrayOfObject, paramArrayOfByte1, paramInt2, paramArrayOfByte2, paramArrayOfLong, arrayOfByte);
/*      */ 
/*  248 */     if (arrayOfByte[0] != 0)
/*      */     {
/*  251 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLBindColDate(long paramLong, int paramInt, Object[] paramArrayOfObject, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, long[] paramArrayOfLong)
/*      */     throws SQLException
/*      */   {
/*  271 */     if (this.tracer.isTracing()) {
/*  272 */       this.tracer.trace("Bound Column Date (SQLBindColDate), hStmt=" + paramLong + ", icol=" + paramInt);
/*      */     }
/*      */ 
/*  277 */     java.sql.Date localDate = null;
/*      */ 
/*  279 */     int i = paramArrayOfObject.length;
/*      */ 
/*  281 */     int[] arrayOfInt1 = new int[i];
/*  282 */     int[] arrayOfInt2 = new int[i];
/*  283 */     int[] arrayOfInt3 = new int[i];
/*      */ 
/*  285 */     byte[] arrayOfByte = new byte[1];
/*      */ 
/*  287 */     Calendar localCalendar = Calendar.getInstance();
/*      */ 
/*  289 */     for (int j = 0; j < i; j++)
/*      */     {
/*  291 */       if (paramArrayOfObject[j] != null)
/*      */       {
/*  293 */         localDate = (java.sql.Date)paramArrayOfObject[j];
/*      */ 
/*  295 */         localCalendar.setTime(localDate);
/*      */ 
/*  297 */         arrayOfInt1[j] = localCalendar.get(1);
/*  298 */         arrayOfInt2[j] = (localCalendar.get(2) + 1);
/*  299 */         arrayOfInt3[j] = localCalendar.get(5);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  304 */     bindColDate(paramLong, paramInt, arrayOfInt1, arrayOfInt2, arrayOfInt3, paramArrayOfByte1, paramArrayOfByte2, paramArrayOfLong, arrayOfByte);
/*      */ 
/*  313 */     if (arrayOfByte[0] != 0)
/*      */     {
/*  316 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLBindColDefault(long paramLong, int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
/*      */     throws SQLException
/*      */   {
/*  336 */     if (this.tracer.isTracing()) {
/*  337 */       this.tracer.trace("Binding default column (SQLBindCol), hStmt=" + paramLong + ", ipar=" + paramInt + ", \t\t\tlength=" + paramArrayOfByte1.length);
/*      */     }
/*      */ 
/*  340 */     byte[] arrayOfByte = new byte[1];
/*  341 */     bindColDefault(paramLong, paramInt, paramArrayOfByte1, paramArrayOfByte2, arrayOfByte);
/*      */ 
/*  343 */     if (arrayOfByte[0] != 0)
/*      */     {
/*  346 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLBindColDouble(long paramLong, int paramInt, Object[] paramArrayOfObject, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, long[] paramArrayOfLong)
/*      */     throws SQLException
/*      */   {
/*  366 */     if (this.tracer.isTracing()) {
/*  367 */       this.tracer.trace("Bind column Double (SQLBindColDouble), hStmt=" + paramLong + ", icol=" + paramInt);
/*      */     }
/*      */ 
/*  372 */     double[] arrayOfDouble = new double[paramArrayOfObject.length];
/*      */ 
/*  374 */     for (int i = 0; i < paramArrayOfObject.length; i++)
/*      */     {
/*  376 */       if (paramArrayOfObject[i] != null) {
/*  377 */         arrayOfDouble[i] = ((Double)paramArrayOfObject[i]).doubleValue();
/*      */       }
/*      */     }
/*  380 */     byte[] arrayOfByte = new byte[1];
/*      */ 
/*  382 */     bindColDouble(paramLong, paramInt, arrayOfDouble, paramArrayOfByte1, paramArrayOfByte2, paramArrayOfLong, arrayOfByte);
/*      */ 
/*  384 */     if (arrayOfByte[0] != 0)
/*      */     {
/*  387 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLBindColFloat(long paramLong, int paramInt, Object[] paramArrayOfObject, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, long[] paramArrayOfLong)
/*      */     throws SQLException
/*      */   {
/*  410 */     float[] arrayOfFloat = new float[paramArrayOfObject.length];
/*      */ 
/*  412 */     for (int i = 0; i < paramArrayOfObject.length; i++)
/*      */     {
/*  414 */       if (paramArrayOfObject[i] != null) {
/*  415 */         arrayOfFloat[i] = ((Float)paramArrayOfObject[i]).floatValue();
/*      */       }
/*      */     }
/*      */ 
/*  419 */     if (this.tracer.isTracing()) {
/*  420 */       this.tracer.trace("Binding default column (SQLBindCol Float), hStmt=" + paramLong + ", icol=" + paramInt);
/*      */     }
/*      */ 
/*  423 */     byte[] arrayOfByte = new byte[1];
/*  424 */     bindColFloat(paramLong, paramInt, arrayOfFloat, paramArrayOfByte1, paramArrayOfByte2, paramArrayOfLong, arrayOfByte);
/*      */ 
/*  426 */     if (arrayOfByte[0] != 0)
/*      */     {
/*  429 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLBindColInteger(long paramLong, int paramInt, Object[] paramArrayOfObject, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, long[] paramArrayOfLong)
/*      */     throws SQLException
/*      */   {
/*  450 */     if (this.tracer.isTracing()) {
/*  451 */       this.tracer.trace("Binding default column (SQLBindCol Integer), hStmt=" + paramLong + ", icol=" + paramInt);
/*      */     }
/*      */ 
/*  454 */     int[] arrayOfInt = new int[paramArrayOfObject.length];
/*      */ 
/*  456 */     for (int i = 0; i < paramArrayOfObject.length; i++)
/*      */     {
/*  458 */       if (paramArrayOfObject[i] != null) {
/*  459 */         arrayOfInt[i] = ((Integer)paramArrayOfObject[i]).intValue();
/*      */       }
/*      */     }
/*  462 */     byte[] arrayOfByte = new byte[1];
/*      */ 
/*  464 */     bindColInteger(paramLong, paramInt, arrayOfInt, paramArrayOfByte1, paramArrayOfByte2, paramArrayOfLong, arrayOfByte);
/*      */ 
/*  466 */     if (arrayOfByte[0] != 0)
/*      */     {
/*  469 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLBindColString(long paramLong, int paramInt1, int paramInt2, Object[] paramArrayOfObject, int paramInt3, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, long[] paramArrayOfLong)
/*      */     throws SQLException
/*      */   {
/*  493 */     if (this.tracer.isTracing()) {
/*  494 */       this.tracer.trace("Binding string/decimal Column (SQLBindColString), hStmt=" + paramLong + ", icol=" + paramInt1 + ", SQLtype=" + paramInt2 + ", rgbValue=" + paramArrayOfObject);
/*      */     }
/*      */ 
/*  499 */     byte[] arrayOfByte = new byte[1];
/*      */ 
/*  501 */     bindColString(paramLong, paramInt1, OdbcDef.jdbcTypeToOdbc(paramInt2), paramArrayOfObject, paramInt3, paramArrayOfByte1, paramArrayOfByte2, paramArrayOfLong, arrayOfByte);
/*      */ 
/*  504 */     if (arrayOfByte[0] != 0)
/*      */     {
/*  507 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLBindColTime(long paramLong, int paramInt, Object[] paramArrayOfObject, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, long[] paramArrayOfLong)
/*      */     throws SQLException
/*      */   {
/*  527 */     if (this.tracer.isTracing()) {
/*  528 */       this.tracer.trace("Bind column Time (SQLBindColTime), hStmt=" + paramLong + ", icol=" + paramInt);
/*      */     }
/*      */ 
/*  535 */     Time localTime = null;
/*      */ 
/*  537 */     int i = paramArrayOfObject.length;
/*      */ 
/*  539 */     int[] arrayOfInt1 = new int[i];
/*  540 */     int[] arrayOfInt2 = new int[i];
/*  541 */     int[] arrayOfInt3 = new int[i];
/*      */ 
/*  543 */     byte[] arrayOfByte = new byte[1];
/*      */ 
/*  545 */     Calendar localCalendar = Calendar.getInstance();
/*      */ 
/*  547 */     for (int j = 0; j < i; j++)
/*      */     {
/*  549 */       if (paramArrayOfObject[j] != null)
/*      */       {
/*  551 */         localTime = (Time)paramArrayOfObject[j];
/*      */ 
/*  553 */         localCalendar.setTime(localTime);
/*      */ 
/*  555 */         arrayOfInt1[j] = localCalendar.get(11);
/*  556 */         arrayOfInt2[j] = localCalendar.get(12);
/*  557 */         arrayOfInt3[j] = localCalendar.get(13);
/*      */       }
/*      */     }
/*      */ 
/*  561 */     bindColTime(paramLong, paramInt, arrayOfInt1, arrayOfInt2, arrayOfInt3, paramArrayOfByte1, paramArrayOfByte2, paramArrayOfLong, arrayOfByte);
/*      */ 
/*  570 */     if (arrayOfByte[0] != 0)
/*      */     {
/*  573 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLBindColTimestamp(long paramLong, int paramInt, Object[] paramArrayOfObject, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, long[] paramArrayOfLong)
/*      */     throws SQLException
/*      */   {
/*  594 */     if (this.tracer.isTracing()) {
/*  595 */       this.tracer.trace("Bind Column Timestamp (SQLBindColTimestamp), hStmt=" + paramLong + ", icol=" + paramInt);
/*      */     }
/*      */ 
/*  602 */     Timestamp localTimestamp = null;
/*      */ 
/*  604 */     int i = paramArrayOfObject.length;
/*      */ 
/*  606 */     int[] arrayOfInt1 = new int[i];
/*  607 */     int[] arrayOfInt2 = new int[i];
/*  608 */     int[] arrayOfInt3 = new int[i];
/*  609 */     int[] arrayOfInt4 = new int[i];
/*  610 */     int[] arrayOfInt5 = new int[i];
/*  611 */     int[] arrayOfInt6 = new int[i];
/*  612 */     int[] arrayOfInt7 = new int[i];
/*      */ 
/*  614 */     byte[] arrayOfByte = new byte[1];
/*      */ 
/*  616 */     Calendar localCalendar = Calendar.getInstance();
/*      */ 
/*  618 */     for (int j = 0; j < i; j++)
/*      */     {
/*  620 */       if (paramArrayOfObject[j] != null)
/*      */       {
/*  622 */         localTimestamp = (Timestamp)paramArrayOfObject[j];
/*      */ 
/*  624 */         localCalendar.setTime(localTimestamp);
/*      */ 
/*  626 */         arrayOfInt1[j] = localCalendar.get(1);
/*  627 */         arrayOfInt2[j] = (localCalendar.get(2) + 1);
/*  628 */         arrayOfInt3[j] = localCalendar.get(5);
/*  629 */         arrayOfInt4[j] = localCalendar.get(11);
/*  630 */         arrayOfInt5[j] = localCalendar.get(12);
/*  631 */         arrayOfInt6[j] = localCalendar.get(13);
/*  632 */         arrayOfInt7[j] = localTimestamp.getNanos();
/*      */       }
/*      */     }
/*      */ 
/*  636 */     bindColTimestamp(paramLong, paramInt, arrayOfInt1, arrayOfInt2, arrayOfInt3, arrayOfInt4, arrayOfInt5, arrayOfInt6, arrayOfInt7, paramArrayOfByte1, paramArrayOfByte2, paramArrayOfLong, arrayOfByte);
/*      */ 
/*  646 */     if (arrayOfByte[0] != 0)
/*      */     {
/*  649 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLBindInParameterAtExec(long paramLong, int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, long[] paramArrayOfLong)
/*      */     throws SQLException
/*      */   {
/*  672 */     if (this.tracer.isTracing()) {
/*  673 */       this.tracer.trace("Binding DATA_AT_EXEC parameter (SQLBindParameter), hStmt=" + paramLong + ", ipar=" + paramInt1 + ", SQLtype=" + paramInt2 + ", len=" + paramInt3);
/*      */     }
/*      */ 
/*  676 */     byte[] arrayOfByte = new byte[1];
/*  677 */     bindInParameterAtExec(paramLong, paramInt1, paramInt2, paramInt3, paramArrayOfByte1, paramArrayOfByte2, arrayOfByte, paramArrayOfLong);
/*      */ 
/*  680 */     if (arrayOfByte[0] != 0)
/*      */     {
/*  683 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLBindInOutParameterAtExec(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte1, int paramInt5, byte[] paramArrayOfByte2, long[] paramArrayOfLong)
/*      */     throws SQLException
/*      */   {
/*  708 */     if (this.tracer.isTracing()) {
/*  709 */       this.tracer.trace("Binding DATA_AT_EXEC parameter (SQLBindParameter), hStmt=" + paramLong + ", ipar=" + paramInt1 + ", SQLtype=" + paramInt3 + ", streamLength = " + paramInt5 + " ,dataBufLen = " + paramInt4);
/*      */     }
/*      */ 
/*  714 */     byte[] arrayOfByte = new byte[1];
/*  715 */     bindInOutParameterAtExec(paramLong, paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfByte1, paramInt5, paramArrayOfByte2, arrayOfByte, paramArrayOfLong);
/*      */ 
/*  718 */     if (arrayOfByte[0] != 0)
/*      */     {
/*  721 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLBindInParameterBinary(long paramLong, int paramInt1, int paramInt2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, long[] paramArrayOfLong)
/*      */     throws SQLException
/*      */   {
/*  748 */     int i = 0;
/*      */ 
/*  756 */     if (paramArrayOfByte2.length < 8000)
/*  757 */       i = paramArrayOfByte2.length;
/*      */     else {
/*  759 */       i = 8000;
/*      */     }
/*  761 */     if (this.tracer.isTracing()) {
/*  762 */       this.tracer.trace("Binding IN binary parameter (SQLBindParameter), hStmt=" + paramLong + ", ipar=" + paramInt1 + ", SQLtype=" + paramInt2);
/*      */ 
/*  764 */       dumpByte(paramArrayOfByte1, paramArrayOfByte1.length);
/*      */     }
/*      */ 
/*  767 */     byte[] arrayOfByte = new byte[1];
/*  768 */     bindInParameterBinary(paramLong, paramInt1, OdbcDef.jdbcTypeToOdbc(paramInt2), paramArrayOfByte1, i, paramArrayOfByte2, paramArrayOfByte3, arrayOfByte, paramArrayOfLong);
/*      */ 
/*  773 */     if (arrayOfByte[0] != 0)
/*      */     {
/*  776 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLBindInParameterDate(long paramLong, int paramInt, java.sql.Date paramDate, byte[] paramArrayOfByte, long[] paramArrayOfLong)
/*      */     throws SQLException
/*      */   {
/*  796 */     if (this.tracer.isTracing()) {
/*  797 */       this.tracer.trace("Binding IN parameter date (SQLBindParameter), hStmt=" + paramLong + ", ipar=" + paramInt + ", rgbValue=" + paramDate.toString());
/*      */     }
/*      */ 
/*  801 */     byte[] arrayOfByte = new byte[1];
/*      */ 
/*  803 */     Calendar localCalendar = Calendar.getInstance();
/*  804 */     localCalendar.setTime(paramDate);
/*      */ 
/*  806 */     bindInParameterDate(paramLong, paramInt, localCalendar.get(1), localCalendar.get(2) + 1, localCalendar.get(5), paramArrayOfByte, arrayOfByte, paramArrayOfLong);
/*      */ 
/*  812 */     if (arrayOfByte[0] != 0)
/*      */     {
/*  815 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLBindInParameterCalendarDate(long paramLong, int paramInt, Calendar paramCalendar, byte[] paramArrayOfByte, long[] paramArrayOfLong)
/*      */     throws SQLException
/*      */   {
/*  835 */     if (this.tracer.isTracing()) {
/*  836 */       this.tracer.trace("Binding IN parameter date (SQLBindParameter), hStmt=" + paramLong + ", ipar=" + paramInt + ", rgbValue=" + paramCalendar.toString());
/*      */     }
/*      */ 
/*  840 */     byte[] arrayOfByte = new byte[1];
/*      */ 
/*  842 */     bindInParameterDate(paramLong, paramInt, paramCalendar.get(1), paramCalendar.get(2) + 1, paramCalendar.get(5), paramArrayOfByte, arrayOfByte, paramArrayOfLong);
/*      */ 
/*  848 */     if (arrayOfByte[0] != 0)
/*      */     {
/*  851 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLBindInParameterDouble(long paramLong, int paramInt1, int paramInt2, int paramInt3, double paramDouble, byte[] paramArrayOfByte, long[] paramArrayOfLong)
/*      */     throws SQLException
/*      */   {
/*  874 */     if (this.tracer.isTracing()) {
/*  875 */       this.tracer.trace("Binding IN parameter double (SQLBindParameter), hStmt=" + paramLong + ", ipar=" + paramInt1 + ", SQLtype=" + paramInt2 + ", scale=" + paramInt3 + ", rgbValue=" + paramDouble);
/*      */     }
/*      */ 
/*  880 */     byte[] arrayOfByte = new byte[1];
/*  881 */     bindInParameterDouble(paramLong, paramInt1, OdbcDef.jdbcTypeToOdbc(paramInt2), paramInt3, paramDouble, paramArrayOfByte, arrayOfByte, paramArrayOfLong);
/*      */ 
/*  886 */     if (arrayOfByte[0] != 0)
/*      */     {
/*  889 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLBindInParameterFloat(long paramLong, int paramInt1, int paramInt2, int paramInt3, float paramFloat, byte[] paramArrayOfByte, long[] paramArrayOfLong)
/*      */     throws SQLException
/*      */   {
/*  911 */     if (this.tracer.isTracing()) {
/*  912 */       this.tracer.trace("Binding IN parameter float (SQLBindParameter), hStmt=" + paramLong + ", ipar=" + paramInt1 + ", SQLtype=" + paramInt2 + ", scale=" + paramInt3 + ", rgbValue=" + paramFloat);
/*      */     }
/*      */ 
/*  917 */     byte[] arrayOfByte = new byte[1];
/*      */ 
/*  919 */     bindInParameterFloat(paramLong, paramInt1, OdbcDef.jdbcTypeToOdbc(paramInt2), paramInt3, paramFloat, paramArrayOfByte, arrayOfByte, paramArrayOfLong);
/*      */ 
/*  923 */     if (arrayOfByte[0] != 0)
/*      */     {
/*  926 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLBindInParameterInteger(long paramLong, int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte, long[] paramArrayOfLong)
/*      */     throws SQLException
/*      */   {
/*  947 */     if (this.tracer.isTracing()) {
/*  948 */       this.tracer.trace("Binding IN parameter integer (SQLBindParameter), hStmt=" + paramLong + ", ipar=" + paramInt1 + "SQLtype=" + paramInt2 + ", rgbValue=" + paramInt3);
/*      */     }
/*      */ 
/*  952 */     byte[] arrayOfByte = new byte[1];
/*  953 */     bindInParameterInteger(paramLong, paramInt1, OdbcDef.jdbcTypeToOdbc(paramInt2), paramInt3, paramArrayOfByte, arrayOfByte, paramArrayOfLong);
/*      */ 
/*  957 */     if (arrayOfByte[0] != 0)
/*      */     {
/*  960 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLBindInParameterNull(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, long[] paramArrayOfLong)
/*      */     throws SQLException
/*      */   {
/*  982 */     if (this.tracer.isTracing()) {
/*  983 */       this.tracer.trace("Binding IN NULL parameter (SQLBindParameter), hStmt=" + paramLong + ", ipar=" + paramInt1 + ", SQLtype=" + paramInt2);
/*      */     }
/*      */ 
/*  987 */     byte[] arrayOfByte = new byte[1];
/*  988 */     bindInParameterNull(paramLong, paramInt1, OdbcDef.jdbcTypeToOdbc(paramInt2), paramInt3, paramInt4, paramArrayOfByte, arrayOfByte, paramArrayOfLong);
/*      */ 
/*  990 */     if (arrayOfByte[0] != 0)
/*      */     {
/*  993 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLBindInParameterString(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, long[] paramArrayOfLong)
/*      */     throws SQLException
/*      */   {
/* 1016 */     if (this.tracer.isTracing()) {
/* 1017 */       this.tracer.trace("Binding IN string parameter (SQLBindParameter), hStmt=" + paramLong + ", ipar=" + paramInt1 + ", SQLtype=" + paramInt2 + ", precision=" + paramInt3 + ", scale=" + paramInt4 + ", rgbValue=" + paramArrayOfByte1);
/*      */     }
/*      */ 
/* 1023 */     byte[] arrayOfByte = new byte[1];
/* 1024 */     bindInParameterString(paramLong, paramInt1, OdbcDef.jdbcTypeToOdbc(paramInt2), paramArrayOfByte1, paramInt3, paramInt4, paramArrayOfByte2, arrayOfByte, paramArrayOfLong);
/*      */ 
/* 1029 */     if (arrayOfByte[0] != 0)
/*      */     {
/* 1032 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLBindInParameterTime(long paramLong, int paramInt, Time paramTime, byte[] paramArrayOfByte, long[] paramArrayOfLong)
/*      */     throws SQLException
/*      */   {
/* 1052 */     if (this.tracer.isTracing()) {
/* 1053 */       this.tracer.trace("Binding IN parameter time (SQLBindParameter), hStmt=" + paramLong + ", ipar=" + paramInt + ", rgbValue=" + paramTime.toString());
/*      */     }
/*      */ 
/* 1057 */     byte[] arrayOfByte = new byte[1];
/*      */ 
/* 1059 */     Calendar localCalendar = Calendar.getInstance();
/* 1060 */     localCalendar.setTime(paramTime);
/*      */ 
/* 1062 */     bindInParameterTime(paramLong, paramInt, localCalendar.get(11), localCalendar.get(12), localCalendar.get(13), paramArrayOfByte, arrayOfByte, paramArrayOfLong);
/*      */ 
/* 1068 */     if (arrayOfByte[0] != 0)
/*      */     {
/* 1071 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLBindInParameterCalendarTime(long paramLong, int paramInt, Calendar paramCalendar, byte[] paramArrayOfByte, long[] paramArrayOfLong)
/*      */     throws SQLException
/*      */   {
/* 1091 */     if (this.tracer.isTracing()) {
/* 1092 */       this.tracer.trace("Binding IN parameter time (SQLBindParameter), hStmt=" + paramLong + ", ipar=" + paramInt + ", rgbValue=" + paramCalendar.toString());
/*      */     }
/*      */ 
/* 1096 */     byte[] arrayOfByte = new byte[1];
/*      */ 
/* 1099 */     bindInParameterTime(paramLong, paramInt, paramCalendar.get(11), paramCalendar.get(12), paramCalendar.get(13), paramArrayOfByte, arrayOfByte, paramArrayOfLong);
/*      */ 
/* 1105 */     if (arrayOfByte[0] != 0)
/*      */     {
/* 1108 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLBindInParameterTimestamp(long paramLong, int paramInt, Timestamp paramTimestamp, byte[] paramArrayOfByte, long[] paramArrayOfLong)
/*      */     throws SQLException
/*      */   {
/* 1129 */     if (this.tracer.isTracing()) {
/* 1130 */       this.tracer.trace("Binding IN parameter timestamp (SQLBindParameter), hStmt=" + paramLong + ", ipar=" + paramInt + ", rgbValue=" + paramTimestamp.toString());
/*      */     }
/*      */ 
/* 1134 */     byte[] arrayOfByte = new byte[1];
/*      */ 
/* 1136 */     Calendar localCalendar = Calendar.getInstance();
/* 1137 */     localCalendar.setTime(paramTimestamp);
/*      */ 
/* 1139 */     bindInParameterTimestamp(paramLong, paramInt, localCalendar.get(1), localCalendar.get(2) + 1, localCalendar.get(5), localCalendar.get(11), localCalendar.get(12), localCalendar.get(13), paramTimestamp.getNanos(), paramArrayOfByte, arrayOfByte, paramArrayOfLong);
/*      */ 
/* 1149 */     if (arrayOfByte[0] != 0)
/*      */     {
/* 1152 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLBindInParameterCalendarTimestamp(long paramLong, int paramInt, Calendar paramCalendar, byte[] paramArrayOfByte, long[] paramArrayOfLong)
/*      */     throws SQLException
/*      */   {
/* 1172 */     if (this.tracer.isTracing()) {
/* 1173 */       this.tracer.trace("Binding IN parameter timestamp (SQLBindParameter), hStmt=" + paramLong + ", ipar=" + paramInt + ", rgbValue=" + paramCalendar.toString());
/*      */     }
/*      */ 
/* 1177 */     byte[] arrayOfByte = new byte[1];
/*      */ 
/* 1180 */     int i = paramCalendar.get(14) * 1000000;
/*      */ 
/* 1182 */     bindInParameterTimestamp(paramLong, paramInt, paramCalendar.get(1), paramCalendar.get(2) + 1, paramCalendar.get(5), paramCalendar.get(11), paramCalendar.get(12), paramCalendar.get(13), i, paramArrayOfByte, arrayOfByte, paramArrayOfLong);
/*      */ 
/* 1192 */     if (arrayOfByte[0] != 0)
/*      */     {
/* 1195 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLBindInParameterBigint(long paramLong1, int paramInt1, int paramInt2, int paramInt3, long paramLong2, byte[] paramArrayOfByte, long[] paramArrayOfLong)
/*      */     throws SQLException
/*      */   {
/* 1217 */     if (this.tracer.isTracing()) {
/* 1218 */       this.tracer.trace("Binding IN parameter bigint (SQLBindParameter), hStmt=" + paramLong1 + ", ipar=" + paramInt1 + ", SQLtype=" + paramInt2 + ", scale=" + paramInt3 + ", rgbValue=" + paramLong2);
/*      */     }
/*      */ 
/* 1223 */     byte[] arrayOfByte = new byte[1];
/* 1224 */     bindInParameterBigint(paramLong1, paramInt1, OdbcDef.jdbcTypeToOdbc(paramInt2), paramInt3, paramLong2, paramArrayOfByte, arrayOfByte, paramArrayOfLong);
/*      */ 
/* 1228 */     if (arrayOfByte[0] != 0)
/*      */     {
/* 1231 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong1);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLBindOutParameterString(long paramLong, int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, long[] paramArrayOfLong)
/*      */     throws SQLException
/*      */   {
/* 1253 */     if (this.tracer.isTracing()) {
/* 1254 */       this.tracer.trace("Binding OUT string parameter (SQLBindParameter), hStmt=" + paramLong + ", ipar=" + paramInt1 + ", SQLtype=" + paramInt2 + ", prec=" + (paramArrayOfByte1.length - 1) + ", scale=" + paramInt3);
/*      */     }
/*      */ 
/* 1259 */     byte[] arrayOfByte = new byte[1];
/* 1260 */     bindOutParameterString(paramLong, paramInt1, OdbcDef.jdbcTypeToOdbc(paramInt2), paramInt3, paramArrayOfByte1, paramArrayOfByte2, arrayOfByte, paramArrayOfLong);
/*      */ 
/* 1264 */     if (arrayOfByte[0] != 0)
/*      */     {
/* 1267 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLBindInOutParameterDate(long paramLong, int paramInt1, int paramInt2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, long[] paramArrayOfLong)
/*      */     throws SQLException
/*      */   {
/* 1289 */     if (this.tracer.isTracing()) {
/* 1290 */       this.tracer.trace("Binding IN OUT date parameter (SQLBindParameter), hStmt=" + paramLong + ", ipar=" + paramInt1 + ", prec=" + (paramArrayOfByte1.length - 1) + ", scale=" + paramInt2);
/*      */     }
/*      */ 
/* 1295 */     byte[] arrayOfByte = new byte[1];
/*      */ 
/* 1297 */     bindInOutParameterDate(paramLong, paramInt1, paramInt2, paramArrayOfByte1, paramArrayOfByte2, arrayOfByte, paramArrayOfLong);
/*      */ 
/* 1300 */     if (arrayOfByte[0] != 0)
/*      */     {
/* 1303 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLBindInOutParameterTime(long paramLong, int paramInt1, int paramInt2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, long[] paramArrayOfLong)
/*      */     throws SQLException
/*      */   {
/* 1325 */     if (this.tracer.isTracing()) {
/* 1326 */       this.tracer.trace("Binding IN OUT time parameter (SQLBindParameter), hStmt=" + paramLong + ", ipar=" + paramInt1 + ", prec=" + (paramArrayOfByte1.length - 1) + ", scale=" + paramInt2);
/*      */     }
/*      */ 
/* 1331 */     byte[] arrayOfByte = new byte[1];
/*      */ 
/* 1333 */     bindInOutParameterTime(paramLong, paramInt1, paramInt2, paramArrayOfByte1, paramArrayOfByte2, arrayOfByte, paramArrayOfLong);
/*      */ 
/* 1336 */     if (arrayOfByte[0] != 0)
/*      */     {
/* 1339 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLBindInOutParameterTimestamp(long paramLong, int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, long[] paramArrayOfLong)
/*      */     throws SQLException
/*      */   {
/* 1361 */     if (this.tracer.isTracing()) {
/* 1362 */       this.tracer.trace("Binding IN OUT time parameter (SQLBindParameter), hStmt=" + paramLong + ", ipar=" + paramInt1 + ", scale=" + paramInt3 + "length = " + (paramArrayOfByte1.length - 1) + ", precision=" + paramInt2);
/*      */     }
/*      */ 
/* 1367 */     byte[] arrayOfByte = new byte[1];
/*      */ 
/* 1369 */     bindInOutParameterTimestamp(paramLong, paramInt1, paramInt2, paramInt3, paramArrayOfByte1, paramArrayOfByte2, arrayOfByte, paramArrayOfLong);
/*      */ 
/* 1372 */     if (arrayOfByte[0] != 0)
/*      */     {
/* 1375 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLBindInOutParameterString(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, long[] paramArrayOfLong)
/*      */     throws SQLException
/*      */   {
/* 1399 */     if (this.tracer.isTracing()) {
/* 1400 */       this.tracer.trace("Binding INOUT string parameter (SQLBindParameter), hStmt=" + paramLong + ", ipar=" + paramInt1 + ", SQLtype=" + paramInt2 + ", precision=" + paramInt3 + ", scale=" + paramInt4 + ", rgbValue=" + paramArrayOfByte1 + ", lenBuf=" + paramArrayOfByte2);
/*      */     }
/*      */ 
/* 1406 */     byte[] arrayOfByte = new byte[1];
/* 1407 */     bindInOutParameterString(paramLong, paramInt1, OdbcDef.jdbcTypeToOdbc(paramInt2), paramInt3, paramInt4, paramArrayOfByte1, paramArrayOfByte2, arrayOfByte, paramArrayOfLong);
/*      */ 
/* 1412 */     if (arrayOfByte[0] != 0)
/*      */     {
/* 1415 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLBindInOutParameterStr(long paramLong, int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, long[] paramArrayOfLong, int paramInt4)
/*      */     throws SQLException
/*      */   {
/* 1446 */     if (this.tracer.isTracing()) {
/* 1447 */       this.tracer.trace("Binding INOUT string parameter (SQLBindParameter), hStmt=" + paramLong + ", ipar=" + paramInt1 + ", SQLtype=" + paramInt2 + ", precision=" + paramInt3 + ", rgbValue=" + paramArrayOfByte1 + ", lenBuf=" + paramArrayOfByte2);
/*      */     }
/*      */ 
/* 1453 */     byte[] arrayOfByte = new byte[1];
/* 1454 */     bindInOutParameterStr(paramLong, paramInt1, OdbcDef.jdbcTypeToOdbc(paramInt2), paramInt3, paramArrayOfByte1, paramArrayOfByte2, arrayOfByte, paramArrayOfLong, paramInt4);
/*      */ 
/* 1460 */     if (arrayOfByte[0] != 0)
/*      */     {
/* 1463 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLBindInOutParameterBin(long paramLong, int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, long[] paramArrayOfLong, int paramInt4)
/*      */     throws SQLException
/*      */   {
/* 1495 */     if (this.tracer.isTracing()) {
/* 1496 */       this.tracer.trace("Binding INOUT binary parameter (SQLBindParameter), hStmt=" + paramLong + ", ipar=" + paramInt1 + ", SQLtype=" + paramInt2 + ", precision=" + paramInt3 + ", rgbValue=" + paramArrayOfByte1 + ", lenBuf=" + paramArrayOfByte2);
/*      */     }
/*      */ 
/* 1502 */     byte[] arrayOfByte = new byte[1];
/* 1503 */     bindInOutParameterBin(paramLong, paramInt1, OdbcDef.jdbcTypeToOdbc(paramInt2), paramInt3, paramArrayOfByte1, paramArrayOfByte2, arrayOfByte, paramArrayOfLong, paramInt4);
/*      */ 
/* 1508 */     if (arrayOfByte[0] != 0)
/*      */     {
/* 1511 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLBindInOutParameterBinary(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, long[] paramArrayOfLong)
/*      */     throws SQLException
/*      */   {
/* 1535 */     if (this.tracer.isTracing()) {
/* 1536 */       this.tracer.trace("Binding INOUT binary parameter (SQLBindParameter), hStmt=" + paramLong + ", ipar=" + paramInt1 + ", SQLtype=" + paramInt2 + ", precision=" + paramInt3 + ", scale=" + paramInt4 + ", rgbValue=" + paramArrayOfByte1 + ", lenBuf=" + paramArrayOfByte2);
/*      */     }
/*      */ 
/* 1542 */     byte[] arrayOfByte = new byte[1];
/* 1543 */     bindInOutParameterBinary(paramLong, paramInt1, OdbcDef.jdbcTypeToOdbc(paramInt2), paramInt3, paramInt4, paramArrayOfByte1, paramArrayOfByte2, arrayOfByte, paramArrayOfLong);
/*      */ 
/* 1548 */     if (arrayOfByte[0] != 0)
/*      */     {
/* 1551 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLBindInOutParameterFixed(long paramLong, int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, long[] paramArrayOfLong)
/*      */     throws SQLException
/*      */   {
/* 1580 */     if (this.tracer.isTracing()) {
/* 1581 */       this.tracer.trace("Binding IN OUT parameter for fixed types (SQLBindParameter), hStmt=" + paramLong + ", ipar=" + paramInt1 + "SQLtype=" + paramInt2 + ", maxLen=" + paramInt3);
/*      */     }
/*      */ 
/* 1585 */     byte[] arrayOfByte = new byte[1];
/* 1586 */     bindInOutParameterFixed(paramLong, paramInt1, OdbcDef.jdbcTypeToCType(paramInt2), OdbcDef.jdbcTypeToOdbc(paramInt2), paramInt3, paramArrayOfByte1, paramArrayOfByte2, arrayOfByte, paramArrayOfLong);
/*      */ 
/* 1590 */     if (arrayOfByte[0] != 0)
/*      */     {
/* 1593 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLBindInOutParameterTimeStamp(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, long[] paramArrayOfLong)
/*      */     throws SQLException
/*      */   {
/* 1618 */     if (this.tracer.isTracing()) {
/* 1619 */       this.tracer.trace("Binding INOUT string parameter (SQLBindParameter), hStmt=" + paramLong + ", ipar=" + paramInt1 + ", SQLtype=" + paramInt2 + ", precision=" + paramInt3 + ", scale=" + paramInt4 + ", rgbValue=" + paramArrayOfByte1 + ", lenBuf=" + paramArrayOfByte2);
/*      */     }
/*      */ 
/* 1625 */     byte[] arrayOfByte = new byte[1];
/* 1626 */     bindInOutParameterTimeStamp(paramLong, paramInt1, OdbcDef.jdbcTypeToOdbc(paramInt2), paramInt3, paramInt4, paramArrayOfByte1, paramArrayOfByte2, arrayOfByte, paramArrayOfLong);
/*      */ 
/* 1631 */     if (arrayOfByte[0] != 0)
/*      */     {
/* 1634 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLBindInOutParameter(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4, double paramDouble, byte[] paramArrayOfByte, long[] paramArrayOfLong)
/*      */     throws SQLException
/*      */   {
/* 1656 */     if (this.tracer.isTracing()) {
/* 1657 */       this.tracer.trace("Binding INOUT parameter (SQLBindParameter), hStmt=" + paramLong + ", ipar=" + paramInt1 + ", SQLtype=" + paramInt2 + ", precision=" + paramInt3 + ", scale=" + paramInt4 + ", rgbValue=" + paramDouble);
/*      */     }
/*      */ 
/* 1663 */     byte[] arrayOfByte = new byte[1];
/* 1664 */     bindInOutParameter(paramLong, paramInt1, OdbcDef.jdbcTypeToOdbc(paramInt2), paramInt3, paramInt4, paramDouble, paramArrayOfByte, arrayOfByte, paramArrayOfLong);
/*      */ 
/* 1669 */     if (arrayOfByte[0] != 0)
/*      */     {
/* 1672 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLBindInOutParameterNull(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, long[] paramArrayOfLong)
/*      */     throws SQLException
/*      */   {
/* 1694 */     if (this.tracer.isTracing()) {
/* 1695 */       this.tracer.trace("Binding IN OUT NULL parameter (SQLBindParameter), hStmt=" + paramLong + ", ipar=" + paramInt1 + ", SQLtype=" + paramInt2);
/*      */     }
/*      */ 
/* 1699 */     byte[] arrayOfByte = new byte[1];
/* 1700 */     bindInOutParameterNull(paramLong, paramInt1, OdbcDef.jdbcTypeToOdbc(paramInt2), paramInt3, paramInt4, paramArrayOfByte, arrayOfByte, paramArrayOfLong);
/*      */ 
/* 1702 */     if (arrayOfByte[0] != 0)
/*      */     {
/* 1705 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLBindInParameterStringArray(long paramLong, int paramInt1, int paramInt2, Object[] paramArrayOfObject, int paramInt3, int paramInt4, int[] paramArrayOfInt)
/*      */     throws SQLException
/*      */   {
/* 1726 */     int i = paramArrayOfObject.length;
/*      */ 
/* 1728 */     Object[] arrayOfObject = new Object[i];
/*      */ 
/* 1730 */     if ((paramInt2 == 2) || (paramInt2 == 3))
/*      */     {
/* 1733 */       for (int j = 0; j < i; j++)
/*      */       {
/* 1735 */         if (paramArrayOfObject[j] != null)
/*      */         {
/* 1738 */           localObject = (BigDecimal)paramArrayOfObject[j];
/*      */ 
/* 1740 */           String str1 = ((BigDecimal)localObject).toString();
/*      */ 
/* 1745 */           int k = str1.indexOf('.');
/*      */ 
/* 1747 */           if (k != -1)
/*      */           {
/* 1749 */             String str2 = str1.substring(k + 1, str1.length());
/*      */ 
/* 1751 */             int m = str2.length();
/*      */ 
/* 1753 */             if (m < paramInt4)
/*      */             {
/* 1755 */               for (int n = 0; n < paramInt4 - m; n++)
/*      */               {
/* 1757 */                 str1 = str1 + "0";
/*      */               }
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/* 1763 */           arrayOfObject[j] = str1;
/*      */         }
/*      */       }
/*      */     }
/*      */     else {
/* 1768 */       arrayOfObject = paramArrayOfObject;
/*      */     }
/*      */ 
/* 1771 */     if (this.tracer.isTracing()) {
/* 1772 */       this.tracer.trace("Binding IN parameter timestamp (SQLBindParameter), hStmt=" + paramLong + ", ipar=" + paramInt1);
/*      */     }
/*      */ 
/* 1779 */     byte[] arrayOfByte = new byte[1];
/* 1780 */     Object localObject = new byte[(paramInt3 + 1) * i];
/*      */ 
/* 1782 */     bindInParameterStringArray(paramLong, paramInt1, OdbcDef.jdbcTypeToOdbc(paramInt2), arrayOfObject, (byte[])localObject, paramInt3, paramInt4, paramArrayOfInt, arrayOfByte);
/*      */ 
/* 1787 */     if (arrayOfByte[0] != 0)
/*      */     {
/* 1790 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLBindInParameterIntegerArray(long paramLong, int paramInt1, int paramInt2, Object[] paramArrayOfObject, int[] paramArrayOfInt)
/*      */     throws SQLException
/*      */   {
/* 1810 */     int[] arrayOfInt = new int[paramArrayOfObject.length];
/*      */ 
/* 1813 */     for (int i = 0; i < paramArrayOfObject.length; i++)
/*      */     {
/* 1815 */       if (paramArrayOfObject[i] != null) {
/* 1816 */         arrayOfInt[i] = ((Integer)paramArrayOfObject[i]).intValue();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1822 */     if (this.tracer.isTracing()) {
/* 1823 */       this.tracer.trace("Binding IN parameter Integer Array (SQLBindParameter), hStmt=" + paramLong + ", ipar=" + paramInt1);
/*      */     }
/*      */ 
/* 1827 */     byte[] arrayOfByte = new byte[1];
/*      */ 
/* 1829 */     bindInParameterIntegerArray(paramLong, paramInt1, OdbcDef.jdbcTypeToOdbc(paramInt2), arrayOfInt, paramArrayOfInt, arrayOfByte);
/*      */ 
/* 1832 */     if (arrayOfByte[0] != 0)
/*      */     {
/* 1835 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLBindInParameterFloatArray(long paramLong, int paramInt1, int paramInt2, Object[] paramArrayOfObject, int[] paramArrayOfInt)
/*      */     throws SQLException
/*      */   {
/* 1855 */     float[] arrayOfFloat = new float[paramArrayOfObject.length];
/*      */ 
/* 1857 */     for (int i = 0; i < paramArrayOfObject.length; i++)
/*      */     {
/* 1859 */       if (paramArrayOfObject[i] != null) {
/* 1860 */         arrayOfFloat[i] = ((Float)paramArrayOfObject[i]).floatValue();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1865 */     if (this.tracer.isTracing()) {
/* 1866 */       this.tracer.trace("Binding IN parameter timestamp (SQLBindParameter), hStmt=" + paramLong + ", ipar=" + paramInt1);
/*      */     }
/*      */ 
/* 1870 */     byte[] arrayOfByte = new byte[1];
/*      */ 
/* 1872 */     bindInParameterFloatArray(paramLong, paramInt1, OdbcDef.jdbcTypeToOdbc(paramInt2), 0, arrayOfFloat, paramArrayOfInt, arrayOfByte);
/*      */ 
/* 1875 */     if (arrayOfByte[0] != 0)
/*      */     {
/* 1878 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLBindInParameterDoubleArray(long paramLong, int paramInt1, int paramInt2, Object[] paramArrayOfObject, int[] paramArrayOfInt)
/*      */     throws SQLException
/*      */   {
/* 1898 */     double[] arrayOfDouble = new double[paramArrayOfObject.length];
/*      */ 
/* 1900 */     for (int i = 0; i < paramArrayOfObject.length; i++)
/*      */     {
/* 1902 */       if (paramArrayOfObject[i] != null) {
/* 1903 */         arrayOfDouble[i] = ((Double)paramArrayOfObject[i]).doubleValue();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1908 */     if (this.tracer.isTracing()) {
/* 1909 */       this.tracer.trace("Binding IN parameter timestamp (SQLBindParameter), hStmt=" + paramLong + ", ipar=" + paramInt1);
/*      */     }
/*      */ 
/* 1913 */     byte[] arrayOfByte = new byte[1];
/*      */ 
/* 1915 */     bindInParameterDoubleArray(paramLong, paramInt1, OdbcDef.jdbcTypeToOdbc(paramInt2), 0, arrayOfDouble, paramArrayOfInt, arrayOfByte);
/*      */ 
/* 1918 */     if (arrayOfByte[0] != 0)
/*      */     {
/* 1921 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLBindInParameterDateArray(long paramLong, int paramInt, Object[] paramArrayOfObject, int[] paramArrayOfInt)
/*      */     throws SQLException
/*      */   {
/* 1940 */     if (this.tracer.isTracing()) {
/* 1941 */       this.tracer.trace("Binding IN parameter timestamp (SQLBindParameterDateArray), hStmt=" + paramLong + ", ipar=" + paramInt);
/*      */     }
/*      */ 
/* 1945 */     int i = paramArrayOfObject.length;
/*      */ 
/* 1947 */     int[] arrayOfInt1 = new int[i];
/* 1948 */     int[] arrayOfInt2 = new int[i];
/* 1949 */     int[] arrayOfInt3 = new int[i];
/*      */ 
/* 1954 */     byte[] arrayOfByte1 = new byte[1];
/* 1955 */     byte[] arrayOfByte2 = new byte[11 * i];
/*      */     Calendar localCalendar;
/* 1959 */     if ((java.sql.Date)paramArrayOfObject[0] != null)
/*      */     {
/* 1962 */       localCalendar = Calendar.getInstance();
/*      */ 
/* 1964 */       java.sql.Date localDate = null;
/*      */ 
/* 1966 */       for (int k = 0; k < i; k++)
/*      */       {
/* 1968 */         if (paramArrayOfObject[k] != null)
/*      */         {
/* 1970 */           localDate = (java.sql.Date)paramArrayOfObject[k];
/*      */ 
/* 1972 */           localCalendar.setTime(localDate);
/*      */ 
/* 1974 */           arrayOfInt1[k] = localCalendar.get(1);
/* 1975 */           arrayOfInt2[k] = (localCalendar.get(2) + 1);
/* 1976 */           arrayOfInt3[k] = localCalendar.get(5);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 1986 */       for (int j = 0; j < i; j++)
/*      */       {
/* 1988 */         if (paramArrayOfObject[j] != null)
/*      */         {
/* 1990 */           localCalendar = (Calendar)paramArrayOfObject[j];
/*      */ 
/* 1992 */           arrayOfInt1[j] = localCalendar.get(1);
/* 1993 */           arrayOfInt2[j] = (localCalendar.get(2) + 1);
/* 1994 */           arrayOfInt3[j] = localCalendar.get(5);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2002 */     bindInParameterDateArray(paramLong, paramInt, arrayOfInt1, arrayOfInt2, arrayOfInt3, arrayOfByte2, arrayOfByte1, paramArrayOfInt);
/*      */ 
/* 2008 */     if (arrayOfByte1[0] != 0)
/*      */     {
/* 2011 */       standardError((short)arrayOfByte1[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLBindInParameterTimeArray(long paramLong, int paramInt, Object[] paramArrayOfObject, int[] paramArrayOfInt)
/*      */     throws SQLException
/*      */   {
/* 2029 */     if (this.tracer.isTracing()) {
/* 2030 */       this.tracer.trace("Binding IN parameter timestamp (SQLBindParameterTimeArray), hStmt=" + paramLong + ", ipar=" + paramInt);
/*      */     }
/*      */ 
/* 2034 */     int i = paramArrayOfObject.length;
/*      */ 
/* 2036 */     int[] arrayOfInt1 = new int[i];
/* 2037 */     int[] arrayOfInt2 = new int[i];
/* 2038 */     int[] arrayOfInt3 = new int[i];
/*      */ 
/* 2043 */     byte[] arrayOfByte1 = new byte[1];
/* 2044 */     byte[] arrayOfByte2 = new byte[9 * i];
/*      */     Calendar localCalendar;
/* 2048 */     if ((Time)paramArrayOfObject[0] != null)
/*      */     {
/* 2050 */       localCalendar = Calendar.getInstance();
/*      */ 
/* 2052 */       Time localTime = null;
/*      */ 
/* 2054 */       for (int k = 0; k < i; k++)
/*      */       {
/* 2056 */         if (paramArrayOfObject[k] != null)
/*      */         {
/* 2058 */           localTime = (Time)paramArrayOfObject[k];
/*      */ 
/* 2060 */           localCalendar.setTime(localTime);
/*      */ 
/* 2062 */           arrayOfInt1[k] = localCalendar.get(11);
/* 2063 */           arrayOfInt2[k] = localCalendar.get(12);
/* 2064 */           arrayOfInt3[k] = localCalendar.get(13);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 2073 */       for (int j = 0; j < i; j++)
/*      */       {
/* 2075 */         if (paramArrayOfObject[j] != null)
/*      */         {
/* 2077 */           localCalendar = (Calendar)paramArrayOfObject[j];
/*      */ 
/* 2079 */           arrayOfInt1[j] = localCalendar.get(11);
/* 2080 */           arrayOfInt2[j] = localCalendar.get(12);
/* 2081 */           arrayOfInt3[j] = localCalendar.get(13);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2088 */     bindInParameterTimeArray(paramLong, paramInt, arrayOfInt1, arrayOfInt2, arrayOfInt3, arrayOfByte2, arrayOfByte1, paramArrayOfInt);
/*      */ 
/* 2094 */     if (arrayOfByte1[0] != 0)
/*      */     {
/* 2097 */       standardError((short)arrayOfByte1[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLBindInParameterTimestampArray(long paramLong, int paramInt, Object[] paramArrayOfObject, int[] paramArrayOfInt)
/*      */     throws SQLException
/*      */   {
/* 2115 */     if (this.tracer.isTracing()) {
/* 2116 */       this.tracer.trace("Binding IN parameter timestamp (SQLBindParameterTimestampArray), hStmt=" + paramLong + ", ipar=" + paramInt);
/*      */     }
/*      */ 
/* 2120 */     int i = paramArrayOfObject.length;
/*      */ 
/* 2122 */     int[] arrayOfInt1 = new int[i];
/* 2123 */     int[] arrayOfInt2 = new int[i];
/* 2124 */     int[] arrayOfInt3 = new int[i];
/* 2125 */     int[] arrayOfInt4 = new int[i];
/* 2126 */     int[] arrayOfInt5 = new int[i];
/* 2127 */     int[] arrayOfInt6 = new int[i];
/* 2128 */     int[] arrayOfInt7 = new int[i];
/*      */ 
/* 2133 */     byte[] arrayOfByte1 = new byte[1];
/* 2134 */     byte[] arrayOfByte2 = new byte[30 * i];
/*      */     Calendar localCalendar;
/* 2138 */     if ((Timestamp)paramArrayOfObject[0] != null)
/*      */     {
/* 2141 */       localCalendar = Calendar.getInstance();
/*      */ 
/* 2143 */       Timestamp localTimestamp = null;
/*      */ 
/* 2145 */       for (int k = 0; k < i; k++)
/*      */       {
/* 2147 */         if (paramArrayOfObject[k] != null)
/*      */         {
/* 2149 */           localTimestamp = (Timestamp)paramArrayOfObject[k];
/*      */ 
/* 2151 */           localCalendar.setTime(localTimestamp);
/*      */ 
/* 2153 */           arrayOfInt1[k] = localCalendar.get(1);
/* 2154 */           arrayOfInt2[k] = (localCalendar.get(2) + 1);
/* 2155 */           arrayOfInt3[k] = localCalendar.get(5);
/* 2156 */           arrayOfInt4[k] = localCalendar.get(11);
/* 2157 */           arrayOfInt5[k] = localCalendar.get(12);
/* 2158 */           arrayOfInt6[k] = localCalendar.get(13);
/* 2159 */           arrayOfInt7[k] = localTimestamp.getNanos();
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 2168 */       for (int j = 0; j < i; j++)
/*      */       {
/* 2170 */         if (paramArrayOfObject[j] != null)
/*      */         {
/* 2172 */           localCalendar = (Calendar)paramArrayOfObject[j];
/*      */ 
/* 2174 */           arrayOfInt1[j] = localCalendar.get(1);
/* 2175 */           arrayOfInt2[j] = (localCalendar.get(2) + 1);
/* 2176 */           arrayOfInt3[j] = localCalendar.get(5);
/* 2177 */           arrayOfInt4[j] = localCalendar.get(11);
/* 2178 */           arrayOfInt5[j] = localCalendar.get(12);
/* 2179 */           arrayOfInt6[j] = localCalendar.get(13);
/* 2180 */           arrayOfInt7[j] = localCalendar.get(14);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2188 */     bindInParameterTimestampArray(paramLong, paramInt, arrayOfInt1, arrayOfInt2, arrayOfInt3, arrayOfInt4, arrayOfInt5, arrayOfInt6, arrayOfInt7, arrayOfByte2, arrayOfByte1, paramArrayOfInt);
/*      */ 
/* 2198 */     if (arrayOfByte1[0] != 0)
/*      */     {
/* 2201 */       standardError((short)arrayOfByte1[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLBindInParameterBinaryArray(long paramLong, int paramInt1, int paramInt2, Object[] paramArrayOfObject, int paramInt3, int[] paramArrayOfInt)
/*      */     throws SQLException
/*      */   {
/* 2223 */     int i = paramArrayOfObject.length;
/*      */ 
/* 2229 */     int j = 8000;
/*      */ 
/* 2231 */     if (this.tracer.isTracing()) {
/* 2232 */       this.tracer.trace("Binding IN binary parameter (SQLBindParameterBinaryArray), hStmt=" + paramLong + ", ipar=" + paramInt1 + ", SQLtype=" + paramInt2);
/*      */     }
/*      */ 
/* 2237 */     byte[] arrayOfByte1 = new byte[1];
/* 2238 */     byte[] arrayOfByte2 = new byte[paramInt3 * i];
/*      */ 
/* 2240 */     bindInParameterBinaryArray(paramLong, paramInt1, OdbcDef.jdbcTypeToOdbc(paramInt2), paramArrayOfObject, paramInt3, arrayOfByte2, paramArrayOfInt, arrayOfByte1);
/*      */ 
/* 2244 */     if (arrayOfByte1[0] != 0)
/*      */     {
/* 2247 */       standardError((short)arrayOfByte1[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLBindInParameterAtExecArray(long paramLong, int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt)
/*      */     throws SQLException
/*      */   {
/* 2269 */     if (this.tracer.isTracing()) {
/* 2270 */       this.tracer.trace("Binding DATA_AT_EXEC Array parameter (SQLBindParameter), hStmt=" + paramLong + ", ipar=" + paramInt1 + ", SQLtype=" + paramInt2 + ", len=" + paramInt3);
/*      */     }
/*      */ 
/* 2274 */     byte[] arrayOfByte1 = new byte[1];
/* 2275 */     byte[] arrayOfByte2 = new byte[paramArrayOfInt.length];
/*      */ 
/* 2277 */     bindInParameterAtExecArray(paramLong, paramInt1, paramInt2, paramInt3, arrayOfByte2, paramArrayOfInt, arrayOfByte1);
/*      */ 
/* 2279 */     if (arrayOfByte1[0] != 0)
/*      */     {
/* 2282 */       standardError((short)arrayOfByte1[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLBindOutParameterNull(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, long[] paramArrayOfLong)
/*      */     throws SQLException
/*      */   {
/* 2306 */     if (this.tracer.isTracing()) {
/* 2307 */       this.tracer.trace("Binding OUT NULL parameter (SQLBindParameter), hStmt=" + paramLong + ", ipar=" + paramInt1 + ", SQLtype=" + paramInt2);
/*      */     }
/*      */ 
/* 2311 */     byte[] arrayOfByte = new byte[1];
/* 2312 */     bindOutParameterNull(paramLong, paramInt1, OdbcDef.jdbcTypeToOdbc(paramInt2), paramInt3, paramInt4, paramArrayOfByte, arrayOfByte, paramArrayOfLong);
/*      */ 
/* 2315 */     if (arrayOfByte[0] != 0)
/*      */     {
/* 2318 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLBindOutParameterFixed(long paramLong, int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, long[] paramArrayOfLong)
/*      */     throws SQLException
/*      */   {
/* 2341 */     if (this.tracer.isTracing()) {
/* 2342 */       this.tracer.trace("Binding OUT string parameter (SQLBindParameter), hStmt=" + paramLong + ", ipar=" + paramInt1 + ", SQLtype=" + paramInt2 + ", maxLen=" + paramInt3);
/*      */     }
/*      */ 
/* 2347 */     byte[] arrayOfByte = new byte[1];
/*      */ 
/* 2349 */     bindOutParameterFixed(paramLong, paramInt1, OdbcDef.jdbcTypeToCType(paramInt2), OdbcDef.jdbcTypeToOdbc(paramInt2), paramInt3, paramArrayOfByte1, paramArrayOfByte2, arrayOfByte, paramArrayOfLong);
/*      */ 
/* 2352 */     if (arrayOfByte[0] != 0)
/*      */     {
/* 2355 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLBindOutParameterBinary(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, long[] paramArrayOfLong)
/*      */     throws SQLException
/*      */   {
/* 2379 */     if (this.tracer.isTracing()) {
/* 2380 */       this.tracer.trace("Binding INOUT binary parameter (SQLBindParameter), hStmt=" + paramLong + ", ipar=" + paramInt1 + ", SQLtype=" + paramInt2 + ", precision=" + paramInt3 + ", scale=" + paramInt4 + ", rgbValue=" + paramArrayOfByte1 + ", lenBuf=" + paramArrayOfByte2);
/*      */     }
/*      */ 
/* 2386 */     byte[] arrayOfByte = new byte[1];
/* 2387 */     bindOutParameterBinary(paramLong, paramInt1, OdbcDef.jdbcTypeToOdbc(paramInt2), paramInt3, paramInt4, paramArrayOfByte1, paramArrayOfByte2, arrayOfByte, paramArrayOfLong);
/*      */ 
/* 2392 */     if (arrayOfByte[0] != 0)
/*      */     {
/* 2394 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLBindOutParameterDate(long paramLong, int paramInt1, int paramInt2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, long[] paramArrayOfLong)
/*      */     throws SQLException
/*      */   {
/* 2417 */     if (this.tracer.isTracing()) {
/* 2418 */       this.tracer.trace("Binding IN OUT date parameter (SQLBindParameter), hStmt=" + paramLong + ", ipar=" + paramInt1 + ", prec=" + (paramArrayOfByte1.length - 1) + ", scale=" + paramInt2);
/*      */     }
/*      */ 
/* 2423 */     byte[] arrayOfByte = new byte[1];
/*      */ 
/* 2425 */     bindOutParameterDate(paramLong, paramInt1, paramInt2, paramArrayOfByte1, paramArrayOfByte2, arrayOfByte, paramArrayOfLong);
/*      */ 
/* 2428 */     if (arrayOfByte[0] != 0)
/*      */     {
/* 2431 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLBindOutParameterTime(long paramLong, int paramInt1, int paramInt2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, long[] paramArrayOfLong)
/*      */     throws SQLException
/*      */   {
/* 2454 */     if (this.tracer.isTracing()) {
/* 2455 */       this.tracer.trace("Binding IN OUT time parameter (SQLBindParameter), hStmt=" + paramLong + ", ipar=" + paramInt1 + ", prec=" + (paramArrayOfByte1.length - 1) + ", scale=" + paramInt2);
/*      */     }
/*      */ 
/* 2460 */     byte[] arrayOfByte = new byte[1];
/*      */ 
/* 2462 */     bindOutParameterTime(paramLong, paramInt1, paramInt2, paramArrayOfByte1, paramArrayOfByte2, arrayOfByte, paramArrayOfLong);
/*      */ 
/* 2465 */     if (arrayOfByte[0] != 0)
/*      */     {
/* 2468 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLBindOutParameterTimestamp(long paramLong, int paramInt1, int paramInt2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, long[] paramArrayOfLong)
/*      */     throws SQLException
/*      */   {
/* 2490 */     if (this.tracer.isTracing()) {
/* 2491 */       this.tracer.trace("Binding OUT time parameter (SQLBindParameter), hStmt=" + paramLong + ", ipar=" + paramInt1 + ", prec=" + (paramArrayOfByte1.length - 1) + ", precision=" + paramInt2);
/*      */     }
/*      */ 
/* 2496 */     byte[] arrayOfByte = new byte[1];
/*      */ 
/* 2498 */     bindOutParameterTimestamp(paramLong, paramInt1, paramInt2, paramArrayOfByte1, paramArrayOfByte2, arrayOfByte, paramArrayOfLong);
/*      */ 
/* 2501 */     if (arrayOfByte[0] != 0)
/*      */     {
/* 2504 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public String SQLBrowseConnect(long paramLong, String paramString)
/*      */     throws SQLException, SQLWarning
/*      */   {
/* 2526 */     String str = null;
/*      */ 
/* 2528 */     if (this.tracer.isTracing()) {
/* 2529 */       this.tracer.trace("Connecting (SQLBrowseConnect), hDbc=" + paramLong + ", szConnStrIn=" + paramString);
/*      */     }
/*      */ 
/* 2532 */     byte[] arrayOfByte1 = new byte[1];
/* 2533 */     byte[] arrayOfByte2 = new byte[2000];
/*      */ 
/* 2535 */     byte[] arrayOfByte3 = null;
/* 2536 */     char[] arrayOfChar = null;
/* 2537 */     if (paramString != null)
/* 2538 */       arrayOfChar = paramString.toCharArray();
/*      */     try {
/* 2540 */       if (paramString != null)
/* 2541 */         arrayOfByte3 = CharsToBytes(this.charSet, arrayOfChar);
/*      */     } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/*      */     }
/* 2544 */     browseConnect(paramLong, arrayOfByte3, arrayOfByte2, arrayOfByte1);
/*      */ 
/* 2548 */     if (arrayOfByte1[0] == 99) {
/* 2549 */       str = new String(arrayOfByte2);
/* 2550 */       str = str.trim();
/* 2551 */       arrayOfByte1[0] = 0;
/*      */     }
/*      */ 
/* 2554 */     if (arrayOfByte1[0] != 0)
/*      */     {
/* 2558 */       standardError((short)arrayOfByte1[0], 0L, paramLong, 0L);
/*      */     }
/*      */ 
/* 2562 */     if (this.tracer.isTracing()) {
/* 2563 */       this.tracer.trace("Attributes=" + str);
/*      */     }
/*      */ 
/* 2566 */     return str;
/*      */   }
/*      */ 
/*      */   public void SQLCancel(long paramLong)
/*      */     throws SQLException, SQLWarning
/*      */   {
/* 2580 */     if (this.tracer.isTracing()) {
/* 2581 */       this.tracer.trace("Cancelling (SQLCancel), hStmt=" + paramLong);
/*      */     }
/*      */ 
/* 2584 */     byte[] arrayOfByte = new byte[1];
/* 2585 */     cancel(paramLong, arrayOfByte);
/*      */ 
/* 2587 */     if (arrayOfByte[0] != 0)
/*      */     {
/* 2591 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public int SQLColAttributes(long paramLong, int paramInt1, int paramInt2)
/*      */     throws SQLException, JdbcOdbcSQLWarning
/*      */   {
/* 2606 */     int i = 0;
/*      */ 
/* 2609 */     if (this.tracer.isTracing()) {
/* 2610 */       this.tracer.trace("Column attributes (SQLColAttributes), hStmt=" + paramLong + ", icol=" + paramInt1 + ", type=" + paramInt2);
/*      */     }
/*      */ 
/* 2614 */     byte[] arrayOfByte = new byte[1];
/* 2615 */     i = colAttributes(paramLong, paramInt1, paramInt2, arrayOfByte);
/*      */ 
/* 2617 */     if (arrayOfByte[0] != 0) {
/*      */       try
/*      */       {
/* 2620 */         standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */       }
/*      */       catch (JdbcOdbcSQLWarning localJdbcOdbcSQLWarning)
/*      */       {
/* 2625 */         if (this.tracer.isTracing()) {
/* 2626 */           this.tracer.trace("value (int)=" + i);
/*      */         }
/*      */ 
/* 2632 */         localJdbcOdbcSQLWarning.value = BigDecimal.valueOf(i);
/*      */ 
/* 2636 */         throw localJdbcOdbcSQLWarning;
/*      */       }
/*      */ 
/*      */     }
/* 2641 */     else if (this.tracer.isTracing()) {
/* 2642 */       this.tracer.trace("value (int)=" + i);
/*      */     }
/*      */ 
/* 2646 */     return i;
/*      */   }
/*      */ 
/*      */   public String SQLColAttributesString(long paramLong, int paramInt1, int paramInt2)
/*      */     throws SQLException, JdbcOdbcSQLWarning
/*      */   {
/* 2662 */     if (this.tracer.isTracing()) {
/* 2663 */       this.tracer.trace("Column attributes (SQLColAttributes), hStmt=" + paramLong + ", icol=" + paramInt1 + ", type=" + paramInt2);
/*      */     }
/*      */ 
/* 2667 */     byte[] arrayOfByte2 = new byte[1];
/* 2668 */     byte[] arrayOfByte1 = new byte[300];
/* 2669 */     colAttributesString(paramLong, paramInt1, paramInt2, arrayOfByte1, arrayOfByte2);
/*      */ 
/* 2671 */     if (arrayOfByte2[0] != 0) {
/*      */       try
/*      */       {
/* 2674 */         standardError((short)arrayOfByte2[0], 0L, 0L, paramLong);
/*      */       }
/*      */       catch (JdbcOdbcSQLWarning localJdbcOdbcSQLWarning)
/*      */       {
/* 2683 */         String str2 = new String();
/*      */         try {
/* 2685 */           str2 = BytesToChars(this.charSet, arrayOfByte1);
/*      */         } catch (UnsupportedEncodingException localUnsupportedEncodingException2) {
/* 2687 */           System.out.println(localUnsupportedEncodingException2);
/*      */         }
/*      */ 
/* 2690 */         if (this.tracer.isTracing()) {
/* 2691 */           this.tracer.trace("value (String)=" + str2.trim());
/*      */         }
/*      */ 
/* 2694 */         localJdbcOdbcSQLWarning.value = str2.trim();
/*      */ 
/* 2698 */         throw localJdbcOdbcSQLWarning;
/*      */       }
/*      */     }
/*      */ 
/* 2702 */     String str1 = new String();
/*      */     try {
/* 2704 */       str1 = BytesToChars(this.charSet, arrayOfByte1);
/*      */     } catch (UnsupportedEncodingException localUnsupportedEncodingException1) {
/* 2706 */       System.out.println(localUnsupportedEncodingException1);
/*      */     }
/*      */ 
/* 2709 */     if (this.tracer.isTracing()) {
/* 2710 */       this.tracer.trace("value (String)=" + str1.trim());
/*      */     }
/* 2712 */     return str1.trim();
/*      */   }
/*      */ 
/*      */   public void SQLColumns(long paramLong, String paramString1, String paramString2, String paramString3, String paramString4)
/*      */     throws SQLException, SQLWarning
/*      */   {
/* 2729 */     if (this.tracer.isTracing()) {
/* 2730 */       this.tracer.trace("(SQLColumns), hStmt=" + paramLong + ", catalog=" + paramString1 + ", schema=" + paramString2 + ", table=" + paramString3 + ", column=" + paramString4);
/*      */     }
/*      */ 
/* 2735 */     byte[] arrayOfByte1 = new byte[1];
/*      */ 
/* 2737 */     byte[] arrayOfByte2 = null;
/* 2738 */     byte[] arrayOfByte3 = null;
/* 2739 */     byte[] arrayOfByte4 = null;
/* 2740 */     byte[] arrayOfByte5 = null;
/* 2741 */     char[] arrayOfChar1 = null;
/* 2742 */     char[] arrayOfChar2 = null;
/* 2743 */     char[] arrayOfChar3 = null;
/* 2744 */     char[] arrayOfChar4 = null;
/* 2745 */     if (paramString1 != null)
/* 2746 */       arrayOfChar1 = paramString1.toCharArray();
/* 2747 */     if (paramString2 != null)
/* 2748 */       arrayOfChar2 = paramString2.toCharArray();
/* 2749 */     if (paramString3 != null)
/* 2750 */       arrayOfChar3 = paramString3.toCharArray();
/* 2751 */     if (paramString4 != null)
/* 2752 */       arrayOfChar4 = paramString4.toCharArray();
/*      */     try {
/* 2754 */       if (paramString1 != null)
/* 2755 */         arrayOfByte2 = CharsToBytes(this.charSet, arrayOfChar1);
/* 2756 */       if (paramString2 != null)
/* 2757 */         arrayOfByte3 = CharsToBytes(this.charSet, arrayOfChar2);
/* 2758 */       if (paramString3 != null)
/* 2759 */         arrayOfByte4 = CharsToBytes(this.charSet, arrayOfChar3);
/* 2760 */       if (paramString4 != null)
/* 2761 */         arrayOfByte5 = CharsToBytes(this.charSet, arrayOfChar4);
/*      */     }
/*      */     catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/*      */     }
/* 2765 */     columns(paramLong, arrayOfByte2, paramString1 == null, arrayOfByte3, paramString2 == null, arrayOfByte4, paramString3 == null, arrayOfByte5, paramString4 == null, arrayOfByte1);
/*      */ 
/* 2772 */     if (arrayOfByte1[0] != 0)
/*      */     {
/* 2776 */       standardError((short)arrayOfByte1[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLColumnPrivileges(long paramLong, String paramString1, String paramString2, String paramString3, String paramString4)
/*      */     throws SQLException, SQLWarning
/*      */   {
/* 2795 */     if (this.tracer.isTracing()) {
/* 2796 */       this.tracer.trace("(SQLColumnPrivileges), hStmt=" + paramLong + ", catalog=" + paramString1 + ", schema=" + paramString2 + ", table=" + paramString3 + ", column=" + paramString4);
/*      */     }
/*      */ 
/* 2801 */     byte[] arrayOfByte1 = new byte[1];
/*      */ 
/* 2803 */     byte[] arrayOfByte2 = null;
/* 2804 */     byte[] arrayOfByte3 = null;
/* 2805 */     byte[] arrayOfByte4 = null;
/* 2806 */     byte[] arrayOfByte5 = null;
/* 2807 */     char[] arrayOfChar1 = null;
/* 2808 */     char[] arrayOfChar2 = null;
/* 2809 */     char[] arrayOfChar3 = null;
/* 2810 */     char[] arrayOfChar4 = null;
/* 2811 */     if (paramString1 != null)
/* 2812 */       arrayOfChar1 = paramString1.toCharArray();
/* 2813 */     if (paramString2 != null)
/* 2814 */       arrayOfChar2 = paramString2.toCharArray();
/* 2815 */     if (paramString3 != null)
/* 2816 */       arrayOfChar3 = paramString3.toCharArray();
/* 2817 */     if (paramString4 != null)
/* 2818 */       arrayOfChar4 = paramString4.toCharArray();
/*      */     try {
/* 2820 */       if (paramString1 != null)
/* 2821 */         arrayOfByte2 = CharsToBytes(this.charSet, arrayOfChar1);
/* 2822 */       if (paramString2 != null)
/* 2823 */         arrayOfByte3 = CharsToBytes(this.charSet, arrayOfChar2);
/* 2824 */       if (paramString3 != null)
/* 2825 */         arrayOfByte4 = CharsToBytes(this.charSet, arrayOfChar3);
/* 2826 */       if (paramString4 != null)
/* 2827 */         arrayOfByte5 = CharsToBytes(this.charSet, arrayOfChar4);
/*      */     } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/*      */     }
/* 2830 */     columnPrivileges(paramLong, arrayOfByte2, paramString1 == null, arrayOfByte3, paramString2 == null, arrayOfByte4, paramString3 == null, arrayOfByte5, paramString4 == null, arrayOfByte1);
/*      */ 
/* 2837 */     if (arrayOfByte1[0] != 0)
/*      */     {
/* 2841 */       standardError((short)arrayOfByte1[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean SQLDescribeParamNullable(long paramLong, int paramInt)
/*      */     throws SQLException, JdbcOdbcSQLWarning
/*      */   {
/* 2857 */     boolean bool = false;
/*      */ 
/* 2859 */     if (this.tracer.isTracing()) {
/* 2860 */       this.tracer.trace("Parameter nullable (SQLDescribeParam), hStmt=" + paramLong + ", ipar=" + paramInt);
/*      */     }
/*      */ 
/* 2864 */     byte[] arrayOfByte = new byte[1];
/*      */ 
/* 2869 */     int i = describeParam(paramLong, paramInt, 4, arrayOfByte);
/*      */ 
/* 2871 */     if (arrayOfByte[0] != 0)
/*      */     {
/* 2875 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */ 
/* 2879 */     if (i == 1) {
/* 2880 */       bool = true;
/*      */     }
/*      */ 
/* 2883 */     if (this.tracer.isTracing()) {
/* 2884 */       this.tracer.trace("nullable=" + bool);
/*      */     }
/*      */ 
/* 2887 */     return bool;
/*      */   }
/*      */ 
/*      */   public int SQLDescribeParamPrecision(long paramLong, int paramInt)
/*      */     throws SQLException, JdbcOdbcSQLWarning
/*      */   {
/* 2901 */     if (this.tracer.isTracing()) {
/* 2902 */       this.tracer.trace("Parameter precision (SQLDescribeParam), hStmt=" + paramLong + ", ipar=" + paramInt);
/*      */     }
/*      */ 
/* 2906 */     byte[] arrayOfByte = new byte[1];
/*      */ 
/* 2911 */     int i = describeParam(paramLong, paramInt, 2, arrayOfByte);
/*      */ 
/* 2913 */     if (arrayOfByte[0] != 0)
/*      */     {
/* 2917 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */ 
/* 2921 */     if (this.tracer.isTracing()) {
/* 2922 */       this.tracer.trace("precision=" + i);
/*      */     }
/*      */ 
/* 2925 */     return i;
/*      */   }
/*      */ 
/*      */   public int SQLDescribeParamScale(long paramLong, int paramInt)
/*      */     throws SQLException, JdbcOdbcSQLWarning
/*      */   {
/* 2940 */     if (this.tracer.isTracing()) {
/* 2941 */       this.tracer.trace("Parameter scale (SQLDescribeParam), hStmt=" + paramLong + ", ipar=" + paramInt);
/*      */     }
/*      */ 
/* 2945 */     byte[] arrayOfByte = new byte[1];
/*      */ 
/* 2950 */     int i = describeParam(paramLong, paramInt, 3, arrayOfByte);
/*      */ 
/* 2952 */     if (arrayOfByte[0] != 0)
/*      */     {
/* 2956 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */ 
/* 2960 */     if (this.tracer.isTracing()) {
/* 2961 */       this.tracer.trace("scale=" + i);
/*      */     }
/*      */ 
/* 2964 */     return i;
/*      */   }
/*      */ 
/*      */   public int SQLDescribeParamType(long paramLong, int paramInt)
/*      */     throws SQLException, JdbcOdbcSQLWarning
/*      */   {
/* 2979 */     if (this.tracer.isTracing()) {
/* 2980 */       this.tracer.trace("Parameter type (SQLDescribeParam), hStmt=" + paramLong + ", ipar=" + paramInt);
/*      */     }
/*      */ 
/* 2984 */     byte[] arrayOfByte = new byte[1];
/*      */ 
/* 2989 */     int i = describeParam(paramLong, paramInt, 1, arrayOfByte);
/*      */ 
/* 2991 */     if (arrayOfByte[0] != 0)
/*      */     {
/* 2995 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */ 
/* 2999 */     if (this.tracer.isTracing()) {
/* 3000 */       this.tracer.trace("type=" + i);
/*      */     }
/*      */ 
/* 3003 */     return i;
/*      */   }
/*      */ 
/*      */   public void SQLDisconnect(long paramLong)
/*      */     throws SQLException
/*      */   {
/* 3015 */     if (this.tracer.isTracing()) {
/* 3016 */       this.tracer.trace("Disconnecting (SQLDisconnect), hDbc=" + paramLong);
/*      */     }
/*      */ 
/* 3020 */     Set localSet = hstmtMap.keySet();
/* 3021 */     Object[] arrayOfObject = localSet.toArray();
/* 3022 */     int i = arrayOfObject.length;
/*      */ 
/* 3024 */     for (int j = 0; j < i; j++) {
/* 3025 */       Long localLong = (Long)hstmtMap.get(arrayOfObject[j]);
/* 3026 */       if ((localLong != null) && 
/* 3027 */         (localLong.longValue() == paramLong)) {
/* 3028 */         SQLFreeStmt(((Long)arrayOfObject[j]).longValue(), 1);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 3033 */     byte[] arrayOfByte = new byte[1];
/* 3034 */     disconnect(paramLong, arrayOfByte);
/*      */ 
/* 3036 */     if (arrayOfByte[0] != 0)
/*      */     {
/* 3040 */       standardError((short)arrayOfByte[0], 0L, paramLong, 0L);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLDriverConnect(long paramLong, String paramString)
/*      */     throws SQLException, SQLWarning
/*      */   {
/* 3056 */     if (this.tracer.isTracing()) {
/* 3057 */       this.tracer.trace("Connecting (SQLDriverConnect), hDbc=" + paramLong + ", szConnStrIn=" + paramString);
/*      */     }
/*      */ 
/* 3061 */     byte[] arrayOfByte1 = new byte[1];
/* 3062 */     byte[] arrayOfByte2 = null;
/* 3063 */     char[] arrayOfChar = null;
/*      */ 
/* 3065 */     if (paramString != null)
/* 3066 */       arrayOfChar = paramString.toCharArray();
/*      */     try
/*      */     {
/* 3069 */       if (paramString != null)
/* 3070 */         arrayOfByte2 = CharsToBytes(this.charSet, arrayOfChar);
/*      */     }
/*      */     catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/*      */     }
/* 3074 */     driverConnect(paramLong, arrayOfByte2, arrayOfByte1);
/*      */ 
/* 3076 */     if (arrayOfByte1[0] != 0)
/*      */     {
/* 3080 */       standardError((short)arrayOfByte1[0], 0L, paramLong, 0L);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLExecDirect(long paramLong, String paramString)
/*      */     throws SQLException, SQLWarning
/*      */   {
/* 3095 */     if (this.tracer.isTracing()) {
/* 3096 */       this.tracer.trace("Executing (SQLExecDirect), hStmt=" + paramLong + ", szSqlStr=" + paramString);
/*      */     }
/*      */ 
/* 3100 */     byte[] arrayOfByte1 = new byte[1];
/*      */ 
/* 3102 */     byte[] arrayOfByte2 = null;
/* 3103 */     char[] arrayOfChar = null;
/* 3104 */     if (paramString != null)
/* 3105 */       arrayOfChar = paramString.toCharArray();
/*      */     try {
/* 3107 */       if (paramString != null)
/* 3108 */         arrayOfByte2 = CharsToBytes(this.charSet, arrayOfChar);
/*      */     } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/*      */     }
/* 3111 */     execDirect(paramLong, arrayOfByte2, arrayOfByte1);
/*      */ 
/* 3113 */     if (arrayOfByte1[0] != 0)
/*      */     {
/* 3117 */       standardError((short)arrayOfByte1[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean SQLExecute(long paramLong)
/*      */     throws SQLException, SQLWarning
/*      */   {
/* 3132 */     boolean bool = false;
/*      */ 
/* 3134 */     if (this.tracer.isTracing()) {
/* 3135 */       this.tracer.trace("Executing (SQLExecute), hStmt=" + paramLong);
/*      */     }
/*      */ 
/* 3138 */     byte[] arrayOfByte = new byte[1];
/* 3139 */     execute(paramLong, arrayOfByte);
/*      */ 
/* 3144 */     if (arrayOfByte[0] == 99) {
/* 3145 */       if (this.tracer.isTracing()) {
/* 3146 */         this.tracer.trace("SQL_NEED_DATA returned");
/*      */       }
/* 3148 */       bool = true;
/* 3149 */       arrayOfByte[0] = 0;
/*      */     }
/*      */ 
/* 3152 */     if (arrayOfByte[0] != 0)
/*      */     {
/* 3156 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */ 
/* 3159 */     return bool;
/*      */   }
/*      */ 
/*      */   public boolean SQLFetch(long paramLong)
/*      */     throws SQLException, SQLWarning
/*      */   {
/* 3170 */     boolean bool = true;
/*      */ 
/* 3172 */     if (this.tracer.isTracing()) {
/* 3173 */       this.tracer.trace("Fetching (SQLFetch), hStmt=" + paramLong);
/*      */     }
/*      */ 
/* 3176 */     byte[] arrayOfByte = new byte[1];
/* 3177 */     fetch(paramLong, arrayOfByte);
/*      */ 
/* 3183 */     if (arrayOfByte[0] == 100) {
/* 3184 */       bool = false;
/* 3185 */       arrayOfByte[0] = 0;
/* 3186 */       if (this.tracer.isTracing()) {
/* 3187 */         this.tracer.trace("End of result set (SQL_NO_DATA)");
/*      */       }
/*      */     }
/*      */ 
/* 3191 */     if (arrayOfByte[0] != 0)
/*      */     {
/* 3195 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */ 
/* 3198 */     return bool;
/*      */   }
/*      */ 
/*      */   public boolean SQLFetchScroll(long paramLong, short paramShort, int paramInt)
/*      */     throws SQLException, SQLWarning
/*      */   {
/* 3211 */     boolean bool = true;
/*      */ 
/* 3213 */     if (this.tracer.isTracing()) {
/* 3214 */       this.tracer.trace("Fetching (SQLFetchScroll), hStmt=" + paramLong);
/*      */     }
/*      */ 
/* 3217 */     byte[] arrayOfByte = new byte[1];
/* 3218 */     fetchScroll(paramLong, paramShort, paramInt, arrayOfByte);
/*      */ 
/* 3224 */     if (arrayOfByte[0] == 100) {
/* 3225 */       bool = false;
/* 3226 */       arrayOfByte[0] = 0;
/* 3227 */       if (this.tracer.isTracing()) {
/* 3228 */         this.tracer.trace("End of result set (SQL_NO_DATA)");
/*      */       }
/*      */     }
/*      */ 
/* 3232 */     if (arrayOfByte[0] != 0)
/*      */     {
/* 3236 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */ 
/* 3239 */     return bool;
/*      */   }
/*      */ 
/*      */   public void SQLForeignKeys(long paramLong, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6)
/*      */     throws SQLException, SQLWarning
/*      */   {
/* 3258 */     if (this.tracer.isTracing()) {
/* 3259 */       this.tracer.trace("(SQLForeignKeys), hStmt=" + paramLong + ", Pcatalog=" + paramString1 + ", Pschema=" + paramString2 + ", Ptable=" + paramString3 + ", Fcatalog=" + paramString4 + ", Fschema=" + paramString5 + ", Ftable=" + paramString6);
/*      */     }
/*      */ 
/* 3265 */     byte[] arrayOfByte1 = new byte[1];
/*      */ 
/* 3267 */     byte[] arrayOfByte2 = null;
/* 3268 */     byte[] arrayOfByte3 = null;
/* 3269 */     byte[] arrayOfByte4 = null;
/* 3270 */     byte[] arrayOfByte5 = null;
/* 3271 */     byte[] arrayOfByte6 = null;
/* 3272 */     byte[] arrayOfByte7 = null;
/* 3273 */     char[] arrayOfChar1 = null;
/* 3274 */     char[] arrayOfChar2 = null;
/* 3275 */     char[] arrayOfChar3 = null;
/* 3276 */     char[] arrayOfChar4 = null;
/* 3277 */     char[] arrayOfChar5 = null;
/* 3278 */     char[] arrayOfChar6 = null;
/* 3279 */     if (paramString1 != null)
/* 3280 */       arrayOfChar1 = paramString1.toCharArray();
/* 3281 */     if (paramString2 != null)
/* 3282 */       arrayOfChar2 = paramString2.toCharArray();
/* 3283 */     if (paramString3 != null)
/* 3284 */       arrayOfChar3 = paramString3.toCharArray();
/* 3285 */     if (paramString4 != null)
/* 3286 */       arrayOfChar4 = paramString4.toCharArray();
/* 3287 */     if (paramString5 != null)
/* 3288 */       arrayOfChar5 = paramString5.toCharArray();
/* 3289 */     if (paramString6 != null)
/* 3290 */       arrayOfChar6 = paramString6.toCharArray();
/*      */     try {
/* 3292 */       if (paramString1 != null)
/* 3293 */         arrayOfByte2 = CharsToBytes(this.charSet, arrayOfChar1);
/* 3294 */       if (paramString2 != null)
/* 3295 */         arrayOfByte3 = CharsToBytes(this.charSet, arrayOfChar2);
/* 3296 */       if (paramString3 != null)
/* 3297 */         arrayOfByte4 = CharsToBytes(this.charSet, arrayOfChar3);
/* 3298 */       if (paramString4 != null)
/* 3299 */         arrayOfByte5 = CharsToBytes(this.charSet, arrayOfChar4);
/* 3300 */       if (paramString5 != null)
/* 3301 */         arrayOfByte6 = CharsToBytes(this.charSet, arrayOfChar5);
/* 3302 */       if (paramString6 != null)
/* 3303 */         arrayOfByte7 = CharsToBytes(this.charSet, arrayOfChar6);
/*      */     } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/*      */     }
/* 3306 */     foreignKeys(paramLong, arrayOfByte2, paramString1 == null, arrayOfByte3, paramString2 == null, arrayOfByte4, paramString3 == null, arrayOfByte5, paramString4 == null, arrayOfByte6, paramString5 == null, arrayOfByte7, paramString6 == null, arrayOfByte1);
/*      */ 
/* 3315 */     if (arrayOfByte1[0] != 0)
/*      */     {
/* 3319 */       standardError((short)arrayOfByte1[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLFreeConnect(long paramLong)
/*      */     throws SQLException
/*      */   {
/* 3334 */     if (this.tracer.isTracing()) {
/* 3335 */       this.tracer.trace("Closing connection (SQLFreeConnect), hDbc=" + paramLong);
/*      */     }
/*      */ 
/* 3338 */     byte[] arrayOfByte = new byte[1];
/* 3339 */     freeConnect(paramLong, arrayOfByte);
/*      */ 
/* 3341 */     if (arrayOfByte[0] != 0)
/*      */     {
/* 3345 */       standardError((short)arrayOfByte[0], 0L, paramLong, 0L);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLFreeEnv(long paramLong)
/*      */     throws SQLException
/*      */   {
/* 3360 */     if (this.tracer.isTracing()) {
/* 3361 */       this.tracer.trace("Closing environment (SQLFreeEnv), hEnv=" + paramLong);
/*      */     }
/*      */ 
/* 3364 */     byte[] arrayOfByte = new byte[1];
/* 3365 */     freeEnv(paramLong, arrayOfByte);
/*      */ 
/* 3367 */     if (arrayOfByte[0] != 0)
/* 3368 */       throwGenericSQLException();
/*      */   }
/*      */ 
/*      */   public synchronized void SQLFreeStmt(long paramLong, int paramInt)
/*      */     throws SQLException
/*      */   {
/* 3384 */     byte[] arrayOfByte = new byte[1];
/*      */ 
/* 3387 */     Long localLong = new Long(paramLong);
/* 3388 */     if (paramInt == 1) {
/* 3389 */       if (hstmtMap.containsKey(localLong)) {
/* 3390 */         hstmtMap.remove(localLong);
/* 3391 */         freeStmt(paramLong, paramInt, arrayOfByte);
/* 3392 */         if (this.tracer.isTracing())
/* 3393 */           this.tracer.trace("Free statement (SQLFreeStmt), hStmt=" + paramLong + ", fOption=" + paramInt);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 3398 */       freeStmt(paramLong, paramInt, arrayOfByte);
/* 3399 */       if (this.tracer.isTracing()) {
/* 3400 */         this.tracer.trace("Free statement (SQLFreeStmt), hStmt=" + paramLong + ", fOption=" + paramInt);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 3405 */     if (arrayOfByte[0] != 0)
/* 3406 */       throwGenericSQLException();
/*      */   }
/*      */ 
/*      */   public long SQLGetConnectOption(long paramLong, short paramShort)
/*      */     throws SQLException
/*      */   {
/* 3422 */     if (this.tracer.isTracing()) {
/* 3423 */       this.tracer.trace("Connection Option (SQLGetConnectOption), hDbc=" + paramLong + ", fOption=" + paramShort);
/*      */     }
/*      */ 
/* 3427 */     byte[] arrayOfByte = new byte[1];
/* 3428 */     long l = getConnectOption(paramLong, paramShort, arrayOfByte);
/*      */ 
/* 3430 */     if (arrayOfByte[0] != 0)
/*      */     {
/* 3434 */       standardError((short)arrayOfByte[0], 0L, paramLong, 0L);
/*      */     }
/*      */ 
/* 3438 */     if (this.tracer.isTracing()) {
/* 3439 */       this.tracer.trace("option value (int)=" + l);
/*      */     }
/*      */ 
/* 3442 */     return l;
/*      */   }
/*      */ 
/*      */   public String SQLGetConnectOptionString(long paramLong, short paramShort)
/*      */     throws SQLException
/*      */   {
/* 3457 */     if (this.tracer.isTracing()) {
/* 3458 */       this.tracer.trace("Connection Option (SQLGetConnectOption), hDbc=" + paramLong + ", fOption=" + paramShort);
/*      */     }
/*      */ 
/* 3462 */     byte[] arrayOfByte1 = new byte[1];
/* 3463 */     byte[] arrayOfByte2 = new byte[300];
/*      */ 
/* 3465 */     getConnectOptionString(paramLong, paramShort, arrayOfByte2, arrayOfByte1);
/*      */ 
/* 3467 */     if (arrayOfByte1[0] != 0)
/*      */     {
/* 3471 */       standardError((short)arrayOfByte1[0], 0L, paramLong, 0L);
/*      */     }
/*      */ 
/* 3475 */     String str = new String();
/*      */     try {
/* 3477 */       str = BytesToChars(this.charSet, arrayOfByte2);
/*      */     } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/*      */     }
/* 3480 */     if (this.tracer.isTracing()) {
/* 3481 */       this.tracer.trace("option value (int)=" + str.trim());
/*      */     }
/*      */ 
/* 3484 */     return str.trim();
/*      */   }
/*      */ 
/*      */   public String SQLGetCursorName(long paramLong)
/*      */     throws SQLException, JdbcOdbcSQLWarning
/*      */   {
/* 3498 */     if (this.tracer.isTracing()) {
/* 3499 */       this.tracer.trace("Cursor name (SQLGetCursorName), hStmt=" + paramLong);
/*      */     }
/*      */ 
/* 3502 */     byte[] arrayOfByte2 = new byte[1];
/* 3503 */     byte[] arrayOfByte1 = new byte[300];
/* 3504 */     getCursorName(paramLong, arrayOfByte1, arrayOfByte2);
/*      */ 
/* 3506 */     if (arrayOfByte2[0] != 0) {
/*      */       try
/*      */       {
/* 3509 */         standardError((short)arrayOfByte2[0], 0L, 0L, paramLong);
/*      */       }
/*      */       catch (JdbcOdbcSQLWarning localJdbcOdbcSQLWarning)
/*      */       {
/* 3518 */         String str2 = new String();
/*      */         try {
/* 3520 */           str2 = BytesToChars(this.charSet, arrayOfByte1);
/*      */         } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/*      */         }
/* 3523 */         if (this.tracer.isTracing()) {
/* 3524 */           this.tracer.trace("value=" + str2.trim());
/*      */         }
/* 3526 */         localJdbcOdbcSQLWarning.value = str2.trim();
/*      */ 
/* 3530 */         throw localJdbcOdbcSQLWarning;
/*      */       }
/*      */     }
/*      */ 
/* 3534 */     String str1 = new String(arrayOfByte1);
/* 3535 */     if (this.tracer.isTracing()) {
/* 3536 */       this.tracer.trace("value=" + str1.trim());
/*      */     }
/* 3538 */     return str1.trim();
/*      */   }
/*      */ 
/*      */   public int SQLGetDataBinary(long paramLong, int paramInt, byte[] paramArrayOfByte)
/*      */     throws SQLException, JdbcOdbcSQLWarning
/*      */   {
/* 3551 */     return SQLGetDataBinary(paramLong, paramInt, -2, paramArrayOfByte, paramArrayOfByte.length);
/*      */   }
/*      */ 
/*      */   public int SQLGetDataBinary(long paramLong, int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3)
/*      */     throws SQLException, JdbcOdbcSQLWarning
/*      */   {
/* 3568 */     int i = 0;
/*      */ 
/* 3570 */     if (this.tracer.isTracing()) {
/* 3571 */       this.tracer.trace("Get binary data (SQLGetData), hStmt=" + paramLong + ", column=" + paramInt1 + ", type=" + paramInt2 + ", length=" + paramInt3);
/*      */     }
/*      */ 
/* 3580 */     byte[] arrayOfByte = new byte[2];
/* 3581 */     i = getDataBinary(paramLong, paramInt1, paramInt2, paramArrayOfByte, paramInt3, arrayOfByte);
/*      */ 
/* 3586 */     if (arrayOfByte[0] == 100) {
/* 3587 */       i = -1;
/* 3588 */       arrayOfByte[0] = 0;
/*      */     }
/*      */ 
/* 3591 */     if (arrayOfByte[0] != 0)
/*      */     {
/*      */       try
/*      */       {
/* 3595 */         standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */       }
/*      */       catch (JdbcOdbcSQLWarning localJdbcOdbcSQLWarning)
/*      */       {
/* 3602 */         if (this.tracer.isTracing()) {
/* 3603 */           if (i == -1) {
/* 3604 */             this.tracer.trace("NULL");
/*      */           }
/* 3607 */           else if (this.tracer.isTracing()) {
/* 3608 */             this.tracer.trace("Bytes: " + i);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 3617 */         localJdbcOdbcSQLWarning.value = new Integer(i);
/*      */ 
/* 3620 */         throw localJdbcOdbcSQLWarning;
/*      */       }
/*      */     }
/* 3623 */     if (this.tracer.isTracing()) {
/* 3624 */       if (i == -1) {
/* 3625 */         this.tracer.trace("NULL");
/*      */       }
/* 3628 */       else if (this.tracer.isTracing()) {
/* 3629 */         this.tracer.trace("Bytes: " + i);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 3634 */     return i;
/*      */   }
/*      */ 
/*      */   public Double SQLGetDataDouble(long paramLong, int paramInt)
/*      */     throws SQLException, JdbcOdbcSQLWarning
/*      */   {
/* 3648 */     if (this.tracer.isTracing()) {
/* 3649 */       this.tracer.trace("Get double data (SQLGetData), hStmt=" + paramLong + ", column=" + paramInt);
/*      */     }
/*      */ 
/* 3657 */     byte[] arrayOfByte = new byte[2];
/* 3658 */     double d = getDataDouble(paramLong, paramInt, arrayOfByte);
/*      */ 
/* 3660 */     if (arrayOfByte[0] != 0)
/*      */     {
/*      */       try
/*      */       {
/* 3664 */         standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */       }
/*      */       catch (JdbcOdbcSQLWarning localJdbcOdbcSQLWarning)
/*      */       {
/* 3674 */         if (arrayOfByte[1] == 0) {
/* 3675 */           if (this.tracer.isTracing()) {
/* 3676 */             this.tracer.trace("value=" + d);
/*      */           }
/*      */ 
/* 3680 */           localJdbcOdbcSQLWarning.value = new Double(d);
/*      */         }
/*      */         else
/*      */         {
/* 3684 */           if (this.tracer.isTracing()) {
/* 3685 */             this.tracer.trace("NULL");
/*      */           }
/*      */ 
/* 3689 */           localJdbcOdbcSQLWarning.value = null;
/*      */         }
/*      */ 
/* 3693 */         throw localJdbcOdbcSQLWarning;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 3699 */     if (arrayOfByte[1] == 0) {
/* 3700 */       if (this.tracer.isTracing()) {
/* 3701 */         this.tracer.trace("value=" + d);
/*      */       }
/*      */ 
/* 3704 */       return new Double(d);
/*      */     }
/*      */ 
/* 3707 */     if (this.tracer.isTracing()) {
/* 3708 */       this.tracer.trace("NULL");
/*      */     }
/*      */ 
/* 3711 */     return null;
/*      */   }
/*      */ 
/*      */   public Float SQLGetDataFloat(long paramLong, int paramInt)
/*      */     throws SQLException, JdbcOdbcSQLWarning
/*      */   {
/* 3726 */     if (this.tracer.isTracing()) {
/* 3727 */       this.tracer.trace("Get float data (SQLGetData), hStmt=" + paramLong + ", column=" + paramInt);
/*      */     }
/*      */ 
/* 3735 */     byte[] arrayOfByte = new byte[2];
/*      */ 
/* 3737 */     float f = (float)getDataFloat(paramLong, paramInt, arrayOfByte);
/*      */ 
/* 3739 */     if (arrayOfByte[0] != 0)
/*      */     {
/*      */       try
/*      */       {
/* 3743 */         standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */       }
/*      */       catch (JdbcOdbcSQLWarning localJdbcOdbcSQLWarning)
/*      */       {
/* 3753 */         if (arrayOfByte[1] == 0) {
/* 3754 */           if (this.tracer.isTracing()) {
/* 3755 */             this.tracer.trace("value=" + f);
/*      */           }
/*      */ 
/* 3758 */           localJdbcOdbcSQLWarning.value = new Float(f);
/*      */         }
/*      */         else {
/* 3761 */           if (this.tracer.isTracing()) {
/* 3762 */             this.tracer.trace("NULL");
/*      */           }
/*      */ 
/* 3765 */           localJdbcOdbcSQLWarning.value = null;
/*      */         }
/*      */ 
/* 3769 */         throw localJdbcOdbcSQLWarning;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 3775 */     if (arrayOfByte[1] == 0)
/*      */     {
/* 3777 */       if (this.tracer.isTracing()) {
/* 3778 */         this.tracer.trace("value=" + f);
/*      */       }
/* 3780 */       return new Float(f);
/*      */     }
/*      */ 
/* 3783 */     if (this.tracer.isTracing()) {
/* 3784 */       this.tracer.trace("NULL");
/*      */     }
/*      */ 
/* 3787 */     return null;
/*      */   }
/*      */ 
/*      */   public Integer SQLGetDataInteger(long paramLong, int paramInt)
/*      */     throws SQLException, JdbcOdbcSQLWarning
/*      */   {
/* 3802 */     if (this.tracer.isTracing()) {
/* 3803 */       this.tracer.trace("Get integer data (SQLGetData), hStmt=" + paramLong + ", column=" + paramInt);
/*      */     }
/*      */ 
/* 3811 */     byte[] arrayOfByte = new byte[2];
/* 3812 */     int i = getDataInteger(paramLong, paramInt, arrayOfByte);
/*      */ 
/* 3814 */     if (arrayOfByte[0] != 0)
/*      */     {
/*      */       try
/*      */       {
/* 3818 */         standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */       }
/*      */       catch (JdbcOdbcSQLWarning localJdbcOdbcSQLWarning)
/*      */       {
/* 3828 */         if (arrayOfByte[1] == 0) {
/* 3829 */           if (this.tracer.isTracing()) {
/* 3830 */             this.tracer.trace("value=" + i);
/*      */           }
/*      */ 
/* 3833 */           localJdbcOdbcSQLWarning.value = new Integer(i);
/*      */         }
/*      */         else
/*      */         {
/* 3837 */           if (this.tracer.isTracing()) {
/* 3838 */             this.tracer.trace("NULL");
/*      */           }
/*      */ 
/* 3841 */           localJdbcOdbcSQLWarning.value = null;
/*      */         }
/*      */ 
/* 3845 */         throw localJdbcOdbcSQLWarning;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 3851 */     if (arrayOfByte[1] == 0) {
/* 3852 */       if (this.tracer.isTracing()) {
/* 3853 */         this.tracer.trace("value=" + i);
/*      */       }
/*      */ 
/* 3856 */       return new Integer(i);
/*      */     }
/*      */ 
/* 3859 */     if (this.tracer.isTracing()) {
/* 3860 */       this.tracer.trace("NULL");
/*      */     }
/*      */ 
/* 3863 */     return null;
/*      */   }
/*      */ 
/*      */   public String SQLGetDataString(long paramLong, int paramInt1, int paramInt2, boolean paramBoolean)
/*      */     throws SQLException, JdbcOdbcSQLWarning
/*      */   {
/* 3880 */     if (this.tracer.isTracing()) {
/* 3881 */       this.tracer.trace("Get string data (SQLGetData), hStmt=" + paramLong + ", column=" + paramInt1 + ", maxLen=" + paramInt2);
/*      */     }
/*      */ 
/* 3889 */     byte[] arrayOfByte1 = new byte[2];
/* 3890 */     byte[] arrayOfByte2 = new byte[paramInt2];
/*      */ 
/* 3892 */     int i = getDataString(paramLong, paramInt1, arrayOfByte2, arrayOfByte1);
/*      */ 
/* 3897 */     if (i < 0) {
/* 3898 */       arrayOfByte1[1] = 1;
/*      */     }
/*      */ 
/* 3906 */     if (i > paramInt2)
/* 3907 */       i = paramInt2;
/*      */     char[] arrayOfChar;
/* 3910 */     if (arrayOfByte1[0] != 0)
/*      */     {
/*      */       try
/*      */       {
/* 3914 */         standardError((short)arrayOfByte1[0], 0L, 0L, paramLong);
/*      */       }
/*      */       catch (JdbcOdbcSQLWarning localJdbcOdbcSQLWarning)
/*      */       {
/* 3924 */         if (arrayOfByte1[1] == 0)
/*      */         {
/* 3928 */           arrayOfChar = new char[i];
/* 3929 */           String str2 = new String();
/* 3930 */           if (i > 0)
/*      */             try {
/* 3932 */               str2 = BytesToChars(this.charSet, arrayOfByte2);
/*      */             } catch (UnsupportedEncodingException localUnsupportedEncodingException3) {
/* 3934 */               System.out.println(localUnsupportedEncodingException3);
/*      */             }
/*      */           else {
/*      */             try
/*      */             {
/* 3939 */               str2 = BytesToChars(this.charSet, arrayOfByte2);
/*      */             } catch (UnsupportedEncodingException localUnsupportedEncodingException4) {
/* 3941 */               System.out.println(localUnsupportedEncodingException4);
/*      */             }
/*      */           }
/*      */ 
/* 3945 */           if (this.tracer.isTracing()) {
/* 3946 */             this.tracer.trace(str2.trim());
/*      */           }
/* 3948 */           if (paramBoolean) {
/* 3949 */             localJdbcOdbcSQLWarning.value = str2.trim();
/*      */           }
/*      */           else
/* 3952 */             localJdbcOdbcSQLWarning.value = str2;
/*      */         }
/*      */         else
/*      */         {
/* 3956 */           if (this.tracer.isTracing()) {
/* 3957 */             this.tracer.trace("NULL");
/*      */           }
/*      */ 
/* 3960 */           localJdbcOdbcSQLWarning.value = null;
/*      */         }
/*      */ 
/* 3964 */         throw localJdbcOdbcSQLWarning;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 3970 */     if (arrayOfByte1[1] == 0)
/*      */     {
/* 3974 */       String str1 = new String();
/* 3975 */       arrayOfChar = new char[i];
/* 3976 */       if (i > 0)
/*      */         try {
/* 3978 */           str1 = BytesToChars(this.charSet, arrayOfByte2);
/*      */         } catch (UnsupportedEncodingException localUnsupportedEncodingException1) {
/* 3980 */           System.out.println(localUnsupportedEncodingException1);
/*      */         }
/*      */       else {
/*      */         try
/*      */         {
/* 3985 */           str1 = BytesToChars(this.charSet, arrayOfByte2);
/*      */         } catch (UnsupportedEncodingException localUnsupportedEncodingException2) {
/* 3987 */           System.out.println(localUnsupportedEncodingException2);
/*      */         }
/*      */       }
/*      */ 
/* 3991 */       if (this.tracer.isTracing()) {
/* 3992 */         this.tracer.trace(str1.trim());
/*      */       }
/*      */ 
/* 3997 */       if (paramBoolean) {
/* 3998 */         return str1.trim();
/*      */       }
/*      */ 
/* 4001 */       return str1;
/*      */     }
/*      */ 
/* 4005 */     if (this.tracer.isTracing()) {
/* 4006 */       this.tracer.trace("NULL");
/*      */     }
/* 4008 */     return null;
/*      */   }
/*      */ 
/*      */   public String SQLGetDataStringDate(long paramLong, int paramInt)
/*      */     throws SQLException, JdbcOdbcSQLWarning
/*      */   {
/* 4023 */     if (this.tracer.isTracing()) {
/* 4024 */       this.tracer.trace("Get date data (SQLGetData), hStmt=" + paramLong + ", column=" + paramInt);
/*      */     }
/*      */ 
/* 4032 */     byte[] arrayOfByte1 = new byte[2];
/* 4033 */     byte[] arrayOfByte2 = new byte[11];
/*      */ 
/* 4035 */     getDataStringDate(paramLong, paramInt, arrayOfByte2, arrayOfByte1);
/*      */ 
/* 4037 */     if (arrayOfByte1[0] != 0)
/*      */     {
/*      */       try
/*      */       {
/* 4041 */         standardError((short)arrayOfByte1[0], 0L, 0L, paramLong);
/*      */       }
/*      */       catch (JdbcOdbcSQLWarning localJdbcOdbcSQLWarning)
/*      */       {
/* 4051 */         if (arrayOfByte1[1] == 0)
/*      */         {
/* 4053 */           String str2 = new String();
/*      */           try {
/* 4055 */             str2 = BytesToChars(this.charSet, arrayOfByte2);
/*      */           } catch (UnsupportedEncodingException localUnsupportedEncodingException2) {
/*      */           }
/* 4058 */           if (this.tracer.isTracing()) {
/* 4059 */             this.tracer.trace(str2.trim());
/*      */           }
/* 4061 */           localJdbcOdbcSQLWarning.value = str2.trim();
/*      */         }
/*      */         else {
/* 4064 */           if (this.tracer.isTracing()) {
/* 4065 */             this.tracer.trace("NULL");
/*      */           }
/*      */ 
/* 4068 */           localJdbcOdbcSQLWarning.value = null;
/*      */         }
/*      */ 
/* 4072 */         throw localJdbcOdbcSQLWarning;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 4078 */     if (arrayOfByte1[1] == 0) {
/* 4079 */       String str1 = new String();
/*      */       try {
/* 4081 */         str1 = BytesToChars(this.charSet, arrayOfByte2);
/*      */       } catch (UnsupportedEncodingException localUnsupportedEncodingException1) {
/*      */       }
/* 4084 */       if (this.tracer.isTracing()) {
/* 4085 */         this.tracer.trace(str1.trim());
/*      */       }
/*      */ 
/* 4090 */       return str1.trim();
/*      */     }
/*      */ 
/* 4093 */     if (this.tracer.isTracing()) {
/* 4094 */       this.tracer.trace("NULL");
/*      */     }
/* 4096 */     return null;
/*      */   }
/*      */ 
/*      */   public String SQLGetDataStringTime(long paramLong, int paramInt)
/*      */     throws SQLException, JdbcOdbcSQLWarning
/*      */   {
/* 4111 */     if (this.tracer.isTracing()) {
/* 4112 */       this.tracer.trace("Get time data (SQLGetData), hStmt=" + paramLong + ", column=" + paramInt);
/*      */     }
/*      */ 
/* 4120 */     byte[] arrayOfByte1 = new byte[2];
/* 4121 */     byte[] arrayOfByte2 = new byte[9];
/*      */ 
/* 4123 */     getDataStringTime(paramLong, paramInt, arrayOfByte2, arrayOfByte1);
/*      */ 
/* 4125 */     if (arrayOfByte1[0] != 0)
/*      */     {
/*      */       try
/*      */       {
/* 4129 */         standardError((short)arrayOfByte1[0], 0L, 0L, paramLong);
/*      */       }
/*      */       catch (JdbcOdbcSQLWarning localJdbcOdbcSQLWarning)
/*      */       {
/* 4139 */         if (arrayOfByte1[1] == 0)
/*      */         {
/* 4141 */           String str2 = new String();
/*      */           try {
/* 4143 */             str2 = BytesToChars(this.charSet, arrayOfByte2);
/*      */           } catch (UnsupportedEncodingException localUnsupportedEncodingException2) {
/*      */           }
/* 4146 */           if (this.tracer.isTracing()) {
/* 4147 */             this.tracer.trace(str2.trim());
/*      */           }
/* 4149 */           localJdbcOdbcSQLWarning.value = str2.trim();
/*      */         }
/*      */         else {
/* 4152 */           if (this.tracer.isTracing()) {
/* 4153 */             this.tracer.trace("NULL");
/*      */           }
/*      */ 
/* 4156 */           localJdbcOdbcSQLWarning.value = null;
/*      */         }
/*      */ 
/* 4160 */         throw localJdbcOdbcSQLWarning;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 4166 */     if (arrayOfByte1[1] == 0) {
/* 4167 */       String str1 = new String();
/*      */       try {
/* 4169 */         str1 = BytesToChars(this.charSet, arrayOfByte2);
/*      */       } catch (UnsupportedEncodingException localUnsupportedEncodingException1) {
/*      */       }
/* 4172 */       if (this.tracer.isTracing()) {
/* 4173 */         this.tracer.trace(str1.trim());
/*      */       }
/*      */ 
/* 4178 */       return str1.trim();
/*      */     }
/*      */ 
/* 4181 */     if (this.tracer.isTracing()) {
/* 4182 */       this.tracer.trace("NULL");
/*      */     }
/* 4184 */     return null;
/*      */   }
/*      */ 
/*      */   public String SQLGetDataStringTimestamp(long paramLong, int paramInt)
/*      */     throws SQLException, JdbcOdbcSQLWarning
/*      */   {
/* 4199 */     if (this.tracer.isTracing()) {
/* 4200 */       this.tracer.trace("Get timestamp data (SQLGetData), hStmt=" + paramLong + ", column=" + paramInt);
/*      */     }
/*      */ 
/* 4208 */     byte[] arrayOfByte1 = new byte[2];
/* 4209 */     byte[] arrayOfByte2 = new byte[30];
/*      */ 
/* 4211 */     getDataStringTimestamp(paramLong, paramInt, arrayOfByte2, arrayOfByte1);
/*      */ 
/* 4213 */     if (arrayOfByte1[0] != 0)
/*      */     {
/*      */       try
/*      */       {
/* 4217 */         standardError((short)arrayOfByte1[0], 0L, 0L, paramLong);
/*      */       }
/*      */       catch (JdbcOdbcSQLWarning localJdbcOdbcSQLWarning)
/*      */       {
/* 4227 */         if (arrayOfByte1[1] == 0)
/*      */         {
/* 4229 */           String str2 = new String();
/*      */           try {
/* 4231 */             str2 = BytesToChars(this.charSet, arrayOfByte2);
/*      */           } catch (UnsupportedEncodingException localUnsupportedEncodingException2) {
/*      */           }
/* 4234 */           if (this.tracer.isTracing()) {
/* 4235 */             this.tracer.trace(str2.trim());
/*      */           }
/* 4237 */           localJdbcOdbcSQLWarning.value = str2.trim();
/*      */         }
/*      */         else {
/* 4240 */           if (this.tracer.isTracing()) {
/* 4241 */             this.tracer.trace("NULL");
/*      */           }
/*      */ 
/* 4244 */           localJdbcOdbcSQLWarning.value = null;
/*      */         }
/*      */ 
/* 4248 */         throw localJdbcOdbcSQLWarning;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 4254 */     if (arrayOfByte1[1] == 0) {
/* 4255 */       String str1 = new String();
/*      */       try {
/* 4257 */         str1 = BytesToChars(this.charSet, arrayOfByte2);
/*      */       } catch (UnsupportedEncodingException localUnsupportedEncodingException1) {
/*      */       }
/* 4260 */       if (this.tracer.isTracing()) {
/* 4261 */         this.tracer.trace(str1.trim());
/*      */       }
/*      */ 
/* 4266 */       return str1.trim();
/*      */     }
/*      */ 
/* 4269 */     if (this.tracer.isTracing()) {
/* 4270 */       this.tracer.trace("NULL");
/*      */     }
/* 4272 */     return null;
/*      */   }
/*      */ 
/*      */   public int SQLGetInfo(long paramLong, short paramShort)
/*      */     throws SQLException
/*      */   {
/* 4288 */     if (this.tracer.isTracing()) {
/* 4289 */       this.tracer.trace("Get connection info (SQLGetInfo), hDbc=" + paramLong + ", fInfoType=" + paramShort);
/*      */     }
/*      */ 
/* 4293 */     byte[] arrayOfByte = new byte[1];
/* 4294 */     int i = getInfo(paramLong, paramShort, arrayOfByte);
/*      */ 
/* 4296 */     if (arrayOfByte[0] != 0)
/*      */     {
/* 4300 */       standardError((short)arrayOfByte[0], 0L, paramLong, 0L);
/*      */     }
/*      */ 
/* 4303 */     if (this.tracer.isTracing()) {
/* 4304 */       this.tracer.trace(" int value=" + i);
/*      */     }
/* 4306 */     return i;
/*      */   }
/*      */ 
/*      */   public int SQLGetInfoShort(long paramLong, short paramShort)
/*      */     throws SQLException
/*      */   {
/* 4321 */     if (this.tracer.isTracing()) {
/* 4322 */       this.tracer.trace("Get connection info (SQLGetInfo), hDbc=" + paramLong + ", fInfoType=" + paramShort);
/*      */     }
/*      */ 
/* 4326 */     byte[] arrayOfByte = new byte[1];
/* 4327 */     int i = getInfoShort(paramLong, paramShort, arrayOfByte);
/*      */ 
/* 4329 */     if (arrayOfByte[0] != 0)
/*      */     {
/* 4333 */       standardError((short)arrayOfByte[0], 0L, paramLong, 0L);
/*      */     }
/*      */ 
/* 4336 */     if (this.tracer.isTracing()) {
/* 4337 */       this.tracer.trace(" short value=" + i);
/*      */     }
/* 4339 */     return i;
/*      */   }
/*      */ 
/*      */   public String SQLGetInfoString(long paramLong, short paramShort)
/*      */     throws SQLException
/*      */   {
/* 4352 */     return SQLGetInfoString(paramLong, paramShort, 300);
/*      */   }
/*      */ 
/*      */   public String SQLGetInfoString(long paramLong, short paramShort, int paramInt)
/*      */     throws SQLException
/*      */   {
/* 4371 */     if (this.tracer.isTracing()) {
/* 4372 */       this.tracer.trace("Get connection info string (SQLGetInfo), hDbc=" + paramLong + ", fInfoType=" + paramShort + ", len=" + paramInt);
/*      */     }
/*      */ 
/* 4376 */     byte[] arrayOfByte1 = new byte[1];
/* 4377 */     byte[] arrayOfByte2 = new byte[paramInt];
/*      */ 
/* 4379 */     getInfoString(paramLong, paramShort, arrayOfByte2, arrayOfByte1);
/*      */ 
/* 4381 */     if (arrayOfByte1[0] != 0)
/*      */     {
/* 4385 */       standardError((short)arrayOfByte1[0], 0L, paramLong, 0L);
/*      */     }
/*      */ 
/* 4389 */     String str = new String();
/*      */     try {
/* 4391 */       str = BytesToChars(this.charSet, arrayOfByte2);
/*      */     } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/*      */     }
/* 4394 */     if (this.tracer.isTracing()) {
/* 4395 */       this.tracer.trace(str.trim());
/*      */     }
/* 4397 */     return str.trim();
/*      */   }
/*      */ 
/*      */   public long SQLGetStmtOption(long paramLong, short paramShort)
/*      */     throws SQLException, JdbcOdbcSQLWarning
/*      */   {
/* 4409 */     long l = 0L;
/*      */ 
/* 4412 */     if (this.tracer.isTracing()) {
/* 4413 */       this.tracer.trace("Get statement option (SQLGetStmtOption), hStmt=" + paramLong + ", fOption=" + paramShort);
/*      */     }
/*      */ 
/* 4417 */     byte[] arrayOfByte = new byte[1];
/* 4418 */     l = getStmtOption(paramLong, paramShort, arrayOfByte);
/*      */ 
/* 4420 */     if (arrayOfByte[0] != 0) {
/*      */       try
/*      */       {
/* 4423 */         standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */       }
/*      */       catch (JdbcOdbcSQLWarning localJdbcOdbcSQLWarning)
/*      */       {
/* 4429 */         if (this.tracer.isTracing()) {
/* 4430 */           this.tracer.trace("value=" + l);
/*      */         }
/*      */ 
/* 4435 */         localJdbcOdbcSQLWarning.value = BigDecimal.valueOf(l);
/*      */ 
/* 4439 */         throw localJdbcOdbcSQLWarning;
/*      */       }
/*      */     }
/*      */ 
/* 4443 */     if (this.tracer.isTracing()) {
/* 4444 */       this.tracer.trace("value=" + l);
/*      */     }
/* 4446 */     return l;
/*      */   }
/*      */ 
/*      */   public int SQLGetStmtAttr(long paramLong, int paramInt)
/*      */     throws SQLException
/*      */   {
/* 4461 */     if (this.tracer.isTracing()) {
/* 4462 */       this.tracer.trace("Get Statement Attribute (SQLGetStmtAttr), hDbc=" + paramLong + ", AttrType=" + paramInt);
/*      */     }
/*      */ 
/* 4466 */     byte[] arrayOfByte = new byte[1];
/* 4467 */     int i = getStmtAttr(paramLong, paramInt, arrayOfByte);
/*      */ 
/* 4469 */     if (arrayOfByte[0] != 0)
/*      */     {
/*      */       try
/*      */       {
/* 4474 */         standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */       }
/*      */       catch (JdbcOdbcSQLWarning localJdbcOdbcSQLWarning)
/*      */       {
/* 4480 */         if (this.tracer.isTracing()) {
/* 4481 */           this.tracer.trace("value=" + i);
/*      */         }
/*      */ 
/* 4487 */         localJdbcOdbcSQLWarning.value = BigDecimal.valueOf(i);
/*      */ 
/* 4491 */         throw localJdbcOdbcSQLWarning;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 4496 */     if (this.tracer.isTracing()) {
/* 4497 */       this.tracer.trace(" int value=" + i);
/*      */     }
/* 4499 */     return i;
/*      */   }
/*      */ 
/*      */   public void SQLGetTypeInfo(long paramLong, short paramShort)
/*      */     throws SQLException
/*      */   {
/* 4514 */     if (this.tracer.isTracing()) {
/* 4515 */       this.tracer.trace("Get type info (SQLGetTypeInfo), hStmt=" + paramLong + ", fSqlType=" + paramShort);
/*      */     }
/*      */ 
/* 4519 */     byte[] arrayOfByte = new byte[1];
/* 4520 */     getTypeInfo(paramLong, paramShort, arrayOfByte);
/*      */ 
/* 4522 */     if (arrayOfByte[0] != 0)
/*      */     {
/* 4526 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean SQLMoreResults(long paramLong)
/*      */     throws SQLException, SQLWarning
/*      */   {
/* 4539 */     boolean bool = true;
/*      */ 
/* 4541 */     if (this.tracer.isTracing()) {
/* 4542 */       this.tracer.trace("Get more results (SQLMoreResults), hStmt=" + paramLong);
/*      */     }
/*      */ 
/* 4545 */     byte[] arrayOfByte = new byte[1];
/* 4546 */     moreResults(paramLong, arrayOfByte);
/*      */ 
/* 4552 */     if (arrayOfByte[0] == 100) {
/* 4553 */       bool = false;
/* 4554 */       arrayOfByte[0] = 0;
/*      */     }
/*      */ 
/* 4557 */     if (arrayOfByte[0] != 0)
/*      */     {
/* 4561 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */ 
/* 4564 */     if (this.tracer.isTracing()) {
/* 4565 */       this.tracer.trace("More results: " + bool);
/*      */     }
/* 4567 */     return bool;
/*      */   }
/*      */ 
/*      */   public String SQLNativeSql(long paramLong, String paramString)
/*      */     throws SQLException
/*      */   {
/* 4582 */     byte[] arrayOfByte1 = new byte[1];
/*      */ 
/* 4587 */     int i = 1024;
/* 4588 */     if (paramString.length() * 4 > i) {
/* 4589 */       i = paramString.length() * 4;
/* 4590 */       if (i > 32768) {
/* 4591 */         i = 32768;
/*      */       }
/*      */     }
/*      */ 
/* 4595 */     if (this.tracer.isTracing()) {
/* 4596 */       this.tracer.trace("Convert native SQL (SQLNativeSql), hDbc=" + paramLong + ", nativeLen=" + i + ", SQL=" + paramString);
/*      */     }
/*      */ 
/* 4601 */     byte[] arrayOfByte2 = new byte[i];
/* 4602 */     byte[] arrayOfByte3 = null;
/* 4603 */     char[] arrayOfChar = null;
/* 4604 */     if (paramString != null)
/* 4605 */       arrayOfChar = paramString.toCharArray();
/*      */     try {
/* 4607 */       if (paramString != null)
/* 4608 */         arrayOfByte3 = CharsToBytes(this.charSet, arrayOfChar);
/*      */     } catch (UnsupportedEncodingException localUnsupportedEncodingException1) {
/*      */     }
/* 4611 */     nativeSql(paramLong, arrayOfByte3, arrayOfByte2, arrayOfByte1);
/*      */ 
/* 4613 */     if (arrayOfByte1[0] != 0)
/*      */     {
/* 4617 */       standardError((short)arrayOfByte1[0], 0L, paramLong, 0L);
/*      */     }
/*      */ 
/* 4621 */     String str = new String();
/*      */     try {
/* 4623 */       str = BytesToChars(this.charSet, arrayOfByte2);
/*      */     } catch (UnsupportedEncodingException localUnsupportedEncodingException2) {
/*      */     }
/* 4626 */     if (this.tracer.isTracing()) {
/* 4627 */       this.tracer.trace("Native SQL=" + str.trim());
/*      */     }
/* 4629 */     return str.trim();
/*      */   }
/*      */ 
/*      */   public int SQLNumParams(long paramLong)
/*      */     throws SQLException, JdbcOdbcSQLWarning
/*      */   {
/* 4640 */     int i = 0;
/*      */ 
/* 4642 */     if (this.tracer.isTracing()) {
/* 4643 */       this.tracer.trace("Number of parameter markers (SQLNumParams), hStmt=" + paramLong);
/*      */     }
/*      */ 
/* 4646 */     byte[] arrayOfByte = new byte[1];
/* 4647 */     i = numParams(paramLong, arrayOfByte);
/*      */ 
/* 4649 */     if (arrayOfByte[0] != 0)
/*      */     {
/* 4653 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */ 
/* 4656 */     if (this.tracer.isTracing()) {
/* 4657 */       this.tracer.trace("value=" + i);
/*      */     }
/* 4659 */     return i;
/*      */   }
/*      */ 
/*      */   public int SQLNumResultCols(long paramLong)
/*      */     throws SQLException, JdbcOdbcSQLWarning
/*      */   {
/* 4670 */     int i = 0;
/*      */ 
/* 4672 */     if (this.tracer.isTracing()) {
/* 4673 */       this.tracer.trace("Number of result columns (SQLNumResultCols), hStmt=" + paramLong);
/*      */     }
/*      */ 
/* 4676 */     byte[] arrayOfByte = new byte[1];
/* 4677 */     i = numResultCols(paramLong, arrayOfByte);
/*      */ 
/* 4679 */     if (arrayOfByte[0] != 0)
/*      */     {
/*      */       try
/*      */       {
/* 4683 */         standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */       }
/*      */       catch (JdbcOdbcSQLWarning localJdbcOdbcSQLWarning)
/*      */       {
/* 4688 */         if (this.tracer.isTracing()) {
/* 4689 */           this.tracer.trace("value=" + i);
/*      */         }
/*      */ 
/* 4695 */         localJdbcOdbcSQLWarning.value = BigDecimal.valueOf(i);
/*      */ 
/* 4699 */         throw localJdbcOdbcSQLWarning;
/*      */       }
/*      */     }
/* 4702 */     if (this.tracer.isTracing()) {
/* 4703 */       this.tracer.trace("value=" + i);
/*      */     }
/* 4705 */     return i;
/*      */   }
/*      */ 
/*      */   public int SQLParamData(long paramLong)
/*      */     throws SQLException, JdbcOdbcSQLWarning
/*      */   {
/* 4717 */     int i = 0;
/*      */ 
/* 4719 */     if (this.tracer.isTracing()) {
/* 4720 */       this.tracer.trace("Get parameter number (SQLParamData), hStmt=" + paramLong);
/*      */     }
/*      */ 
/* 4724 */     byte[] arrayOfByte = new byte[1];
/* 4725 */     i = paramData(paramLong, arrayOfByte);
/*      */ 
/* 4730 */     if (arrayOfByte[0] == 99) {
/* 4731 */       arrayOfByte[0] = 0;
/*      */     }
/*      */     else
/*      */     {
/* 4737 */       i = -1;
/*      */     }
/*      */ 
/* 4740 */     if (arrayOfByte[0] != 0)
/*      */     {
/* 4744 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */ 
/* 4748 */     if (this.tracer.isTracing()) {
/* 4749 */       this.tracer.trace("Parameter needing data=" + i);
/*      */     }
/*      */ 
/* 4752 */     return i;
/*      */   }
/*      */ 
/*      */   public int SQLParamDataInBlock(long paramLong, int paramInt)
/*      */     throws SQLException, JdbcOdbcSQLWarning
/*      */   {
/* 4766 */     int i = 0;
/*      */ 
/* 4768 */     if (this.tracer.isTracing()) {
/* 4769 */       this.tracer.trace("Get parameter number (SQLParamData in block-cursor), hStmt=" + paramLong);
/*      */     }
/*      */ 
/* 4773 */     byte[] arrayOfByte = new byte[1];
/* 4774 */     i = paramDataInBlock(paramLong, paramInt, arrayOfByte);
/*      */ 
/* 4779 */     if (arrayOfByte[0] == 99) {
/* 4780 */       arrayOfByte[0] = 0;
/*      */     }
/*      */     else
/*      */     {
/* 4786 */       i = -1;
/*      */     }
/*      */ 
/* 4789 */     if (arrayOfByte[0] != 0)
/*      */     {
/* 4793 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */ 
/* 4797 */     if (this.tracer.isTracing()) {
/* 4798 */       this.tracer.trace("Parameter needing data=" + i);
/*      */     }
/*      */ 
/* 4801 */     return i;
/*      */   }
/*      */ 
/*      */   public void SQLPrepare(long paramLong, String paramString)
/*      */     throws SQLException, SQLWarning
/*      */   {
/* 4814 */     if (this.tracer.isTracing()) {
/* 4815 */       this.tracer.trace("Preparing (SQLPrepare), hStmt=" + paramLong + ", szSqlStr=" + paramString);
/*      */     }
/*      */ 
/* 4819 */     byte[] arrayOfByte1 = new byte[1];
/*      */ 
/* 4821 */     byte[] arrayOfByte2 = null;
/* 4822 */     char[] arrayOfChar = null;
/* 4823 */     if (paramString != null)
/* 4824 */       arrayOfChar = paramString.toCharArray();
/*      */     try {
/* 4826 */       if (paramString != null)
/* 4827 */         arrayOfByte2 = CharsToBytes(this.charSet, arrayOfChar);
/*      */     }
/*      */     catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/*      */     }
/* 4831 */     prepare(paramLong, arrayOfByte2, arrayOfByte1);
/*      */ 
/* 4833 */     if (arrayOfByte1[0] != 0)
/*      */     {
/* 4837 */       standardError((short)arrayOfByte1[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLPutData(long paramLong, byte[] paramArrayOfByte, int paramInt)
/*      */     throws SQLException
/*      */   {
/* 4854 */     if (this.tracer.isTracing()) {
/* 4855 */       this.tracer.trace("Putting data (SQLPutData), hStmt=" + paramLong + ", len=" + paramInt);
/*      */     }
/*      */ 
/* 4860 */     byte[] arrayOfByte = new byte[1];
/* 4861 */     putData(paramLong, paramArrayOfByte, paramInt, arrayOfByte);
/*      */ 
/* 4863 */     if (arrayOfByte[0] != 0)
/*      */     {
/* 4866 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLPrimaryKeys(long paramLong, String paramString1, String paramString2, String paramString3)
/*      */     throws SQLException, SQLWarning
/*      */   {
/* 4885 */     if (this.tracer.isTracing()) {
/* 4886 */       this.tracer.trace("Primary keys (SQLPrimaryKeys), hStmt=" + paramLong + ", catalog=" + paramString1 + ", schema=" + paramString2 + ", table=" + paramString3);
/*      */     }
/*      */ 
/* 4891 */     byte[] arrayOfByte2 = null;
/* 4892 */     byte[] arrayOfByte3 = null;
/* 4893 */     byte[] arrayOfByte4 = null;
/* 4894 */     char[] arrayOfChar1 = null;
/* 4895 */     char[] arrayOfChar2 = null;
/* 4896 */     char[] arrayOfChar3 = null;
/* 4897 */     if (paramString1 != null)
/* 4898 */       arrayOfChar1 = paramString1.toCharArray();
/* 4899 */     if (paramString2 != null)
/* 4900 */       arrayOfChar2 = paramString2.toCharArray();
/* 4901 */     if (paramString3 != null)
/* 4902 */       arrayOfChar3 = paramString3.toCharArray();
/*      */     try {
/* 4904 */       if (paramString1 != null)
/* 4905 */         arrayOfByte2 = CharsToBytes(this.charSet, arrayOfChar1);
/* 4906 */       if (paramString2 != null)
/* 4907 */         arrayOfByte3 = CharsToBytes(this.charSet, arrayOfChar2);
/* 4908 */       if (paramString3 != null)
/* 4909 */         arrayOfByte4 = CharsToBytes(this.charSet, arrayOfChar3);
/*      */     } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/*      */     }
/* 4912 */     byte[] arrayOfByte1 = new byte[1];
/* 4913 */     primaryKeys(paramLong, arrayOfByte2, paramString1 == null, arrayOfByte3, paramString2 == null, arrayOfByte4, paramString3 == null, arrayOfByte1);
/*      */ 
/* 4919 */     if (arrayOfByte1[0] != 0)
/*      */     {
/* 4923 */       standardError((short)arrayOfByte1[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLProcedures(long paramLong, String paramString1, String paramString2, String paramString3)
/*      */     throws SQLException, SQLWarning
/*      */   {
/* 4941 */     if (this.tracer.isTracing()) {
/* 4942 */       this.tracer.trace("Procedures (SQLProcedures), hStmt=" + paramLong + ", catalog=" + paramString1 + ", schema=" + paramString2 + ", procedure=" + paramString3);
/*      */     }
/*      */ 
/* 4947 */     byte[] arrayOfByte1 = new byte[1];
/*      */ 
/* 4949 */     byte[] arrayOfByte2 = null;
/* 4950 */     byte[] arrayOfByte3 = null;
/* 4951 */     byte[] arrayOfByte4 = null;
/* 4952 */     char[] arrayOfChar1 = null;
/* 4953 */     char[] arrayOfChar2 = null;
/* 4954 */     char[] arrayOfChar3 = null;
/* 4955 */     if (paramString1 != null)
/* 4956 */       arrayOfChar1 = paramString1.toCharArray();
/* 4957 */     if (paramString2 != null)
/* 4958 */       arrayOfChar2 = paramString2.toCharArray();
/* 4959 */     if (paramString3 != null)
/* 4960 */       arrayOfChar3 = paramString3.toCharArray();
/*      */     try {
/* 4962 */       if (paramString1 != null)
/* 4963 */         arrayOfByte2 = CharsToBytes(this.charSet, arrayOfChar1);
/* 4964 */       if (paramString2 != null)
/* 4965 */         arrayOfByte3 = CharsToBytes(this.charSet, arrayOfChar2);
/* 4966 */       if (paramString3 != null)
/* 4967 */         arrayOfByte4 = CharsToBytes(this.charSet, arrayOfChar3);
/*      */     } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/*      */     }
/* 4970 */     procedures(paramLong, arrayOfByte2, paramString1 == null, arrayOfByte3, paramString2 == null, arrayOfByte4, paramString3 == null, arrayOfByte1);
/*      */ 
/* 4976 */     if (arrayOfByte1[0] != 0)
/*      */     {
/* 4980 */       standardError((short)arrayOfByte1[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLProcedureColumns(long paramLong, String paramString1, String paramString2, String paramString3, String paramString4)
/*      */     throws SQLException, SQLWarning
/*      */   {
/* 4999 */     if (this.tracer.isTracing()) {
/* 5000 */       this.tracer.trace("Procedure columns (SQLProcedureColumns), hStmt=" + paramLong + ", catalog=" + paramString1 + ", schema=" + paramString2 + ", procedure=" + paramString3 + ", column=" + paramString4);
/*      */     }
/*      */ 
/* 5005 */     byte[] arrayOfByte1 = new byte[1];
/*      */ 
/* 5007 */     byte[] arrayOfByte2 = null;
/* 5008 */     byte[] arrayOfByte3 = null;
/* 5009 */     byte[] arrayOfByte4 = null;
/* 5010 */     byte[] arrayOfByte5 = null;
/* 5011 */     char[] arrayOfChar1 = null;
/* 5012 */     char[] arrayOfChar2 = null;
/* 5013 */     char[] arrayOfChar3 = null;
/* 5014 */     char[] arrayOfChar4 = null;
/* 5015 */     if (paramString1 != null)
/* 5016 */       arrayOfChar1 = paramString1.toCharArray();
/* 5017 */     if (paramString2 != null)
/* 5018 */       arrayOfChar2 = paramString2.toCharArray();
/* 5019 */     if (paramString3 != null)
/* 5020 */       arrayOfChar3 = paramString3.toCharArray();
/* 5021 */     if (paramString4 != null)
/* 5022 */       arrayOfChar4 = paramString4.toCharArray();
/*      */     try {
/* 5024 */       if (paramString1 != null)
/* 5025 */         arrayOfByte2 = CharsToBytes(this.charSet, arrayOfChar1);
/* 5026 */       if (paramString2 != null)
/* 5027 */         arrayOfByte3 = CharsToBytes(this.charSet, arrayOfChar2);
/* 5028 */       if (paramString3 != null)
/* 5029 */         arrayOfByte4 = CharsToBytes(this.charSet, arrayOfChar3);
/* 5030 */       if (paramString4 != null)
/* 5031 */         arrayOfByte5 = CharsToBytes(this.charSet, arrayOfChar4);
/*      */     } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/*      */     }
/* 5034 */     procedureColumns(paramLong, arrayOfByte2, paramString1 == null, arrayOfByte3, paramString2 == null, arrayOfByte4, paramString3 == null, arrayOfByte5, paramString4 == null, arrayOfByte1);
/*      */ 
/* 5041 */     if (arrayOfByte1[0] != 0)
/*      */     {
/* 5045 */       standardError((short)arrayOfByte1[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public int SQLRowCount(long paramLong)
/*      */     throws SQLException, JdbcOdbcSQLWarning
/*      */   {
/* 5058 */     int i = 0;
/*      */ 
/* 5060 */     if (this.tracer.isTracing()) {
/* 5061 */       this.tracer.trace("Number of affected rows (SQLRowCount), hStmt=" + paramLong);
/*      */     }
/*      */ 
/* 5064 */     byte[] arrayOfByte = new byte[1];
/* 5065 */     i = rowCount(paramLong, arrayOfByte);
/*      */ 
/* 5067 */     if (arrayOfByte[0] != 0)
/*      */     {
/*      */       try
/*      */       {
/* 5071 */         standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */       }
/*      */       catch (JdbcOdbcSQLWarning localJdbcOdbcSQLWarning)
/*      */       {
/* 5077 */         if (this.tracer.isTracing()) {
/* 5078 */           this.tracer.trace("value=" + i);
/*      */         }
/*      */ 
/* 5084 */         localJdbcOdbcSQLWarning.value = BigDecimal.valueOf(i);
/*      */ 
/* 5088 */         throw localJdbcOdbcSQLWarning;
/*      */       }
/*      */     }
/* 5091 */     if (this.tracer.isTracing()) {
/* 5092 */       this.tracer.trace("value=" + i);
/*      */     }
/* 5094 */     return i;
/*      */   }
/*      */ 
/*      */   public void SQLSetConnectOption(long paramLong, short paramShort, int paramInt)
/*      */     throws SQLException
/*      */   {
/* 5109 */     if (this.tracer.isTracing()) {
/* 5110 */       this.tracer.trace("Setting connection option (SQLSetConnectOption), hDbc=" + paramLong + ", fOption=" + paramShort + ", vParam=" + paramInt);
/*      */     }
/*      */ 
/* 5114 */     byte[] arrayOfByte = new byte[1];
/* 5115 */     setConnectOption(paramLong, paramShort, paramInt, arrayOfByte);
/*      */ 
/* 5117 */     if (arrayOfByte[0] != 0)
/*      */     {
/* 5121 */       standardError((short)arrayOfByte[0], 0L, paramLong, 0L);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLSetConnectOption(long paramLong, short paramShort, String paramString)
/*      */     throws SQLException
/*      */   {
/* 5138 */     if (this.tracer.isTracing()) {
/* 5139 */       this.tracer.trace("Setting connection option string (SQLSetConnectOption), hDbc=" + paramLong + ", fOption=" + paramShort + ", vParam=" + paramString);
/*      */     }
/*      */ 
/* 5143 */     byte[] arrayOfByte1 = new byte[1];
/* 5144 */     byte[] arrayOfByte2 = null;
/* 5145 */     char[] arrayOfChar = null;
/* 5146 */     if (paramString != null)
/* 5147 */       arrayOfChar = paramString.toCharArray();
/*      */     try {
/* 5149 */       if (paramString != null)
/* 5150 */         arrayOfByte2 = CharsToBytes(this.charSet, arrayOfChar);
/*      */     } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/*      */     }
/* 5153 */     setConnectOptionString(paramLong, paramShort, arrayOfByte2, arrayOfByte1);
/*      */ 
/* 5155 */     if (arrayOfByte1[0] != 0)
/*      */     {
/* 5159 */       standardError((short)arrayOfByte1[0], 0L, paramLong, 0L);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLSetCursorName(long paramLong, String paramString)
/*      */     throws SQLException
/*      */   {
/* 5175 */     if (this.tracer.isTracing()) {
/* 5176 */       this.tracer.trace("Setting cursor name (SQLSetCursorName), hStmt=" + paramLong + ", szCursor=" + paramString);
/*      */     }
/*      */ 
/* 5180 */     byte[] arrayOfByte1 = new byte[1];
/*      */ 
/* 5182 */     byte[] arrayOfByte2 = null;
/* 5183 */     char[] arrayOfChar = null;
/* 5184 */     if (paramString != null)
/* 5185 */       arrayOfChar = paramString.toCharArray();
/*      */     try {
/* 5187 */       if (paramString != null)
/* 5188 */         arrayOfByte2 = CharsToBytes(this.charSet, arrayOfChar);
/*      */     } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/*      */     }
/* 5191 */     setCursorName(paramLong, arrayOfByte2, arrayOfByte1);
/*      */ 
/* 5193 */     if (arrayOfByte1[0] != 0)
/*      */     {
/* 5197 */       standardError((short)arrayOfByte1[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLSetStmtOption(long paramLong, short paramShort, int paramInt)
/*      */     throws SQLException
/*      */   {
/* 5214 */     if (this.tracer.isTracing()) {
/* 5215 */       this.tracer.trace("Setting statement option (SQLSetStmtOption), hStmt=" + paramLong + ", fOption=" + paramShort + ", vParam=" + paramInt);
/*      */     }
/*      */ 
/* 5219 */     byte[] arrayOfByte = new byte[1];
/* 5220 */     setStmtOption(paramLong, paramShort, paramInt, arrayOfByte);
/*      */ 
/* 5222 */     if (arrayOfByte[0] != 0)
/*      */     {
/* 5226 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLSetStmtAttr(long paramLong, int paramInt1, int paramInt2, int paramInt3)
/*      */     throws SQLException
/*      */   {
/* 5244 */     if (this.tracer.isTracing()) {
/* 5245 */       this.tracer.trace("Setting statement option (SQLSetStmtAttr), hStmt=" + paramLong + ", fOption=" + paramInt1 + ", vParam=" + paramInt2);
/*      */     }
/*      */ 
/* 5249 */     byte[] arrayOfByte = new byte[1];
/*      */ 
/* 5251 */     setStmtAttr(paramLong, paramInt1, paramInt2, paramInt3, arrayOfByte);
/*      */ 
/* 5253 */     if (arrayOfByte[0] != 0)
/*      */     {
/* 5257 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLSetStmtAttrPtr(long paramLong, int paramInt1, int[] paramArrayOfInt, int paramInt2, long[] paramArrayOfLong)
/*      */     throws SQLException
/*      */   {
/* 5278 */     if (this.tracer.isTracing()) {
/* 5279 */       this.tracer.trace("Setting statement option (SQLSetStmtAttr), hStmt=" + paramLong + ", fOption=" + paramInt1);
/*      */     }
/*      */ 
/* 5283 */     byte[] arrayOfByte = new byte[1];
/* 5284 */     setStmtAttrPtr(paramLong, paramInt1, paramArrayOfInt, paramInt2, arrayOfByte, paramArrayOfLong);
/*      */ 
/* 5286 */     if (arrayOfByte[0] != 0)
/*      */     {
/* 5290 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean SQLSetPos(long paramLong, int paramInt1, int paramInt2, int paramInt3)
/*      */     throws SQLException
/*      */   {
/* 5308 */     boolean bool = false;
/*      */ 
/* 5310 */     if (this.tracer.isTracing()) {
/* 5311 */       this.tracer.trace("Setting row position (SQLSetPos), hStmt=" + paramLong + ", operation = " + paramInt2);
/*      */     }
/*      */ 
/* 5315 */     byte[] arrayOfByte = new byte[1];
/*      */ 
/* 5317 */     setPos(paramLong, paramInt1, paramInt2, paramInt3, arrayOfByte);
/*      */ 
/* 5322 */     if (arrayOfByte[0] == 99) {
/* 5323 */       if (this.tracer.isTracing()) {
/* 5324 */         this.tracer.trace("SQL_NEED_DATA returned");
/*      */       }
/* 5326 */       bool = true;
/* 5327 */       arrayOfByte[0] = 0;
/*      */     }
/*      */ 
/* 5330 */     if (arrayOfByte[0] != 0)
/*      */     {
/* 5334 */       standardError((short)arrayOfByte[0], 0L, 0L, paramLong);
/*      */     }
/*      */ 
/* 5338 */     return bool;
/*      */   }
/*      */ 
/*      */   public void SQLSpecialColumns(long paramLong, short paramShort, String paramString1, String paramString2, String paramString3, int paramInt, boolean paramBoolean)
/*      */     throws SQLException, SQLWarning
/*      */   {
/* 5358 */     if (this.tracer.isTracing()) {
/* 5359 */       this.tracer.trace("Special columns (SQLSpecialColumns), hStmt=" + paramLong + ", fColType=" + paramShort + ",catalog=" + paramString1 + ", schema=" + paramString2 + ", table=" + paramString3 + ", fScope=" + paramInt + ", fNullable=" + paramBoolean);
/*      */     }
/*      */ 
/* 5365 */     byte[] arrayOfByte1 = new byte[1];
/*      */ 
/* 5367 */     byte[] arrayOfByte2 = null;
/* 5368 */     byte[] arrayOfByte3 = null;
/* 5369 */     byte[] arrayOfByte4 = null;
/* 5370 */     char[] arrayOfChar1 = null;
/* 5371 */     char[] arrayOfChar2 = null;
/* 5372 */     char[] arrayOfChar3 = null;
/* 5373 */     if (paramString1 != null)
/* 5374 */       arrayOfChar1 = paramString1.toCharArray();
/* 5375 */     if (paramString2 != null)
/* 5376 */       arrayOfChar2 = paramString2.toCharArray();
/* 5377 */     if (paramString3 != null)
/* 5378 */       arrayOfChar3 = paramString3.toCharArray();
/*      */     try {
/* 5380 */       if (paramString1 != null)
/* 5381 */         arrayOfByte2 = CharsToBytes(this.charSet, arrayOfChar1);
/* 5382 */       if (paramString2 != null)
/* 5383 */         arrayOfByte3 = CharsToBytes(this.charSet, arrayOfChar2);
/* 5384 */       if (paramString3 != null)
/* 5385 */         arrayOfByte4 = CharsToBytes(this.charSet, arrayOfChar3);
/*      */     } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/*      */     }
/* 5388 */     specialColumns(paramLong, paramShort, arrayOfByte2, paramString1 == null, arrayOfByte3, paramString2 == null, arrayOfByte4, paramString3 == null, paramInt, paramBoolean, arrayOfByte1);
/*      */ 
/* 5395 */     if (arrayOfByte1[0] != 0)
/*      */     {
/* 5399 */       standardError((short)arrayOfByte1[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLStatistics(long paramLong, String paramString1, String paramString2, String paramString3, boolean paramBoolean1, boolean paramBoolean2)
/*      */     throws SQLException, SQLWarning
/*      */   {
/* 5419 */     if (this.tracer.isTracing()) {
/* 5420 */       this.tracer.trace("Statistics (SQLStatistics), hStmt=" + paramLong + ",catalog=" + paramString1 + ", schema=" + paramString2 + ", table=" + paramString3 + ", unique=" + paramBoolean1 + ", approximate=" + paramBoolean2);
/*      */     }
/*      */ 
/* 5426 */     byte[] arrayOfByte1 = new byte[1];
/*      */ 
/* 5428 */     byte[] arrayOfByte2 = null;
/* 5429 */     byte[] arrayOfByte3 = null;
/* 5430 */     byte[] arrayOfByte4 = null;
/* 5431 */     char[] arrayOfChar1 = null;
/* 5432 */     char[] arrayOfChar2 = null;
/* 5433 */     char[] arrayOfChar3 = null;
/* 5434 */     if (paramString1 != null)
/* 5435 */       arrayOfChar1 = paramString1.toCharArray();
/* 5436 */     if (paramString2 != null)
/* 5437 */       arrayOfChar2 = paramString2.toCharArray();
/* 5438 */     if (paramString3 != null)
/* 5439 */       arrayOfChar3 = paramString3.toCharArray();
/*      */     try {
/* 5441 */       if (paramString1 != null)
/* 5442 */         arrayOfByte2 = CharsToBytes(this.charSet, arrayOfChar1);
/* 5443 */       if (paramString2 != null)
/* 5444 */         arrayOfByte3 = CharsToBytes(this.charSet, arrayOfChar2);
/* 5445 */       if (paramString3 != null)
/* 5446 */         arrayOfByte4 = CharsToBytes(this.charSet, arrayOfChar3);
/*      */     } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/*      */     }
/* 5449 */     statistics(paramLong, arrayOfByte2, paramString1 == null, arrayOfByte3, paramString2 == null, arrayOfByte4, paramString3 == null, paramBoolean1, paramBoolean2, arrayOfByte1);
/*      */ 
/* 5456 */     if (arrayOfByte1[0] != 0)
/*      */     {
/* 5460 */       standardError((short)arrayOfByte1[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLTables(long paramLong, String paramString1, String paramString2, String paramString3, String paramString4)
/*      */     throws SQLException, SQLWarning
/*      */   {
/* 5479 */     if (this.tracer.isTracing()) {
/* 5480 */       this.tracer.trace("Tables (SQLTables), hStmt=" + paramLong + ",catalog=" + paramString1 + ", schema=" + paramString2 + ", table=" + paramString3 + ", types=" + paramString4);
/*      */     }
/*      */ 
/* 5485 */     byte[] arrayOfByte1 = new byte[1];
/*      */ 
/* 5487 */     byte[] arrayOfByte2 = null;
/* 5488 */     byte[] arrayOfByte3 = null;
/* 5489 */     byte[] arrayOfByte4 = null;
/* 5490 */     byte[] arrayOfByte5 = null;
/* 5491 */     char[] arrayOfChar1 = null;
/* 5492 */     char[] arrayOfChar2 = null;
/* 5493 */     char[] arrayOfChar3 = null;
/* 5494 */     char[] arrayOfChar4 = null;
/* 5495 */     if (paramString1 != null)
/* 5496 */       arrayOfChar1 = paramString1.toCharArray();
/* 5497 */     if (paramString2 != null)
/* 5498 */       arrayOfChar2 = paramString2.toCharArray();
/* 5499 */     if (paramString3 != null)
/* 5500 */       arrayOfChar3 = paramString3.toCharArray();
/* 5501 */     if (paramString4 != null)
/* 5502 */       arrayOfChar4 = paramString4.toCharArray();
/*      */     try {
/* 5504 */       if (paramString1 != null)
/* 5505 */         arrayOfByte2 = CharsToBytes(this.charSet, arrayOfChar1);
/* 5506 */       if (paramString2 != null)
/* 5507 */         arrayOfByte3 = CharsToBytes(this.charSet, arrayOfChar2);
/* 5508 */       if (paramString3 != null)
/* 5509 */         arrayOfByte4 = CharsToBytes(this.charSet, arrayOfChar3);
/* 5510 */       if (paramString4 != null)
/* 5511 */         arrayOfByte5 = CharsToBytes(this.charSet, arrayOfChar4);
/*      */     } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/*      */     }
/* 5514 */     tables(paramLong, arrayOfByte2, paramString1 == null, arrayOfByte3, paramString2 == null, arrayOfByte4, paramString3 == null, arrayOfByte5, paramString4 == null, arrayOfByte1);
/*      */ 
/* 5521 */     if (arrayOfByte1[0] != 0)
/*      */     {
/* 5525 */       standardError((short)arrayOfByte1[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLTablePrivileges(long paramLong, String paramString1, String paramString2, String paramString3)
/*      */     throws SQLException, SQLWarning
/*      */   {
/* 5543 */     if (this.tracer.isTracing()) {
/* 5544 */       this.tracer.trace("Tables (SQLTables), hStmt=" + paramLong + ",catalog=" + paramString1 + ", schema=" + paramString2 + ", table=" + paramString3);
/*      */     }
/*      */ 
/* 5549 */     byte[] arrayOfByte1 = new byte[1];
/*      */ 
/* 5551 */     byte[] arrayOfByte2 = null;
/* 5552 */     byte[] arrayOfByte3 = null;
/* 5553 */     byte[] arrayOfByte4 = null;
/* 5554 */     char[] arrayOfChar1 = null;
/* 5555 */     char[] arrayOfChar2 = null;
/* 5556 */     char[] arrayOfChar3 = null;
/* 5557 */     if (paramString1 != null)
/* 5558 */       arrayOfChar1 = paramString1.toCharArray();
/* 5559 */     if (paramString2 != null)
/* 5560 */       arrayOfChar2 = paramString2.toCharArray();
/* 5561 */     if (paramString3 != null)
/* 5562 */       arrayOfChar3 = paramString3.toCharArray();
/*      */     try {
/* 5564 */       if (paramString1 != null)
/* 5565 */         arrayOfByte2 = CharsToBytes(this.charSet, arrayOfChar1);
/* 5566 */       if (paramString2 != null)
/* 5567 */         arrayOfByte3 = CharsToBytes(this.charSet, arrayOfChar2);
/* 5568 */       if (paramString3 != null)
/* 5569 */         arrayOfByte4 = CharsToBytes(this.charSet, arrayOfChar3);
/*      */     } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/*      */     }
/* 5572 */     tablePrivileges(paramLong, arrayOfByte2, paramString1 == null, arrayOfByte3, paramString2 == null, arrayOfByte4, paramString3 == null, arrayOfByte1);
/*      */ 
/* 5578 */     if (arrayOfByte1[0] != 0)
/*      */     {
/* 5582 */       standardError((short)arrayOfByte1[0], 0L, 0L, paramLong);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void SQLTransact(long paramLong1, long paramLong2, short paramShort)
/*      */     throws SQLException
/*      */   {
/* 5599 */     if (this.tracer.isTracing()) {
/* 5600 */       this.tracer.trace("Transaction (SQLTransact), hEnv=" + paramLong1 + ", hDbc=" + paramLong2 + ", fType=" + paramShort);
/*      */     }
/*      */ 
/* 5604 */     byte[] arrayOfByte = new byte[1];
/* 5605 */     transact(paramLong1, paramLong2, paramShort, arrayOfByte);
/*      */ 
/* 5607 */     if (arrayOfByte[0] != 0)
/* 5608 */       throwGenericSQLException();
/*      */   }
/*      */ 
/*      */   public native int bufferToInt(byte[] paramArrayOfByte);
/*      */ 
/*      */   public native float bufferToFloat(byte[] paramArrayOfByte);
/*      */ 
/*      */   public native double bufferToDouble(byte[] paramArrayOfByte);
/*      */ 
/*      */   public native long bufferToLong(byte[] paramArrayOfByte);
/*      */ 
/*      */   public native void convertDateString(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);
/*      */ 
/*      */   public native void getDateStruct(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3);
/*      */ 
/*      */   public native void convertTimeString(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);
/*      */ 
/*      */   public native void getTimeStruct(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3);
/*      */ 
/*      */   public native void getTimestampStruct(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, long paramLong);
/*      */ 
/*      */   public native void convertTimestampString(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);
/*      */ 
/*      */   public static native int getSQLLENSize();
/*      */ 
/*      */   public static native void intToBytes(int paramInt, byte[] paramArrayOfByte);
/*      */ 
/*      */   public static native void longToBytes(long paramLong, byte[] paramArrayOfByte);
/*      */ 
/*      */   public static native void intTo4Bytes(int paramInt, byte[] paramArrayOfByte);
/*      */ 
/*      */   public static SQLWarning convertWarning(JdbcOdbcSQLWarning paramJdbcOdbcSQLWarning)
/*      */   {
/* 5670 */     Object localObject = paramJdbcOdbcSQLWarning;
/*      */ 
/* 5675 */     if (paramJdbcOdbcSQLWarning.getSQLState().equals("01004")) {
/* 5676 */       DataTruncation localDataTruncation = new DataTruncation(-1, false, true, 0, 0);
/*      */ 
/* 5678 */       localObject = localDataTruncation;
/*      */     }
/* 5680 */     return localObject;
/*      */   }
/*      */ 
/*      */   protected native long allocConnect(long paramLong, byte[] paramArrayOfByte);
/*      */ 
/*      */   protected native long allocEnv(byte[] paramArrayOfByte);
/*      */ 
/*      */   protected native long allocStmt(long paramLong, byte[] paramArrayOfByte);
/*      */ 
/*      */   protected native void cancel(long paramLong, byte[] paramArrayOfByte);
/*      */ 
/*      */   protected native void bindColAtExec(long paramLong, int paramInt1, int paramInt2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, long[] paramArrayOfLong, byte[] paramArrayOfByte3);
/*      */ 
/*      */   protected native void bindColBinary(long paramLong, int paramInt1, Object[] paramArrayOfObject, byte[] paramArrayOfByte1, int paramInt2, byte[] paramArrayOfByte2, long[] paramArrayOfLong, byte[] paramArrayOfByte3);
/*      */ 
/*      */   protected native void bindColDate(long paramLong, int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, long[] paramArrayOfLong, byte[] paramArrayOfByte3);
/*      */ 
/*      */   protected native void bindColDefault(long paramLong, int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3);
/*      */ 
/*      */   protected native void bindColDouble(long paramLong, int paramInt, double[] paramArrayOfDouble, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, long[] paramArrayOfLong, byte[] paramArrayOfByte3);
/*      */ 
/*      */   protected native void bindColFloat(long paramLong, int paramInt, float[] paramArrayOfFloat, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, long[] paramArrayOfLong, byte[] paramArrayOfByte3);
/*      */ 
/*      */   protected native void bindColInteger(long paramLong, int paramInt, int[] paramArrayOfInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, long[] paramArrayOfLong, byte[] paramArrayOfByte3);
/*      */ 
/*      */   protected native void bindColString(long paramLong, int paramInt1, int paramInt2, Object[] paramArrayOfObject, int paramInt3, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, long[] paramArrayOfLong, byte[] paramArrayOfByte3);
/*      */ 
/*      */   protected native void bindColTime(long paramLong, int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, long[] paramArrayOfLong, byte[] paramArrayOfByte3);
/*      */ 
/*      */   protected native void bindColTimestamp(long paramLong, int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3, int[] paramArrayOfInt4, int[] paramArrayOfInt5, int[] paramArrayOfInt6, int[] paramArrayOfInt7, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, long[] paramArrayOfLong, byte[] paramArrayOfByte3);
/*      */ 
/*      */   protected native void bindInParameterAtExec(long paramLong, int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, long[] paramArrayOfLong);
/*      */ 
/*      */   protected native void bindInOutParameterAtExec(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte1, int paramInt5, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, long[] paramArrayOfLong);
/*      */ 
/*      */   protected native void bindInParameterBinary(long paramLong, int paramInt1, int paramInt2, byte[] paramArrayOfByte1, int paramInt3, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, byte[] paramArrayOfByte4, long[] paramArrayOfLong);
/*      */ 
/*      */   protected native void bindInParameterDate(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, long[] paramArrayOfLong);
/*      */ 
/*      */   protected native void bindInParameterDouble(long paramLong, int paramInt1, int paramInt2, int paramInt3, double paramDouble, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, long[] paramArrayOfLong);
/*      */ 
/*      */   protected native void bindInParameterFloat(long paramLong, int paramInt1, int paramInt2, int paramInt3, double paramDouble, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, long[] paramArrayOfLong);
/*      */ 
/*      */   protected native void bindInParameterBigint(long paramLong1, int paramInt1, int paramInt2, int paramInt3, long paramLong2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, long[] paramArrayOfLong);
/*      */ 
/*      */   protected native void bindInParameterInteger(long paramLong, int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, long[] paramArrayOfLong);
/*      */ 
/*      */   protected native void bindInParameterNull(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, long[] paramArrayOfLong);
/*      */ 
/*      */   protected native void bindInParameterString(long paramLong, int paramInt1, int paramInt2, byte[] paramArrayOfByte1, int paramInt3, int paramInt4, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, long[] paramArrayOfLong);
/*      */ 
/*      */   protected native void bindInParameterTime(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, long[] paramArrayOfLong);
/*      */ 
/*      */   protected native void bindInParameterTimestamp(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, long[] paramArrayOfLong);
/*      */ 
/*      */   protected native void bindOutParameterString(long paramLong, int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, long[] paramArrayOfLong);
/*      */ 
/*      */   protected native void bindInOutParameterDate(long paramLong, int paramInt1, int paramInt2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, long[] paramArrayOfLong);
/*      */ 
/*      */   protected native void bindInOutParameterTime(long paramLong, int paramInt1, int paramInt2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, long[] paramArrayOfLong);
/*      */ 
/*      */   protected native void bindInOutParameterString(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, long[] paramArrayOfLong);
/*      */ 
/*      */   protected native void bindInOutParameterStr(long paramLong, int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, long[] paramArrayOfLong, int paramInt4);
/*      */ 
/*      */   protected native void bindInOutParameterBin(long paramLong, int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, long[] paramArrayOfLong, int paramInt4);
/*      */ 
/*      */   protected native void bindInOutParameterBinary(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, long[] paramArrayOfLong);
/*      */ 
/*      */   protected native void bindInOutParameterFixed(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, long[] paramArrayOfLong);
/*      */ 
/*      */   protected native void bindInOutParameterTimeStamp(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, long[] paramArrayOfLong);
/*      */ 
/*      */   protected native void bindInOutParameter(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4, double paramDouble, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, long[] paramArrayOfLong);
/*      */ 
/*      */   protected native void bindInOutParameterNull(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, long[] paramArrayOfLong);
/*      */ 
/*      */   protected native void bindInOutParameterTimestamp(long paramLong, int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, long[] paramArrayOfLong);
/*      */ 
/*      */   protected native void bindInParameterStringArray(long paramLong, int paramInt1, int paramInt2, Object[] paramArrayOfObject, byte[] paramArrayOfByte1, int paramInt3, int paramInt4, int[] paramArrayOfInt, byte[] paramArrayOfByte2);
/*      */ 
/*      */   protected native void bindInParameterIntegerArray(long paramLong, int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2, byte[] paramArrayOfByte);
/*      */ 
/*      */   protected native void bindInParameterFloatArray(long paramLong, int paramInt1, int paramInt2, int paramInt3, float[] paramArrayOfFloat, int[] paramArrayOfInt, byte[] paramArrayOfByte);
/*      */ 
/*      */   protected native void bindInParameterDoubleArray(long paramLong, int paramInt1, int paramInt2, int paramInt3, double[] paramArrayOfDouble, int[] paramArrayOfInt, byte[] paramArrayOfByte);
/*      */ 
/*      */   protected native void bindInParameterDateArray(long paramLong, int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int[] paramArrayOfInt4);
/*      */ 
/*      */   protected native void bindInParameterTimeArray(long paramLong, int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int[] paramArrayOfInt4);
/*      */ 
/*      */   protected native void bindInParameterTimestampArray(long paramLong, int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3, int[] paramArrayOfInt4, int[] paramArrayOfInt5, int[] paramArrayOfInt6, int[] paramArrayOfInt7, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int[] paramArrayOfInt8);
/*      */ 
/*      */   protected native void bindInParameterBinaryArray(long paramLong, int paramInt1, int paramInt2, Object[] paramArrayOfObject, int paramInt3, byte[] paramArrayOfByte1, int[] paramArrayOfInt, byte[] paramArrayOfByte2);
/*      */ 
/*      */   protected native void bindInParameterAtExecArray(long paramLong, int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte1, int[] paramArrayOfInt, byte[] paramArrayOfByte2);
/*      */ 
/*      */   protected native void bindOutParameterNull(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, long[] paramArrayOfLong);
/*      */ 
/*      */   protected native void bindOutParameterFixed(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, long[] paramArrayOfLong);
/*      */ 
/*      */   protected native void bindOutParameterBinary(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, long[] paramArrayOfLong);
/*      */ 
/*      */   protected native void bindOutParameterDate(long paramLong, int paramInt1, int paramInt2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, long[] paramArrayOfLong);
/*      */ 
/*      */   protected native void bindOutParameterTime(long paramLong, int paramInt1, int paramInt2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, long[] paramArrayOfLong);
/*      */ 
/*      */   protected native void bindOutParameterTimestamp(long paramLong, int paramInt1, int paramInt2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, long[] paramArrayOfLong);
/*      */ 
/*      */   protected native void browseConnect(long paramLong, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3);
/*      */ 
/*      */   protected native int colAttributes(long paramLong, int paramInt1, int paramInt2, byte[] paramArrayOfByte);
/*      */ 
/*      */   protected native void colAttributesString(long paramLong, int paramInt1, int paramInt2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);
/*      */ 
/*      */   protected native void columns(long paramLong, byte[] paramArrayOfByte1, boolean paramBoolean1, byte[] paramArrayOfByte2, boolean paramBoolean2, byte[] paramArrayOfByte3, boolean paramBoolean3, byte[] paramArrayOfByte4, boolean paramBoolean4, byte[] paramArrayOfByte5);
/*      */ 
/*      */   protected native void columnPrivileges(long paramLong, byte[] paramArrayOfByte1, boolean paramBoolean1, byte[] paramArrayOfByte2, boolean paramBoolean2, byte[] paramArrayOfByte3, boolean paramBoolean3, byte[] paramArrayOfByte4, boolean paramBoolean4, byte[] paramArrayOfByte5);
/*      */ 
/*      */   protected native int describeParam(long paramLong, int paramInt1, int paramInt2, byte[] paramArrayOfByte);
/*      */ 
/*      */   protected native void disconnect(long paramLong, byte[] paramArrayOfByte);
/*      */ 
/*      */   protected native void driverConnect(long paramLong, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);
/*      */ 
/*      */   protected native int error(long paramLong1, long paramLong2, long paramLong3, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3);
/*      */ 
/*      */   protected native void execDirect(long paramLong, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);
/*      */ 
/*      */   protected native void execute(long paramLong, byte[] paramArrayOfByte);
/*      */ 
/*      */   protected native void fetch(long paramLong, byte[] paramArrayOfByte);
/*      */ 
/*      */   protected native void fetchScroll(long paramLong, short paramShort, int paramInt, byte[] paramArrayOfByte);
/*      */ 
/*      */   protected native void foreignKeys(long paramLong, byte[] paramArrayOfByte1, boolean paramBoolean1, byte[] paramArrayOfByte2, boolean paramBoolean2, byte[] paramArrayOfByte3, boolean paramBoolean3, byte[] paramArrayOfByte4, boolean paramBoolean4, byte[] paramArrayOfByte5, boolean paramBoolean5, byte[] paramArrayOfByte6, boolean paramBoolean6, byte[] paramArrayOfByte7);
/*      */ 
/*      */   protected native void freeConnect(long paramLong, byte[] paramArrayOfByte);
/*      */ 
/*      */   protected native void freeEnv(long paramLong, byte[] paramArrayOfByte);
/*      */ 
/*      */   protected native void freeStmt(long paramLong, int paramInt, byte[] paramArrayOfByte);
/*      */ 
/*      */   protected native long getConnectOption(long paramLong, short paramShort, byte[] paramArrayOfByte);
/*      */ 
/*      */   protected native void getConnectOptionString(long paramLong, short paramShort, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);
/*      */ 
/*      */   protected native void getCursorName(long paramLong, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);
/*      */ 
/*      */   protected native long getStmtOption(long paramLong, short paramShort, byte[] paramArrayOfByte);
/*      */ 
/*      */   protected native int getStmtAttr(long paramLong, int paramInt, byte[] paramArrayOfByte);
/*      */ 
/*      */   protected native int getDataBinary(long paramLong, int paramInt1, int paramInt2, byte[] paramArrayOfByte1, int paramInt3, byte[] paramArrayOfByte2);
/*      */ 
/*      */   protected native double getDataDouble(long paramLong, int paramInt, byte[] paramArrayOfByte);
/*      */ 
/*      */   protected native double getDataFloat(long paramLong, int paramInt, byte[] paramArrayOfByte);
/*      */ 
/*      */   protected native int getDataInteger(long paramLong, int paramInt, byte[] paramArrayOfByte);
/*      */ 
/*      */   protected native int getDataString(long paramLong, int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);
/*      */ 
/*      */   protected native void getDataStringDate(long paramLong, int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);
/*      */ 
/*      */   protected native void getDataStringTime(long paramLong, int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);
/*      */ 
/*      */   protected native void getDataStringTimestamp(long paramLong, int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);
/*      */ 
/*      */   protected native int getInfo(long paramLong, short paramShort, byte[] paramArrayOfByte);
/*      */ 
/*      */   protected native int getInfoShort(long paramLong, short paramShort, byte[] paramArrayOfByte);
/*      */ 
/*      */   protected native void getInfoString(long paramLong, short paramShort, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);
/*      */ 
/*      */   protected native void getTypeInfo(long paramLong, short paramShort, byte[] paramArrayOfByte);
/*      */ 
/*      */   protected native void moreResults(long paramLong, byte[] paramArrayOfByte);
/*      */ 
/*      */   protected native void nativeSql(long paramLong, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3);
/*      */ 
/*      */   protected native int numParams(long paramLong, byte[] paramArrayOfByte);
/*      */ 
/*      */   protected native int numResultCols(long paramLong, byte[] paramArrayOfByte);
/*      */ 
/*      */   protected native int paramData(long paramLong, byte[] paramArrayOfByte);
/*      */ 
/*      */   protected native int paramDataInBlock(long paramLong, int paramInt, byte[] paramArrayOfByte);
/*      */ 
/*      */   protected native void prepare(long paramLong, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);
/*      */ 
/*      */   protected native void primaryKeys(long paramLong, byte[] paramArrayOfByte1, boolean paramBoolean1, byte[] paramArrayOfByte2, boolean paramBoolean2, byte[] paramArrayOfByte3, boolean paramBoolean3, byte[] paramArrayOfByte4);
/*      */ 
/*      */   protected native void procedures(long paramLong, byte[] paramArrayOfByte1, boolean paramBoolean1, byte[] paramArrayOfByte2, boolean paramBoolean2, byte[] paramArrayOfByte3, boolean paramBoolean3, byte[] paramArrayOfByte4);
/*      */ 
/*      */   protected native void procedureColumns(long paramLong, byte[] paramArrayOfByte1, boolean paramBoolean1, byte[] paramArrayOfByte2, boolean paramBoolean2, byte[] paramArrayOfByte3, boolean paramBoolean3, byte[] paramArrayOfByte4, boolean paramBoolean4, byte[] paramArrayOfByte5);
/*      */ 
/*      */   protected native void putData(long paramLong, byte[] paramArrayOfByte1, int paramInt, byte[] paramArrayOfByte2);
/*      */ 
/*      */   protected native int rowCount(long paramLong, byte[] paramArrayOfByte);
/*      */ 
/*      */   protected native void setConnectOption(long paramLong, short paramShort, int paramInt, byte[] paramArrayOfByte);
/*      */ 
/*      */   protected native void setConnectOptionString(long paramLong, short paramShort, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);
/*      */ 
/*      */   protected native void setCursorName(long paramLong, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);
/*      */ 
/*      */   protected native void setStmtOption(long paramLong, short paramShort, int paramInt, byte[] paramArrayOfByte);
/*      */ 
/*      */   protected native void setStmtAttr(long paramLong, int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte);
/*      */ 
/*      */   protected native void setStmtAttrPtr(long paramLong, int paramInt1, int[] paramArrayOfInt, int paramInt2, byte[] paramArrayOfByte, long[] paramArrayOfLong);
/*      */ 
/*      */   protected native void setPos(long paramLong, int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte);
/*      */ 
/*      */   protected native void specialColumns(long paramLong, short paramShort, byte[] paramArrayOfByte1, boolean paramBoolean1, byte[] paramArrayOfByte2, boolean paramBoolean2, byte[] paramArrayOfByte3, boolean paramBoolean3, int paramInt, boolean paramBoolean4, byte[] paramArrayOfByte4);
/*      */ 
/*      */   protected native void statistics(long paramLong, byte[] paramArrayOfByte1, boolean paramBoolean1, byte[] paramArrayOfByte2, boolean paramBoolean2, byte[] paramArrayOfByte3, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5, byte[] paramArrayOfByte4);
/*      */ 
/*      */   protected native void tables(long paramLong, byte[] paramArrayOfByte1, boolean paramBoolean1, byte[] paramArrayOfByte2, boolean paramBoolean2, byte[] paramArrayOfByte3, boolean paramBoolean3, byte[] paramArrayOfByte4, boolean paramBoolean4, byte[] paramArrayOfByte5);
/*      */ 
/*      */   protected native void tablePrivileges(long paramLong, byte[] paramArrayOfByte1, boolean paramBoolean1, byte[] paramArrayOfByte2, boolean paramBoolean2, byte[] paramArrayOfByte3, boolean paramBoolean3, byte[] paramArrayOfByte4);
/*      */ 
/*      */   protected native void transact(long paramLong1, long paramLong2, short paramShort, byte[] paramArrayOfByte);
/*      */ 
/*      */   protected static native void ReleaseStoredBytes(long paramLong1, long paramLong2);
/*      */ 
/*      */   protected static native void ReleaseStoredChars(long paramLong1, long paramLong2);
/*      */ 
/*      */   protected static native void ReleaseStoredIntegers(long paramLong1, long paramLong2);
/*      */ 
/*      */   SQLException createSQLException(long paramLong1, long paramLong2, long paramLong3)
/*      */   {
/* 6931 */     int j = 0;
/* 6932 */     Object localObject1 = null;
/* 6933 */     Object localObject2 = null;
/*      */ 
/* 6935 */     if (this.tracer.isTracing())
/* 6936 */       this.tracer.trace("ERROR - Generating SQLException...");
/*      */     Object localObject3;
/*      */     String str1;
/* 6939 */     while (j == 0) {
/* 6940 */       byte[] arrayOfByte3 = new byte[1];
/* 6941 */       byte[] arrayOfByte1 = new byte[6];
/* 6942 */       byte[] arrayOfByte2 = new byte[300];
/*      */ 
/* 6945 */       int i = error(paramLong1, paramLong2, paramLong3, arrayOfByte1, arrayOfByte2, arrayOfByte3);
/*      */ 
/* 6948 */       if (arrayOfByte3[0] != 0) {
/* 6949 */         j = 1;
/*      */       }
/*      */       else {
/* 6952 */         localObject3 = null;
/*      */ 
/* 6954 */         str1 = new String();
/* 6955 */         String str2 = new String();
/*      */         try {
/* 6957 */           str1 = BytesToChars(this.charSet, arrayOfByte2);
/* 6958 */           str2 = BytesToChars(this.charSet, arrayOfByte1);
/*      */         }
/*      */         catch (UnsupportedEncodingException localUnsupportedEncodingException)
/*      */         {
/*      */         }
/*      */ 
/* 6964 */         localObject3 = new SQLException(str1.trim(), str2.trim(), i);
/*      */ 
/* 6971 */         if (localObject1 == null) {
/* 6972 */           localObject1 = localObject3;
/*      */         }
/*      */         else {
/* 6975 */           localObject2.setNextException((SQLException)localObject3);
/*      */         }
/*      */ 
/* 6979 */         localObject2 = localObject3;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 6986 */     if (localObject1 == null) {
/* 6987 */       localObject3 = "General error";
/* 6988 */       str1 = "S1000";
/*      */ 
/* 6990 */       if (this.tracer.isTracing()) {
/* 6991 */         this.tracer.trace("ERROR - " + str1 + " " + (String)localObject3);
/*      */       }
/* 6993 */       localObject1 = new SQLException((String)localObject3, str1);
/*      */     }
/* 6995 */     return localObject1;
/*      */   }
/*      */ 
/*      */   SQLWarning createSQLWarning(long paramLong1, long paramLong2, long paramLong3)
/*      */   {
/* 7014 */     int j = 0;
/* 7015 */     Object localObject1 = null;
/* 7016 */     Object localObject2 = null;
/*      */ 
/* 7018 */     if (this.tracer.isTracing())
/* 7019 */       this.tracer.trace("WARNING - Generating SQLWarning...");
/*      */     Object localObject3;
/*      */     String str1;
/* 7022 */     while (j == 0) {
/* 7023 */       byte[] arrayOfByte3 = new byte[1];
/* 7024 */       byte[] arrayOfByte1 = new byte[6];
/* 7025 */       byte[] arrayOfByte2 = new byte[300];
/*      */ 
/* 7028 */       int i = error(paramLong1, paramLong2, paramLong3, arrayOfByte1, arrayOfByte2, arrayOfByte3);
/*      */ 
/* 7031 */       if (arrayOfByte3[0] != 0) {
/* 7032 */         j = 1;
/*      */       }
/*      */       else {
/* 7035 */         localObject3 = null;
/* 7036 */         str1 = new String();
/* 7037 */         String str2 = new String();
/*      */         try {
/* 7039 */           str1 = BytesToChars(this.charSet, arrayOfByte2);
/* 7040 */           str2 = BytesToChars(this.charSet, arrayOfByte1);
/*      */         }
/*      */         catch (UnsupportedEncodingException localUnsupportedEncodingException)
/*      */         {
/*      */         }
/* 7045 */         localObject3 = new JdbcOdbcSQLWarning(str1.trim(), str2.trim(), i);
/*      */ 
/* 7053 */         if (localObject1 == null) {
/* 7054 */           localObject1 = localObject3;
/*      */         }
/*      */         else {
/* 7057 */           localObject2.setNextWarning((SQLWarning)localObject3);
/*      */         }
/*      */ 
/* 7061 */         localObject2 = localObject3;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 7068 */     if (localObject1 == null) {
/* 7069 */       localObject3 = "General warning";
/* 7070 */       str1 = "S1000";
/*      */ 
/* 7072 */       if (this.tracer.isTracing()) {
/* 7073 */         this.tracer.trace("WARNING - " + str1 + " " + (String)localObject3);
/*      */       }
/* 7075 */       localObject1 = new JdbcOdbcSQLWarning((String)localObject3, str1);
/*      */     }
/* 7077 */     return localObject1;
/*      */   }
/*      */ 
/*      */   void throwGenericSQLException()
/*      */     throws SQLException
/*      */   {
/* 7087 */     String str1 = "General error";
/* 7088 */     String str2 = "S1000";
/*      */ 
/* 7090 */     if (this.tracer.isTracing()) {
/* 7091 */       this.tracer.trace("ERROR - " + str2 + " " + str1);
/*      */     }
/* 7093 */     throw new SQLException(str1, str2);
/*      */   }
/*      */ 
/*      */   void standardError(short paramShort, long paramLong1, long paramLong2, long paramLong3)
/*      */     throws SQLException, SQLWarning
/*      */   {
/* 7111 */     if (this.tracer.isTracing())
/* 7112 */       this.tracer.trace("RETCODE = " + paramShort);
/*      */     String str;
/* 7115 */     switch (paramShort)
/*      */     {
/*      */     case -1:
/* 7121 */       throw createSQLException(paramLong1, paramLong2, paramLong3);
/*      */     case 1:
/* 7127 */       throw createSQLWarning(paramLong1, paramLong2, paramLong3);
/*      */     case -2:
/* 7132 */       str = "Invalid handle";
/* 7133 */       if (this.tracer.isTracing()) {
/* 7134 */         this.tracer.trace("ERROR - " + str);
/*      */       }
/* 7136 */       throw new SQLException(str);
/*      */     case 100:
/* 7141 */       str = "No data found";
/* 7142 */       if (this.tracer.isTracing()) {
/* 7143 */         this.tracer.trace("ERROR - " + str);
/*      */       }
/* 7145 */       throw new SQLException(str);
/*      */     }
/*      */ 
/* 7151 */     throwGenericSQLException();
/*      */   }
/*      */ 
/*      */   public JdbcOdbcTracer getTracer()
/*      */   {
/* 7161 */     return this.tracer;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.jdbc.odbc.JdbcOdbc
 * JD-Core Version:    0.6.2
 */