package view;

import controller.GameController;
import model.Chessboard;

import model.Step;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


/**
 * 这个类表示游戏过程中的整个游戏界面，是一切的载体
 */
public class ChessGameFrame extends JFrame {
    //    public final Dimension FRAME_SIZE ;
    private final int WIDTH;
    private final int HEIGTH;

    private final int ONE_CHESS_SIZE;

    private ChessboardComponent chessboardComponent;
    public ChessGameFrame(int width, int height) {
        setTitle("2023 CS109 Project Demo"); //设置标题
        this.WIDTH = width;
        this.HEIGTH = height;
        this.ONE_CHESS_SIZE = (HEIGTH * 4 / 5) / 9;

        setSize(WIDTH, HEIGTH);
        setLocationRelativeTo(null); // Center the window.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setLayout(null);


        addChessboard();//在这
        addLabel();
        addRemakeButton();
        addUndoButton();
        addSaveButton();
        addLoadButton();
    }

    public ChessboardComponent getChessboardComponent() {
        return chessboardComponent;
    }

    public void setChessboardComponent(ChessboardComponent chessboardComponent) {
        this.chessboardComponent = chessboardComponent;
    }

    /**
     * 在游戏面板中添加棋盘
     */
    private void addChessboard() //该操作初始化了chessboardcomponent
    {
        chessboardComponent = new ChessboardComponent(ONE_CHESS_SIZE);//构造chessboardcomponent并设置了size
        chessboardComponent.setLocation(HEIGTH / 5, HEIGTH / 10);//没有找到这个函数？
        add(chessboardComponent);
    }

    /**
     * 在游戏面板中添加标签
     */
    private void addLabel() {
        JLabel statusLabel = new JLabel("Sample label");
        statusLabel.setLocation(HEIGTH, HEIGTH / 10);
        statusLabel.setSize(200, 60);
        statusLabel.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(statusLabel);
    }

    /**
     * 在游戏面板中增加一个按钮，如果按下的话就会显示Hello, world!
     */

    private void addRemakeButton() {
        JButton button = new JButton("RE!");
        button.setLocation(HEIGTH, HEIGTH / 10 + 120);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        button.addActionListener((e) -> {
            chessboardComponent.clear(chessboardComponent.getGameController().getModel());
            GameController g1=new GameController(chessboardComponent,new Chessboard());
            JOptionPane.showMessageDialog(this, "棋盘已重置！", "提示", JOptionPane.INFORMATION_MESSAGE);
        });


        add(button);
    }
    private void addUndoButton() {
        JButton button = new JButton("UNDO");
        button.setLocation(HEIGTH, HEIGTH / 10 + 180);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        button.addActionListener((e) -> {
            chessboardComponent.getGameController().undo();
//            JOptionPane.showMessageDialog(this, "已撤销", "提示", JOptionPane.INFORMATION_MESSAGE);
        });

        add(button);
    }
    private void addSaveButton() {
        JButton button = new JButton("Save");
        button.setLocation(HEIGTH, HEIGTH / 10 + 240);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        button.addActionListener((e) -> {
            chessboardComponent.getGameController().save();
            JOptionPane.showMessageDialog(this, "保存", "提示", JOptionPane.INFORMATION_MESSAGE);
        });

        add(button);
    }


    private void addLoadButton() {
        JButton button = new JButton("Load");
        button.setLocation(HEIGTH, HEIGTH / 10 + 300);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);

        button.addActionListener(e -> {
            System.out.println("Click load");
            String path = JOptionPane.showInputDialog(this,"Input Path here");
            List<Step> loadsteps=chessboardComponent.getGameController().loadGameFromFile(path);
            if(loadsteps.size()>0) {
                chessboardComponent.clear(chessboardComponent.getGameController().getModel());
                GameController g1 = new GameController(chessboardComponent, loadsteps.get(loadsteps.size() - 1).getModel());
                g1.setTurns(loadsteps.size());
                g1.setSteps(loadsteps);
            }


        });
    }


}
