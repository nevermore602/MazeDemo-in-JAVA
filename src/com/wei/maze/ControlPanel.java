package com.wei.maze;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout.Constraints;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.wei.maze.MazePaintPanel.MazeDirection;


public class ControlPanel extends JPanel {
	
	private final int cpWidth=250;
	private final int cpHeight=600;

	private GridBagLayout gridBag;
	private JButton maze_cls;
	private GridBagConstraints constraintsZero;
	private JButton runByCom;
	private GridBagConstraints constraintsRun;
	
	private JList<String> SelectList;
	private ScrollPane selectPane;
	private int select;
	private DefaultListModel<String> listModel;
	private Vector<Integer> saveVector;
	private GridBagConstraints constranintsSELECT;
	private JButton showSelect;
	private GridBagConstraints constranintsSHOW;
	
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
		
		int temp=MazePaintPanel.FIFTY;
		int i=0;
		saveVector=new Vector<Integer>();
		listModel= new DefaultListModel<String>();
		while(temp>=10)
		{
			if(MazePaintPanel.paintWidth%temp==0)
			{
				int m=MazePaintPanel.paintWidth/temp;
				saveVector.add(m);
				listModel.add(i,(temp-2)+" X "+(temp-2)+"大小的迷宫");
				i++;
			}
			temp--;
		}
		SelectList=new JList<String>(listModel);
		SelectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		SelectList.setFont(new Font(null, Font.BOLD, 20));
		selectPane=new ScrollPane();
		selectPane.add(SelectList);
		selectPane.setPreferredSize(new Dimension(220, 250));
		//selectPane.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
		selectPane.setFocusable(false);
		constranintsSELECT=new GridBagConstraints();
		constranintsSELECT.weightx=100;
		constranintsSELECT.weighty=100;
		constranintsSELECT.gridx=0;
		constranintsSELECT.gridy=1;
		constranintsSELECT.gridheight=1;
		constranintsSELECT.gridwidth=GridBagConstraints.REMAINDER;
		constranintsSELECT.fill= GridBagConstraints.NONE;
		this.add(selectPane, constranintsSELECT);
		
	    showSelect=new JButton("生成迷宫");
	    showSelect.setPreferredSize(new Dimension(220, 50));
	    showSelect.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
	    showSelect.setFocusable(false);
		constranintsSHOW=new GridBagConstraints();
		constranintsSHOW.weightx=100;
		constranintsSHOW.weighty=100;
		constranintsSHOW.gridx=0;
		constranintsSHOW.gridy=2;
		constranintsSHOW.gridheight=1;
		constranintsSHOW.gridwidth=GridBagConstraints.REMAINDER;
		constranintsSHOW.fill= GridBagConstraints.NONE;
		this.add(showSelect, constranintsSHOW);
		
		runByCom=new JButton("自动寻路");
		runByCom.setPreferredSize(new Dimension(220, 50));
		runByCom.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
		runByCom.setFocusable(false);
		constraintsRun=new GridBagConstraints();
		constraintsRun.weightx=100;
		constraintsRun.weighty=100;
		constraintsRun.gridx=0;
		constraintsRun.gridy=3;
		constraintsRun.gridheight=1;
		constraintsRun.gridwidth=GridBagConstraints.REMAINDER;
		constraintsRun.fill= GridBagConstraints.NONE;
		this.add(runByCom, constraintsRun);
	}
	
	public void setMazeSizeListener(final MazeShowPanel p)
	{

		
		SelectList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
			
				select=SelectList.getSelectedIndex();
			}
		});
		
		showSelect.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO 自动生成的方法存根
				p.setMazeSize(saveVector.get(select));
			}
		});
		
		
		maze_cls.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				p.setMazeSize(MazePaintPanel.NONE);
			}
		});
	}
	
	public void setRunListener(final MazeShowPanel p)
	{
		runByCom.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				p.computerRun();
			}
		});
	}

}
