/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sr3d.controls.vehicle;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.control.VehicleControl;
import com.jme3.cinematic.MotionPath;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import java.io.IOException;
import sr3d.game.logic.GameMath;

/**
 *
 * @author Vusman
 */
public class PlayerNavControl extends AbstractAppState 
implements ActionListener{
InputManager inputManager;
MotionPath path;
GameMath math=new GameMath();
VehicleControl car_con;
Vector3f target;
AssetManager assetManager;
AudioNode acc,brake,turn,engine;
CheckPointManager checkPoint;
Trigger acc_key=new KeyTrigger(KeyInput.KEY_UP);
Trigger brake_key=new KeyTrigger(KeyInput.KEY_DOWN);
Trigger left_key=new KeyTrigger(KeyInput.KEY_LEFT);
Trigger right_key=new KeyTrigger(KeyInput.KEY_RIGHT);
Trigger reverse_key=new KeyTrigger(KeyInput.KEY_RSHIFT);
  Quaternion dir=new Quaternion();
  SimpleApplication app;
 float brakeForce =40.0f,accelerationForce=200.0f,reverseValue=0,steeringValue=0, steerConstant=.15f,accelerationValue=0;
public PlayerNavControl(VehicleControl vehicle,MotionPath path){
car_con=vehicle;
    this.path=path;
}

    @Override
    public void initialize(AppStateManager stateManager,Application app){
    super.initialize(stateManager, app);
    this.app=(SimpleApplication)app;
    inputManager=this.app.getInputManager();
    assetManager=this.app.getAssetManager();
    
              inputManager.addMapping("sright", new KeyTrigger(KeyInput.KEY_D));
         inputManager.addMapping("sleft", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("acc", acc_key);
             inputManager.addMapping("brake", brake_key);
      inputManager .addMapping("left", left_key);       
             inputManager.addMapping("right", right_key);
      inputManager.addMapping("reset", new KeyTrigger(KeyInput.KEY_Q));      
  inputManager.addMapping("reverse", new KeyTrigger(KeyInput.KEY_SPACE));
       inputManager.addListener(this, new String[]{
            "acc","brake","left","right","reset","reverse","sleft","sright"
        }); 
  acc=new AudioNode(assetManager,"Interface/Nifty/sounds/sound.wav");
  acc.setLooping(true);
   engine=new AudioNode(assetManager,"Sounds/sports car engine.wav");
 engine.setLooping(true);
 turn=new AudioNode(assetManager,"Sounds/Brake.wav");
 brake=new AudioNode(assetManager,"Sounds/turn1.wav");     
}

    @Override
    public void update(float tpf) {
   
    float speed=car_con.getCurrentVehicleSpeedKmHour();
    if(speed>4){
        engine.play();
    }
    else if(speed<4){
        engine.stop();
    }
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
    if(name.equals("left")){
  if(isPressed) {
         steeringValue +=steerConstant; 
       turn.play();
     } else {
    turn.stop();
      steeringValue -=steerConstant;
  }
car_con.steer(steeringValue);
    }
 if(name.equals("right")){
  if(isPressed) {
     turn.play();
         steeringValue -=steerConstant;
     } else {
    
      turn.stop();
 
      steeringValue +=steerConstant;
  }
car_con.steer( steeringValue);
    }
 if(name.equals("brake")){
  if(isPressed) {
       car_con.brake(brakeForce);
      brake.play();
     } else {
      car_con.brake(0);
     brake.stop();
  }    
    }
 if(name.equals("acc")){
  if(isPressed) { 
        accelerationValue +=accelerationForce;
         
     } else {
     accelerationValue -=accelerationForce;

  }
       car_con.accelerate(accelerationValue);  
    }
 if(name.equals("reset")){
     if(isPressed){  
int currentWaypoint=math.findClosestWayPoint(car_con.getPhysicsLocation(), path);
   int nextWayPoint =currentWaypoint+1;
   if(nextWayPoint>=path.getNbWayPoints()-1){
       nextWayPoint=0;
   }
   Vector3f vector1=path.getWayPoint(currentWaypoint).normalize();
   Vector3f vector2=path.getWayPoint(nextWayPoint).normalize();
 Vector3f dire=vector2.subtract(vector1).normalize();
   Vector3f loc=car_con.getPhysicsLocation().normalize();
   float dot= dire.dot(loc); 
   float angle=dot/loc.length()*dire.length();
   dir.fromAngleAxis(FastMath.acos(angle), Vector3f.UNIT_Y);
  
 car_con.setPhysicsLocation(path.getWayPoint(currentWaypoint));
    car_con.setAngularVelocity(Vector3f.ZERO);
    car_con.resetSuspension();
    car_con.setPhysicsRotation(dir);
    car_con.setLinearVelocity(Vector3f.ZERO);   
     }
 }
 if(name.equals("reverse")){  
     car_con.setLinearVelocity(Vector3f.ZERO);
     if(isPressed){
        reverseValue-=200;
     }
     else{
         reverseValue+=200;
     }
     car_con.accelerate(reverseValue);
 }
 if(name.equals("sleft")){
  if(isPressed) {
         steeringValue +=.5f; 
     turn.play();
     } else {
    turn.stop();
      steeringValue -=.5f;
  }
    car_con.steer(steeringValue);

    }
 if(name.equals("sright")){
  if(isPressed) {
    
         steeringValue -=.5f;
     } else {
     
      steeringValue +=.5f;
  }
  car_con.steer(steeringValue);
    }  
    }
    @Override
 public void cleanup(){       
        if(brake!=null){
         
            brake.stop();
       
        }
        if(engine!=null){
            
           engine.stop();
       
        }
       if(turn!=null){
        
            turn.stop();
      
       }
        
        inputManager.removeListener(this);
 
    
 }
    public void setSteeringValue(float steeringValue){
      this.steerConstant=steeringValue;  
    }
  public void setAccForce(float accForce){
      this.accelerationForce=accForce;
  } 
}

