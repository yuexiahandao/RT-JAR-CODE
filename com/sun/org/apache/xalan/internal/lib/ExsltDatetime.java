/*      */ package com.sun.org.apache.xalan.internal.lib;
/*      */ 
/*      */ import com.sun.org.apache.xpath.internal.objects.XBoolean;
/*      */ import com.sun.org.apache.xpath.internal.objects.XNumber;
/*      */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*      */ import java.io.PrintStream;
/*      */ import java.text.ParseException;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.Calendar;
/*      */ import java.util.Date;
/*      */ import java.util.Locale;
/*      */ import java.util.TimeZone;
/*      */ 
/*      */ public class ExsltDatetime
/*      */ {
/*      */   static final String dt = "yyyy-MM-dd'T'HH:mm:ss";
/*      */   static final String d = "yyyy-MM-dd";
/*      */   static final String gym = "yyyy-MM";
/*      */   static final String gy = "yyyy";
/*      */   static final String gmd = "--MM-dd";
/*      */   static final String gm = "--MM--";
/*      */   static final String gd = "---dd";
/*      */   static final String t = "HH:mm:ss";
/*      */   static final String EMPTY_STR = "";
/*      */ 
/*      */   public static String dateTime()
/*      */   {
/*   80 */     Calendar cal = Calendar.getInstance();
/*   81 */     Date datetime = cal.getTime();
/*      */ 
/*   83 */     SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
/*      */ 
/*   85 */     StringBuffer buff = new StringBuffer(dateFormat.format(datetime));
/*      */ 
/*   88 */     int offset = cal.get(15) + cal.get(16);
/*      */ 
/*   91 */     if (offset == 0) {
/*   92 */       buff.append("Z");
/*      */     }
/*      */     else
/*      */     {
/*   96 */       int hrs = offset / 3600000;
/*      */ 
/*   98 */       int min = offset % 3600000;
/*   99 */       char posneg = hrs < 0 ? '-' : '+';
/*  100 */       buff.append(posneg).append(formatDigits(hrs)).append(':').append(formatDigits(min));
/*      */     }
/*  102 */     return buff.toString();
/*      */   }
/*      */ 
/*      */   private static String formatDigits(int q)
/*      */   {
/*  112 */     String dd = String.valueOf(Math.abs(q));
/*  113 */     return dd.length() == 1 ? '0' + dd : dd;
/*      */   }
/*      */ 
/*      */   public static String date(String datetimeIn)
/*      */     throws ParseException
/*      */   {
/*  140 */     String[] edz = getEraDatetimeZone(datetimeIn);
/*  141 */     String leader = edz[0];
/*  142 */     String datetime = edz[1];
/*  143 */     String zone = edz[2];
/*  144 */     if ((datetime == null) || (zone == null)) {
/*  145 */       return "";
/*      */     }
/*  147 */     String[] formatsIn = { "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd" };
/*  148 */     String formatOut = "yyyy-MM-dd";
/*  149 */     Date date = testFormats(datetime, formatsIn);
/*  150 */     if (date == null) return "";
/*      */ 
/*  152 */     SimpleDateFormat dateFormat = new SimpleDateFormat(formatOut);
/*  153 */     dateFormat.setLenient(false);
/*  154 */     String dateOut = dateFormat.format(date);
/*  155 */     if (dateOut.length() == 0) {
/*  156 */       return "";
/*      */     }
/*  158 */     return leader + dateOut + zone;
/*      */   }
/*      */ 
/*      */   public static String date()
/*      */   {
/*  167 */     String datetime = dateTime().toString();
/*  168 */     String date = datetime.substring(0, datetime.indexOf("T"));
/*  169 */     String zone = datetime.substring(getZoneStart(datetime));
/*  170 */     return date + zone;
/*      */   }
/*      */ 
/*      */   public static String time(String timeIn)
/*      */     throws ParseException
/*      */   {
/*  198 */     String[] edz = getEraDatetimeZone(timeIn);
/*  199 */     String time = edz[1];
/*  200 */     String zone = edz[2];
/*  201 */     if ((time == null) || (zone == null)) {
/*  202 */       return "";
/*      */     }
/*  204 */     String[] formatsIn = { "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd", "HH:mm:ss" };
/*  205 */     String formatOut = "HH:mm:ss";
/*  206 */     Date date = testFormats(time, formatsIn);
/*  207 */     if (date == null) return "";
/*  208 */     SimpleDateFormat dateFormat = new SimpleDateFormat(formatOut);
/*  209 */     String out = dateFormat.format(date);
/*  210 */     return out + zone;
/*      */   }
/*      */ 
/*      */   public static String time()
/*      */   {
/*  218 */     String datetime = dateTime().toString();
/*  219 */     String time = datetime.substring(datetime.indexOf("T") + 1);
/*      */ 
/*  227 */     return time;
/*      */   }
/*      */ 
/*      */   public static double year(String datetimeIn)
/*      */     throws ParseException
/*      */   {
/*  248 */     String[] edz = getEraDatetimeZone(datetimeIn);
/*  249 */     boolean ad = edz[0].length() == 0;
/*  250 */     String datetime = edz[1];
/*  251 */     if (datetime == null) {
/*  252 */       return (0.0D / 0.0D);
/*      */     }
/*  254 */     String[] formats = { "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd", "yyyy-MM", "yyyy" };
/*  255 */     double yr = getNumber(datetime, formats, 1);
/*  256 */     if ((ad) || (yr == (0.0D / 0.0D))) {
/*  257 */       return yr;
/*      */     }
/*  259 */     return -yr;
/*      */   }
/*      */ 
/*      */   public static double year()
/*      */   {
/*  267 */     Calendar cal = Calendar.getInstance();
/*  268 */     return cal.get(1);
/*      */   }
/*      */ 
/*      */   public static double monthInYear(String datetimeIn)
/*      */     throws ParseException
/*      */   {
/*  290 */     String[] edz = getEraDatetimeZone(datetimeIn);
/*  291 */     String datetime = edz[1];
/*  292 */     if (datetime == null) {
/*  293 */       return (0.0D / 0.0D);
/*      */     }
/*  295 */     String[] formats = { "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd", "yyyy-MM", "--MM--", "--MM-dd" };
/*  296 */     return getNumber(datetime, formats, 2) + 1.0D;
/*      */   }
/*      */ 
/*      */   public static double monthInYear()
/*      */   {
/*  304 */     Calendar cal = Calendar.getInstance();
/*  305 */     return cal.get(2) + 1;
/*      */   }
/*      */ 
/*      */   public static double weekInYear(String datetimeIn)
/*      */     throws ParseException
/*      */   {
/*  324 */     String[] edz = getEraDatetimeZone(datetimeIn);
/*  325 */     String datetime = edz[1];
/*  326 */     if (datetime == null) {
/*  327 */       return (0.0D / 0.0D);
/*      */     }
/*  329 */     String[] formats = { "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd" };
/*  330 */     return getNumber(datetime, formats, 3);
/*      */   }
/*      */ 
/*      */   public static double weekInYear()
/*      */   {
/*  338 */     Calendar cal = Calendar.getInstance();
/*  339 */     return cal.get(3);
/*      */   }
/*      */ 
/*      */   public static double dayInYear(String datetimeIn)
/*      */     throws ParseException
/*      */   {
/*  358 */     String[] edz = getEraDatetimeZone(datetimeIn);
/*  359 */     String datetime = edz[1];
/*  360 */     if (datetime == null) {
/*  361 */       return (0.0D / 0.0D);
/*      */     }
/*  363 */     String[] formats = { "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd" };
/*  364 */     return getNumber(datetime, formats, 6);
/*      */   }
/*      */ 
/*      */   public static double dayInYear()
/*      */   {
/*  372 */     Calendar cal = Calendar.getInstance();
/*  373 */     return cal.get(6);
/*      */   }
/*      */ 
/*      */   public static double dayInMonth(String datetimeIn)
/*      */     throws ParseException
/*      */   {
/*  395 */     String[] edz = getEraDatetimeZone(datetimeIn);
/*  396 */     String datetime = edz[1];
/*  397 */     String[] formats = { "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd", "--MM-dd", "---dd" };
/*  398 */     double day = getNumber(datetime, formats, 5);
/*  399 */     return day;
/*      */   }
/*      */ 
/*      */   public static double dayInMonth()
/*      */   {
/*  407 */     Calendar cal = Calendar.getInstance();
/*  408 */     return cal.get(5);
/*      */   }
/*      */ 
/*      */   public static double dayOfWeekInMonth(String datetimeIn)
/*      */     throws ParseException
/*      */   {
/*  428 */     String[] edz = getEraDatetimeZone(datetimeIn);
/*  429 */     String datetime = edz[1];
/*  430 */     if (datetime == null) {
/*  431 */       return (0.0D / 0.0D);
/*      */     }
/*  433 */     String[] formats = { "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd" };
/*  434 */     return getNumber(datetime, formats, 8);
/*      */   }
/*      */ 
/*      */   public static double dayOfWeekInMonth()
/*      */   {
/*  442 */     Calendar cal = Calendar.getInstance();
/*  443 */     return cal.get(8);
/*      */   }
/*      */ 
/*      */   public static double dayInWeek(String datetimeIn)
/*      */     throws ParseException
/*      */   {
/*  464 */     String[] edz = getEraDatetimeZone(datetimeIn);
/*  465 */     String datetime = edz[1];
/*  466 */     if (datetime == null) {
/*  467 */       return (0.0D / 0.0D);
/*      */     }
/*  469 */     String[] formats = { "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd" };
/*  470 */     return getNumber(datetime, formats, 7);
/*      */   }
/*      */ 
/*      */   public static double dayInWeek()
/*      */   {
/*  478 */     Calendar cal = Calendar.getInstance();
/*  479 */     return cal.get(7);
/*      */   }
/*      */ 
/*      */   public static double hourInDay(String datetimeIn)
/*      */     throws ParseException
/*      */   {
/*  498 */     String[] edz = getEraDatetimeZone(datetimeIn);
/*  499 */     String datetime = edz[1];
/*  500 */     if (datetime == null) {
/*  501 */       return (0.0D / 0.0D);
/*      */     }
/*  503 */     String[] formats = { "yyyy-MM-dd'T'HH:mm:ss", "HH:mm:ss" };
/*  504 */     return getNumber(datetime, formats, 11);
/*      */   }
/*      */ 
/*      */   public static double hourInDay()
/*      */   {
/*  512 */     Calendar cal = Calendar.getInstance();
/*  513 */     return cal.get(11);
/*      */   }
/*      */ 
/*      */   public static double minuteInHour(String datetimeIn)
/*      */     throws ParseException
/*      */   {
/*  532 */     String[] edz = getEraDatetimeZone(datetimeIn);
/*  533 */     String datetime = edz[1];
/*  534 */     if (datetime == null) {
/*  535 */       return (0.0D / 0.0D);
/*      */     }
/*  537 */     String[] formats = { "yyyy-MM-dd'T'HH:mm:ss", "HH:mm:ss" };
/*  538 */     return getNumber(datetime, formats, 12);
/*      */   }
/*      */ 
/*      */   public static double minuteInHour()
/*      */   {
/*  546 */     Calendar cal = Calendar.getInstance();
/*  547 */     return cal.get(12);
/*      */   }
/*      */ 
/*      */   public static double secondInMinute(String datetimeIn)
/*      */     throws ParseException
/*      */   {
/*  566 */     String[] edz = getEraDatetimeZone(datetimeIn);
/*  567 */     String datetime = edz[1];
/*  568 */     if (datetime == null) {
/*  569 */       return (0.0D / 0.0D);
/*      */     }
/*  571 */     String[] formats = { "yyyy-MM-dd'T'HH:mm:ss", "HH:mm:ss" };
/*  572 */     return getNumber(datetime, formats, 13);
/*      */   }
/*      */ 
/*      */   public static double secondInMinute()
/*      */   {
/*  580 */     Calendar cal = Calendar.getInstance();
/*  581 */     return cal.get(13);
/*      */   }
/*      */ 
/*      */   public static XObject leapYear(String datetimeIn)
/*      */     throws ParseException
/*      */   {
/*  602 */     String[] edz = getEraDatetimeZone(datetimeIn);
/*  603 */     String datetime = edz[1];
/*  604 */     if (datetime == null) {
/*  605 */       return new XNumber((0.0D / 0.0D));
/*      */     }
/*  607 */     String[] formats = { "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd", "yyyy-MM", "yyyy" };
/*  608 */     double dbl = getNumber(datetime, formats, 1);
/*  609 */     if (dbl == (0.0D / 0.0D))
/*  610 */       return new XNumber((0.0D / 0.0D));
/*  611 */     int yr = (int)dbl;
/*  612 */     return new XBoolean((yr % 400 == 0) || ((yr % 100 != 0) && (yr % 4 == 0)));
/*      */   }
/*      */ 
/*      */   public static boolean leapYear()
/*      */   {
/*  620 */     Calendar cal = Calendar.getInstance();
/*  621 */     int yr = cal.get(1);
/*  622 */     return (yr % 400 == 0) || ((yr % 100 != 0) && (yr % 4 == 0));
/*      */   }
/*      */ 
/*      */   public static String monthName(String datetimeIn)
/*      */     throws ParseException
/*      */   {
/*  647 */     String[] edz = getEraDatetimeZone(datetimeIn);
/*  648 */     String datetime = edz[1];
/*  649 */     if (datetime == null) {
/*  650 */       return "";
/*      */     }
/*  652 */     String[] formatsIn = { "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd", "yyyy-MM", "--MM--" };
/*  653 */     String formatOut = "MMMM";
/*  654 */     return getNameOrAbbrev(datetimeIn, formatsIn, formatOut);
/*      */   }
/*      */ 
/*      */   public static String monthName()
/*      */   {
/*  662 */     Calendar cal = Calendar.getInstance();
/*  663 */     String format = "MMMM";
/*  664 */     return getNameOrAbbrev(format);
/*      */   }
/*      */ 
/*      */   public static String monthAbbreviation(String datetimeIn)
/*      */     throws ParseException
/*      */   {
/*  690 */     String[] edz = getEraDatetimeZone(datetimeIn);
/*  691 */     String datetime = edz[1];
/*  692 */     if (datetime == null) {
/*  693 */       return "";
/*      */     }
/*  695 */     String[] formatsIn = { "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd", "yyyy-MM", "--MM--" };
/*  696 */     String formatOut = "MMM";
/*  697 */     return getNameOrAbbrev(datetimeIn, formatsIn, formatOut);
/*      */   }
/*      */ 
/*      */   public static String monthAbbreviation()
/*      */   {
/*  705 */     String format = "MMM";
/*  706 */     return getNameOrAbbrev(format);
/*      */   }
/*      */ 
/*      */   public static String dayName(String datetimeIn)
/*      */     throws ParseException
/*      */   {
/*  730 */     String[] edz = getEraDatetimeZone(datetimeIn);
/*  731 */     String datetime = edz[1];
/*  732 */     if (datetime == null) {
/*  733 */       return "";
/*      */     }
/*  735 */     String[] formatsIn = { "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd" };
/*  736 */     String formatOut = "EEEE";
/*  737 */     return getNameOrAbbrev(datetimeIn, formatsIn, formatOut);
/*      */   }
/*      */ 
/*      */   public static String dayName()
/*      */   {
/*  745 */     String format = "EEEE";
/*  746 */     return getNameOrAbbrev(format);
/*      */   }
/*      */ 
/*      */   public static String dayAbbreviation(String datetimeIn)
/*      */     throws ParseException
/*      */   {
/*  770 */     String[] edz = getEraDatetimeZone(datetimeIn);
/*  771 */     String datetime = edz[1];
/*  772 */     if (datetime == null) {
/*  773 */       return "";
/*      */     }
/*  775 */     String[] formatsIn = { "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd" };
/*  776 */     String formatOut = "EEE";
/*  777 */     return getNameOrAbbrev(datetimeIn, formatsIn, formatOut);
/*      */   }
/*      */ 
/*      */   public static String dayAbbreviation()
/*      */   {
/*  785 */     String format = "EEE";
/*  786 */     return getNameOrAbbrev(format);
/*      */   }
/*      */ 
/*      */   private static String[] getEraDatetimeZone(String in)
/*      */   {
/*  796 */     String leader = "";
/*  797 */     String datetime = in;
/*  798 */     String zone = "";
/*  799 */     if ((in.charAt(0) == '-') && (!in.startsWith("--")))
/*      */     {
/*  801 */       leader = "-";
/*  802 */       datetime = in.substring(1);
/*      */     }
/*  804 */     int z = getZoneStart(datetime);
/*  805 */     if (z > 0)
/*      */     {
/*  807 */       zone = datetime.substring(z);
/*  808 */       datetime = datetime.substring(0, z);
/*      */     }
/*  810 */     else if (z == -2) {
/*  811 */       zone = null;
/*      */     }
/*  813 */     return new String[] { leader, datetime, zone };
/*      */   }
/*      */ 
/*      */   private static int getZoneStart(String datetime)
/*      */   {
/*  824 */     if (datetime.indexOf("Z") == datetime.length() - 1)
/*  825 */       return datetime.length() - 1;
/*  826 */     if ((datetime.length() >= 6) && (datetime.charAt(datetime.length() - 3) == ':') && ((datetime.charAt(datetime.length() - 6) == '+') || (datetime.charAt(datetime.length() - 6) == '-')))
/*      */     {
/*      */       try
/*      */       {
/*  833 */         SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
/*  834 */         dateFormat.setLenient(false);
/*  835 */         Date d = dateFormat.parse(datetime.substring(datetime.length() - 5));
/*  836 */         return datetime.length() - 6;
/*      */       }
/*      */       catch (ParseException pe)
/*      */       {
/*  840 */         System.out.println("ParseException " + pe.getErrorOffset());
/*  841 */         return -2;
/*      */       }
/*      */     }
/*      */ 
/*  845 */     return -1;
/*      */   }
/*      */ 
/*      */   private static Date testFormats(String in, String[] formats)
/*      */     throws ParseException
/*      */   {
/*  855 */     for (int i = 0; i < formats.length; i++)
/*      */     {
/*      */       try
/*      */       {
/*  859 */         SimpleDateFormat dateFormat = new SimpleDateFormat(formats[i]);
/*  860 */         dateFormat.setLenient(false);
/*  861 */         return dateFormat.parse(in);
/*      */       }
/*      */       catch (ParseException pe)
/*      */       {
/*      */       }
/*      */     }
/*  867 */     return null;
/*      */   }
/*      */ 
/*      */   private static double getNumber(String in, String[] formats, int calField)
/*      */     throws ParseException
/*      */   {
/*  878 */     Calendar cal = Calendar.getInstance();
/*  879 */     cal.setLenient(false);
/*      */ 
/*  881 */     Date date = testFormats(in, formats);
/*  882 */     if (date == null) return (0.0D / 0.0D);
/*  883 */     cal.setTime(date);
/*  884 */     return cal.get(calField);
/*      */   }
/*      */ 
/*      */   private static String getNameOrAbbrev(String in, String[] formatsIn, String formatOut)
/*      */     throws ParseException
/*      */   {
/*  895 */     for (int i = 0; i < formatsIn.length; i++)
/*      */     {
/*      */       try
/*      */       {
/*  899 */         SimpleDateFormat dateFormat = new SimpleDateFormat(formatsIn[i], Locale.ENGLISH);
/*  900 */         dateFormat.setLenient(false);
/*  901 */         Date dt = dateFormat.parse(in);
/*  902 */         dateFormat.applyPattern(formatOut);
/*  903 */         return dateFormat.format(dt);
/*      */       }
/*      */       catch (ParseException pe)
/*      */       {
/*      */       }
/*      */     }
/*  909 */     return "";
/*      */   }
/*      */ 
/*      */   private static String getNameOrAbbrev(String format)
/*      */   {
/*  917 */     Calendar cal = Calendar.getInstance();
/*  918 */     SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
/*  919 */     return dateFormat.format(cal.getTime());
/*      */   }
/*      */ 
/*      */   public static String formatDate(String dateTime, String pattern)
/*      */   {
/*  960 */     String yearSymbols = "Gy";
/*  961 */     String monthSymbols = "M";
/*  962 */     String daySymbols = "dDEFwW";
/*      */     String zone;
/*      */     TimeZone timeZone;
/*      */     String zone;
/*  968 */     if ((dateTime.endsWith("Z")) || (dateTime.endsWith("z")))
/*      */     {
/*  970 */       TimeZone timeZone = TimeZone.getTimeZone("GMT");
/*  971 */       dateTime = dateTime.substring(0, dateTime.length() - 1) + "GMT";
/*  972 */       zone = "z";
/*      */     }
/*  974 */     else if ((dateTime.length() >= 6) && (dateTime.charAt(dateTime.length() - 3) == ':') && ((dateTime.charAt(dateTime.length() - 6) == '+') || (dateTime.charAt(dateTime.length() - 6) == '-')))
/*      */     {
/*  979 */       String offset = dateTime.substring(dateTime.length() - 6);
/*      */       TimeZone timeZone;
/*      */       TimeZone timeZone;
/*  981 */       if (("+00:00".equals(offset)) || ("-00:00".equals(offset)))
/*      */       {
/*  983 */         timeZone = TimeZone.getTimeZone("GMT");
/*      */       }
/*      */       else
/*      */       {
/*  987 */         timeZone = TimeZone.getTimeZone("GMT" + offset);
/*      */       }
/*  989 */       String zone = "z";
/*      */ 
/*  992 */       dateTime = dateTime.substring(0, dateTime.length() - 6) + "GMT" + offset;
/*      */     }
/*      */     else
/*      */     {
/*  997 */       timeZone = TimeZone.getDefault();
/*  998 */       zone = "";
/*      */     }
/*      */ 
/* 1002 */     String[] formats = { "yyyy-MM-dd'T'HH:mm:ss" + zone, "yyyy-MM-dd", "yyyy-MM", "yyyy" };
/*      */     try
/*      */     {
/* 1009 */       SimpleDateFormat inFormat = new SimpleDateFormat("HH:mm:ss" + zone);
/* 1010 */       inFormat.setLenient(false);
/* 1011 */       Date d = inFormat.parse(dateTime);
/* 1012 */       SimpleDateFormat outFormat = new SimpleDateFormat(strip("GyMdDEFwW", pattern));
/*      */ 
/* 1014 */       outFormat.setTimeZone(timeZone);
/* 1015 */       return outFormat.format(d);
/*      */     }
/*      */     catch (ParseException pe)
/*      */     {
/* 1022 */       for (int i = 0; i < formats.length; i++)
/*      */       {
/*      */         try
/*      */         {
/* 1026 */           SimpleDateFormat inFormat = new SimpleDateFormat(formats[i]);
/* 1027 */           inFormat.setLenient(false);
/* 1028 */           Date d = inFormat.parse(dateTime);
/* 1029 */           SimpleDateFormat outFormat = new SimpleDateFormat(pattern);
/* 1030 */           outFormat.setTimeZone(timeZone);
/* 1031 */           return outFormat.format(d);
/*      */         }
/*      */         catch (ParseException pe)
/*      */         {
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */       try
/*      */       {
/* 1044 */         SimpleDateFormat inFormat = new SimpleDateFormat("--MM-dd");
/* 1045 */         inFormat.setLenient(false);
/* 1046 */         Date d = inFormat.parse(dateTime);
/* 1047 */         SimpleDateFormat outFormat = new SimpleDateFormat(strip("Gy", pattern));
/* 1048 */         outFormat.setTimeZone(timeZone);
/* 1049 */         return outFormat.format(d);
/*      */       }
/*      */       catch (ParseException pe)
/*      */       {
/*      */         try
/*      */         {
/* 1056 */           SimpleDateFormat inFormat = new SimpleDateFormat("--MM--");
/* 1057 */           inFormat.setLenient(false);
/* 1058 */           Date d = inFormat.parse(dateTime);
/* 1059 */           SimpleDateFormat outFormat = new SimpleDateFormat(strip("Gy", pattern));
/* 1060 */           outFormat.setTimeZone(timeZone);
/* 1061 */           return outFormat.format(d);
/*      */         }
/*      */         catch (ParseException pe)
/*      */         {
/*      */           try
/*      */           {
/* 1068 */             SimpleDateFormat inFormat = new SimpleDateFormat("---dd");
/* 1069 */             inFormat.setLenient(false);
/* 1070 */             Date d = inFormat.parse(dateTime);
/* 1071 */             SimpleDateFormat outFormat = new SimpleDateFormat(strip("GyM", pattern));
/* 1072 */             outFormat.setTimeZone(timeZone);
/* 1073 */             return outFormat.format(d); } catch (ParseException pe) {
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1078 */     return "";
/*      */   }
/*      */ 
/*      */   private static String strip(String symbols, String pattern)
/*      */   {
/* 1089 */     int quoteSemaphore = 0;
/* 1090 */     int i = 0;
/* 1091 */     StringBuffer result = new StringBuffer(pattern.length());
/*      */ 
/* 1093 */     while (i < pattern.length())
/*      */     {
/* 1095 */       char ch = pattern.charAt(i);
/* 1096 */       if (ch == '\'')
/*      */       {
/* 1100 */         int endQuote = pattern.indexOf('\'', i + 1);
/* 1101 */         if (endQuote == -1)
/*      */         {
/* 1103 */           endQuote = pattern.length();
/*      */         }
/* 1105 */         result.append(pattern.substring(i, endQuote));
/* 1106 */         i = endQuote++;
/*      */       }
/* 1108 */       else if (symbols.indexOf(ch) > -1)
/*      */       {
/* 1111 */         i++;
/*      */       }
/*      */       else
/*      */       {
/* 1115 */         result.append(ch);
/* 1116 */         i++;
/*      */       }
/*      */     }
/* 1119 */     return result.toString();
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.lib.ExsltDatetime
 * JD-Core Version:    0.6.2
 */