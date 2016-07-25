/*     */ package com.sun.java.util.jar.pack;
/*     */ 
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ class ClassWriter
/*     */ {
/*     */   int verbose;
/*     */   Package pkg;
/*     */   Package.Class cls;
/*     */   DataOutputStream out;
/*     */   ConstantPool.Index cpIndex;
/* 190 */   ByteArrayOutputStream buf = new ByteArrayOutputStream();
/* 191 */   DataOutputStream bufOut = new DataOutputStream(this.buf);
/*     */ 
/*     */   ClassWriter(Package.Class paramClass, OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/*  55 */     this.pkg = paramClass.getPackage();
/*  56 */     this.cls = paramClass;
/*  57 */     this.verbose = this.pkg.verbose;
/*  58 */     this.out = new DataOutputStream(new BufferedOutputStream(paramOutputStream));
/*  59 */     this.cpIndex = ConstantPool.makeIndex(paramClass.toString(), paramClass.getCPMap());
/*  60 */     this.cpIndex.flattenSigs = true;
/*  61 */     if (this.verbose > 1)
/*  62 */       Utils.log.fine("local CP=" + (this.verbose > 2 ? this.cpIndex.dumpString() : this.cpIndex.toString()));
/*     */   }
/*     */ 
/*     */   private void writeShort(int paramInt) throws IOException {
/*  66 */     this.out.writeShort(paramInt);
/*     */   }
/*     */ 
/*     */   private void writeInt(int paramInt) throws IOException {
/*  70 */     this.out.writeInt(paramInt);
/*     */   }
/*     */ 
/*     */   private void writeRef(ConstantPool.Entry paramEntry) throws IOException
/*     */   {
/*  75 */     int i = paramEntry == null ? 0 : this.cpIndex.indexOf(paramEntry);
/*  76 */     writeShort(i);
/*     */   }
/*     */ 
/*     */   void write() throws IOException {
/*  80 */     int i = 0;
/*     */     try {
/*  82 */       if (this.verbose > 1) Utils.log.fine("...writing " + this.cls);
/*  83 */       writeMagicNumbers();
/*  84 */       writeConstantPool();
/*  85 */       writeHeader();
/*  86 */       writeMembers(false);
/*  87 */       writeMembers(true);
/*  88 */       writeAttributes(0, this.cls);
/*     */ 
/*  94 */       this.out.flush();
/*  95 */       i = 1;
/*     */     } finally {
/*  97 */       if (i == 0)
/*  98 */         Utils.log.warning("Error on output of " + this.cls);
/*     */     }
/*     */   }
/*     */ 
/*     */   void writeMagicNumbers() throws IOException
/*     */   {
/* 104 */     writeInt(this.cls.magic);
/* 105 */     writeShort(this.cls.minver);
/* 106 */     writeShort(this.cls.majver);
/*     */   }
/*     */ 
/*     */   void writeConstantPool() throws IOException {
/* 110 */     ConstantPool.Entry[] arrayOfEntry = this.cls.cpMap;
/* 111 */     writeShort(arrayOfEntry.length);
/* 112 */     for (int i = 0; i < arrayOfEntry.length; i++) {
/* 113 */       ConstantPool.Entry localEntry = arrayOfEntry[i];
/* 114 */       if (!$assertionsDisabled) if ((localEntry == null ? 1 : 0) != ((i == 0) || ((arrayOfEntry[(i - 1)] != null) && (arrayOfEntry[(i - 1)].isDoubleWord())) ? 1 : 0)) throw new AssertionError();
/* 115 */       if (localEntry != null) {
/* 116 */         int j = localEntry.getTag();
/* 117 */         if (this.verbose > 2) Utils.log.fine("   CP[" + i + "] = " + localEntry);
/* 118 */         this.out.write(j);
/* 119 */         switch (j) {
/*     */         case 13:
/* 121 */           if (!$assertionsDisabled) throw new AssertionError();
/*     */           break;
/*     */         case 1:
/* 124 */           this.out.writeUTF(localEntry.stringValue());
/* 125 */           break;
/*     */         case 3:
/* 127 */           this.out.writeInt(((ConstantPool.NumberEntry)localEntry).numberValue().intValue());
/* 128 */           break;
/*     */         case 4:
/* 130 */           float f = ((ConstantPool.NumberEntry)localEntry).numberValue().floatValue();
/* 131 */           this.out.writeInt(Float.floatToRawIntBits(f));
/* 132 */           break;
/*     */         case 5:
/* 134 */           this.out.writeLong(((ConstantPool.NumberEntry)localEntry).numberValue().longValue());
/* 135 */           break;
/*     */         case 6:
/* 137 */           double d = ((ConstantPool.NumberEntry)localEntry).numberValue().doubleValue();
/* 138 */           this.out.writeLong(Double.doubleToRawLongBits(d));
/* 139 */           break;
/*     */         case 7:
/*     */         case 8:
/* 142 */           writeRef(localEntry.getRef(0));
/* 143 */           break;
/*     */         case 9:
/*     */         case 10:
/*     */         case 11:
/*     */         case 12:
/* 148 */           writeRef(localEntry.getRef(0));
/* 149 */           writeRef(localEntry.getRef(1));
/* 150 */           break;
/*     */         case 2:
/*     */         default:
/* 152 */           throw new IOException("Bad constant pool tag " + j);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/* 158 */   void writeHeader() throws IOException { writeShort(this.cls.flags);
/* 159 */     writeRef(this.cls.thisClass);
/* 160 */     writeRef(this.cls.superClass);
/* 161 */     writeShort(this.cls.interfaces.length);
/* 162 */     for (int i = 0; i < this.cls.interfaces.length; i++)
/* 163 */       writeRef(this.cls.interfaces[i]);
/*     */   }
/*     */ 
/*     */   void writeMembers(boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/*     */     List localList;
/* 169 */     if (!paramBoolean)
/* 170 */       localList = this.cls.getFields();
/*     */     else
/* 172 */       localList = this.cls.getMethods();
/* 173 */     writeShort(localList.size());
/* 174 */     for (Iterator localIterator = localList.iterator(); localIterator.hasNext(); ) {
/* 175 */       Package.Class.Member localMember = (Package.Class.Member)localIterator.next();
/* 176 */       writeMember(localMember, paramBoolean);
/*     */     }
/*     */   }
/*     */ 
/*     */   void writeMember(Package.Class.Member paramMember, boolean paramBoolean) throws IOException {
/* 181 */     if (this.verbose > 2) Utils.log.fine("writeMember " + paramMember);
/* 182 */     writeShort(paramMember.flags);
/* 183 */     writeRef(paramMember.getDescriptor().nameRef);
/* 184 */     writeRef(paramMember.getDescriptor().typeRef);
/* 185 */     writeAttributes(!paramBoolean ? 1 : 2, paramMember);
/*     */   }
/*     */ 
/*     */   void writeAttributes(int paramInt, Attribute.Holder paramHolder)
/*     */     throws IOException
/*     */   {
/* 194 */     if (paramHolder.attributes == null) {
/* 195 */       writeShort(0);
/* 196 */       return;
/*     */     }
/* 198 */     writeShort(paramHolder.attributes.size());
/* 199 */     for (Attribute localAttribute : paramHolder.attributes) {
/* 200 */       localAttribute.finishRefs(this.cpIndex);
/* 201 */       writeRef(localAttribute.getNameRef());
/* 202 */       if ((localAttribute.layout() == Package.attrCodeEmpty) || (localAttribute.layout() == Package.attrInnerClassesEmpty))
/*     */       {
/* 205 */         DataOutputStream localDataOutputStream = this.out;
/* 206 */         assert (this.out != this.bufOut);
/* 207 */         this.buf.reset();
/* 208 */         this.out = this.bufOut;
/* 209 */         if ("Code".equals(localAttribute.name())) {
/* 210 */           Package.Class.Method localMethod = (Package.Class.Method)paramHolder;
/* 211 */           writeCode(localMethod.code);
/*     */         } else {
/* 213 */           assert (paramHolder == this.cls);
/* 214 */           writeInnerClasses(this.cls);
/*     */         }
/* 216 */         this.out = localDataOutputStream;
/* 217 */         if (this.verbose > 2)
/* 218 */           Utils.log.fine("Attribute " + localAttribute.name() + " [" + this.buf.size() + "]");
/* 219 */         writeInt(this.buf.size());
/* 220 */         this.buf.writeTo(this.out);
/*     */       } else {
/* 222 */         if (this.verbose > 2)
/* 223 */           Utils.log.fine("Attribute " + localAttribute.name() + " [" + localAttribute.size() + "]");
/* 224 */         writeInt(localAttribute.size());
/* 225 */         this.out.write(localAttribute.bytes());
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   void writeCode(Code paramCode) throws IOException {
/* 231 */     paramCode.finishRefs(this.cpIndex);
/* 232 */     writeShort(paramCode.max_stack);
/* 233 */     writeShort(paramCode.max_locals);
/* 234 */     writeInt(paramCode.bytes.length);
/* 235 */     this.out.write(paramCode.bytes);
/* 236 */     int i = paramCode.getHandlerCount();
/* 237 */     writeShort(i);
/* 238 */     for (int j = 0; j < i; j++) {
/* 239 */       writeShort(paramCode.handler_start[j]);
/* 240 */       writeShort(paramCode.handler_end[j]);
/* 241 */       writeShort(paramCode.handler_catch[j]);
/* 242 */       writeRef(paramCode.handler_class[j]);
/*     */     }
/* 244 */     writeAttributes(3, paramCode);
/*     */   }
/*     */ 
/*     */   void writeInnerClasses(Package.Class paramClass) throws IOException {
/* 248 */     List localList = paramClass.getInnerClasses();
/* 249 */     writeShort(localList.size());
/* 250 */     for (Package.InnerClass localInnerClass : localList) {
/* 251 */       writeRef(localInnerClass.thisClass);
/* 252 */       writeRef(localInnerClass.outerClass);
/* 253 */       writeRef(localInnerClass.name);
/* 254 */       writeShort(localInnerClass.flags);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.util.jar.pack.ClassWriter
 * JD-Core Version:    0.6.2
 */