import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Hanoi extends JFrame implements ActionListener, Runnable
{
	private Tower[] towers = new Tower[3];
	private JButton moveButton = new JButton("Move");
	private Tower selectedTower = null;

	public Hanoi(int numberOfDisks, int animationDelay)
	{
		super("Towers of Hanoi");
		this.setSize(600,300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		int diskHeight = Math.min(20,(this.getHeight()-80)/numberOfDisks);

		JPanel p = new JPanel();
		JPanel towerPanel = new JPanel();

		towerPanel.setLayout(new GridLayout(1,3));
		p.setLayout(new BorderLayout());
		p.add(towerPanel, BorderLayout.CENTER);
		p.add(moveButton, BorderLayout.PAGE_END);
		moveButton.addActionListener(this);
		this.setContentPane(p);

		// Create the towers
		for (int i=0; i<3; i++)
		{
			towers[i] = new Tower(numberOfDisks, animationDelay);
			towers[i].setSize((this.getWidth()-50)/3, this.getHeight()-80);
			towerPanel.add(towers[i]);

			towers[i].addActionListener(this);
		}

		// Create the disks
		for (int i=0; i<numberOfDisks; i++)
		{
			float ratio = (float)(numberOfDisks-i)/(float)numberOfDisks;
			float brightness = 0.2f + ratio * 0.8f;

			towers[0].addDisk(new Disk(new Color(0,brightness,0), (int)((float)towers[0].getWidth()*ratio), diskHeight));
		}

		// Make everything visible
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e)
	{
		JButton b = (JButton)e.getSource();

		moveButton.setEnabled(false);

		if (b == moveButton)
		{
			Thread t = new Thread(this);
        		t.start();
		} 
		else 
		{
			Tower t = (Tower) b;

			// The user is trying to move a disk
			if (this.selectedTower == null)
			{
				if (t.getTowerHeight() > 0)
				{
					this.selectedTower = (Tower) b;
					this.selectedTower.setHighlight(true);
					return;
				}
			} else {

				this.selectedTower.setHighlight(false);
				Disk d = this.selectedTower.removeDisk();

				if (t.addDisk(d))
				{
					this.selectedTower = null;
				}
				else
				{
					this.selectedTower.addDisk(d);
					this.selectedTower.setHighlight(true);
				}
			}
		}

		this.repaint();
	}	

	public void run()
	{
		// Perform a recursive move of all the disks from tower0 to tower2... 
        towers[0].move(towers[2],towers[0].getTowerHeight(), towers);
	}
	
	public static void main(String[] args)
	{
		int disks;
		int speed = 500;

		if (args.length < 1 || args.length > 2)
		{
			usage();
			return;
		}

		try
		{
			disks = Integer.parseInt(args[0]);
			if (args.length == 2)
				speed = Integer.parseInt(args[1]);
		}
		catch (Exception e)
		{
			usage();
			return;
		}


		Hanoi h = new Hanoi(disks, speed);
	}

	private static void usage()
	{
			System.out.println("Usage: java Hanoi <number of disks> [animation_speed (ms)]\n\n");
	}
}
