package org.noble.helium.screens;

import org.noble.helium.actors.PlayerController;

public abstract class HeliumLevel extends BaseScreen {
  public boolean m_hasInit = false;
  public HeliumLevel() {
    super();
  }
  public void init() {
    PlayerController.getInstance().reset();
    m_hasInit = true;
  }

  public String getSimpleName() {
    return getClass().getSimpleName();
  }

  public void render(float delta) {
    if(!m_hasInit) {
      init();
    }
    super.render(delta);
    m_batch.end();
  }
}
