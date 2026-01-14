package org.noble.helium.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.noble.helium.Helium;
import org.noble.helium.actors.PlayerController;
import org.noble.helium.handling.ActorHandler;
import org.noble.helium.subsystems.World;
import org.noble.helium.handling.TextureHandler;
import org.noble.helium.rendering.HeliumModelBatch;
import org.noble.helium.rendering.HeliumModelBuilder;
import org.noble.helium.subsystems.ui.UserInterface;

public class BaseScreen implements Screen {
  public final Helium m_game;
  public final HeliumModelBatch m_batch;
  public final PlayerController m_player;
  public final TextureHandler m_textureHandler;
  public final HeliumModelBuilder m_modelBuilder;
  public final World m_world;
  public final ActorHandler m_actorHandler;
  private final Viewport m_viewport;

  public BaseScreen() {
    m_game = Helium.getInstance();
    m_batch = m_game.getModelBatch();
    m_player = PlayerController.getInstance();
    m_modelBuilder = HeliumModelBuilder.getInstance();
    m_world = World.getInstance();
    m_actorHandler = ActorHandler.getInstance();
    m_textureHandler = TextureHandler.getInstance();
    m_game.setState(Helium.State.PLAY);
    m_viewport = new ScreenViewport(m_player.getCamera());
  }

  @Override
  public void show() {

  }

  @Override
  public void render(float delta) {
    if(m_game.getState() == Helium.State.PLAY) {
      m_player.update();
      m_actorHandler.update();
    }
  }

  @Override
  public void resize(int x, int y) {
    if(m_player.getWorldObject() == null) {
      return;
    }
    Vector3 playerPos = m_player.getPosition();
    m_viewport.update(x, y, true);
    m_player.setPosition(playerPos);
    UserInterface.getInstance().reset();
  }

  @Override
  public void pause() {
    m_game.setState(Helium.State.PAUSE);
  }

  @Override
  public void resume() {
    m_game.setState(Helium.State.PLAY);
  }

  @Override
  public void hide() {

  }

  @Override
  public void dispose() {
    m_world.clear();
    m_actorHandler.clear();
  }
}
