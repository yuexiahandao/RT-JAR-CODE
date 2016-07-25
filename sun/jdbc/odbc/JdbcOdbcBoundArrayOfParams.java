/*     */ package sun.jdbc.odbc;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class JdbcOdbcBoundArrayOfParams extends JdbcOdbcObject
/*     */ {
/*     */   protected int numParams;
/*     */   protected Hashtable hashedLenIdx;
/*     */   protected Object[] storedParams;
/*     */   protected int[] paramLenIdx;
/*     */   protected Object[][] storedInputStreams;
/*     */   protected Object[][] paramSets;
/*     */   protected int[][] paramLenIdxSets;
/*     */   protected int batchSize;
/*     */ 
/*     */   public JdbcOdbcBoundArrayOfParams(int paramInt)
/*     */   {
/*  42 */     this.numParams = paramInt;
/*     */ 
/*  44 */     initialize();
/*     */   }
/*     */ 
/*     */   public void initialize()
/*     */   {
/*  59 */     this.storedParams = new Object[this.numParams];
/*  60 */     this.paramLenIdx = new int[this.numParams];
/*  61 */     this.hashedLenIdx = new Hashtable();
/*     */ 
/*  64 */     this.batchSize = 0;
/*     */ 
/*  69 */     for (int i = 0; i < this.numParams; i++)
/*     */     {
/*  71 */       this.paramLenIdx[i] = -5;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void storeValue(int paramInt1, Object paramObject, int paramInt2)
/*     */   {
/*  84 */     this.storedParams[paramInt1] = paramObject;
/*  85 */     this.paramLenIdx[paramInt1] = paramInt2;
/*     */   }
/*     */ 
/*     */   public void clearParameterSet()
/*     */   {
/*  96 */     if (this.storedParams != null)
/*     */     {
/*  98 */       for (int i = 0; i < this.numParams; i++)
/*     */       {
/* 100 */         this.storedParams[i] = new Object();
/* 101 */         this.paramLenIdx[i] = -5;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object[] getStoredParameterSet()
/*     */   {
/* 113 */     Object[] arrayOfObject = new Object[0];
/*     */ 
/* 115 */     if (this.storedParams != null)
/*     */     {
/* 117 */       arrayOfObject = new Object[this.numParams];
/*     */       try
/*     */       {
/* 121 */         for (int i = 0; i < this.numParams; i++)
/*     */         {
/* 123 */           arrayOfObject[i] = this.storedParams[i];
/*     */         }
/*     */       }
/*     */       catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
/*     */       {
/* 128 */         System.out.println("exception: " + localArrayIndexOutOfBoundsException.getMessage());
/* 129 */         localArrayIndexOutOfBoundsException.printStackTrace();
/*     */       }
/*     */     }
/* 132 */     return arrayOfObject;
/*     */   }
/*     */ 
/*     */   public void storeRowIndex(int paramInt, int[] paramArrayOfInt)
/*     */   {
/* 142 */     this.hashedLenIdx.put(new Integer(paramInt), paramArrayOfInt);
/*     */   }
/*     */ 
/*     */   public int[] getStoredRowIndex(int paramInt)
/*     */   {
/* 151 */     return (int[])this.hashedLenIdx.get(new Integer(paramInt));
/*     */   }
/*     */ 
/*     */   public void clearStoredRowIndexs()
/*     */   {
/* 160 */     if (!this.hashedLenIdx.isEmpty())
/*     */     {
/* 162 */       this.hashedLenIdx.clear();
/*     */     }
/*     */   }
/*     */ 
/*     */   public int[] getStoredIndexSet()
/*     */   {
/* 174 */     int[] arrayOfInt = new int[0];
/*     */ 
/* 176 */     if (this.paramLenIdx != null)
/*     */     {
/* 178 */       arrayOfInt = new int[this.numParams];
/*     */       try
/*     */       {
/* 182 */         for (int i = 0; i < this.numParams; i++)
/*     */         {
/* 184 */           arrayOfInt[i] = this.paramLenIdx[i];
/*     */ 
/* 186 */           if (arrayOfInt[i] == -5)
/*     */           {
/* 188 */             return new int[0];
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */       catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
/*     */       {
/* 195 */         System.out.println("exception: " + localArrayIndexOutOfBoundsException.getMessage());
/* 196 */         localArrayIndexOutOfBoundsException.printStackTrace();
/*     */       }
/*     */     }
/* 199 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   public void builtColumWiseParameteSets(int paramInt, Vector paramVector)
/*     */   {
/* 210 */     int[] arrayOfInt = new int[0];
/* 211 */     Object[] arrayOfObject = new Object[0];
/*     */ 
/* 213 */     this.batchSize = paramInt;
/*     */ 
/* 215 */     if (paramVector.size() == this.batchSize)
/*     */     {
/* 217 */       this.storedInputStreams = new Object[this.batchSize][this.numParams];
/* 218 */       this.paramSets = new Object[this.batchSize][this.numParams];
/* 219 */       this.paramLenIdxSets = new int[this.batchSize][this.numParams];
/*     */ 
/* 222 */       for (int i = 0; i < this.batchSize; i++)
/*     */       {
/* 224 */         arrayOfInt = getStoredRowIndex(i);
/*     */ 
/* 226 */         arrayOfObject = (Object[])paramVector.elementAt(i);
/*     */ 
/* 228 */         int j = 0;
/*     */ 
/* 230 */         while (j < this.numParams)
/*     */         {
/* 232 */           this.paramSets[i][j] = arrayOfObject[j];
/* 233 */           this.paramLenIdxSets[i][j] = arrayOfInt[j];
/* 234 */           j++;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object[] getColumnWiseParamSet(int paramInt)
/*     */   {
/* 248 */     Object[] arrayOfObject = new Object[this.batchSize];
/*     */ 
/* 250 */     if (this.paramSets != null)
/*     */     {
/* 252 */       for (int i = 0; i < this.batchSize; i++)
/*     */       {
/* 254 */         arrayOfObject[i] = this.paramSets[i][(paramInt - 1)];
/*     */       }
/*     */     }
/*     */ 
/* 258 */     return arrayOfObject;
/*     */   }
/*     */ 
/*     */   public int[] getColumnWiseIndexArray(int paramInt)
/*     */   {
/* 270 */     int[] arrayOfInt = new int[this.batchSize];
/*     */ 
/* 272 */     if (this.paramLenIdxSets != null)
/*     */     {
/* 274 */       for (int i = 0; i < this.batchSize; i++)
/*     */       {
/* 276 */         arrayOfInt[i] = this.paramLenIdxSets[i][(paramInt - 1)];
/*     */       }
/*     */     }
/*     */ 
/* 280 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   public void setInputStreamElements(int paramInt, Object[] paramArrayOfObject)
/*     */   {
/* 291 */     if ((paramInt >= 1) && (paramInt <= this.numParams))
/*     */     {
/* 293 */       if ((this.storedInputStreams != null) && (paramArrayOfObject != null))
/*     */       {
/* 295 */         int i = 0;
/*     */ 
/* 297 */         while (i < this.batchSize)
/*     */         {
/* 299 */           this.storedInputStreams[i][(paramInt - 1)] = paramArrayOfObject[i];
/* 300 */           i++;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public InputStream getInputStreamElement(int paramInt1, int paramInt2)
/*     */   {
/* 316 */     InputStream localInputStream = null;
/*     */ 
/* 318 */     if ((paramInt1 >= 1) && (paramInt1 <= this.numParams))
/*     */     {
/* 320 */       if ((paramInt2 >= 1) && (paramInt2 <= this.batchSize)) {
/* 321 */         localInputStream = (InputStream)this.storedInputStreams[(paramInt2 - 1)][(paramInt1 - 1)];
/*     */       }
/*     */     }
/* 324 */     return localInputStream;
/*     */   }
/*     */ 
/*     */   public int getElementLength(int paramInt1, int paramInt2)
/*     */   {
/* 336 */     return this.paramLenIdxSets[(paramInt2 - 1)][(paramInt1 - 1)];
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.jdbc.odbc.JdbcOdbcBoundArrayOfParams
 * JD-Core Version:    0.6.2
 */