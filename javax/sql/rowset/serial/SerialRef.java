/*     */ package javax.sql.rowset.serial;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.sql.Ref;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class SerialRef
/*     */   implements Ref, Serializable, Cloneable
/*     */ {
/*     */   private String baseTypeName;
/*     */   private Object object;
/*     */   private Ref reference;
/*     */   static final long serialVersionUID = -4727123500609662274L;
/*     */ 
/*     */   public SerialRef(Ref paramRef)
/*     */     throws SerialException, SQLException
/*     */   {
/*  70 */     if (paramRef == null) {
/*  71 */       throw new SQLException("Cannot instantiate a SerialRef object with a null Ref object");
/*     */     }
/*     */ 
/*  74 */     this.reference = paramRef;
/*  75 */     this.object = paramRef;
/*  76 */     if (paramRef.getBaseTypeName() == null) {
/*  77 */       throw new SQLException("Cannot instantiate a SerialRef object that returns a null base type name");
/*     */     }
/*     */ 
/*  80 */     this.baseTypeName = paramRef.getBaseTypeName();
/*     */   }
/*     */ 
/*     */   public String getBaseTypeName()
/*     */     throws SerialException
/*     */   {
/*  91 */     return this.baseTypeName;
/*     */   }
/*     */ 
/*     */   public Object getObject(Map<String, Class<?>> paramMap)
/*     */     throws SerialException
/*     */   {
/* 112 */     paramMap = new Hashtable(paramMap);
/* 113 */     if (this.object != null) {
/* 114 */       return paramMap.get(this.object);
/*     */     }
/* 116 */     throw new SerialException("The object is not set");
/*     */   }
/*     */ 
/*     */   public Object getObject()
/*     */     throws SerialException
/*     */   {
/* 130 */     if (this.reference != null) {
/*     */       try {
/* 132 */         return this.reference.getObject();
/*     */       } catch (SQLException localSQLException) {
/* 134 */         throw new SerialException("SQLException: " + localSQLException.getMessage());
/*     */       }
/*     */     }
/*     */ 
/* 138 */     if (this.object != null) {
/* 139 */       return this.object;
/*     */     }
/*     */ 
/* 143 */     throw new SerialException("The object is not set");
/*     */   }
/*     */ 
/*     */   public void setObject(Object paramObject)
/*     */     throws SerialException
/*     */   {
/*     */     try
/*     */     {
/* 158 */       this.reference.setObject(paramObject);
/*     */     } catch (SQLException localSQLException) {
/* 160 */       throw new SerialException("SQLException: " + localSQLException.getMessage());
/*     */     }
/* 162 */     this.object = paramObject;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sql.rowset.serial.SerialRef
 * JD-Core Version:    0.6.2
 */