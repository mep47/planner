package plan.sim;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aminePlatform.engines.prologPlusCG.interpreter.Interpreter;
import aminePlatform.kernel.lexicons.Lexicon;
import aminePlatform.kernel.ontology.Individual;
import aminePlatform.kernel.ontology.Type;
import aminePlatform.util.AmineList;
import aminePlatform.util.Variable;
import aminePlatform.util.cg.CG;

//import PrologPlusCG.PPCGIO_CLI;
//import PrologPlusCG.cgspace.SpaceManager;
//import PrologPlusCG.gui.PrologPlusCGFrame;
//import PrologPlusCG.prolog.PPCGEnv;

import controller.sim.PlanController;

import sim.engine.SimState;
import sim.field.grid.SparseGrid2D;
import sim.util.Double2D;


public class PlanSim extends SimState {
	
	static final long serialVersionUID = -1;
	
	public SparseGrid2D area;
	
	private HashMap<String,Location> locations;
	private Location location = null;
	private Map<String,Object> conceptMap = null;
	
	public final static int LOC_MAX_X = 700;
	public final static int LOC_MAX_Y = 700;
//	
//    private PPCGEnv env = null;
//    private PrologPlusCGFrame prologFrame = null;
    
	
	String ontologyFilePath = "/home/mike/dev/amineKnowledgeBases/PlanningOntology.xml";
//	String ontologyFilePath = "/home/mike/dev/amineKnowledgeBases/test1Ontology.xml";
	String[] ppcgFilePaths = {"/home/mike/dev/amineKnowledgeBases/linearFormGraphs/PlanState2.plgCG"};
//	String[] ppcgFilePaths = {"/home/mike/dev/amineKnowledgeBases/linearFormGraphs/Test1.plgCG"};
	
	Interpreter interpreter;
	Lexicon mainLexicon;

	
	public PlanSim(long seed)
	{
		super(seed);
		
		conceptMap = new HashMap<String,Object>();
		
//        env = new PPCGEnv();
//        PPCGIO_CLI io = new PPCGIO_CLI(env);
//        env.io = io;
//        env.bWriteToDebugTree = true;
//        prologFrame = new PrologPlusCGFrame(env, false);		
    	
    	CG.setFunctionalCG(false);
    	
    	interpreter = new Interpreter(ontologyFilePath, ppcgFilePaths);
    	mainLexicon = interpreter.getLexicon();
}
	
