package com.sun.jmx.snmp.internal;

import com.sun.jmx.snmp.SnmpParams;
import com.sun.jmx.snmp.SnmpPdu;
import com.sun.jmx.snmp.SnmpPduFactory;
import com.sun.jmx.snmp.SnmpSecurityParameters;
import com.sun.jmx.snmp.SnmpStatusException;
import com.sun.jmx.snmp.SnmpTooBigException;
import com.sun.jmx.snmp.SnmpUnknownMsgProcModelException;

public abstract interface SnmpMsgProcessingSubSystem extends SnmpSubSystem
{
  public abstract void setSecuritySubSystem(SnmpSecuritySubSystem paramSnmpSecuritySubSystem);

  public abstract SnmpSecuritySubSystem getSecuritySubSystem();

  public abstract SnmpIncomingRequest getIncomingRequest(int paramInt, SnmpPduFactory paramSnmpPduFactory)
    throws SnmpUnknownMsgProcModelException;

  public abstract SnmpOutgoingRequest getOutgoingRequest(int paramInt, SnmpPduFactory paramSnmpPduFactory)
    throws SnmpUnknownMsgProcModelException;

  public abstract SnmpPdu getRequestPdu(int paramInt1, SnmpParams paramSnmpParams, int paramInt2)
    throws SnmpUnknownMsgProcModelException, SnmpStatusException;

  public abstract SnmpIncomingResponse getIncomingResponse(int paramInt, SnmpPduFactory paramSnmpPduFactory)
    throws SnmpUnknownMsgProcModelException;

  public abstract int encode(int paramInt1, int paramInt2, int paramInt3, byte paramByte, int paramInt4, SnmpSecurityParameters paramSnmpSecurityParameters, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, int paramInt5, byte[] paramArrayOfByte4)
    throws SnmpTooBigException, SnmpUnknownMsgProcModelException;

  public abstract int encodePriv(int paramInt1, int paramInt2, int paramInt3, byte paramByte, int paramInt4, SnmpSecurityParameters paramSnmpSecurityParameters, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    throws SnmpTooBigException, SnmpUnknownMsgProcModelException;

  public abstract SnmpDecryptedPdu decode(int paramInt, byte[] paramArrayOfByte)
    throws SnmpStatusException, SnmpUnknownMsgProcModelException;

  public abstract int encode(int paramInt, SnmpDecryptedPdu paramSnmpDecryptedPdu, byte[] paramArrayOfByte)
    throws SnmpTooBigException, SnmpUnknownMsgProcModelException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.internal.SnmpMsgProcessingSubSystem
 * JD-Core Version:    0.6.2
 */