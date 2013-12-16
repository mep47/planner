package plan.sim;

import sim.field.grid.SparseGrid2D;
import sim.util.Double2D;
import sim.util.Int2D;

public class Crane {
	
	private SparseGrid2D grid;
	private String name;
	private Location location;
	private Container container;
	
	public Crane(SparseGrid2D grid, String name)
	{
		this.grid = grid;
		this.name = name;
		this.location = location;
	}
	
	public Container takeFrom(Pile pile)
	{
		container = pile.pop();
		Int2D loc = grid.getObjectLocation(this);
		int x = loc.x + 40;
		int y = loc.y - 40;
		grid.setObjectLocation(container, x, y);
		
		return container;
	}
	
	public Container putOn(Pile pile)
	{
		Container tmpContainer = null;
		if(container != null)
		{
			tmpContainer = container;
			pile.add(container);
			container = null;
		}
		
		return tmpContainer;
	}
	
	public Container loadOn(Robot robot)
	{
		robot.receive(container);
		
		return robot.getContainer();
	}
	
	public Container unload(Robot robot)
	{
//		System.out.println("Crane.unload");
		Container tmpContainer = null;
	
		if(robot != null)
		{
			tmpContainer = robot.unload();
			
			if(tmpContainer != null)
			{
				Int2D loc = grid.getObjectLocation(this);
				int x = loc.x + 40;
				int y = loc.y - 40;
				grid.setObjectLocation(tmpContainer,x,y);
				
				this.container = tmpContainer;
			}
		}
		
		return tmpContainer;

	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Container getContainer() {
		return container;
	}

	public void setContainer(Container container) {
		this.container = container;
	}

}