	public void start()
	{
		super.start();
		
		System.out.println("Starting");
		
		area = new SparseGrid2D(LOC_MAX_X, LOC_MAX_Y);
		
		try {
			
			locations = readLocationsT(area);
			
			readCranesT(area);
			
			readPilesT(area);
			
			readRobotsT(area);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		PlanController controller = new PlanController();
		
		schedule.scheduleRepeating(controller);
		
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String,Location> readLocationsT(SparseGrid2D area)
	{
		HashMap<String,Location> locs = new HashMap<String,Location>();
		
		try {
			Location loc = null;
			String lVal = null, xVal = null, yVal = null, hVal = null, wVal = null, rVal = null, gVal = null, bVal = null;
			
			List<HashMap<Variable, Object>> allSolutions = interpreter.findAllSolutions("[Location: l]- -loc->[Coordinate =[[XLoc =x],[YLoc =y]]], -attr->[Dimension=[[Height =h],[Width =w]]], -attr->[Color =[[rgbR =r],[rgbG =g],[rgbB =b]]].");
			
			if(allSolutions != null)
			{
				for(HashMap<Variable, Object> res: allSolutions)
				{
					Set<Variable> vars = res.keySet();
					int keyCount = 0;
					for(Variable var: vars)
					{
						Object tmpVal = res.get(var);
//						tmpVal = tmpVal.substring(1,tmpVal.length()-1); //strip quotes
						
						if(var.getName().equals("l"))
						{
							lVal = ((Individual)tmpVal).toString(mainLexicon);
						}
						else if(var.getName().equals("x"))
						{
							xVal = tmpVal.toString();
						}
						else if(var.getName().equals("y"))
						{
							yVal = tmpVal.toString();
						}
						else if(var.getName().equals("h"))
						{
							hVal = tmpVal.toString();
						}
						else if(var.getName().equals("w"))
						{
							wVal = tmpVal.toString();
						}
						else if(var.getName().equals("r"))
						{
							rVal = tmpVal.toString();
						}
						else if(var.getName().equals("g"))
						{
							gVal = tmpVal.toString();
						}
						else if(var.getName().equals("b"))
						{
							bVal = tmpVal.toString();
						}
						keyCount +=1;
						if(keyCount >= 8)
						{
							keyCount = 0;
							loc = new Location(lVal, area,
								new Color(Integer.parseInt(rVal), Integer.parseInt(gVal), Integer.parseInt(bVal)),
								new Double2D(Double.valueOf(xVal),Double.valueOf(yVal)),
								Integer.parseInt(hVal),
								Integer.parseInt(wVal));
							locs.put(lVal, loc);
						}
					}
				}
			}
			else
			{
				System.out.println("No Locations were found!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return locs;
		
	}
	
	@SuppressWarnings("unchecked")
	private void readRobotsT(SparseGrid2D area) throws Exception
	{
		String lVal = null, rVal = null;

		List<HashMap<Variable, Object>> allSolutions = interpreter.findAllSolutions("[Location: l]-contains->[Robot: r].");
		
		if(allSolutions != null)
		{
			for(HashMap<Variable, Object> res: allSolutions)
			{
				Set<Variable> vars = res.keySet();
				int keyCount = 0;
				for(Variable var: vars)
				{
					Object tmpVal = res.get(var);
//					tmpVal = tmpVal.substring(1,tmpVal.length()-1); //strip quotes
					
					if(var.getName().equals("r"))
					{
						rVal = ((Individual)tmpVal).toString(mainLexicon);
					}
					if(var.getName().equals("l"))
					{
						lVal = ((Individual)tmpVal).toString(mainLexicon);
					}
					keyCount +=1;
					if(keyCount >= 2)
					{
						keyCount = 0;
						location = locations.get(lVal);
						Robot robot = new Robot(area, rVal);
						robot.moveTo(location);
						conceptMap.put(rVal, robot);
					}
				}
			}
		}
		else
		{
			System.out.println("No Robots were found!");
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private void readCranesT(SparseGrid2D area) throws Exception
	{
		String lVal = null, cVal = null;

		List<HashMap<Variable, Object>> allSolutions = interpreter.findAllSolutions("[Location: l]-contains->[Crane: c].");
		
		if(allSolutions != null)
		{
			for(HashMap<Variable, Object> res: allSolutions)
			{
				Set<Variable> vars = res.keySet();
				int keyCount = 0;
				for(Variable var: vars)
				{
					Object tmpVal = res.get(var);
//					tmpVal = tmpVal.substring(1,tmpVal.length()-1); //strip quotes
					
					if(var.getName().equals("c"))
					{
						cVal = ((Individual)tmpVal).toString(mainLexicon);
					}
					if(var.getName().equals("l"))
					{
						lVal = ((Individual)tmpVal).toString(mainLexicon);
					}
					keyCount +=1;
					if(keyCount >= 2)
					{
						keyCount = 0;
						location = locations.get(lVal);
						Crane crane = new Crane(area, cVal);
						location.add(crane);
						conceptMap.put(cVal, crane);
					}
				}
			}
		}
		else
		{
			System.out.println("No Cranes were found!");
		}
		
	}
	
	private void readPilesT(SparseGrid2D area) throws Exception
	{
		String lVal = null, pVal = null;
		AmineList zVal = null;
    	
    	CG.setFunctionalCG(false);

		List<HashMap<Variable, Object>> allSolutions = interpreter.findAllSolutions("[Location: l]-contains->[Pile: p]-contains->[ContainerList =z].");
		
		if(allSolutions != null)
		{
			for(HashMap<Variable, Object> res: allSolutions)
			{
				Set<Variable> vars = res.keySet();
				int keyCount = 0;
				for(Variable var: vars)
				{
					Object tmpVal = res.get(var);
//					tmpVal = tmpVal.substring(1,tmpVal.length()-1); //strip quotes
					
					if(var.getName().equals("p"))
					{
						pVal = ((Individual)tmpVal).toString(mainLexicon);
					}
					if(var.getName().equals("z"))
					{
						zVal = (AmineList)tmpVal;
					}
					if(var.getName().equals("l"))
					{
						lVal = ((Individual)tmpVal).toString(mainLexicon);
					}
					keyCount +=1;
					if(keyCount >= 3)
					{
						keyCount = 0;
						location = locations.get(lVal);
						Pile pile = new Pile(area, pVal);
						location.add(pile);
						conceptMap.put(pVal, pile);
//						List<String> containerList = parseContainerList(tmpVal.)
						AmineList cList = (AmineList)zVal;
						for(Object obj1: cList)
						{
							AmineList subList = (AmineList)obj1;
							for(Object obj2: subList)
							{
								Type type = (Type)obj2;
								String containerName = type.toString(mainLexicon);
								
								Container con = new Container(area, containerName);
								pile.add(con);
								conceptMap.put(containerName,  con);
							}
						}
					}
				}
			}
		}
		else
		{
			System.out.println("No Piles were found!");
		}
		
	}
	
	private Double2D getCoords(String coordsString)
	{

		String[] coordsArray = coordsString.split(" ");
		
		Double coordX = new Double(coordsArray[0].substring(1));
		Double coordY = new Double(coordsArray[1].substring(1));
		
		return new Double2D(coordX, coordY);
	}
	
	private Color getColor(String colorString)
	{
		String[] colorArray = colorString.split(" ");
		
		int rVal = Integer.parseInt(colorArray[1]);
		int gVal = Integer.parseInt(colorArray[2]);
		int bVal = Integer.parseInt(colorArray[3]);
		
		return new Color(rVal, gVal, bVal);
	}
	
	private int getInt(String valString, int index)
	{
		String[] valArray = valString.split(" ");
		
		return Integer.parseInt(valArray[index].substring(1));
	}
	
	public static void main(String[] args)
	{
		SimState state = new PlanSim(System.currentTimeMillis());
		state.start();
		do {
			if (!state.schedule.step(state)) break;
		} while(state.schedule.getSteps() < 25);
			
		System.out.println("Ending Simulation");
		state.finish();
		System.exit(0); 
	}

	public HashMap<String,Location> getLocations() {
		return locations;
	}

	public void setLocations(HashMap<String,Location> locations) {
		this.locations = locations;
	}

	public Map<String, Object> getConceptMap() {
		return conceptMap;
	}

	public void setConceptMap(Map<String, Object> conceptMap) {
		this.conceptMap = conceptMap;
	}

}
