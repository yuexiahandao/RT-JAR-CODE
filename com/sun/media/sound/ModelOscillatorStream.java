package com.sun.media.sound;

import java.io.IOException;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.VoiceStatus;

public abstract interface ModelOscillatorStream
{
  public abstract void setPitch(float paramFloat);

  public abstract void noteOn(MidiChannel paramMidiChannel, VoiceStatus paramVoiceStatus, int paramInt1, int paramInt2);

  public abstract void noteOff(int paramInt);

  public abstract int read(float[][] paramArrayOfFloat, int paramInt1, int paramInt2)
    throws IOException;

  public abstract void close()
    throws IOException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.ModelOscillatorStream
 * JD-Core Version:    0.6.2
 */