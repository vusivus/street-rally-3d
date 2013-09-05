/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sr3d.game.logic;

import com.jme3.cinematic.MotionPath;
import com.jme3.math.Vector3f;

/**
 *
 * @author Vusman
 */
public class GameMath {
    public float roundOff(float number,int decimalPlaces){
  /*
   * round off your  number to the deisred decimal places
   */
        float result = 0;
      String num=String.valueOf(number);
   try{
    result=Float.valueOf((num.substring(0, num.indexOf(".")+decimalPlaces+1)));
   }
    catch(Exception ex){
        
    }
     return result;
 }  
public String convertToMinutes(float number){
 
 float minutes = number/60;
 float seconds=number%60;
 
 String minRound=String.valueOf(minutes);
 String min=String.valueOf(minRound.substring(0,minRound.indexOf(".")));
 String sec=String.valueOf(roundOff(seconds,2));

 if(seconds<10){
     sec="0"+sec;
 }
 if(minutes<10){
     min="0"+min;
 }
String result=min+":"+sec;
 return result;
 
}
public Float findWayPointsDistance(MotionPath path,int targetWayPoint){
  /*
   * returns the distance from your last way point to the target waypoint
   */
    float distance; 
 int lastWayPoint=targetWayPoint-1;
   if(lastWayPoint<0){
       lastWayPoint=path.getNbWayPoints()-1;
   }

  distance=path.getWayPoint(targetWayPoint).subtract(path.getWayPoint(lastWayPoint)).length(); 

   return distance;
}
public int findClosestWayPoint(Vector3f current_location,MotionPath path){
Vector3f desired_location; 
/*
 * returns the waypoint location which is closest to your vehcile
 */
float least_distance=1000;
float distance;
int index = 0;
for(int i=0;i<path.getNbWayPoints();i++){
  distance=current_location.subtract(path.getWayPoint(i)).length();
  if(distance<least_distance){
      least_distance=distance;
      index=i;
  }
}

return index;
}

public Integer findPlayerPosition(MotionPath path,int enemy_index, int player_index,
    Vector3f enemyLoc, Vector3f playerLoc, int enemy_lap,int player_lap){
 int position=0;
 /*
  * returns an integer representing the player position between two cars
  */
 if(player_lap>enemy_lap){
     position=1;
 }
else if(player_lap<enemy_lap){
     position=2;
 }
else if(player_lap==enemy_lap){
    if(player_index>enemy_index){
     position=1;
 }
 else  if(player_index<enemy_index){
     position=2;
 } 
 else if(player_index==enemy_index){
    float player_distance=playerLoc.subtract(path.getWayPoint(player_index)).length();
    float enemy_distance=enemyLoc.subtract(path.getWayPoint(enemy_index)).length();
    
   if(player_distance>enemy_distance){
       position=2;
   } 
    else  if(player_distance<enemy_distance){
       position=1;
   }
    else  if(player_distance==enemy_distance){
       position=1;
   }    
 } 
 }

  
  return position;
 }



public Float findDistanceFromWayPoint(Vector3f currentLocation,
        int targetWayPoint,MotionPath path){
    /*
     * returns the distance from your last waypoint to your vehicle's
     * current location
     */
   int lastWayPoint=targetWayPoint-1;
   if(lastWayPoint<0){
       lastWayPoint=path.getNbWayPoints()-1;
   }
  float distToNextWayPoint= path.getWayPoint(targetWayPoint).subtract(currentLocation).length();
  float distBtwnWayPoints=path.getWayPoint(targetWayPoint).subtract(path.getWayPoint(lastWayPoint)).length(); 

    float distance=-distToNextWayPoint+distBtwnWayPoints;
  
    return distance;
}
}
