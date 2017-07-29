package com.wei.maze;

import javax.swing.*;
import java.awt.*;


public class MazeShowPanel extends JPanel {
    private final int mpsWidth = 700;
    private final int mpsHeight = 600;
    private MazePaintPanel paintPanel;
    private RunInMaze run;

    public MazeShowPanel() {
        super();
        this.setPreferredSize(new Dimension(mpsWidth, mpsHeight));
        this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2), "MAZE SHOW"));
        this.setBackground(Color.PINK);

        FlowLayout showflow = new FlowLayout();
        this.setLayout(showflow);
        showflow.setAlignment(FlowLayout.CENTER);
        showflow.setVgap(30);

        paintPanel = new MazePaintPanel();
        this.add(paintPanel);

        run = new RunInMaze(paintPanel);
    }

    public void setMazeSize(int maze_size) {
        if (!run.runThreadIsNull())
            run.interruptRun();
        paintPanel.setMazeSize(maze_size);
        run.runAgain();
    }


    public void computerRun() {
        run.runByComputer();
    }

}
