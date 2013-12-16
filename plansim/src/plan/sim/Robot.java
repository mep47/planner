package plan.sim;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.field.grid.SparseGrid2D;
import sim.util.Double2D;
import sim.util.Int2D;

public class Robot implements Steppable
{
	private static final long serialVersionUID = 1L;
	
	private SparseGrid2D grid;
	private String name;
	private Location location;
	private Container container;
	
	public Robot(SparseGrid2D grid, String name)
	{
		this.grid = grid;
		this.name = name;
	}
	
	@Override
	public void step(SimState sim)
	{
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Robot moveTo(Location location)
	{
		
		Robot robot =location.arrive(this);
		if(robot != null)
		{
			if(this.location != null)
			{
				this.location.leave();
			}
			this.location = location;
			Int2D loc = grid.getObjectLocation(location);
			int x = loc.x + 20;
			int y = loc.y + 40;
			grid.setObjectLocation(this,  x, y);
			if(container != null)
			{
				int cx = x + 40;
				int cy = y + 12;
				grid.setObjectLocation(container, cx, cy);
			}
		}
		
		return robot;
	}
	
	public Container receive(Container container)
	{

		if(this.container == null)
		{
			this.container = container;
			Int2D loc = grid.getObjectLocation(this);
			int x = loc.x + 40;
			int y = loc.y + 12;
			grid.setObjectLocation(container, x, y);
		}
		
		return container;
	}
	
	public Container unload()
	{
		Container tmpContainer = null;
		
		tmpContainer = container;
		container = null;
		
		return tmpContainer;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Container getContainer() {
		return container;
	}

	public void setContainer(Container container) {
		this.container = container;
	}

}
