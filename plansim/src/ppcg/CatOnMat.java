package ppcg;

import java.io.IOException;
import java.util.Collection;
import java.util.Hashtable;

import PrologPlusCG.PPCGIO_CLI;
import PrologPlusCG.cgspace.SpaceManager;
import PrologPlusCG.gui.PrologPlusCGFrame;
import PrologPlusCG.prolog.PPCGEnv;
import PrologPlusCG.prolog.RuleVector;
import PrologPlusCG.prolog.TypeHierarchy;

public class CatOnMat {
        
        // Construct the application
        PPCGEnv env = null;
        PrologPlusCGFrame prologFrame = null;
          
          public CatOnMat() throws IOException
          {
                        env = new PPCGEnv();
                        PPCGIO_CLI io = new PPCGIO_CLI(env);
                        env.io = io;
                        prologFrame = new PrologPlusCGFrame(env, false);
          }
                
                private void runCLI() throws InterruptedException {
                        prologFrame.LoadFile("WizardOfOz.plgCG");
//                		SpaceManager.loadSpace("Planning.db4o", env);
//System.out.println("Printing entire knowledge base");
//                        Collection<RuleVector> values = env.program.values();
//                        for(RuleVector rv: values)
//                        {
//                        	System.out.println(rv.toString());
//                        }
                        java.util.Vector<Hashtable<String, String> > vec = prologFrame.Resolve("[CITIZEN : x]<-MEMB-[COUNTRY : y].", true);
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
          
          public static void main(String [] args) throws IOException {

                        try {
                                CatOnMat cli = new CatOnMat();
                                cli.runCLI();
                        } catch (Throwable e) {
                                e.printStackTrace();
                        }

          }

}