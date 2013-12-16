package plan.sim;

import java.util.Stack;

import sim.field.grid.SparseGrid2D;
import sim.util.Double2D;
import sim.util.Int2D;

public class Pile {
	
	private SparseGrid2D grid;
	private String name;
	public int width = 80;
	public int height = 10;
	
	private Stack<Container> containers;
	
	public Pile(SparseGrid2D grid, String name)
	{
		containers = new Stack<Container>();
		this.grid = grid;
		this.name = name;
	}
	
	public Container pop()
	{
		return containers.pop();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Stack<Container> getContainers() {
		return containers;
	}

	public void setContainers(Stack<Container> containers) {
		this.containers = containers;
	}

	public Container add(Container container)
	{
		containers.push(container);
		
		Int2D loc = grid.getObjectLocation(this);
		int x = loc.x;
		int y = loc.y - ((containers.size() - 1) * container.height) - (container.height / 2 + height / 2);
		container.setGridLocation(x, y);
		
		return container;
		
	}
	
	public Container remove(Container container)
	{
		return containers.pop();
	}

}
