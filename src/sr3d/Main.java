package sr3d;

import sr3d.controls.screen.StartScreenControllerState;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;

public class Main extends SimpleApplication {
BulletAppState bulletAppState;
StartScreenControllerState screenState;
    public static void main(String[] args) {
      
        AppSettings settings=new AppSettings(true);
 settings.setFrameRate(30);

      settings.setResolution(800,600);
        settings.setVSync(true);
        settings.setTitle("STREET RALLY 3D");
        Main app = new Main();
        app.setSettings(settings);
    app.setShowSettings(false);
        app.start();
   settings.setUseJoysticks(true);
    }

    @Override
    public void simpleInitApp() {
setDisplayFps(false);
setDisplayStatView(false);
inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_EXIT);
bulletAppState=new BulletAppState();
bulletAppState.setSpeed(30);
bulletAppState.setThreadingType(BulletAppState.ThreadingType.PARALLEL);
stateManager.attach(bulletAppState);
  screenState=new StartScreenControllerState();
stateManager.attach(screenState);
   flyCam.setEnabled(false);
  
    }

    @Override
    public void simpleUpdate(float tpf) {

    }
    
    @Override
    public void simpleRender(RenderManager rm) {  

}

}

  
   

 
 
    

