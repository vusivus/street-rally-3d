/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sr3d.factory;

import com.jme3.cinematic.MotionPath;
import com.jme3.math.Vector3f;

/**
 *
 * @author Sanet
 */
public class MotionPaths {
    public MotionPath getPath(int num){
     MotionPath path = null;
         if(num==1){       
                path=new MotionPath();
     
    path.addWayPoint(new Vector3f(-36.3f,0f,35f));
     path.addWayPoint(new Vector3f(-25.8f,0f,49.1f));
         path.addWayPoint(new Vector3f(-5f,0f,48.7f));
     path.addWayPoint(new Vector3f(27f,0f,47.5f));
     path.addWayPoint(new Vector3f(38.4f,0f,40f));
     path.addWayPoint(new Vector3f(42.4f,0f,13.2f));
     path.addWayPoint(new Vector3f(17.8f,0f,2.8f));
     path.addWayPoint(new Vector3f(13.3f,0f,25f));
       path.addWayPoint(new Vector3f(2.5f,0f,31.7f));
    path.addWayPoint(new Vector3f(-13.3f,0f,27.3f));
     path.addWayPoint(new Vector3f(-21.7f,-0f,4.3f));
     path.addWayPoint(new Vector3f(6.3f,0f,-6.5f));
       path.addWayPoint(new Vector3f(23.3f,0f,-10.6f));
     path.addWayPoint(new Vector3f(28.4f,-0f,-23.3f));
     path.addWayPoint(new Vector3f(22.2f,-0f,-45.8f));
      path.addWayPoint(new Vector3f(-13.9f,-0f,-40f));
     path.addWayPoint(new Vector3f(-27f,0f,-32f)); 
       path.addWayPoint(new Vector3f(-41.1f,0f,-35.2f));
      path.addWayPoint(new Vector3f(-45.4f,0f,-26.7f));   
      path.addWayPoint(new Vector3f(-40.2f,0f,-13.7f));
     
         }
       if(num==2){    
           path=new MotionPath();
    path.addWayPoint(new Vector3f(-60.6f,0f,80f));
       path.addWayPoint(new Vector3f(-47.9f,0f,97.7f));
  path.addWayPoint(new Vector3f(-26.5f,0f,101.2f));
    path.addWayPoint(new Vector3f(-8.7f,0f,78.8f));
    path.addWayPoint(new Vector3f(8.7f,0f,63.4f));
      path.addWayPoint(new Vector3f(54.1f,0f,55.8f));
    path.addWayPoint(new Vector3f(77f,0f,43.8f));
      path.addWayPoint(new Vector3f(79.3f,0f,15.6f));
    path.addWayPoint(new Vector3f(47.1f,0f,-.9f));
      path.addWayPoint(new Vector3f(21f,0f,8f));
    path.addWayPoint(new Vector3f(-15.3f,0f,21.6f));
      path.addWayPoint(new Vector3f(-33.7f,0f,3.8f));
    path.addWayPoint(new Vector3f(-26f,0f,-31.22f));
      path.addWayPoint(new Vector3f(-12.6f,0f,-50f));
      path.addWayPoint(new Vector3f(4.6f,0f,-54.4f));
    path.addWayPoint(new Vector3f(54.3f,0f,-40.5f));
      path.addWayPoint(new Vector3f(84.4f,0f,-32.2f));
      path.addWayPoint(new Vector3f(100.2f,0f,-36.2f));
    path.addWayPoint(new Vector3f(102.7f,0f,-60.2f));
      path.addWayPoint(new Vector3f(85.5f,0f,-81.8f));
    path.addWayPoint(new Vector3f(36.1f,0f,-86.5f));
      path.addWayPoint(new Vector3f(-35.1f,0f,-82.5f));
      path.addWayPoint(new Vector3f(-75.6f,0f,-71.2f));
        path.addWayPoint(new Vector3f(-84.4f,0f,-58.8f));
      path.addWayPoint(new Vector3f(-77.8f,0f,-46f));
      
      path.addWayPoint(new Vector3f(-62.3f,0f,-14.7f));
        path.addWayPoint(new Vector3f(-64f,0f,14f));
       }
     if(num==3){       
  path=new MotionPath();
          path.addWayPoint(new Vector3f(-1,0.15f,61));        
        path.addWayPoint(new Vector3f(-30,0.15f,83));
          path.addWayPoint(new Vector3f(-49,0.15f,75));
         path.addWayPoint(new Vector3f(-62,0.15f,52));
         path.addWayPoint(new Vector3f(-45,0.15f,19));
           path.addWayPoint(new Vector3f(-22,0.15f,-1));
            path.addWayPoint(new Vector3f(-24,0.15f,-13));
         path.addWayPoint(new Vector3f(-65,0.15f,-41)); 
         path.addWayPoint(new Vector3f(-71,0.15f,-62));
           path.addWayPoint(new Vector3f(-69,0.15f,-87));
         path.addWayPoint(new Vector3f(-21,0.15f,-116)); 
         path.addWayPoint(new Vector3f(56,.15f,-103)); 
          path.addWayPoint(new Vector3f(84,0.15f,-68));
            path.addWayPoint(new Vector3f(94,0.15f,51));
             path.addWayPoint(new Vector3f(84,0.15f,73));
          path.addWayPoint(new Vector3f(60,0.15f,87));
           path.addWayPoint(new Vector3f(34,0.15f,74));
            path.addWayPoint(new Vector3f(25,0.15f,51));  
          path.addWayPoint(new Vector3f(25,0.15f,-72)); 
           path.addWayPoint(new Vector3f(-17,0.15f,-85));
           path.addWayPoint(new Vector3f(-31,0.15f,-55));
           path.addWayPoint(new Vector3f(-24,0.15f,-30)); 
             path.addWayPoint(new Vector3f(-5,0.15f,-20));
           path.addWayPoint(new Vector3f(1,0.15f,-6)); 
           path.addWayPoint(new Vector3f(.6f,0.15f,28.1f)); 
         
     }
     
        return path;
    }
}
