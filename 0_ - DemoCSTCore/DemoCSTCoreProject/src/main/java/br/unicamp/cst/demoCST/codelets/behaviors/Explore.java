package br.unicamp.cst.demoCST.codelets.behaviors;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryContainer;
import br.unicamp.cst.core.entities.MemoryObject;
import java.util.List;
import org.json.JSONObject;
import static support.Constants.*;
import ws3dproxy.model.Thing;

public class Explore extends Codelet {

    private Memory knownApplesMO;

    private MemoryContainer legsMC;
    
    private List<Thing> known;
     
    public Explore(){
        this.name = "Explore";
    }

    @Override()
    public void accessMemoryObjects() {
        knownApplesMO = (MemoryObject)this.getInput("knownApplesMO");
        legsMC = (MemoryContainer)this.getOutput("legsMC");
    }

    @Override()
    public void calculateActivation() {
    }

    @Override()
    public void proc() {
        System.out.println("Executing proc explore");
        known = (List<Thing>) knownApplesMO.getI();
        if (known.size() == 0) {
            activation=1.0;
        }
        else activation=0.0;
        JSONObject message=new JSONObject();
        message.put(ACTION, EXPLORE);
        if(legsMC.getI() == message.toString() &&  legsMC.getEvaluation() == activation) {
            System.out.println("Nothing changed explore");
        } else {
            legsMC.setI(message.toString(),activation,name);		
        }
    }
}
