package org.example;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.JPanel;

public class MainPanel extends JPanel implements Runnable {

    // SCREEN SETTINGS
    final int maximumCols = 30;
    final int maximumRows = 30;

    final int nodeSize = 20;
    final int screenWidth = nodeSize * maximumCols;
    final int screenHeight = nodeSize * maximumRows;


    int FPS = 250;

    Thread mainThread;

    // NODE
    Node[][] nodesGrid = new Node[maximumCols][maximumRows];
    Node startNode, goalNode, currentNode;

    ArrayList<Node> openSet = new ArrayList<>();
    ArrayList<Node> closedSet = new ArrayList<>();

    ArrayList<Node> thePathSet = new ArrayList<>();

    boolean goalNodeReached = false;

    boolean noSolution = false;

    public MainPanel() {

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setLayout(new GridLayout(maximumRows, maximumCols));
        this.setDoubleBuffered(true);
        // PLACE NODES
        int col = 0;
        int row = 0;
        while (col < maximumCols && row < maximumRows) {
            nodesGrid[col][row] = new Node(col, row);
            this.add(nodesGrid[col][row]);
            col++;
            if (col == maximumCols) {
                col = 0;
                row++;
            }
        }
        System.out.println("A*\n");


        setStartNode(0, 0);
        setGoalNode(maximumCols - 1, maximumRows - 1);

        setCostOnNodes();

        int rndMinimumLimit = 0;
        int rndMaximumLimit = 1;

        double randomNumber;

        for (int i = 0; i < maximumCols; i++) {
            for (int j = 0; j < maximumRows; j++) {
                randomNumber = Math.random() * (rndMaximumLimit - rndMinimumLimit + 1) + rndMinimumLimit;
                if (randomNumber < 0.5) {
                    if (nodesGrid[i][j] != startNode && nodesGrid[i][j] != goalNode) {
                        setWallNode(i, j);
                    }
                }
            }
        }
    }

    private void setStartNode(int col, int row) {
        nodesGrid[col][row].setAsStart();
        startNode = nodesGrid[col][row];
        currentNode = startNode;

        openSet.add(startNode);

    }

    private void setGoalNode(int col, int row) {
        nodesGrid[col][row].setAsGoal();
        goalNode = nodesGrid[col][row];

    }

    private void setWallNode(int col, int row) {
        nodesGrid[col][row].setAsWall();

    }

    private void setCostOnNodes() {

        int col = 0;
        int row = 0;

        while (col < maximumCols && row < maximumRows) {
            getNodeCost(nodesGrid[col][row]);
            col++;
            if (col == maximumCols) {
                col = 0;
                row++;
            }
        }
    }

    private void getNodeCost(Node node) {

        int xDistance = Math.abs(node.col - startNode.col);
        int yDistance = Math.abs(node.row - startNode.row);
        node.gCost = xDistance + yDistance;

        xDistance = Math.abs(node.col - goalNode.col);
        yDistance = Math.abs(node.row - goalNode.row);
        node.hCost = xDistance + yDistance;

        node.fCost = node.gCost + node.hCost;
    }


    int maxSteps = 10000;

    public void searchGoalNode() {
        if (!noSolution) {
            if (goalNodeReached == false && maxSteps > 0) {
                int col = currentNode.col;
                int row = currentNode.row;
                currentNode.setAsChecked();
                if (!(currentNode.isWall)) {
                    if (row - 1 >= 0) {
                        openNewNode(nodesGrid[col][row - 1]);
                    }
                    if (col - 1 >= 0) {
                        openNewNode(nodesGrid[col - 1][row]);
                    }
                    if (row + 1 < maximumRows) {
                        openNewNode(nodesGrid[col][row + 1]);
                    }
                    if (col + 1 < maximumCols) {
                        openNewNode(nodesGrid[col + 1][row]);
                    }
                }

                int bestNodeIndex = 0;
                int bestNodefCost = 999;

                for (int i = 0; i < openSet.size(); i++) {
                    if ((openSet.get(i).fCost < bestNodefCost) && !(currentNode.isWall)) {
                        bestNodeIndex = i;
                        bestNodefCost = openSet.get(i).fCost;

                    }
                    // If F cost is equal, check the G cost
                    else if ((openSet.get(i).fCost == bestNodefCost) && !(currentNode.isWall)) {
                        if ((openSet.get(i).gCost < openSet.get(bestNodeIndex).gCost) && !(currentNode.isWall)) {
                            bestNodeIndex = i;
                        }
                    }
                }

                if (openSet.size() > 0) {
                    currentNode = openSet.get(bestNodeIndex);
                }

                if (currentNode == goalNode) {
                    System.out.println("'goalNode' FOUND!");
                    goalNodeReached = true;
                    trackThePath();

                    isSearchCompleted = true;

                }
                openSet.remove(currentNode);

                if (!(closedSet.contains(currentNode))) {
                    closedSet.add(currentNode);
                }
                maxSteps--;
            } else {
                System.out.println("no solution!");
                noSolution = true;
            }
        }


        if (goalNodeReached || noSolution) {
            for (int i = 0; i < thePathSet.size(); i++) {
            }
            isSearchCompleted = true;
        }
    }


    private void openNewNode(Node node) {
        if (node.isOpen == false && node.isChecked == false && node.isWall == false) {
            node.setAsOpen();
            node.parent = currentNode;
            openSet.add(node);
        }
    }

    private void trackThePath() {

        Node current = goalNode;

        while (current != startNode) {
            current = current.parent;
            if (current != startNode) {
                current.setAsPath();
                thePathSet.add(current);
            }
        }
    }

    public void startMainThread() {

        mainThread = new Thread(this);

        // starting the  Thread
        mainThread.start();

    }


    boolean isSearchCompleted;

    // to run a Thread
    @Override
    public void run() {

        double drawInterval = 1000000000 / FPS;

        double delta = 0;

        long lastTime = System.nanoTime();

        long currentTime;

        long timer = 0;
        int drawCount = 0;


        while (mainThread != null) {

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;

            timer += (currentTime - lastTime);

            lastTime = currentTime;

            if (delta >= 1) {
                if (!isSearchCompleted) {
                    searchGoalNode();
                }
                delta--;
                drawCount++;
            }


            if (timer >= 1000000000) {
                System.out.println("FPS:" + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update() {
    }

    public void paintComponent() {
    }

}
