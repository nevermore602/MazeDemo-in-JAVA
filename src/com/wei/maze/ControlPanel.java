package com.wei.maze;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SpringLayout.Constraints;

import com.wei.maze.MazePaintPanel.MazeSize;

public class ControlPanel extends JPanel {
	
	private final int cpWidth=250;
	private final int cpHeight=600;
	private JButton ten10;
	private GridBagConstraints constraints10;
	private JButton twenty20;
	private GridBagConstraints constraints20;
	private JButton fifty50;
	private GridBagConstraints constraints50;
	private GridBagLayout gridBag;
	private JButton maze_cls;
	private GridBagConstraints constraintsZero;
	public ControlPanel() {
		// TODO 自动生成的构造函数存根
		super();
		//设置面板基本参数
		this.setBackground(Color.lightGray);
		this.setPreferredSize(new Dimension(cpWidth, cpHeight));
		this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK,2),"CONTROL"));
		
		//组件添加及布局设置
		gridBag = new GridBagLayout();
		this.setLayout(gridBag);
		
		ten10=new JButton("生成48x48迷宫");
		ten10.setPreferredSize(new Dimension(220, 50));
		ten10.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
		ten10.setFocusable(false);
		constraints10=new GridBagConstraints();
		constraints10.weightx=100;
		constraints10.weighty=100;
		constraints10.gridx=0;
		constraints10.gridy=3;
		constraints10.gridheight=1;
		constraints10.gridwidth=GridBagConstraints.REMAINDER;
		constraints10.fill= GridBagConstraints.NONE;
		this.add(ten10, constraints10);
		
		twenty20=new JButton("生成23x23迷宫");
		twenty20.setPreferredSize(new Dimension(220, 50));
		twenty20.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
		twenty20.setFocusable(false);
		constraints20=new GridBagConstraints();
		constraints20.weightx=100;
		constraints20.weighty=100;
		constraints20.gridx=0;
		constraints20.gridy=2;
		constraints20.gridheight=1;
		constraints20.gridwidth=GridBagConstraints.REMAINDER;
		constraints20.fill= GridBagConstraints.NONE;
		this.add(twenty20, constraints20);
		
		fifty50=new JButton("生成8x8迷宫");
		fifty50.setPreferredSize(new Dimension(220, 50));
		fifty50.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
		fifty50.setFocusable(false);
		constraints50=new GridBagConstraints();
		constraints50.weightx=100;
		constraints50.weighty=100;
		constraints50.gridx=0;
		constraints50.gridy=1;
		constraints50.gridheight=1;
		constraints50.gridwidth=GridBagConstraints.REMAINDER;
		constraints50.fill= GridBagConstraints.NONE;
		this.add(fifty50, constraints50);
		
		maze_cls=new JButton("清空迷宫");
		maze_cls.setPreferredSize(new Dimension(220, 50));
		maze_cls.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
		maze_cls.setFocusable(false);
		constraintsZero=new GridBagConstraints();
		constraintsZero.weightx=100;
		constraintsZero.weighty=100;
		constraintsZero.gridx=0;
		constraintsZero.gridy=0;
		constraintsZero.gridheight=1;
		constraintsZero.gridwidth=GridBagConstraints.REMAINDER;
		constraintsZero.fill= GridBagConstraints.NONE;
		this.add(maze_cls, constraintsZero);
	}
	
	public void setMazeSizeListener(final MazeShowPanel p)
	{
		ten10.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				p.setMazeSize(MazeSize.ten);
			}
		});
		
		twenty20.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				p.setMazeSize(MazeSize.twenty);
			}
		});
		
		fifty50.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				p.setMazeSize(MazeSize.fifty);
			}
		});
		
		maze_cls.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				p.setMazeSize(MazeSize.none);
			}
		});
	}

}
