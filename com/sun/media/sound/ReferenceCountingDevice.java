package com.sun.media.sound;

import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

public abstract interface ReferenceCountingDevice
{
  public abstract Receiver getReceiverReferenceCounting()
    throws MidiUnavailableException;

  public abstract Transmitter getTransmitterReferenceCounting()
    throws MidiUnavailableException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.ReferenceCountingDevice
 * JD-Core Version:    0.6.2
 */