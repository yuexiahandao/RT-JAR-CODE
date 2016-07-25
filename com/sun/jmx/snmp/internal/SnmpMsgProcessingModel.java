package com.sun.jmx.snmp.internal;

import com.sun.jmx.snmp.SnmpParams;
import com.sun.jmx.snmp.SnmpPdu;
import com.sun.jmx.snmp.SnmpPduFactory;
import com.sun.jmx.snmp.SnmpSecurityParameters;
import com.sun.jmx.snmp.SnmpStatusException;
import com.sun.jmx.snmp.SnmpTooBigException;
import com.sun.jmx.snmp.mpm.SnmpMsgTranslator;

public abstract interface SnmpMsgProcessingModel extends SnmpModel
{
  public abstract SnmpOutgoingRequest getOutgoingRequest(SnmpPduFactory paramSnmpPduFactory);

  public abstract SnmpIncomingRequest getIncomingRequest(SnmpPduFactory paramSnmpPduFactory);

  public abstract SnmpIncomingResponse getIncomingResponse(SnmpPduFactory paramSnmpPduFactory);

  public abstract SnmpPdu getRequestPdu(SnmpParams paramSnmpParams, int paramInt)
    throws SnmpStatusException;

  public abstract int encode(int paramInt1, int paramInt2, int paramInt3, byte paramByte, int paramInt4, SnmpSecurityParameters paramSnmpSecurityParameters, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, int paramInt5, byte[] paramArrayOfByte4)
    throws SnmpTooBigException;

  public abstract int encodePriv(int paramInt1, int paramInt2, int paramInt3, byte paramByte, int paramInt4, SnmpSecurityParameters paramSnmpSecurityParameters, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    throws SnmpTooBigException;

  public abstract SnmpDecryptedPdu decode(byte[] paramArrayOfByte)
    throws SnmpStatusException;

  public abstract int encode(SnmpDecryptedPdu paramSnmpDecryptedPdu, byte[] paramArrayOfByte)
    throws SnmpTooBigException;

  public abstract void setMsgTranslator(SnmpMsgTranslator paramSnmpMsgTranslator);

  public abstract SnmpMsgTranslator getMsgTranslator();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.internal.SnmpMsgProcessingModel
 * JD-Core Version:    0.6.2
 */