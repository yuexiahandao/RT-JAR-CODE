/*      */ package com.sun.media.sound;
/*      */ 
/*      */ import java.util.List;
/*      */ import java.util.Random;
/*      */ import javax.sound.midi.Patch;
/*      */ import javax.sound.midi.SoundbankResource;
/*      */ import javax.sound.sampled.AudioFormat;
/*      */ 
/*      */ public final class EmergencySoundbank
/*      */ {
/*   40 */   private static final String[] general_midi_instruments = { "Acoustic Grand Piano", "Bright Acoustic Piano", "Electric Grand Piano", "Honky-tonk Piano", "Electric Piano 1", "Electric Piano 2", "Harpsichord", "Clavi", "Celesta", "Glockenspiel", "Music Box", "Vibraphone", "Marimba", "Xylophone", "Tubular Bells", "Dulcimer", "Drawbar Organ", "Percussive Organ", "Rock Organ", "Church Organ", "Reed Organ", "Accordion", "Harmonica", "Tango Accordion", "Acoustic Guitar (nylon)", "Acoustic Guitar (steel)", "Electric Guitar (jazz)", "Electric Guitar (clean)", "Electric Guitar (muted)", "Overdriven Guitar", "Distortion Guitar", "Guitar harmonics", "Acoustic Bass", "Electric Bass (finger)", "Electric Bass (pick)", "Fretless Bass", "Slap Bass 1", "Slap Bass 2", "Synth Bass 1", "Synth Bass 2", "Violin", "Viola", "Cello", "Contrabass", "Tremolo Strings", "Pizzicato Strings", "Orchestral Harp", "Timpani", "String Ensemble 1", "String Ensemble 2", "SynthStrings 1", "SynthStrings 2", "Choir Aahs", "Voice Oohs", "Synth Voice", "Orchestra Hit", "Trumpet", "Trombone", "Tuba", "Muted Trumpet", "French Horn", "Brass Section", "SynthBrass 1", "SynthBrass 2", "Soprano Sax", "Alto Sax", "Tenor Sax", "Baritone Sax", "Oboe", "English Horn", "Bassoon", "Clarinet", "Piccolo", "Flute", "Recorder", "Pan Flute", "Blown Bottle", "Shakuhachi", "Whistle", "Ocarina", "Lead 1 (square)", "Lead 2 (sawtooth)", "Lead 3 (calliope)", "Lead 4 (chiff)", "Lead 5 (charang)", "Lead 6 (voice)", "Lead 7 (fifths)", "Lead 8 (bass + lead)", "Pad 1 (new age)", "Pad 2 (warm)", "Pad 3 (polysynth)", "Pad 4 (choir)", "Pad 5 (bowed)", "Pad 6 (metallic)", "Pad 7 (halo)", "Pad 8 (sweep)", "FX 1 (rain)", "FX 2 (soundtrack)", "FX 3 (crystal)", "FX 4 (atmosphere)", "FX 5 (brightness)", "FX 6 (goblins)", "FX 7 (echoes)", "FX 8 (sci-fi)", "Sitar", "Banjo", "Shamisen", "Koto", "Kalimba", "Bag pipe", "Fiddle", "Shanai", "Tinkle Bell", "Agogo", "Steel Drums", "Woodblock", "Taiko Drum", "Melodic Tom", "Synth Drum", "Reverse Cymbal", "Guitar Fret Noise", "Breath Noise", "Seashore", "Bird Tweet", "Telephone Ring", "Helicopter", "Applause", "Gunshot" };
/*      */ 
/*      */   public static SF2Soundbank createSoundbank()
/*      */     throws Exception
/*      */   {
/*  172 */     SF2Soundbank localSF2Soundbank = new SF2Soundbank();
/*  173 */     localSF2Soundbank.setName("Emergency GM sound set");
/*  174 */     localSF2Soundbank.setVendor("Generated");
/*  175 */     localSF2Soundbank.setDescription("Emergency generated soundbank");
/*      */ 
/*  181 */     SF2Layer localSF2Layer1 = new_bass_drum(localSF2Soundbank);
/*  182 */     SF2Layer localSF2Layer2 = new_snare_drum(localSF2Soundbank);
/*  183 */     SF2Layer localSF2Layer3 = new_tom(localSF2Soundbank);
/*  184 */     SF2Layer localSF2Layer4 = new_open_hihat(localSF2Soundbank);
/*  185 */     SF2Layer localSF2Layer5 = new_closed_hihat(localSF2Soundbank);
/*  186 */     SF2Layer localSF2Layer6 = new_crash_cymbal(localSF2Soundbank);
/*  187 */     SF2Layer localSF2Layer7 = new_side_stick(localSF2Soundbank);
/*      */ 
/*  189 */     SF2Layer[] arrayOfSF2Layer = new SF2Layer[''];
/*  190 */     arrayOfSF2Layer[35] = localSF2Layer1;
/*  191 */     arrayOfSF2Layer[36] = localSF2Layer1;
/*  192 */     arrayOfSF2Layer[38] = localSF2Layer2;
/*  193 */     arrayOfSF2Layer[40] = localSF2Layer2;
/*  194 */     arrayOfSF2Layer[41] = localSF2Layer3;
/*  195 */     arrayOfSF2Layer[43] = localSF2Layer3;
/*  196 */     arrayOfSF2Layer[45] = localSF2Layer3;
/*  197 */     arrayOfSF2Layer[47] = localSF2Layer3;
/*  198 */     arrayOfSF2Layer[48] = localSF2Layer3;
/*  199 */     arrayOfSF2Layer[50] = localSF2Layer3;
/*  200 */     arrayOfSF2Layer[42] = localSF2Layer5;
/*  201 */     arrayOfSF2Layer[44] = localSF2Layer5;
/*  202 */     arrayOfSF2Layer[46] = localSF2Layer4;
/*  203 */     arrayOfSF2Layer[49] = localSF2Layer6;
/*  204 */     arrayOfSF2Layer[51] = localSF2Layer6;
/*  205 */     arrayOfSF2Layer[52] = localSF2Layer6;
/*  206 */     arrayOfSF2Layer[55] = localSF2Layer6;
/*  207 */     arrayOfSF2Layer[57] = localSF2Layer6;
/*  208 */     arrayOfSF2Layer[59] = localSF2Layer6;
/*      */ 
/*  211 */     arrayOfSF2Layer[37] = localSF2Layer7;
/*  212 */     arrayOfSF2Layer[39] = localSF2Layer7;
/*  213 */     arrayOfSF2Layer[53] = localSF2Layer7;
/*  214 */     arrayOfSF2Layer[54] = localSF2Layer7;
/*  215 */     arrayOfSF2Layer[56] = localSF2Layer7;
/*  216 */     arrayOfSF2Layer[58] = localSF2Layer7;
/*  217 */     arrayOfSF2Layer[69] = localSF2Layer7;
/*  218 */     arrayOfSF2Layer[70] = localSF2Layer7;
/*  219 */     arrayOfSF2Layer[75] = localSF2Layer7;
/*  220 */     arrayOfSF2Layer[60] = localSF2Layer7;
/*  221 */     arrayOfSF2Layer[61] = localSF2Layer7;
/*  222 */     arrayOfSF2Layer[62] = localSF2Layer7;
/*  223 */     arrayOfSF2Layer[63] = localSF2Layer7;
/*  224 */     arrayOfSF2Layer[64] = localSF2Layer7;
/*  225 */     arrayOfSF2Layer[65] = localSF2Layer7;
/*  226 */     arrayOfSF2Layer[66] = localSF2Layer7;
/*  227 */     arrayOfSF2Layer[67] = localSF2Layer7;
/*  228 */     arrayOfSF2Layer[68] = localSF2Layer7;
/*  229 */     arrayOfSF2Layer[71] = localSF2Layer7;
/*  230 */     arrayOfSF2Layer[72] = localSF2Layer7;
/*  231 */     arrayOfSF2Layer[73] = localSF2Layer7;
/*  232 */     arrayOfSF2Layer[74] = localSF2Layer7;
/*  233 */     arrayOfSF2Layer[76] = localSF2Layer7;
/*  234 */     arrayOfSF2Layer[77] = localSF2Layer7;
/*  235 */     arrayOfSF2Layer[78] = localSF2Layer7;
/*  236 */     arrayOfSF2Layer[79] = localSF2Layer7;
/*  237 */     arrayOfSF2Layer[80] = localSF2Layer7;
/*  238 */     arrayOfSF2Layer[81] = localSF2Layer7;
/*      */ 
/*  241 */     SF2Instrument localSF2Instrument1 = new SF2Instrument(localSF2Soundbank);
/*  242 */     localSF2Instrument1.setName("Standard Kit");
/*  243 */     localSF2Instrument1.setPatch(new ModelPatch(0, 0, true));
/*  244 */     localSF2Soundbank.addInstrument(localSF2Instrument1);
/*  245 */     for (int i = 0; i < arrayOfSF2Layer.length; i++) {
/*  246 */       if (arrayOfSF2Layer[i] != null) {
/*  247 */         localObject1 = new SF2InstrumentRegion();
/*  248 */         ((SF2InstrumentRegion)localObject1).setLayer(arrayOfSF2Layer[i]);
/*  249 */         ((SF2InstrumentRegion)localObject1).putBytes(43, new byte[] { (byte)i, (byte)i });
/*      */ 
/*  251 */         localSF2Instrument1.getRegions().add(localObject1);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  260 */     SF2Layer localSF2Layer8 = new_gpiano(localSF2Soundbank);
/*  261 */     Object localObject1 = new_gpiano2(localSF2Soundbank);
/*  262 */     SF2Layer localSF2Layer9 = new_piano_hammer(localSF2Soundbank);
/*  263 */     SF2Layer localSF2Layer10 = new_piano1(localSF2Soundbank);
/*  264 */     SF2Layer localSF2Layer11 = new_epiano1(localSF2Soundbank);
/*  265 */     SF2Layer localSF2Layer12 = new_epiano2(localSF2Soundbank);
/*      */ 
/*  267 */     SF2Layer localSF2Layer13 = new_guitar1(localSF2Soundbank);
/*  268 */     SF2Layer localSF2Layer14 = new_guitar_pick(localSF2Soundbank);
/*  269 */     SF2Layer localSF2Layer15 = new_guitar_dist(localSF2Soundbank);
/*  270 */     SF2Layer localSF2Layer16 = new_bass1(localSF2Soundbank);
/*  271 */     SF2Layer localSF2Layer17 = new_bass2(localSF2Soundbank);
/*  272 */     SF2Layer localSF2Layer18 = new_synthbass(localSF2Soundbank);
/*  273 */     SF2Layer localSF2Layer19 = new_string2(localSF2Soundbank);
/*  274 */     SF2Layer localSF2Layer20 = new_orchhit(localSF2Soundbank);
/*  275 */     SF2Layer localSF2Layer21 = new_choir(localSF2Soundbank);
/*  276 */     SF2Layer localSF2Layer22 = new_solostring(localSF2Soundbank);
/*  277 */     SF2Layer localSF2Layer23 = new_organ(localSF2Soundbank);
/*  278 */     SF2Layer localSF2Layer24 = new_ch_organ(localSF2Soundbank);
/*  279 */     SF2Layer localSF2Layer25 = new_bell(localSF2Soundbank);
/*  280 */     SF2Layer localSF2Layer26 = new_flute(localSF2Soundbank);
/*      */ 
/*  282 */     SF2Layer localSF2Layer27 = new_timpani(localSF2Soundbank);
/*  283 */     SF2Layer localSF2Layer28 = new_melodic_toms(localSF2Soundbank);
/*  284 */     SF2Layer localSF2Layer29 = new_trumpet(localSF2Soundbank);
/*  285 */     SF2Layer localSF2Layer30 = new_trombone(localSF2Soundbank);
/*  286 */     SF2Layer localSF2Layer31 = new_brass_section(localSF2Soundbank);
/*  287 */     SF2Layer localSF2Layer32 = new_horn(localSF2Soundbank);
/*  288 */     SF2Layer localSF2Layer33 = new_sax(localSF2Soundbank);
/*  289 */     SF2Layer localSF2Layer34 = new_oboe(localSF2Soundbank);
/*  290 */     SF2Layer localSF2Layer35 = new_bassoon(localSF2Soundbank);
/*  291 */     SF2Layer localSF2Layer36 = new_clarinet(localSF2Soundbank);
/*  292 */     SF2Layer localSF2Layer37 = new_reverse_cymbal(localSF2Soundbank);
/*      */ 
/*  294 */     SF2Layer localSF2Layer38 = localSF2Layer10;
/*      */ 
/*  296 */     newInstrument(localSF2Soundbank, "Piano", new Patch(0, 0), new SF2Layer[] { localSF2Layer8, localSF2Layer9 });
/*  297 */     newInstrument(localSF2Soundbank, "Piano", new Patch(0, 1), new SF2Layer[] { localObject1, localSF2Layer9 });
/*  298 */     newInstrument(localSF2Soundbank, "Piano", new Patch(0, 2), new SF2Layer[] { localSF2Layer10 });
/*      */ 
/*  300 */     SF2Instrument localSF2Instrument2 = newInstrument(localSF2Soundbank, "Honky-tonk Piano", new Patch(0, 3), new SF2Layer[] { localSF2Layer10, localSF2Layer10 });
/*      */ 
/*  302 */     SF2InstrumentRegion localSF2InstrumentRegion = (SF2InstrumentRegion)localSF2Instrument2.getRegions().get(0);
/*  303 */     localSF2InstrumentRegion.putInteger(8, 80);
/*  304 */     localSF2InstrumentRegion.putInteger(52, 30);
/*  305 */     localSF2InstrumentRegion = (SF2InstrumentRegion)localSF2Instrument2.getRegions().get(1);
/*  306 */     localSF2InstrumentRegion.putInteger(8, 30);
/*      */ 
/*  308 */     newInstrument(localSF2Soundbank, "Rhodes", new Patch(0, 4), new SF2Layer[] { localSF2Layer12 });
/*  309 */     newInstrument(localSF2Soundbank, "Rhodes", new Patch(0, 5), new SF2Layer[] { localSF2Layer12 });
/*  310 */     newInstrument(localSF2Soundbank, "Clavinet", new Patch(0, 6), new SF2Layer[] { localSF2Layer11 });
/*  311 */     newInstrument(localSF2Soundbank, "Clavinet", new Patch(0, 7), new SF2Layer[] { localSF2Layer11 });
/*  312 */     newInstrument(localSF2Soundbank, "Rhodes", new Patch(0, 8), new SF2Layer[] { localSF2Layer12 });
/*  313 */     newInstrument(localSF2Soundbank, "Bell", new Patch(0, 9), new SF2Layer[] { localSF2Layer25 });
/*  314 */     newInstrument(localSF2Soundbank, "Bell", new Patch(0, 10), new SF2Layer[] { localSF2Layer25 });
/*  315 */     newInstrument(localSF2Soundbank, "Vibraphone", new Patch(0, 11), new SF2Layer[] { localSF2Layer25 });
/*  316 */     newInstrument(localSF2Soundbank, "Marimba", new Patch(0, 12), new SF2Layer[] { localSF2Layer25 });
/*  317 */     newInstrument(localSF2Soundbank, "Marimba", new Patch(0, 13), new SF2Layer[] { localSF2Layer25 });
/*  318 */     newInstrument(localSF2Soundbank, "Bell", new Patch(0, 14), new SF2Layer[] { localSF2Layer25 });
/*  319 */     newInstrument(localSF2Soundbank, "Rock Organ", new Patch(0, 15), new SF2Layer[] { localSF2Layer23 });
/*  320 */     newInstrument(localSF2Soundbank, "Rock Organ", new Patch(0, 16), new SF2Layer[] { localSF2Layer23 });
/*  321 */     newInstrument(localSF2Soundbank, "Perc Organ", new Patch(0, 17), new SF2Layer[] { localSF2Layer23 });
/*  322 */     newInstrument(localSF2Soundbank, "Rock Organ", new Patch(0, 18), new SF2Layer[] { localSF2Layer23 });
/*  323 */     newInstrument(localSF2Soundbank, "Church Organ", new Patch(0, 19), new SF2Layer[] { localSF2Layer24 });
/*  324 */     newInstrument(localSF2Soundbank, "Accordion", new Patch(0, 20), new SF2Layer[] { localSF2Layer23 });
/*  325 */     newInstrument(localSF2Soundbank, "Accordion", new Patch(0, 21), new SF2Layer[] { localSF2Layer23 });
/*  326 */     newInstrument(localSF2Soundbank, "Accordion", new Patch(0, 22), new SF2Layer[] { localSF2Layer23 });
/*  327 */     newInstrument(localSF2Soundbank, "Accordion", new Patch(0, 23), new SF2Layer[] { localSF2Layer23 });
/*  328 */     newInstrument(localSF2Soundbank, "Guitar", new Patch(0, 24), new SF2Layer[] { localSF2Layer13, localSF2Layer14 });
/*  329 */     newInstrument(localSF2Soundbank, "Guitar", new Patch(0, 25), new SF2Layer[] { localSF2Layer13, localSF2Layer14 });
/*  330 */     newInstrument(localSF2Soundbank, "Guitar", new Patch(0, 26), new SF2Layer[] { localSF2Layer13, localSF2Layer14 });
/*  331 */     newInstrument(localSF2Soundbank, "Guitar", new Patch(0, 27), new SF2Layer[] { localSF2Layer13, localSF2Layer14 });
/*  332 */     newInstrument(localSF2Soundbank, "Guitar", new Patch(0, 28), new SF2Layer[] { localSF2Layer13, localSF2Layer14 });
/*  333 */     newInstrument(localSF2Soundbank, "Distorted Guitar", new Patch(0, 29), new SF2Layer[] { localSF2Layer15 });
/*  334 */     newInstrument(localSF2Soundbank, "Distorted Guitar", new Patch(0, 30), new SF2Layer[] { localSF2Layer15 });
/*  335 */     newInstrument(localSF2Soundbank, "Guitar", new Patch(0, 31), new SF2Layer[] { localSF2Layer13, localSF2Layer14 });
/*  336 */     newInstrument(localSF2Soundbank, "Finger Bass", new Patch(0, 32), new SF2Layer[] { localSF2Layer16 });
/*  337 */     newInstrument(localSF2Soundbank, "Finger Bass", new Patch(0, 33), new SF2Layer[] { localSF2Layer16 });
/*  338 */     newInstrument(localSF2Soundbank, "Finger Bass", new Patch(0, 34), new SF2Layer[] { localSF2Layer16 });
/*  339 */     newInstrument(localSF2Soundbank, "Frettless Bass", new Patch(0, 35), new SF2Layer[] { localSF2Layer17 });
/*  340 */     newInstrument(localSF2Soundbank, "Frettless Bass", new Patch(0, 36), new SF2Layer[] { localSF2Layer17 });
/*  341 */     newInstrument(localSF2Soundbank, "Frettless Bass", new Patch(0, 37), new SF2Layer[] { localSF2Layer17 });
/*  342 */     newInstrument(localSF2Soundbank, "Synth Bass1", new Patch(0, 38), new SF2Layer[] { localSF2Layer18 });
/*  343 */     newInstrument(localSF2Soundbank, "Synth Bass2", new Patch(0, 39), new SF2Layer[] { localSF2Layer18 });
/*  344 */     newInstrument(localSF2Soundbank, "Solo String", new Patch(0, 40), new SF2Layer[] { localSF2Layer19, localSF2Layer22 });
/*  345 */     newInstrument(localSF2Soundbank, "Solo String", new Patch(0, 41), new SF2Layer[] { localSF2Layer19, localSF2Layer22 });
/*  346 */     newInstrument(localSF2Soundbank, "Solo String", new Patch(0, 42), new SF2Layer[] { localSF2Layer19, localSF2Layer22 });
/*  347 */     newInstrument(localSF2Soundbank, "Solo String", new Patch(0, 43), new SF2Layer[] { localSF2Layer19, localSF2Layer22 });
/*  348 */     newInstrument(localSF2Soundbank, "Solo String", new Patch(0, 44), new SF2Layer[] { localSF2Layer19, localSF2Layer22 });
/*  349 */     newInstrument(localSF2Soundbank, "Def", new Patch(0, 45), new SF2Layer[] { localSF2Layer38 });
/*  350 */     newInstrument(localSF2Soundbank, "Harp", new Patch(0, 46), new SF2Layer[] { localSF2Layer25 });
/*  351 */     newInstrument(localSF2Soundbank, "Timpani", new Patch(0, 47), new SF2Layer[] { localSF2Layer27 });
/*  352 */     newInstrument(localSF2Soundbank, "Strings", new Patch(0, 48), new SF2Layer[] { localSF2Layer19 });
/*  353 */     localSF2Instrument2 = newInstrument(localSF2Soundbank, "Slow Strings", new Patch(0, 49), new SF2Layer[] { localSF2Layer19 });
/*      */ 
/*  355 */     localSF2InstrumentRegion = (SF2InstrumentRegion)localSF2Instrument2.getRegions().get(0);
/*  356 */     localSF2InstrumentRegion.putInteger(34, 2500);
/*  357 */     localSF2InstrumentRegion.putInteger(38, 2000);
/*  358 */     newInstrument(localSF2Soundbank, "Synth Strings", new Patch(0, 50), new SF2Layer[] { localSF2Layer19 });
/*  359 */     newInstrument(localSF2Soundbank, "Synth Strings", new Patch(0, 51), new SF2Layer[] { localSF2Layer19 });
/*      */ 
/*  362 */     newInstrument(localSF2Soundbank, "Choir", new Patch(0, 52), new SF2Layer[] { localSF2Layer21 });
/*  363 */     newInstrument(localSF2Soundbank, "Choir", new Patch(0, 53), new SF2Layer[] { localSF2Layer21 });
/*  364 */     newInstrument(localSF2Soundbank, "Choir", new Patch(0, 54), new SF2Layer[] { localSF2Layer21 });
/*      */ 
/*  366 */     Object localObject2 = newInstrument(localSF2Soundbank, "Orch Hit", new Patch(0, 55), new SF2Layer[] { localSF2Layer20, localSF2Layer20, localSF2Layer27 });
/*      */ 
/*  368 */     localSF2InstrumentRegion = (SF2InstrumentRegion)((SF2Instrument)localObject2).getRegions().get(0);
/*  369 */     localSF2InstrumentRegion.putInteger(51, -12);
/*  370 */     localSF2InstrumentRegion.putInteger(48, -100);
/*      */ 
/*  372 */     newInstrument(localSF2Soundbank, "Trumpet", new Patch(0, 56), new SF2Layer[] { localSF2Layer29 });
/*  373 */     newInstrument(localSF2Soundbank, "Trombone", new Patch(0, 57), new SF2Layer[] { localSF2Layer30 });
/*  374 */     newInstrument(localSF2Soundbank, "Trombone", new Patch(0, 58), new SF2Layer[] { localSF2Layer30 });
/*  375 */     newInstrument(localSF2Soundbank, "Trumpet", new Patch(0, 59), new SF2Layer[] { localSF2Layer29 });
/*  376 */     newInstrument(localSF2Soundbank, "Horn", new Patch(0, 60), new SF2Layer[] { localSF2Layer32 });
/*  377 */     newInstrument(localSF2Soundbank, "Brass Section", new Patch(0, 61), new SF2Layer[] { localSF2Layer31 });
/*  378 */     newInstrument(localSF2Soundbank, "Brass Section", new Patch(0, 62), new SF2Layer[] { localSF2Layer31 });
/*  379 */     newInstrument(localSF2Soundbank, "Brass Section", new Patch(0, 63), new SF2Layer[] { localSF2Layer31 });
/*  380 */     newInstrument(localSF2Soundbank, "Sax", new Patch(0, 64), new SF2Layer[] { localSF2Layer33 });
/*  381 */     newInstrument(localSF2Soundbank, "Sax", new Patch(0, 65), new SF2Layer[] { localSF2Layer33 });
/*  382 */     newInstrument(localSF2Soundbank, "Sax", new Patch(0, 66), new SF2Layer[] { localSF2Layer33 });
/*  383 */     newInstrument(localSF2Soundbank, "Sax", new Patch(0, 67), new SF2Layer[] { localSF2Layer33 });
/*  384 */     newInstrument(localSF2Soundbank, "Oboe", new Patch(0, 68), new SF2Layer[] { localSF2Layer34 });
/*  385 */     newInstrument(localSF2Soundbank, "Horn", new Patch(0, 69), new SF2Layer[] { localSF2Layer32 });
/*  386 */     newInstrument(localSF2Soundbank, "Bassoon", new Patch(0, 70), new SF2Layer[] { localSF2Layer35 });
/*  387 */     newInstrument(localSF2Soundbank, "Clarinet", new Patch(0, 71), new SF2Layer[] { localSF2Layer36 });
/*  388 */     newInstrument(localSF2Soundbank, "Flute", new Patch(0, 72), new SF2Layer[] { localSF2Layer26 });
/*  389 */     newInstrument(localSF2Soundbank, "Flute", new Patch(0, 73), new SF2Layer[] { localSF2Layer26 });
/*  390 */     newInstrument(localSF2Soundbank, "Flute", new Patch(0, 74), new SF2Layer[] { localSF2Layer26 });
/*  391 */     newInstrument(localSF2Soundbank, "Flute", new Patch(0, 75), new SF2Layer[] { localSF2Layer26 });
/*  392 */     newInstrument(localSF2Soundbank, "Flute", new Patch(0, 76), new SF2Layer[] { localSF2Layer26 });
/*  393 */     newInstrument(localSF2Soundbank, "Flute", new Patch(0, 77), new SF2Layer[] { localSF2Layer26 });
/*  394 */     newInstrument(localSF2Soundbank, "Flute", new Patch(0, 78), new SF2Layer[] { localSF2Layer26 });
/*  395 */     newInstrument(localSF2Soundbank, "Flute", new Patch(0, 79), new SF2Layer[] { localSF2Layer26 });
/*  396 */     newInstrument(localSF2Soundbank, "Organ", new Patch(0, 80), new SF2Layer[] { localSF2Layer23 });
/*  397 */     newInstrument(localSF2Soundbank, "Organ", new Patch(0, 81), new SF2Layer[] { localSF2Layer23 });
/*  398 */     newInstrument(localSF2Soundbank, "Flute", new Patch(0, 82), new SF2Layer[] { localSF2Layer26 });
/*  399 */     newInstrument(localSF2Soundbank, "Organ", new Patch(0, 83), new SF2Layer[] { localSF2Layer23 });
/*  400 */     newInstrument(localSF2Soundbank, "Organ", new Patch(0, 84), new SF2Layer[] { localSF2Layer23 });
/*  401 */     newInstrument(localSF2Soundbank, "Choir", new Patch(0, 85), new SF2Layer[] { localSF2Layer21 });
/*  402 */     newInstrument(localSF2Soundbank, "Organ", new Patch(0, 86), new SF2Layer[] { localSF2Layer23 });
/*  403 */     newInstrument(localSF2Soundbank, "Organ", new Patch(0, 87), new SF2Layer[] { localSF2Layer23 });
/*  404 */     newInstrument(localSF2Soundbank, "Synth Strings", new Patch(0, 88), new SF2Layer[] { localSF2Layer19 });
/*  405 */     newInstrument(localSF2Soundbank, "Organ", new Patch(0, 89), new SF2Layer[] { localSF2Layer23 });
/*  406 */     newInstrument(localSF2Soundbank, "Def", new Patch(0, 90), new SF2Layer[] { localSF2Layer38 });
/*  407 */     newInstrument(localSF2Soundbank, "Choir", new Patch(0, 91), new SF2Layer[] { localSF2Layer21 });
/*  408 */     newInstrument(localSF2Soundbank, "Organ", new Patch(0, 92), new SF2Layer[] { localSF2Layer23 });
/*  409 */     newInstrument(localSF2Soundbank, "Organ", new Patch(0, 93), new SF2Layer[] { localSF2Layer23 });
/*  410 */     newInstrument(localSF2Soundbank, "Organ", new Patch(0, 94), new SF2Layer[] { localSF2Layer23 });
/*  411 */     newInstrument(localSF2Soundbank, "Organ", new Patch(0, 95), new SF2Layer[] { localSF2Layer23 });
/*  412 */     newInstrument(localSF2Soundbank, "Organ", new Patch(0, 96), new SF2Layer[] { localSF2Layer23 });
/*  413 */     newInstrument(localSF2Soundbank, "Organ", new Patch(0, 97), new SF2Layer[] { localSF2Layer23 });
/*  414 */     newInstrument(localSF2Soundbank, "Bell", new Patch(0, 98), new SF2Layer[] { localSF2Layer25 });
/*  415 */     newInstrument(localSF2Soundbank, "Organ", new Patch(0, 99), new SF2Layer[] { localSF2Layer23 });
/*  416 */     newInstrument(localSF2Soundbank, "Organ", new Patch(0, 100), new SF2Layer[] { localSF2Layer23 });
/*  417 */     newInstrument(localSF2Soundbank, "Organ", new Patch(0, 101), new SF2Layer[] { localSF2Layer23 });
/*  418 */     newInstrument(localSF2Soundbank, "Def", new Patch(0, 102), new SF2Layer[] { localSF2Layer38 });
/*  419 */     newInstrument(localSF2Soundbank, "Synth Strings", new Patch(0, 103), new SF2Layer[] { localSF2Layer19 });
/*  420 */     newInstrument(localSF2Soundbank, "Def", new Patch(0, 104), new SF2Layer[] { localSF2Layer38 });
/*  421 */     newInstrument(localSF2Soundbank, "Def", new Patch(0, 105), new SF2Layer[] { localSF2Layer38 });
/*  422 */     newInstrument(localSF2Soundbank, "Def", new Patch(0, 106), new SF2Layer[] { localSF2Layer38 });
/*  423 */     newInstrument(localSF2Soundbank, "Def", new Patch(0, 107), new SF2Layer[] { localSF2Layer38 });
/*  424 */     newInstrument(localSF2Soundbank, "Marimba", new Patch(0, 108), new SF2Layer[] { localSF2Layer25 });
/*  425 */     newInstrument(localSF2Soundbank, "Sax", new Patch(0, 109), new SF2Layer[] { localSF2Layer33 });
/*  426 */     newInstrument(localSF2Soundbank, "Solo String", new Patch(0, 110), new SF2Layer[] { localSF2Layer19, localSF2Layer22 });
/*  427 */     newInstrument(localSF2Soundbank, "Oboe", new Patch(0, 111), new SF2Layer[] { localSF2Layer34 });
/*  428 */     newInstrument(localSF2Soundbank, "Bell", new Patch(0, 112), new SF2Layer[] { localSF2Layer25 });
/*  429 */     newInstrument(localSF2Soundbank, "Melodic Toms", new Patch(0, 113), new SF2Layer[] { localSF2Layer28 });
/*  430 */     newInstrument(localSF2Soundbank, "Marimba", new Patch(0, 114), new SF2Layer[] { localSF2Layer25 });
/*  431 */     newInstrument(localSF2Soundbank, "Melodic Toms", new Patch(0, 115), new SF2Layer[] { localSF2Layer28 });
/*  432 */     newInstrument(localSF2Soundbank, "Melodic Toms", new Patch(0, 116), new SF2Layer[] { localSF2Layer28 });
/*  433 */     newInstrument(localSF2Soundbank, "Melodic Toms", new Patch(0, 117), new SF2Layer[] { localSF2Layer28 });
/*  434 */     newInstrument(localSF2Soundbank, "Reverse Cymbal", new Patch(0, 118), new SF2Layer[] { localSF2Layer37 });
/*  435 */     newInstrument(localSF2Soundbank, "Reverse Cymbal", new Patch(0, 119), new SF2Layer[] { localSF2Layer37 });
/*  436 */     newInstrument(localSF2Soundbank, "Guitar", new Patch(0, 120), new SF2Layer[] { localSF2Layer13 });
/*  437 */     newInstrument(localSF2Soundbank, "Def", new Patch(0, 121), new SF2Layer[] { localSF2Layer38 });
/*      */ 
/*  439 */     localObject2 = newInstrument(localSF2Soundbank, "Seashore/Reverse Cymbal", new Patch(0, 122), new SF2Layer[] { localSF2Layer37 });
/*      */ 
/*  441 */     localSF2InstrumentRegion = (SF2InstrumentRegion)((SF2Instrument)localObject2).getRegions().get(0);
/*  442 */     localSF2InstrumentRegion.putInteger(37, 1000);
/*  443 */     localSF2InstrumentRegion.putInteger(36, 18500);
/*  444 */     localSF2InstrumentRegion.putInteger(38, 4500);
/*  445 */     localSF2InstrumentRegion.putInteger(8, -4500);
/*      */ 
/*  448 */     localObject2 = newInstrument(localSF2Soundbank, "Bird/Flute", new Patch(0, 123), new SF2Layer[] { localSF2Layer26 });
/*      */ 
/*  450 */     localSF2InstrumentRegion = (SF2InstrumentRegion)((SF2Instrument)localObject2).getRegions().get(0);
/*  451 */     localSF2InstrumentRegion.putInteger(51, 24);
/*  452 */     localSF2InstrumentRegion.putInteger(36, -3000);
/*  453 */     localSF2InstrumentRegion.putInteger(37, 1000);
/*      */ 
/*  455 */     newInstrument(localSF2Soundbank, "Def", new Patch(0, 124), new SF2Layer[] { localSF2Layer7 });
/*      */ 
/*  457 */     localObject2 = newInstrument(localSF2Soundbank, "Seashore/Reverse Cymbal", new Patch(0, 125), new SF2Layer[] { localSF2Layer37 });
/*      */ 
/*  459 */     localSF2InstrumentRegion = (SF2InstrumentRegion)((SF2Instrument)localObject2).getRegions().get(0);
/*  460 */     localSF2InstrumentRegion.putInteger(37, 1000);
/*  461 */     localSF2InstrumentRegion.putInteger(36, 18500);
/*  462 */     localSF2InstrumentRegion.putInteger(38, 4500);
/*  463 */     localSF2InstrumentRegion.putInteger(8, -4500);
/*      */ 
/*  465 */     newInstrument(localSF2Soundbank, "Applause/crash_cymbal", new Patch(0, 126), new SF2Layer[] { localSF2Layer6 });
/*      */ 
/*  467 */     newInstrument(localSF2Soundbank, "Gunshot/side_stick", new Patch(0, 127), new SF2Layer[] { localSF2Layer7 });
/*      */ 
/*  469 */     for (Object localObject3 : localSF2Soundbank.getInstruments()) {
/*  470 */       Patch localPatch = localObject3.getPatch();
/*  471 */       if ((!(localPatch instanceof ModelPatch)) || 
/*  472 */         (!((ModelPatch)localPatch).isPercussion()))
/*      */       {
/*  475 */         localObject3.setName(general_midi_instruments[localPatch.getProgram()]);
/*      */       }
/*      */     }
/*  478 */     return localSF2Soundbank;
/*      */   }
/*      */ 
/*      */   public static SF2Layer new_bell(SF2Soundbank paramSF2Soundbank)
/*      */   {
/*  483 */     Random localRandom = new Random(102030201L);
/*  484 */     int i = 8;
/*  485 */     int j = 4096 * i;
/*  486 */     double[] arrayOfDouble = new double[j * 2];
/*  487 */     double d1 = i * 25;
/*  488 */     double d2 = 0.01D;
/*  489 */     double d3 = 0.05D;
/*  490 */     double d4 = 0.2D;
/*  491 */     double d5 = 1.E-005D;
/*  492 */     double d6 = d4;
/*  493 */     double d7 = Math.pow(d5 / d4, 0.025D);
/*  494 */     for (int k = 0; k < 40; k++) {
/*  495 */       double d8 = 1.0D + (localRandom.nextDouble() * 2.0D - 1.0D) * 0.01D;
/*  496 */       double d9 = d2 + (d3 - d2) * (k / 40.0D);
/*  497 */       complexGaussianDist(arrayOfDouble, d1 * (k + 1) * d8, d9, d6);
/*  498 */       d6 *= d7;
/*      */     }
/*  500 */     SF2Sample localSF2Sample = newSimpleFFTSample(paramSF2Soundbank, "EPiano", arrayOfDouble, d1);
/*  501 */     SF2Layer localSF2Layer = newLayer(paramSF2Soundbank, "EPiano", localSF2Sample);
/*  502 */     SF2Region localSF2Region = (SF2Region)localSF2Layer.getRegions().get(0);
/*  503 */     localSF2Region.putInteger(54, 1);
/*  504 */     localSF2Region.putInteger(34, -12000);
/*  505 */     localSF2Region.putInteger(38, 0);
/*  506 */     localSF2Region.putInteger(36, 4000);
/*  507 */     localSF2Region.putInteger(37, 1000);
/*  508 */     localSF2Region.putInteger(26, 1200);
/*  509 */     localSF2Region.putInteger(30, 12000);
/*  510 */     localSF2Region.putInteger(11, -9000);
/*  511 */     localSF2Region.putInteger(8, 16000);
/*  512 */     return localSF2Layer;
/*      */   }
/*      */ 
/*      */   public static SF2Layer new_guitar1(SF2Soundbank paramSF2Soundbank)
/*      */   {
/*  517 */     int i = 8;
/*  518 */     int j = 4096 * i;
/*  519 */     double[] arrayOfDouble1 = new double[j * 2];
/*  520 */     double d1 = i * 25;
/*  521 */     double d2 = 0.01D;
/*  522 */     double d3 = 0.01D;
/*  523 */     double d4 = 2.0D;
/*  524 */     double d5 = 0.01D;
/*  525 */     double d6 = d4;
/*  526 */     double d7 = Math.pow(d5 / d4, 0.025D);
/*      */ 
/*  528 */     double[] arrayOfDouble2 = new double[40];
/*  529 */     for (int k = 0; k < 40; k++) {
/*  530 */       arrayOfDouble2[k] = d6;
/*  531 */       d6 *= d7;
/*      */     }
/*      */ 
/*  534 */     arrayOfDouble2[0] = 2.0D;
/*  535 */     arrayOfDouble2[1] = 0.5D;
/*  536 */     arrayOfDouble2[2] = 0.45D;
/*  537 */     arrayOfDouble2[3] = 0.2D;
/*  538 */     arrayOfDouble2[4] = 1.0D;
/*  539 */     arrayOfDouble2[5] = 0.5D;
/*  540 */     arrayOfDouble2[6] = 2.0D;
/*  541 */     arrayOfDouble2[7] = 1.0D;
/*  542 */     arrayOfDouble2[8] = 0.5D;
/*  543 */     arrayOfDouble2[9] = 1.0D;
/*  544 */     arrayOfDouble2[9] = 0.5D;
/*  545 */     arrayOfDouble2[10] = 0.2D;
/*  546 */     arrayOfDouble2[11] = 1.0D;
/*  547 */     arrayOfDouble2[12] = 0.7D;
/*  548 */     arrayOfDouble2[13] = 0.5D;
/*  549 */     arrayOfDouble2[14] = 1.0D;
/*      */ 
/*  551 */     for (k = 0; k < 40; k++) {
/*  552 */       double d8 = d2 + (d3 - d2) * (k / 40.0D);
/*  553 */       complexGaussianDist(arrayOfDouble1, d1 * (k + 1), d8, arrayOfDouble2[k]);
/*      */     }
/*      */ 
/*  556 */     SF2Sample localSF2Sample = newSimpleFFTSample(paramSF2Soundbank, "Guitar", arrayOfDouble1, d1);
/*  557 */     SF2Layer localSF2Layer = newLayer(paramSF2Soundbank, "Guitar", localSF2Sample);
/*  558 */     SF2Region localSF2Region = (SF2Region)localSF2Layer.getRegions().get(0);
/*  559 */     localSF2Region.putInteger(54, 1);
/*  560 */     localSF2Region.putInteger(34, -12000);
/*  561 */     localSF2Region.putInteger(38, 0);
/*  562 */     localSF2Region.putInteger(36, 2400);
/*  563 */     localSF2Region.putInteger(37, 1000);
/*      */ 
/*  565 */     localSF2Region.putInteger(26, -100);
/*  566 */     localSF2Region.putInteger(30, 12000);
/*  567 */     localSF2Region.putInteger(11, -6000);
/*  568 */     localSF2Region.putInteger(8, 16000);
/*  569 */     localSF2Region.putInteger(48, -20);
/*  570 */     return localSF2Layer;
/*      */   }
/*      */ 
/*      */   public static SF2Layer new_guitar_dist(SF2Soundbank paramSF2Soundbank)
/*      */   {
/*  575 */     int i = 8;
/*  576 */     int j = 4096 * i;
/*  577 */     double[] arrayOfDouble1 = new double[j * 2];
/*  578 */     double d1 = i * 25;
/*  579 */     double d2 = 0.01D;
/*  580 */     double d3 = 0.01D;
/*  581 */     double d4 = 2.0D;
/*  582 */     double d5 = 0.01D;
/*  583 */     double d6 = d4;
/*  584 */     double d7 = Math.pow(d5 / d4, 0.025D);
/*      */ 
/*  586 */     double[] arrayOfDouble2 = new double[40];
/*  587 */     for (int k = 0; k < 40; k++) {
/*  588 */       arrayOfDouble2[k] = d6;
/*  589 */       d6 *= d7;
/*      */     }
/*      */ 
/*  592 */     arrayOfDouble2[0] = 5.0D;
/*  593 */     arrayOfDouble2[1] = 2.0D;
/*  594 */     arrayOfDouble2[2] = 0.45D;
/*  595 */     arrayOfDouble2[3] = 0.2D;
/*  596 */     arrayOfDouble2[4] = 1.0D;
/*  597 */     arrayOfDouble2[5] = 0.5D;
/*  598 */     arrayOfDouble2[6] = 2.0D;
/*  599 */     arrayOfDouble2[7] = 1.0D;
/*  600 */     arrayOfDouble2[8] = 0.5D;
/*  601 */     arrayOfDouble2[9] = 1.0D;
/*  602 */     arrayOfDouble2[9] = 0.5D;
/*  603 */     arrayOfDouble2[10] = 0.2D;
/*  604 */     arrayOfDouble2[11] = 1.0D;
/*  605 */     arrayOfDouble2[12] = 0.7D;
/*  606 */     arrayOfDouble2[13] = 0.5D;
/*  607 */     arrayOfDouble2[14] = 1.0D;
/*      */ 
/*  609 */     for (k = 0; k < 40; k++) {
/*  610 */       double d8 = d2 + (d3 - d2) * (k / 40.0D);
/*  611 */       complexGaussianDist(arrayOfDouble1, d1 * (k + 1), d8, arrayOfDouble2[k]);
/*      */     }
/*      */ 
/*  615 */     SF2Sample localSF2Sample = newSimpleFFTSample_dist(paramSF2Soundbank, "Distorted Guitar", arrayOfDouble1, d1, 10000.0D);
/*      */ 
/*  619 */     SF2Layer localSF2Layer = newLayer(paramSF2Soundbank, "Distorted Guitar", localSF2Sample);
/*  620 */     SF2Region localSF2Region = (SF2Region)localSF2Layer.getRegions().get(0);
/*  621 */     localSF2Region.putInteger(54, 1);
/*  622 */     localSF2Region.putInteger(34, -12000);
/*  623 */     localSF2Region.putInteger(38, 0);
/*      */ 
/*  630 */     localSF2Region.putInteger(8, 8000);
/*      */ 
/*  632 */     return localSF2Layer;
/*      */   }
/*      */ 
/*      */   public static SF2Layer new_guitar_pick(SF2Soundbank paramSF2Soundbank)
/*      */   {
/*  641 */     int i = 2;
/*  642 */     int j = 4096 * i;
/*  643 */     Object localObject2 = new double[2 * j];
/*  644 */     Object localObject3 = new Random(3049912L);
/*  645 */     for (int k = 0; k < localObject2.length; k += 2)
/*  646 */       localObject2[k] = (2.0D * (((Random)localObject3).nextDouble() - 0.5D));
/*  647 */     fft((double[])localObject2);
/*      */ 
/*  649 */     for (k = j / 2; k < localObject2.length; k++)
/*  650 */       localObject2[k] = 0.0D;
/*  651 */     for (k = 0; k < 2048 * i; k++) {
/*  652 */       localObject2[k] *= (Math.exp(-Math.abs((k - 23) / i) * 1.2D) + Math.exp(-Math.abs((k - 40) / i) * 0.9D));
/*      */     }
/*      */ 
/*  655 */     randomPhase((double[])localObject2, new Random(3049912L));
/*  656 */     ifft((double[])localObject2);
/*  657 */     normalize((double[])localObject2, 0.8D);
/*  658 */     localObject2 = realPart((double[])localObject2);
/*  659 */     double d = 1.0D;
/*  660 */     for (int m = 0; m < localObject2.length; m++) {
/*  661 */       localObject2[m] *= d;
/*  662 */       d *= 0.9994D;
/*      */     }
/*  664 */     Object localObject1 = localObject2;
/*      */ 
/*  666 */     fadeUp((double[])localObject2, 80);
/*      */ 
/*  669 */     SF2Sample localSF2Sample = newSimpleDrumSample(paramSF2Soundbank, "Guitar Noise", localObject1);
/*      */ 
/*  671 */     SF2Layer localSF2Layer = new SF2Layer(paramSF2Soundbank);
/*  672 */     localSF2Layer.setName("Guitar Noise");
/*      */ 
/*  674 */     localObject2 = new SF2GlobalRegion();
/*  675 */     localSF2Layer.setGlobalZone((SF2GlobalRegion)localObject2);
/*  676 */     paramSF2Soundbank.addResource(localSF2Layer);
/*      */ 
/*  678 */     localObject3 = new SF2LayerRegion();
/*  679 */     ((SF2LayerRegion)localObject3).putInteger(38, 12000);
/*      */ 
/*  690 */     ((SF2LayerRegion)localObject3).setSample(localSF2Sample);
/*  691 */     localSF2Layer.getRegions().add(localObject3);
/*      */ 
/*  693 */     return localSF2Layer;
/*      */   }
/*      */ 
/*      */   public static SF2Layer new_gpiano(SF2Soundbank paramSF2Soundbank)
/*      */   {
/*  698 */     int i = 8;
/*  699 */     int j = 4096 * i;
/*  700 */     double[] arrayOfDouble1 = new double[j * 2];
/*  701 */     double d1 = i * 25;
/*  702 */     double d2 = 0.2D;
/*  703 */     double d3 = 0.001D;
/*  704 */     double d4 = d2;
/*  705 */     double d5 = Math.pow(d3 / d2, 0.06666666666666667D);
/*      */ 
/*  707 */     double[] arrayOfDouble2 = new double[30];
/*  708 */     for (int k = 0; k < 30; k++) {
/*  709 */       arrayOfDouble2[k] = d4;
/*  710 */       d4 *= d5;
/*      */     }
/*      */ 
/*  713 */     arrayOfDouble2[0] *= 2.0D;
/*      */ 
/*  715 */     arrayOfDouble2[4] *= 2.0D;
/*      */ 
/*  718 */     arrayOfDouble2[12] *= 0.9D;
/*  719 */     arrayOfDouble2[13] *= 0.7D;
/*  720 */     for (k = 14; k < 30; k++) {
/*  721 */       arrayOfDouble2[k] *= 0.5D;
/*      */     }
/*      */ 
/*  725 */     for (k = 0; k < 30; k++)
/*      */     {
/*  727 */       double d6 = 0.2D;
/*  728 */       double d7 = arrayOfDouble2[k];
/*  729 */       if (k > 10) {
/*  730 */         d6 = 5.0D;
/*  731 */         d7 *= 10.0D;
/*      */       }
/*  733 */       int m = 0;
/*  734 */       if (k > 5) {
/*  735 */         m = (k - 5) * 7;
/*      */       }
/*  737 */       complexGaussianDist(arrayOfDouble1, d1 * (k + 1) + m, d6, d7);
/*      */     }
/*      */ 
/*  740 */     SF2Sample localSF2Sample = newSimpleFFTSample(paramSF2Soundbank, "Grand Piano", arrayOfDouble1, d1, 200);
/*  741 */     SF2Layer localSF2Layer = newLayer(paramSF2Soundbank, "Grand Piano", localSF2Sample);
/*  742 */     SF2Region localSF2Region = (SF2Region)localSF2Layer.getRegions().get(0);
/*  743 */     localSF2Region.putInteger(54, 1);
/*  744 */     localSF2Region.putInteger(34, -7000);
/*  745 */     localSF2Region.putInteger(38, 0);
/*  746 */     localSF2Region.putInteger(36, 4000);
/*  747 */     localSF2Region.putInteger(37, 1000);
/*  748 */     localSF2Region.putInteger(26, -6000);
/*  749 */     localSF2Region.putInteger(30, 12000);
/*  750 */     localSF2Region.putInteger(11, -5500);
/*  751 */     localSF2Region.putInteger(8, 18000);
/*  752 */     return localSF2Layer;
/*      */   }
/*      */ 
/*      */   public static SF2Layer new_gpiano2(SF2Soundbank paramSF2Soundbank)
/*      */   {
/*  757 */     int i = 8;
/*  758 */     int j = 4096 * i;
/*  759 */     double[] arrayOfDouble1 = new double[j * 2];
/*  760 */     double d1 = i * 25;
/*  761 */     double d2 = 0.2D;
/*  762 */     double d3 = 0.001D;
/*  763 */     double d4 = d2;
/*  764 */     double d5 = Math.pow(d3 / d2, 0.05D);
/*      */ 
/*  766 */     double[] arrayOfDouble2 = new double[30];
/*  767 */     for (int k = 0; k < 30; k++) {
/*  768 */       arrayOfDouble2[k] = d4;
/*  769 */       d4 *= d5;
/*      */     }
/*      */ 
/*  772 */     arrayOfDouble2[0] *= 1.0D;
/*      */ 
/*  774 */     arrayOfDouble2[4] *= 2.0D;
/*      */ 
/*  777 */     arrayOfDouble2[12] *= 0.9D;
/*  778 */     arrayOfDouble2[13] *= 0.7D;
/*  779 */     for (k = 14; k < 30; k++) {
/*  780 */       arrayOfDouble2[k] *= 0.5D;
/*      */     }
/*      */ 
/*  784 */     for (k = 0; k < 30; k++)
/*      */     {
/*  786 */       double d6 = 0.2D;
/*  787 */       double d7 = arrayOfDouble2[k];
/*  788 */       if (k > 10) {
/*  789 */         d6 = 5.0D;
/*  790 */         d7 *= 10.0D;
/*      */       }
/*  792 */       int m = 0;
/*  793 */       if (k > 5) {
/*  794 */         m = (k - 5) * 7;
/*      */       }
/*  796 */       complexGaussianDist(arrayOfDouble1, d1 * (k + 1) + m, d6, d7);
/*      */     }
/*      */ 
/*  799 */     SF2Sample localSF2Sample = newSimpleFFTSample(paramSF2Soundbank, "Grand Piano", arrayOfDouble1, d1, 200);
/*  800 */     SF2Layer localSF2Layer = newLayer(paramSF2Soundbank, "Grand Piano", localSF2Sample);
/*  801 */     SF2Region localSF2Region = (SF2Region)localSF2Layer.getRegions().get(0);
/*  802 */     localSF2Region.putInteger(54, 1);
/*  803 */     localSF2Region.putInteger(34, -7000);
/*  804 */     localSF2Region.putInteger(38, 0);
/*  805 */     localSF2Region.putInteger(36, 4000);
/*  806 */     localSF2Region.putInteger(37, 1000);
/*  807 */     localSF2Region.putInteger(26, -6000);
/*  808 */     localSF2Region.putInteger(30, 12000);
/*  809 */     localSF2Region.putInteger(11, -5500);
/*  810 */     localSF2Region.putInteger(8, 18000);
/*  811 */     return localSF2Layer;
/*      */   }
/*      */ 
/*      */   public static SF2Layer new_piano_hammer(SF2Soundbank paramSF2Soundbank)
/*      */   {
/*  820 */     int i = 2;
/*  821 */     int j = 4096 * i;
/*  822 */     Object localObject2 = new double[2 * j];
/*  823 */     Object localObject3 = new Random(3049912L);
/*  824 */     for (int k = 0; k < localObject2.length; k += 2)
/*  825 */       localObject2[k] = (2.0D * (((Random)localObject3).nextDouble() - 0.5D));
/*  826 */     fft((double[])localObject2);
/*      */ 
/*  828 */     for (k = j / 2; k < localObject2.length; k++)
/*  829 */       localObject2[k] = 0.0D;
/*  830 */     for (k = 0; k < 2048 * i; k++)
/*  831 */       localObject2[k] *= Math.exp(-Math.abs((k - 37) / i) * 0.05D);
/*  832 */     randomPhase((double[])localObject2, new Random(3049912L));
/*  833 */     ifft((double[])localObject2);
/*  834 */     normalize((double[])localObject2, 0.6D);
/*  835 */     localObject2 = realPart((double[])localObject2);
/*  836 */     double d = 1.0D;
/*  837 */     for (int m = 0; m < localObject2.length; m++) {
/*  838 */       localObject2[m] *= d;
/*  839 */       d *= 0.9997D;
/*      */     }
/*  841 */     Object localObject1 = localObject2;
/*      */ 
/*  843 */     fadeUp((double[])localObject2, 80);
/*      */ 
/*  846 */     SF2Sample localSF2Sample = newSimpleDrumSample(paramSF2Soundbank, "Piano Hammer", localObject1);
/*      */ 
/*  848 */     SF2Layer localSF2Layer = new SF2Layer(paramSF2Soundbank);
/*  849 */     localSF2Layer.setName("Piano Hammer");
/*      */ 
/*  851 */     localObject2 = new SF2GlobalRegion();
/*  852 */     localSF2Layer.setGlobalZone((SF2GlobalRegion)localObject2);
/*  853 */     paramSF2Soundbank.addResource(localSF2Layer);
/*      */ 
/*  855 */     localObject3 = new SF2LayerRegion();
/*  856 */     ((SF2LayerRegion)localObject3).putInteger(38, 12000);
/*      */ 
/*  866 */     ((SF2LayerRegion)localObject3).setSample(localSF2Sample);
/*  867 */     localSF2Layer.getRegions().add(localObject3);
/*      */ 
/*  869 */     return localSF2Layer;
/*      */   }
/*      */ 
/*      */   public static SF2Layer new_piano1(SF2Soundbank paramSF2Soundbank)
/*      */   {
/*  874 */     int i = 8;
/*  875 */     int j = 4096 * i;
/*  876 */     double[] arrayOfDouble1 = new double[j * 2];
/*  877 */     double d1 = i * 25;
/*  878 */     double d2 = 0.2D;
/*  879 */     double d3 = 0.0001D;
/*  880 */     double d4 = d2;
/*  881 */     double d5 = Math.pow(d3 / d2, 0.025D);
/*      */ 
/*  883 */     double[] arrayOfDouble2 = new double[30];
/*  884 */     for (int k = 0; k < 30; k++) {
/*  885 */       arrayOfDouble2[k] = d4;
/*  886 */       d4 *= d5;
/*      */     }
/*      */ 
/*  889 */     arrayOfDouble2[0] *= 5.0D;
/*  890 */     arrayOfDouble2[2] *= 0.1D;
/*  891 */     arrayOfDouble2[7] *= 5.0D;
/*      */ 
/*  894 */     for (k = 0; k < 30; k++)
/*      */     {
/*  896 */       double d6 = 0.2D;
/*  897 */       double d7 = arrayOfDouble2[k];
/*  898 */       if (k > 12) {
/*  899 */         d6 = 5.0D;
/*  900 */         d7 *= 10.0D;
/*      */       }
/*  902 */       int m = 0;
/*  903 */       if (k > 5) {
/*  904 */         m = (k - 5) * 7;
/*      */       }
/*  906 */       complexGaussianDist(arrayOfDouble1, d1 * (k + 1) + m, d6, d7);
/*      */     }
/*      */ 
/*  909 */     complexGaussianDist(arrayOfDouble1, d1 * 15.5D, 1.0D, 0.1D);
/*  910 */     complexGaussianDist(arrayOfDouble1, d1 * 17.5D, 1.0D, 0.01D);
/*      */ 
/*  912 */     SF2Sample localSF2Sample = newSimpleFFTSample(paramSF2Soundbank, "EPiano", arrayOfDouble1, d1, 200);
/*  913 */     SF2Layer localSF2Layer = newLayer(paramSF2Soundbank, "EPiano", localSF2Sample);
/*  914 */     SF2Region localSF2Region = (SF2Region)localSF2Layer.getRegions().get(0);
/*  915 */     localSF2Region.putInteger(54, 1);
/*  916 */     localSF2Region.putInteger(34, -12000);
/*  917 */     localSF2Region.putInteger(38, 0);
/*  918 */     localSF2Region.putInteger(36, 4000);
/*  919 */     localSF2Region.putInteger(37, 1000);
/*  920 */     localSF2Region.putInteger(26, -1200);
/*  921 */     localSF2Region.putInteger(30, 12000);
/*  922 */     localSF2Region.putInteger(11, -5500);
/*  923 */     localSF2Region.putInteger(8, 16000);
/*  924 */     return localSF2Layer;
/*      */   }
/*      */ 
/*      */   public static SF2Layer new_epiano1(SF2Soundbank paramSF2Soundbank) {
/*  928 */     Random localRandom = new Random(302030201L);
/*  929 */     int i = 8;
/*  930 */     int j = 4096 * i;
/*  931 */     double[] arrayOfDouble = new double[j * 2];
/*  932 */     double d1 = i * 25;
/*  933 */     double d2 = 0.05D;
/*  934 */     double d3 = 0.05D;
/*  935 */     double d4 = 0.2D;
/*  936 */     double d5 = 0.0001D;
/*  937 */     double d6 = d4;
/*  938 */     double d7 = Math.pow(d5 / d4, 0.025D);
/*  939 */     for (int k = 0; k < 40; k++) {
/*  940 */       double d8 = 1.0D + (localRandom.nextDouble() * 2.0D - 1.0D) * 0.0001D;
/*  941 */       double d9 = d2 + (d3 - d2) * (k / 40.0D);
/*  942 */       complexGaussianDist(arrayOfDouble, d1 * (k + 1) * d8, d9, d6);
/*  943 */       d6 *= d7;
/*      */     }
/*      */ 
/*  948 */     SF2Sample localSF2Sample = newSimpleFFTSample(paramSF2Soundbank, "EPiano", arrayOfDouble, d1);
/*  949 */     SF2Layer localSF2Layer = newLayer(paramSF2Soundbank, "EPiano", localSF2Sample);
/*  950 */     SF2Region localSF2Region = (SF2Region)localSF2Layer.getRegions().get(0);
/*  951 */     localSF2Region.putInteger(54, 1);
/*  952 */     localSF2Region.putInteger(34, -12000);
/*  953 */     localSF2Region.putInteger(38, 0);
/*  954 */     localSF2Region.putInteger(36, 4000);
/*  955 */     localSF2Region.putInteger(37, 1000);
/*  956 */     localSF2Region.putInteger(26, 1200);
/*  957 */     localSF2Region.putInteger(30, 12000);
/*  958 */     localSF2Region.putInteger(11, -9000);
/*  959 */     localSF2Region.putInteger(8, 16000);
/*  960 */     return localSF2Layer;
/*      */   }
/*      */ 
/*      */   public static SF2Layer new_epiano2(SF2Soundbank paramSF2Soundbank) {
/*  964 */     Random localRandom = new Random(302030201L);
/*  965 */     int i = 8;
/*  966 */     int j = 4096 * i;
/*  967 */     double[] arrayOfDouble = new double[j * 2];
/*  968 */     double d1 = i * 25;
/*  969 */     double d2 = 0.01D;
/*  970 */     double d3 = 0.05D;
/*  971 */     double d4 = 0.2D;
/*  972 */     double d5 = 1.E-005D;
/*  973 */     double d6 = d4;
/*  974 */     double d7 = Math.pow(d5 / d4, 0.025D);
/*  975 */     for (int k = 0; k < 40; k++) {
/*  976 */       double d8 = 1.0D + (localRandom.nextDouble() * 2.0D - 1.0D) * 0.0001D;
/*  977 */       double d9 = d2 + (d3 - d2) * (k / 40.0D);
/*  978 */       complexGaussianDist(arrayOfDouble, d1 * (k + 1) * d8, d9, d6);
/*  979 */       d6 *= d7;
/*      */     }
/*      */ 
/*  982 */     SF2Sample localSF2Sample = newSimpleFFTSample(paramSF2Soundbank, "EPiano", arrayOfDouble, d1);
/*  983 */     SF2Layer localSF2Layer = newLayer(paramSF2Soundbank, "EPiano", localSF2Sample);
/*  984 */     SF2Region localSF2Region = (SF2Region)localSF2Layer.getRegions().get(0);
/*  985 */     localSF2Region.putInteger(54, 1);
/*  986 */     localSF2Region.putInteger(34, -12000);
/*  987 */     localSF2Region.putInteger(38, 0);
/*  988 */     localSF2Region.putInteger(36, 8000);
/*  989 */     localSF2Region.putInteger(37, 1000);
/*  990 */     localSF2Region.putInteger(26, 2400);
/*  991 */     localSF2Region.putInteger(30, 12000);
/*  992 */     localSF2Region.putInteger(11, -9000);
/*  993 */     localSF2Region.putInteger(8, 16000);
/*  994 */     localSF2Region.putInteger(48, -100);
/*  995 */     return localSF2Layer;
/*      */   }
/*      */ 
/*      */   public static SF2Layer new_bass1(SF2Soundbank paramSF2Soundbank) {
/*  999 */     int i = 8;
/* 1000 */     int j = 4096 * i;
/* 1001 */     double[] arrayOfDouble1 = new double[j * 2];
/* 1002 */     double d1 = i * 25;
/* 1003 */     double d2 = 0.05D;
/* 1004 */     double d3 = 0.05D;
/* 1005 */     double d4 = 0.2D;
/* 1006 */     double d5 = 0.02D;
/* 1007 */     double d6 = d4;
/* 1008 */     double d7 = Math.pow(d5 / d4, 0.04D);
/*      */ 
/* 1010 */     double[] arrayOfDouble2 = new double[25];
/* 1011 */     for (int k = 0; k < 25; k++) {
/* 1012 */       arrayOfDouble2[k] = d6;
/* 1013 */       d6 *= d7;
/*      */     }
/*      */ 
/* 1016 */     arrayOfDouble2[0] *= 8.0D;
/* 1017 */     arrayOfDouble2[1] *= 4.0D;
/* 1018 */     arrayOfDouble2[3] *= 8.0D;
/* 1019 */     arrayOfDouble2[5] *= 8.0D;
/*      */ 
/* 1021 */     for (k = 0; k < 25; k++) {
/* 1022 */       double d8 = d2 + (d3 - d2) * (k / 40.0D);
/* 1023 */       complexGaussianDist(arrayOfDouble1, d1 * (k + 1), d8, arrayOfDouble2[k]);
/*      */     }
/*      */ 
/* 1027 */     SF2Sample localSF2Sample = newSimpleFFTSample(paramSF2Soundbank, "Bass", arrayOfDouble1, d1);
/* 1028 */     SF2Layer localSF2Layer = newLayer(paramSF2Soundbank, "Bass", localSF2Sample);
/* 1029 */     SF2Region localSF2Region = (SF2Region)localSF2Layer.getRegions().get(0);
/* 1030 */     localSF2Region.putInteger(54, 1);
/* 1031 */     localSF2Region.putInteger(34, -12000);
/* 1032 */     localSF2Region.putInteger(38, 0);
/* 1033 */     localSF2Region.putInteger(36, 4000);
/* 1034 */     localSF2Region.putInteger(37, 1000);
/* 1035 */     localSF2Region.putInteger(26, -3000);
/* 1036 */     localSF2Region.putInteger(30, 12000);
/* 1037 */     localSF2Region.putInteger(11, -5000);
/* 1038 */     localSF2Region.putInteger(8, 11000);
/* 1039 */     localSF2Region.putInteger(48, -100);
/* 1040 */     return localSF2Layer;
/*      */   }
/*      */ 
/*      */   public static SF2Layer new_synthbass(SF2Soundbank paramSF2Soundbank) {
/* 1044 */     int i = 8;
/* 1045 */     int j = 4096 * i;
/* 1046 */     double[] arrayOfDouble1 = new double[j * 2];
/* 1047 */     double d1 = i * 25;
/* 1048 */     double d2 = 0.05D;
/* 1049 */     double d3 = 0.05D;
/* 1050 */     double d4 = 0.2D;
/* 1051 */     double d5 = 0.02D;
/* 1052 */     double d6 = d4;
/* 1053 */     double d7 = Math.pow(d5 / d4, 0.04D);
/*      */ 
/* 1055 */     double[] arrayOfDouble2 = new double[25];
/* 1056 */     for (int k = 0; k < 25; k++) {
/* 1057 */       arrayOfDouble2[k] = d6;
/* 1058 */       d6 *= d7;
/*      */     }
/*      */ 
/* 1061 */     arrayOfDouble2[0] *= 16.0D;
/* 1062 */     arrayOfDouble2[1] *= 4.0D;
/* 1063 */     arrayOfDouble2[3] *= 16.0D;
/* 1064 */     arrayOfDouble2[5] *= 8.0D;
/*      */ 
/* 1066 */     for (k = 0; k < 25; k++) {
/* 1067 */       double d8 = d2 + (d3 - d2) * (k / 40.0D);
/* 1068 */       complexGaussianDist(arrayOfDouble1, d1 * (k + 1), d8, arrayOfDouble2[k]);
/*      */     }
/*      */ 
/* 1072 */     SF2Sample localSF2Sample = newSimpleFFTSample(paramSF2Soundbank, "Bass", arrayOfDouble1, d1);
/* 1073 */     SF2Layer localSF2Layer = newLayer(paramSF2Soundbank, "Bass", localSF2Sample);
/* 1074 */     SF2Region localSF2Region = (SF2Region)localSF2Layer.getRegions().get(0);
/* 1075 */     localSF2Region.putInteger(54, 1);
/* 1076 */     localSF2Region.putInteger(34, -12000);
/* 1077 */     localSF2Region.putInteger(38, 0);
/* 1078 */     localSF2Region.putInteger(36, 4000);
/* 1079 */     localSF2Region.putInteger(37, 1000);
/* 1080 */     localSF2Region.putInteger(26, -3000);
/* 1081 */     localSF2Region.putInteger(30, 12000);
/* 1082 */     localSF2Region.putInteger(11, -3000);
/* 1083 */     localSF2Region.putInteger(9, 100);
/* 1084 */     localSF2Region.putInteger(8, 8000);
/* 1085 */     localSF2Region.putInteger(48, -100);
/* 1086 */     return localSF2Layer;
/*      */   }
/*      */ 
/*      */   public static SF2Layer new_bass2(SF2Soundbank paramSF2Soundbank) {
/* 1090 */     int i = 8;
/* 1091 */     int j = 4096 * i;
/* 1092 */     double[] arrayOfDouble1 = new double[j * 2];
/* 1093 */     double d1 = i * 25;
/* 1094 */     double d2 = 0.05D;
/* 1095 */     double d3 = 0.05D;
/* 1096 */     double d4 = 0.2D;
/* 1097 */     double d5 = 0.002D;
/* 1098 */     double d6 = d4;
/* 1099 */     double d7 = Math.pow(d5 / d4, 0.04D);
/*      */ 
/* 1101 */     double[] arrayOfDouble2 = new double[25];
/* 1102 */     for (int k = 0; k < 25; k++) {
/* 1103 */       arrayOfDouble2[k] = d6;
/* 1104 */       d6 *= d7;
/*      */     }
/*      */ 
/* 1107 */     arrayOfDouble2[0] *= 8.0D;
/* 1108 */     arrayOfDouble2[1] *= 4.0D;
/* 1109 */     arrayOfDouble2[3] *= 8.0D;
/* 1110 */     arrayOfDouble2[5] *= 8.0D;
/*      */ 
/* 1112 */     for (k = 0; k < 25; k++) {
/* 1113 */       double d8 = d2 + (d3 - d2) * (k / 40.0D);
/* 1114 */       complexGaussianDist(arrayOfDouble1, d1 * (k + 1), d8, arrayOfDouble2[k]);
/*      */     }
/*      */ 
/* 1118 */     SF2Sample localSF2Sample = newSimpleFFTSample(paramSF2Soundbank, "Bass2", arrayOfDouble1, d1);
/* 1119 */     SF2Layer localSF2Layer = newLayer(paramSF2Soundbank, "Bass2", localSF2Sample);
/* 1120 */     SF2Region localSF2Region = (SF2Region)localSF2Layer.getRegions().get(0);
/* 1121 */     localSF2Region.putInteger(54, 1);
/* 1122 */     localSF2Region.putInteger(34, -8000);
/* 1123 */     localSF2Region.putInteger(38, 0);
/* 1124 */     localSF2Region.putInteger(36, 4000);
/* 1125 */     localSF2Region.putInteger(37, 1000);
/* 1126 */     localSF2Region.putInteger(26, -6000);
/* 1127 */     localSF2Region.putInteger(30, 12000);
/* 1128 */     localSF2Region.putInteger(8, 5000);
/* 1129 */     localSF2Region.putInteger(48, -100);
/* 1130 */     return localSF2Layer;
/*      */   }
/*      */ 
/*      */   public static SF2Layer new_solostring(SF2Soundbank paramSF2Soundbank) {
/* 1134 */     int i = 8;
/* 1135 */     int j = 4096 * i;
/* 1136 */     double[] arrayOfDouble1 = new double[j * 2];
/* 1137 */     double d1 = i * 25;
/* 1138 */     double d2 = 2.0D;
/* 1139 */     double d3 = 2.0D;
/* 1140 */     double d4 = 0.2D;
/* 1141 */     double d5 = 0.01D;
/*      */ 
/* 1143 */     double[] arrayOfDouble2 = new double[18];
/* 1144 */     double d6 = d4;
/* 1145 */     double d7 = Math.pow(d5 / d4, 0.025D);
/* 1146 */     for (int k = 0; k < arrayOfDouble2.length; k++) {
/* 1147 */       d6 *= d7;
/* 1148 */       arrayOfDouble2[k] = d6;
/*      */     }
/*      */ 
/* 1151 */     arrayOfDouble2[0] *= 5.0D;
/* 1152 */     arrayOfDouble2[1] *= 5.0D;
/* 1153 */     arrayOfDouble2[2] *= 5.0D;
/* 1154 */     arrayOfDouble2[3] *= 4.0D;
/* 1155 */     arrayOfDouble2[4] *= 4.0D;
/* 1156 */     arrayOfDouble2[5] *= 3.0D;
/* 1157 */     arrayOfDouble2[6] *= 3.0D;
/* 1158 */     arrayOfDouble2[7] *= 2.0D;
/*      */ 
/* 1160 */     for (k = 0; k < arrayOfDouble2.length; k++) {
/* 1161 */       double d8 = d2 + (d3 - d2) * (k / 40.0D);
/* 1162 */       complexGaussianDist(arrayOfDouble1, d1 * (k + 1), d8, d6);
/*      */     }
/* 1164 */     SF2Sample localSF2Sample = newSimpleFFTSample(paramSF2Soundbank, "Strings", arrayOfDouble1, d1);
/* 1165 */     SF2Layer localSF2Layer = newLayer(paramSF2Soundbank, "Strings", localSF2Sample);
/* 1166 */     SF2Region localSF2Region = (SF2Region)localSF2Layer.getRegions().get(0);
/* 1167 */     localSF2Region.putInteger(54, 1);
/* 1168 */     localSF2Region.putInteger(34, -5000);
/* 1169 */     localSF2Region.putInteger(38, 1000);
/* 1170 */     localSF2Region.putInteger(36, 4000);
/* 1171 */     localSF2Region.putInteger(37, -100);
/* 1172 */     localSF2Region.putInteger(8, 9500);
/* 1173 */     localSF2Region.putInteger(24, -1000);
/* 1174 */     localSF2Region.putInteger(6, 15);
/* 1175 */     return localSF2Layer;
/*      */   }
/*      */ 
/*      */   public static SF2Layer new_orchhit(SF2Soundbank paramSF2Soundbank)
/*      */   {
/* 1180 */     int i = 8;
/* 1181 */     int j = 4096 * i;
/* 1182 */     double[] arrayOfDouble = new double[j * 2];
/* 1183 */     double d1 = i * 25;
/* 1184 */     double d2 = 2.0D;
/* 1185 */     double d3 = 80.0D;
/* 1186 */     double d4 = 0.2D;
/* 1187 */     double d5 = 0.001D;
/* 1188 */     double d6 = d4;
/* 1189 */     double d7 = Math.pow(d5 / d4, 0.025D);
/* 1190 */     for (int k = 0; k < 40; k++) {
/* 1191 */       double d8 = d2 + (d3 - d2) * (k / 40.0D);
/* 1192 */       complexGaussianDist(arrayOfDouble, d1 * (k + 1), d8, d6);
/* 1193 */       d6 *= d7;
/*      */     }
/* 1195 */     complexGaussianDist(arrayOfDouble, d1 * 4.0D, 300.0D, 1.0D);
/*      */ 
/* 1198 */     SF2Sample localSF2Sample = newSimpleFFTSample(paramSF2Soundbank, "Och Strings", arrayOfDouble, d1);
/* 1199 */     SF2Layer localSF2Layer = newLayer(paramSF2Soundbank, "Och Strings", localSF2Sample);
/* 1200 */     SF2Region localSF2Region = (SF2Region)localSF2Layer.getRegions().get(0);
/* 1201 */     localSF2Region.putInteger(54, 1);
/* 1202 */     localSF2Region.putInteger(34, -5000);
/* 1203 */     localSF2Region.putInteger(38, 200);
/* 1204 */     localSF2Region.putInteger(36, 200);
/* 1205 */     localSF2Region.putInteger(37, 1000);
/* 1206 */     localSF2Region.putInteger(8, 9500);
/* 1207 */     return localSF2Layer;
/*      */   }
/*      */ 
/*      */   public static SF2Layer new_string2(SF2Soundbank paramSF2Soundbank)
/*      */   {
/* 1212 */     int i = 8;
/* 1213 */     int j = 4096 * i;
/* 1214 */     double[] arrayOfDouble = new double[j * 2];
/* 1215 */     double d1 = i * 25;
/* 1216 */     double d2 = 2.0D;
/* 1217 */     double d3 = 80.0D;
/* 1218 */     double d4 = 0.2D;
/* 1219 */     double d5 = 0.001D;
/* 1220 */     double d6 = d4;
/* 1221 */     double d7 = Math.pow(d5 / d4, 0.025D);
/* 1222 */     for (int k = 0; k < 40; k++) {
/* 1223 */       double d8 = d2 + (d3 - d2) * (k / 40.0D);
/* 1224 */       complexGaussianDist(arrayOfDouble, d1 * (k + 1), d8, d6);
/* 1225 */       d6 *= d7;
/*      */     }
/* 1227 */     SF2Sample localSF2Sample = newSimpleFFTSample(paramSF2Soundbank, "Strings", arrayOfDouble, d1);
/* 1228 */     SF2Layer localSF2Layer = newLayer(paramSF2Soundbank, "Strings", localSF2Sample);
/* 1229 */     SF2Region localSF2Region = (SF2Region)localSF2Layer.getRegions().get(0);
/* 1230 */     localSF2Region.putInteger(54, 1);
/* 1231 */     localSF2Region.putInteger(34, -5000);
/* 1232 */     localSF2Region.putInteger(38, 1000);
/* 1233 */     localSF2Region.putInteger(36, 4000);
/* 1234 */     localSF2Region.putInteger(37, -100);
/* 1235 */     localSF2Region.putInteger(8, 9500);
/* 1236 */     return localSF2Layer;
/*      */   }
/*      */ 
/*      */   public static SF2Layer new_choir(SF2Soundbank paramSF2Soundbank)
/*      */   {
/* 1241 */     int i = 8;
/* 1242 */     int j = 4096 * i;
/* 1243 */     double[] arrayOfDouble1 = new double[j * 2];
/* 1244 */     double d1 = i * 25;
/* 1245 */     double d2 = 2.0D;
/* 1246 */     double d3 = 80.0D;
/* 1247 */     double d4 = 0.2D;
/* 1248 */     double d5 = 0.001D;
/* 1249 */     double d6 = d4;
/* 1250 */     double d7 = Math.pow(d5 / d4, 0.025D);
/* 1251 */     double[] arrayOfDouble2 = new double[40];
/* 1252 */     for (int k = 0; k < arrayOfDouble2.length; k++) {
/* 1253 */       d6 *= d7;
/* 1254 */       arrayOfDouble2[k] = d6;
/*      */     }
/*      */ 
/* 1257 */     arrayOfDouble2[5] *= 0.1D;
/* 1258 */     arrayOfDouble2[6] *= 0.01D;
/* 1259 */     arrayOfDouble2[7] *= 0.1D;
/* 1260 */     arrayOfDouble2[8] *= 0.1D;
/*      */ 
/* 1262 */     for (k = 0; k < arrayOfDouble2.length; k++) {
/* 1263 */       double d8 = d2 + (d3 - d2) * (k / 40.0D);
/* 1264 */       complexGaussianDist(arrayOfDouble1, d1 * (k + 1), d8, arrayOfDouble2[k]);
/*      */     }
/* 1266 */     SF2Sample localSF2Sample = newSimpleFFTSample(paramSF2Soundbank, "Strings", arrayOfDouble1, d1);
/* 1267 */     SF2Layer localSF2Layer = newLayer(paramSF2Soundbank, "Strings", localSF2Sample);
/* 1268 */     SF2Region localSF2Region = (SF2Region)localSF2Layer.getRegions().get(0);
/* 1269 */     localSF2Region.putInteger(54, 1);
/* 1270 */     localSF2Region.putInteger(34, -5000);
/* 1271 */     localSF2Region.putInteger(38, 1000);
/* 1272 */     localSF2Region.putInteger(36, 4000);
/* 1273 */     localSF2Region.putInteger(37, -100);
/* 1274 */     localSF2Region.putInteger(8, 9500);
/* 1275 */     return localSF2Layer;
/*      */   }
/*      */ 
/*      */   public static SF2Layer new_organ(SF2Soundbank paramSF2Soundbank)
/*      */   {
/* 1280 */     Random localRandom = new Random(102030201L);
/* 1281 */     int i = 1;
/* 1282 */     int j = 4096 * i;
/* 1283 */     double[] arrayOfDouble = new double[j * 2];
/* 1284 */     double d1 = i * 15;
/* 1285 */     double d2 = 0.01D;
/* 1286 */     double d3 = 0.01D;
/* 1287 */     double d4 = 0.2D;
/* 1288 */     double d5 = 0.001D;
/* 1289 */     double d6 = d4;
/* 1290 */     double d7 = Math.pow(d5 / d4, 0.025D);
/*      */ 
/* 1292 */     for (int k = 0; k < 12; k++) {
/* 1293 */       double d8 = d2 + (d3 - d2) * (k / 40.0D);
/* 1294 */       complexGaussianDist(arrayOfDouble, d1 * (k + 1), d8, d6 * (0.5D + 3.0D * localRandom.nextDouble()));
/*      */ 
/* 1296 */       d6 *= d7;
/*      */     }
/* 1298 */     SF2Sample localSF2Sample = newSimpleFFTSample(paramSF2Soundbank, "Organ", arrayOfDouble, d1);
/* 1299 */     SF2Layer localSF2Layer = newLayer(paramSF2Soundbank, "Organ", localSF2Sample);
/* 1300 */     SF2Region localSF2Region = (SF2Region)localSF2Layer.getRegions().get(0);
/* 1301 */     localSF2Region.putInteger(54, 1);
/* 1302 */     localSF2Region.putInteger(34, -6000);
/* 1303 */     localSF2Region.putInteger(38, -1000);
/* 1304 */     localSF2Region.putInteger(36, 4000);
/* 1305 */     localSF2Region.putInteger(37, -100);
/* 1306 */     localSF2Region.putInteger(8, 9500);
/* 1307 */     return localSF2Layer;
/*      */   }
/*      */ 
/*      */   public static SF2Layer new_ch_organ(SF2Soundbank paramSF2Soundbank)
/*      */   {
/* 1312 */     int i = 1;
/* 1313 */     int j = 4096 * i;
/* 1314 */     double[] arrayOfDouble1 = new double[j * 2];
/* 1315 */     double d1 = i * 15;
/* 1316 */     double d2 = 0.01D;
/* 1317 */     double d3 = 0.01D;
/* 1318 */     double d4 = 0.2D;
/* 1319 */     double d5 = 0.001D;
/* 1320 */     double d6 = d4;
/* 1321 */     double d7 = Math.pow(d5 / d4, 0.01666666666666667D);
/*      */ 
/* 1323 */     double[] arrayOfDouble2 = new double[60];
/* 1324 */     for (int k = 0; k < arrayOfDouble2.length; k++) {
/* 1325 */       d6 *= d7;
/* 1326 */       arrayOfDouble2[k] = d6;
/*      */     }
/*      */ 
/* 1329 */     arrayOfDouble2[0] *= 5.0D;
/* 1330 */     arrayOfDouble2[1] *= 2.0D;
/* 1331 */     arrayOfDouble2[2] = 0.0D;
/* 1332 */     arrayOfDouble2[4] = 0.0D;
/* 1333 */     arrayOfDouble2[5] = 0.0D;
/* 1334 */     arrayOfDouble2[7] *= 7.0D;
/* 1335 */     arrayOfDouble2[9] = 0.0D;
/* 1336 */     arrayOfDouble2[10] = 0.0D;
/* 1337 */     arrayOfDouble2[12] = 0.0D;
/* 1338 */     arrayOfDouble2[15] *= 7.0D;
/* 1339 */     arrayOfDouble2[18] = 0.0D;
/* 1340 */     arrayOfDouble2[20] = 0.0D;
/* 1341 */     arrayOfDouble2[24] = 0.0D;
/* 1342 */     arrayOfDouble2[27] *= 5.0D;
/* 1343 */     arrayOfDouble2[29] = 0.0D;
/* 1344 */     arrayOfDouble2[30] = 0.0D;
/* 1345 */     arrayOfDouble2[33] = 0.0D;
/* 1346 */     arrayOfDouble2[36] *= 4.0D;
/* 1347 */     arrayOfDouble2[37] = 0.0D;
/* 1348 */     arrayOfDouble2[39] = 0.0D;
/* 1349 */     arrayOfDouble2[42] = 0.0D;
/* 1350 */     arrayOfDouble2[43] = 0.0D;
/* 1351 */     arrayOfDouble2[47] = 0.0D;
/* 1352 */     arrayOfDouble2[50] *= 4.0D;
/* 1353 */     arrayOfDouble2[52] = 0.0D;
/* 1354 */     arrayOfDouble2[55] = 0.0D;
/* 1355 */     arrayOfDouble2[57] = 0.0D;
/*      */ 
/* 1358 */     arrayOfDouble2[10] *= 0.1D;
/* 1359 */     arrayOfDouble2[11] *= 0.1D;
/* 1360 */     arrayOfDouble2[12] *= 0.1D;
/* 1361 */     arrayOfDouble2[13] *= 0.1D;
/*      */ 
/* 1363 */     arrayOfDouble2[17] *= 0.1D;
/* 1364 */     arrayOfDouble2[18] *= 0.1D;
/* 1365 */     arrayOfDouble2[19] *= 0.1D;
/* 1366 */     arrayOfDouble2[20] *= 0.1D;
/*      */ 
/* 1368 */     for (k = 0; k < 60; k++) {
/* 1369 */       double d8 = d2 + (d3 - d2) * (k / 40.0D);
/* 1370 */       complexGaussianDist(arrayOfDouble1, d1 * (k + 1), d8, arrayOfDouble2[k]);
/* 1371 */       d6 *= d7;
/*      */     }
/* 1373 */     SF2Sample localSF2Sample = newSimpleFFTSample(paramSF2Soundbank, "Organ", arrayOfDouble1, d1);
/* 1374 */     SF2Layer localSF2Layer = newLayer(paramSF2Soundbank, "Organ", localSF2Sample);
/* 1375 */     SF2Region localSF2Region = (SF2Region)localSF2Layer.getRegions().get(0);
/* 1376 */     localSF2Region.putInteger(54, 1);
/* 1377 */     localSF2Region.putInteger(34, -10000);
/* 1378 */     localSF2Region.putInteger(38, -1000);
/* 1379 */     return localSF2Layer;
/*      */   }
/*      */ 
/*      */   public static SF2Layer new_flute(SF2Soundbank paramSF2Soundbank)
/*      */   {
/* 1384 */     int i = 8;
/* 1385 */     int j = 4096 * i;
/* 1386 */     double[] arrayOfDouble = new double[j * 2];
/* 1387 */     double d = i * 15;
/*      */ 
/* 1389 */     complexGaussianDist(arrayOfDouble, d * 1.0D, 0.001D, 0.5D);
/* 1390 */     complexGaussianDist(arrayOfDouble, d * 2.0D, 0.001D, 0.5D);
/* 1391 */     complexGaussianDist(arrayOfDouble, d * 3.0D, 0.001D, 0.5D);
/* 1392 */     complexGaussianDist(arrayOfDouble, d * 4.0D, 0.01D, 0.5D);
/*      */ 
/* 1394 */     complexGaussianDist(arrayOfDouble, d * 4.0D, 100.0D, 120.0D);
/* 1395 */     complexGaussianDist(arrayOfDouble, d * 6.0D, 100.0D, 40.0D);
/* 1396 */     complexGaussianDist(arrayOfDouble, d * 8.0D, 100.0D, 80.0D);
/*      */ 
/* 1398 */     complexGaussianDist(arrayOfDouble, d * 5.0D, 0.001D, 0.05D);
/* 1399 */     complexGaussianDist(arrayOfDouble, d * 6.0D, 0.001D, 0.06D);
/* 1400 */     complexGaussianDist(arrayOfDouble, d * 7.0D, 0.001D, 0.04D);
/* 1401 */     complexGaussianDist(arrayOfDouble, d * 8.0D, 0.005D, 0.06D);
/* 1402 */     complexGaussianDist(arrayOfDouble, d * 9.0D, 0.005D, 0.06D);
/* 1403 */     complexGaussianDist(arrayOfDouble, d * 10.0D, 0.01D, 0.1D);
/* 1404 */     complexGaussianDist(arrayOfDouble, d * 11.0D, 0.08D, 0.7D);
/* 1405 */     complexGaussianDist(arrayOfDouble, d * 12.0D, 0.08D, 0.6D);
/* 1406 */     complexGaussianDist(arrayOfDouble, d * 13.0D, 0.08D, 0.6D);
/* 1407 */     complexGaussianDist(arrayOfDouble, d * 14.0D, 0.08D, 0.6D);
/* 1408 */     complexGaussianDist(arrayOfDouble, d * 15.0D, 0.08D, 0.5D);
/* 1409 */     complexGaussianDist(arrayOfDouble, d * 16.0D, 0.08D, 0.5D);
/* 1410 */     complexGaussianDist(arrayOfDouble, d * 17.0D, 0.08D, 0.2D);
/*      */ 
/* 1413 */     complexGaussianDist(arrayOfDouble, d * 1.0D, 10.0D, 8.0D);
/* 1414 */     complexGaussianDist(arrayOfDouble, d * 2.0D, 10.0D, 8.0D);
/* 1415 */     complexGaussianDist(arrayOfDouble, d * 3.0D, 10.0D, 8.0D);
/* 1416 */     complexGaussianDist(arrayOfDouble, d * 4.0D, 10.0D, 8.0D);
/* 1417 */     complexGaussianDist(arrayOfDouble, d * 5.0D, 10.0D, 8.0D);
/* 1418 */     complexGaussianDist(arrayOfDouble, d * 6.0D, 20.0D, 9.0D);
/* 1419 */     complexGaussianDist(arrayOfDouble, d * 7.0D, 20.0D, 9.0D);
/* 1420 */     complexGaussianDist(arrayOfDouble, d * 8.0D, 20.0D, 9.0D);
/* 1421 */     complexGaussianDist(arrayOfDouble, d * 9.0D, 20.0D, 8.0D);
/* 1422 */     complexGaussianDist(arrayOfDouble, d * 10.0D, 30.0D, 8.0D);
/* 1423 */     complexGaussianDist(arrayOfDouble, d * 11.0D, 30.0D, 9.0D);
/* 1424 */     complexGaussianDist(arrayOfDouble, d * 12.0D, 30.0D, 9.0D);
/* 1425 */     complexGaussianDist(arrayOfDouble, d * 13.0D, 30.0D, 8.0D);
/* 1426 */     complexGaussianDist(arrayOfDouble, d * 14.0D, 30.0D, 8.0D);
/* 1427 */     complexGaussianDist(arrayOfDouble, d * 15.0D, 30.0D, 7.0D);
/* 1428 */     complexGaussianDist(arrayOfDouble, d * 16.0D, 30.0D, 7.0D);
/* 1429 */     complexGaussianDist(arrayOfDouble, d * 17.0D, 30.0D, 6.0D);
/*      */ 
/* 1431 */     SF2Sample localSF2Sample = newSimpleFFTSample(paramSF2Soundbank, "Flute", arrayOfDouble, d);
/* 1432 */     SF2Layer localSF2Layer = newLayer(paramSF2Soundbank, "Flute", localSF2Sample);
/* 1433 */     SF2Region localSF2Region = (SF2Region)localSF2Layer.getRegions().get(0);
/* 1434 */     localSF2Region.putInteger(54, 1);
/* 1435 */     localSF2Region.putInteger(34, -6000);
/* 1436 */     localSF2Region.putInteger(38, -1000);
/* 1437 */     localSF2Region.putInteger(36, 4000);
/* 1438 */     localSF2Region.putInteger(37, -100);
/* 1439 */     localSF2Region.putInteger(8, 9500);
/* 1440 */     return localSF2Layer;
/*      */   }
/*      */ 
/*      */   public static SF2Layer new_horn(SF2Soundbank paramSF2Soundbank)
/*      */   {
/* 1445 */     int i = 8;
/* 1446 */     int j = 4096 * i;
/* 1447 */     double[] arrayOfDouble = new double[j * 2];
/* 1448 */     double d1 = i * 15;
/*      */ 
/* 1450 */     double d2 = 0.5D;
/* 1451 */     double d3 = 9.999999999999999E-012D;
/* 1452 */     double d4 = d2;
/* 1453 */     double d5 = Math.pow(d3 / d2, 0.025D);
/* 1454 */     for (int k = 0; k < 40; k++) {
/* 1455 */       if (k == 0)
/* 1456 */         complexGaussianDist(arrayOfDouble, d1 * (k + 1), 0.1D, d4 * 0.2D);
/*      */       else
/* 1458 */         complexGaussianDist(arrayOfDouble, d1 * (k + 1), 0.1D, d4);
/* 1459 */       d4 *= d5;
/*      */     }
/*      */ 
/* 1462 */     complexGaussianDist(arrayOfDouble, d1 * 2.0D, 100.0D, 1.0D);
/*      */ 
/* 1465 */     SF2Sample localSF2Sample = newSimpleFFTSample(paramSF2Soundbank, "Horn", arrayOfDouble, d1);
/* 1466 */     SF2Layer localSF2Layer = newLayer(paramSF2Soundbank, "Horn", localSF2Sample);
/* 1467 */     SF2Region localSF2Region = (SF2Region)localSF2Layer.getRegions().get(0);
/* 1468 */     localSF2Region.putInteger(54, 1);
/* 1469 */     localSF2Region.putInteger(34, -6000);
/* 1470 */     localSF2Region.putInteger(38, -1000);
/* 1471 */     localSF2Region.putInteger(36, 4000);
/* 1472 */     localSF2Region.putInteger(37, -100);
/*      */ 
/* 1474 */     localSF2Region.putInteger(26, -500);
/* 1475 */     localSF2Region.putInteger(30, 12000);
/* 1476 */     localSF2Region.putInteger(11, 5000);
/* 1477 */     localSF2Region.putInteger(8, 4500);
/* 1478 */     return localSF2Layer;
/*      */   }
/*      */ 
/*      */   public static SF2Layer new_trumpet(SF2Soundbank paramSF2Soundbank)
/*      */   {
/* 1483 */     int i = 8;
/* 1484 */     int j = 4096 * i;
/* 1485 */     double[] arrayOfDouble1 = new double[j * 2];
/* 1486 */     double d1 = i * 15;
/*      */ 
/* 1488 */     double d2 = 0.5D;
/* 1489 */     double d3 = 1.E-005D;
/* 1490 */     double d4 = d2;
/* 1491 */     double d5 = Math.pow(d3 / d2, 0.0125D);
/* 1492 */     double[] arrayOfDouble2 = new double[80];
/* 1493 */     for (int k = 0; k < 80; k++) {
/* 1494 */       arrayOfDouble2[k] = d4;
/* 1495 */       d4 *= d5;
/*      */     }
/*      */ 
/* 1498 */     arrayOfDouble2[0] *= 0.05D;
/* 1499 */     arrayOfDouble2[1] *= 0.2D;
/* 1500 */     arrayOfDouble2[2] *= 0.5D;
/* 1501 */     arrayOfDouble2[3] *= 0.85D;
/*      */ 
/* 1503 */     for (k = 0; k < 80; k++) {
/* 1504 */       complexGaussianDist(arrayOfDouble1, d1 * (k + 1), 0.1D, arrayOfDouble2[k]);
/*      */     }
/*      */ 
/* 1507 */     complexGaussianDist(arrayOfDouble1, d1 * 5.0D, 300.0D, 3.0D);
/*      */ 
/* 1510 */     SF2Sample localSF2Sample = newSimpleFFTSample(paramSF2Soundbank, "Trumpet", arrayOfDouble1, d1);
/* 1511 */     SF2Layer localSF2Layer = newLayer(paramSF2Soundbank, "Trumpet", localSF2Sample);
/* 1512 */     SF2Region localSF2Region = (SF2Region)localSF2Layer.getRegions().get(0);
/* 1513 */     localSF2Region.putInteger(54, 1);
/* 1514 */     localSF2Region.putInteger(34, -10000);
/* 1515 */     localSF2Region.putInteger(38, 0);
/* 1516 */     localSF2Region.putInteger(36, 4000);
/* 1517 */     localSF2Region.putInteger(37, -100);
/*      */ 
/* 1519 */     localSF2Region.putInteger(26, -4000);
/* 1520 */     localSF2Region.putInteger(30, -2500);
/* 1521 */     localSF2Region.putInteger(11, 5000);
/* 1522 */     localSF2Region.putInteger(8, 4500);
/* 1523 */     localSF2Region.putInteger(9, 10);
/* 1524 */     return localSF2Layer;
/*      */   }
/*      */ 
/*      */   public static SF2Layer new_brass_section(SF2Soundbank paramSF2Soundbank)
/*      */   {
/* 1529 */     int i = 8;
/* 1530 */     int j = 4096 * i;
/* 1531 */     double[] arrayOfDouble1 = new double[j * 2];
/* 1532 */     double d1 = i * 15;
/*      */ 
/* 1534 */     double d2 = 0.5D;
/* 1535 */     double d3 = 0.005D;
/* 1536 */     double d4 = d2;
/* 1537 */     double d5 = Math.pow(d3 / d2, 0.03333333333333333D);
/* 1538 */     double[] arrayOfDouble2 = new double[30];
/* 1539 */     for (int k = 0; k < 30; k++) {
/* 1540 */       arrayOfDouble2[k] = d4;
/* 1541 */       d4 *= d5;
/*      */     }
/*      */ 
/* 1544 */     arrayOfDouble2[0] *= 0.8D;
/* 1545 */     arrayOfDouble2[1] *= 0.9D;
/*      */ 
/* 1547 */     double d6 = 5.0D;
/* 1548 */     for (int m = 0; m < 30; m++) {
/* 1549 */       complexGaussianDist(arrayOfDouble1, d1 * (m + 1), 0.1D * d6, arrayOfDouble2[m] * d6);
/* 1550 */       d6 += 6.0D;
/*      */     }
/*      */ 
/* 1553 */     complexGaussianDist(arrayOfDouble1, d1 * 6.0D, 300.0D, 2.0D);
/*      */ 
/* 1556 */     SF2Sample localSF2Sample = newSimpleFFTSample(paramSF2Soundbank, "Brass Section", arrayOfDouble1, d1);
/* 1557 */     SF2Layer localSF2Layer = newLayer(paramSF2Soundbank, "Brass Section", localSF2Sample);
/* 1558 */     SF2Region localSF2Region = (SF2Region)localSF2Layer.getRegions().get(0);
/* 1559 */     localSF2Region.putInteger(54, 1);
/* 1560 */     localSF2Region.putInteger(34, -9200);
/* 1561 */     localSF2Region.putInteger(38, -1000);
/* 1562 */     localSF2Region.putInteger(36, 4000);
/* 1563 */     localSF2Region.putInteger(37, -100);
/*      */ 
/* 1565 */     localSF2Region.putInteger(26, -3000);
/* 1566 */     localSF2Region.putInteger(30, 12000);
/* 1567 */     localSF2Region.putInteger(11, 5000);
/* 1568 */     localSF2Region.putInteger(8, 4500);
/* 1569 */     return localSF2Layer;
/*      */   }
/*      */ 
/*      */   public static SF2Layer new_trombone(SF2Soundbank paramSF2Soundbank)
/*      */   {
/* 1574 */     int i = 8;
/* 1575 */     int j = 4096 * i;
/* 1576 */     double[] arrayOfDouble1 = new double[j * 2];
/* 1577 */     double d1 = i * 15;
/*      */ 
/* 1579 */     double d2 = 0.5D;
/* 1580 */     double d3 = 0.001D;
/* 1581 */     double d4 = d2;
/* 1582 */     double d5 = Math.pow(d3 / d2, 0.0125D);
/* 1583 */     double[] arrayOfDouble2 = new double[80];
/* 1584 */     for (int k = 0; k < 80; k++) {
/* 1585 */       arrayOfDouble2[k] = d4;
/* 1586 */       d4 *= d5;
/*      */     }
/*      */ 
/* 1589 */     arrayOfDouble2[0] *= 0.3D;
/* 1590 */     arrayOfDouble2[1] *= 0.7D;
/*      */ 
/* 1592 */     for (k = 0; k < 80; k++) {
/* 1593 */       complexGaussianDist(arrayOfDouble1, d1 * (k + 1), 0.1D, arrayOfDouble2[k]);
/*      */     }
/*      */ 
/* 1596 */     complexGaussianDist(arrayOfDouble1, d1 * 6.0D, 300.0D, 2.0D);
/*      */ 
/* 1599 */     SF2Sample localSF2Sample = newSimpleFFTSample(paramSF2Soundbank, "Trombone", arrayOfDouble1, d1);
/* 1600 */     SF2Layer localSF2Layer = newLayer(paramSF2Soundbank, "Trombone", localSF2Sample);
/* 1601 */     SF2Region localSF2Region = (SF2Region)localSF2Layer.getRegions().get(0);
/* 1602 */     localSF2Region.putInteger(54, 1);
/* 1603 */     localSF2Region.putInteger(34, -8000);
/* 1604 */     localSF2Region.putInteger(38, -1000);
/* 1605 */     localSF2Region.putInteger(36, 4000);
/* 1606 */     localSF2Region.putInteger(37, -100);
/*      */ 
/* 1608 */     localSF2Region.putInteger(26, -2000);
/* 1609 */     localSF2Region.putInteger(30, 12000);
/* 1610 */     localSF2Region.putInteger(11, 5000);
/* 1611 */     localSF2Region.putInteger(8, 4500);
/* 1612 */     localSF2Region.putInteger(9, 10);
/* 1613 */     return localSF2Layer;
/*      */   }
/*      */ 
/*      */   public static SF2Layer new_sax(SF2Soundbank paramSF2Soundbank)
/*      */   {
/* 1618 */     int i = 8;
/* 1619 */     int j = 4096 * i;
/* 1620 */     double[] arrayOfDouble = new double[j * 2];
/* 1621 */     double d1 = i * 15;
/*      */ 
/* 1623 */     double d2 = 0.5D;
/* 1624 */     double d3 = 0.01D;
/* 1625 */     double d4 = d2;
/* 1626 */     double d5 = Math.pow(d3 / d2, 0.025D);
/* 1627 */     for (int k = 0; k < 40; k++) {
/* 1628 */       if ((k == 0) || (k == 2))
/* 1629 */         complexGaussianDist(arrayOfDouble, d1 * (k + 1), 0.1D, d4 * 4.0D);
/*      */       else
/* 1631 */         complexGaussianDist(arrayOfDouble, d1 * (k + 1), 0.1D, d4);
/* 1632 */       d4 *= d5;
/*      */     }
/*      */ 
/* 1635 */     complexGaussianDist(arrayOfDouble, d1 * 4.0D, 200.0D, 1.0D);
/*      */ 
/* 1637 */     SF2Sample localSF2Sample = newSimpleFFTSample(paramSF2Soundbank, "Sax", arrayOfDouble, d1);
/* 1638 */     SF2Layer localSF2Layer = newLayer(paramSF2Soundbank, "Sax", localSF2Sample);
/* 1639 */     SF2Region localSF2Region = (SF2Region)localSF2Layer.getRegions().get(0);
/* 1640 */     localSF2Region.putInteger(54, 1);
/* 1641 */     localSF2Region.putInteger(34, -6000);
/* 1642 */     localSF2Region.putInteger(38, -1000);
/* 1643 */     localSF2Region.putInteger(36, 4000);
/* 1644 */     localSF2Region.putInteger(37, -100);
/*      */ 
/* 1646 */     localSF2Region.putInteger(26, -3000);
/* 1647 */     localSF2Region.putInteger(30, 12000);
/* 1648 */     localSF2Region.putInteger(11, 5000);
/* 1649 */     localSF2Region.putInteger(8, 4500);
/* 1650 */     return localSF2Layer;
/*      */   }
/*      */ 
/*      */   public static SF2Layer new_oboe(SF2Soundbank paramSF2Soundbank)
/*      */   {
/* 1655 */     int i = 8;
/* 1656 */     int j = 4096 * i;
/* 1657 */     double[] arrayOfDouble = new double[j * 2];
/* 1658 */     double d = i * 15;
/*      */ 
/* 1660 */     complexGaussianDist(arrayOfDouble, d * 5.0D, 100.0D, 80.0D);
/*      */ 
/* 1663 */     complexGaussianDist(arrayOfDouble, d * 1.0D, 0.01D, 0.53D);
/* 1664 */     complexGaussianDist(arrayOfDouble, d * 2.0D, 0.01D, 0.51D);
/* 1665 */     complexGaussianDist(arrayOfDouble, d * 3.0D, 0.01D, 0.48D);
/* 1666 */     complexGaussianDist(arrayOfDouble, d * 4.0D, 0.01D, 0.49D);
/* 1667 */     complexGaussianDist(arrayOfDouble, d * 5.0D, 0.01D, 5.0D);
/* 1668 */     complexGaussianDist(arrayOfDouble, d * 6.0D, 0.01D, 0.51D);
/* 1669 */     complexGaussianDist(arrayOfDouble, d * 7.0D, 0.01D, 0.5D);
/* 1670 */     complexGaussianDist(arrayOfDouble, d * 8.0D, 0.01D, 0.59D);
/* 1671 */     complexGaussianDist(arrayOfDouble, d * 9.0D, 0.01D, 0.61D);
/* 1672 */     complexGaussianDist(arrayOfDouble, d * 10.0D, 0.01D, 0.52D);
/* 1673 */     complexGaussianDist(arrayOfDouble, d * 11.0D, 0.01D, 0.49D);
/* 1674 */     complexGaussianDist(arrayOfDouble, d * 12.0D, 0.01D, 0.51D);
/* 1675 */     complexGaussianDist(arrayOfDouble, d * 13.0D, 0.01D, 0.48D);
/* 1676 */     complexGaussianDist(arrayOfDouble, d * 14.0D, 0.01D, 0.51D);
/* 1677 */     complexGaussianDist(arrayOfDouble, d * 15.0D, 0.01D, 0.46D);
/* 1678 */     complexGaussianDist(arrayOfDouble, d * 16.0D, 0.01D, 0.35D);
/* 1679 */     complexGaussianDist(arrayOfDouble, d * 17.0D, 0.01D, 0.2D);
/* 1680 */     complexGaussianDist(arrayOfDouble, d * 18.0D, 0.01D, 0.1D);
/* 1681 */     complexGaussianDist(arrayOfDouble, d * 19.0D, 0.01D, 0.5D);
/* 1682 */     complexGaussianDist(arrayOfDouble, d * 20.0D, 0.01D, 0.1D);
/*      */ 
/* 1685 */     SF2Sample localSF2Sample = newSimpleFFTSample(paramSF2Soundbank, "Oboe", arrayOfDouble, d);
/* 1686 */     SF2Layer localSF2Layer = newLayer(paramSF2Soundbank, "Oboe", localSF2Sample);
/* 1687 */     SF2Region localSF2Region = (SF2Region)localSF2Layer.getRegions().get(0);
/* 1688 */     localSF2Region.putInteger(54, 1);
/* 1689 */     localSF2Region.putInteger(34, -6000);
/* 1690 */     localSF2Region.putInteger(38, -1000);
/* 1691 */     localSF2Region.putInteger(36, 4000);
/* 1692 */     localSF2Region.putInteger(37, -100);
/* 1693 */     localSF2Region.putInteger(8, 9500);
/* 1694 */     return localSF2Layer;
/*      */   }
/*      */ 
/*      */   public static SF2Layer new_bassoon(SF2Soundbank paramSF2Soundbank)
/*      */   {
/* 1699 */     int i = 8;
/* 1700 */     int j = 4096 * i;
/* 1701 */     double[] arrayOfDouble = new double[j * 2];
/* 1702 */     double d = i * 15;
/*      */ 
/* 1704 */     complexGaussianDist(arrayOfDouble, d * 2.0D, 100.0D, 40.0D);
/* 1705 */     complexGaussianDist(arrayOfDouble, d * 4.0D, 100.0D, 20.0D);
/*      */ 
/* 1707 */     complexGaussianDist(arrayOfDouble, d * 1.0D, 0.01D, 0.53D);
/* 1708 */     complexGaussianDist(arrayOfDouble, d * 2.0D, 0.01D, 5.0D);
/* 1709 */     complexGaussianDist(arrayOfDouble, d * 3.0D, 0.01D, 0.51D);
/* 1710 */     complexGaussianDist(arrayOfDouble, d * 4.0D, 0.01D, 0.48D);
/* 1711 */     complexGaussianDist(arrayOfDouble, d * 5.0D, 0.01D, 1.49D);
/* 1712 */     complexGaussianDist(arrayOfDouble, d * 6.0D, 0.01D, 0.51D);
/* 1713 */     complexGaussianDist(arrayOfDouble, d * 7.0D, 0.01D, 0.5D);
/* 1714 */     complexGaussianDist(arrayOfDouble, d * 8.0D, 0.01D, 0.59D);
/* 1715 */     complexGaussianDist(arrayOfDouble, d * 9.0D, 0.01D, 0.61D);
/* 1716 */     complexGaussianDist(arrayOfDouble, d * 10.0D, 0.01D, 0.52D);
/* 1717 */     complexGaussianDist(arrayOfDouble, d * 11.0D, 0.01D, 0.49D);
/* 1718 */     complexGaussianDist(arrayOfDouble, d * 12.0D, 0.01D, 0.51D);
/* 1719 */     complexGaussianDist(arrayOfDouble, d * 13.0D, 0.01D, 0.48D);
/* 1720 */     complexGaussianDist(arrayOfDouble, d * 14.0D, 0.01D, 0.51D);
/* 1721 */     complexGaussianDist(arrayOfDouble, d * 15.0D, 0.01D, 0.46D);
/* 1722 */     complexGaussianDist(arrayOfDouble, d * 16.0D, 0.01D, 0.35D);
/* 1723 */     complexGaussianDist(arrayOfDouble, d * 17.0D, 0.01D, 0.2D);
/* 1724 */     complexGaussianDist(arrayOfDouble, d * 18.0D, 0.01D, 0.1D);
/* 1725 */     complexGaussianDist(arrayOfDouble, d * 19.0D, 0.01D, 0.5D);
/* 1726 */     complexGaussianDist(arrayOfDouble, d * 20.0D, 0.01D, 0.1D);
/*      */ 
/* 1729 */     SF2Sample localSF2Sample = newSimpleFFTSample(paramSF2Soundbank, "Flute", arrayOfDouble, d);
/* 1730 */     SF2Layer localSF2Layer = newLayer(paramSF2Soundbank, "Flute", localSF2Sample);
/* 1731 */     SF2Region localSF2Region = (SF2Region)localSF2Layer.getRegions().get(0);
/* 1732 */     localSF2Region.putInteger(54, 1);
/* 1733 */     localSF2Region.putInteger(34, -6000);
/* 1734 */     localSF2Region.putInteger(38, -1000);
/* 1735 */     localSF2Region.putInteger(36, 4000);
/* 1736 */     localSF2Region.putInteger(37, -100);
/* 1737 */     localSF2Region.putInteger(8, 9500);
/* 1738 */     return localSF2Layer;
/*      */   }
/*      */ 
/*      */   public static SF2Layer new_clarinet(SF2Soundbank paramSF2Soundbank)
/*      */   {
/* 1743 */     int i = 8;
/* 1744 */     int j = 4096 * i;
/* 1745 */     double[] arrayOfDouble = new double[j * 2];
/* 1746 */     double d = i * 15;
/*      */ 
/* 1748 */     complexGaussianDist(arrayOfDouble, d * 1.0D, 0.001D, 0.5D);
/* 1749 */     complexGaussianDist(arrayOfDouble, d * 2.0D, 0.001D, 0.02D);
/* 1750 */     complexGaussianDist(arrayOfDouble, d * 3.0D, 0.001D, 0.2D);
/* 1751 */     complexGaussianDist(arrayOfDouble, d * 4.0D, 0.01D, 0.1D);
/*      */ 
/* 1753 */     complexGaussianDist(arrayOfDouble, d * 4.0D, 100.0D, 60.0D);
/* 1754 */     complexGaussianDist(arrayOfDouble, d * 6.0D, 100.0D, 20.0D);
/* 1755 */     complexGaussianDist(arrayOfDouble, d * 8.0D, 100.0D, 20.0D);
/*      */ 
/* 1757 */     complexGaussianDist(arrayOfDouble, d * 5.0D, 0.001D, 0.1D);
/* 1758 */     complexGaussianDist(arrayOfDouble, d * 6.0D, 0.001D, 0.09D);
/* 1759 */     complexGaussianDist(arrayOfDouble, d * 7.0D, 0.001D, 0.02D);
/* 1760 */     complexGaussianDist(arrayOfDouble, d * 8.0D, 0.005D, 0.16D);
/* 1761 */     complexGaussianDist(arrayOfDouble, d * 9.0D, 0.005D, 0.96D);
/* 1762 */     complexGaussianDist(arrayOfDouble, d * 10.0D, 0.01D, 0.9D);
/* 1763 */     complexGaussianDist(arrayOfDouble, d * 11.0D, 0.08D, 1.2D);
/* 1764 */     complexGaussianDist(arrayOfDouble, d * 12.0D, 0.08D, 1.8D);
/* 1765 */     complexGaussianDist(arrayOfDouble, d * 13.0D, 0.08D, 1.6D);
/* 1766 */     complexGaussianDist(arrayOfDouble, d * 14.0D, 0.08D, 1.2D);
/* 1767 */     complexGaussianDist(arrayOfDouble, d * 15.0D, 0.08D, 0.9D);
/* 1768 */     complexGaussianDist(arrayOfDouble, d * 16.0D, 0.08D, 0.5D);
/* 1769 */     complexGaussianDist(arrayOfDouble, d * 17.0D, 0.08D, 0.2D);
/*      */ 
/* 1772 */     complexGaussianDist(arrayOfDouble, d * 1.0D, 10.0D, 8.0D);
/* 1773 */     complexGaussianDist(arrayOfDouble, d * 2.0D, 10.0D, 8.0D);
/* 1774 */     complexGaussianDist(arrayOfDouble, d * 3.0D, 10.0D, 8.0D);
/* 1775 */     complexGaussianDist(arrayOfDouble, d * 4.0D, 10.0D, 8.0D);
/* 1776 */     complexGaussianDist(arrayOfDouble, d * 5.0D, 10.0D, 8.0D);
/* 1777 */     complexGaussianDist(arrayOfDouble, d * 6.0D, 20.0D, 9.0D);
/* 1778 */     complexGaussianDist(arrayOfDouble, d * 7.0D, 20.0D, 9.0D);
/* 1779 */     complexGaussianDist(arrayOfDouble, d * 8.0D, 20.0D, 9.0D);
/* 1780 */     complexGaussianDist(arrayOfDouble, d * 9.0D, 20.0D, 8.0D);
/* 1781 */     complexGaussianDist(arrayOfDouble, d * 10.0D, 30.0D, 8.0D);
/* 1782 */     complexGaussianDist(arrayOfDouble, d * 11.0D, 30.0D, 9.0D);
/* 1783 */     complexGaussianDist(arrayOfDouble, d * 12.0D, 30.0D, 9.0D);
/* 1784 */     complexGaussianDist(arrayOfDouble, d * 13.0D, 30.0D, 8.0D);
/* 1785 */     complexGaussianDist(arrayOfDouble, d * 14.0D, 30.0D, 8.0D);
/* 1786 */     complexGaussianDist(arrayOfDouble, d * 15.0D, 30.0D, 7.0D);
/* 1787 */     complexGaussianDist(arrayOfDouble, d * 16.0D, 30.0D, 7.0D);
/* 1788 */     complexGaussianDist(arrayOfDouble, d * 17.0D, 30.0D, 6.0D);
/*      */ 
/* 1790 */     SF2Sample localSF2Sample = newSimpleFFTSample(paramSF2Soundbank, "Clarinet", arrayOfDouble, d);
/* 1791 */     SF2Layer localSF2Layer = newLayer(paramSF2Soundbank, "Clarinet", localSF2Sample);
/* 1792 */     SF2Region localSF2Region = (SF2Region)localSF2Layer.getRegions().get(0);
/* 1793 */     localSF2Region.putInteger(54, 1);
/* 1794 */     localSF2Region.putInteger(34, -6000);
/* 1795 */     localSF2Region.putInteger(38, -1000);
/* 1796 */     localSF2Region.putInteger(36, 4000);
/* 1797 */     localSF2Region.putInteger(37, -100);
/* 1798 */     localSF2Region.putInteger(8, 9500);
/* 1799 */     return localSF2Layer;
/*      */   }
/*      */ 
/*      */   public static SF2Layer new_timpani(SF2Soundbank paramSF2Soundbank)
/*      */   {
/* 1810 */     int i = 32768;
/* 1811 */     Object localObject3 = new double[2 * i];
/* 1812 */     double d1 = 48.0D;
/* 1813 */     complexGaussianDist((double[])localObject3, d1 * 2.0D, 0.2D, 1.0D);
/* 1814 */     complexGaussianDist((double[])localObject3, d1 * 3.0D, 0.2D, 0.7D);
/* 1815 */     complexGaussianDist((double[])localObject3, d1 * 5.0D, 10.0D, 1.0D);
/* 1816 */     complexGaussianDist((double[])localObject3, d1 * 6.0D, 9.0D, 1.0D);
/* 1817 */     complexGaussianDist((double[])localObject3, d1 * 8.0D, 15.0D, 1.0D);
/* 1818 */     complexGaussianDist((double[])localObject3, d1 * 9.0D, 18.0D, 0.8D);
/* 1819 */     complexGaussianDist((double[])localObject3, d1 * 11.0D, 21.0D, 0.5D);
/* 1820 */     complexGaussianDist((double[])localObject3, d1 * 13.0D, 28.0D, 0.3D);
/* 1821 */     complexGaussianDist((double[])localObject3, d1 * 14.0D, 22.0D, 0.1D);
/* 1822 */     randomPhase((double[])localObject3, new Random(3049912L));
/* 1823 */     ifft((double[])localObject3);
/* 1824 */     normalize((double[])localObject3, 0.5D);
/* 1825 */     localObject3 = realPart((double[])localObject3);
/*      */ 
/* 1827 */     double d3 = localObject3.length;
/* 1828 */     for (int m = 0; m < localObject3.length; m++) {
/* 1829 */       double d4 = 1.0D - m / d3;
/* 1830 */       localObject3[m] *= d4 * d4;
/*      */     }
/* 1832 */     fadeUp((double[])localObject3, 40);
/* 1833 */     Object localObject1 = localObject3;
/*      */ 
/* 1838 */     i = 16384;
/* 1839 */     localObject3 = new double[2 * i];
/* 1840 */     Object localObject4 = new Random(3049912L);
/* 1841 */     for (int j = 0; j < localObject3.length; j += 2) {
/* 1842 */       localObject3[j] = (2.0D * (((Random)localObject4).nextDouble() - 0.5D) * 0.1D);
/*      */     }
/* 1844 */     fft((double[])localObject3);
/*      */ 
/* 1846 */     for (j = i / 2; j < localObject3.length; j++)
/* 1847 */       localObject3[j] = 0.0D;
/* 1848 */     for (j = 4096; j < 8192; j++)
/* 1849 */       localObject3[j] = (1.0D - (j - 4096) / 4096.0D);
/* 1850 */     for (j = 0; j < 300; j++) {
/* 1851 */       d3 = 1.0D - j / 300.0D;
/* 1852 */       localObject3[j] *= (1.0D + 20.0D * d3 * d3);
/*      */     }
/* 1854 */     for (j = 0; j < 24; j++)
/* 1855 */       localObject3[j] = 0.0D;
/* 1856 */     randomPhase((double[])localObject3, new Random(3049912L));
/* 1857 */     ifft((double[])localObject3);
/* 1858 */     normalize((double[])localObject3, 0.9D);
/* 1859 */     localObject3 = realPart((double[])localObject3);
/* 1860 */     double d2 = 1.0D;
/* 1861 */     for (int k = 0; k < localObject3.length; k++) {
/* 1862 */       localObject3[k] *= d2;
/* 1863 */       d2 *= 0.9998D;
/*      */     }
/* 1865 */     Object localObject2 = localObject3;
/*      */ 
/* 1868 */     for (i = 0; i < localObject2.length; i++) {
/* 1869 */       localObject1[i] += localObject2[i] * 0.02D;
/*      */     }
/* 1871 */     normalize(localObject1, 0.9D);
/*      */ 
/* 1873 */     SF2Sample localSF2Sample = newSimpleDrumSample(paramSF2Soundbank, "Timpani", localObject1);
/*      */ 
/* 1875 */     localObject3 = new SF2Layer(paramSF2Soundbank);
/* 1876 */     ((SF2Layer)localObject3).setName("Timpani");
/*      */ 
/* 1878 */     localObject4 = new SF2GlobalRegion();
/* 1879 */     ((SF2Layer)localObject3).setGlobalZone((SF2GlobalRegion)localObject4);
/* 1880 */     paramSF2Soundbank.addResource((SoundbankResource)localObject3);
/*      */ 
/* 1882 */     SF2LayerRegion localSF2LayerRegion = new SF2LayerRegion();
/* 1883 */     localSF2LayerRegion.putInteger(38, 12000);
/* 1884 */     localSF2LayerRegion.putInteger(48, -100);
/* 1885 */     localSF2LayerRegion.setSample(localSF2Sample);
/* 1886 */     ((SF2Layer)localObject3).getRegions().add(localSF2LayerRegion);
/*      */ 
/* 1888 */     return localObject3;
/*      */   }
/*      */ 
/*      */   public static SF2Layer new_melodic_toms(SF2Soundbank paramSF2Soundbank)
/*      */   {
/* 1898 */     int i = 16384;
/* 1899 */     Object localObject3 = new double[2 * i];
/* 1900 */     complexGaussianDist((double[])localObject3, 30.0D, 0.5D, 1.0D);
/* 1901 */     randomPhase((double[])localObject3, new Random(3049912L));
/* 1902 */     ifft((double[])localObject3);
/* 1903 */     normalize((double[])localObject3, 0.8D);
/* 1904 */     localObject3 = realPart((double[])localObject3);
/*      */ 
/* 1906 */     double d1 = localObject3.length;
/* 1907 */     for (int k = 0; k < localObject3.length; k++)
/* 1908 */       localObject3[k] *= (1.0D - k / d1);
/* 1909 */     Object localObject1 = localObject3;
/*      */ 
/* 1914 */     i = 16384;
/* 1915 */     localObject3 = new double[2 * i];
/* 1916 */     Object localObject4 = new Random(3049912L);
/* 1917 */     for (int j = 0; j < localObject3.length; j += 2)
/* 1918 */       localObject3[j] = (2.0D * (((Random)localObject4).nextDouble() - 0.5D) * 0.1D);
/* 1919 */     fft((double[])localObject3);
/*      */ 
/* 1921 */     for (j = i / 2; j < localObject3.length; j++)
/* 1922 */       localObject3[j] = 0.0D;
/* 1923 */     for (j = 4096; j < 8192; j++)
/* 1924 */       localObject3[j] = (1.0D - (j - 4096) / 4096.0D);
/* 1925 */     for (j = 0; j < 200; j++) {
/* 1926 */       double d3 = 1.0D - j / 200.0D;
/* 1927 */       localObject3[j] *= (1.0D + 20.0D * d3 * d3);
/*      */     }
/* 1929 */     for (j = 0; j < 30; j++)
/* 1930 */       localObject3[j] = 0.0D;
/* 1931 */     randomPhase((double[])localObject3, new Random(3049912L));
/* 1932 */     ifft((double[])localObject3);
/* 1933 */     normalize((double[])localObject3, 0.9D);
/* 1934 */     localObject3 = realPart((double[])localObject3);
/* 1935 */     double d2 = 1.0D;
/* 1936 */     for (int m = 0; m < localObject3.length; m++) {
/* 1937 */       localObject3[m] *= d2;
/* 1938 */       d2 *= 0.9996D;
/*      */     }
/* 1940 */     Object localObject2 = localObject3;
/*      */ 
/* 1943 */     for (i = 0; i < localObject2.length; i++)
/* 1944 */       localObject1[i] += localObject2[i] * 0.5D;
/* 1945 */     for (i = 0; i < 5; i++) {
/* 1946 */       localObject1[i] *= i / 5.0D;
/*      */     }
/* 1948 */     normalize(localObject1, 0.99D);
/*      */ 
/* 1950 */     SF2Sample localSF2Sample = newSimpleDrumSample(paramSF2Soundbank, "Melodic Toms", localObject1);
/* 1951 */     localSF2Sample.setOriginalPitch(63);
/*      */ 
/* 1953 */     localObject3 = new SF2Layer(paramSF2Soundbank);
/* 1954 */     ((SF2Layer)localObject3).setName("Melodic Toms");
/*      */ 
/* 1956 */     localObject4 = new SF2GlobalRegion();
/* 1957 */     ((SF2Layer)localObject3).setGlobalZone((SF2GlobalRegion)localObject4);
/* 1958 */     paramSF2Soundbank.addResource((SoundbankResource)localObject3);
/*      */ 
/* 1960 */     SF2LayerRegion localSF2LayerRegion = new SF2LayerRegion();
/* 1961 */     localSF2LayerRegion.putInteger(38, 12000);
/*      */ 
/* 1963 */     localSF2LayerRegion.putInteger(48, -100);
/* 1964 */     localSF2LayerRegion.setSample(localSF2Sample);
/* 1965 */     ((SF2Layer)localObject3).getRegions().add(localSF2LayerRegion);
/*      */ 
/* 1967 */     return localObject3;
/*      */   }
/*      */ 
/*      */   public static SF2Layer new_reverse_cymbal(SF2Soundbank paramSF2Soundbank)
/*      */   {
/* 1973 */     int i = 16384;
/* 1974 */     Object localObject2 = new double[2 * i];
/* 1975 */     Object localObject3 = new Random(3049912L);
/* 1976 */     for (int j = 0; j < localObject2.length; j += 2)
/* 1977 */       localObject2[j] = (2.0D * (((Random)localObject3).nextDouble() - 0.5D));
/* 1978 */     for (j = i / 2; j < localObject2.length; j++)
/* 1979 */       localObject2[j] = 0.0D;
/* 1980 */     for (j = 0; j < 100; j++) {
/* 1981 */       localObject2[j] = 0.0D;
/*      */     }
/* 1983 */     for (j = 0; j < 1024; j++) {
/* 1984 */       double d = j / 1024.0D;
/* 1985 */       localObject2[j] = (1.0D - d);
/*      */     }
/* 1987 */     Object localObject1 = localObject2;
/*      */ 
/* 1990 */     SF2Sample localSF2Sample = newSimpleFFTSample(paramSF2Soundbank, "Reverse Cymbal", localObject1, 100.0D, 20);
/*      */ 
/* 1993 */     localObject2 = new SF2Layer(paramSF2Soundbank);
/* 1994 */     ((SF2Layer)localObject2).setName("Reverse Cymbal");
/*      */ 
/* 1996 */     localObject3 = new SF2GlobalRegion();
/* 1997 */     ((SF2Layer)localObject2).setGlobalZone((SF2GlobalRegion)localObject3);
/* 1998 */     paramSF2Soundbank.addResource((SoundbankResource)localObject2);
/*      */ 
/* 2000 */     SF2LayerRegion localSF2LayerRegion = new SF2LayerRegion();
/* 2001 */     localSF2LayerRegion.putInteger(34, -200);
/* 2002 */     localSF2LayerRegion.putInteger(36, -12000);
/* 2003 */     localSF2LayerRegion.putInteger(54, 1);
/* 2004 */     localSF2LayerRegion.putInteger(38, -1000);
/* 2005 */     localSF2LayerRegion.putInteger(37, 1000);
/* 2006 */     localSF2LayerRegion.setSample(localSF2Sample);
/* 2007 */     ((SF2Layer)localObject2).getRegions().add(localSF2LayerRegion);
/*      */ 
/* 2009 */     return localObject2;
/*      */   }
/*      */ 
/*      */   public static SF2Layer new_snare_drum(SF2Soundbank paramSF2Soundbank)
/*      */   {
/* 2019 */     int i = 16384;
/* 2020 */     Object localObject3 = new double[2 * i];
/* 2021 */     complexGaussianDist((double[])localObject3, 24.0D, 0.5D, 1.0D);
/* 2022 */     randomPhase((double[])localObject3, new Random(3049912L));
/* 2023 */     ifft((double[])localObject3);
/* 2024 */     normalize((double[])localObject3, 0.5D);
/* 2025 */     localObject3 = realPart((double[])localObject3);
/*      */ 
/* 2027 */     double d1 = localObject3.length;
/* 2028 */     for (int k = 0; k < localObject3.length; k++)
/* 2029 */       localObject3[k] *= (1.0D - k / d1);
/* 2030 */     Object localObject1 = localObject3;
/*      */ 
/* 2035 */     i = 16384;
/* 2036 */     localObject3 = new double[2 * i];
/* 2037 */     Object localObject4 = new Random(3049912L);
/* 2038 */     for (int j = 0; j < localObject3.length; j += 2)
/* 2039 */       localObject3[j] = (2.0D * (((Random)localObject4).nextDouble() - 0.5D) * 0.1D);
/* 2040 */     fft((double[])localObject3);
/*      */ 
/* 2042 */     for (j = i / 2; j < localObject3.length; j++)
/* 2043 */       localObject3[j] = 0.0D;
/* 2044 */     for (j = 4096; j < 8192; j++)
/* 2045 */       localObject3[j] = (1.0D - (j - 4096) / 4096.0D);
/* 2046 */     for (j = 0; j < 300; j++) {
/* 2047 */       double d3 = 1.0D - j / 300.0D;
/* 2048 */       localObject3[j] *= (1.0D + 20.0D * d3 * d3);
/*      */     }
/* 2050 */     for (j = 0; j < 24; j++)
/* 2051 */       localObject3[j] = 0.0D;
/* 2052 */     randomPhase((double[])localObject3, new Random(3049912L));
/* 2053 */     ifft((double[])localObject3);
/* 2054 */     normalize((double[])localObject3, 0.9D);
/* 2055 */     localObject3 = realPart((double[])localObject3);
/* 2056 */     double d2 = 1.0D;
/* 2057 */     for (int m = 0; m < localObject3.length; m++) {
/* 2058 */       localObject3[m] *= d2;
/* 2059 */       d2 *= 0.9998D;
/*      */     }
/* 2061 */     Object localObject2 = localObject3;
/*      */ 
/* 2064 */     for (i = 0; i < localObject2.length; i++)
/* 2065 */       localObject1[i] += localObject2[i];
/* 2066 */     for (i = 0; i < 5; i++) {
/* 2067 */       localObject1[i] *= i / 5.0D;
/*      */     }
/* 2069 */     SF2Sample localSF2Sample = newSimpleDrumSample(paramSF2Soundbank, "Snare Drum", localObject1);
/*      */ 
/* 2071 */     localObject3 = new SF2Layer(paramSF2Soundbank);
/* 2072 */     ((SF2Layer)localObject3).setName("Snare Drum");
/*      */ 
/* 2074 */     localObject4 = new SF2GlobalRegion();
/* 2075 */     ((SF2Layer)localObject3).setGlobalZone((SF2GlobalRegion)localObject4);
/* 2076 */     paramSF2Soundbank.addResource((SoundbankResource)localObject3);
/*      */ 
/* 2078 */     SF2LayerRegion localSF2LayerRegion = new SF2LayerRegion();
/* 2079 */     localSF2LayerRegion.putInteger(38, 12000);
/* 2080 */     localSF2LayerRegion.putInteger(56, 0);
/* 2081 */     localSF2LayerRegion.putInteger(48, -100);
/* 2082 */     localSF2LayerRegion.setSample(localSF2Sample);
/* 2083 */     ((SF2Layer)localObject3).getRegions().add(localSF2LayerRegion);
/*      */ 
/* 2085 */     return localObject3;
/*      */   }
/*      */ 
/*      */   public static SF2Layer new_bass_drum(SF2Soundbank paramSF2Soundbank)
/*      */   {
/* 2095 */     int i = 16384;
/* 2096 */     Object localObject3 = new double[2 * i];
/* 2097 */     complexGaussianDist((double[])localObject3, 10.0D, 2.0D, 1.0D);
/* 2098 */     complexGaussianDist((double[])localObject3, 17.199999999999999D, 2.0D, 1.0D);
/* 2099 */     randomPhase((double[])localObject3, new Random(3049912L));
/* 2100 */     ifft((double[])localObject3);
/* 2101 */     normalize((double[])localObject3, 0.9D);
/* 2102 */     localObject3 = realPart((double[])localObject3);
/* 2103 */     double d1 = localObject3.length;
/* 2104 */     for (int k = 0; k < localObject3.length; k++)
/* 2105 */       localObject3[k] *= (1.0D - k / d1);
/* 2106 */     Object localObject1 = localObject3;
/*      */ 
/* 2111 */     i = 4096;
/* 2112 */     localObject3 = new double[2 * i];
/* 2113 */     Object localObject4 = new Random(3049912L);
/* 2114 */     for (int j = 0; j < localObject3.length; j += 2)
/* 2115 */       localObject3[j] = (2.0D * (((Random)localObject4).nextDouble() - 0.5D) * 0.1D);
/* 2116 */     fft((double[])localObject3);
/*      */ 
/* 2118 */     for (j = i / 2; j < localObject3.length; j++)
/* 2119 */       localObject3[j] = 0.0D;
/* 2120 */     for (j = 1024; j < 2048; j++)
/* 2121 */       localObject3[j] = (1.0D - (j - 1024) / 1024.0D);
/* 2122 */     for (j = 0; j < 512; j++)
/* 2123 */       localObject3[j] = (10 * j / 512.0D);
/* 2124 */     for (j = 0; j < 10; j++)
/* 2125 */       localObject3[j] = 0.0D;
/* 2126 */     randomPhase((double[])localObject3, new Random(3049912L));
/* 2127 */     ifft((double[])localObject3);
/* 2128 */     normalize((double[])localObject3, 0.9D);
/* 2129 */     localObject3 = realPart((double[])localObject3);
/* 2130 */     double d2 = 1.0D;
/* 2131 */     for (int m = 0; m < localObject3.length; m++) {
/* 2132 */       localObject3[m] *= d2;
/* 2133 */       d2 *= 0.999D;
/*      */     }
/* 2135 */     Object localObject2 = localObject3;
/*      */ 
/* 2138 */     for (i = 0; i < localObject2.length; i++)
/* 2139 */       localObject1[i] += localObject2[i] * 0.5D;
/* 2140 */     for (i = 0; i < 5; i++) {
/* 2141 */       localObject1[i] *= i / 5.0D;
/*      */     }
/* 2143 */     SF2Sample localSF2Sample = newSimpleDrumSample(paramSF2Soundbank, "Bass Drum", localObject1);
/*      */ 
/* 2145 */     localObject3 = new SF2Layer(paramSF2Soundbank);
/* 2146 */     ((SF2Layer)localObject3).setName("Bass Drum");
/*      */ 
/* 2148 */     localObject4 = new SF2GlobalRegion();
/* 2149 */     ((SF2Layer)localObject3).setGlobalZone((SF2GlobalRegion)localObject4);
/* 2150 */     paramSF2Soundbank.addResource((SoundbankResource)localObject3);
/*      */ 
/* 2152 */     SF2LayerRegion localSF2LayerRegion = new SF2LayerRegion();
/* 2153 */     localSF2LayerRegion.putInteger(38, 12000);
/* 2154 */     localSF2LayerRegion.putInteger(56, 0);
/* 2155 */     localSF2LayerRegion.putInteger(48, -100);
/* 2156 */     localSF2LayerRegion.setSample(localSF2Sample);
/* 2157 */     ((SF2Layer)localObject3).getRegions().add(localSF2LayerRegion);
/*      */ 
/* 2159 */     return localObject3;
/*      */   }
/*      */ 
/*      */   public static SF2Layer new_tom(SF2Soundbank paramSF2Soundbank)
/*      */   {
/* 2169 */     int i = 16384;
/* 2170 */     Object localObject3 = new double[2 * i];
/* 2171 */     complexGaussianDist((double[])localObject3, 30.0D, 0.5D, 1.0D);
/* 2172 */     randomPhase((double[])localObject3, new Random(3049912L));
/* 2173 */     ifft((double[])localObject3);
/* 2174 */     normalize((double[])localObject3, 0.8D);
/* 2175 */     localObject3 = realPart((double[])localObject3);
/*      */ 
/* 2177 */     double d1 = localObject3.length;
/* 2178 */     for (int k = 0; k < localObject3.length; k++)
/* 2179 */       localObject3[k] *= (1.0D - k / d1);
/* 2180 */     Object localObject1 = localObject3;
/*      */ 
/* 2185 */     i = 16384;
/* 2186 */     localObject3 = new double[2 * i];
/* 2187 */     Object localObject4 = new Random(3049912L);
/* 2188 */     for (int j = 0; j < localObject3.length; j += 2)
/* 2189 */       localObject3[j] = (2.0D * (((Random)localObject4).nextDouble() - 0.5D) * 0.1D);
/* 2190 */     fft((double[])localObject3);
/*      */ 
/* 2192 */     for (j = i / 2; j < localObject3.length; j++)
/* 2193 */       localObject3[j] = 0.0D;
/* 2194 */     for (j = 4096; j < 8192; j++)
/* 2195 */       localObject3[j] = (1.0D - (j - 4096) / 4096.0D);
/* 2196 */     for (j = 0; j < 200; j++) {
/* 2197 */       double d3 = 1.0D - j / 200.0D;
/* 2198 */       localObject3[j] *= (1.0D + 20.0D * d3 * d3);
/*      */     }
/* 2200 */     for (j = 0; j < 30; j++)
/* 2201 */       localObject3[j] = 0.0D;
/* 2202 */     randomPhase((double[])localObject3, new Random(3049912L));
/* 2203 */     ifft((double[])localObject3);
/* 2204 */     normalize((double[])localObject3, 0.9D);
/* 2205 */     localObject3 = realPart((double[])localObject3);
/* 2206 */     double d2 = 1.0D;
/* 2207 */     for (int m = 0; m < localObject3.length; m++) {
/* 2208 */       localObject3[m] *= d2;
/* 2209 */       d2 *= 0.9996D;
/*      */     }
/* 2211 */     Object localObject2 = localObject3;
/*      */ 
/* 2214 */     for (i = 0; i < localObject2.length; i++)
/* 2215 */       localObject1[i] += localObject2[i] * 0.5D;
/* 2216 */     for (i = 0; i < 5; i++) {
/* 2217 */       localObject1[i] *= i / 5.0D;
/*      */     }
/* 2219 */     normalize(localObject1, 0.99D);
/*      */ 
/* 2221 */     SF2Sample localSF2Sample = newSimpleDrumSample(paramSF2Soundbank, "Tom", localObject1);
/* 2222 */     localSF2Sample.setOriginalPitch(50);
/*      */ 
/* 2224 */     localObject3 = new SF2Layer(paramSF2Soundbank);
/* 2225 */     ((SF2Layer)localObject3).setName("Tom");
/*      */ 
/* 2227 */     localObject4 = new SF2GlobalRegion();
/* 2228 */     ((SF2Layer)localObject3).setGlobalZone((SF2GlobalRegion)localObject4);
/* 2229 */     paramSF2Soundbank.addResource((SoundbankResource)localObject3);
/*      */ 
/* 2231 */     SF2LayerRegion localSF2LayerRegion = new SF2LayerRegion();
/* 2232 */     localSF2LayerRegion.putInteger(38, 12000);
/*      */ 
/* 2234 */     localSF2LayerRegion.putInteger(48, -100);
/* 2235 */     localSF2LayerRegion.setSample(localSF2Sample);
/* 2236 */     ((SF2Layer)localObject3).getRegions().add(localSF2LayerRegion);
/*      */ 
/* 2238 */     return localObject3;
/*      */   }
/*      */ 
/*      */   public static SF2Layer new_closed_hihat(SF2Soundbank paramSF2Soundbank)
/*      */   {
/* 2246 */     int i = 16384;
/* 2247 */     Object localObject2 = new double[2 * i];
/* 2248 */     Object localObject3 = new Random(3049912L);
/* 2249 */     for (int j = 0; j < localObject2.length; j += 2)
/* 2250 */       localObject2[j] = (2.0D * (((Random)localObject3).nextDouble() - 0.5D) * 0.1D);
/* 2251 */     fft((double[])localObject2);
/*      */ 
/* 2253 */     for (j = i / 2; j < localObject2.length; j++)
/* 2254 */       localObject2[j] = 0.0D;
/* 2255 */     for (j = 4096; j < 8192; j++)
/* 2256 */       localObject2[j] = (1.0D - (j - 4096) / 4096.0D);
/* 2257 */     for (j = 0; j < 2048; j++)
/* 2258 */       localObject2[j] = (0.2D + 0.8D * (j / 2048.0D));
/* 2259 */     randomPhase((double[])localObject2, new Random(3049912L));
/* 2260 */     ifft((double[])localObject2);
/* 2261 */     normalize((double[])localObject2, 0.9D);
/* 2262 */     localObject2 = realPart((double[])localObject2);
/* 2263 */     double d = 1.0D;
/* 2264 */     for (int k = 0; k < localObject2.length; k++) {
/* 2265 */       localObject2[k] *= d;
/* 2266 */       d *= 0.9996D;
/*      */     }
/* 2268 */     Object localObject1 = localObject2;
/*      */ 
/* 2271 */     for (i = 0; i < 5; i++)
/* 2272 */       localObject1[i] *= i / 5.0D;
/* 2273 */     SF2Sample localSF2Sample = newSimpleDrumSample(paramSF2Soundbank, "Closed Hi-Hat", localObject1);
/*      */ 
/* 2275 */     localObject2 = new SF2Layer(paramSF2Soundbank);
/* 2276 */     ((SF2Layer)localObject2).setName("Closed Hi-Hat");
/*      */ 
/* 2278 */     localObject3 = new SF2GlobalRegion();
/* 2279 */     ((SF2Layer)localObject2).setGlobalZone((SF2GlobalRegion)localObject3);
/* 2280 */     paramSF2Soundbank.addResource((SoundbankResource)localObject2);
/*      */ 
/* 2282 */     SF2LayerRegion localSF2LayerRegion = new SF2LayerRegion();
/* 2283 */     localSF2LayerRegion.putInteger(38, 12000);
/* 2284 */     localSF2LayerRegion.putInteger(56, 0);
/* 2285 */     localSF2LayerRegion.putInteger(57, 1);
/* 2286 */     localSF2LayerRegion.setSample(localSF2Sample);
/* 2287 */     ((SF2Layer)localObject2).getRegions().add(localSF2LayerRegion);
/*      */ 
/* 2289 */     return localObject2;
/*      */   }
/*      */ 
/*      */   public static SF2Layer new_open_hihat(SF2Soundbank paramSF2Soundbank)
/*      */   {
/* 2295 */     int i = 16384;
/* 2296 */     Object localObject2 = new double[2 * i];
/* 2297 */     Object localObject3 = new Random(3049912L);
/* 2298 */     for (int j = 0; j < localObject2.length; j += 2)
/* 2299 */       localObject2[j] = (2.0D * (((Random)localObject3).nextDouble() - 0.5D));
/* 2300 */     for (j = i / 2; j < localObject2.length; j++)
/* 2301 */       localObject2[j] = 0.0D;
/* 2302 */     for (j = 0; j < 200; j++)
/* 2303 */       localObject2[j] = 0.0D;
/* 2304 */     for (j = 0; j < 8192; j++) {
/* 2305 */       double d = j / 8192.0D;
/* 2306 */       localObject2[j] = d;
/*      */     }
/* 2308 */     Object localObject1 = localObject2;
/*      */ 
/* 2311 */     SF2Sample localSF2Sample = newSimpleFFTSample(paramSF2Soundbank, "Open Hi-Hat", localObject1, 1000.0D, 5);
/*      */ 
/* 2313 */     localObject2 = new SF2Layer(paramSF2Soundbank);
/* 2314 */     ((SF2Layer)localObject2).setName("Open Hi-Hat");
/*      */ 
/* 2316 */     localObject3 = new SF2GlobalRegion();
/* 2317 */     ((SF2Layer)localObject2).setGlobalZone((SF2GlobalRegion)localObject3);
/* 2318 */     paramSF2Soundbank.addResource((SoundbankResource)localObject2);
/*      */ 
/* 2320 */     SF2LayerRegion localSF2LayerRegion = new SF2LayerRegion();
/* 2321 */     localSF2LayerRegion.putInteger(36, 1500);
/* 2322 */     localSF2LayerRegion.putInteger(54, 1);
/* 2323 */     localSF2LayerRegion.putInteger(38, 1500);
/* 2324 */     localSF2LayerRegion.putInteger(37, 1000);
/* 2325 */     localSF2LayerRegion.putInteger(56, 0);
/* 2326 */     localSF2LayerRegion.putInteger(57, 1);
/* 2327 */     localSF2LayerRegion.setSample(localSF2Sample);
/* 2328 */     ((SF2Layer)localObject2).getRegions().add(localSF2LayerRegion);
/*      */ 
/* 2330 */     return localObject2;
/*      */   }
/*      */ 
/*      */   public static SF2Layer new_crash_cymbal(SF2Soundbank paramSF2Soundbank)
/*      */   {
/* 2336 */     int i = 16384;
/* 2337 */     Object localObject2 = new double[2 * i];
/* 2338 */     Object localObject3 = new Random(3049912L);
/* 2339 */     for (int j = 0; j < localObject2.length; j += 2)
/* 2340 */       localObject2[j] = (2.0D * (((Random)localObject3).nextDouble() - 0.5D));
/* 2341 */     for (j = i / 2; j < localObject2.length; j++)
/* 2342 */       localObject2[j] = 0.0D;
/* 2343 */     for (j = 0; j < 100; j++)
/* 2344 */       localObject2[j] = 0.0D;
/* 2345 */     for (j = 0; j < 1024; j++) {
/* 2346 */       double d = j / 1024.0D;
/* 2347 */       localObject2[j] = d;
/*      */     }
/* 2349 */     Object localObject1 = localObject2;
/*      */ 
/* 2352 */     SF2Sample localSF2Sample = newSimpleFFTSample(paramSF2Soundbank, "Crash Cymbal", localObject1, 1000.0D, 5);
/*      */ 
/* 2354 */     localObject2 = new SF2Layer(paramSF2Soundbank);
/* 2355 */     ((SF2Layer)localObject2).setName("Crash Cymbal");
/*      */ 
/* 2357 */     localObject3 = new SF2GlobalRegion();
/* 2358 */     ((SF2Layer)localObject2).setGlobalZone((SF2GlobalRegion)localObject3);
/* 2359 */     paramSF2Soundbank.addResource((SoundbankResource)localObject2);
/*      */ 
/* 2361 */     SF2LayerRegion localSF2LayerRegion = new SF2LayerRegion();
/* 2362 */     localSF2LayerRegion.putInteger(36, 1800);
/* 2363 */     localSF2LayerRegion.putInteger(54, 1);
/* 2364 */     localSF2LayerRegion.putInteger(38, 1800);
/* 2365 */     localSF2LayerRegion.putInteger(37, 1000);
/* 2366 */     localSF2LayerRegion.putInteger(56, 0);
/* 2367 */     localSF2LayerRegion.setSample(localSF2Sample);
/* 2368 */     ((SF2Layer)localObject2).getRegions().add(localSF2LayerRegion);
/*      */ 
/* 2370 */     return localObject2;
/*      */   }
/*      */ 
/*      */   public static SF2Layer new_side_stick(SF2Soundbank paramSF2Soundbank)
/*      */   {
/* 2378 */     int i = 16384;
/* 2379 */     Object localObject2 = new double[2 * i];
/* 2380 */     Object localObject3 = new Random(3049912L);
/* 2381 */     for (int j = 0; j < localObject2.length; j += 2)
/* 2382 */       localObject2[j] = (2.0D * (((Random)localObject3).nextDouble() - 0.5D) * 0.1D);
/* 2383 */     fft((double[])localObject2);
/*      */ 
/* 2385 */     for (j = i / 2; j < localObject2.length; j++)
/* 2386 */       localObject2[j] = 0.0D;
/* 2387 */     for (j = 4096; j < 8192; j++)
/* 2388 */       localObject2[j] = (1.0D - (j - 4096) / 4096.0D);
/* 2389 */     for (j = 0; j < 200; j++) {
/* 2390 */       double d2 = 1.0D - j / 200.0D;
/* 2391 */       localObject2[j] *= (1.0D + 20.0D * d2 * d2);
/*      */     }
/* 2393 */     for (j = 0; j < 30; j++)
/* 2394 */       localObject2[j] = 0.0D;
/* 2395 */     randomPhase((double[])localObject2, new Random(3049912L));
/* 2396 */     ifft((double[])localObject2);
/* 2397 */     normalize((double[])localObject2, 0.9D);
/* 2398 */     localObject2 = realPart((double[])localObject2);
/* 2399 */     double d1 = 1.0D;
/* 2400 */     for (int k = 0; k < localObject2.length; k++) {
/* 2401 */       localObject2[k] *= d1;
/* 2402 */       d1 *= 0.9996D;
/*      */     }
/* 2404 */     Object localObject1 = localObject2;
/*      */ 
/* 2407 */     for (i = 0; i < 10; i++) {
/* 2408 */       localObject1[i] *= i / 10.0D;
/*      */     }
/* 2410 */     SF2Sample localSF2Sample = newSimpleDrumSample(paramSF2Soundbank, "Side Stick", localObject1);
/*      */ 
/* 2412 */     localObject2 = new SF2Layer(paramSF2Soundbank);
/* 2413 */     ((SF2Layer)localObject2).setName("Side Stick");
/*      */ 
/* 2415 */     localObject3 = new SF2GlobalRegion();
/* 2416 */     ((SF2Layer)localObject2).setGlobalZone((SF2GlobalRegion)localObject3);
/* 2417 */     paramSF2Soundbank.addResource((SoundbankResource)localObject2);
/*      */ 
/* 2419 */     SF2LayerRegion localSF2LayerRegion = new SF2LayerRegion();
/* 2420 */     localSF2LayerRegion.putInteger(38, 12000);
/* 2421 */     localSF2LayerRegion.putInteger(56, 0);
/* 2422 */     localSF2LayerRegion.putInteger(48, -50);
/* 2423 */     localSF2LayerRegion.setSample(localSF2Sample);
/* 2424 */     ((SF2Layer)localObject2).getRegions().add(localSF2LayerRegion);
/*      */ 
/* 2426 */     return localObject2;
/*      */   }
/*      */ 
/*      */   public static SF2Sample newSimpleFFTSample(SF2Soundbank paramSF2Soundbank, String paramString, double[] paramArrayOfDouble, double paramDouble)
/*      */   {
/* 2432 */     return newSimpleFFTSample(paramSF2Soundbank, paramString, paramArrayOfDouble, paramDouble, 10);
/*      */   }
/*      */ 
/*      */   public static SF2Sample newSimpleFFTSample(SF2Soundbank paramSF2Soundbank, String paramString, double[] paramArrayOfDouble, double paramDouble, int paramInt)
/*      */   {
/* 2438 */     int i = paramArrayOfDouble.length / 2;
/* 2439 */     AudioFormat localAudioFormat = new AudioFormat(44100.0F, 16, 1, true, false);
/* 2440 */     double d1 = paramDouble / i * localAudioFormat.getSampleRate() * 0.5D;
/*      */ 
/* 2442 */     randomPhase(paramArrayOfDouble);
/* 2443 */     ifft(paramArrayOfDouble);
/* 2444 */     paramArrayOfDouble = realPart(paramArrayOfDouble);
/* 2445 */     normalize(paramArrayOfDouble, 0.9D);
/* 2446 */     float[] arrayOfFloat = toFloat(paramArrayOfDouble);
/* 2447 */     arrayOfFloat = loopExtend(arrayOfFloat, arrayOfFloat.length + 512);
/* 2448 */     fadeUp(arrayOfFloat, paramInt);
/* 2449 */     byte[] arrayOfByte = toBytes(arrayOfFloat, localAudioFormat);
/*      */ 
/* 2454 */     SF2Sample localSF2Sample = new SF2Sample(paramSF2Soundbank);
/* 2455 */     localSF2Sample.setName(paramString);
/* 2456 */     localSF2Sample.setData(arrayOfByte);
/* 2457 */     localSF2Sample.setStartLoop(256L);
/* 2458 */     localSF2Sample.setEndLoop(i + 256);
/* 2459 */     localSF2Sample.setSampleRate(()localAudioFormat.getSampleRate());
/* 2460 */     double d2 = 81.0D + 12.0D * Math.log(d1 / 440.0D) / Math.log(2.0D);
/*      */ 
/* 2462 */     localSF2Sample.setOriginalPitch((int)d2);
/* 2463 */     localSF2Sample.setPitchCorrection((byte)(int)(-(d2 - (int)d2) * 100.0D));
/* 2464 */     paramSF2Soundbank.addResource(localSF2Sample);
/*      */ 
/* 2466 */     return localSF2Sample;
/*      */   }
/*      */ 
/*      */   public static SF2Sample newSimpleFFTSample_dist(SF2Soundbank paramSF2Soundbank, String paramString, double[] paramArrayOfDouble, double paramDouble1, double paramDouble2)
/*      */   {
/* 2472 */     int i = paramArrayOfDouble.length / 2;
/* 2473 */     AudioFormat localAudioFormat = new AudioFormat(44100.0F, 16, 1, true, false);
/* 2474 */     double d1 = paramDouble1 / i * localAudioFormat.getSampleRate() * 0.5D;
/*      */ 
/* 2476 */     randomPhase(paramArrayOfDouble);
/* 2477 */     ifft(paramArrayOfDouble);
/* 2478 */     paramArrayOfDouble = realPart(paramArrayOfDouble);
/*      */ 
/* 2480 */     for (int j = 0; j < paramArrayOfDouble.length; j++) {
/* 2481 */       paramArrayOfDouble[j] = ((1.0D - Math.exp(-Math.abs(paramArrayOfDouble[j] * paramDouble2))) * Math.signum(paramArrayOfDouble[j]));
/*      */     }
/*      */ 
/* 2485 */     normalize(paramArrayOfDouble, 0.9D);
/* 2486 */     float[] arrayOfFloat = toFloat(paramArrayOfDouble);
/* 2487 */     arrayOfFloat = loopExtend(arrayOfFloat, arrayOfFloat.length + 512);
/* 2488 */     fadeUp(arrayOfFloat, 80);
/* 2489 */     byte[] arrayOfByte = toBytes(arrayOfFloat, localAudioFormat);
/*      */ 
/* 2494 */     SF2Sample localSF2Sample = new SF2Sample(paramSF2Soundbank);
/* 2495 */     localSF2Sample.setName(paramString);
/* 2496 */     localSF2Sample.setData(arrayOfByte);
/* 2497 */     localSF2Sample.setStartLoop(256L);
/* 2498 */     localSF2Sample.setEndLoop(i + 256);
/* 2499 */     localSF2Sample.setSampleRate(()localAudioFormat.getSampleRate());
/* 2500 */     double d2 = 81.0D + 12.0D * Math.log(d1 / 440.0D) / Math.log(2.0D);
/*      */ 
/* 2502 */     localSF2Sample.setOriginalPitch((int)d2);
/* 2503 */     localSF2Sample.setPitchCorrection((byte)(int)(-(d2 - (int)d2) * 100.0D));
/* 2504 */     paramSF2Soundbank.addResource(localSF2Sample);
/*      */ 
/* 2506 */     return localSF2Sample;
/*      */   }
/*      */ 
/*      */   public static SF2Sample newSimpleDrumSample(SF2Soundbank paramSF2Soundbank, String paramString, double[] paramArrayOfDouble)
/*      */   {
/* 2512 */     int i = paramArrayOfDouble.length;
/* 2513 */     AudioFormat localAudioFormat = new AudioFormat(44100.0F, 16, 1, true, false);
/*      */ 
/* 2515 */     byte[] arrayOfByte = toBytes(toFloat(realPart(paramArrayOfDouble)), localAudioFormat);
/*      */ 
/* 2520 */     SF2Sample localSF2Sample = new SF2Sample(paramSF2Soundbank);
/* 2521 */     localSF2Sample.setName(paramString);
/* 2522 */     localSF2Sample.setData(arrayOfByte);
/* 2523 */     localSF2Sample.setStartLoop(256L);
/* 2524 */     localSF2Sample.setEndLoop(i + 256);
/* 2525 */     localSF2Sample.setSampleRate(()localAudioFormat.getSampleRate());
/* 2526 */     localSF2Sample.setOriginalPitch(60);
/* 2527 */     paramSF2Soundbank.addResource(localSF2Sample);
/*      */ 
/* 2529 */     return localSF2Sample;
/*      */   }
/*      */ 
/*      */   public static SF2Layer newLayer(SF2Soundbank paramSF2Soundbank, String paramString, SF2Sample paramSF2Sample) {
/* 2533 */     SF2LayerRegion localSF2LayerRegion = new SF2LayerRegion();
/* 2534 */     localSF2LayerRegion.setSample(paramSF2Sample);
/*      */ 
/* 2536 */     SF2Layer localSF2Layer = new SF2Layer(paramSF2Soundbank);
/* 2537 */     localSF2Layer.setName(paramString);
/* 2538 */     localSF2Layer.getRegions().add(localSF2LayerRegion);
/* 2539 */     paramSF2Soundbank.addResource(localSF2Layer);
/*      */ 
/* 2541 */     return localSF2Layer;
/*      */   }
/*      */ 
/*      */   public static SF2Instrument newInstrument(SF2Soundbank paramSF2Soundbank, String paramString, Patch paramPatch, SF2Layer[] paramArrayOfSF2Layer)
/*      */   {
/* 2550 */     SF2Instrument localSF2Instrument = new SF2Instrument(paramSF2Soundbank);
/* 2551 */     localSF2Instrument.setPatch(paramPatch);
/* 2552 */     localSF2Instrument.setName(paramString);
/* 2553 */     paramSF2Soundbank.addInstrument(localSF2Instrument);
/*      */ 
/* 2558 */     for (int i = 0; i < paramArrayOfSF2Layer.length; i++) {
/* 2559 */       SF2InstrumentRegion localSF2InstrumentRegion = new SF2InstrumentRegion();
/* 2560 */       localSF2InstrumentRegion.setLayer(paramArrayOfSF2Layer[i]);
/* 2561 */       localSF2Instrument.getRegions().add(localSF2InstrumentRegion);
/*      */     }
/*      */ 
/* 2564 */     return localSF2Instrument;
/*      */   }
/*      */ 
/*      */   public static void ifft(double[] paramArrayOfDouble) {
/* 2568 */     new FFT(paramArrayOfDouble.length / 2, 1).transform(paramArrayOfDouble);
/*      */   }
/*      */ 
/*      */   public static void fft(double[] paramArrayOfDouble) {
/* 2572 */     new FFT(paramArrayOfDouble.length / 2, -1).transform(paramArrayOfDouble);
/*      */   }
/*      */ 
/*      */   public static void complexGaussianDist(double[] paramArrayOfDouble, double paramDouble1, double paramDouble2, double paramDouble3)
/*      */   {
/* 2577 */     for (int i = 0; i < paramArrayOfDouble.length / 4; i++)
/* 2578 */       paramArrayOfDouble[(i * 2)] += paramDouble3 * (1.0D / (paramDouble2 * Math.sqrt(6.283185307179586D)) * Math.exp(-0.5D * Math.pow((i - paramDouble1) / paramDouble2, 2.0D)));
/*      */   }
/*      */ 
/*      */   public static void randomPhase(double[] paramArrayOfDouble)
/*      */   {
/* 2584 */     for (int i = 0; i < paramArrayOfDouble.length; i += 2) {
/* 2585 */       double d1 = Math.random() * 2.0D * 3.141592653589793D;
/* 2586 */       double d2 = paramArrayOfDouble[i];
/* 2587 */       paramArrayOfDouble[i] = (Math.sin(d1) * d2);
/* 2588 */       paramArrayOfDouble[(i + 1)] = (Math.cos(d1) * d2);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static void randomPhase(double[] paramArrayOfDouble, Random paramRandom) {
/* 2593 */     for (int i = 0; i < paramArrayOfDouble.length; i += 2) {
/* 2594 */       double d1 = paramRandom.nextDouble() * 2.0D * 3.141592653589793D;
/* 2595 */       double d2 = paramArrayOfDouble[i];
/* 2596 */       paramArrayOfDouble[i] = (Math.sin(d1) * d2);
/* 2597 */       paramArrayOfDouble[(i + 1)] = (Math.cos(d1) * d2);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static void normalize(double[] paramArrayOfDouble, double paramDouble) {
/* 2602 */     double d1 = 0.0D;
/* 2603 */     for (int i = 0; i < paramArrayOfDouble.length; i++) {
/* 2604 */       if (paramArrayOfDouble[i] > d1)
/* 2605 */         d1 = paramArrayOfDouble[i];
/* 2606 */       if (-paramArrayOfDouble[i] > d1)
/* 2607 */         d1 = -paramArrayOfDouble[i];
/*      */     }
/* 2609 */     if (d1 == 0.0D)
/* 2610 */       return;
/* 2611 */     double d2 = paramDouble / d1;
/* 2612 */     for (int j = 0; j < paramArrayOfDouble.length; j++)
/* 2613 */       paramArrayOfDouble[j] *= d2;
/*      */   }
/*      */ 
/*      */   public static void normalize(float[] paramArrayOfFloat, double paramDouble) {
/* 2617 */     double d1 = 0.5D;
/* 2618 */     for (int i = 0; i < paramArrayOfFloat.length; i++) {
/* 2619 */       if (paramArrayOfFloat[(i * 2)] > d1)
/* 2620 */         d1 = paramArrayOfFloat[(i * 2)];
/* 2621 */       if (-paramArrayOfFloat[(i * 2)] > d1)
/* 2622 */         d1 = -paramArrayOfFloat[(i * 2)];
/*      */     }
/* 2624 */     double d2 = paramDouble / d1;
/* 2625 */     for (int j = 0; j < paramArrayOfFloat.length; j++)
/*      */     {
/*      */       int tmp82_81 = (j * 2); paramArrayOfFloat[tmp82_81] = ((float)(paramArrayOfFloat[tmp82_81] * d2));
/*      */     }
/*      */   }
/*      */ 
/* 2630 */   public static double[] realPart(double[] paramArrayOfDouble) { double[] arrayOfDouble = new double[paramArrayOfDouble.length / 2];
/* 2631 */     for (int i = 0; i < arrayOfDouble.length; i++) {
/* 2632 */       arrayOfDouble[i] = paramArrayOfDouble[(i * 2)];
/*      */     }
/* 2634 */     return arrayOfDouble; }
/*      */ 
/*      */   public static double[] imgPart(double[] paramArrayOfDouble)
/*      */   {
/* 2638 */     double[] arrayOfDouble = new double[paramArrayOfDouble.length / 2];
/* 2639 */     for (int i = 0; i < arrayOfDouble.length; i++) {
/* 2640 */       arrayOfDouble[i] = paramArrayOfDouble[(i * 2)];
/*      */     }
/* 2642 */     return arrayOfDouble;
/*      */   }
/*      */ 
/*      */   public static float[] toFloat(double[] paramArrayOfDouble) {
/* 2646 */     float[] arrayOfFloat = new float[paramArrayOfDouble.length];
/* 2647 */     for (int i = 0; i < arrayOfFloat.length; i++) {
/* 2648 */       arrayOfFloat[i] = ((float)paramArrayOfDouble[i]);
/*      */     }
/* 2650 */     return arrayOfFloat;
/*      */   }
/*      */ 
/*      */   public static byte[] toBytes(float[] paramArrayOfFloat, AudioFormat paramAudioFormat) {
/* 2654 */     byte[] arrayOfByte = new byte[paramArrayOfFloat.length * paramAudioFormat.getFrameSize()];
/* 2655 */     return AudioFloatConverter.getConverter(paramAudioFormat).toByteArray(paramArrayOfFloat, arrayOfByte);
/*      */   }
/*      */ 
/*      */   public static void fadeUp(double[] paramArrayOfDouble, int paramInt) {
/* 2659 */     double d = paramInt;
/* 2660 */     for (int i = 0; i < paramInt; i++)
/* 2661 */       paramArrayOfDouble[i] *= i / d;
/*      */   }
/*      */ 
/*      */   public static void fadeUp(float[] paramArrayOfFloat, int paramInt) {
/* 2665 */     double d = paramInt;
/* 2666 */     for (int i = 0; i < paramInt; tmp15_13++)
/*      */     {
/*      */       int tmp15_13 = i; paramArrayOfFloat[tmp15_13] = ((float)(paramArrayOfFloat[tmp15_13] * (tmp15_13 / d)));
/*      */     }
/*      */   }
/*      */ 
/* 2671 */   public static double[] loopExtend(double[] paramArrayOfDouble, int paramInt) { double[] arrayOfDouble = new double[paramInt];
/* 2672 */     int i = paramArrayOfDouble.length;
/* 2673 */     int j = 0;
/* 2674 */     for (int k = 0; k < arrayOfDouble.length; k++) {
/* 2675 */       arrayOfDouble[k] = paramArrayOfDouble[j];
/* 2676 */       j++;
/* 2677 */       if (j == i)
/* 2678 */         j = 0;
/*      */     }
/* 2680 */     return arrayOfDouble; }
/*      */ 
/*      */   public static float[] loopExtend(float[] paramArrayOfFloat, int paramInt)
/*      */   {
/* 2684 */     float[] arrayOfFloat = new float[paramInt];
/* 2685 */     int i = paramArrayOfFloat.length;
/* 2686 */     int j = 0;
/* 2687 */     for (int k = 0; k < arrayOfFloat.length; k++) {
/* 2688 */       arrayOfFloat[k] = paramArrayOfFloat[j];
/* 2689 */       j++;
/* 2690 */       if (j == i)
/* 2691 */         j = 0;
/*      */     }
/* 2693 */     return arrayOfFloat;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.EmergencySoundbank
 * JD-Core Version:    0.6.2
 */