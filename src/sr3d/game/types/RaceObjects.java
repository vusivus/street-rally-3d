/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sr3d.game.types;

import sr3d.game.logic.RaceLogic;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.control.VehicleControl;
import com.jme3.effect.ParticleEmitter;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.ChaseCamera;
import com.jme3.input.InputManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.Timer;
import com.jme3.ui.Picture;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.SizeValue;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;
import sr3d.factory.GameFactory;
import sr3d.factory.MotionPaths;
import sr3d.controls.screen.StartScreenControllerState;
import sr3d.controls.vehicle.VehicleControlValues;
import sr3d.controls.screen.nextLevelState;
import sr3d.controls.vehicle.AutomaticVehicleControl;
import sr3d.controls.vehicle.PlayerNavControl;
import sr3d.game.logic.GameMath;

/**
 *
 * 
 */
public class RaceObjects extends AbstractAppState 
implements Savable,ScreenController{
NiftyJmeDisplay display;
Nifty nifty;
Element progressBar;
boolean loading =false;
Future loadFuture;
TextRenderer textRenderer;
 AssetManager assetManager;
 Node localRootNode=new Node("gameRoot");
 Node localGuiNode=new Node("gameGui");
 Node rootNode,guiNode;
DirectionalLight light;
InputManager inputManager;
 Camera cam;
RaceLogic gameLogic;
 String playerName;
 AppStateManager stateManager;
BulletAppState physics;
Node player;
  Element loadingtext;
Spatial enemy;
String carName;
VehicleControl player_con,enemy_con;
BitmapFont guiFont;
ChaseCamera chase;
BitmapText timeText,speedText;
float lap1time=0f,lap2time=0f,speed;
 Spatial racingTrack,start;  
Material arrow_mat;
RigidBodyControl track_con;
nextLevelState nextLevel;
ScheduledThreadPoolExecutor executor;
ScheduledThreadPoolExecutor exec;
Timer timer;
ViewPort guiViewPort;
GameMath math=new GameMath();
float startTime,stopTime,time;
SimpleApplication app;
PlayerNavControl input;
CharacterControl arrow_con;
Picture one,two,three,go,speedo,frame;
GameFactory factory;
boolean gameFinished;
AmbientLight sun;
String[] car=new String[5];
StartScreenControllerState screenState=new StartScreenControllerState();
AutomaticVehicleControl auto;
int gameLevel;
MotionPaths paths;
AmbientLight al;
int defeated;
ParticleEmitter smoke;
Future speedGet;
TextRenderer label;
 public RaceObjects(){
     factory=new GameFactory();   

gameLogic=new RaceLogic();
paths=new MotionPaths();

} 
 
    @Override
    public void initialize(AppStateManager stateManager,Application app) {
      
        super.initialize(stateManager, app);
        this.app=(SimpleApplication)app;
       this.stateManager=stateManager;
           this.rootNode=this.app.getRootNode();
  this.assetManager=this.app.getAssetManager();
    this.guiNode=this.app.getGuiNode();
this.inputManager=this.app.getInputManager();
    this.cam=this.app.getCamera();
  this.timer=this.app.getTimer();
  this.guiViewPort=this.app.getGuiViewPort();
  this.inputManager=this.app.getInputManager();
   rootNode.attachChild(localRootNode);
   guiNode.attachChild(localGuiNode);

gameLogic.setName(playerName);

car[2]="Porsche";
car[0]="Hummer";
car[1]="BMW M3";
car[3]="Car1";

   

auto=new AutomaticVehicleControl(this.stateManager,paths.getPath(gameLevel),2);
exec=new ScheduledThreadPoolExecutor(3);
executor=new ScheduledThreadPoolExecutor(2); 
physics=this.stateManager.getState(BulletAppState.class);

initLoadScreen();


start=assetManager.loadModel("Models/start/start.j3o");
start.setLocalTranslation(factory.getStartPoint(gameLevel).add(0,1,0));
start.scale(.5f);
localRootNode.attachChild(start);
    } 
    Callable<Void> loadingCallable=new Callable<Void>(){

        public Void call() throws Exception {      
           initScene();
           setProgress(.2f,"loading player car");
initGuiDisplay();
setProgress(.6f,"loading challenger car");
initPlayer(carName);
 input=new PlayerNavControl(player_con,paths.getPath(gameLevel));
setProgress(1f,"complete");
initEnemyCars(car[defeated]);

return null;
        }
        
    };
public void initLoadScreen(){
    display=new NiftyJmeDisplay(assetManager,inputManager,this.app.getAudioRenderer(),this.app.getViewPort());
    nifty=display.getNifty();
    nifty.fromXml("Interface/Nifty/loadingScreen.xml", "start",this);
    guiViewPort.addProcessor(display);
    loading=true;   
   Element element= nifty.getScreen("start").findElementByName("loadingtext");
           label=element.getRenderer(TextRenderer.class);
}


    public void setDefeatedCars(int num){
        this.defeated=num;
    }
    public void setCar(String name){
        this.carName=name;
    }
    public void initPlayer(String car) {

        player=(Node) factory.getCar(car,assetManager);
player.setName("player");
player.setUserData("level", gameLevel);
player_con=player.getControl(VehicleControl.class);
player_con.setPhysicsLocation(factory.getStartPoint(gameLevel));
player.addControl(new VehicleControlValues().cloneForSpatial(player));

chase=new ChaseCamera(cam,player,inputManager);
chase.setDefaultDistance(3);
chase.setDefaultVerticalRotation(FastMath.INV_PI);
chase.setDefaultHorizontalRotation(FastMath.HALF_PI);
chase.setSmoothMotion(true);
chase.setTrailingEnabled(true);
chase.setLookAtOffset(new Vector3f(0,.5f,0));
    }    
public void initEnemyCars(String car){

   enemy=factory.getCar(car, assetManager);
   enemy.setUserData("speed", 200f);
    enemy.setName("enemy");
    enemy.setUserData("level", gameLevel);
      enemy_con=enemy.getControl(VehicleControl.class);
    enemy.addControl(new VehicleControlValues().cloneForSpatial(enemy));
     
    
 enemy_con.setPhysicsLocation((factory.getStartPoint(gameLevel)).add(1.5f,0,0)); 
  
 
}
 public void initScene() {
     racingTrack=factory.getTrack(gameLevel,assetManager);
       racingTrack.setName("track");
  track_con=new RigidBodyControl(0.0f);
             racingTrack.addControl(track_con);   
  light =new DirectionalLight();
  light.setDirection(factory.getStartPoint(gameLevel));
sun=new AmbientLight();

    }
         public void  initGuiDisplay(){

      guiFont =assetManager.loadFont("Interface/Fonts/VirtualDJ.fnt");

   timeText =factory.getText("time", cam, guiFont);
    
  frame=factory.getPic("frame", assetManager, cam);
  
one=factory.getPic("one", assetManager, cam);
two=factory.getPic("two", assetManager, cam);
three=factory.getPic("three", assetManager, cam);
go=factory.getPic("go", assetManager, cam);

 speedo=factory.getPic("speed", assetManager, cam);

  speedText =factory.getText("speed", cam, guiFont);
  
} 
         public void attachObjects(){
             physics.getPhysicsSpace().add(player_con);
 localRootNode.attachChild(player);
 physics.getPhysicsSpace().add(enemy_con);
   localRootNode.attachChild(enemy);
   physics.getPhysicsSpace().add(track_con);
        localRootNode.attachChild(racingTrack);
          localRootNode.addLight(light);
        localRootNode.addLight(sun);
        localGuiNode.attachChild(timeText); 
          localRootNode.attachChild(frame);
           localGuiNode.attachChild(speedText);
          localRootNode.attachChild(speedo);         
          stateManager.attach(gameLogic);
          gameLogic.setLevel(gameLevel);
         }
    public void setName(){
        this.playerName=factory.getName();
    }
   

  public void setLevel(int level,boolean finished){
    this.gameLevel=level;
    this.gameFinished=finished;
  }
public void loading(){
    if(loadFuture==null){
       loadFuture= exec.submit(loadingCallable);
    }
   else if(loadFuture.isDone()){
        nifty.exit();
guiViewPort.removeProcessor(display);
exec.shutdown();
attachObjects();
loading=false;
timer.reset();
    }
}
    @Override
    public void update(float tpf) {  
           if(loading){
               loading();
           }
           else if(!loading){
float times=timer.getTimeInSeconds();
if(times>9&&times<=10){
    localGuiNode.attachChild(three);
}
if(times>11&&times<=12){
  localGuiNode.detachChild(three);
  localGuiNode.attachChild(two);
}
if(times>13&&times<=14){
 localGuiNode.detachChild(two);
  localGuiNode.attachChild(one);
 
}
if(times>15&&times<=16){
 localGuiNode.detachChild(one);
  localGuiNode.attachChild(go);
    stateManager.attach(input);
enemy.addControl(auto);
   
}
if(times>17){
          localGuiNode.detachChild(go);
                       start.rotate(0, FastMath.QUARTER_PI/6f, 0);         
processTime();
    speedText.setText(""+math.roundOff(3*speed, 1));
    }

           }
    }
public void processTime(){
    startTime=  timer.getTimeInSeconds()-16f;
    if(startTime>=0){
        timeText.setText(""+math.convertToMinutes(startTime));
    }
    if(speedGet==null){
   speedGet= executor.submit(new Callable<Float>(){

                public Float call()  {

                 return player_con.getCurrentVehicleSpeedKmHour();
                }

                
            });
    }
    else if(speedGet!=null){
        if(speedGet.isDone()){
            try {               
                 speed=(Float) speedGet.get();
             
                   } catch (InterruptedException ex) {
                       Logger.getLogger(RaceObjects.class.getName()).log(Level.SEVERE, null, ex);
                   } catch (ExecutionException ex) {
                       Logger.getLogger(RaceObjects.class.getName()).log(Level.SEVERE, null, ex);
                   }
            speedGet=null;
    }
}
}
public void setName(String name){
    this.playerName=name;
}
   
    
   @Override
  
    public void cleanup() {
      cam.setRotation(Quaternion.IDENTITY);
      cam.setLocation(Vector3f.ZERO);
      
    try{
      physics.getPhysicsSpace().remove(player_con);
physics.getPhysicsSpace().remove(track_con);
physics.getPhysicsSpace().remove(enemy_con);
    }
    catch (Exception e){
        
    }
executor.shutdown();
  localRootNode.removeLight(sun);
  localRootNode.detachChild(player);
localRootNode.removeLight(light);
localRootNode.detachChild(frame);
     localRootNode.detachChild(start);
     localRootNode.detachChild(racingTrack);
      localGuiNode.detachChild(timeText);
      localRootNode.detachChild(speedo);
      localGuiNode.detachAllChildren();
      guiNode.detachChild(localGuiNode);
      rootNode.detachChild(localRootNode);
        super.cleanup();
    }         

   
       public void setLap1Time(Float time1){

         this.lap1time=time1;
  
 }
 public void setLap2Time(Float time2){
 
         this.lap2time=time2;

 }
public void endStage(boolean finished){
  time= Math.min(lap1time, lap2time);
    stateManager.detach(input);
     nextLevel=new nextLevelState(finished);
      stateManager.detach(this);
      nextLevel.setLevel(gameLevel, time);
      nextLevel.setName(playerName,carName);
     nextLevel.setDefeated(defeated);
     stateManager.attach(nextLevel);
}




    public void write(JmeExporter ex) throws IOException {
 OutputCapsule outPut=ex.getCapsule(this);
 outPut.write(Math.min(lap1time, lap2time), playerName, 0);
    }

    public void read(JmeImporter im) throws IOException {
        InputCapsule inPut=im.getCapsule(this);
        inPut.readFloat(playerName,0);
    }

    public void bind(Nifty nifty, Screen screen) {
      progressBar=nifty.getScreen("start").findElementByName("progressbar");
    }

    public void onStartScreen() {
     
    }

    public void onEndScreen() {
     
    }
   public void setProgress(final float progress,final String loadingText){
      this.app.enqueue(new Callable(){

           public Object call() throws Exception {
              final int MIN_WIDTH=32;
              int pixelWidth=(int) (MIN_WIDTH+(progressBar.getParent().getWidth()-MIN_WIDTH)*progress);
              progressBar.setConstraintWidth(new SizeValue(pixelWidth+"px"));
              progressBar.getParent().layoutElements();
              label.setText(loadingText);
              return null;
              
           }
           
       });
   }
    }

  

 

  
       
 
    

  
 
    
  
   

