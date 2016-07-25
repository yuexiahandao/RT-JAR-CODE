/*     */ package com.sun.corba.se.impl.encoding;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import java.io.PrintStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.omg.CORBA.CompletionStatus;
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import sun.corba.EncapsInputStreamFactory;
/*     */ import sun.corba.OutputStreamFactory;
/*     */ 
/*     */ public final class TypeCodeOutputStream extends EncapsOutputStream
/*     */ {
/*  68 */   private org.omg.CORBA_2_3.portable.OutputStream enclosure = null;
/*  69 */   private Map typeMap = null;
/*  70 */   private boolean isEncapsulation = false;
/*     */ 
/*     */   public TypeCodeOutputStream(com.sun.corba.se.spi.orb.ORB paramORB) {
/*  73 */     super(paramORB, false);
/*     */   }
/*     */ 
/*     */   public TypeCodeOutputStream(com.sun.corba.se.spi.orb.ORB paramORB, boolean paramBoolean) {
/*  77 */     super(paramORB, paramBoolean);
/*     */   }
/*     */ 
/*     */   public InputStream create_input_stream()
/*     */   {
/*  82 */     TypeCodeInputStream localTypeCodeInputStream = EncapsInputStreamFactory.newTypeCodeInputStream((com.sun.corba.se.spi.orb.ORB)orb(), getByteBuffer(), getIndex(), isLittleEndian(), getGIOPVersion());
/*     */ 
/*  89 */     return localTypeCodeInputStream;
/*     */   }
/*     */ 
/*     */   public void setEnclosingOutputStream(org.omg.CORBA_2_3.portable.OutputStream paramOutputStream) {
/*  93 */     this.enclosure = paramOutputStream;
/*     */   }
/*     */ 
/*     */   public TypeCodeOutputStream getTopLevelStream()
/*     */   {
/* 110 */     if (this.enclosure == null)
/* 111 */       return this;
/* 112 */     if ((this.enclosure instanceof TypeCodeOutputStream))
/* 113 */       return ((TypeCodeOutputStream)this.enclosure).getTopLevelStream();
/* 114 */     return this;
/*     */   }
/*     */ 
/*     */   public int getTopLevelPosition() {
/* 118 */     if ((this.enclosure != null) && ((this.enclosure instanceof TypeCodeOutputStream))) {
/* 119 */       int i = ((TypeCodeOutputStream)this.enclosure).getTopLevelPosition() + getPosition();
/*     */ 
/* 122 */       if (this.isEncapsulation) i += 4;
/*     */ 
/* 130 */       return i;
/*     */     }
/*     */ 
/* 136 */     return getPosition();
/*     */   }
/*     */ 
/*     */   public void addIDAtPosition(String paramString, int paramInt) {
/* 140 */     if (this.typeMap == null) {
/* 141 */       this.typeMap = new HashMap(16);
/*     */     }
/* 143 */     this.typeMap.put(paramString, new Integer(paramInt));
/*     */   }
/*     */ 
/*     */   public int getPositionForID(String paramString) {
/* 147 */     if (this.typeMap == null) {
/* 148 */       throw this.wrapper.refTypeIndirType(CompletionStatus.COMPLETED_NO);
/*     */     }
/*     */ 
/* 151 */     return ((Integer)this.typeMap.get(paramString)).intValue();
/*     */   }
/*     */ 
/*     */   public void writeRawBuffer(org.omg.CORBA.portable.OutputStream paramOutputStream, int paramInt)
/*     */   {
/* 171 */     paramOutputStream.write_long(paramInt);
/*     */ 
/* 177 */     ByteBuffer localByteBuffer = getByteBuffer();
/* 178 */     if (localByteBuffer.hasArray())
/*     */     {
/* 180 */       paramOutputStream.write_octet_array(localByteBuffer.array(), 4, getIndex() - 4);
/*     */     }
/*     */     else
/*     */     {
/* 188 */       byte[] arrayOfByte = new byte[localByteBuffer.limit()];
/* 189 */       for (int i = 0; i < arrayOfByte.length; i++)
/* 190 */         arrayOfByte[i] = localByteBuffer.get(i);
/* 191 */       paramOutputStream.write_octet_array(arrayOfByte, 4, getIndex() - 4);
/*     */     }
/*     */   }
/*     */ 
/*     */   public TypeCodeOutputStream createEncapsulation(org.omg.CORBA.ORB paramORB)
/*     */   {
/* 201 */     TypeCodeOutputStream localTypeCodeOutputStream = OutputStreamFactory.newTypeCodeOutputStream((com.sun.corba.se.spi.orb.ORB)paramORB, isLittleEndian());
/*     */ 
/* 203 */     localTypeCodeOutputStream.setEnclosingOutputStream(this);
/* 204 */     localTypeCodeOutputStream.makeEncapsulation();
/*     */ 
/* 206 */     return localTypeCodeOutputStream;
/*     */   }
/*     */ 
/*     */   protected void makeEncapsulation()
/*     */   {
/* 211 */     putEndian();
/* 212 */     this.isEncapsulation = true;
/*     */   }
/*     */ 
/*     */   public static TypeCodeOutputStream wrapOutputStream(org.omg.CORBA_2_3.portable.OutputStream paramOutputStream) {
/* 216 */     boolean bool = (paramOutputStream instanceof CDROutputStream) ? ((CDROutputStream)paramOutputStream).isLittleEndian() : false;
/* 217 */     TypeCodeOutputStream localTypeCodeOutputStream = OutputStreamFactory.newTypeCodeOutputStream((com.sun.corba.se.spi.orb.ORB)paramOutputStream.orb(), bool);
/*     */ 
/* 219 */     localTypeCodeOutputStream.setEnclosingOutputStream(paramOutputStream);
/*     */ 
/* 221 */     return localTypeCodeOutputStream;
/*     */   }
/*     */ 
/*     */   public int getPosition() {
/* 225 */     return getIndex();
/*     */   }
/*     */ 
/*     */   public int getRealIndex(int paramInt) {
/* 229 */     int i = getTopLevelPosition();
/*     */ 
/* 232 */     return i;
/*     */   }
/*     */ 
/*     */   public byte[] getTypeCodeBuffer()
/*     */   {
/* 242 */     ByteBuffer localByteBuffer = getByteBuffer();
/*     */ 
/* 244 */     byte[] arrayOfByte = new byte[getIndex() - 4];
/*     */ 
/* 249 */     for (int i = 0; i < arrayOfByte.length; i++)
/* 250 */       arrayOfByte[i] = localByteBuffer.get(i + 4);
/* 251 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public void printTypeMap() {
/* 255 */     System.out.println("typeMap = {");
/* 256 */     Iterator localIterator = this.typeMap.keySet().iterator();
/* 257 */     while (localIterator.hasNext()) {
/* 258 */       String str = (String)localIterator.next();
/* 259 */       Integer localInteger = (Integer)this.typeMap.get(str);
/* 260 */       System.out.println("  key = " + str + ", value = " + localInteger);
/*     */     }
/* 262 */     System.out.println("}");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.encoding.TypeCodeOutputStream
 * JD-Core Version:    0.6.2
 */