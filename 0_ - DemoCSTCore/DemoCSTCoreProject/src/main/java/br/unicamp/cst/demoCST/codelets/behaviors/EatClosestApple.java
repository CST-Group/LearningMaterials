package br.unicamp.cst.demoCST.codelets.behaviors;

import java.awt.Point;
import java.awt.geom.Point2D;
import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryObject;
import br.unicamp.cst.representation.idea.Idea;
import java.util.List;
import org.json.JSONException;
import ws3dproxy.model.Thing;
import org.json.JSONObject;
import static support.Constants.*;

public class EatClosestApple extends Codelet {

    private Memory closestAppleMO;

    private Memory innerSenseMO;

    private Memory handsMO;

    private Memory knownApplesMO;
    
    private final int reachDistance;
    Thing closestApple;
    Idea cis;
    List<Thing> known;
    
    public EatClosestApple() {
        setTimeStep(50);
        this.reachDistance=REACH_DISTANCE;
        this.name = "EatClosestApple";
    }

    @Override()
    public void accessMemoryObjects() {
        closestAppleMO = (MemoryObject)this.getInput(CLOSEST_APPLE);
        innerSenseMO = (MemoryObject)this.getInput(INNER);
        handsMO = (MemoryObject)this.getOutput(HANDS);
        knownApplesMO = (MemoryObject)this.getOutput(KNOWN_APPLES);
    }

    @Override()
    public void calculateActivation() {
    }

    @Override()
    public void proc() {
        System.out.println("Executing proc eat closest apple");
        String appleName="";
        closestApple = (Thing) closestAppleMO.getI();
        cis = (Idea) innerSenseMO.getI();
        known = (List<Thing>) knownApplesMO.getI();
        //Find distance between closest apple and self
        //If closer than reachDistance, eat the apple

        if(closestApple != null)
        {
            double appleX=0;
            double appleY=0;
            try {
                appleX=closestApple.getCenterPosition().getX();
                appleY=closestApple.getCenterPosition().getY();
                appleName = closestApple.getName();

            } catch (Exception e) {
                e.printStackTrace();
            }
            
            double selfX=(double)cis.get("position.x").getValue();
            double selfY=(double)cis.get("position.y").getValue();

            Point2D pApple = new Point();
            pApple.setLocation(appleX, appleY);

            Point2D pSelf = new Point();
            pSelf.setLocation(selfX, selfY);

            double distance = pSelf.distance(pApple);
            JSONObject message=new JSONObject();
            try {
                if(distance<=reachDistance){ //eat it						
                    message.put(OBJECT, appleName);
                    message.put(ACTION, EATIT);
                    handsMO.setI(message.toString());
                    activation=1.0;
                    DestroyClosestApple();
                }else{
                    handsMO.setI("");	//nothing
                    activation=0.0;
                }

            } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
            }
        }else{
            if(handsMO.getI() != ""){
                handsMO.setI("");	//nothing
                activation=0.0;
            }
        }
    }
    
    public void DestroyClosestApple() {
        int r = -1;
        int i = 0;
        synchronized(known) {
            for (Thing t : known) {
                if (closestApple != null) 
                    if (t.getName().equals(closestApple.getName())) r = i;
                i++;
            }   
            if (r != -1) known.remove(r);
            closestApple = null;
            knownApplesMO.setI(known);
        }
     }
}
