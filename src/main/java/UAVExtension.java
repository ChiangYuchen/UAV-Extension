import org.nlogo.api.DefaultClassManager;
import org.nlogo.api.PrimitiveManager;

/**
 * UAV extension class.
 *
 * @author Yuchen Chiang
 */
public class UAVExtension extends DefaultClassManager{

    @Override
    public void load(PrimitiveManager primitiveManager) {
        primitiveManager.addPrimitive("neigh-reporter", new NeighboursReporter());
        primitiveManager.addPrimitive("area-reporter", new AreaReporter());
        primitiveManager.addPrimitive("accel-reporter", new AccelerationReporter());
    }
}
