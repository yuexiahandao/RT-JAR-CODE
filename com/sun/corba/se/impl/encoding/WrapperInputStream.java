/*     */ package com.sun.corba.se.impl.encoding;
/*     */ 
/*     */ import com.sun.corba.se.impl.corba.TypeCodeImpl;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Serializable;
/*     */ import java.math.BigDecimal;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.omg.CORBA.Any;
/*     */ import org.omg.CORBA.Context;
/*     */ import org.omg.CORBA.ORB;
/*     */ import org.omg.CORBA.Object;
/*     */ import org.omg.CORBA.Principal;
/*     */ import org.omg.CORBA.TypeCode;
/*     */ import org.omg.CORBA_2_3.portable.InputStream;
/*     */ 
/*     */ public class WrapperInputStream extends InputStream
/*     */   implements TypeCodeReader
/*     */ {
/*     */   private CDRInputStream stream;
/*  70 */   private Map typeMap = null;
/*  71 */   private int startPos = 0;
/*     */ 
/*     */   public WrapperInputStream(CDRInputStream paramCDRInputStream)
/*     */   {
/*  75 */     this.stream = paramCDRInputStream;
/*  76 */     this.startPos = this.stream.getPosition();
/*     */   }
/*     */   public int read() throws IOException {
/*  79 */     return this.stream.read(); } 
/*  80 */   public int read(byte[] paramArrayOfByte) throws IOException { return this.stream.read(paramArrayOfByte); } 
/*     */   public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException {
/*  82 */     return this.stream.read(paramArrayOfByte, paramInt1, paramInt2);
/*     */   }
/*  84 */   public long skip(long paramLong) throws IOException { return this.stream.skip(paramLong); } 
/*  85 */   public int available() throws IOException { return this.stream.available(); } 
/*  86 */   public void close() throws IOException { this.stream.close(); } 
/*  87 */   public void mark(int paramInt) { this.stream.mark(paramInt); } 
/*  88 */   public void reset() { this.stream.reset(); } 
/*  89 */   public boolean markSupported() { return this.stream.markSupported(); } 
/*  90 */   public int getPosition() { return this.stream.getPosition(); } 
/*  91 */   public void consumeEndian() { this.stream.consumeEndian(); } 
/*  92 */   public boolean read_boolean() { return this.stream.read_boolean(); } 
/*  93 */   public char read_char() { return this.stream.read_char(); } 
/*  94 */   public char read_wchar() { return this.stream.read_wchar(); } 
/*  95 */   public byte read_octet() { return this.stream.read_octet(); } 
/*  96 */   public short read_short() { return this.stream.read_short(); } 
/*  97 */   public short read_ushort() { return this.stream.read_ushort(); } 
/*  98 */   public int read_long() { return this.stream.read_long(); } 
/*  99 */   public int read_ulong() { return this.stream.read_ulong(); } 
/* 100 */   public long read_longlong() { return this.stream.read_longlong(); } 
/* 101 */   public long read_ulonglong() { return this.stream.read_ulonglong(); } 
/* 102 */   public float read_float() { return this.stream.read_float(); } 
/* 103 */   public double read_double() { return this.stream.read_double(); } 
/* 104 */   public String read_string() { return this.stream.read_string(); } 
/* 105 */   public String read_wstring() { return this.stream.read_wstring(); }
/*     */ 
/*     */   public void read_boolean_array(boolean[] paramArrayOfBoolean, int paramInt1, int paramInt2) {
/* 108 */     this.stream.read_boolean_array(paramArrayOfBoolean, paramInt1, paramInt2);
/*     */   }
/*     */   public void read_char_array(char[] paramArrayOfChar, int paramInt1, int paramInt2) {
/* 111 */     this.stream.read_char_array(paramArrayOfChar, paramInt1, paramInt2);
/*     */   }
/*     */   public void read_wchar_array(char[] paramArrayOfChar, int paramInt1, int paramInt2) {
/* 114 */     this.stream.read_wchar_array(paramArrayOfChar, paramInt1, paramInt2);
/*     */   }
/*     */   public void read_octet_array(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
/* 117 */     this.stream.read_octet_array(paramArrayOfByte, paramInt1, paramInt2);
/*     */   }
/*     */   public void read_short_array(short[] paramArrayOfShort, int paramInt1, int paramInt2) {
/* 120 */     this.stream.read_short_array(paramArrayOfShort, paramInt1, paramInt2);
/*     */   }
/*     */   public void read_ushort_array(short[] paramArrayOfShort, int paramInt1, int paramInt2) {
/* 123 */     this.stream.read_ushort_array(paramArrayOfShort, paramInt1, paramInt2);
/*     */   }
/*     */   public void read_long_array(int[] paramArrayOfInt, int paramInt1, int paramInt2) {
/* 126 */     this.stream.read_long_array(paramArrayOfInt, paramInt1, paramInt2);
/*     */   }
/*     */   public void read_ulong_array(int[] paramArrayOfInt, int paramInt1, int paramInt2) {
/* 129 */     this.stream.read_ulong_array(paramArrayOfInt, paramInt1, paramInt2);
/*     */   }
/*     */   public void read_longlong_array(long[] paramArrayOfLong, int paramInt1, int paramInt2) {
/* 132 */     this.stream.read_longlong_array(paramArrayOfLong, paramInt1, paramInt2);
/*     */   }
/*     */   public void read_ulonglong_array(long[] paramArrayOfLong, int paramInt1, int paramInt2) {
/* 135 */     this.stream.read_ulonglong_array(paramArrayOfLong, paramInt1, paramInt2);
/*     */   }
/*     */   public void read_float_array(float[] paramArrayOfFloat, int paramInt1, int paramInt2) {
/* 138 */     this.stream.read_float_array(paramArrayOfFloat, paramInt1, paramInt2);
/*     */   }
/*     */   public void read_double_array(double[] paramArrayOfDouble, int paramInt1, int paramInt2) {
/* 141 */     this.stream.read_double_array(paramArrayOfDouble, paramInt1, paramInt2);
/*     */   }
/*     */   public Object read_Object() {
/* 144 */     return this.stream.read_Object(); } 
/* 145 */   public Serializable read_value() { return this.stream.read_value(); } 
/* 146 */   public TypeCode read_TypeCode() { return this.stream.read_TypeCode(); } 
/* 147 */   public Any read_any() { return this.stream.read_any(); } 
/* 148 */   public Principal read_Principal() { return this.stream.read_Principal(); } 
/* 149 */   public BigDecimal read_fixed() { return this.stream.read_fixed(); } 
/* 150 */   public Context read_Context() { return this.stream.read_Context(); } 
/*     */   public ORB orb() {
/* 152 */     return this.stream.orb();
/*     */   }
/*     */   public void addTypeCodeAtPosition(TypeCodeImpl paramTypeCodeImpl, int paramInt) {
/* 155 */     if (this.typeMap == null)
/*     */     {
/* 157 */       this.typeMap = new HashMap(16);
/*     */     }
/*     */ 
/* 160 */     this.typeMap.put(new Integer(paramInt), paramTypeCodeImpl);
/*     */   }
/*     */ 
/*     */   public TypeCodeImpl getTypeCodeAtPosition(int paramInt) {
/* 164 */     if (this.typeMap == null) {
/* 165 */       return null;
/*     */     }
/*     */ 
/* 168 */     return (TypeCodeImpl)this.typeMap.get(new Integer(paramInt));
/*     */   }
/*     */ 
/*     */   public void setEnclosingInputStream(InputStream paramInputStream)
/*     */   {
/*     */   }
/*     */ 
/*     */   public TypeCodeReader getTopLevelStream()
/*     */   {
/* 177 */     return this;
/*     */   }
/*     */ 
/*     */   public int getTopLevelPosition()
/*     */   {
/* 184 */     return getPosition() - this.startPos;
/*     */   }
/*     */ 
/*     */   public void performORBVersionSpecificInit()
/*     */   {
/* 190 */     this.stream.performORBVersionSpecificInit();
/*     */   }
/*     */ 
/*     */   public void resetCodeSetConverters() {
/* 194 */     this.stream.resetCodeSetConverters();
/*     */   }
/*     */ 
/*     */   public void printTypeMap()
/*     */   {
/* 200 */     System.out.println("typeMap = {");
/* 201 */     ArrayList localArrayList = new ArrayList(this.typeMap.keySet());
/* 202 */     Collections.sort(localArrayList);
/* 203 */     Iterator localIterator = localArrayList.iterator();
/* 204 */     while (localIterator.hasNext()) {
/* 205 */       Integer localInteger = (Integer)localIterator.next();
/* 206 */       TypeCodeImpl localTypeCodeImpl = (TypeCodeImpl)this.typeMap.get(localInteger);
/* 207 */       System.out.println("  key = " + localInteger.intValue() + ", value = " + localTypeCodeImpl.description());
/*     */     }
/* 209 */     System.out.println("}");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.encoding.WrapperInputStream
 * JD-Core Version:    0.6.2
 */