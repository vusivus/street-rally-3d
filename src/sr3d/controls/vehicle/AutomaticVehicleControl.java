/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sr3d.controls.vehicle;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.VehicleControl;
import com.jme3.cinematic.MotionPath;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.FastMath;
import com.jme3.math.Plane;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import java.io.IOException;

/**
 *
 * @author Vusman
 */
public class AutomaticVehicleControl extends AbstractControl 
implements PhysicsCollisionListener{
private VehicleControl vehicle;
float speed=800;
float steer=0;
private SimpleApplication app;
float accelerate=0;
float checkRadius=4;
private Vector3f targetLocation=new Vector3f();
private Vector3f currentLocation=new Vector3f();
private Vector3f targetDirection=new Vector3f();
private Vector3f forwardDirection=new Vector3f();
private Vector3f vector4=new Vector3f();
private Vector3f aimDirection=new Vector3f(Vector3f.UNIT_Z);
private Plane plane=new Plane();
int level;
CheckPointManager checkPoint;
int index=0;
int maxPathPoints;
boolean reached=false;
private MotionPath path;
static final Quaternion ROTATE_RIGHT=new Quaternion().fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_Y);
private boolean moving;
int lapNumber=2;
int currentLap;
Quaternion dir=new Quaternion();
AppStateManager stateManager;

public AutomaticVehicleControl(AppStateManager stateManager,MotionPath path,int lapNumber){
  this.path=path;
  /*
   * The motion path to follow and the number of laps to execute
   */
  this.lapNumber=lapNumber;
  this.stateManager=stateManager;
}
public void setSpeed (float speed){
    this.speed=speed;
}
public void seek(Vector3f currentPos,Vector3f targetPos,Vector3f currentDir){
   
    Vector3f targetDir=targetPos.subtract(currentPos).normalizeLocal();
     Vector3f planeDir=new Vector3f();
     planeDir.set(currentDir).normalizeLocal();
            ROTATE_RIGHT.multLocal(planeDir);
            plane.setOriginNormal(spatial.getWorldTranslation(), planeDir);
            float angle= 1-currentDir.dot(targetDir);       
            float anglemult=FastMath.QUARTER_PI;
            float speedmult=.9f;
           
            if(plane.whichSide(targetPos)==Plane.Side.Negative){
              anglemult*=-1;  
            }
            
            if(angle>1f){
                speedmult*=-1;
                anglemult*=-1;
                angle=1f;
            }
            
            vehicle.steer(angle*anglemult);
            vehicle.accelerate(speedmult*speed);
            vehicle.brake(0);
}


public Integer getIndex(){
    return this.index;
}

    @Override
    public void setSpatial(Spatial spatial){
    this.spatial=spatial;
    if(spatial==null){
  return;}
    this.level=spatial.getUserData("level");

   
    Float spatialSpeed=(Float)spatial.getUserData("speed");
    if(spatialSpeed!=null){
        this.speed=spatialSpeed;
    }
  
 vehicle=spatial.getControl(VehicleControl.class);
maxPathPoints=path.getNbWayPoints();
vehicle.getPhysicsSpace().addCollisionListener(this);
moving=true;
checkPoint=new CheckPointManager(vehicle,path);
checkPoint.setLapNumber(lapNumber);
checkPoint.setCheckRadius(checkRadius);
stateManager.attach(checkPoint);
}

    public Vector3f getTargetLocation(){
       
        return path.getWayPoint(index);
               
    }

    public Vector3f getLocation() {
          return  vehicle.getPhysicsLocation();
    }
    public Vector3f getForwardVector(){

          return  vehicle.getForwardVector(forwardDirection);
    }
    @Override
    public void setEnabled(boolean enabled){
    this.enabled=enabled;
} 
    @Override
    public boolean isEnabled(){
    return enabled;
}
    @Override
    protected void controlUpdate(float tpf) {
     currentLocation=vehicle.getPhysicsLocation();
        vehicle.getForwardVector(forwardDirection);
        index=checkPoint.getCurrentWayPoint();
        currentLap=checkPoint.getCurrentLap();
     
      if(currentLap==lapNumber) {
          reached=true;
      }
        if(!reached){
          targetLocation=path.getWayPoint(index);
           
      }
     
seek(currentLocation,targetLocation,forwardDirection);
     if(!this.enabled){
 
     }
    }
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //Only needed for rendering-related operations,
        //not called when spatial is culled.
    }

    
    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule in = im.getCapsule(this);
        //TODO: load properties of this Control, e.g.
        //this.value = in.readFloat("name", defaultValue);
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule out = ex.getCapsule(this);
        //TODO: save properties of this Control, e.g.
        //out.write(this.value, "name", defaultValue);
    }
public void setCheckRadius(float checkRadius){
    this.checkRadius=checkRadius;
}
    @Override
    public void collision(PhysicsCollisionEvent event) {
    if((event.getNodeA().getName().equals("enemy"))&&(event.getNodeB().getName().equals("track"))){
        if(vehicle.getCurrentVehicleSpeedKmHour()<3&&vehicle.getCurrentVehicleSpeedKmHour()>-3){
   Vector3f vector1=path.getWayPoint(checkPoint.getCurrentWayPoint());
   Vector3f vector2=path.getWayPoint(checkPoint.getNextWayPoint());
   float dot= vector1.dot(vector2); 
   float angle=dot/vector1.length()*vector2.length();
   dir.fromAngleAxis(FastMath.acos(angle), Vector3f.UNIT_Y);
   vehicle.setPhysicsLocation(path.getWayPoint(index));
   
    vehicle.setAngularVelocity(Vector3f.ZERO);
    vehicle.resetSuspension();
   vehicle.setPhysicsRotation(dir);
   vehicle.setLinearVelocity(Vector3f.ZERO); 
        }
    }
    }

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


  
}

