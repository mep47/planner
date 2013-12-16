package ppcg;

import java.io.IOException;

import PrologPlusCG.PPCGIO_CLI;
import PrologPlusCG.gui.PrologPlusCGFrame;
import PrologPlusCG.prolog.PPCGEnv;

public class SpaceLoader {
    
    // Construct the application
    PPCGEnv env = null;
    PrologPlusCGFrame prologFrame = null;
    
    private void runCLI() {
        prologFrame.LoadFile("SimpleExample.plgCG");
        prologFrame.PurgeMemory();
    }
    
      public SpaceLoader() throws IOException
      {
                    env = new PPCGEnv();
                    PPCGIO_CLI io = new PPCGIO_CLI(env);
                    env.io = io;
                    prologFrame = new PrologPlusCGFrame(env, false);
      }

      public static void main(String[] args) {

          try {
                  SpaceLoader cli = new SpaceLoader();
                  cli.runCLI();
          } catch (Throwable e) {
                  e.printStackTrace();
          }
	}

}
