/*     */ package com.sun.org.apache.bcel.internal.generic;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.classfile.Attribute;
/*     */ import com.sun.org.apache.bcel.internal.classfile.Constant;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantObject;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantPool;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantValue;
/*     */ import com.sun.org.apache.bcel.internal.classfile.Field;
/*     */ import com.sun.org.apache.bcel.internal.classfile.Utility;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ public class FieldGen extends FieldGenOrMethodGen
/*     */ {
/*  75 */   private Object value = null;
/*     */   private ArrayList observers;
/*     */ 
/*     */   public FieldGen(int access_flags, Type type, String name, ConstantPoolGen cp)
/*     */   {
/*  88 */     setAccessFlags(access_flags);
/*  89 */     setType(type);
/*  90 */     setName(name);
/*  91 */     setConstantPool(cp);
/*     */   }
/*     */ 
/*     */   public FieldGen(Field field, ConstantPoolGen cp)
/*     */   {
/* 101 */     this(field.getAccessFlags(), Type.getType(field.getSignature()), field.getName(), cp);
/*     */ 
/* 103 */     Attribute[] attrs = field.getAttributes();
/*     */ 
/* 105 */     for (int i = 0; i < attrs.length; i++)
/* 106 */       if ((attrs[i] instanceof ConstantValue))
/* 107 */         setValue(((ConstantValue)attrs[i]).getConstantValueIndex());
/*     */       else
/* 109 */         addAttribute(attrs[i]);
/*     */   }
/*     */ 
/*     */   private void setValue(int index)
/*     */   {
/* 114 */     ConstantPool cp = this.cp.getConstantPool();
/* 115 */     Constant c = cp.getConstant(index);
/* 116 */     this.value = ((ConstantObject)c).getConstantValue(cp);
/*     */   }
/*     */ 
/*     */   public void setInitValue(String str)
/*     */   {
/* 124 */     checkType(new ObjectType("java.lang.String"));
/*     */ 
/* 126 */     if (str != null)
/* 127 */       this.value = str;
/*     */   }
/*     */ 
/*     */   public void setInitValue(long l) {
/* 131 */     checkType(Type.LONG);
/*     */ 
/* 133 */     if (l != 0L)
/* 134 */       this.value = new Long(l);
/*     */   }
/*     */ 
/*     */   public void setInitValue(int i) {
/* 138 */     checkType(Type.INT);
/*     */ 
/* 140 */     if (i != 0)
/* 141 */       this.value = new Integer(i);
/*     */   }
/*     */ 
/*     */   public void setInitValue(short s) {
/* 145 */     checkType(Type.SHORT);
/*     */ 
/* 147 */     if (s != 0)
/* 148 */       this.value = new Integer(s);
/*     */   }
/*     */ 
/*     */   public void setInitValue(char c) {
/* 152 */     checkType(Type.CHAR);
/*     */ 
/* 154 */     if (c != 0)
/* 155 */       this.value = new Integer(c);
/*     */   }
/*     */ 
/*     */   public void setInitValue(byte b) {
/* 159 */     checkType(Type.BYTE);
/*     */ 
/* 161 */     if (b != 0)
/* 162 */       this.value = new Integer(b);
/*     */   }
/*     */ 
/*     */   public void setInitValue(boolean b) {
/* 166 */     checkType(Type.BOOLEAN);
/*     */ 
/* 168 */     if (b)
/* 169 */       this.value = new Integer(1);
/*     */   }
/*     */ 
/*     */   public void setInitValue(float f) {
/* 173 */     checkType(Type.FLOAT);
/*     */ 
/* 175 */     if (f != 0.0D)
/* 176 */       this.value = new Float(f);
/*     */   }
/*     */ 
/*     */   public void setInitValue(double d) {
/* 180 */     checkType(Type.DOUBLE);
/*     */ 
/* 182 */     if (d != 0.0D)
/* 183 */       this.value = new Double(d);
/*     */   }
/*     */ 
/*     */   public void cancelInitValue()
/*     */   {
/* 189 */     this.value = null;
/*     */   }
/*     */ 
/*     */   private void checkType(Type atype) {
/* 193 */     if (this.type == null) {
/* 194 */       throw new ClassGenException("You haven't defined the type of the field yet");
/*     */     }
/* 196 */     if (!isFinal()) {
/* 197 */       throw new ClassGenException("Only final fields may have an initial value!");
/*     */     }
/* 199 */     if (!this.type.equals(atype))
/* 200 */       throw new ClassGenException("Types are not compatible: " + this.type + " vs. " + atype);
/*     */   }
/*     */ 
/*     */   public Field getField()
/*     */   {
/* 207 */     String signature = getSignature();
/* 208 */     int name_index = this.cp.addUtf8(this.name);
/* 209 */     int signature_index = this.cp.addUtf8(signature);
/*     */ 
/* 211 */     if (this.value != null) {
/* 212 */       checkType(this.type);
/* 213 */       int index = addConstant();
/* 214 */       addAttribute(new ConstantValue(this.cp.addUtf8("ConstantValue"), 2, index, this.cp.getConstantPool()));
/*     */     }
/*     */ 
/* 218 */     return new Field(this.access_flags, name_index, signature_index, getAttributes(), this.cp.getConstantPool());
/*     */   }
/*     */ 
/*     */   private int addConstant()
/*     */   {
/* 223 */     switch (this.type.getType()) { case 4:
/*     */     case 5:
/*     */     case 8:
/*     */     case 9:
/*     */     case 10:
/* 226 */       return this.cp.addInteger(((Integer)this.value).intValue());
/*     */     case 6:
/* 229 */       return this.cp.addFloat(((Float)this.value).floatValue());
/*     */     case 7:
/* 232 */       return this.cp.addDouble(((Double)this.value).doubleValue());
/*     */     case 11:
/* 235 */       return this.cp.addLong(((Long)this.value).longValue());
/*     */     case 14:
/* 238 */       return this.cp.addString((String)this.value);
/*     */     case 12:
/*     */     case 13: }
/* 241 */     throw new RuntimeException("Oops: Unhandled : " + this.type.getType());
/*     */   }
/*     */ 
/*     */   public String getSignature() {
/* 245 */     return this.type.getSignature();
/*     */   }
/*     */ 
/*     */   public void addObserver(FieldObserver o)
/*     */   {
/* 252 */     if (this.observers == null) {
/* 253 */       this.observers = new ArrayList();
/*     */     }
/* 255 */     this.observers.add(o);
/*     */   }
/*     */ 
/*     */   public void removeObserver(FieldObserver o)
/*     */   {
/* 261 */     if (this.observers != null)
/* 262 */       this.observers.remove(o);
/*     */   }
/*     */ 
/*     */   public void update()
/*     */   {
/*     */     Iterator e;
/* 270 */     if (this.observers != null)
/* 271 */       for (e = this.observers.iterator(); e.hasNext(); )
/* 272 */         ((FieldObserver)e.next()).notify(this);
/*     */   }
/*     */ 
/*     */   public String getInitValue() {
/* 276 */     if (this.value != null) {
/* 277 */       return this.value.toString();
/*     */     }
/* 279 */     return null;
/*     */   }
/*     */ 
/*     */   public final String toString()
/*     */   {
/* 291 */     String access = Utility.accessToString(this.access_flags);
/* 292 */     access = access + " ";
/* 293 */     String signature = this.type.toString();
/* 294 */     String name = getName();
/*     */ 
/* 296 */     StringBuffer buf = new StringBuffer(access + signature + " " + name);
/* 297 */     String value = getInitValue();
/*     */ 
/* 299 */     if (value != null) {
/* 300 */       buf.append(" = " + value);
/*     */     }
/* 302 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   public FieldGen copy(ConstantPoolGen cp)
/*     */   {
/* 308 */     FieldGen fg = (FieldGen)clone();
/*     */ 
/* 310 */     fg.setConstantPool(cp);
/* 311 */     return fg;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.FieldGen
 * JD-Core Version:    0.6.2
 */