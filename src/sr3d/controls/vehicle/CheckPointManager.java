/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sr3d.controls.vehicle;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.control.VehicleControl;
import com.jme3.cinematic.MotionPath;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vusman
 */
public class CheckPointManager extends AbstractAppState{
    public CheckPointManager(VehicleControl vehicleControl,MotionPath path){
        this.vehicle=vehicleControl;
        this.path=path;
        maxCheckPoints=this.path.getNbWayPoints();
        checkRadius=4;
        currentCheckPoint=1;
        currentWayPoint=0;
        currentLap=0;
        lapNumber=2;
    }
         
    float checkRadius;
    Future locFuture=null;
    int currentCheckPoint;
    int currentWayPoint;
    int currentLap;
    int nextWayPoint;
    boolean moving=true;
    int lapNumber;
    ScheduledThreadPoolExecutor executor;
    int maxCheckPoints;
    VehicleControl vehicle;
    MotionPath path;

    

    public void setCheckRadius(float checkRadius){
        this.checkRadius=checkRadius;
        /*
         * Is 4 by default ,depends on the scaling of your world objects
         */
    }
    public Integer getMaxCheckPoints(){
        return maxCheckPoints;
    }



    @Override
    public void initialize(AppStateManager stateManager,Application app) {
     super.initialize(stateManager, app);
  
        executor=new ScheduledThreadPoolExecutor(2);
    }
    @Override
   public void update(float tpf){
     Vector3f location = null;
        if(locFuture==null){
          locFuture =executor.submit(new Callable<Vector3f>(){

                  @Override
                  public Vector3f call() throws Exception {
                  return   vehicle.getPhysicsLocation();
                  }
                
            });
        } 
        else if(locFuture!=null){
            if(locFuture.isDone()){
                try {         
                    location=(Vector3f)locFuture.get();
                } catch (InterruptedException ex) {
                    Logger.getLogger(CheckPointManager.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ExecutionException ex) {
                    Logger.getLogger(CheckPointManager.class.getName()).log(Level.SEVERE, null, ex);
                }
        
                locFuture=null;
            }
        }
       if(location!=null){ 
        float distance =location.subtract(path.getWayPoint(currentWayPoint)).length();
        
        if(distance<=checkRadius&&moving){
         
            if(currentWayPoint==maxCheckPoints-1){
          
               if(currentLap!=lapNumber){
               currentWayPoint=0;
                currentCheckPoint=1;
                ++currentLap;
               }
             else if(currentLap==lapNumber){
                   moving=false;   
               }
            }
            else if(currentWayPoint!=maxCheckPoints-1){
                ++currentWayPoint;
                ++currentCheckPoint;
            }
        }
       
   } 
    }   
    public void setLapNumber(int lapNumber){
        this.lapNumber=lapNumber;
    }
    @Override
    public void cleanup(){
    executor.shutdown();
}

    public int getCurrentLap(){
    return currentLap;
}
    public int getNextWayPoint(){
        nextWayPoint=currentWayPoint+1;
        if(nextWayPoint==maxCheckPoints-1){
            nextWayPoint=0;
           
        } 
        return nextWayPoint;
    }

public int getCurrentWayPoint(){
    return currentWayPoint;
}
public int getCurrentCheckPoint(){
    return currentCheckPoint;
}
}

