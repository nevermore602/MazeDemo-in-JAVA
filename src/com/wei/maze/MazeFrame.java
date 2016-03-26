package com.wei.maze;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MazeFrame extends JFrame {


	private final int fWidth=1100;
	private final int fHeight=700;
	private FlowLayout frameflow;
	private ImageIcon icon;
	private BackPanel framePanel;
	private MazeShowPanel mazeShow;
	private ControlPanel control;
	
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO 自动生成的方法存根
				MazeFrame mazeFrame = new MazeFrame();
				
			}
		});
		
	}
	
	
	
	public MazeFrame() {
		// TODO 自动生成的构造函数存根
		super();
		this.setTitle("MAZE");
		frameflow=new FlowLayout();
		icon=new ImageIcon("res\\student.png");
		framePanel= new BackPanel();
		framePanel.setLayout(frameflow);
		this.add(framePanel);
		
		
		//设置窗口位置和大小
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int screenX = screenSize.width/2-fWidth/2;
		int screenY = screenSize.height/2-fHeight/2;
		setBounds(screenX, screenY,fWidth,fHeight);
		
		//设置窗口控制属性
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setIconImage(icon.getImage());
		
		//设置布局管理器
		frameflow.setHgap(50);
		frameflow.setVgap(35);
		
		//添加迷宫控制面板和迷宫显示面板
		mazeShow=new MazeShowPanel();
		control=new ControlPanel();
		framePanel.add(mazeShow);
		framePanel.add(control);
		control.setMazeSizeListener(mazeShow);
		
		this.setVisible(true);
	}
	
	
}

class BackPanel extends JPanel
{
	private ImageIcon frameBack;
	@Override
	protected void paintComponent(Graphics g) {
		// TODO 自动生成的方法存根
		super.paintComponent(g);
		frameBack=new ImageIcon("res\\背景.png");
		g.drawImage(frameBack.getImage(),0, 0, null);
		
	}
}

