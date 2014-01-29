package plan.sim;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import aminePlatform.util.cg.CG;

public class Planner {
	
	String sourceLocation = null;
	String targetLocation = null;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public CG forwardSearch(Set<CG> actions, CG initialState, CG goalState)
	{
		
		CG workingState = null;;
		CG workingPlan = null;;
		Set<CG> applicableActions = new HashSet<CG>();
		
		boolean finished = false;

		while(!finished)
		{
			if(workingState.equal(goalState))
			{
				finished = true;
				continue;
			}
			applicableActions = selectApplicableActions(actions);
			if(applicableActions.size() == 0)
			{
				finished = true;
				continue;
			}
			CG action = chooseAction(applicableActions);
			workingState = updateState(workingState, action);
			workingPlan = updatePlan(workingPlan, action);
			
		}
		
		return workingPlan;
	}
	
	private Set<CG> selectApplicableActions(Set<CG> actions)
	{
		Set<CG> applicableActions = new HashSet<CG>();
		
		return applicableActions;
	}
	
	private CG chooseAction(Set<CG> applicableActions)
	{
		CG selectedAction = null;
		
		return selectedAction;
	}
	
	private CG updateState(CG workingState, CG action)
	{
		CG updatedState = null;
		
		return updatedState;
	}
	
	private CG updatePlan(CG workingPlan, CG action)
	{
		CG updatedPlan = null;
		
		return updatedPlan;
	}

}
