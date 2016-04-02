package com.wei.maze;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class MazePaintPanel extends JPanel {

	public final int paintWidth = 500;
	public final int paintHeight = 500;
	public static final int MAX_Directions = 4;

	public enum MazeSize {
		ten(10), twenty(20), fifty(50),none(0);

		private int size;

		private MazeSize(int s) {
			size = s;
		}
	};
	
	public enum MazeDirection{
		up(0),right(1),down(2),left(3);
		
		private int num;
		
		private MazeDirection(int n) {
			num=n;
		}
		public int getNum()
		{
			return num;
		}
		public static MazeDirection changeToDirection(int i)
		{
			switch(i)
			{
			  case 0:
				  return up;
				
			  case 1:
				  return right;
				
			  case 2:
				  return down;
				 
			  case 3:
			  default:
				  return left;	 
			}
		}
		
		public static int moveX(MazeDirection m)
		{
			switch(m)
			{
			case left:
				return -1;
			case right:
				return 1;
			default:
				return 0;
			}
		}
		
		public static int moveY(MazeDirection m)
		{
			switch(m)
			{
			case up:
				return -1;
			case down:
				return 1;
			default:
				return 0;
			}
		}
	}

	private int max_birds; //迷宫边长（单位：单元格数）
	private int cellSize;  //单元格边长（单位：像素）
	private MazeGird[][] mazeArray; //迷宫抽象表示二维数组
	private int personX;      //行走者横坐标
	private int personY;      //行走者纵坐标
	private ImageIcon personImg;   //行走者图标
	private String person_img_name;  //行走者图标文件名
	private LinkedList<Point2D> prohibitList;  //禁止标志位置链表
	private ImageIcon prohibitImg;    //禁止标志图标
	private String prohibit_img_name;  //禁止标志图标文件名
	private LinkedList<Point2D> pathPointList; //路径点链表
	
	private UnionFindTree set; // 并查集
	private int startX;
	private int startY;
	private MazeDirection startWALL;
	private int endX;
	private int endY;
	private MazeDirection endWALL;

	public MazePaintPanel() {
		// TODO 自动生成的构造函数存根
		this.setPreferredSize(new Dimension(paintWidth, paintHeight));
		this.setBackground(Color.white);
		
		
		personX=personY=0;
		person_img_name=new String("res\\蓝兔子.png");
		personImg=new ImageIcon(person_img_name);
		
		prohibitList=new LinkedList<Point2D>();
		prohibit_img_name=new String("res\\禁止通行.png");
		prohibitImg=new ImageIcon(prohibit_img_name);
		
		pathPointList=new LinkedList<Point2D>();
		
		setMazeSize(MazeSize.none);
	}
	
	public int getStartX()
	{
		return startX;
	}
	
	public int getStartY()
	{
		return startY;
	}
	
	public MazeDirection getStartD()
	{
		return startWALL;
	}
	
	public int getEndX()
	{
		return endX;
	}
	
	public int getEndY()
	{
		return endY;
	}
	
	public MazeDirection getEndD()
	{
		return endWALL;
	}
	
	 
	//设置行走者的当前位置并重新绘制
	public void setPerson(int x,int y)
	{
		personX=x;
		personY=y;
		repaint();
	}
	
	//增加路径行走点并绘制路径
	public void addPathPoint(Point2D p)
	{
		pathPointList.add(p);
		repaint();
	}
	
	//删除最后一个路径点
	public void deleteLastPath()
	{
		pathPointList.removeLast();
		repaint();
	}
	
	
	//增加禁止通行点并绘制
	public void addProhibitPoint(Point2D p)
	{
		prohibitList.add(p);
		repaint();
	}
	
	//判断是否能走通
	public boolean canRun(int x,int y,MazeDirection d)
	{
		return mazeArray[y][x].wall[d.num] 
				&& mazeArray[y+MazeDirection.moveY(d)][x+MazeDirection.moveX(d)].getAvailable();
	}
	
	
	//设置位置已走过
	public void setAvailable(int x,int y)
	{
		mazeArray[y][x].setAvailable(false);
	}

	@Override
	protected void paintComponent(Graphics g) {
		// TODO 自动生成的方法存根
		super.paintComponent(g);
		paintMaze((Graphics2D) g);
		paintPerson((Graphics2D) g);
		paintProhibit((Graphics2D)g);
		paintPath((Graphics2D)g);
	}
	
	//设置迷宫尺寸
	public void setMazeSize(MazeSize m) {

		cellSize=m.size;
		clearList();   //清空禁止点和路径列表
		if(m.size!=0)
		{
			max_birds = paintWidth / m.size;

			mazeArray = new MazeGird[max_birds][];
			for (int i = 0; i < max_birds; i++) {
				mazeArray[i] = new MazeGird[max_birds];
				for (int j = 0; j < max_birds; j++) {
					mazeArray[i][j] = new MazeGird();
				}
			}
			mazeArray[0][0].setAvailable(false);

			for (int i = 0; i < max_birds; i++) {
				mazeArray[max_birds - 1][i].setAvailable(false);
				mazeArray[0][i].setAvailable(false);
				mazeArray[i][0].setAvailable(false);
				mazeArray[i][max_birds - 1].setAvailable(false);
			}

			setMazeArray(mazeArray, max_birds);
			personX=startX;
			personY=startY;
		}
		else{
			max_birds=1;
			mazeArray=new MazeGird[1][];
			mazeArray[0]=new MazeGird[1];
			mazeArray[0][0]=new MazeGird();
			for(int i=0;i<4;i++)
				mazeArray[0][0].wall[i]=true;
			personX=personY=0;
		}
		repaint();
	}

	//绘制迷宫
	private void paintMaze(Graphics2D g) {

		for (int i = 1; i < max_birds - 1; i++)
			for (int j = 1; j < max_birds - 1; j++) {
				if (mazeArray[i][j].wall[0] == false)
					g.drawLine(j * cellSize, i * cellSize, j * cellSize +cellSize, i * cellSize);
				if (mazeArray[i][j].wall[1] == false)
					g.drawLine(j * cellSize + cellSize, i *cellSize, j * cellSize + cellSize, i * cellSize + cellSize);
				if (mazeArray[i][j].wall[2] == false)
					g.drawLine(j * cellSize, i * cellSize + cellSize, j * cellSize + cellSize, i * cellSize + cellSize);
				if (mazeArray[i][j].wall[3] == false)
					g.drawLine(j * cellSize, i * cellSize, j * cellSize, i * cellSize + cellSize);
			}

	}
	
	//绘制行走者
	private void paintPerson(Graphics2D g)
	{
		if(cellSize==0) return;
		if(personX<0 && personY<0) return;
		Image img=personImg.getImage().getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH);
		personImg.setImage(img);
		g.drawImage(personImg.getImage(),personX*cellSize,personY*cellSize,null);
		personImg=new ImageIcon(person_img_name);
	}
	
	//绘制禁止通行标志
	private void paintProhibit(Graphics2D g)
	{
		if(cellSize==0) return;
		Image img=prohibitImg.getImage().getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH);
		prohibitImg.setImage(img);
		for (Point2D p : prohibitList) {
			g.drawImage(prohibitImg.getImage(),(int)p.getX()*cellSize,(int)p.getY()*cellSize,null);
		}
		prohibitImg=new ImageIcon(prohibit_img_name);	
	}
	
	//绘制路径
	private void paintPath(Graphics2D g)
	{
		if(cellSize==0) return;
		int n = pathPointList.size();
		Point2D a,b;
		g.setPaint(Color.GREEN);
		g.setStroke(new BasicStroke(5.0f));
		for(int i=0; i<n-1; i++)
		{
			a = pathPointList.get(i);
			b = pathPointList.get(i+1);
			g.drawLine((int)a.getX()*cellSize+cellSize/2, (int)a.getY()*cellSize+cellSize/2,
					(int)b.getX()*cellSize+cellSize/2, (int)b.getY()*cellSize+cellSize/2);
		}
	}
	
	//清空禁止点和路径点列表
	public void clearList()
	{
		prohibitList.clear();
		pathPointList.clear();
	}
	
	
	//设置迷宫数组，生成新迷宫
	private void setMazeArray(MazeGird[][] maze, int size) {
		
		set = new UnionFindTree((size - 2) * (size - 2));
		// 随机生成起始点和结束点
		setSE(maze, size, 0);
		setSE(maze, size, 1);
		
		
		// 生成迷宫布局
		int rx, ry, rfy, rfx, ru; // 随机选择一个点，再随机选择一个联通检测方向
		while (set.getCount() > 1) {
			do {
				rx = (int) (Math.random() * (size - 2) + 1);
				ry = (int) (Math.random() * (size - 2) + 1);
				ru = (int) (Math.random() * 4);

				if (ru == 0 && ry > 1) {
					rfx = 0;
					rfy = -1;
				} else if (ru == 1 && rx < size - 2) {
					rfx = 1;
					rfy = 0;
				} else if (ru == 2 && ry < size - 2) {
					rfx = 0;
					rfy = 1;
				} else if (ru == 3 && rx > 1) {
					rfx = -1;
					rfy = 0;
				} else
					rfx = rfy = 0;
			} while (set.find((ry - 1) * (size - 2) + rx - 1) == set.find((ry - 1 + rfy) * (size - 2) + rx + rfx - 1));
			maze[ry][rx].wall[ru] = true;
			maze[ry + rfy][rx + rfx].wall[(ru + 2) % 4] = true;

			set.union((ry - 1) * (size - 2) + rx - 1, (ry - 1 + rfy) * (size - 2) + rx + rfx - 1);
		}
	}
	
	//随机生成起始点或终止点
	private void setSE(MazeGird [][] maze,int size,int SE) //SE为0则设置起始点，SE为非0则设置中点
	{
		int pointX,pointY,line,ran;
		pointX=pointY=0;
		line = (int)(Math.random()*4.0);
		ran = (int)(Math.random()*(size-2)+1);
		switch (line) {
			case 0:
				pointX=ran;
				pointY=1;
				break;
			case 1:
				pointX=size-2;
				pointY=ran;
				break;
			case 2:
				pointX=ran;
				pointY=size-2;
				break;
			case 3:
				pointX=1;
				pointY=ran;
				break;
		};
		maze[pointY][pointX].wall[line]=true;
		if(SE==0)  
		{
			startX=pointX;startY=pointY;startWALL=MazeDirection.changeToDirection(line);
		}else
		{
			endX=pointX;endY=pointY;endWALL=MazeDirection.changeToDirection(line);
		}
	}
}

class MazeGird {
	public boolean[] wall; // 上右下左
	private boolean available;

	public MazeGird() {
		wall = new boolean[4];
		for (boolean b : wall) {
			b = false;
		}
		available = true;
	}

	public void setAvailable(boolean b) {
		available = b;
	}

	public boolean getAvailable() {
		return available;
	}

}


class UnionFindTree {
	private int[] node;
	private int[] size;
	private int count;

	public UnionFindTree(int nodes) {
		// TODO 自动生成的构造函数存根
		node = new int[nodes];
		size = new int[nodes];
		for (int i = 0; i < nodes; i++) {
			node[i] = i;
			size[i] = 1;
		}
		count = nodes;
	}

	public int getCount() {
		return count;
	}

	public int find(int num) {
		int temp;
		while (node[num] != num) {
			temp = num;
			num = node[num];
			node[temp] = node[node[temp]];
		}
		return num;
	}

	public void union(int a, int b) {
		int rootA = find(a);
		int rootB = find(b);
		if (rootA == rootB)
			return;
		if (size[rootA] < size[rootB]) {
			size[rootB] += size[rootA];
			node[rootA] = rootB;
		} else {
			size[rootA] += size[rootB];
			node[rootB] = rootA;
		}
		count--;
	}
}
