package br.unicamp.cst.demoCST.codelets.perception;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryObject;
import br.unicamp.cst.representation.idea.Idea;
import ws3dproxy.model.Thing;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Collections;
import java.util.List;

public class ClosestAppleDetector extends Codelet {

    private Memory knownApplesMO;

    private Memory innerSenseMO;

    private Memory closestAppleMO;
    
    private List<Thing> known;
    
    public ClosestAppleDetector() {
        this.name = "ClosestAppleDetector";
    }

    @Override()
    public void accessMemoryObjects() {
        knownApplesMO = (MemoryObject)this.getInput("knownApplesMO");
        innerSenseMO = (MemoryObject)this.getInput("innerSenseMO");
        closestAppleMO =(MemoryObject)this.getOutput("closestAppleMO");
    }

    @Override()
    public void calculateActivation() {
    }

    @Override()
    public void proc() {
        System.out.println("Executing proc ClosestAppleDetector");
        Thing closest_apple=null;
        known = Collections.synchronizedList((List<Thing>) knownApplesMO.getI());
        Idea cis = (Idea) innerSenseMO.getI();
        synchronized(known) {
           if(known.size() != 0){
                //Iterate over objects in vision, looking for the closest apple
                CopyOnWriteArrayList<Thing> myknown = new CopyOnWriteArrayList<>(known);
                for (Thing t : myknown) {
                    String objectName=t.getName();
                    if(objectName.contains("PFood") && !objectName.contains("NPFood")){ //Then, it is an apple
                        if(closest_apple == null) closest_apple = t;
                        else {
                            double Dnew = calculateDistance(t.getX1(), t.getY1(), (double)cis.get("position.x").getValue(), (double)cis.get("position.y").getValue());
                            double Dclosest= calculateDistance(closest_apple.getX1(), closest_apple.getY1(), (double)cis.get("position.x").getValue(), (double)cis.get("position.y").getValue());
                            if(Dnew<Dclosest){
                                closest_apple = t;
                            }
                        }
                    }
                }
            }
        }
        closestAppleMO.setI(closest_apple);
    }
    private double calculateDistance(double x1, double y1, double x2, double y2) {
        return(Math.sqrt(Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2)));
    }
}
