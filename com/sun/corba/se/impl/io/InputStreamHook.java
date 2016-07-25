/*     */ package com.sun.corba.se.impl.io;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.OMGSystemException;
/*     */ import com.sun.corba.se.impl.logging.UtilSystemException;
/*     */ import com.sun.corba.se.spi.orb.ORBVersion;
/*     */ import com.sun.corba.se.spi.orb.ORBVersionFactory;
/*     */ import java.io.IOException;
/*     */ import java.io.InvalidClassException;
/*     */ import java.io.NotActiveException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectInputStream.GetField;
/*     */ import java.io.ObjectStreamClass;
/*     */ import java.io.StreamCorruptedException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.omg.CORBA.portable.ValueInputStream;
/*     */ import org.omg.CORBA_2_3.portable.InputStream;
/*     */ 
/*     */ public abstract class InputStreamHook extends ObjectInputStream
/*     */ {
/*  55 */   static final OMGSystemException omgWrapper = OMGSystemException.get("rpc.encoding");
/*     */ 
/*  58 */   static final UtilSystemException utilWrapper = UtilSystemException.get("rpc.encoding");
/*     */ 
/* 268 */   protected ReadObjectState readObjectState = DEFAULT_STATE;
/*     */ 
/* 270 */   protected static final ReadObjectState DEFAULT_STATE = new DefaultState();
/* 271 */   protected static final ReadObjectState IN_READ_OBJECT_OPT_DATA = new InReadObjectOptionalDataState();
/*     */ 
/* 273 */   protected static final ReadObjectState IN_READ_OBJECT_NO_MORE_OPT_DATA = new InReadObjectNoMoreOptionalDataState();
/*     */ 
/* 275 */   protected static final ReadObjectState IN_READ_OBJECT_DEFAULTS_SENT = new InReadObjectDefaultsSentState();
/*     */ 
/* 277 */   protected static final ReadObjectState NO_READ_OBJECT_DEFAULTS_SENT = new NoReadObjectDefaultsSentState();
/*     */ 
/* 280 */   protected static final ReadObjectState IN_READ_OBJECT_REMOTE_NOT_CUSTOM_MARSHALED = new InReadObjectRemoteDidNotUseWriteObjectState();
/*     */ 
/* 282 */   protected static final ReadObjectState IN_READ_OBJECT_PAST_DEFAULTS_REMOTE_NOT_CUSTOM = new InReadObjectPastDefaultsRemoteDidNotUseWOState();
/*     */ 
/*     */   public InputStreamHook()
/*     */     throws IOException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void defaultReadObject()
/*     */     throws IOException, ClassNotFoundException, NotActiveException
/*     */   {
/* 197 */     this.readObjectState.beginDefaultReadObject(this);
/*     */ 
/* 199 */     defaultReadObjectDelegate();
/*     */ 
/* 201 */     this.readObjectState.endDefaultReadObject(this);
/*     */   }
/*     */ 
/*     */   abstract void defaultReadObjectDelegate();
/*     */ 
/*     */   abstract void readFields(Map paramMap)
/*     */     throws InvalidClassException, StreamCorruptedException, ClassNotFoundException, IOException;
/*     */ 
/*     */   public ObjectInputStream.GetField readFields()
/*     */     throws IOException, ClassNotFoundException, NotActiveException
/*     */   {
/* 217 */     HashMap localHashMap = new HashMap();
/*     */ 
/* 232 */     readFields(localHashMap);
/*     */ 
/* 234 */     this.readObjectState.endDefaultReadObject(this);
/*     */ 
/* 236 */     return new HookGetFields(localHashMap);
/*     */   }
/*     */ 
/*     */   protected void setState(ReadObjectState paramReadObjectState)
/*     */   {
/* 250 */     this.readObjectState = paramReadObjectState;
/*     */   }
/*     */ 
/*     */   protected abstract byte getStreamFormatVersion();
/*     */ 
/*     */   abstract InputStream getOrbStream();
/*     */ 
/*     */   protected void throwOptionalDataIncompatibleException()
/*     */   {
/* 364 */     throw omgWrapper.rmiiiopOptionalDataIncompatible2();
/*     */   }
/*     */ 
/*     */   protected static class DefaultState extends InputStreamHook.ReadObjectState
/*     */   {
/*     */     public void beginUnmarshalCustomValue(InputStreamHook paramInputStreamHook, boolean paramBoolean1, boolean paramBoolean2)
/*     */       throws IOException
/*     */     {
/* 292 */       if (paramBoolean2) {
/* 293 */         if (paramBoolean1) {
/* 294 */           paramInputStreamHook.setState(InputStreamHook.IN_READ_OBJECT_DEFAULTS_SENT);
/*     */         } else {
/*     */           try {
/* 297 */             if (paramInputStreamHook.getStreamFormatVersion() == 2) {
/* 298 */               ((ValueInputStream)paramInputStreamHook.getOrbStream()).start_value();
/*     */             }
/*     */ 
/*     */           }
/*     */           catch (Exception localException)
/*     */           {
/*     */           }
/*     */ 
/* 308 */           paramInputStreamHook.setState(InputStreamHook.IN_READ_OBJECT_OPT_DATA);
/*     */         }
/*     */       }
/* 311 */       else if (paramBoolean1) {
/* 312 */         paramInputStreamHook.setState(InputStreamHook.NO_READ_OBJECT_DEFAULTS_SENT);
/*     */       }
/*     */       else
/* 315 */         throw new StreamCorruptedException("No default data sent");
/*     */     }
/*     */   }
/*     */ 
/*     */   private class HookGetFields extends ObjectInputStream.GetField
/*     */   {
/*  62 */     private Map fields = null;
/*     */ 
/*     */     HookGetFields(Map arg2)
/*     */     {
/*     */       Object localObject;
/*  65 */       this.fields = localObject;
/*     */     }
/*     */ 
/*     */     public ObjectStreamClass getObjectStreamClass()
/*     */     {
/*  74 */       return null;
/*     */     }
/*     */ 
/*     */     public boolean defaulted(String paramString)
/*     */       throws IOException, IllegalArgumentException
/*     */     {
/*  83 */       return !this.fields.containsKey(paramString);
/*     */     }
/*     */ 
/*     */     public boolean get(String paramString, boolean paramBoolean)
/*     */       throws IOException, IllegalArgumentException
/*     */     {
/*  91 */       if (defaulted(paramString))
/*  92 */         return paramBoolean;
/*  93 */       return ((Boolean)this.fields.get(paramString)).booleanValue();
/*     */     }
/*     */ 
/*     */     public char get(String paramString, char paramChar)
/*     */       throws IOException, IllegalArgumentException
/*     */     {
/* 101 */       if (defaulted(paramString))
/* 102 */         return paramChar;
/* 103 */       return ((Character)this.fields.get(paramString)).charValue();
/*     */     }
/*     */ 
/*     */     public byte get(String paramString, byte paramByte)
/*     */       throws IOException, IllegalArgumentException
/*     */     {
/* 112 */       if (defaulted(paramString))
/* 113 */         return paramByte;
/* 114 */       return ((Byte)this.fields.get(paramString)).byteValue();
/*     */     }
/*     */ 
/*     */     public short get(String paramString, short paramShort)
/*     */       throws IOException, IllegalArgumentException
/*     */     {
/* 123 */       if (defaulted(paramString))
/* 124 */         return paramShort;
/* 125 */       return ((Short)this.fields.get(paramString)).shortValue();
/*     */     }
/*     */ 
/*     */     public int get(String paramString, int paramInt)
/*     */       throws IOException, IllegalArgumentException
/*     */     {
/* 134 */       if (defaulted(paramString))
/* 135 */         return paramInt;
/* 136 */       return ((Integer)this.fields.get(paramString)).intValue();
/*     */     }
/*     */ 
/*     */     public long get(String paramString, long paramLong)
/*     */       throws IOException, IllegalArgumentException
/*     */     {
/* 145 */       if (defaulted(paramString))
/* 146 */         return paramLong;
/* 147 */       return ((Long)this.fields.get(paramString)).longValue();
/*     */     }
/*     */ 
/*     */     public float get(String paramString, float paramFloat)
/*     */       throws IOException, IllegalArgumentException
/*     */     {
/* 156 */       if (defaulted(paramString))
/* 157 */         return paramFloat;
/* 158 */       return ((Float)this.fields.get(paramString)).floatValue();
/*     */     }
/*     */ 
/*     */     public double get(String paramString, double paramDouble)
/*     */       throws IOException, IllegalArgumentException
/*     */     {
/* 167 */       if (defaulted(paramString))
/* 168 */         return paramDouble;
/* 169 */       return ((Double)this.fields.get(paramString)).doubleValue();
/*     */     }
/*     */ 
/*     */     public Object get(String paramString, Object paramObject)
/*     */       throws IOException, IllegalArgumentException
/*     */     {
/* 178 */       if (defaulted(paramString))
/* 179 */         return paramObject;
/* 180 */       return this.fields.get(paramString);
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 185 */       return this.fields.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static class InReadObjectDefaultsSentState extends InputStreamHook.ReadObjectState
/*     */   {
/*     */     public void beginUnmarshalCustomValue(InputStreamHook paramInputStreamHook, boolean paramBoolean1, boolean paramBoolean2)
/*     */     {
/* 374 */       throw InputStreamHook.utilWrapper.badBeginUnmarshalCustomValue();
/*     */     }
/*     */ 
/*     */     public void endUnmarshalCustomValue(InputStreamHook paramInputStreamHook)
/*     */     {
/* 383 */       if (paramInputStreamHook.getStreamFormatVersion() == 2) {
/* 384 */         ((ValueInputStream)paramInputStreamHook.getOrbStream()).start_value();
/* 385 */         ((ValueInputStream)paramInputStreamHook.getOrbStream()).end_value();
/*     */       }
/*     */ 
/* 388 */       paramInputStreamHook.setState(InputStreamHook.DEFAULT_STATE);
/*     */     }
/*     */ 
/*     */     public void endDefaultReadObject(InputStreamHook paramInputStreamHook)
/*     */       throws IOException
/*     */     {
/* 394 */       if (paramInputStreamHook.getStreamFormatVersion() == 2) {
/* 395 */         ((ValueInputStream)paramInputStreamHook.getOrbStream()).start_value();
/*     */       }
/* 397 */       paramInputStreamHook.setState(InputStreamHook.IN_READ_OBJECT_OPT_DATA);
/*     */     }
/*     */ 
/*     */     public void readData(InputStreamHook paramInputStreamHook) throws IOException {
/* 401 */       org.omg.CORBA.ORB localORB = paramInputStreamHook.getOrbStream().orb();
/* 402 */       if ((localORB == null) || (!(localORB instanceof com.sun.corba.se.spi.orb.ORB)))
/*     */       {
/* 404 */         throw new StreamCorruptedException("Default data must be read first");
/*     */       }
/*     */ 
/* 407 */       ORBVersion localORBVersion = ((com.sun.corba.se.spi.orb.ORB)localORB).getORBVersion();
/*     */ 
/* 414 */       if ((ORBVersionFactory.getPEORB().compareTo(localORBVersion) <= 0) || (localORBVersion.equals(ORBVersionFactory.getFOREIGN())))
/*     */       {
/* 417 */         throw new StreamCorruptedException("Default data must be read first");
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static class InReadObjectNoMoreOptionalDataState extends InputStreamHook.InReadObjectOptionalDataState
/*     */   {
/*     */     public void readData(InputStreamHook paramInputStreamHook)
/*     */       throws IOException
/*     */     {
/* 453 */       paramInputStreamHook.throwOptionalDataIncompatibleException();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static class InReadObjectOptionalDataState extends InputStreamHook.ReadObjectState
/*     */   {
/*     */     public void beginUnmarshalCustomValue(InputStreamHook paramInputStreamHook, boolean paramBoolean1, boolean paramBoolean2)
/*     */     {
/* 429 */       throw InputStreamHook.utilWrapper.badBeginUnmarshalCustomValue();
/*     */     }
/*     */ 
/*     */     public void endUnmarshalCustomValue(InputStreamHook paramInputStreamHook) throws IOException
/*     */     {
/* 434 */       if (paramInputStreamHook.getStreamFormatVersion() == 2) {
/* 435 */         ((ValueInputStream)paramInputStreamHook.getOrbStream()).end_value();
/*     */       }
/* 437 */       paramInputStreamHook.setState(InputStreamHook.DEFAULT_STATE);
/*     */     }
/*     */ 
/*     */     public void beginDefaultReadObject(InputStreamHook paramInputStreamHook)
/*     */       throws IOException
/*     */     {
/* 443 */       throw new StreamCorruptedException("Default data not sent or already read/passed");
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static class InReadObjectPastDefaultsRemoteDidNotUseWOState extends InputStreamHook.ReadObjectState
/*     */   {
/*     */     public void beginUnmarshalCustomValue(InputStreamHook paramInputStreamHook, boolean paramBoolean1, boolean paramBoolean2)
/*     */     {
/* 347 */       throw InputStreamHook.utilWrapper.badBeginUnmarshalCustomValue();
/*     */     }
/*     */ 
/*     */     public void beginDefaultReadObject(InputStreamHook paramInputStreamHook)
/*     */       throws IOException
/*     */     {
/* 353 */       throw new StreamCorruptedException("Default data already read");
/*     */     }
/*     */ 
/*     */     public void readData(InputStreamHook paramInputStreamHook)
/*     */     {
/* 358 */       paramInputStreamHook.throwOptionalDataIncompatibleException();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static class InReadObjectRemoteDidNotUseWriteObjectState extends InputStreamHook.ReadObjectState
/*     */   {
/*     */     public void beginUnmarshalCustomValue(InputStreamHook paramInputStreamHook, boolean paramBoolean1, boolean paramBoolean2)
/*     */     {
/* 329 */       throw InputStreamHook.utilWrapper.badBeginUnmarshalCustomValue();
/*     */     }
/*     */ 
/*     */     public void endDefaultReadObject(InputStreamHook paramInputStreamHook) {
/* 333 */       paramInputStreamHook.setState(InputStreamHook.IN_READ_OBJECT_PAST_DEFAULTS_REMOTE_NOT_CUSTOM);
/*     */     }
/*     */ 
/*     */     public void readData(InputStreamHook paramInputStreamHook) {
/* 337 */       paramInputStreamHook.throwOptionalDataIncompatibleException();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static class NoReadObjectDefaultsSentState extends InputStreamHook.ReadObjectState
/*     */   {
/*     */     public void endUnmarshalCustomValue(InputStreamHook paramInputStreamHook)
/*     */       throws IOException
/*     */     {
/* 461 */       if (paramInputStreamHook.getStreamFormatVersion() == 2) {
/* 462 */         ((ValueInputStream)paramInputStreamHook.getOrbStream()).start_value();
/* 463 */         ((ValueInputStream)paramInputStreamHook.getOrbStream()).end_value();
/*     */       }
/*     */ 
/* 466 */       paramInputStreamHook.setState(InputStreamHook.DEFAULT_STATE);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static class ReadObjectState
/*     */   {
/*     */     public void beginUnmarshalCustomValue(InputStreamHook paramInputStreamHook, boolean paramBoolean1, boolean paramBoolean2)
/*     */       throws IOException
/*     */     {
/*     */     }
/*     */ 
/*     */     public void endUnmarshalCustomValue(InputStreamHook paramInputStreamHook)
/*     */       throws IOException
/*     */     {
/*     */     }
/*     */ 
/*     */     public void beginDefaultReadObject(InputStreamHook paramInputStreamHook)
/*     */       throws IOException
/*     */     {
/*     */     }
/*     */ 
/*     */     public void endDefaultReadObject(InputStreamHook paramInputStreamHook)
/*     */       throws IOException
/*     */     {
/*     */     }
/*     */ 
/*     */     public void readData(InputStreamHook paramInputStreamHook)
/*     */       throws IOException
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.io.InputStreamHook
 * JD-Core Version:    0.6.2
 */