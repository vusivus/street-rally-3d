/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sr3d.controls.screen;

import sr3d.game.types.RaceObjects;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioRenderer;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.control.VehicleControl;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.ChaseCamera;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import com.jme3.ui.Picture;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.EffectBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.PopupBuilder;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.effects.impl.Move;
import sr3d.factory.GameFactory;

/**
 *
 * @author Sanet
 */
public class StartScreenControllerState extends AbstractAppState 
implements ScreenController{
 private AssetManager assetManager;
 private InputManager inputManager ;
 private Camera cam;
 Spatial  floor;
 private AudioRenderer audioRenderer;
 private ViewPort viewPort;
 Node guiNode,rootNode;
 Node localRootNode=new Node("startRoot");
 AudioNode audio;
  RigidBodyControl control;
  DirectionalLight dl;
  AmbientLight ai;
 private SimpleApplication app;
 RaceObjects gameState;
 GameFactory factory;
 String[]car_names=new String[]{"Car1","Porsche","Hummer","BMW M3"};
 Spatial[]cars=new Spatial[4];
 VehicleControl []car_con=new VehicleControl[4];
 Nifty nifty;
 Quaternion rot=new Quaternion();
 ViewPort guiViewPort;
 PhysicsSpace space;
  String playerName;
 TextField textfield;
 Element popup;
 ListBox settingsList;
int index=0;
Integer[] width=new Integer[]{
640,800,864,960,1024    
};
Integer[] height=new Integer[]{
480,600,648,720,768    
};
 ViewPort carView;
  NiftyJmeDisplay  niftyDisplay;
 private AppStateManager stateManager;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app=(SimpleApplication)app;
        this.assetManager=this.app.getAssetManager();
        this.inputManager=this.app.getInputManager();
        this.cam=this.app.getCamera();
        this.stateManager=stateManager;
        this.guiNode=this.app.getGuiNode();
        this.rootNode=this.app.getRootNode();
        this.audioRenderer=this.app.getAudioRenderer();
        this.viewPort=this.app.getViewPort();
        this.space=this.stateManager.getState(BulletAppState.class).getPhysicsSpace();
        this.guiViewPort=this.app.getGuiViewPort();
        
        rootNode.attachChild(localRootNode);
     
factory=new GameFactory();    
gameState=new RaceObjects();


   niftyDisplay  =  new NiftyJmeDisplay(assetManager,inputManager,audioRenderer,viewPort);  
   nifty=niftyDisplay.getNifty();
nifty.fromXml("Interface/Nifty/startscreen.xml","start",this);
guiViewPort.addProcessor(niftyDisplay);     
     
         audio=new AudioNode(this.app.getAssetManager(),"Sounds/default.wav");
audio.play();
audio.setLooping(true);
 
  textfield=nifty.getScreen("name").findNiftyControl("name", TextField.class);

  settingsList=nifty.getScreen("settings").findNiftyControl("resolutions", ListBox.class);
settingsList.addItem("640*480 32bpp");
settingsList.addItem("800*600 32bpp");
settingsList.addItem("864*648 32bpp");
settingsList.addItem("960*720 32bpp");
settingsList.addItem("1024*768 32bpp");

  popup=nifty.createPopup("popupExit");
    }

    
    public void closePopup(){
        nifty.closePopup(popup.getId());
    }
