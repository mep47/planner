package ppcg;

import java.io.IOException;
import java.util.Hashtable;

import PrologPlusCG.PPCGIO_CLI;
import PrologPlusCG.cgspace.SpaceManager;
import PrologPlusCG.gui.PrologPlusCGFrame;
import PrologPlusCG.prolog.PPCGEnv;

public class SpaceQuery {
	
    PPCGEnv env = null;
    PrologPlusCGFrame prologFrame = null;
	
	public SpaceQuery() throws IOException
	{
        env = new PPCGEnv();
        PPCGIO_CLI io = new PPCGIO_CLI(env);
        env.io = io;
        prologFrame = new PrologPlusCGFrame(env, false);		
	}
	
	private void runCLI() throws InterruptedException
	{
//		String[] queries = {"[Location: w] - -loc->[Coordinates: x], -attr1->[Dimensions: y], -attr2->[Color: c].",
//							"[Location: w] -contains1->[Crane: x].",
//							"[Location: w] -contains2->[Pile: x].",
//							"[Location: w] -contains3->[Robot: x]."};
		
		String[] queries = {"[Car: x] - -part->[Door: y], -part->[Fender: z]."};
		

		for(String qry: queries)
		{
			SpaceManager.loadSpace("Planning.db4o", env);
			java.util.Vector<Hashtable<String, String> > vec = prologFrame.Resolve(qry, true);
			

	        if (vec != null) 
	        {
	            for (int i = 0; i < vec.size(); ++i)
	            {
	                    System.out.println(vec.get(i).toString());
	            }
	        } else {
	                System.out.println("No output.");
	        }
	        prologFrame.PurgeMemory();
		}
		

	}
	
//	"[Location: w] - -loc->[Coordinates: x], -attr->[Dimensions: y]."
//	"[Location: w] - -attr->[Color: c]."

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        try {
            SpaceQuery cli = new SpaceQuery();
            cli.runCLI();
    } catch (Throwable e) {
            e.printStackTrace();
    }

	}

}
