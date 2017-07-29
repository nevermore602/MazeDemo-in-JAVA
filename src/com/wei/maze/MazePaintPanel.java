package com.wei.maze;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.LinkedList;

public class MazePaintPanel extends JPanel {

    public static final int paintWidth = 500;
    public static final int paintHeight = 500;
    public static final int MAX_Directions = 4;

    public static final int TEN = 10;
    public static final int TWENTY = 20;
    public static final int FIFTY = 50;
    public static final int NONE = 0;

    public static final int UP = 0;
    public static final int RIGHT = 1;
    public static final int DOWN = 2;
    public static final int LEFT = 3;

    public static class MazeDirection {

        public static int moveX(int m) {
            switch (m) {
                case LEFT:
                    return -1;
                case RIGHT:
                    return 1;
                default:
                    return 0;
            }
        }

        public static int moveY(int m) {
            switch (m) {
                case UP:
                    return -1;
                case DOWN:
                    return 1;
                default:
                    return 0;
            }
        }
    }

    private int max_birds; //�Թ��߳�����λ����Ԫ������
    private int cellSize;  //��Ԫ��߳�����λ�����أ�
    private MazeGird[][] mazeArray; //�Թ������ʾ��ά����
    private int personX;      //�����ߺ�����
    private int personY;      //������������
    private ImageIcon personImg;   //������ͼ��
    private String person_img_name;  //������ͼ���ļ���
    private LinkedList<Point2D> prohibitList;  //��ֹ��־λ������
    private ImageIcon prohibitImg;    //��ֹ��־ͼ��
    private String prohibit_img_name;  //��ֹ��־ͼ���ļ���
    private LinkedList<Point2D> pathPointList; //·��������

    private UnionFindTree set; // ���鼯
    private int startX;
    private int startY;
    private int startWALL;
    private int endX;
    private int endY;
    private int endWALL;

    public MazePaintPanel() {
        this.setPreferredSize(new Dimension(paintWidth, paintHeight));
        this.setBackground(Color.white);


        personX = personY = 0;
        person_img_name = new String("res\\������.png");
        personImg = new ImageIcon(person_img_name);

        prohibitList = new LinkedList<>();
        prohibit_img_name = new String("res\\��ֹͨ��.png");
        prohibitImg = new ImageIcon(prohibit_img_name);

        pathPointList = new LinkedList<>();

        setMazeSize(MazePaintPanel.NONE);
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getStartD() {
        return startWALL;
    }

    public int getEndX() {
        return endX;
    }

    public int getEndY() {
        return endY;
    }

    public int getEndD() {
        return endWALL;
    }


    //���������ߵĵ�ǰλ�ò����»���
    public void setPerson(int x, int y) {
        personX = x;
        personY = y;
        repaint();
    }

    //����·�����ߵ㲢����·��
    public void addPathPoint(Point2D p) {
        pathPointList.add(p);
        repaint();
    }

    //ɾ�����һ��·����
    public void deleteLastPath() {
        pathPointList.removeLast();
        repaint();
    }


    //���ӽ�ֹͨ�е㲢����
    public void addProhibitPoint(Point2D p) {
        prohibitList.add(p);
        repaint();
    }

    //�ж��Ƿ�����ͨ
    public boolean canRun(int x, int y, int d) {
        return mazeArray[y][x].wall[d]
                && mazeArray[y + MazeDirection.moveY(d)][x + MazeDirection.moveX(d)].getAvailable();
    }


    //����λ�����߹�
    public void setAvailable(int x, int y) {
        mazeArray[y][x].setAvailable(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintMaze((Graphics2D) g);
        paintPerson((Graphics2D) g);
        paintProhibit((Graphics2D) g);
        paintPath((Graphics2D) g);
    }

    //�����Թ��ߴ�
    public void setMazeSize(int m) {

        cellSize = m;
        clearList();   //��ս�ֹ���·���б�
        if (m != 0) {
            max_birds = paintWidth / m;

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
            personX = startX;
            personY = startY;
        } else {
            max_birds = 1;
            mazeArray = new MazeGird[1][];
            mazeArray[0] = new MazeGird[1];
            mazeArray[0][0] = new MazeGird();
            for (int i = 0; i < 4; i++)
                mazeArray[0][0].wall[i] = true;
            personX = personY = 0;
        }
        repaint();
    }

    //�����Թ�
    private void paintMaze(Graphics2D g) {

        for (int i = 1; i < max_birds - 1; i++)
            for (int j = 1; j < max_birds - 1; j++) {
                if (!mazeArray[i][j].wall[0])
                    g.drawLine(j * cellSize, i * cellSize, j * cellSize + cellSize, i * cellSize);
                if (!mazeArray[i][j].wall[1])
                    g.drawLine(j * cellSize + cellSize, i * cellSize, j * cellSize + cellSize, i * cellSize + cellSize);
                if (!mazeArray[i][j].wall[2])
                    g.drawLine(j * cellSize, i * cellSize + cellSize, j * cellSize + cellSize, i * cellSize + cellSize);
                if (!mazeArray[i][j].wall[3])
                    g.drawLine(j * cellSize, i * cellSize, j * cellSize, i * cellSize + cellSize);
            }

    }

    //����������
    private void paintPerson(Graphics2D g) {
        if (cellSize == 0) return;
        if (personX < 0 && personY < 0) return;
        Image img = personImg.getImage().getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH);
        personImg.setImage(img);
        g.drawImage(personImg.getImage(), personX * cellSize, personY * cellSize, null);
        personImg = new ImageIcon(person_img_name);
    }

    //���ƽ�ֹͨ�б�־
    private void paintProhibit(Graphics2D g) {
        if (cellSize == 0) return;
        Image img = prohibitImg.getImage().getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH);
        prohibitImg.setImage(img);
        for (Point2D p : prohibitList) {
            g.drawImage(prohibitImg.getImage(), (int) p.getX() * cellSize, (int) p.getY() * cellSize, null);
        }
        prohibitImg = new ImageIcon(prohibit_img_name);
    }

