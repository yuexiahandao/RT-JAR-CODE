/*     */ package javax.sql.rowset.serial;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.io.Serializable;
/*     */ import java.sql.Array;
/*     */ import java.sql.Blob;
/*     */ import java.sql.Clob;
/*     */ import java.sql.Ref;
/*     */ import java.sql.SQLData;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Struct;
/*     */ import java.util.Map;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class SerialStruct
/*     */   implements Struct, Serializable, Cloneable
/*     */ {
/*     */   private String SQLTypeName;
/*     */   private Object[] attribs;
/*     */   static final long serialVersionUID = -8322445504027483372L;
/*     */ 
/*     */   public SerialStruct(Struct paramStruct, Map<String, Class<?>> paramMap)
/*     */     throws SerialException
/*     */   {
/*     */     try
/*     */     {
/*  97 */       this.SQLTypeName = paramStruct.getSQLTypeName();
/*  98 */       System.out.println("SQLTypeName: " + this.SQLTypeName);
/*     */ 
/* 101 */       this.attribs = paramStruct.getAttributes(paramMap);
/*     */ 
/* 108 */       mapToSerial(paramMap);
/*     */     }
/*     */     catch (SQLException localSQLException) {
/* 111 */       throw new SerialException(localSQLException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public SerialStruct(SQLData paramSQLData, Map<String, Class<?>> paramMap)
/*     */     throws SerialException
/*     */   {
/*     */     try
/*     */     {
/* 140 */       this.SQLTypeName = paramSQLData.getSQLTypeName();
/*     */ 
/* 142 */       Vector localVector = new Vector();
/* 143 */       paramSQLData.writeSQL(new SQLOutputImpl(localVector, paramMap));
/* 144 */       this.attribs = localVector.toArray();
/*     */     }
/*     */     catch (SQLException localSQLException) {
/* 147 */       throw new SerialException(localSQLException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getSQLTypeName()
/*     */     throws SerialException
/*     */   {
/* 163 */     return this.SQLTypeName;
/*     */   }
/*     */ 
/*     */   public Object[] getAttributes()
/*     */     throws SerialException
/*     */   {
/* 177 */     return this.attribs;
/*     */   }
/*     */ 
/*     */   public Object[] getAttributes(Map<String, Class<?>> paramMap)
/*     */     throws SerialException
/*     */   {
/* 200 */     return this.attribs;
/*     */   }
/*     */ 
/*     */   private void mapToSerial(Map paramMap)
/*     */     throws SerialException
/*     */   {
/*     */     try
/*     */     {
/* 227 */       for (int i = 0; i < this.attribs.length; i++) {
/* 228 */         if ((this.attribs[i] instanceof Cloneable))
/* 229 */           this.attribs[i] = new SerialStruct((Cloneable)this.attribs[i], paramMap);
/* 230 */         else if ((this.attribs[i] instanceof SQLData))
/* 231 */           this.attribs[i] = new SerialStruct((SQLData)this.attribs[i], paramMap);
/* 232 */         else if ((this.attribs[i] instanceof Blob))
/* 233 */           this.attribs[i] = new SerialBlob((Blob)this.attribs[i]);
/* 234 */         else if ((this.attribs[i] instanceof Clob))
/* 235 */           this.attribs[i] = new SerialClob((Clob)this.attribs[i]);
/* 236 */         else if ((this.attribs[i] instanceof Ref))
/* 237 */           this.attribs[i] = new SerialRef((Ref)this.attribs[i]);
/* 238 */         else if ((this.attribs[i] instanceof Array))
/* 239 */           this.attribs[i] = new SerialArray((Array)this.attribs[i], paramMap);
/*     */       }
/*     */     }
/*     */     catch (SQLException localSQLException)
/*     */     {
/* 244 */       throw new SerialException(localSQLException.getMessage());
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sql.rowset.serial.SerialStruct
 * JD-Core Version:    0.6.2
 */