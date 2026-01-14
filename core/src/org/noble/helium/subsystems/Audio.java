package org.noble.helium.subsystems;

import games.rednblack.miniaudio.MASound;
import games.rednblack.miniaudio.MiniAudio;
import org.noble.helium.Helium;
import org.noble.helium.HeliumIO;

import java.util.ArrayList;

public class Audio extends Subsystem {
  private static Audio m_instance = null;
  private boolean m_isEnabled;
  private final MiniAudio m_miniAudio;
  private final ArrayList<MASound> m_queuedSounds;
  private final ArrayList<MASound> m_instantSounds;

  private Audio() {
    m_miniAudio = new MiniAudio();
    m_isEnabled = true;
    m_queuedSounds = new ArrayList<>();
    m_instantSounds = new ArrayList<>();
    HeliumIO.println("Audio", "Audio subsystem initialized");
  }

  public static Audio getInstance() {
    if(m_instance == null) {
      m_instance = new Audio();
    }
    return m_instance;
  }

  public boolean isEnabled() {
    return m_isEnabled;
  }

  public void playSound(String sound) {
    m_instantSounds.add(m_miniAudio.createSound("assets/audio/" + sound));
  }

  public void queueSound(String sound) {
    m_queuedSounds.add(m_miniAudio.createSound("assets/audio/" + sound));
  }

  public void queueSounds(String[] sounds) {
    for(String sound : sounds) {
      queueSound(sound);
    }
  }

  @Override
  public void update() {
    //Handle sounds that are supposed to play one-after-another
    if(!m_queuedSounds.isEmpty()) {
      if(m_queuedSounds.get(0).isEnd()) {
        m_queuedSounds.get(0).dispose();
        m_queuedSounds.remove(0);
      } else if(!m_queuedSounds.get(0).isPlaying()) {
        m_queuedSounds.get(0).play();
      }
    }

    //Handle instantaneous sounds
    for(MASound sound : m_instantSounds) {
      if(sound.isEnd()) {
        sound.dispose();
        m_instantSounds.remove(sound);
        break;
      } else if (!sound.isPlaying()) {
        sound.play();
      }
    }

    Helium.State gameState = Helium.getInstance().getState();
    if(gameState == Helium.State.PAUSE && isEnabled()) {
      m_isEnabled = false;
      m_miniAudio.stopEngine();
    } else if (gameState == Helium.State.PLAY && !isEnabled()) {
      m_isEnabled = true;
      m_miniAudio.startEngine();
    }
  }

  @Override
  public void dispose() {
    m_miniAudio.dispose();
  }
}
