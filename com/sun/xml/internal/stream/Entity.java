/*     */ package com.sun.xml.internal.stream;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
/*     */ import com.sun.xml.internal.stream.util.BufferAllocator;
/*     */ import com.sun.xml.internal.stream.util.ThreadLocalBufferAllocator;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ 
/*     */ public abstract class Entity
/*     */ {
/*     */   public String name;
/*     */   public boolean inExternalSubset;
/*     */ 
/*     */   public Entity()
/*     */   {
/*  57 */     clear();
/*     */   }
/*     */ 
/*     */   public Entity(String name, boolean inExternalSubset)
/*     */   {
/*  62 */     this.name = name;
/*  63 */     this.inExternalSubset = inExternalSubset;
/*     */   }
/*     */ 
/*     */   public boolean isEntityDeclInExternalSubset()
/*     */   {
/*  72 */     return this.inExternalSubset;
/*     */   }
/*     */ 
/*     */   public abstract boolean isExternal();
/*     */ 
/*     */   public abstract boolean isUnparsed();
/*     */ 
/*     */   public void clear()
/*     */   {
/*  83 */     this.name = null;
/*  84 */     this.inExternalSubset = false;
/*     */   }
/*     */ 
/*     */   public void setValues(Entity entity)
/*     */   {
/*  89 */     this.name = entity.name;
/*  90 */     this.inExternalSubset = entity.inExternalSubset;
/*     */   }
/*     */ 
/*     */   public static class ExternalEntity extends Entity
/*     */   {
/*     */     public XMLResourceIdentifier entityLocation;
/*     */     public String notation;
/*     */ 
/*     */     public ExternalEntity()
/*     */     {
/* 182 */       clear();
/*     */     }
/*     */ 
/*     */     public ExternalEntity(String name, XMLResourceIdentifier entityLocation, String notation, boolean inExternalSubset)
/*     */     {
/* 188 */       super(inExternalSubset);
/* 189 */       this.entityLocation = entityLocation;
/* 190 */       this.notation = notation;
/*     */     }
/*     */ 
/*     */     public final boolean isExternal()
/*     */     {
/* 199 */       return true;
/*     */     }
/*     */ 
/*     */     public final boolean isUnparsed()
/*     */     {
/* 204 */       return this.notation != null;
/*     */     }
/*     */ 
/*     */     public void clear()
/*     */     {
/* 209 */       super.clear();
/* 210 */       this.entityLocation = null;
/* 211 */       this.notation = null;
/*     */     }
/*     */ 
/*     */     public void setValues(Entity entity)
/*     */     {
/* 216 */       super.setValues(entity);
/* 217 */       this.entityLocation = null;
/* 218 */       this.notation = null;
/*     */     }
/*     */ 
/*     */     public void setValues(ExternalEntity entity)
/*     */     {
/* 223 */       super.setValues(entity);
/* 224 */       this.entityLocation = entity.entityLocation;
/* 225 */       this.notation = entity.notation;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class InternalEntity extends Entity
/*     */   {
/*     */     public String text;
/*     */ 
/*     */     public InternalEntity()
/*     */     {
/* 115 */       clear();
/*     */     }
/*     */ 
/*     */     public InternalEntity(String name, String text, boolean inExternalSubset)
/*     */     {
/* 120 */       super(inExternalSubset);
/* 121 */       this.text = text;
/*     */     }
/*     */ 
/*     */     public final boolean isExternal()
/*     */     {
/* 130 */       return false;
/*     */     }
/*     */ 
/*     */     public final boolean isUnparsed()
/*     */     {
/* 135 */       return false;
/*     */     }
/*     */ 
/*     */     public void clear()
/*     */     {
/* 140 */       super.clear();
/* 141 */       this.text = null;
/*     */     }
/*     */ 
/*     */     public void setValues(Entity entity)
/*     */     {
/* 146 */       super.setValues(entity);
/* 147 */       this.text = null;
/*     */     }
/*     */ 
/*     */     public void setValues(InternalEntity entity)
/*     */     {
/* 152 */       super.setValues(entity);
/* 153 */       this.text = entity.text;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class ScannedEntity extends Entity
/*     */   {
/*     */     public static final int DEFAULT_BUFFER_SIZE = 8192;
/* 248 */     public int fBufferSize = 8192;
/*     */     public static final int DEFAULT_XMLDECL_BUFFER_SIZE = 28;
/*     */     public static final int DEFAULT_INTERNAL_BUFFER_SIZE = 1024;
/*     */     public InputStream stream;
/*     */     public Reader reader;
/*     */     public XMLResourceIdentifier entityLocation;
/*     */     public String encoding;
/*     */     public boolean literal;
/*     */     public boolean isExternal;
/*     */     public String version;
/* 294 */     public char[] ch = null;
/*     */     public int position;
/*     */     public int count;
/* 303 */     public int lineNumber = 1;
/*     */ 
/* 306 */     public int columnNumber = 1;
/*     */ 
/* 309 */     boolean declaredEncoding = false;
/*     */ 
/* 317 */     boolean externallySpecifiedEncoding = false;
/*     */ 
/* 320 */     public String xmlVersion = "1.0";
/*     */     public int fTotalCountTillLastLoad;
/*     */     public int fLastCount;
/*     */     public int baseCharOffset;
/*     */     public int startPosition;
/*     */     public boolean mayReadChunks;
/* 345 */     public boolean xmlDeclChunkRead = false;
/*     */ 
/*     */     public String getEncodingName()
/*     */     {
/* 351 */       return this.encoding;
/*     */     }
/*     */ 
/*     */     public String getEntityVersion()
/*     */     {
/* 358 */       return this.version;
/*     */     }
/*     */ 
/*     */     public void setEntityVersion(String version)
/*     */     {
/* 365 */       this.version = version;
/*     */     }
/*     */ 
/*     */     public Reader getEntityReader()
/*     */     {
/* 374 */       return this.reader;
/*     */     }
/*     */ 
/*     */     public InputStream getEntityInputStream()
/*     */     {
/* 383 */       return this.stream;
/*     */     }
/*     */ 
/*     */     public ScannedEntity(String name, XMLResourceIdentifier entityLocation, InputStream stream, Reader reader, String encoding, boolean literal, boolean mayReadChunks, boolean isExternal)
/*     */     {
/* 395 */       this.name = name;
/* 396 */       this.entityLocation = entityLocation;
/* 397 */       this.stream = stream;
/* 398 */       this.reader = reader;
/* 399 */       this.encoding = encoding;
/* 400 */       this.literal = literal;
/* 401 */       this.mayReadChunks = mayReadChunks;
/* 402 */       this.isExternal = isExternal;
/* 403 */       int size = isExternal ? this.fBufferSize : 1024;
/* 404 */       BufferAllocator ba = ThreadLocalBufferAllocator.getBufferAllocator();
/* 405 */       this.ch = ba.getCharBuffer(size);
/* 406 */       if (this.ch == null)
/* 407 */         this.ch = new char[size];
/*     */     }
/*     */ 
/*     */     public void close()
/*     */       throws IOException
/*     */     {
/* 415 */       BufferAllocator ba = ThreadLocalBufferAllocator.getBufferAllocator();
/* 416 */       ba.returnCharBuffer(this.ch);
/* 417 */       this.ch = null;
/* 418 */       this.reader.close();
/*     */     }
/*     */ 
/*     */     public boolean isEncodingExternallySpecified()
/*     */     {
/* 427 */       return this.externallySpecifiedEncoding;
/*     */     }
/*     */ 
/*     */     public void setEncodingExternallySpecified(boolean value)
/*     */     {
/* 432 */       this.externallySpecifiedEncoding = value;
/*     */     }
/*     */ 
/*     */     public boolean isDeclaredEncoding() {
/* 436 */       return this.declaredEncoding;
/*     */     }
/*     */ 
/*     */     public void setDeclaredEncoding(boolean value) {
/* 440 */       this.declaredEncoding = value;
/*     */     }
/*     */ 
/*     */     public final boolean isExternal()
/*     */     {
/* 445 */       return this.isExternal;
/*     */     }
/*     */ 
/*     */     public final boolean isUnparsed()
/*     */     {
/* 450 */       return false;
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 460 */       StringBuffer str = new StringBuffer();
/* 461 */       str.append("name=\"" + this.name + '"');
/* 462 */       str.append(",ch=" + new String(this.ch));
/* 463 */       str.append(",position=" + this.position);
/* 464 */       str.append(",count=" + this.count);
/* 465 */       return str.toString();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.stream.Entity
 * JD-Core Version:    0.6.2
 */