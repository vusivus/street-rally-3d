/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sr3d.controls.vehicle;

import com.jme3.bullet.control.VehicleControl;
import com.jme3.bullet.objects.VehicleWheel;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.FastMath;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import java.io.IOException;

/**
 *
 * @author Sanet
 */
public class VehicleControlValues extends AbstractControl {
  VehicleControl car_con;
  VehicleWheel fl,fr,br,bl;
    
   float compValue =.3f;
   float dampValue=.4f;
   float relaxValue=.5f;
   float stiffness=50f;
   float suspension=6000;
   
    @Override
    public void setSpatial(Spatial spat){
  
    this.spatial=spat;
    car_con=spatial.getControl(VehicleControl.class);
     car_con .setSuspensionDamping(dampValue*2.0f*FastMath.sqrt(stiffness));
     car_con.setSuspensionCompression(compValue*2.0f*FastMath.sqrt(stiffness));
     car_con.setSuspensionStiffness(stiffness);
      car_con.setMaxSuspensionForce(suspension);
     car_con .setMass(200f);
     
     car_con.setLinearDamping(.2f);
     bl=car_con.getWheel(0);
     br=car_con.getWheel(1);
 fl= car_con.getWheel(2);
fr=  car_con.getWheel(3);
fl.setMaxSuspensionForce(suspension);

fl.setWheelsDampingRelaxation(relaxValue*2.0f*FastMath.sqrt(stiffness));
fl.setWheelsDampingCompression(dampValue*2.0f*FastMath.sqrt(stiffness));
fl.setSuspensionStiffness(stiffness);
fl.setFrictionSlip(5f);
fl.setRestLength(bl.getRestLength()+.005f);


fr.setMaxSuspensionForce(suspension);

fr.setSuspensionStiffness(stiffness);
fr.setWheelsDampingRelaxation(relaxValue*2.0f*FastMath.sqrt(stiffness));
fr.setWheelsDampingCompression(dampValue*2.0f*FastMath.sqrt(stiffness));
fr.setFrictionSlip(5f);
fr.setRestLength(bl.getRestLength()+.005f);


bl.setWheelsDampingRelaxation(relaxValue*2.0f*FastMath.sqrt(stiffness));
bl.setWheelsDampingCompression(dampValue*2.0f*FastMath.sqrt(stiffness));
br.setWheelsDampingCompression(dampValue*2.0f*FastMath.sqrt(stiffness));  
br.setWheelsDampingRelaxation(relaxValue*2.0f*FastMath.sqrt(stiffness));
br.setMaxSuspensionForce(suspension);
bl.setMaxSuspensionForce(suspension);
br.setSuspensionStiffness(stiffness);
bl.setSuspensionStiffness(stiffness);
br.setFrictionSlip(8f);
bl.setFrictionSlip(8f);

br.setRestLength(.05f);
bl.setRestLength(.05f);

}
    @Override
    protected void controlUpdate(float tpf) {
        //TODO: add code that controls Spatial,
        //e.g. spatial.rotate(tpf,tpf,tpf);
    }
    
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //Only needed for rendering-related operations,
        //not called when spatial is culled.
    }
    
    public Control cloneForSpatial(Spatial spatial) {
        VehicleControlValues control = new VehicleControlValues();
        //TODO: copy parameters to new Control
        control.setSpatial(spatial);
        return control;
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
}
