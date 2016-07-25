/*     */ package java.sql;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
/*     */ 
/*     */ public class SQLException extends Exception
/*     */   implements Iterable<Throwable>
/*     */ {
/*     */   private String SQLState;
/*     */   private int vendorCode;
/*     */   private volatile SQLException next;
/* 371 */   private static final AtomicReferenceFieldUpdater<SQLException, SQLException> nextUpdater = AtomicReferenceFieldUpdater.newUpdater(SQLException.class, SQLException.class, "next");
/*     */   private static final long serialVersionUID = 2135244094396331484L;
/*     */ 
/*     */   public SQLException(String paramString1, String paramString2, int paramInt)
/*     */   {
/*  70 */     super(paramString1);
/*  71 */     this.SQLState = paramString2;
/*  72 */     this.vendorCode = paramInt;
/*  73 */     if ((!(this instanceof SQLWarning)) && 
/*  74 */       (DriverManager.getLogWriter() != null)) {
/*  75 */       DriverManager.println("SQLState(" + paramString2 + ") vendor code(" + paramInt + ")");
/*     */ 
/*  77 */       printStackTrace(DriverManager.getLogWriter());
/*     */     }
/*     */   }
/*     */ 
/*     */   public SQLException(String paramString1, String paramString2)
/*     */   {
/*  96 */     super(paramString1);
/*  97 */     this.SQLState = paramString2;
/*  98 */     this.vendorCode = 0;
/*  99 */     if ((!(this instanceof SQLWarning)) && 
/* 100 */       (DriverManager.getLogWriter() != null)) {
/* 101 */       printStackTrace(DriverManager.getLogWriter());
/* 102 */       DriverManager.println("SQLException: SQLState(" + paramString2 + ")");
/*     */     }
/*     */   }
/*     */ 
/*     */   public SQLException(String paramString)
/*     */   {
/* 119 */     super(paramString);
/* 120 */     this.SQLState = null;
/* 121 */     this.vendorCode = 0;
/* 122 */     if ((!(this instanceof SQLWarning)) && 
/* 123 */       (DriverManager.getLogWriter() != null))
/* 124 */       printStackTrace(DriverManager.getLogWriter());
/*     */   }
/*     */ 
/*     */   public SQLException()
/*     */   {
/* 141 */     this.SQLState = null;
/* 142 */     this.vendorCode = 0;
/* 143 */     if ((!(this instanceof SQLWarning)) && 
/* 144 */       (DriverManager.getLogWriter() != null))
/* 145 */       printStackTrace(DriverManager.getLogWriter());
/*     */   }
/*     */ 
/*     */   public SQLException(Throwable paramThrowable)
/*     */   {
/* 165 */     super(paramThrowable);
/*     */ 
/* 167 */     if ((!(this instanceof SQLWarning)) && 
/* 168 */       (DriverManager.getLogWriter() != null))
/* 169 */       printStackTrace(DriverManager.getLogWriter());
/*     */   }
/*     */ 
/*     */   public SQLException(String paramString, Throwable paramThrowable)
/*     */   {
/* 187 */     super(paramString, paramThrowable);
/*     */ 
/* 189 */     if ((!(this instanceof SQLWarning)) && 
/* 190 */       (DriverManager.getLogWriter() != null))
/* 191 */       printStackTrace(DriverManager.getLogWriter());
/*     */   }
/*     */ 
/*     */   public SQLException(String paramString1, String paramString2, Throwable paramThrowable)
/*     */   {
/* 210 */     super(paramString1, paramThrowable);
/*     */ 
/* 212 */     this.SQLState = paramString2;
/* 213 */     this.vendorCode = 0;
/* 214 */     if ((!(this instanceof SQLWarning)) && 
/* 215 */       (DriverManager.getLogWriter() != null)) {
/* 216 */       printStackTrace(DriverManager.getLogWriter());
/* 217 */       DriverManager.println("SQLState(" + this.SQLState + ")");
/*     */     }
/*     */   }
/*     */ 
/*     */   public SQLException(String paramString1, String paramString2, int paramInt, Throwable paramThrowable)
/*     */   {
/* 236 */     super(paramString1, paramThrowable);
/*     */ 
/* 238 */     this.SQLState = paramString2;
/* 239 */     this.vendorCode = paramInt;
/* 240 */     if ((!(this instanceof SQLWarning)) && 
/* 241 */       (DriverManager.getLogWriter() != null)) {
/* 242 */       DriverManager.println("SQLState(" + this.SQLState + ") vendor code(" + paramInt + ")");
/*     */ 
/* 244 */       printStackTrace(DriverManager.getLogWriter());
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getSQLState()
/*     */   {
/* 255 */     return this.SQLState;
/*     */   }
/*     */ 
/*     */   public int getErrorCode()
/*     */   {
/* 265 */     return this.vendorCode;
/*     */   }
/*     */ 
/*     */   public SQLException getNextException()
/*     */   {
/* 277 */     return this.next;
/*     */   }
/*     */ 
/*     */   public void setNextException(SQLException paramSQLException)
/*     */   {
/* 289 */     Object localObject = this;
/*     */     while (true) {
/* 291 */       SQLException localSQLException = ((SQLException)localObject).next;
/* 292 */       if (localSQLException != null) {
/* 293 */         localObject = localSQLException;
/*     */       }
/*     */       else
/*     */       {
/* 297 */         if (nextUpdater.compareAndSet(localObject, null, paramSQLException)) {
/* 298 */           return;
/*     */         }
/* 300 */         localObject = ((SQLException)localObject).next;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public Iterator<Throwable> iterator()
/*     */   {
/* 316 */     return new Iterator()
/*     */     {
/* 318 */       SQLException firstException = SQLException.this;
/* 319 */       SQLException nextException = this.firstException.getNextException();
/* 320 */       Throwable cause = this.firstException.getCause();
/*     */ 
/*     */       public boolean hasNext() {
/* 323 */         if ((this.firstException != null) || (this.nextException != null) || (this.cause != null))
/* 324 */           return true;
/* 325 */         return false;
/*     */       }
/*     */ 
/*     */       public Throwable next() {
/* 329 */         Object localObject = null;
/* 330 */         if (this.firstException != null) {
/* 331 */           localObject = this.firstException;
/* 332 */           this.firstException = null;
/*     */         }
/* 334 */         else if (this.cause != null) {
/* 335 */           localObject = this.cause;
/* 336 */           this.cause = this.cause.getCause();
/*     */         }
/* 338 */         else if (this.nextException != null) {
/* 339 */           localObject = this.nextException;
/* 340 */           this.cause = this.nextException.getCause();
/* 341 */           this.nextException = this.nextException.getNextException();
/*     */         }
/*     */         else {
/* 344 */           throw new NoSuchElementException();
/* 345 */         }return localObject;
/*     */       }
/*     */ 
/*     */       public void remove() {
/* 349 */         throw new UnsupportedOperationException();
/*     */       }
/*     */     };
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.sql.SQLException
 * JD-Core Version:    0.6.2
 */