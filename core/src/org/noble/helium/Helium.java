package org.noble.helium;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL32;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.utils.ScreenUtils;
import org.noble.helium.handling.LevelHandler;
import org.noble.helium.subsystems.world.World;
import org.noble.helium.handling.TextureHandler;
import org.noble.helium.subsystems.ui.UserInterface;
import org.noble.helium.subsystems.input.InputProcessing;
import org.noble.helium.rendering.HeliumModelBatch;
import org.noble.helium.subsystems.Subsystem;

import java.util.ArrayList;
import java.util.Objects;

public class Helium extends Game {
  private State m_state;
  private WindowMode m_windowMode;
  private float m_delta;
  private String m_windowTitle;
  private Color m_backgroundColor;
  private static Helium m_instance;
  private final ArrayList<Subsystem> m_subsystems;
  private HeliumModelBatch m_modelBatch;
  private LevelHandler m_levelHandler;
  private Environment m_environment;

  private Helium() {
    m_subsystems = new ArrayList<>();
  }

  public static Helium getInstance() {
    if(m_instance == null) {
      m_instance = new Helium();
    }
    return m_instance;
  }

  public HeliumModelBatch getModelBatch() {
    if(m_modelBatch == null) {
      m_modelBatch = new HeliumModelBatch();
    }
    return m_modelBatch;
  }

  public Environment getEnvironment() {
    if(m_environment == null) {
      //TODO: Depending on context, return a different type of environment?
      m_environment = new Environment();
      m_environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1.0f));
      m_environment.add(new DirectionalLight().set(0.8f,0.8f,0.8f,-1f,-0.8f,-0.2f));
    }
    return m_environment;
  }

  public WindowMode getWindowMode() {
    return m_windowMode;
  }

  public State getState() {
    return m_state;
  }

  public float getDelta() {
    return m_delta;
  }

  public float getFPS() {
    return Gdx.graphics.getFramesPerSecond();
  }

  public Color getBackgroundColor() {
    return m_backgroundColor;
  }

  @Override
  public void create() {
    HeliumIO.println("Telemetry", "Warnings look like this", HeliumIO.printType.WARNING);
    HeliumIO.println("Telemetry", "Errors look like this", HeliumIO.printType.ERROR);
    setBackgroundColor(Color.BLACK);
    setWindowMode(WindowMode.WINDOWED);

    SystemInformation.getInstance();
    m_levelHandler = LevelHandler.getInstance();
    m_subsystems.add(World.getInstance());
    m_subsystems.add(InputProcessing.getInstance());
    m_subsystems.add(UserInterface.getInstance());

    m_modelBatch = new HeliumModelBatch();
    HeliumIO.println(Constants.Engine.k_prettyName, "Ready to render!");
  }

  @Override
  public void render() {
    m_delta = Gdx.graphics.getDeltaTime();
    setTitle(Constants.Engine.k_prettyName + " - " + m_levelHandler.getLevelName() + " - " + getState());

    if(getModelBatch().isWorking()) {
      getModelBatch().end();
      HeliumIO.println("Renderer", "Model batch was not ended last cycle", HeliumIO.printType.ERROR);
    }

    Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    Gdx.gl.glClear(GL32.GL_COLOR_BUFFER_BIT | GL32.GL_DEPTH_BUFFER_BIT);
    ScreenUtils.clear(getBackgroundColor());

    super.render();
    m_subsystems.forEach(Subsystem::update);
  }

  public void setWindowMode(WindowMode windowMode) {
    switch(windowMode) {
      case WINDOWED -> Gdx.graphics.setWindowedMode(1280, 720);
      case FULLSCREEN -> Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
      case MAXIMIZED -> Gdx.graphics.setWindowedMode(Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);
    }

    m_windowMode = windowMode;
  }

  public void setState(State state) {
    if(state == m_state) {
      return;
    }

    m_state = state;

    switch(m_state) {
      case PLAY -> Gdx.input.setCursorCatched(true);
      case PAUSE -> Gdx.input.setCursorCatched(false);
    }

    HeliumIO.println("Helium", "Game state set to " + state);
  }

  public void setBackgroundColor(Color color) {
    m_backgroundColor = color;
  }

  public void setTitle(String title) {
    if(!Objects.equals(title, m_windowTitle)) {
      m_windowTitle = title;
      Gdx.graphics.setTitle(m_windowTitle);
    }
  }

  public enum State {
    PLAY, PAUSE
  }

  public enum WindowMode {
    WINDOWED, BORDERLESS, FULLSCREEN, MAXIMIZED
  }

  @Override
  public void dispose() {
    TextureHandler.getInstance().clear();
    World.getInstance().clear();
    m_subsystems.forEach(Subsystem::dispose);
    m_levelHandler.dispose();
  }
}
