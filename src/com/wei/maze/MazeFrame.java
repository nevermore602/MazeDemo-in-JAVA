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
		// TODO �Զ����ɵķ������
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO �Զ����ɵķ������
				MazeFrame mazeFrame = new MazeFrame();
				
			}
		});
		
	}
	
	
	
	public MazeFrame() {
		// TODO �Զ����ɵĹ��캯�����
		super();
		this.setTitle("MAZE");
		frameflow=new FlowLayout();
		icon=new ImageIcon("res\\student.png");
		framePanel= new BackPanel();
		framePanel.setLayout(frameflow);
		this.add(framePanel);
		
		
		//���ô���λ�úʹ�С
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int screenX = screenSize.width/2-fWidth/2;
		int screenY = screenSize.height/2-fHeight/2;
		setBounds(screenX, screenY,fWidth,fHeight);
		
		//���ô��ڿ�������
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setIconImage(icon.getImage());
		
		//���ò��ֹ�����
		frameflow.setHgap(50);
		frameflow.setVgap(35);
		
		//����Թ����������Թ���ʾ���
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
		// TODO �Զ����ɵķ������
		super.paintComponent(g);
		frameBack=new ImageIcon("res\\����.png");
		g.drawImage(frameBack.getImage(),0, 0, null);
		
	}
}

