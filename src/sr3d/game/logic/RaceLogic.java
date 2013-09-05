/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sr3d.game.logic;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.control.VehicleControl;
import com.jme3.cinematic.MotionPath;
import com.jme3.effect.ParticleEmitter;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.SceneGraphVisitor;
import com.jme3.scene.Spatial;
import com.jme3.system.Timer;
import com.jme3.ui.Picture;
import de.lessvoid.nifty.Nifty;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import sr3d.factory.GameFactory;
import sr3d.factory.MotionPaths;
import sr3d.controls.screen.PauseState;
import sr3d.controls.screen.StartScreenControllerState;
import sr3d.game.types.RaceObjects;
import sr3d.controls.screen.nextLevelState;
import sr3d.controls.vehicle.AutomaticVehicleControl;
import sr3d.controls.vehicle.CheckPointManager;
import sr3d.controls.vehicle.PlayerNavControl;
/**
 *
 * @author Vusman
 */
public class RaceLogic extends AbstractAppState
implements ActionListener,PhysicsCollisionListener{
private Node rootNode,guiNode;
Node localGuiNode=new Node("logicGui");
private AppStateManager stateManager;
private InputManager inputManager;
private AssetManager assetManager;
private SimpleApplication app;
private VehicleControl player_con;
private VehicleControl enemy_con;
private Camera cam;
String playerName;
int count;
Future playerLocFuture,positionFuture;
 NiftyJmeDisplay   niftyDisplay ;
 ViewPort niftyView;
 Nifty nifty;
 PauseState pause;
BulletAppState physics;
Spatial player;
 AudioNode crash;
Spatial enemy;ParticleEmitter smoke;
Vector3f playerloc = null;
Vector3f player_target=new Vector3f();
RaceObjects gameObjects;
float speed=0,lap1time=0f,lap2time=0f;
Spatial racingTrack;  ;
RigidBodyControl track_con;
nextLevelState nextLevel;
boolean setLap=false;
boolean clear=false;
List<Geometry>checkPoints=new ArrayList<Geometry>();
HashSet<Geometry> lap1PlayerPoints =new HashSet<Geometry>(); 
HashSet<Geometry> lap2PlayerPoints =new HashSet<Geometry>();
HashSet<Geometry> lap1EnemyPoints =new HashSet<Geometry>();
HashSet<Geometry> lap2EnemyPoints =new HashSet<Geometry>();
Timer timer;
float startTime,stopTime,time;
boolean running=true;
PlayerNavControl input;
int enemiesDefeated=0;
Trigger next_key=new KeyTrigger(KeyInput.KEY_S); 
Trigger pause_key=new KeyTrigger(KeyInput.KEY_P); 
Trigger restart=new KeyTrigger(KeyInput.KEY_R);
Trigger exit=new KeyTrigger(KeyInput.KEY_ESCAPE);
Picture one,two,three,go;
boolean lapFinished=false;
GameFactory factory;
int player_checkpts=0;
int enemy_checkpts;
String[] car=new String[5];
boolean gameFinished,reached;
Vector3f enemy_location,player_location;
BitmapFont guiFont;
BitmapText speedText ,timeText,locText,countText,lapText,posText,ptText;
int player_index=0;
int enemy_index;
int player_lap=1;
     int enemy_lap;
MotionPath path;
GameMath math;
int position=0;
StartScreenControllerState screenState=new StartScreenControllerState();
int gameLevel=0;
Vector3f enemy_dir;
Vector3f enemy_target;
int defeated=0;
float player_distance=0,enemy_distance=0;
AutomaticVehicleControl auto_con;
MotionPaths paths=new MotionPaths();
 ScheduledThreadPoolExecutor executor ;
CheckPointManager playerManager;
CheckPointManager enemyManager;

public  RaceLogic(){
     pause=new PauseState();
     math=new GameMath();
     factory=new GameFactory();
}
    @Override
    public void initialize(AppStateManager stateManager,Application app){
        super.initialize(stateManager, app);
        this.app=(SimpleApplication)app;
        this.stateManager=stateManager;
       this.cam=this.app.getCamera();
        this.rootNode=this.app.getRootNode();
        this.guiNode=this.app.getGuiNode();
        this.assetManager=this.app.getAssetManager();
        this.timer=this.app.getTimer();
        this.inputManager=this.app.getInputManager();
       physics=stateManager.getState(BulletAppState.class);
       physics.getPhysicsSpace().addCollisionListener(this);
         gameObjects=this.stateManager.getState(RaceObjects.class);
  
        
        guiNode.attachChild(localGuiNode);


car[1]="Porsche";
car[2]="AudiR8";
car[3]="Bugatti";
car[0]="Car1";

path=paths.getPath(gameLevel);
  guiFont =assetManager.loadFont("Interface/Fonts/VirtualDJ.fnt");
    
      locText =factory.getText("lap", cam, guiFont);
    localGuiNode.attachChild(locText); 
     
       lapText =factory.getText("laptime", cam, guiFont);
      localGuiNode.attachChild(lapText);     
  
       countText =factory.getText("count", cam, guiFont);
      localGuiNode.attachChild(countText);    
      
      posText=factory.getText("position", cam, guiFont);
   localGuiNode.attachChild(posText);
      
         ptText =factory.getText("points", cam, guiFont);
 localGuiNode.attachChild(ptText);
      
 crash=new AudioNode(assetManager,"Sounds/crash.wav");
         executor = new ScheduledThreadPoolExecutor(3); 
    initPositions(); 
initKeys();
    }
 public void initKeys(){
 inputManager.addMapping("pause", pause_key);
inputManager.addListener(this, "pause");  
inputManager.addMapping("restart", restart);
inputManager.addListener(this, "restart");
inputManager.addMapping("exit", exit);
inputManager.addListener(this, "exit");
}   
public void setLevel(int level){
    this.gameLevel=level;
}
    public void initPositions(){  
        if(rootNode!=null){
            rootNode.depthFirstTraversal(new SceneGraphVisitor(){

           public void visit(Spatial spat) {
               if(spat!=null){
              if(spat.getName().equals("player")){
                 player=spat;
                 player_con=spat.getControl(VehicleControl.class);
              }
               if(spat.getName().equals("enemy")){
                 enemy=spat;
                 enemy_con=spat.getControl(VehicleControl.class);
                 
              }             
                }     
           }
            });
        }   
 playerManager=new CheckPointManager(player_con,path);
 playerManager.setCheckRadius(8);
enemyManager=new CheckPointManager(enemy_con,path); 
enemyManager.setCheckRadius(8);
this.stateManager.attach(playerManager);
        this.stateManager.attach(enemyManager);
    }
   public void setLap1Time(Float time1){
 if(!lapFinished) {
         this.lap1time=time1;
    
     }
     lapFinished=true;
 }
 public void setLap2Time(Float time2){
   if(!setLap) {
         this.lap2time=time2;
           
     }
 setLap=true;
 }

  public void setName(String name){
    this.playerName=name;
}
public void upDateCheckPoints(){  
player_index=playerManager.getCurrentWayPoint();
enemy_index=enemyManager.getCurrentWayPoint();
}

     public void upDatePositions(){
if(positionFuture==null){
    positionFuture=executor.submit(new Callable(){

        public Object call() throws Exception {
           enemy_location=enemy_con.getPhysicsLocation();
             player_location=player_con.getPhysicsLocation();

            position= math.findPlayerPosition(path, enemy_index, player_index, enemy_location,player_location, enemy_lap, player_lap);

posText.setText("Pos "+position+"/2");
            return null;
        }
        
    });
}
        else if(positionFuture!=null){
     if(positionFuture.isDone()){
         positionFuture=null;
     }
 }
             
     } 
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if(enemy!=null){
            auto_con=enemy.getControl(AutomaticVehicleControl.class);
        }
       
        if(!enabled){
           if(crash.getStatus()==AudioNode.Status.Playing) {
               crash.pause();
           }
           stateManager.attach(pause);
            input.setEnabled(enabled);
            physics.setEnabled(enabled);
            physics.getPhysicsSpace().removeCollisionListener(this);
            if(auto_con!=null){
             auto_con.setEnabled(enabled); 
             
            }
   
            stateManager.getState(RaceObjects.class).setEnabled(enabled);
        }
        else if(enabled){
           stateManager.detach(pause);
            if(crash.getStatus()==AudioNode.Status.Paused){
                crash.playInstance();
            }
                 input.setEnabled(enabled); 
            physics.setEnabled(enabled);
            physics.getPhysicsSpace().addCollisionListener(this);
            if(auto_con!=null){
                   auto_con.setEnabled(enabled);
            }
              stateManager.getState(RaceObjects.class).setEnabled(enabled);
        }
    }
    @Override
     public void update(float tpf){
        upDateCheckPoints();
upDatePositions();
if(!clear){
   upDateLaps();  
}

    ptText.setText("CheckPoints Cleared"+"\nRhymez "+enemy_index+"\n"+playerName+" "+player_index);           
    }
   
    public void collision(PhysicsCollisionEvent event) {
      
     crash.setPitch(2f);
     crash.setVolume(.25f);
        if(event.getNodeA().getName().equals("player")){    
          crash.playInstance();  
      } 
    }

 public Vector3f getVehicleLocation(){
 
            return        player_con.getPhysicsLocation();

}    
 
     private void upDateLaps()  {

          player_lap=playerManager.getCurrentLap();
 enemy_lap=enemyManager.getCurrentLap();

 if(player_lap==1){
       locText.setText("Lap2/2");
       setLap1Time(timer.getTimeInSeconds()-16);
       lapText.setText("Lap1 time "+math.convertToMinutes(lap1time));
   }
  else if(player_lap==2){
            
              player_con.brake(100);
             
       setLap2Time(timer.getTimeInSeconds()-16-lap1time);
        lapText.setText("Lap1 time "+math.convertToMinutes(lap1time)+
                "\nLap2 time "+math.convertToMinutes(lap2time));

if(position==1&&!clear){
     if(defeated<3) {
                gameObjects.setLap1Time(lap1time);
       gameObjects.setLap2Time(lap2time);
           countText.setText("Press s to go to\n next opponent");
                inputManager.addMapping("next", next_key);
inputManager.addListener(this,"next");
       }
      else if(defeated==3) {
           countText.setText("Finished");
       } 
       clear=true;
}
else if(position==2&&!clear){
    countText.setText("lost !press r to retry");
    clear=true;
}     
    

}
    
        }      
    @Override
 public void cleanup(){
inputManager.removeListener(this);
      stateManager.detach(playerManager);
      stateManager.detach(enemyManager);
        localGuiNode.detachChild(ptText);
        localGuiNode.detachChild(posText);
       localGuiNode.detachChild(lapText);
       localGuiNode.detachChild(locText);
      guiNode.detachChild(localGuiNode);
   localGuiNode.detachChild(countText);
physics.getPhysicsSpace().removeCollisionListener(this);
executor.shutdown();
this.lapFinished=false;
this.setLap=false;
executor.shutdown();
 }

    public void onAction(String name, boolean isPressed, float tpf) {
       
        if ((name.equals("next"))&&(!isPressed)) {
                  stateManager.detach(this);
                 
          gameObjects.endStage(true);
      
    
          }
      if ((name.equals("pause"))&&(!isPressed)) {
            
            if(running){
                running=false;
                countText.setText("paused ,press p to resume");
            }
            else if(!running){
                running=true;
                countText.setText("");
            }
            setEnabled(running);
          }
          if(name.equals("restart")){
          stateManager.detach(this);
          
              gameObjects.endStage(false);
          }
           if(name.equals("exit")){
              try{
              stateManager.detach(this);
           stateManager.detach(gameObjects);
              stateManager.attach(screenState);
              }
              catch(Exception ex){
                  
              }
          } 
    }
}