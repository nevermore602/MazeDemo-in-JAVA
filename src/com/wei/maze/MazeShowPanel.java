package com.wei.maze;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.geom.Point2D;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.wei.maze.MazePaintPanel.MazeSize;

public class MazeShowPanel extends JPanel {
	
	//private final int mpsX=40;
	//private final int mpsY=35;
	private final int mpsWidth=700;
	private final int mpsHeight=600;
	private FlowLayout showflow;
	private MazePaintPanel paintPanel;
	private RunInMaze run;
	
	public MazeShowPanel() {
		// TODO 自动生成的构造函数存根
		super();
		this.setPreferredSize(new Dimension(mpsWidth,  mpsHeight));
		this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2),"MAZE SHOW"));
		this.setBackground(Color.PINK);
		
		showflow=new FlowLayout();
		this.setLayout(showflow);
		showflow.setAlignment(FlowLayout.CENTER);
		showflow.setVgap(30);
		
		paintPanel=new MazePaintPanel();
		this.add(paintPanel);
		
		run=new RunInMaze(paintPanel);
	}
	
	public void setMazeSize(MazeSize maze_size)
	{
		paintPanel.setMazeSize(maze_size);
		run.runAgain();
	}
	
	
	public void computerRun()
	{
		run.runByComputer();	
	}

}
