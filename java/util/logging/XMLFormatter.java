/*     */ package java.util.logging;
/*     */ 
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Date;
/*     */ import java.util.ResourceBundle;
/*     */ 
/*     */ public class XMLFormatter extends Formatter
/*     */ {
/*  47 */   private LogManager manager = LogManager.getLogManager();
/*     */ 
/*     */   private void a2(StringBuffer paramStringBuffer, int paramInt)
/*     */   {
/*  51 */     if (paramInt < 10) {
/*  52 */       paramStringBuffer.append('0');
/*     */     }
/*  54 */     paramStringBuffer.append(paramInt);
/*     */   }
/*     */ 
/*     */   private void appendISO8601(StringBuffer paramStringBuffer, long paramLong)
/*     */   {
/*  59 */     Date localDate = new Date(paramLong);
/*  60 */     paramStringBuffer.append(localDate.getYear() + 1900);
/*  61 */     paramStringBuffer.append('-');
/*  62 */     a2(paramStringBuffer, localDate.getMonth() + 1);
/*  63 */     paramStringBuffer.append('-');
/*  64 */     a2(paramStringBuffer, localDate.getDate());
/*  65 */     paramStringBuffer.append('T');
/*  66 */     a2(paramStringBuffer, localDate.getHours());
/*  67 */     paramStringBuffer.append(':');
/*  68 */     a2(paramStringBuffer, localDate.getMinutes());
/*  69 */     paramStringBuffer.append(':');
/*  70 */     a2(paramStringBuffer, localDate.getSeconds());
/*     */   }
/*     */ 
/*     */   private void escape(StringBuffer paramStringBuffer, String paramString)
/*     */   {
/*  77 */     if (paramString == null) {
/*  78 */       paramString = "<null>";
/*     */     }
/*  80 */     for (int i = 0; i < paramString.length(); i++) {
/*  81 */       char c = paramString.charAt(i);
/*  82 */       if (c == '<')
/*  83 */         paramStringBuffer.append("&lt;");
/*  84 */       else if (c == '>')
/*  85 */         paramStringBuffer.append("&gt;");
/*  86 */       else if (c == '&')
/*  87 */         paramStringBuffer.append("&amp;");
/*     */       else
/*  89 */         paramStringBuffer.append(c);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String format(LogRecord paramLogRecord)
/*     */   {
/* 105 */     StringBuffer localStringBuffer = new StringBuffer(500);
/* 106 */     localStringBuffer.append("<record>\n");
/*     */ 
/* 108 */     localStringBuffer.append("  <date>");
/* 109 */     appendISO8601(localStringBuffer, paramLogRecord.getMillis());
/* 110 */     localStringBuffer.append("</date>\n");
/*     */ 
/* 112 */     localStringBuffer.append("  <millis>");
/* 113 */     localStringBuffer.append(paramLogRecord.getMillis());
/* 114 */     localStringBuffer.append("</millis>\n");
/*     */ 
/* 116 */     localStringBuffer.append("  <sequence>");
/* 117 */     localStringBuffer.append(paramLogRecord.getSequenceNumber());
/* 118 */     localStringBuffer.append("</sequence>\n");
/*     */ 
/* 120 */     String str = paramLogRecord.getLoggerName();
/* 121 */     if (str != null) {
/* 122 */       localStringBuffer.append("  <logger>");
/* 123 */       escape(localStringBuffer, str);
/* 124 */       localStringBuffer.append("</logger>\n");
/*     */     }
/*     */ 
/* 127 */     localStringBuffer.append("  <level>");
/* 128 */     escape(localStringBuffer, paramLogRecord.getLevel().toString());
/* 129 */     localStringBuffer.append("</level>\n");
/*     */ 
/* 131 */     if (paramLogRecord.getSourceClassName() != null) {
/* 132 */       localStringBuffer.append("  <class>");
/* 133 */       escape(localStringBuffer, paramLogRecord.getSourceClassName());
/* 134 */       localStringBuffer.append("</class>\n");
/*     */     }
/*     */ 
/* 137 */     if (paramLogRecord.getSourceMethodName() != null) {
/* 138 */       localStringBuffer.append("  <method>");
/* 139 */       escape(localStringBuffer, paramLogRecord.getSourceMethodName());
/* 140 */       localStringBuffer.append("</method>\n");
/*     */     }
/*     */ 
/* 143 */     localStringBuffer.append("  <thread>");
/* 144 */     localStringBuffer.append(paramLogRecord.getThreadID());
/* 145 */     localStringBuffer.append("</thread>\n");
/*     */ 
/* 147 */     if (paramLogRecord.getMessage() != null)
/*     */     {
/* 149 */       localObject = formatMessage(paramLogRecord);
/* 150 */       localStringBuffer.append("  <message>");
/* 151 */       escape(localStringBuffer, (String)localObject);
/* 152 */       localStringBuffer.append("</message>");
/* 153 */       localStringBuffer.append("\n");
/*     */     }
/*     */ 
/* 158 */     Object localObject = paramLogRecord.getResourceBundle();
/*     */     try {
/* 160 */       if ((localObject != null) && (((ResourceBundle)localObject).getString(paramLogRecord.getMessage()) != null)) {
/* 161 */         localStringBuffer.append("  <key>");
/* 162 */         escape(localStringBuffer, paramLogRecord.getMessage());
/* 163 */         localStringBuffer.append("</key>\n");
/* 164 */         localStringBuffer.append("  <catalog>");
/* 165 */         escape(localStringBuffer, paramLogRecord.getResourceBundleName());
/* 166 */         localStringBuffer.append("</catalog>\n");
/*     */       }
/*     */     }
/*     */     catch (Exception localException1)
/*     */     {
/*     */     }
/* 172 */     Object[] arrayOfObject = paramLogRecord.getParameters();
/*     */ 
/* 175 */     if ((arrayOfObject != null) && (arrayOfObject.length != 0) && (paramLogRecord.getMessage().indexOf("{") == -1))
/*     */     {
/* 177 */       for (int i = 0; i < arrayOfObject.length; i++) {
/* 178 */         localStringBuffer.append("  <param>");
/*     */         try {
/* 180 */           escape(localStringBuffer, arrayOfObject[i].toString());
/*     */         } catch (Exception localException2) {
/* 182 */           localStringBuffer.append("???");
/*     */         }
/* 184 */         localStringBuffer.append("</param>\n");
/*     */       }
/*     */     }
/*     */ 
/* 188 */     if (paramLogRecord.getThrown() != null)
/*     */     {
/* 190 */       Throwable localThrowable = paramLogRecord.getThrown();
/* 191 */       localStringBuffer.append("  <exception>\n");
/* 192 */       localStringBuffer.append("    <message>");
/* 193 */       escape(localStringBuffer, localThrowable.toString());
/* 194 */       localStringBuffer.append("</message>\n");
/* 195 */       StackTraceElement[] arrayOfStackTraceElement = localThrowable.getStackTrace();
/* 196 */       for (int j = 0; j < arrayOfStackTraceElement.length; j++) {
/* 197 */         StackTraceElement localStackTraceElement = arrayOfStackTraceElement[j];
/* 198 */         localStringBuffer.append("    <frame>\n");
/* 199 */         localStringBuffer.append("      <class>");
/* 200 */         escape(localStringBuffer, localStackTraceElement.getClassName());
/* 201 */         localStringBuffer.append("</class>\n");
/* 202 */         localStringBuffer.append("      <method>");
/* 203 */         escape(localStringBuffer, localStackTraceElement.getMethodName());
/* 204 */         localStringBuffer.append("</method>\n");
/*     */ 
/* 206 */         if (localStackTraceElement.getLineNumber() >= 0) {
/* 207 */           localStringBuffer.append("      <line>");
/* 208 */           localStringBuffer.append(localStackTraceElement.getLineNumber());
/* 209 */           localStringBuffer.append("</line>\n");
/*     */         }
/* 211 */         localStringBuffer.append("    </frame>\n");
/*     */       }
/* 213 */       localStringBuffer.append("  </exception>\n");
/*     */     }
/*     */ 
/* 216 */     localStringBuffer.append("</record>\n");
/* 217 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   public String getHead(Handler paramHandler)
/*     */   {
/* 227 */     StringBuffer localStringBuffer = new StringBuffer();
/*     */ 
/* 229 */     localStringBuffer.append("<?xml version=\"1.0\"");
/*     */     String str;
/* 231 */     if (paramHandler != null)
/* 232 */       str = paramHandler.getEncoding();
/*     */     else {
/* 234 */       str = null;
/*     */     }
/*     */ 
/* 237 */     if (str == null)
/*     */     {
/* 239 */       str = Charset.defaultCharset().name();
/*     */     }
/*     */     try
/*     */     {
/* 243 */       Charset localCharset = Charset.forName(str);
/* 244 */       str = localCharset.name();
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/*     */ 
/* 250 */     localStringBuffer.append(" encoding=\"");
/* 251 */     localStringBuffer.append(str);
/* 252 */     localStringBuffer.append("\"");
/* 253 */     localStringBuffer.append(" standalone=\"no\"?>\n");
/* 254 */     localStringBuffer.append("<!DOCTYPE log SYSTEM \"logger.dtd\">\n");
/* 255 */     localStringBuffer.append("<log>\n");
/* 256 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   public String getTail(Handler paramHandler)
/*     */   {
/* 266 */     return "</log>\n";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.logging.XMLFormatter
 * JD-Core Version:    0.6.2
 */