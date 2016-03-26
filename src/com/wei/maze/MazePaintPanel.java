package com.wei.maze;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;

import javax.swing.JPanel;

public class MazePaintPanel extends JPanel {

	private final int paintWidth = 500;
	private final int paintHeight = 500;

	public enum MazeSize {
		ten(10), twenty(20), fifty(50),none(0);

		private int size;

		private MazeSize(int s) {
			size = s;
		}
	};

	private int max_birds;
	private MazeGird[][] mazeArray;
	private NewMaze new_maze;

	public MazePaintPanel() {
		// TODO 自动生成的构造函数存根
		this.setPreferredSize(new Dimension(paintWidth, paintHeight));
		this.setBackground(Color.white);
		setMazeSize(MazeSize.none);
	}

	@Override
	protected void paintComponent(Graphics g) {
		// TODO 自动生成的方法存根
		super.paintComponent(g);
		paintMaze((Graphics2D) g);
	}

	public void setMazeSize(MazeSize m) {

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

			new_maze = new NewMaze(max_birds);
			new_maze.setMazeArray(mazeArray, max_birds);
		}
		else{
			max_birds=1;
			mazeArray=new MazeGird[1][];
			mazeArray[0]=new MazeGird[1];
			mazeArray[0][0]=new MazeGird();
			for(int i=0;i<4;i++)
				mazeArray[0][0].wall[i]=true;
		}
		repaint();
	}

	private void paintMaze(Graphics2D g) {
		int m = paintWidth / max_birds;
		for (int i = 1; i < max_birds - 1; i++)
			for (int j = 1; j < max_birds - 1; j++) {
				if (mazeArray[i][j].wall[0] == false)
					g.drawLine(j * m, i * m, j * m + m, i * m);
				if (mazeArray[i][j].wall[1] == false)
					g.drawLine(j * m + m, i * m, j * m + m, i * m + m);
				if (mazeArray[i][j].wall[2] == false)
					g.drawLine(j * m, i * m + m, j * m + m, i * m + m);
				if (mazeArray[i][j].wall[3] == false)
					g.drawLine(j * m, i * m, j * m, i * m + m);
			}

	}
}

class MazeGird {
	public boolean[] wall; // 上右下左
	public boolean available;

	public MazeGird() {
		wall = new boolean[4];
		for (boolean b : wall) {
			b = false;
		}
		available = false;
	}

	public void setAvailable(boolean b) {
		available = b;
	}

	public void setWall(int i, boolean b) {
		wall[i] = b;
	}

}

class NewMaze {
	private UnionFindTree set; // 并查集
	private int startX;
	private int startY;
	private int startWALL;
	private int endX;
	private int endY;
	private int endWALL;

	public NewMaze(int size) {
		// TODO 自动生成的构造函数存根
		set = new UnionFindTree((size - 2) * (size - 2));
	}

	public void setMazeArray(MazeGird[][] maze, int size) {
		
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
			startX=pointX;startY=pointY;startWALL=line;
		}else
		{
			endX=pointX;endY=pointY;endWALL=line;
		}
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
