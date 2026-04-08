package actionselection.codelets.cognition;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import java.util.Arrays;
import java.util.List;

public class ActionSelectionCodelet extends Codelet {

    
    private Memory LightIntensityMO;
    private Memory WheelsVelocityMO;
    
    public ActionSelectionCodelet() {
        this.name = "ActionSelectionCodelet";
    }

    @Override
    public void accessMemoryObjects() {
        
        LightIntensityMO = getInput("LightIntensityMO");
        
        WheelsVelocityMO = getOutput("WheelsVelocityMO");
    }

    @Override
    public void calculateActivation() {

    }

    @Override
    public void proc() {
        List<Double> intensityValues = (List<Double>) LightIntensityMO.getI();
        
        Double intensityLeft = intensityValues.get(0); 
        Double intensityRight = intensityValues.get(1); 
//        paralel, the more, the less - love
        double vLeft = calculateVelocity(intensityLeft, false);
        double vRight = calculateVelocity(intensityRight, false);
        List<Double> wheelsVelocity = Arrays.asList(vLeft, vRight);
        WheelsVelocityMO.setI(wheelsVelocity);
    }
    
    
    public static Double calculateVelocity(Double lightIntensity, Boolean more){ 
        double minimumIntensity = 0.001061032954;
        double maximumIntensity = 0.1;
        
        double minimumVelocity = 0;
        double maximumVelocity = 5;
        
        // Interpolação linear para mapear intensidade da luz para a velocidade
        double velocity = 0.0;
        if (more == true) {
            velocity = minimumVelocity + ((lightIntensity - minimumIntensity) / (maximumIntensity - minimumIntensity)) * (maximumVelocity - minimumVelocity);
        } else {
            velocity = maximumVelocity - ((lightIntensity - minimumIntensity) / (maximumIntensity - minimumIntensity)) * (maximumVelocity - minimumVelocity);
        }
        velocity = Math.max(minimumVelocity, Math.min(maximumVelocity, velocity));
        return velocity;
    }
}
