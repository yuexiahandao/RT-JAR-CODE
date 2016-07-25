/*     */ package com.sun.corba.se.impl.transport;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.impl.protocol.CorbaInvocationInfo;
/*     */ import com.sun.corba.se.pept.transport.ContactInfo;
/*     */ import com.sun.corba.se.pept.transport.ContactInfoList;
/*     */ import com.sun.corba.se.spi.ior.IOR;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.orb.ORBData;
/*     */ import com.sun.corba.se.spi.transport.CorbaContactInfo;
/*     */ import com.sun.corba.se.spi.transport.CorbaContactInfoList;
/*     */ import com.sun.corba.se.spi.transport.CorbaContactInfoListIterator;
/*     */ import com.sun.corba.se.spi.transport.IIOPPrimaryToContactInfo;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.omg.CORBA.COMM_FAILURE;
/*     */ import org.omg.CORBA.CompletionStatus;
/*     */ import org.omg.CORBA.SystemException;
/*     */ 
/*     */ public class CorbaContactInfoListIteratorImpl
/*     */   implements CorbaContactInfoListIterator
/*     */ {
/*     */   protected ORB orb;
/*     */   protected CorbaContactInfoList contactInfoList;
/*     */   protected CorbaContactInfo successContactInfo;
/*     */   protected CorbaContactInfo failureContactInfo;
/*     */   protected RuntimeException failureException;
/*     */   protected Iterator effectiveTargetIORIterator;
/*     */   protected CorbaContactInfo previousContactInfo;
/*     */   protected boolean isAddrDispositionRetry;
/*     */   protected IIOPPrimaryToContactInfo primaryToContactInfo;
/*     */   protected ContactInfo primaryContactInfo;
/*     */   protected List listOfContactInfos;
/*     */ 
/*     */   public CorbaContactInfoListIteratorImpl(ORB paramORB, CorbaContactInfoList paramCorbaContactInfoList, ContactInfo paramContactInfo, List paramList)
/*     */   {
/*  77 */     this.orb = paramORB;
/*  78 */     this.contactInfoList = paramCorbaContactInfoList;
/*  79 */     this.primaryContactInfo = paramContactInfo;
/*  80 */     if (paramList != null)
/*     */     {
/*  83 */       this.effectiveTargetIORIterator = paramList.iterator();
/*     */     }
/*     */ 
/*  86 */     this.listOfContactInfos = paramList;
/*     */ 
/*  88 */     this.previousContactInfo = null;
/*  89 */     this.isAddrDispositionRetry = false;
/*     */ 
/*  91 */     this.successContactInfo = null;
/*  92 */     this.failureContactInfo = null;
/*  93 */     this.failureException = null;
/*     */ 
/*  95 */     this.primaryToContactInfo = paramORB.getORBData().getIIOPPrimaryToContactInfo();
/*     */   }
/*     */ 
/*     */   public boolean hasNext()
/*     */   {
/* 109 */     if (this.isAddrDispositionRetry)
/* 110 */       return true;
/*     */     boolean bool;
/* 115 */     if (this.primaryToContactInfo != null) {
/* 116 */       bool = this.primaryToContactInfo.hasNext(this.primaryContactInfo, this.previousContactInfo, this.listOfContactInfos);
/*     */     }
/*     */     else
/*     */     {
/* 120 */       bool = this.effectiveTargetIORIterator.hasNext();
/*     */     }
/*     */ 
/* 123 */     return bool;
/*     */   }
/*     */ 
/*     */   public Object next()
/*     */   {
/* 128 */     if (this.isAddrDispositionRetry) {
/* 129 */       this.isAddrDispositionRetry = false;
/* 130 */       return this.previousContactInfo;
/*     */     }
/*     */ 
/* 138 */     if (this.primaryToContactInfo != null) {
/* 139 */       this.previousContactInfo = ((CorbaContactInfo)this.primaryToContactInfo.next(this.primaryContactInfo, this.previousContactInfo, this.listOfContactInfos));
/*     */     }
/*     */     else
/*     */     {
/* 144 */       this.previousContactInfo = ((CorbaContactInfo)this.effectiveTargetIORIterator.next());
/*     */     }
/*     */ 
/* 148 */     return this.previousContactInfo;
/*     */   }
/*     */ 
/*     */   public void remove()
/*     */   {
/* 153 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public ContactInfoList getContactInfoList()
/*     */   {
/* 163 */     return this.contactInfoList;
/*     */   }
/*     */ 
/*     */   public void reportSuccess(ContactInfo paramContactInfo)
/*     */   {
/* 168 */     this.successContactInfo = ((CorbaContactInfo)paramContactInfo);
/*     */   }
/*     */ 
/*     */   public boolean reportException(ContactInfo paramContactInfo, RuntimeException paramRuntimeException)
/*     */   {
/* 174 */     this.failureContactInfo = ((CorbaContactInfo)paramContactInfo);
/* 175 */     this.failureException = paramRuntimeException;
/* 176 */     if ((paramRuntimeException instanceof COMM_FAILURE)) {
/* 177 */       SystemException localSystemException = (SystemException)paramRuntimeException;
/* 178 */       if (localSystemException.completed == CompletionStatus.COMPLETED_NO) {
/* 179 */         if (hasNext()) {
/* 180 */           return true;
/*     */         }
/* 182 */         if (this.contactInfoList.getEffectiveTargetIOR() != this.contactInfoList.getTargetIOR())
/*     */         {
/* 186 */           updateEffectiveTargetIOR(this.contactInfoList.getTargetIOR());
/* 187 */           return true;
/*     */         }
/*     */       }
/*     */     }
/* 191 */     return false;
/*     */   }
/*     */ 
/*     */   public RuntimeException getFailureException()
/*     */   {
/* 196 */     if (this.failureException == null) {
/* 197 */       return ORBUtilSystemException.get(this.orb, "rpc.transport").invalidContactInfoListIteratorFailureException();
/*     */     }
/*     */ 
/* 202 */     return this.failureException;
/*     */   }
/*     */ 
/*     */   public void reportAddrDispositionRetry(CorbaContactInfo paramCorbaContactInfo, short paramShort)
/*     */   {
/* 214 */     this.previousContactInfo.setAddressingDisposition(paramShort);
/* 215 */     this.isAddrDispositionRetry = true;
/*     */   }
/*     */ 
/*     */   public void reportRedirect(CorbaContactInfo paramCorbaContactInfo, IOR paramIOR)
/*     */   {
/* 221 */     updateEffectiveTargetIOR(paramIOR);
/*     */   }
/*     */ 
/*     */   public void updateEffectiveTargetIOR(IOR paramIOR)
/*     */   {
/* 245 */     this.contactInfoList.setEffectiveTargetIOR(paramIOR);
/*     */ 
/* 254 */     ((CorbaInvocationInfo)this.orb.getInvocationInfo()).setContactInfoListIterator(this.contactInfoList.iterator());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.transport.CorbaContactInfoListIteratorImpl
 * JD-Core Version:    0.6.2
 */