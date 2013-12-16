package plan.sim;

import sim.field.grid.SparseGrid2D;

public class Container {
	
	private SparseGrid2D grid;
	private String name;
	public int width = 50;;
	public int height = 25;
	
	public Container(SparseGrid2D grid, String name)
	{
		this.grid = grid;
		this.name = name;
	}

	public void setGridLocation(int x, int y)
	{
		grid.setObjectLocation(this, x, y);
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}

}
