package com.wei.maze;

import com.wei.maze.MazePaintPanel.MazeDirection;

import java.awt.geom.Point2D;
import java.util.Stack;

public class RunInMaze {

    private int locX;
    private int locY;
    private int endX;
    private int endY;
    private int startDirection;

    private MazePaintPanel relatedMaze;
    private Stack<Point2D> pathStack;
    private Stack<Integer> directionStack;
    private Thread t;

    public RunInMaze(MazePaintPanel maze) {
        relatedMaze = maze;
        locX = relatedMaze.getStartX();
        locY = relatedMaze.getStartY();
        endX = relatedMaze.getEndX();
        endY = relatedMaze.getEndY();
        pathStack = new Stack<>();
        directionStack = new Stack<>();
        setStartDirection(relatedMaze.getStartX(), relatedMaze.getStartY());
    }

    private void setStartDirection(int nowX, int nowY) {
        int x = nowX - relatedMaze.getEndX();
        int y = nowY - relatedMaze.getEndY();
        if (x < 0 && y <= 0) startDirection = MazePaintPanel.RIGHT;
        if (x >= 0 && y < 0) startDirection = MazePaintPanel.DOWN;
        if (x > 0 && y >= 0) startDirection = MazePaintPanel.LEFT;
        if (x <= 0 && y > 0) startDirection = MazePaintPanel.UP;
        //endDirection=(startDirection+3)%4;
    }


    public void runAgain() {
        locX = relatedMaze.getStartX();
        locY = relatedMaze.getStartY();
        endX = relatedMaze.getEndX();
        endY = relatedMaze.getEndY();
        relatedMaze.setPerson(locX, locY);
        pathStack.clear();
        directionStack.clear();
        relatedMaze.clearList();
        setStartDirection(relatedMaze.getStartX(), relatedMaze.getStartY());
    }

    public void runByComputer() {
        ComputerRunTask task = new ComputerRunTask();
        t = new Thread(task);
        t.start();
    }

    public void interruptRun() {
        t.interrupt();
    }

    public boolean runThreadIsNull() {
        return t == null;
    }

    class ComputerRunTask implements Runnable {

        @Override
        public void run() {
            try {
                int line = 0;
                int dire;
                Point2D temp;
                while (locX != endX || locY != endY) {

                    if (line < 4) {
                        setStartDirection(locX, locY);
                        dire = (startDirection + line) % 4;
                        if (relatedMaze.canRun(locX, locY, dire)) {

                            relatedMaze.addPathPoint(new Point2D.Float((float) locX, (float) locY));
                            relatedMaze.setAvailable(locX, locY);
                            pathStack.push(new Point2D.Float((float) locX, (float) locY));
                            directionStack.push(line);
                            locX = locX + MazeDirection.moveX(dire);
                            locY = locY + MazeDirection.moveY(dire);
                            relatedMaze.setPerson(locX, locY);
                            line = 0;
                        } else {
                            line++;
                        }
                    } else {
                        relatedMaze.addProhibitPoint(new Point2D.Float((float) locX, (float) locY));
                        temp = pathStack.pop();
                        line = directionStack.pop();
                        locX = (int) temp.getX();
                        locY = (int) temp.getY();
                        relatedMaze.deleteLastPath();
                        relatedMaze.setPerson(locX, locY);
                        line++;
                    }

                    Thread.sleep(250);

                }
                relatedMaze.addPathPoint(new Point2D.Float((float) locX, (float) locY));
                relatedMaze.setPerson(locX + MazeDirection.moveX(relatedMaze.getEndD()),
                        locY + MazeDirection.moveY(relatedMaze.getEndD()));
            } catch (InterruptedException e) {
                System.out.println("迷宫行走线程被中断!这并非一个错误，而是一个提醒");
                e.printStackTrace();
                return;
            }
        }

    }

}
