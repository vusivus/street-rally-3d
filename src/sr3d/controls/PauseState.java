/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sr3d.controls.screen;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioRenderer;
import com.jme3.input.InputManager;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 *
 * @author Vusman
 */
public class PauseState extends AbstractAppState 
implements ScreenController{
  ViewPort niftyView; 
  SimpleApplication app;
  Camera cam;
  NiftyJmeDisplay niftyDisplay;
  AssetManager assetManager;
  InputManager inputManager;
  AudioRenderer audioRenderer;
  Nifty nifty;
  ViewPort guiViewPort;
  AppStateManager stateManager;
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app=(SimpleApplication)app;
        this.stateManager=stateManager;
        this.assetManager=this.app.getAssetManager();
        this.audioRenderer=this.app.getAudioRenderer();
       this.cam=this.app.getCamera();
        this.guiViewPort=this.app.getGuiViewPort();
       Camera camera=cam.clone();      
 niftyView=this.app.getRenderManager().createMainView("nifty", camera);
       
 niftyDisplay  =  new NiftyJmeDisplay(assetManager,inputManager,audioRenderer,niftyView);  
 nifty=niftyDisplay.getNifty();
 nifty.fromXml("Interface/Nifty/pauseScreen.xml", "start",this);
niftyView.addProcessor(niftyDisplay);
    }
    
    @Override
    public void update(float tpf) {
        //TODO: implement behavior during runtime
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        nifty.exit();
       niftyView.removeProcessor(niftyDisplay);
    }

    public void bind(Nifty nifty, Screen screen) {
     
    }

    public void onStartScreen() {
     
    }

    public void onEndScreen() {

    }
}
