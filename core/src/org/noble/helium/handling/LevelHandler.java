package org.noble.helium.handling;

import org.noble.helium.Helium;
import org.noble.helium.screens.HeliumLevel;
import org.noble.helium.screens.tests.PhysicsTest;
import org.noble.helium.HeliumIO;
import org.noble.helium.subsystems.world.World;

public class LevelHandler {
  private static LevelHandler m_instance;
  private HeliumLevel m_currentLevel;
  private final Helium m_helium;
  private final World m_world;

  private LevelHandler() {
    m_helium = Helium.getInstance();
    m_world = World.getInstance();
    changeScreen(new PhysicsTest());
    HeliumIO.println("Level Handler","Level handler initialized");
  }

  public static LevelHandler getInstance() {
    if (m_instance == null) {
      m_instance = new LevelHandler();
    }
    return m_instance;
  }

  public String getLevelName() {
    return m_currentLevel.getSimpleName();
  }

  public void dispose() {
    m_currentLevel.dispose();
  }

  public void changeScreen(HeliumLevel level) {
    if (m_currentLevel != null) {
      HeliumIO.println("Level Handler", "Changing screen to " + level.getSimpleName());
    } else {
      HeliumIO.println("Level Handler","Starting game on " + level.getSimpleName());
    }
    TextureHandler.getInstance().clear();
    m_world.clear();
    m_helium.setScreen(level);
    if (m_currentLevel != null) {
      m_currentLevel.dispose();
    }
    m_currentLevel = level;
  }
}