public void showPopup(){
    nifty.showPopup(nifty.getCurrentScreen(), popup.getId(),null);
}
        
    public void nextScreen(String name){
        if(name.equals("controls")){
            nifty.gotoScreen(name);
        }
         if(name.equals("name")){
            nifty.gotoScreen(name);
        }
      if(name.equals("start")){
            nifty.gotoScreen(name);
            space.remove(car_con[index]);
     localRootNode.detachChild(cars[index]);
     localRootNode.removeLight(ai);
     localRootNode.removeLight(dl);
     localRootNode.detachChild(floor);
        } 
      if(name.equals("settings")){
            nifty.gotoScreen(name);
        }
       if(name.equals("credits")){
            nifty.gotoScreen(name);
        }
         if(name.equals("startMenu")){
            nifty.gotoScreen("start");

        } 
         if(name.equals("carSelect")){
              playerName=textfield.getText();
              
       
       if(!"".equals(playerName)){
             nifty.gotoScreen(name);
             showCar();
       }
          
        }
       if(name.equals("startSettings")){
            int res=settingsList.getFocusItemIndex();
            boolean fullScreen=true;
            boolean vSync=true;
           AppSettings settings=new AppSettings(true);
           settings.setFullscreen(fullScreen);
           settings.setVSync(vSync);
         settings.setResolution(width[res], height[res]);
         app.setSettings(settings);
         nifty.gotoScreen("start");
        }    
    } 
    public void showCar(){
        for(int i=0;i<4;i++){
            cars[i]=factory.getCar(car_names[i], assetManager);
            car_con[i]=cars[i].getControl(VehicleControl.class);
            
        }
        Camera camera=cam.clone();
      
        camera.setViewPort(.25f, .9f, .1f, .75f);
     carView=this.app.getRenderManager().createPostView("carview",camera );
     
        space.add(car_con[index]);
         dl=new DirectionalLight();
        localRootNode.addLight(dl);
        ai=new AmbientLight();
        localRootNode.addLight(ai);
        car_con[index].setPhysicsLocation(new Vector3f(0,1,0));
        localRootNode.attachChild(cars[index]);
       floor=assetManager.loadModel("Models/garage/garage.mesh.j3o");
      control=new RigidBodyControl(0);
      floor.addControl(control);
      control.setPhysicsLocation(Vector3f.ZERO);
      space.add(control);
      localRootNode.attachChild(floor);
    
      camera.setLocation(car_con[index].getPhysicsLocation().add(new Vector3f(3,1f,0)));
      camera.lookAt(car_con[index].getPhysicsLocation().add(new Vector3f(0,-1,0)), Vector3f.UNIT_Y);
        dl.setDirection(camera.getDirection());
       
    carView.attachScene(localRootNode);
    
    }
    public void nextCar(){
     if(index==3){
         index=0;
         localRootNode.detachChild(cars[3]);
         space.remove(car_con[3]);
         localRootNode.attachChild(cars[index]);
         space.add(car_con[index]);
     }
     else if(index!=3){
         localRootNode.detachChild(cars[index]);
         space.remove(car_con[index]);
         ++index;
         localRootNode.attachChild(cars[index]);
         space.add(car_con[index]);
     }
  
 
    }
    @Override
    public void update(float tpf) {
      Quaternion rot=new Quaternion().fromAngleAxis(FastMath.QUARTER_PI*tpf, Vector3f.UNIT_Y);
     if(control!=null){
        Quaternion angle=control.getPhysicsRotation();
        Quaternion rotation=rot.mult(angle).normalizeLocal();
      control.setPhysicsRotation(rotation);
      car_con[index].setPhysicsRotation(rotation);
     }
      
    }
    public void startGame(){
      space.remove(car_con[index]);
     localRootNode.detachChild(cars[index]);    
    localRootNode.detachChild(floor);
      carView.detachScene(localRootNode);
      stateManager.detach(this);
 gameState.setName(playerName);
 gameState.setCar(car_names[index]);
    gameState.setLevel(1,false);
    gameState.setDefeatedCars(0);
        stateManager.attach(gameState);  
    }
  public void quit(){
    System.exit(1);
} 
    @Override
    public void cleanup() {
    localRootNode.removeLight(ai);
     localRootNode.removeLight(dl);
   
    
       this.app.getTimer().reset();   
       rootNode.detachChild(localRootNode);
viewPort.removeProcessor(niftyDisplay);
audio.stop();
nifty.exit();
 cam.setRotation(Quaternion.IDENTITY);
  cam.setLocation(Vector3f.ZERO);
cam.setViewPort(0, 1,0, 1);
   super.cleanup(); 
    }

   

    public void bind(Nifty nifty, Screen screen) {
        
    }

    public void onStartScreen() {
      
    }

    public void onEndScreen() {

}
}