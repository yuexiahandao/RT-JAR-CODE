/*     */ package com.sun.corba.se.impl.encoding;
/*     */ 
/*     */ import com.sun.corba.se.impl.corba.TypeCodeImpl;
/*     */ import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
/*     */ import java.io.PrintStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.omg.CORBA_2_3.portable.InputStream;
/*     */ import sun.corba.EncapsInputStreamFactory;
/*     */ 
/*     */ public class TypeCodeInputStream extends EncapsInputStream
/*     */   implements TypeCodeReader
/*     */ {
/*  71 */   private Map typeMap = null;
/*  72 */   private InputStream enclosure = null;
/*  73 */   private boolean isEncapsulation = false;
/*     */ 
/*     */   public TypeCodeInputStream(org.omg.CORBA.ORB paramORB, byte[] paramArrayOfByte, int paramInt) {
/*  76 */     super(paramORB, paramArrayOfByte, paramInt);
/*     */   }
/*     */ 
/*     */   public TypeCodeInputStream(org.omg.CORBA.ORB paramORB, byte[] paramArrayOfByte, int paramInt, boolean paramBoolean, GIOPVersion paramGIOPVersion)
/*     */   {
/*  84 */     super(paramORB, paramArrayOfByte, paramInt, paramBoolean, paramGIOPVersion);
/*     */   }
/*     */ 
/*     */   public TypeCodeInputStream(org.omg.CORBA.ORB paramORB, ByteBuffer paramByteBuffer, int paramInt, boolean paramBoolean, GIOPVersion paramGIOPVersion)
/*     */   {
/*  92 */     super(paramORB, paramByteBuffer, paramInt, paramBoolean, paramGIOPVersion);
/*     */   }
/*     */ 
/*     */   public void addTypeCodeAtPosition(TypeCodeImpl paramTypeCodeImpl, int paramInt) {
/*  96 */     if (this.typeMap == null)
/*     */     {
/*  98 */       this.typeMap = new HashMap(16);
/*     */     }
/*     */ 
/* 101 */     this.typeMap.put(new Integer(paramInt), paramTypeCodeImpl);
/*     */   }
/*     */ 
/*     */   public TypeCodeImpl getTypeCodeAtPosition(int paramInt) {
/* 105 */     if (this.typeMap == null) {
/* 106 */       return null;
/*     */     }
/*     */ 
/* 111 */     return (TypeCodeImpl)this.typeMap.get(new Integer(paramInt));
/*     */   }
/*     */ 
/*     */   public void setEnclosingInputStream(InputStream paramInputStream) {
/* 115 */     this.enclosure = paramInputStream;
/*     */   }
/*     */ 
/*     */   public TypeCodeReader getTopLevelStream() {
/* 119 */     if (this.enclosure == null)
/* 120 */       return this;
/* 121 */     if ((this.enclosure instanceof TypeCodeReader))
/* 122 */       return ((TypeCodeReader)this.enclosure).getTopLevelStream();
/* 123 */     return this;
/*     */   }
/*     */ 
/*     */   public int getTopLevelPosition() {
/* 127 */     if ((this.enclosure != null) && ((this.enclosure instanceof TypeCodeReader)))
/*     */     {
/* 131 */       int i = ((TypeCodeReader)this.enclosure).getTopLevelPosition();
/*     */ 
/* 134 */       int j = i - getBufferLength() + getPosition();
/*     */ 
/* 141 */       return j;
/*     */     }
/*     */ 
/* 147 */     return getPosition();
/*     */   }
/*     */ 
/*     */   public static TypeCodeInputStream readEncapsulation(InputStream paramInputStream, org.omg.CORBA.ORB paramORB)
/*     */   {
/* 154 */     int i = paramInputStream.read_long();
/*     */ 
/* 157 */     byte[] arrayOfByte = new byte[i];
/* 158 */     paramInputStream.read_octet_array(arrayOfByte, 0, arrayOfByte.length);
/*     */     TypeCodeInputStream localTypeCodeInputStream;
/* 161 */     if ((paramInputStream instanceof CDRInputStream)) {
/* 162 */       localTypeCodeInputStream = EncapsInputStreamFactory.newTypeCodeInputStream((com.sun.corba.se.spi.orb.ORB)paramORB, arrayOfByte, arrayOfByte.length, ((CDRInputStream)paramInputStream).isLittleEndian(), ((CDRInputStream)paramInputStream).getGIOPVersion());
/*     */     }
/*     */     else
/*     */     {
/* 167 */       localTypeCodeInputStream = EncapsInputStreamFactory.newTypeCodeInputStream((com.sun.corba.se.spi.orb.ORB)paramORB, arrayOfByte, arrayOfByte.length);
/*     */     }
/*     */ 
/* 170 */     localTypeCodeInputStream.setEnclosingInputStream(paramInputStream);
/* 171 */     localTypeCodeInputStream.makeEncapsulation();
/*     */ 
/* 176 */     return localTypeCodeInputStream;
/*     */   }
/*     */ 
/*     */   protected void makeEncapsulation()
/*     */   {
/* 181 */     consumeEndian();
/* 182 */     this.isEncapsulation = true;
/*     */   }
/*     */ 
/*     */   public void printTypeMap() {
/* 186 */     System.out.println("typeMap = {");
/* 187 */     Iterator localIterator = this.typeMap.keySet().iterator();
/* 188 */     while (localIterator.hasNext()) {
/* 189 */       Integer localInteger = (Integer)localIterator.next();
/* 190 */       TypeCodeImpl localTypeCodeImpl = (TypeCodeImpl)this.typeMap.get(localInteger);
/* 191 */       System.out.println("  key = " + localInteger.intValue() + ", value = " + localTypeCodeImpl.description());
/*     */     }
/* 193 */     System.out.println("}");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.encoding.TypeCodeInputStream
 * JD-Core Version:    0.6.2
 */