    //����·��
    private void paintPath(Graphics2D g) {
        if (cellSize == 0) return;
        int n = pathPointList.size();
        Point2D a, b;
        g.setPaint(Color.GREEN);
        g.setStroke(new BasicStroke(5.0f));
        for (int i = 0; i < n - 1; i++) {
            a = pathPointList.get(i);
            b = pathPointList.get(i + 1);
            g.drawLine((int) a.getX() * cellSize + cellSize / 2, (int) a.getY() * cellSize + cellSize / 2,
                    (int) b.getX() * cellSize + cellSize / 2, (int) b.getY() * cellSize + cellSize / 2);
        }
    }

    //��ս�ֹ���·�����б�
    public void clearList() {
        prohibitList.clear();
        pathPointList.clear();
    }


    //�����Թ����飬�������Թ�
    private void setMazeArray(MazeGird[][] maze, int size) {

        set = new UnionFindTree((size - 2) * (size - 2));
        // ���������ʼ��ͽ�����
        setSE(maze, size, 0);
        setSE(maze, size, 1);


        // �����Թ�����
        int rx, ry, rfy, rfx, ru; // ���ѡ��һ���㣬�����ѡ��һ����ͨ��ⷽ��
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

    //���������ʼ�����ֹ��
    private void setSE(MazeGird[][] maze, int size, int SE) //SEΪ0��������ʼ�㣬SEΪ��0�������е�
    {
        int pointX, pointY, line, ran;
        pointX = pointY = 0;
        line = (int) (Math.random() * 4.0);
        ran = (int) (Math.random() * (size - 2) + 1);
        switch (line) {
            case 0:
                pointX = ran;
                pointY = 1;
                break;
            case 1:
                pointX = size - 2;
                pointY = ran;
                break;
            case 2:
                pointX = ran;
                pointY = size - 2;
                break;
            case 3:
                pointX = 1;
                pointY = ran;
                break;
        }
        ;
        maze[pointY][pointX].wall[line] = true;
        if (SE == 0) {
            startX = pointX;
            startY = pointY;
            startWALL = line;
        } else {
            endX = pointX;
            endY = pointY;
            endWALL = line;
        }
    }
}

class MazeGird {
    public boolean[] wall; // ��������
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
