package controller.sim;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aminePlatform.engines.prologPlusCG.interpreter.Interpreter;
import aminePlatform.kernel.lexicons.Lexicon;
import aminePlatform.kernel.ontology.Individual;
import aminePlatform.util.Variable;

import plan.sim.Crane;
import plan.sim.Location;
import plan.sim.Pile;
import plan.sim.Robot;
import plan.sim.Container;

public class ActionDictionary {
	
	static private final String queryPart1 = "[Plan: Plan1]- -part->[Step]- -attr->[SequenceNumber =";
	
	static private final String moveSituation = "], -attr->[Action: Move]- -from->[Location: f], -dest->[Location: d], -agnt->[Robot: r].";
	static private final String takeSituation = "], -attr->[Action: Take]- -from->[Pile: p], -agnt->[Crane: c].";
	static private final String loadSituation = "], -attr->[Action: Load]- -dest->[Robot: r], -agnt->[Crane: c].";
	static private final String unloadSituation = "], -attr->[Action: Unload]- -from->[Robot: r], -agnt->[Crane: c].";
	static private final String putSituation = "], -attr->[Action: Put]- -dest->[Pile: p], -agnt->[Crane: c].";
	
	static private Map<Variable, Object> performQuery(Interpreter interpreter,String graph)
	{
		Map<Variable, Object> result = null;

		try {
			List<Map<Variable, Object>> allSolutions = interpreter.findAllSolutions(graph);			
			if(allSolutions != null)
			{
				result = allSolutions.get(0);
			}
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;

	}
	
	static public void move(Interpreter interpreter, HashMap<String,Location> locations, int stepNumber)
	{
		try {
			Thread.sleep(2000L);
			Lexicon lexicon = interpreter.getLexicon();
			System.out.println("ActionDictionary.move - "+queryPart1+Integer.toString(stepNumber)+moveSituation);
			Map<Variable, Object> result = performQuery(interpreter, queryPart1+Integer.toString(stepNumber)+moveSituation);
			String fromName = ((Individual)result.get(new Variable("f"))).toString(lexicon);
			String destinationName = ((Individual)result.get(new Variable("d"))).toString(lexicon);
			String robotName = ((Individual)result.get(new Variable("r"))).toString(lexicon);

			Robot robot = locations.get(fromName).getRobot(robotName);
			robot.moveTo(locations.get(destinationName));
		} catch (Exception e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static public void load(Interpreter interpreter, Map<String,Object> conceptMap, int stepNumber)
	{
		
		try {
			Thread.sleep(2000L);
			Lexicon lexicon = interpreter.getLexicon();
			System.out.println("ActionDictionary.load - "+queryPart1+Integer.toString(stepNumber)+loadSituation);
			Map<Variable, Object> result = performQuery(interpreter, queryPart1+Integer.toString(stepNumber)+loadSituation);
			String craneName = ((Individual)result.get(new Variable("c"))).toString(lexicon);
			String robotName = ((Individual)result.get(new Variable("r"))).toString(lexicon);

			Crane crane = (Crane)conceptMap.get(craneName);
			Robot robot = (Robot)conceptMap.get(robotName);
			
			crane.loadOn(robot);
		} catch (Exception e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static public void unload(Interpreter interpreter, Map<String,Object> conceptMap, int stepNumber)
	{
		try {
			Thread.sleep(2000L);
			Lexicon lexicon = interpreter.getLexicon();
			System.out.println("ActionDictionary.unload - "+queryPart1+Integer.toString(stepNumber)+unloadSituation);
			Map<Variable, Object> result = performQuery(interpreter, queryPart1+Integer.toString(stepNumber)+unloadSituation);
			String craneName = ((Individual)result.get(new Variable("c"))).toString(lexicon);
			String robotName = ((Individual)result.get(new Variable("r"))).toString(lexicon);

			Crane crane = (Crane)conceptMap.get(craneName);
			Robot robot = (Robot)conceptMap.get(robotName);
			
			crane.unload(robot);
	} catch (Exception e) {
		
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	}
	
	static public void put(Interpreter interpreter, Map<String, Object> conceptMap, int stepNumber)
	{
		try {
			Thread.sleep(2000L);
			Lexicon lexicon = interpreter.getLexicon();
			System.out.println("ActionDictionary.put - "+queryPart1+Integer.toString(stepNumber)+putSituation);
			Map<Variable, Object> result = performQuery(interpreter, queryPart1+Integer.toString(stepNumber)+putSituation);
			String craneName = ((Individual)result.get(new Variable("c"))).toString(lexicon);
			String pileName = ((Individual)result.get(new Variable("p"))).toString(lexicon);
			
			Crane crane = (Crane)conceptMap.get(craneName);
			Pile pile = (Pile)conceptMap.get(pileName);
			Container container = crane.putOn(pile);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	static public void take(Interpreter interpreter, Map<String, Object> conceptMap, int stepNumber)
	{
		try {
			Thread.sleep(2000L);
			Lexicon lexicon = interpreter.getLexicon();
			System.out.println("ActionDictionary.take - "+queryPart1+Integer.toString(stepNumber)+takeSituation);
			Map<Variable, Object> result = performQuery(interpreter, queryPart1+Integer.toString(stepNumber)+takeSituation);
			String craneName = ((Individual)result.get(new Variable("c"))).toString(lexicon);
			String pileName = ((Individual)result.get(new Variable("p"))).toString(lexicon);
			
			Crane crane = (Crane)conceptMap.get(craneName);
			Pile pile = (Pile)conceptMap.get(pileName);
			Container container = crane.takeFrom(pile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
