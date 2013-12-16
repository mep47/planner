package plan.sim;

import java.awt.Color;

import sim.field.grid.SparseGrid2D;
import sim.util.Double2D;
import sim.util.Int2D;

public class Location {
	
	private String name;
	private SparseGrid2D grid;
	public Double2D loc;
	Color color;
	int width;
	int height;
	private Crane crane;
	private Robot robot;
	private Pile pile;
	
	public Location(String name, SparseGrid2D grid, Color color, Double2D loc, int width, int height)
	{
		this.name = name;
		this.grid = grid;
		this.color = color;
		this.loc = loc;
		this.width = width;
		this.height = height;
		
		grid.setObjectLocation(this,  round(loc.x), round(loc.y));
	}
	
	public Robot arrive(Robot robot)
	{
		Robot retval = null;
		
		if(this.robot == null)
		{
			this.robot = robot;
			retval = robot;
		}
		
		return retval;
	}
	
	public void leave()
	{
		robot = null;
	}
	
	public Crane add(Crane crane)
	{
		this.crane = crane;
		Int2D loc = grid.getObjectLocation(this);
		int x = loc.x;
		int y = loc.y - 50;
		grid.setObjectLocation(crane,  x, y);
		
		return crane;
	}
	
	public Pile add(Pile pile)
	{
		this.pile = pile;
		Int2D loc = grid.getObjectLocation(this);
		int x = loc.x - 50;
		int y = loc.y + 50;
		grid.setObjectLocation(pile, x, y);
		return pile;
		
	}
	
	private int round(double d){
	    double dAbs = Math.abs(d);
	    int i = (int) dAbs;
	    double result = dAbs - (double) i;
	    if(result<0.5){
	        return d<0 ? -i : i;            
	    }else{
	        return d<0 ? -(i+1) : i+1;          
	    }
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public SparseGrid2D getGrid() {
		return grid;
	}

	public void setGrid(SparseGrid2D grid) {
		this.grid = grid;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
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

	public Crane getCrane() {
		return crane;
	}
	public void setCrane(Crane crane) {
		this.crane = crane;
	}
	public Robot getRobot(String robotName) {
		return robot;
	}
	public void setRobot(Robot robot) {
		this.robot = robot;
	}
	public Pile getPile() {
		return pile;
	}
	public void setPile(Pile pile) {
		this.pile = pile;
	}

}
