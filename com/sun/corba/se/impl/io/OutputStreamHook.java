/*     */ package com.sun.corba.se.impl.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.ObjectOutputStream.PutField;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.omg.CORBA.portable.ValueOutputStream;
/*     */ import org.omg.CORBA_2_3.portable.OutputStream;
/*     */ 
/*     */ public abstract class OutputStreamHook extends ObjectOutputStream
/*     */ {
/*  45 */   private HookPutFields putFields = null;
/*     */ 
/* 162 */   protected byte streamFormatVersion = 1;
/*     */ 
/* 193 */   protected WriteObjectState writeObjectState = NOT_IN_WRITE_OBJECT;
/*     */ 
/* 213 */   protected static final WriteObjectState NOT_IN_WRITE_OBJECT = new DefaultState();
/* 214 */   protected static final WriteObjectState IN_WRITE_OBJECT = new InWriteObjectState();
/* 215 */   protected static final WriteObjectState WROTE_DEFAULT_DATA = new WroteDefaultDataState();
/* 216 */   protected static final WriteObjectState WROTE_CUSTOM_DATA = new WroteCustomDataState();
/*     */ 
/*     */   abstract void writeField(ObjectStreamField paramObjectStreamField, Object paramObject)
/*     */     throws IOException;
/*     */ 
/*     */   public OutputStreamHook()
/*     */     throws IOException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void defaultWriteObject()
/*     */     throws IOException
/*     */   {
/* 148 */     this.writeObjectState.defaultWriteObject(this);
/*     */ 
/* 150 */     defaultWriteObjectDelegate();
/*     */   }
/*     */ 
/*     */   public abstract void defaultWriteObjectDelegate();
/*     */ 
/*     */   public ObjectOutputStream.PutField putFields() throws IOException
/*     */   {
/* 157 */     this.putFields = new HookPutFields(null);
/* 158 */     return this.putFields;
/*     */   }
/*     */ 
/*     */   public byte getStreamFormatVersion()
/*     */   {
/* 167 */     return this.streamFormatVersion;
/*     */   }
/*     */ 
/*     */   abstract ObjectStreamField[] getFieldsNoCopy();
/*     */ 
/*     */   public void writeFields()
/*     */     throws IOException
/*     */   {
/* 177 */     this.writeObjectState.defaultWriteObject(this);
/*     */ 
/* 179 */     this.putFields.write(this);
/*     */   }
/*     */ 
/*     */   abstract OutputStream getOrbStream();
/*     */ 
/*     */   protected abstract void beginOptionalCustomData();
/*     */ 
/*     */   protected void setState(WriteObjectState paramWriteObjectState)
/*     */   {
/* 196 */     this.writeObjectState = paramWriteObjectState;
/*     */   }
/*     */ 
/*     */   protected static class DefaultState extends OutputStreamHook.WriteObjectState
/*     */   {
/*     */     public void enterWriteObject(OutputStreamHook paramOutputStreamHook)
/*     */       throws IOException
/*     */     {
/* 209 */       paramOutputStreamHook.setState(OutputStreamHook.IN_WRITE_OBJECT);
/*     */     }
/*     */   }
/*     */ 
/*     */   private class HookPutFields extends ObjectOutputStream.PutField
/*     */   {
/*  53 */     private Map<String, Object> fields = new HashMap();
/*     */ 
/*     */     private HookPutFields() {
/*     */     }
/*     */ 
/*     */     public void put(String paramString, boolean paramBoolean) {
/*  59 */       this.fields.put(paramString, new Boolean(paramBoolean));
/*     */     }
/*     */ 
/*     */     public void put(String paramString, char paramChar)
/*     */     {
/*  66 */       this.fields.put(paramString, new Character(paramChar));
/*     */     }
/*     */ 
/*     */     public void put(String paramString, byte paramByte)
/*     */     {
/*  73 */       this.fields.put(paramString, new Byte(paramByte));
/*     */     }
/*     */ 
/*     */     public void put(String paramString, short paramShort)
/*     */     {
/*  80 */       this.fields.put(paramString, new Short(paramShort));
/*     */     }
/*     */ 
/*     */     public void put(String paramString, int paramInt)
/*     */     {
/*  87 */       this.fields.put(paramString, new Integer(paramInt));
/*     */     }
/*     */ 
/*     */     public void put(String paramString, long paramLong)
/*     */     {
/*  94 */       this.fields.put(paramString, new Long(paramLong));
/*     */     }
/*     */ 
/*     */     public void put(String paramString, float paramFloat)
/*     */     {
/* 102 */       this.fields.put(paramString, new Float(paramFloat));
/*     */     }
/*     */ 
/*     */     public void put(String paramString, double paramDouble)
/*     */     {
/* 109 */       this.fields.put(paramString, new Double(paramDouble));
/*     */     }
/*     */ 
/*     */     public void put(String paramString, Object paramObject)
/*     */     {
/* 116 */       this.fields.put(paramString, paramObject);
/*     */     }
/*     */ 
/*     */     public void write(ObjectOutput paramObjectOutput)
/*     */       throws IOException
/*     */     {
/* 123 */       OutputStreamHook localOutputStreamHook = (OutputStreamHook)paramObjectOutput;
/*     */ 
/* 125 */       ObjectStreamField[] arrayOfObjectStreamField = localOutputStreamHook.getFieldsNoCopy();
/*     */ 
/* 130 */       for (int i = 0; i < arrayOfObjectStreamField.length; i++)
/*     */       {
/* 132 */         Object localObject = this.fields.get(arrayOfObjectStreamField[i].getName());
/*     */ 
/* 134 */         localOutputStreamHook.writeField(arrayOfObjectStreamField[i], localObject);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static class InWriteObjectState extends OutputStreamHook.WriteObjectState
/*     */   {
/*     */     public void enterWriteObject(OutputStreamHook paramOutputStreamHook)
/*     */       throws IOException
/*     */     {
/* 222 */       throw new IOException("Internal state failure: Entered writeObject twice");
/*     */     }
/*     */ 
/*     */     public void exitWriteObject(OutputStreamHook paramOutputStreamHook)
/*     */       throws IOException
/*     */     {
/* 229 */       paramOutputStreamHook.getOrbStream().write_boolean(false);
/*     */ 
/* 234 */       if (paramOutputStreamHook.getStreamFormatVersion() == 2) {
/* 235 */         paramOutputStreamHook.getOrbStream().write_long(0);
/*     */       }
/* 237 */       paramOutputStreamHook.setState(OutputStreamHook.NOT_IN_WRITE_OBJECT);
/*     */     }
/*     */ 
/*     */     public void defaultWriteObject(OutputStreamHook paramOutputStreamHook)
/*     */       throws IOException
/*     */     {
/* 245 */       paramOutputStreamHook.getOrbStream().write_boolean(true);
/*     */ 
/* 247 */       paramOutputStreamHook.setState(OutputStreamHook.WROTE_DEFAULT_DATA);
/*     */     }
/*     */ 
/*     */     public void writeData(OutputStreamHook paramOutputStreamHook)
/*     */       throws IOException
/*     */     {
/* 257 */       paramOutputStreamHook.getOrbStream().write_boolean(false);
/* 258 */       paramOutputStreamHook.beginOptionalCustomData();
/* 259 */       paramOutputStreamHook.setState(OutputStreamHook.WROTE_CUSTOM_DATA);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static class WriteObjectState
/*     */   {
/*     */     public void enterWriteObject(OutputStreamHook paramOutputStreamHook)
/*     */       throws IOException
/*     */     {
/*     */     }
/*     */ 
/*     */     public void exitWriteObject(OutputStreamHook paramOutputStreamHook)
/*     */       throws IOException
/*     */     {
/*     */     }
/*     */ 
/*     */     public void defaultWriteObject(OutputStreamHook paramOutputStreamHook)
/*     */       throws IOException
/*     */     {
/*     */     }
/*     */ 
/*     */     public void writeData(OutputStreamHook paramOutputStreamHook)
/*     */       throws IOException
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static class WroteCustomDataState extends OutputStreamHook.InWriteObjectState
/*     */   {
/*     */     public void exitWriteObject(OutputStreamHook paramOutputStreamHook)
/*     */       throws IOException
/*     */     {
/* 297 */       if (paramOutputStreamHook.getStreamFormatVersion() == 2) {
/* 298 */         ((ValueOutputStream)paramOutputStreamHook.getOrbStream()).end_value();
/*     */       }
/* 300 */       paramOutputStreamHook.setState(OutputStreamHook.NOT_IN_WRITE_OBJECT);
/*     */     }
/*     */ 
/*     */     public void defaultWriteObject(OutputStreamHook paramOutputStreamHook) throws IOException
/*     */     {
/* 305 */       throw new IOException("Cannot call defaultWriteObject/writeFields after writing custom data in RMI-IIOP");
/*     */     }
/*     */ 
/*     */     public void writeData(OutputStreamHook paramOutputStreamHook)
/*     */       throws IOException
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static class WroteDefaultDataState extends OutputStreamHook.InWriteObjectState
/*     */   {
/*     */     public void exitWriteObject(OutputStreamHook paramOutputStreamHook)
/*     */       throws IOException
/*     */     {
/* 270 */       if (paramOutputStreamHook.getStreamFormatVersion() == 2) {
/* 271 */         paramOutputStreamHook.getOrbStream().write_long(0);
/*     */       }
/* 273 */       paramOutputStreamHook.setState(OutputStreamHook.NOT_IN_WRITE_OBJECT);
/*     */     }
/*     */ 
/*     */     public void defaultWriteObject(OutputStreamHook paramOutputStreamHook) throws IOException
/*     */     {
/* 278 */       throw new IOException("Called defaultWriteObject/writeFields twice");
/*     */     }
/*     */ 
/*     */     public void writeData(OutputStreamHook paramOutputStreamHook)
/*     */       throws IOException
/*     */     {
/* 286 */       paramOutputStreamHook.beginOptionalCustomData();
/*     */ 
/* 288 */       paramOutputStreamHook.setState(OutputStreamHook.WROTE_CUSTOM_DATA);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.io.OutputStreamHook
 * JD-Core Version:    0.6.2
 */