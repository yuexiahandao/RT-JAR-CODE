/*     */ package com.sun.corba.se.impl.oa.poa;
/*     */ 
/*     */ import com.sun.corba.se.spi.extension.CopyObjectPolicy;
/*     */ import com.sun.corba.se.spi.extension.ServantCachingPolicy;
/*     */ import com.sun.corba.se.spi.extension.ZeroPortPolicy;
/*     */ import java.util.BitSet;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import org.omg.CORBA.Policy;
/*     */ import org.omg.PortableServer.IdAssignmentPolicy;
/*     */ import org.omg.PortableServer.IdAssignmentPolicyValue;
/*     */ import org.omg.PortableServer.IdUniquenessPolicy;
/*     */ import org.omg.PortableServer.IdUniquenessPolicyValue;
/*     */ import org.omg.PortableServer.ImplicitActivationPolicy;
/*     */ import org.omg.PortableServer.ImplicitActivationPolicyValue;
/*     */ import org.omg.PortableServer.LifespanPolicy;
/*     */ import org.omg.PortableServer.LifespanPolicyValue;
/*     */ import org.omg.PortableServer.POAPackage.InvalidPolicy;
/*     */ import org.omg.PortableServer.RequestProcessingPolicy;
/*     */ import org.omg.PortableServer.RequestProcessingPolicyValue;
/*     */ import org.omg.PortableServer.ServantRetentionPolicy;
/*     */ import org.omg.PortableServer.ServantRetentionPolicyValue;
/*     */ import org.omg.PortableServer.ThreadPolicy;
/*     */ import org.omg.PortableServer.ThreadPolicyValue;
/*     */ 
/*     */ public final class Policies
/*     */ {
/*     */   private static final int MIN_POA_POLICY_ID = 16;
/*     */   private static final int MAX_POA_POLICY_ID = 22;
/*     */   private static final int POLICY_TABLE_SIZE = 7;
/*     */   int defaultObjectCopierFactoryId;
/*  59 */   private HashMap policyMap = new HashMap();
/*     */ 
/*  61 */   public static final Policies defaultPolicies = new Policies();
/*     */ 
/*  64 */   public static final Policies rootPOAPolicies = new Policies(0, 0, 0, 1, 0, 0, 0);
/*     */   private int[] poaPolicyValues;
/*     */ 
/*     */   private int getPolicyValue(int paramInt)
/*     */   {
/*  78 */     return this.poaPolicyValues[(paramInt - 16)];
/*     */   }
/*     */ 
/*     */   private void setPolicyValue(int paramInt1, int paramInt2)
/*     */   {
/*  83 */     this.poaPolicyValues[(paramInt1 - 16)] = paramInt2;
/*     */   }
/*     */ 
/*     */   private Policies(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7)
/*     */   {
/*  95 */     this.poaPolicyValues = new int[] { paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7 };
/*     */   }
/*     */ 
/*     */   private Policies()
/*     */   {
/* 106 */     this(0, 0, 0, 1, 1, 0, 0);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 116 */     StringBuffer localStringBuffer = new StringBuffer();
/* 117 */     localStringBuffer.append("Policies[");
/* 118 */     int i = 1;
/* 119 */     Iterator localIterator = this.policyMap.values().iterator();
/* 120 */     while (localIterator.hasNext()) {
/* 121 */       if (i != 0)
/* 122 */         i = 0;
/*     */       else {
/* 124 */         localStringBuffer.append(",");
/*     */       }
/* 126 */       localStringBuffer.append(localIterator.next().toString());
/*     */     }
/* 128 */     localStringBuffer.append("]");
/* 129 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   private int getPOAPolicyValue(Policy paramPolicy)
/*     */   {
/* 137 */     if ((paramPolicy instanceof ThreadPolicy))
/* 138 */       return ((ThreadPolicy)paramPolicy).value().value();
/* 139 */     if ((paramPolicy instanceof LifespanPolicy))
/* 140 */       return ((LifespanPolicy)paramPolicy).value().value();
/* 141 */     if ((paramPolicy instanceof IdUniquenessPolicy))
/* 142 */       return ((IdUniquenessPolicy)paramPolicy).value().value();
/* 143 */     if ((paramPolicy instanceof IdAssignmentPolicy))
/* 144 */       return ((IdAssignmentPolicy)paramPolicy).value().value();
/* 145 */     if ((paramPolicy instanceof ServantRetentionPolicy))
/* 146 */       return ((ServantRetentionPolicy)paramPolicy).value().value();
/* 147 */     if ((paramPolicy instanceof RequestProcessingPolicy))
/* 148 */       return ((RequestProcessingPolicy)paramPolicy).value().value();
/* 149 */     if ((paramPolicy instanceof ImplicitActivationPolicy)) {
/* 150 */       return ((ImplicitActivationPolicy)paramPolicy).value().value();
/*     */     }
/* 152 */     return -1;
/*     */   }
/*     */ 
/*     */   private void checkForPolicyError(BitSet paramBitSet)
/*     */     throws InvalidPolicy
/*     */   {
/*     */     int j;
/* 160 */     for (int i = 0; i < paramBitSet.length(); j = (short)(i + 1))
/* 161 */       if (paramBitSet.get(i))
/* 162 */         throw new InvalidPolicy(i);
/*     */   }
/*     */ 
/*     */   private void addToErrorSet(Policy[] paramArrayOfPolicy, int paramInt, BitSet paramBitSet)
/*     */   {
/* 171 */     for (int i = 0; i < paramArrayOfPolicy.length; i++)
/* 172 */       if (paramArrayOfPolicy[i].policy_type() == paramInt) {
/* 173 */         paramBitSet.set(i);
/* 174 */         return;
/*     */       }
/*     */   }
/*     */ 
/*     */   Policies(Policy[] paramArrayOfPolicy, int paramInt)
/*     */     throws InvalidPolicy
/*     */   {
/* 184 */     this();
/*     */ 
/* 186 */     this.defaultObjectCopierFactoryId = paramInt;
/*     */ 
/* 188 */     if (paramArrayOfPolicy == null) {
/* 189 */       return;
/*     */     }
/*     */ 
/* 193 */     BitSet localBitSet = new BitSet(paramArrayOfPolicy.length);
/*     */ 
/* 195 */     for (int i = 0; i < paramArrayOfPolicy.length; i = (short)(i + 1)) {
/* 196 */       Policy localPolicy1 = paramArrayOfPolicy[i];
/* 197 */       int j = getPOAPolicyValue(localPolicy1);
/*     */ 
/* 202 */       Integer localInteger = new Integer(localPolicy1.policy_type());
/* 203 */       Policy localPolicy2 = (Policy)this.policyMap.get(localInteger);
/* 204 */       if (localPolicy2 == null) {
/* 205 */         this.policyMap.put(localInteger, localPolicy1);
/*     */       }
/* 207 */       if (j >= 0) {
/* 208 */         setPolicyValue(localInteger.intValue(), j);
/*     */ 
/* 213 */         if ((localPolicy2 != null) && (getPOAPolicyValue(localPolicy2) != j))
/*     */         {
/* 215 */           localBitSet.set(i);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 222 */     if ((!retainServants()) && (useActiveMapOnly())) {
/* 223 */       addToErrorSet(paramArrayOfPolicy, 21, localBitSet);
/*     */ 
/* 225 */       addToErrorSet(paramArrayOfPolicy, 22, localBitSet);
/*     */     }
/*     */ 
/* 230 */     if (isImplicitlyActivated()) {
/* 231 */       if (!retainServants()) {
/* 232 */         addToErrorSet(paramArrayOfPolicy, 20, localBitSet);
/*     */ 
/* 234 */         addToErrorSet(paramArrayOfPolicy, 21, localBitSet);
/*     */       }
/*     */ 
/* 238 */       if (!isSystemAssignedIds()) {
/* 239 */         addToErrorSet(paramArrayOfPolicy, 20, localBitSet);
/*     */ 
/* 241 */         addToErrorSet(paramArrayOfPolicy, 19, localBitSet);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 246 */     checkForPolicyError(localBitSet);
/*     */   }
/*     */ 
/*     */   public Policy get_effective_policy(int paramInt)
/*     */   {
/* 251 */     Integer localInteger = new Integer(paramInt);
/* 252 */     Policy localPolicy = (Policy)this.policyMap.get(localInteger);
/* 253 */     return localPolicy;
/*     */   }
/*     */ 
/*     */   public final boolean isOrbControlledThreads()
/*     */   {
/* 258 */     return getPolicyValue(16) == 0;
/*     */   }
/*     */ 
/*     */   public final boolean isSingleThreaded() {
/* 262 */     return getPolicyValue(16) == 1;
/*     */   }
/*     */ 
/*     */   public final boolean isTransient()
/*     */   {
/* 268 */     return getPolicyValue(17) == 0;
/*     */   }
/*     */ 
/*     */   public final boolean isPersistent() {
/* 272 */     return getPolicyValue(17) == 1;
/*     */   }
/*     */ 
/*     */   public final boolean isUniqueIds()
/*     */   {
/* 278 */     return getPolicyValue(18) == 0;
/*     */   }
/*     */ 
/*     */   public final boolean isMultipleIds() {
/* 282 */     return getPolicyValue(18) == 1;
/*     */   }
/*     */ 
/*     */   public final boolean isUserAssignedIds()
/*     */   {
/* 288 */     return getPolicyValue(19) == 0;
/*     */   }
/*     */ 
/*     */   public final boolean isSystemAssignedIds() {
/* 292 */     return getPolicyValue(19) == 1;
/*     */   }
/*     */ 
/*     */   public final boolean retainServants()
/*     */   {
/* 298 */     return getPolicyValue(21) == 0;
/*     */   }
/*     */ 
/*     */   public final boolean useActiveMapOnly()
/*     */   {
/* 304 */     return getPolicyValue(22) == 0;
/*     */   }
/*     */ 
/*     */   public final boolean useDefaultServant() {
/* 308 */     return getPolicyValue(22) == 1;
/*     */   }
/*     */ 
/*     */   public final boolean useServantManager() {
/* 312 */     return getPolicyValue(22) == 2;
/*     */   }
/*     */ 
/*     */   public final boolean isImplicitlyActivated()
/*     */   {
/* 318 */     return getPolicyValue(20) == 0;
/*     */   }
/*     */ 
/*     */   public final int servantCachingLevel()
/*     */   {
/* 325 */     Integer localInteger = new Integer(1398079488);
/* 326 */     ServantCachingPolicy localServantCachingPolicy = (ServantCachingPolicy)this.policyMap.get(localInteger);
/* 327 */     if (localServantCachingPolicy == null) {
/* 328 */       return 0;
/*     */     }
/* 330 */     return localServantCachingPolicy.getType();
/*     */   }
/*     */ 
/*     */   public final boolean forceZeroPort()
/*     */   {
/* 335 */     Integer localInteger = new Integer(1398079489);
/* 336 */     ZeroPortPolicy localZeroPortPolicy = (ZeroPortPolicy)this.policyMap.get(localInteger);
/* 337 */     if (localZeroPortPolicy == null) {
/* 338 */       return false;
/*     */     }
/* 340 */     return localZeroPortPolicy.forceZeroPort();
/*     */   }
/*     */ 
/*     */   public final int getCopierId()
/*     */   {
/* 345 */     Integer localInteger = new Integer(1398079490);
/* 346 */     CopyObjectPolicy localCopyObjectPolicy = (CopyObjectPolicy)this.policyMap.get(localInteger);
/* 347 */     if (localCopyObjectPolicy != null) {
/* 348 */       return localCopyObjectPolicy.getValue();
/*     */     }
/* 350 */     return this.defaultObjectCopierFactoryId;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.oa.poa.Policies
 * JD-Core Version:    0.6.2
 */