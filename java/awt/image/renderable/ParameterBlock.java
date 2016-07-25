/*     */ package java.awt.image.renderable;
/*     */ 
/*     */ import java.awt.image.RenderedImage;
/*     */ import java.io.Serializable;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class ParameterBlock
/*     */   implements Cloneable, Serializable
/*     */ {
/*  97 */   protected Vector<Object> sources = new Vector();
/*     */ 
/* 100 */   protected Vector<Object> parameters = new Vector();
/*     */ 
/*     */   public ParameterBlock()
/*     */   {
/*     */   }
/*     */ 
/*     */   public ParameterBlock(Vector<Object> paramVector)
/*     */   {
/* 111 */     setSources(paramVector);
/*     */   }
/*     */ 
/*     */   public ParameterBlock(Vector<Object> paramVector1, Vector<Object> paramVector2)
/*     */   {
/* 124 */     setSources(paramVector1);
/* 125 */     setParameters(paramVector2);
/*     */   }
/*     */ 
/*     */   public Object shallowClone()
/*     */   {
/*     */     try
/*     */     {
/* 137 */       return super.clone();
/*     */     } catch (Exception localException) {
/*     */     }
/* 140 */     return null;
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     ParameterBlock localParameterBlock;
/*     */     try
/*     */     {
/* 158 */       localParameterBlock = (ParameterBlock)super.clone();
/*     */     }
/*     */     catch (Exception localException) {
/* 161 */       return null;
/*     */     }
/*     */ 
/* 164 */     if (this.sources != null) {
/* 165 */       localParameterBlock.setSources((Vector)this.sources.clone());
/*     */     }
/* 167 */     if (this.parameters != null) {
/* 168 */       localParameterBlock.setParameters((Vector)this.parameters.clone());
/*     */     }
/* 170 */     return localParameterBlock;
/*     */   }
/*     */ 
/*     */   public ParameterBlock addSource(Object paramObject)
/*     */   {
/* 183 */     this.sources.addElement(paramObject);
/* 184 */     return this;
/*     */   }
/*     */ 
/*     */   public Object getSource(int paramInt)
/*     */   {
/* 198 */     return this.sources.elementAt(paramInt);
/*     */   }
/*     */ 
/*     */   public ParameterBlock setSource(Object paramObject, int paramInt)
/*     */   {
/* 215 */     int i = this.sources.size();
/* 216 */     int j = paramInt + 1;
/* 217 */     if (i < j) {
/* 218 */       this.sources.setSize(j);
/*     */     }
/* 220 */     this.sources.setElementAt(paramObject, paramInt);
/* 221 */     return this;
/*     */   }
/*     */ 
/*     */   public RenderedImage getRenderedSource(int paramInt)
/*     */   {
/* 235 */     return (RenderedImage)this.sources.elementAt(paramInt);
/*     */   }
/*     */ 
/*     */   public RenderableImage getRenderableSource(int paramInt)
/*     */   {
/* 249 */     return (RenderableImage)this.sources.elementAt(paramInt);
/*     */   }
/*     */ 
/*     */   public int getNumSources()
/*     */   {
/* 258 */     return this.sources.size();
/*     */   }
/*     */ 
/*     */   public Vector<Object> getSources()
/*     */   {
/* 267 */     return this.sources;
/*     */   }
/*     */ 
/*     */   public void setSources(Vector<Object> paramVector)
/*     */   {
/* 276 */     this.sources = paramVector;
/*     */   }
/*     */ 
/*     */   public void removeSources()
/*     */   {
/* 281 */     this.sources = new Vector();
/*     */   }
/*     */ 
/*     */   public int getNumParameters()
/*     */   {
/* 290 */     return this.parameters.size();
/*     */   }
/*     */ 
/*     */   public Vector<Object> getParameters()
/*     */   {
/* 299 */     return this.parameters;
/*     */   }
/*     */ 
/*     */   public void setParameters(Vector<Object> paramVector)
/*     */   {
/* 309 */     this.parameters = paramVector;
/*     */   }
/*     */ 
/*     */   public void removeParameters()
/*     */   {
/* 314 */     this.parameters = new Vector();
/*     */   }
/*     */ 
/*     */   public ParameterBlock add(Object paramObject)
/*     */   {
/* 325 */     this.parameters.addElement(paramObject);
/* 326 */     return this;
/*     */   }
/*     */ 
/*     */   public ParameterBlock add(byte paramByte)
/*     */   {
/* 337 */     return add(new Byte(paramByte));
/*     */   }
/*     */ 
/*     */   public ParameterBlock add(char paramChar)
/*     */   {
/* 348 */     return add(new Character(paramChar));
/*     */   }
/*     */ 
/*     */   public ParameterBlock add(short paramShort)
/*     */   {
/* 359 */     return add(new Short(paramShort));
/*     */   }
/*     */ 
/*     */   public ParameterBlock add(int paramInt)
/*     */   {
/* 370 */     return add(new Integer(paramInt));
/*     */   }
/*     */ 
/*     */   public ParameterBlock add(long paramLong)
/*     */   {
/* 381 */     return add(new Long(paramLong));
/*     */   }
/*     */ 
/*     */   public ParameterBlock add(float paramFloat)
/*     */   {
/* 392 */     return add(new Float(paramFloat));
/*     */   }
/*     */ 
/*     */   public ParameterBlock add(double paramDouble)
/*     */   {
/* 403 */     return add(new Double(paramDouble));
/*     */   }
/*     */ 
/*     */   public ParameterBlock set(Object paramObject, int paramInt)
/*     */   {
/* 419 */     int i = this.parameters.size();
/* 420 */     int j = paramInt + 1;
/* 421 */     if (i < j) {
/* 422 */       this.parameters.setSize(j);
/*     */     }
/* 424 */     this.parameters.setElementAt(paramObject, paramInt);
/* 425 */     return this;
/*     */   }
/*     */ 
/*     */   public ParameterBlock set(byte paramByte, int paramInt)
/*     */   {
/* 441 */     return set(new Byte(paramByte), paramInt);
/*     */   }
/*     */ 
/*     */   public ParameterBlock set(char paramChar, int paramInt)
/*     */   {
/* 457 */     return set(new Character(paramChar), paramInt);
/*     */   }
/*     */ 
/*     */   public ParameterBlock set(short paramShort, int paramInt)
/*     */   {
/* 473 */     return set(new Short(paramShort), paramInt);
/*     */   }
/*     */ 
/*     */   public ParameterBlock set(int paramInt1, int paramInt2)
/*     */   {
/* 489 */     return set(new Integer(paramInt1), paramInt2);
/*     */   }
/*     */ 
/*     */   public ParameterBlock set(long paramLong, int paramInt)
/*     */   {
/* 505 */     return set(new Long(paramLong), paramInt);
/*     */   }
/*     */ 
/*     */   public ParameterBlock set(float paramFloat, int paramInt)
/*     */   {
/* 521 */     return set(new Float(paramFloat), paramInt);
/*     */   }
/*     */ 
/*     */   public ParameterBlock set(double paramDouble, int paramInt)
/*     */   {
/* 537 */     return set(new Double(paramDouble), paramInt);
/*     */   }
/*     */ 
/*     */   public Object getObjectParameter(int paramInt)
/*     */   {
/* 549 */     return this.parameters.elementAt(paramInt);
/*     */   }
/*     */ 
/*     */   public byte getByteParameter(int paramInt)
/*     */   {
/* 569 */     return ((Byte)this.parameters.elementAt(paramInt)).byteValue();
/*     */   }
/*     */ 
/*     */   public char getCharParameter(int paramInt)
/*     */   {
/* 589 */     return ((Character)this.parameters.elementAt(paramInt)).charValue();
/*     */   }
/*     */ 
/*     */   public short getShortParameter(int paramInt)
/*     */   {
/* 609 */     return ((Short)this.parameters.elementAt(paramInt)).shortValue();
/*     */   }
/*     */ 
/*     */   public int getIntParameter(int paramInt)
/*     */   {
/* 629 */     return ((Integer)this.parameters.elementAt(paramInt)).intValue();
/*     */   }
/*     */ 
/*     */   public long getLongParameter(int paramInt)
/*     */   {
/* 649 */     return ((Long)this.parameters.elementAt(paramInt)).longValue();
/*     */   }
/*     */ 
/*     */   public float getFloatParameter(int paramInt)
/*     */   {
/* 669 */     return ((Float)this.parameters.elementAt(paramInt)).floatValue();
/*     */   }
/*     */ 
/*     */   public double getDoubleParameter(int paramInt)
/*     */   {
/* 689 */     return ((Double)this.parameters.elementAt(paramInt)).doubleValue();
/*     */   }
/*     */ 
/*     */   public Class[] getParamClasses()
/*     */   {
/* 698 */     int i = getNumParameters();
/* 699 */     Class[] arrayOfClass = new Class[i];
/*     */ 
/* 702 */     for (int j = 0; j < i; j++) {
/* 703 */       Object localObject = getObjectParameter(j);
/* 704 */       if ((localObject instanceof Byte))
/* 705 */         arrayOfClass[j] = Byte.TYPE;
/* 706 */       else if ((localObject instanceof Character))
/* 707 */         arrayOfClass[j] = Character.TYPE;
/* 708 */       else if ((localObject instanceof Short))
/* 709 */         arrayOfClass[j] = Short.TYPE;
/* 710 */       else if ((localObject instanceof Integer))
/* 711 */         arrayOfClass[j] = Integer.TYPE;
/* 712 */       else if ((localObject instanceof Long))
/* 713 */         arrayOfClass[j] = Long.TYPE;
/* 714 */       else if ((localObject instanceof Float))
/* 715 */         arrayOfClass[j] = Float.TYPE;
/* 716 */       else if ((localObject instanceof Double))
/* 717 */         arrayOfClass[j] = Double.TYPE;
/*     */       else {
/* 719 */         arrayOfClass[j] = localObject.getClass();
/*     */       }
/*     */     }
/*     */ 
/* 723 */     return arrayOfClass;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.image.renderable.ParameterBlock
 * JD-Core Version:    0.6.2
 */