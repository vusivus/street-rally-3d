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
import com.jme3.audio.AudioRenderer;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import sr3d.factory.GameFactory;
import sr3d.Main;
import sr3d.game.logic.GameMath;

/**
 *
 * @author Sanet
 */
public  class nextLevelState extends AbstractAppState 
implements ScreenController {
   AppStateManager stateManager;
   SimpleApplication app;
   float lap1time,lap2time;
   Nifty nifty;
   StartScreenControllerState screenState=new StartScreenControllerState();
    NiftyJmeDisplay niftyDisplay;
   float time=0;
   GameMath math=new GameMath();
   private InputManager inputManager;
   private AssetManager assetManager;
   private AudioRenderer audioRenderer;
   Node localGuiNode=new Node("LevelGui");
   Node guiNode;
ViewPort viewPort;
ViewPort guiViewPort;
   Main main=new Main();
  int gameLevel;
  String car;
   RaceObjects game;
  BitmapFont font;
  GameFactory factory=new GameFactory();
  BitmapText text;
 boolean finished=false;
String playerName;
ListBox timesList;
int defeated;

  public  nextLevelState(boolean finished) {
   this.finished=finished;
   
    }
public void setDefeated(int num){
    this.defeated=num;
}
 public void setName(String name,String car){
     this.playerName=name;
     this.car=car;
 } 
    @Override
    public void initialize(AppStateManager stateManager, Application app) {   
        super.initialize(stateManager, app);
        game=new RaceObjects();         
        this.app=(SimpleApplication) app;
         this.guiNode=this.app.getGuiNode();
     this.stateManager=stateManager;
     this.assetManager=this.app.getAssetManager();
     this.audioRenderer=this.app.getAudioRenderer();
     this.inputManager=this.app.getInputManager();
    this.guiViewPort=this.app.getGuiViewPort();
         this.viewPort=app.getViewPort();
         guiNode.attachChild(localGuiNode);
         
        font=assetManager.loadFont("Interface/Fonts/VirtualDJ.fnt");
      text=factory.getText("count", this.app.getCamera(), font);  
localGuiNode.attachChild(text);

niftyDisplay=new NiftyJmeDisplay(assetManager,inputManager,audioRenderer,viewPort);  
   nifty=niftyDisplay.getNifty();
nifty.fromXml("Interface/Nifty/nextLevel.xml","start",this);
   guiViewPort.addProcessor(niftyDisplay);  
  timesList=nifty.getScreen("start").findNiftyControl("times", ListBox.class);
 if(finished){
     display(time);
 }
       }

    
    public void display(float tim){

        text.setText("Your time "+math.convertToMinutes(tim));
               
    }
  
  public void nextTrack(){

        if(defeated!=3&&finished){
                stateManager.detach(this) ; 
      game.setLevel(gameLevel,false);  
      game.setName(playerName);
      game.setDefeatedCars(defeated+1);
      game.setCar(car);
        stateManager.attach(game); 
        }  
      else if(defeated==3&&finished){
               if(gameLevel!=3){
                  stateManager.detach(this) ; 
      game.setLevel(gameLevel+1,false);  
      game.setName(playerName);
      game.setDefeatedCars(0);
      game.setCar(car);
        stateManager.attach(game);  
               }  
        }   
  if(!finished){
                stateManager.detach(this) ; 
      game.setLevel(gameLevel,false);  
      game.setName(playerName);
      game.setDefeatedCars(defeated);
      game.setCar(car);
        stateManager.attach(game); 
        }  
    }
 public void menu(){
     stateManager.detach(this);
     stateManager.attach(screenState);
 }
    
   public void setLevel(int level,float time){
       this.gameLevel=level;
       this.time=time;

   }
    @Override
    public void cleanup(){
  
       viewPort.removeProcessor(niftyDisplay);
        nifty.exit();
       localGuiNode.detachChild(text); 
       guiNode.detachChild(localGuiNode);
       this.app.getTimer().reset();
       
    }
 

    public void bind(Nifty nifty, Screen screen) {
       
    }

    public void onStartScreen() {
     
    }

    public void onEndScreen() {
      
    }
}
