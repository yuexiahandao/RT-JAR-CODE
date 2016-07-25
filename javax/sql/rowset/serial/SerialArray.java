/*     */ package javax.sql.rowset.serial;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.net.URL;
/*     */ import java.sql.Array;
/*     */ import java.sql.Blob;
/*     */ import java.sql.Clob;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.SQLFeatureNotSupportedException;
/*     */ import java.sql.Struct;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class SerialArray
/*     */   implements Array, Serializable, Cloneable
/*     */ {
/*     */   private Object[] elements;
/*     */   private int baseType;
/*     */   private String baseTypeName;
/*     */   private int len;
/*     */   static final long serialVersionUID = -8466174297270688520L;
/*     */ 
/*     */   public SerialArray(Array paramArray, Map<String, Class<?>> paramMap)
/*     */     throws SerialException, SQLException
/*     */   {
/* 141 */     if ((paramArray == null) || (paramMap == null)) {
/* 142 */       throw new SQLException("Cannot instantiate a SerialArray object with null parameters");
/*     */     }
/*     */ 
/* 146 */     if ((this.elements = (Object[])paramArray.getArray()) == null) {
/* 147 */       throw new SQLException("Invalid Array object. Calls to Array.getArray() return null value which cannot be serialized");
/*     */     }
/*     */ 
/* 151 */     this.elements = ((Object[])paramArray.getArray(paramMap));
/* 152 */     this.baseType = paramArray.getBaseType();
/* 153 */     this.baseTypeName = paramArray.getBaseTypeName();
/* 154 */     this.len = this.elements.length;
/*     */     int i;
/* 156 */     switch (this.baseType) {
/*     */     case 2002:
/* 158 */       for (i = 0; i < this.len; i++) {
/* 159 */         this.elements[i] = new SerialStruct((Struct)this.elements[i], paramMap);
/*     */       }
/* 161 */       break;
/*     */     case 2003:
/* 164 */       for (i = 0; i < this.len; i++) {
/* 165 */         this.elements[i] = new SerialArray((Cloneable)this.elements[i], paramMap);
/*     */       }
/* 167 */       break;
/*     */     case 2004:
/* 170 */       for (i = 0; i < this.len; i++) {
/* 171 */         this.elements[i] = new SerialBlob((Blob)this.elements[i]);
/*     */       }
/* 173 */       break;
/*     */     case 2005:
/* 176 */       for (i = 0; i < this.len; i++) {
/* 177 */         this.elements[i] = new SerialClob((Clob)this.elements[i]);
/*     */       }
/* 179 */       break;
/*     */     case 70:
/* 182 */       for (i = 0; i < this.len; i++) {
/* 183 */         this.elements[i] = new SerialDatalink((URL)this.elements[i]);
/*     */       }
/* 185 */       break;
/*     */     case 2000:
/* 188 */       for (i = 0; i < this.len; i++)
/* 189 */         this.elements[i] = new SerialJavaObject(this.elements[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void free()
/*     */     throws SQLException
/*     */   {
/* 214 */     throw new SQLFeatureNotSupportedException("Feature not supported");
/*     */   }
/*     */ 
/*     */   public SerialArray(Array paramArray)
/*     */     throws SerialException, SQLException
/*     */   {
/* 251 */     if (paramArray == null) {
/* 252 */       throw new SQLException("Cannot instantiate a SerialArray object with a null Array object");
/*     */     }
/*     */ 
/* 256 */     if ((this.elements = (Object[])paramArray.getArray()) == null) {
/* 257 */       throw new SQLException("Invalid Array object. Calls to Array.getArray() return null value which cannot be serialized");
/*     */     }
/*     */ 
/* 262 */     this.baseType = paramArray.getBaseType();
/* 263 */     this.baseTypeName = paramArray.getBaseTypeName();
/* 264 */     this.len = this.elements.length;
/*     */     int i;
/* 266 */     switch (this.baseType)
/*     */     {
/*     */     case 2004:
/* 269 */       for (i = 0; i < this.len; i++) {
/* 270 */         this.elements[i] = new SerialBlob((Blob)this.elements[i]);
/*     */       }
/* 272 */       break;
/*     */     case 2005:
/* 275 */       for (i = 0; i < this.len; i++) {
/* 276 */         this.elements[i] = new SerialClob((Clob)this.elements[i]);
/*     */       }
/* 278 */       break;
/*     */     case 70:
/* 281 */       for (i = 0; i < this.len; i++) {
/* 282 */         this.elements[i] = new SerialDatalink((URL)this.elements[i]);
/*     */       }
/* 284 */       break;
/*     */     case 2000:
/* 287 */       for (i = 0; i < this.len; i++)
/* 288 */         this.elements[i] = new SerialJavaObject(this.elements[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object getArray()
/*     */     throws SerialException
/*     */   {
/* 308 */     Object[] arrayOfObject = new Object[this.len];
/* 309 */     System.arraycopy(this.elements, 0, arrayOfObject, 0, this.len);
/* 310 */     return arrayOfObject;
/*     */   }
/*     */ 
/*     */   public Object getArray(Map<String, Class<?>> paramMap)
/*     */     throws SerialException
/*     */   {
/* 337 */     Object[] arrayOfObject = new Object[this.len];
/* 338 */     System.arraycopy(this.elements, 0, arrayOfObject, 0, this.len);
/* 339 */     return arrayOfObject;
/*     */   }
/*     */ 
/*     */   public Object getArray(long paramLong, int paramInt)
/*     */     throws SerialException
/*     */   {
/* 358 */     Object[] arrayOfObject = new Object[paramInt];
/* 359 */     System.arraycopy(this.elements, (int)paramLong, arrayOfObject, 0, paramInt);
/* 360 */     return arrayOfObject;
/*     */   }
/*     */ 
/*     */   public Object getArray(long paramLong, int paramInt, Map<String, Class<?>> paramMap)
/*     */     throws SerialException
/*     */   {
/* 394 */     Object[] arrayOfObject = new Object[paramInt];
/* 395 */     System.arraycopy(this.elements, (int)paramLong, arrayOfObject, 0, paramInt);
/* 396 */     return arrayOfObject;
/*     */   }
/*     */ 
/*     */   public int getBaseType()
/*     */     throws SerialException
/*     */   {
/* 409 */     return this.baseType;
/*     */   }
/*     */ 
/*     */   public String getBaseTypeName()
/*     */     throws SerialException
/*     */   {
/* 421 */     return this.baseTypeName;
/*     */   }
/*     */ 
/*     */   public ResultSet getResultSet(long paramLong, int paramInt)
/*     */     throws SerialException
/*     */   {
/* 444 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public ResultSet getResultSet(Map<String, Class<?>> paramMap)
/*     */     throws SerialException
/*     */   {
/* 473 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public ResultSet getResultSet()
/*     */     throws SerialException
/*     */   {
/* 490 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public ResultSet getResultSet(long paramLong, int paramInt, Map<String, Class<?>> paramMap)
/*     */     throws SerialException
/*     */   {
/* 527 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sql.rowset.serial.SerialArray
 * JD-Core Version:    0.6.2
 */