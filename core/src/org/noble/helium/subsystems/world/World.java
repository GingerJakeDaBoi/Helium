package org.noble.helium.subsystems.world;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;
import org.noble.helium.Helium;
import org.noble.helium.HeliumIO;
import org.noble.helium.actors.PlayerController;
import org.noble.helium.rendering.HeliumModelBatch;
import org.noble.helium.subsystems.Subsystem;

import java.util.ArrayList;

public class World extends Subsystem {
  private static World m_instance;
  private final ArrayList<WorldObject> m_objects;

  private World() {
    m_objects = new ArrayList<>();
    HeliumIO.println("Object Handler", "Object handler initialized");
  }

  public static World getInstance() {
    if(m_instance == null) {
      m_instance = new World();
    }
    return m_instance;
  }

  public void add(WorldObject object) {
    m_objects.add(object);
  }

  public ArrayList<WorldObject> getAllObjects() {
    return m_objects;
  }

  public void clear() {
    for(WorldObject object : m_objects) {
      object.dispose();
    }
    m_objects.clear();
  }
  @Override
  public void update() {
    HeliumModelBatch batch = Helium.getInstance().getModelBatch();
    Camera camera = PlayerController.getInstance().getCamera();
    Environment environment = Helium.getInstance().getEnvironment();

    batch.begin(camera);

    for(WorldObject object : m_objects) {
      object.update();
      if(object.shouldRender()) {
        batch.render(object, environment);
      }
    }

    batch.end();
  }

  @Override
  public void dispose() {

  }
}
