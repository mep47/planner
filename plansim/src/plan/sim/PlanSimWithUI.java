package plan.sim;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import sim.display.Console;
import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.portrayal.grid.SparseGridPortrayal2D;
import sim.portrayal.simple.AdjustablePortrayal2D;
import sim.portrayal.simple.RectanglePortrayal2D;
import sim.portrayal.DrawInfo2D;

public class PlanSimWithUI extends GUIState {
	
    public Display2D display;
    public JFrame displayFrame;
    public BufferedImage robotImage;
    public BufferedImage robot2Image;
    public BufferedImage craneImage;
    
    SparseGridPortrayal2D planPortrayal = new SparseGridPortrayal2D();
	
	public PlanSimWithUI() { super(new PlanSim( System.currentTimeMillis())); }
	
	public PlanSimWithUI(SimState state) { super(state); }
	
	public static String getName() { return "Plan Simulation"; }

	public static void main(String[] args)
	{
	    PlanSimWithUI vid = new PlanSimWithUI();
	    Console c = new Console(vid);
	    c.setVisible(true);
	}
	
	public void start()
	{
		super.start();
//		System.out.println(System.getProperty("user.dir"));
		try {
			robotImage = ImageIO.read(new File("images/truck.png"));
			robot2Image = ImageIO.read(new File("images/truckPart2.png"));
			craneImage = ImageIO.read(new File("images/crane.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setupPortrayals();
	}
	
	public void setupPortrayals()
	{
		PlanSim sim = (PlanSim) state;

		planPortrayal.setField(sim.area);
		
		planPortrayal.setPortrayalForClass(Location.class,
		  new AdjustablePortrayal2D(
			new RectanglePortrayal2D()
			{
			  	private static final long serialVersionUID = 1L;
			  	    		
			  	public void draw(Object object, Graphics2D graphics, DrawInfo2D info)
			  	{
	    			double x = Math.round(info.draw.x);
	    			double y = Math.round(info.draw.y);
	    			double w = ((Location)object).getWidth();
	    			double h = ((Location)object).getHeight();
	    			
	    			Rectangle2D rect = info.draw;
	    			rect.setRect(x, y, w, h);
	    			
	    			paint = ((Location)object).getColor();
	    			filled = true;
	    			
			  		super.draw(object,graphics,info);
			  	}
			}
		));
		
		planPortrayal.setPortrayalForClass(Pile.class,
		  new AdjustablePortrayal2D(
			new RectanglePortrayal2D()
			{
			  	private static final long serialVersionUID = 1L;
			  	    		
			  	public void draw(Object object, Graphics2D graphics, DrawInfo2D info)
			  	{
			  		Pile pile = (Pile) object;
	    			double x = Math.round(info.draw.x);
	    			double y = Math.round(info.draw.y);
	    			double w = pile.width;
	    			double h = pile.height;
	    			
	    			Rectangle2D rect = info.draw;
	    			rect.setRect(x, y, w, h);
	    			
	    			paint = new Color(153, 76, 0);
	    			filled = true;
//	    			System.out.println("pile portrayal: "+x+" "+y+" "+w+" "+h);
	    			
			  		super.draw(object,graphics,info);
			  	}
			}
		));
		
	    planPortrayal.setPortrayalForClass(Robot.class,
	      new AdjustablePortrayal2D(
	    	new RectanglePortrayal2D()
	    	{
	    		private static final long serialVersionUID = 1L;
	    		
	    		public void draw(Object object, Graphics2D graphics, DrawInfo2D info)
	    		{
	    			
	    			int x = (int)Math.round(info.draw.x);
	    			int y = (int)Math.round(info.draw.y);
	    			int w = robotImage.getWidth();
	    			int h = robotImage.getHeight();
	    			
	    			graphics.drawImage(robotImage, x, y, w, h, null);
	    			
	    			x += robotImage.getWidth();
	    			graphics.drawImage(robot2Image, x, y, w, h, null);
	    		}
	    	}
	    ));
	    
	    planPortrayal.setPortrayalForClass(Crane.class,
	      new AdjustablePortrayal2D(
	    	new RectanglePortrayal2D()
	    	{
	    		private static final long serialVersionUID = 1L;
	    		
	    		public void draw(Object object, Graphics2D graphics, DrawInfo2D info)
	    		{
	    			
	    			int x = (int)Math.round(info.draw.x) - 40;
	    			int y = (int)Math.round(info.draw.y) - 100;
	    			int w = 104;
	    			int h = 121;
	    			
	    			graphics.drawImage(craneImage, x, y, w, h, null);
	    		}
	    	}
	    ));
		
		planPortrayal.setPortrayalForClass(Container.class,
		  new AdjustablePortrayal2D(
			new RectanglePortrayal2D()
			{
			  	private static final long serialVersionUID = 1L;
			  	    		
			  	public void draw(Object object, Graphics2D graphics, DrawInfo2D info)
			  	{
			  		Container ct1 = (Container) object;
	    			double x = Math.round(info.draw.x);
	    			double y = Math.round(info.draw.y);
	    			double w = ((Container)object).getWidth();
	    			double h = ((Container)object).getHeight();
	    			
	    			Rectangle2D rect = info.draw;
	    			rect.setRect(x, y, w, h);
	    			
	    			paint = Color.RED;
	    			filled = true;
//	    			System.out.println("container portrayal: "+x+" "+y+" "+w+" "+h);

	    			graphics.setStroke(new BasicStroke(2));	    			
			  		super.draw(object,graphics,info);
			  		
			  		paint = Color.BLACK;
			  		filled = false;
			  		super.draw(object,graphics,info);
			  	}
			}
		));
    
	    
	    // reschedule the displayer
	    display.reset();
	    display.setBackdrop(Color.white);

	    // redraw the display
	    display.repaint();

	}

    public void init(Controller c)
        {
        super.init(c);

        // make the displayer
        display = new Display2D(PlanSim.LOC_MAX_X, PlanSim.LOC_MAX_Y, this);
        // turn off clipping
        display.setClipping(false);

        displayFrame = display.createFrame();
        displayFrame.setTitle("Plan Display");
        c.registerFrame(displayFrame);   // register the frame so it appears in the "Display" list
        displayFrame.setVisible(true);
        display.attach( planPortrayal, "Locations" );
        }
	
	public void quit()
    {
	    super.quit();
	
	    if (displayFrame!=null) displayFrame.dispose();
	    displayFrame = null;
	    display = null;
    }

}