package controller.sim;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aminePlatform.engines.prologPlusCG.interpreter.Interpreter;
import aminePlatform.kernel.lexicons.Lexicon;
import aminePlatform.kernel.ontology.Individual;
import aminePlatform.util.Variable;

import plan.sim.Container;
import plan.sim.Crane;
import plan.sim.Location;
import plan.sim.Pile;
import plan.sim.PlanSim;
import plan.sim.Robot;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Double2D;

public class PlanController implements Steppable {

	private static final long serialVersionUID = 1L;
	
	String ontologyFilePath = "/home/mike/dev/amineKnowledgeBases/PlanningOntology.xml";
	String[] ppcgFilePaths = {"/home/mike/dev/amineKnowledgeBases/linearFormGraphs/PlanState2.plgCG"};

	Interpreter interpreter = new Interpreter(ontologyFilePath, ppcgFilePaths);
	Lexicon mainLexicon = interpreter.getLexicon();
	
	HashMap<String,Location> locations;
	Location location;
	int stepNum = 1;

	@Override
	public void step(SimState state)
	{
		PlanSim sim = (PlanSim) state;
		
		Map<String,Object> conceptMap = sim.getConceptMap();
		
		String queryGraphPart1 = "[Plan: Plan1]-part->[Step]- -attr->[SequenceNumber=";
		String queryGraphPart2 = "], -attr->[Action:a].";

		try {
			List<HashMap<Variable, Object>> allSolutions = interpreter.findAllSolutions(queryGraphPart1+stepNum+queryGraphPart2);			
			if(allSolutions != null)
			{
				for(HashMap<Variable, Object> res: allSolutions)
				{
					String aVal = null;
					Set<Variable> vars = res.keySet();
					int keyCount = 0;
					for(Variable var: vars)
					{
						Object tmpVal = res.get(var);
	//					tmpVal = tmpVal.substring(1,tmpVal.length()-1); //strip quotes
						
						if(var.getName().equals("a"))
						{
							aVal = ((Individual)tmpVal).toString(mainLexicon);
						}
						
						System.out.println("The Plan Action is "+aVal);
						
						if(aVal.equals("Move"))
						{
							ActionDictionary.move(interpreter, sim.getLocations(), stepNum);
						}
						else if(aVal.equals("Take"))
						{
							ActionDictionary.take(interpreter, sim.getConceptMap(), stepNum);
						}
						else if(aVal.equals("Put"))
						{
							ActionDictionary.put(interpreter, sim.getConceptMap(), stepNum);
						}
						else if(aVal.equals("Load"))
						{
							ActionDictionary.load(interpreter, sim.getConceptMap(), stepNum);
						}
						else if(aVal.equals("Unload"))
						{
							ActionDictionary.unload(interpreter, sim.getConceptMap(), stepNum);
						}
					}
				}
			}
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			Thread.sleep(2000L);
			locations = sim.getLocations();
			location = locations.get("Location1");
			Pile pile = location.getPile();
//			if(pile.getContainers().size() == 2)
//			{
//				Crane crane = location.getCrane();
//				Container cont = crane.takeFrom(pile);
//			}
//			if(stepNum == 1)
//			{
//				Thread.sleep(2000L);
//				Robot robot = locations.get("Location2").getRobot();
//				robot.moveTo(locations.get("Location1"));
//			}
//			if(stepNum == 3)
//			{
//				Container container = null;
//				Location location = locations.get("Location1");
//				Crane crane = location.getCrane();
//				Robot robot = location.getRobot("Robot1");
//				if(robot != null)
//				{
//					crane.loadOn(robot);
//				}
//			}
//			if(stepNum == 5)
//			{
//				Thread.sleep(2000L);
//				Robot robot = locations.get("Location1").getRobot("Robot1");
//				robot.moveTo(locations.get("Location2"));
//				robot.getContainer();
//
//			}
//			if(stepNum == 7)
//			{
//				Container container = null;
//				location = locations.get("Location2");
//				Crane crane = location.getCrane();
//				Robot robot = location.getRobot("Robot1");
//				if(crane != null && robot != null)
//				{
//					crane.unload(robot);
//				}
//			}
//			if(stepNum == 9)
//			{
//				location = locations.get("Location2");
//				Crane crane = location.getCrane();
//				pile = location.getPile();
//				Container container = crane.putOn(pile);
//			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		stepNum += 1;
	}
	
}
