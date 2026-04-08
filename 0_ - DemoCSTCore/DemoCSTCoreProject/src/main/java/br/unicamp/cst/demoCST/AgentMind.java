package br.unicamp.cst.demoCST;

import br.unicamp.cst.demoCST.codelets.sensors.Vision;
import br.unicamp.cst.demoCST.codelets.sensors.InnerSense;
import br.unicamp.cst.demoCST.codelets.perception.AppleDetector;
import br.unicamp.cst.demoCST.codelets.perception.ClosestAppleDetector;
import br.unicamp.cst.demoCST.codelets.behaviors.GoToClosestApple;
import br.unicamp.cst.demoCST.codelets.behaviors.EatClosestApple;
import br.unicamp.cst.demoCST.codelets.behaviors.Explore;
import br.unicamp.cst.demoCST.codelets.motor.LegsActionCodelet;
import br.unicamp.cst.demoCST.codelets.motor.HandsActionCodelet;
import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryContainer;
import br.unicamp.cst.core.entities.Mind;
import br.unicamp.cst.representation.idea.Idea;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static support.Constants.INNER;
import ws3dproxy.model.Creature;
import ws3dproxy.model.Thing;

public class AgentMind extends Mind {
    public String name;
    
    public AgentMind(Creature creature, String name) {
        super();
        this.name=name;
        // Codelets Groups Declaration
        createCodeletGroup("Sensors");
        createCodeletGroup("Perception");
        createCodeletGroup("Behaviors");
        createCodeletGroup("Motor");
       
        // Memory Groups Declaration
        createMemoryGroup("Motor");
        createMemoryGroup("Sensory");
        createMemoryGroup("Working");
       
        MemoryContainer legsMC; //new
        Memory legsExploreMO; //new
        Memory legsGoToClosestAppleMO; //new
        Memory handsMO;
        Memory visionMO;
        Memory innerSenseMO;
        Memory closestAppleMO; 
        Memory knownApplesMO;
        legsMC = createMemoryContainer("legsMC");
        legsExploreMO=createMemoryObject("GoToClosestApple"); //new
        legsGoToClosestAppleMO=createMemoryObject("Explore"); //new
        legsMC.add(legsExploreMO);
        legsMC.add(legsGoToClosestAppleMO);
        registerMemory(legsMC, "Motor");
        handsMO = createMemoryObject("handsMO", ""); //new
        registerMemory(handsMO, "Motor");
        
        List<Thing> vision_list = Collections.synchronizedList(new ArrayList<Thing>()); //new
        visionMO = createMemoryObject("visionMO", vision_list); //new
        registerMemory(visionMO, "Sensory");
        
        Idea cis = Idea.createIdea("cis" + name,"", Idea.guessType("AbstractObject",null,1.0,0.5));
        cis.add(Idea.createIdea("cis"+ name +".pitch", 0D, Idea.guessType("Property", null,1.0,0.5)));
        cis.add(Idea.createIdea("cis"+ name +".fuel", 0D, Idea.guessType("Property", null,1.0,0.5)));
        Idea position = Idea.createIdea("cis"+ name +".position","", Idea.guessType("Property",null,1.0,0.5));
        position.add(Idea.createIdea("cis"+ name +".position.x",0D,Idea.guessType("QualityDimension",null,1.0,0.5)));
        position.add(Idea.createIdea("cis"+ name +".position.y",0D,Idea.guessType("QualityDimension",null,1.0,0.5)));
        cis.add(position);
        Idea fov = Idea.createIdea("cis"+ name +".FOV","", Idea.guessType("Property", null,1.0,0.5));
        Idea bounds = Idea.createIdea("cis"+ name +".FOV.bounds","", Idea.guessType("Property", null,1.0,0.5));
        bounds.add(Idea.createIdea("cis"+ name +".FOV.bounds.x",null, Idea.guessType("Property", null,1.0,0.5)));
        bounds.add(Idea.createIdea("cis"+ name +".FOV.bounds.y",null, Idea.guessType("Property", null,1.0,0.5)));
        bounds.add(Idea.createIdea("cis"+ name +".FOV.bounds.height",null, Idea.guessType("Property", null,1.0,0.5)));
        bounds.add(Idea.createIdea("cis"+ name +".FOV.bounds.width",null, Idea.guessType("Property", null,1.0,0.5)));
        fov.add(bounds);
        fov.add(Idea.createIdea("cis"+ name +".FOV.npoints",0, Idea.guessType("Property", null,1.0,0.5)));
        fov.add(Idea.createIdea("cis"+ name +".FOV.points","", Idea.guessType("Property", null,1.0,0.5)));
        cis.add(fov);
        innerSenseMO = createMemoryObject(INNER, cis);

        registerMemory(innerSenseMO, "Sensory");
        closestAppleMO = createMemoryObject("closestAppleMO");
        registerMemory(closestAppleMO, "Working");
        List<Thing> knownApples = Collections.synchronizedList(new ArrayList<Thing>()); //new
        knownApplesMO = createMemoryObject("knownApplesMO", knownApples); //new
        registerMemory(knownApplesMO, "Working");
       
        Codelet vision = new Vision(creature);
        vision.addOutput(visionMO);
        insertCodelet(vision);
        registerCodelet(vision, "Sensors");
       
        Codelet innerSense = new InnerSense(creature);
        innerSense.addOutput(innerSenseMO);
        insertCodelet(innerSense);
        registerCodelet(innerSense, "Sensors");
       
        Codelet appleDetector = new AppleDetector();
        appleDetector.addInput(visionMO);
        appleDetector.addOutput(knownApplesMO);
        insertCodelet(appleDetector);
        registerCodelet(appleDetector, "Perception");
       
        Codelet closestAppleDetector = new ClosestAppleDetector();
        closestAppleDetector.addInput(knownApplesMO);
        closestAppleDetector.addInput(innerSenseMO);
        closestAppleDetector.addOutput(closestAppleMO);
        insertCodelet(closestAppleDetector);
        registerCodelet(closestAppleDetector, "Perception");
       
        Codelet goToClosestApple = new GoToClosestApple();
        goToClosestApple.addInput(closestAppleMO);
        goToClosestApple.addInput(innerSenseMO);
        goToClosestApple.addOutput(legsMC);
        insertCodelet(goToClosestApple);
        registerCodelet(goToClosestApple, "Behaviors");
       
        Codelet eatClosestApple = new EatClosestApple();
        eatClosestApple.addInput(closestAppleMO);
        eatClosestApple.addInput(innerSenseMO);
        eatClosestApple.addOutput(handsMO);
        eatClosestApple.addOutput(knownApplesMO);
        insertCodelet(eatClosestApple);
        registerCodelet(eatClosestApple, "Behaviors");
       
        Codelet explore = new Explore();
        explore.addInput(knownApplesMO);
        explore.addOutput(legsMC);
        insertCodelet(explore);
        registerCodelet(explore, "Behaviors");
       
        Codelet legsActionCodelet = new LegsActionCodelet(creature);
        legsActionCodelet.addInput(legsMC);
        insertCodelet(legsActionCodelet);
        registerCodelet(legsActionCodelet, "Motor");
       
        Codelet handsActionCodelet = new HandsActionCodelet(creature);
        handsActionCodelet.addInput(handsMO);
        insertCodelet(handsActionCodelet);
        registerCodelet(handsActionCodelet, "Motor");
       
        for (Codelet c : this.getCodeRack().getAllCodelets()) {
            c.setTimeStep(200);
        }
        start();
    }
}
