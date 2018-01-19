import delaunay.DelaunayTriangulator;
import delaunay.NotEnoughPointsException;
import delaunay.Triangle2D;
import delaunay.Vector2D;
import org.nlogo.api.Argument;
import org.nlogo.api.Context;
import org.nlogo.api.ExtensionException;
import org.nlogo.api.Reporter;
import org.nlogo.core.LogoList;
import org.nlogo.core.Syntax;
import org.nlogo.core.SyntaxJ;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The reporter which receives two LogoList:
 * one is owner`s vertex [x1, y1], the other is all turtles`s vertex [[x1, y1] [x2, y2] ... [xn, yn]].
 * The reporter will return the number of neighbours of the owner vertex.
 *
 * @author Yuchen Chiang
 */
public class NeighboursReporter implements Reporter {

    private static Logger logger = Logger.getLogger("NeighboursReporter");

    @Override
    public Syntax getSyntax() {
        return SyntaxJ.reporterSyntax(new int[] {Syntax.ListType(), Syntax.ListType()}, Syntax.NumberType());
    }

    @Override
    public Object report(Argument[] args, Context context) {

        LogoList allCorList = null;
        LogoList singleCorList = null;
        List<Triangle2D> triangleList = null;
        List<Vector2D> pointSet = new ArrayList<>();
        Set<Vector2D> neighboursSet = new HashSet<>();

        logger.setLevel(Level.INFO);
        FileHandler handler = null;
        try {
            handler = new FileHandler("C:\\Users\\Yuche\\Documents\\Silver_Data\\UAV\\Patent\\Code\\UAV\\log\\UAV.log");
        } catch (IOException ioe) {
            System.out.println("err...");
        }
        logger.addHandler(handler);
        logger.info("Reporter Init...");

        try {
            allCorList = args[1].getList();
            singleCorList = args[0].getList();
        } catch (ExtensionException ee) {
            System.out.println(ee.getMessage());
        }

        logger.info("allCorList size="+allCorList.size()+"/n"+"singleCorList size="+singleCorList.size());

        for (int i = 0; i < allCorList.length(); i++) {
            LogoList corList = (LogoList) allCorList.get(i);
            pointSet.add(new Vector2D((Double) corList.get(0), (Double) corList.get(1)));
        }
        Vector2D vertex = new Vector2D((Double) singleCorList.get(0), (Double) singleCorList.get(1));

        try {
            DelaunayTriangulator delaunayTriangulator = new DelaunayTriangulator(pointSet);
            delaunayTriangulator.triangulate();
            triangleList = delaunayTriangulator.getTriangles();
        } catch (NotEnoughPointsException npe) {
            System.out.println(npe.getMessage());
        }

        for (Triangle2D triangle : triangleList) {
            if (triangle.a == vertex) {
                neighboursSet.add(triangle.b);
                neighboursSet.add(triangle.c);
            } else if (triangle.b == vertex) {
                neighboursSet.add(triangle.a);
                neighboursSet.add(triangle.c);
            } else if (triangle.c == vertex){
                neighboursSet.add(triangle.a);
                neighboursSet.add(triangle.b);
            }
        }

        return neighboursSet.size();
    }
}
