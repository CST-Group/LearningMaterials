package br.unicamp.cst.demoCST.codelets.behaviors;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryContainer;
import br.unicamp.cst.core.entities.MemoryObject;
import static support.Constants.*;
import br.unicamp.cst.representation.idea.Idea;
import java.awt.Point;
import java.awt.geom.Point2D;
import org.json.JSONException;
import ws3dproxy.model.Thing;


public class GoToClosestApple extends Codelet {

    private Memory closestAppleMO;

    private Memory innerSenseMO;

    private MemoryContainer legsMC;
    
    private int creatureBasicSpeed;
    private double reachDistance;
    
    public GoToClosestApple() {
        this.creatureBasicSpeed=CREATURE_BASIC_SPEED;
        this.reachDistance=REACH_DISTANCE;
        this.name = "GoToClosestApple";
    }

    @Override()
    public void accessMemoryObjects() {
        closestAppleMO=(MemoryObject)this.getInput("closestAppleMO");
        innerSenseMO=(MemoryObject)this.getInput("innerSenseMO");
        legsMC=(MemoryContainer)this.getOutput("legsMC");
    }

    @Override()
    public void calculateActivation() {
    }

    @Override()
    public void proc() {
        System.out.println("Executing proc gotoclosestapple");
        // Find distance between creature and closest apple
        //If far, go towards it
        //If close, stops

        Thing closestApple = (Thing) closestAppleMO.getI();
        Idea cis = (Idea) innerSenseMO.getI();

        if(closestApple != null)
        {
            double appleX=0;
            double appleY=0;
            try {
                appleX = closestApple.getCenterPosition().getX();
                appleY = closestApple.getCenterPosition().getY();

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
            //JSONObject message=new JSONObject();
            Idea message = Idea.createIdea(MESSAGE,"", Idea.guessType("Property",null,1.0,0.5));
            try {
                if(distance>reachDistance){ //Go to it
                    message.add(Idea.createIdea(ACTION,GOTO, Idea.guessType("Property",null,1.0,0.5)));
                    message.add(Idea.createIdea("X",(int)appleX, Idea.guessType("Property",null,1.0,0.5)));
                    message.add(Idea.createIdea("Y",(int)appleY, Idea.guessType("Property",null,1.0,0.5)));
                    message.add(Idea.createIdea("SPEED",creatureBasicSpeed, Idea.guessType("Property",null,1.0,0.5)));
                    activation=1.0;

                }else{//Stop
                    message.add(Idea.createIdea(ACTION,GOTO, Idea.guessType("Property",null,1.0,0.5)));
                    message.add(Idea.createIdea("X",(int)appleX, Idea.guessType("Property",null,1.0,0.5)));
                    message.add(Idea.createIdea("Y",(int)appleY, Idea.guessType("Property",null,1.0,0.5)));
                    message.add(Idea.createIdea("SPEED",0, Idea.guessType("Property",null,1.0,0.5)));
                    activation=0.5;
                }
                legsMC.setI(toJson(message),activation,name);
            } catch (JSONException e) {
                    e.printStackTrace();
            }	
        }
        else {
            activation=0.0;
            legsMC.setI("",activation,name);
        }
    }
    
    String toJson(Idea i) {
        String q = "\"";
        String out = "{";
        String val;
        int ii=0;
        for (Idea il : i.getL()) {
            if (il.getL().isEmpty()) {
                if (il.isNumber()) val = il.getValue().toString();
                else val = q+il.getValue()+q;
            }
            else val = toJson(il);
            if (ii == 0) out += q+il.getName()+q+":"+val;
            else out += ","+q+il.getName()+q+":"+val;
            ii++;
        }
        out += "}";
        return out;
    }
}
