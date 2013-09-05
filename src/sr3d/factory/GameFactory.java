/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sr3d.factory;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Spatial;
import com.jme3.ui.Picture;

/**
 *
 * @author Sanet
 */
public class GameFactory {
String name;
public void setName(String name){
    this.name=name;
}
public String getName(){
    return name;
}
    public Picture getPic(String name,AssetManager assetManager,Camera cam){
 Picture pic = null;
 if(name.equals("one")){
 pic=new Picture("one");
pic.setImage(assetManager,"Textures/numbers/one.png",true);
pic.setHeight(cam.getHeight()*.25f);
pic.setWidth(cam.getWidth()*.25f);
pic.setPosition(cam.getWidth()*.4f, cam.getHeight()*.5f);
 }
  if(name.equals("two")){
 pic=new Picture("two");
pic.setImage(assetManager,"Textures/numbers/two.png",true);
pic.setHeight(cam.getHeight()*.25f);
pic.setWidth(cam.getWidth()*.25f);
pic.setPosition(cam.getWidth()*.4f, cam.getHeight()*.5f);
 }
  
    if(name.equals("three")){
 pic=new Picture("three");
pic.setImage(assetManager,"Textures/numbers/three.png",true);
pic.setHeight(cam.getHeight()*.25f);
pic.setWidth(cam.getWidth()*.25f);
pic.setPosition(cam.getWidth()*.4f, cam.getHeight()*.5f);
 }
  if(name.equals("go")){
 pic=new Picture("go");
pic.setImage(assetManager,"Textures/numbers/GO.png",true);
pic.setHeight(cam.getHeight()*.25f);
pic.setWidth(cam.getWidth()*.25f);
pic.setPosition(cam.getWidth()*.4f, cam.getHeight()*.5f);
 } 
  if(name.equals("speed")){
    pic=  new Picture("speed");
pic.setImage(assetManager,"Textures/speedometer.dds", true);
pic.setHeight(cam.getHeight()*.2f);
pic.setWidth(cam.getWidth()*.2f);
pic.setPosition(cam.getWidth()*.8f, cam.getHeight()*.01f);

  }
    if(name.equals("frame")){
    pic=  new Picture("speed");
pic.setImage(assetManager,"Textures/frame.png", true);
pic.setHeight(cam.getHeight()*.2f);
pic.setWidth(cam.getWidth());
pic.setPosition(0, cam.getHeight()*.9f);

  }
    return  pic;  
    
}
  public Spatial getTrack(int name,AssetManager assetManager){
  
      Spatial track = null;
  
      if(name==1){
        track =  assetManager.loadModel("Models/Tracks/streetracingtrack/streetracingtrack.j3o");
      } 
       if(name==2){
        track =  assetManager.loadModel("Models/Tracks/BluesCity/BluesCity.j3o");
      } 
      if(name==3){
        track =  assetManager.loadModel("Models/Tracks/streetracingtrack2/streetracingtrack2.j3o");
      }   
 return track;
 } 
 public Spatial getCar(String name,AssetManager assetManager){
    Spatial spatial = null;
     if(name.equals("McLarenF1")){
         spatial = assetManager.loadModel("Models/Cars/McLaren F1/McLaren F1.j3o");

     } 
     if(name.equals("Viper")){
         spatial =   assetManager.loadModel("Models/Cars/Viper/Viper.j3o");
       
     } 
     if(name.equals("BMW M3")){
         spatial =  assetManager.loadModel("Models/Cars/BMW M3/BMW M3.j3o");
       
     } 
    if(name.equals("Police")){
         spatial =  assetManager.loadModel("Models/Cars/police car/police car.j3o");
       
     }  
     if(name.equals("Porsche Cop")){
         spatial =  assetManager.loadModel("Models/Cars/Porsche Cop/Porsche Cop.j3o");
        
     }  
      if(name.equals("Loftus")){
         spatial =  assetManager.loadModel("Models/Cars/Loftus/Loftus.j3o");
      
     } 
       if(name.equals("Hummer")){
         spatial =  assetManager.loadModel("Models/Cars/Hummer/Hummer.j3o");
      
     }
      if(name.equals("Sincar")){
         spatial =  assetManager.loadModel("Models/Cars/Sincar/Sincar.j3o");
      
     }  
       if(name.equals("Car1")){
         spatial =  assetManager.loadModel("Models/Cars/Car1/Car1.j3o");
       
     } 
        if(name.equals("Porsche")){
         spatial =  assetManager.loadModel("Models/Cars/Porsche/Porsche.j3o");
   
     } 
        return spatial;
    }

public Vector3f getStartPoint(int track){
  Vector3f loc=null;

    if(track==1){
           loc=new Vector3f(-40.2f,0,-13.7f); 
    }
    if(track==2){
           loc=new Vector3f(-66,0f,14); 
    }
     if(track==3){
           loc=new Vector3f(42.6f,0f,-74.1f); 
    }
    return loc;
}


public BitmapText getText(String name,Camera cam,BitmapFont font){
     BitmapText text=null;
     if("lap".equals(name)){
    text =new BitmapText(font,false);
       text.setSize(28);
      text.setColor(ColorRGBA.White);
      text.setLocalTranslation(new Vector3f(.75f*cam.getWidth(),.95f*cam.getHeight(),0));
  text.setText("Lap 1/2");
     }
    if(name.equals("speed")) {
       text= new BitmapText(font,false);
       text.setSize(16);
     text.setColor(ColorRGBA.Cyan);
     text.setLocalTranslation(new Vector3f(.87f*cam.getWidth(),.085f*cam.getHeight(),0));

    }
    if(name.equals("time")){
     text=   new BitmapText(font,false);
      text.setSize(24);
     text.setColor(ColorRGBA.White);
      text.setLocalTranslation(new Vector3f(.35f*cam.getWidth(),.95f*cam.getHeight(),0));
    }
       if(name.equals("position")){
     text=   new BitmapText(font,false);
      text.setSize(24);
     text.setColor(ColorRGBA.White);
      text.setLocalTranslation(new Vector3f(.05f*cam.getWidth(),.95f*cam.getHeight(),0));
    }
    if(name.equals("laptime")){
      text=  new BitmapText(font,false);
      text.setSize(24);
      text.setColor(ColorRGBA.Yellow);
      text.setLocalTranslation(new Vector3f(.03f*cam.getWidth(),.90f*cam.getHeight(),0));
    }
    if(name.equals("count")){
       text= new BitmapText(font,false);
      text.setSize(20);
      text.setColor(ColorRGBA.Cyan);
      text.setLocalTranslation(new Vector3f(.1f*cam.getWidth(),.5f*cam.getHeight(),0));
    }
     if(name.equals("points")){
       text= new BitmapText(font,false);
      text.setSize(18);
      text.setColor(ColorRGBA.White);
      text.setLocalTranslation(new Vector3f(.03f*cam.getWidth(),.1f*cam.getHeight(),0));
    }
     return text;
}
public ParticleEmitter getParticle(String particle,AssetManager assetManager){
  ParticleEmitter emitter = null;
    if("smoke".equals(particle)){
        emitter = new ParticleEmitter("Smoke", ParticleMesh.Type.Triangle, 30);
    Material mat_smoke = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
    mat_smoke.setTexture("Texture", assetManager.loadTexture("Effects/Smoke/Smoke.png"));
    emitter.setMaterial(mat_smoke);
    emitter.setImagesX(2); emitter.setImagesY(2); // 2x2 texture animation
    emitter.setEndColor(ColorRGBA.DarkGray);   // red
    emitter.setStartColor(ColorRGBA.LightGray); // yellow
      emitter.getParticleInfluencer().setInitialVelocity(new Vector3f(0,2,0));
    emitter.setStartSize(1.5f);
   emitter.setEndSize(0.1f);
    emitter.setGravity(0,0,0);
    emitter.setLowLife(0.5f);
    emitter.setHighLife(3f);
   emitter.getParticleInfluencer().setVelocityVariation(0.3f);
 
    }
    return emitter;
}
public AudioNode getSound(String name,AssetManager assetManager){
    AudioNode sound=null;
    if(name=="accelerate"){
        sound=new AudioNode(assetManager,"Interface/Nifty/sounds/sound.wav");
    }
     if(name=="brake"){
        sound=new AudioNode(assetManager,"Sounds/Brake.wav");
    }
    if(name=="turn"){
        sound=new AudioNode(assetManager,"Sounds/turn1.wav");
    }  
        return null;
}
}
