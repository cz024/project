package view;

import controller.GameController;
import model.Chessboard;

import model.PlayerColor;
import model.Step;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;


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

//        ImageIcon bg=new ImageIcon("resources/IMG_2043.JPG");
//        setBounds(0,0,bg.getIconWidth(),bg.getIconHeight());
//        JLabel jLabel=new JLabel(bg);
//        jLabel.setBounds(0,0,bg.getIconWidth(),bg.getIconHeight());
//        JPanel jPanel=(JPanel) getContentPane();
//        jPanel.setOpaque(false);
//        getLayeredPane().add(jLabel,Integer.MIN_VALUE);
////        setVisible(true);

        setSize(WIDTH, HEIGTH);
        setLocationRelativeTo(null); // Center the window.
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setLayout(null);
        //        JLabel background=new JLabel(new ImageIcon("E:\\My life in SUSTech\\学习\\作业\\Java\\project\\CS109-2023-Sping-Chess1的副本(1)\\CS109-2023-Sping-Chess1的副本\\resources\\IMG_2043.JPG"));
//        background.setBounds(0, 0, WIDTH, HEIGTH);
//        add(background);

        clickTime=0;
        addChessboard();//在这
        //addLabel();
        addRemakeButton();
        addUndoButton();
        addSaveButton();
        addLoadButton();
        addMuteButton();
        ChangeBG();

//        JLabel background=new JLabel(new ImageIcon("E:\\My life in SUSTech\\学习\\作业\\Java\\project\\CS109-2023-Sping-Chess1的副本(1)\\CS109-2023-Sping-Chess1的副本\\resources\\IMG_2043.JPG"));
//        background.setBounds(0, 0, WIDTH, HEIGTH);
//        add(background);

        playbackButton();


//        JLabel background=new JLabel(new ImageIcon("E:\\My life in SUSTech\\学习\\作业\\Java\\project\\CS109-2023-Sping-Chess1的副本(1)\\CS109-2023-Sping-Chess1的副本\\resources\\IMG_2043.JPG"));
//        background.setBounds(0, 0, WIDTH, HEIGTH);
//        add(background);


//        JButton red=new JButton("RED");
//        JButton blue=new JButton("BLUE");
//        JFrame frame=new JFrame();
//        red.setLocation(150,50);
//        red.setFont(new Font("Rockwell", Font.BOLD, 20));
//        red.setSize(200,60);
//        blue.setLocation(150,120);
//        blue.setFont(new Font("Rockwell", Font.BOLD, 20));
//        blue.setSize(200,60);
//        frame.add(red);
//        frame.add(blue);
//        frame.setSize(500,500);
//        frame.setLocationRelativeTo(null);
//        //frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        frame.setLayout(null);
//        frame.setVisible(true);
//        JLabel label=new JLabel("Who will go first?");
//        label.setSize(200,60);
//        label.setFont(new Font("Rockwell", Font.BOLD, 20));
//        label.setLocation(150,80);
//        frame.add(label);
//        add(frame);


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
        JLabel statusLabel = new JLabel("");
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
        button.setLocation(HEIGTH, HEIGTH / 10 );
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        button.addActionListener((e) -> {

            chessboardComponent.clear(chessboardComponent.getGameController().getModel());
            chessboardComponent.getGameController().getTimer1().cancel();
            chessboardComponent.getGameController().getTimer1().purge();
            chessboardComponent.getGameController().timer.setVisible(false);
            chessboardComponent.getGameController().showCurrentUser1().setVisible(false);
            chessboardComponent.getGameController().showCurrentUser2().setVisible(false);
            chessboardComponent.getGameController().showCurrentUser3().setVisible(false);
            GameController g1=new GameController(chessboardComponent,new Chessboard(),chessboardComponent.getGameController().getUserRed(),chessboardComponent.getGameController().getUserBlue());
            g1.timer.setFont(new Font("楷体", Font.BOLD, 20));
            g1.timer.setSize(200, 60);
            g1.timer.setLocation(890, 81 + 420+120);

            this.add(g1.timer);
            this.add(g1.showCurrentUser1());
            this.add(g1.showCurrentUser2());
            this.add(g1.showCurrentUser3());
            g1.CountDown();
            this.add(g1.timer);
////                chessboardComponent.getGameController().timer.setVisible(false);


            JOptionPane.showMessageDialog(this, "棋盘已重置！", "提示", JOptionPane.INFORMATION_MESSAGE);

            clickTime=0;
            this.button.setVisible(false);

            playbackButton();
            add(this.button);
            this.button.setText("回放当前棋局");
            this.repaint();
        });



        add(button);
    }

    private void addUndoButton() {
        JButton button = new JButton("UNDO");
        button.setLocation(HEIGTH, HEIGTH / 10 +60);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        button.addActionListener((e) -> {
            chessboardComponent.getGameController().undo();
//            JOptionPane.showMessageDialog(this, "已撤销", "提示", JOptionPane.INFORMATION_MESSAGE);
        });
//        StringBuilder red;
//        StringBuilder blue;
//        red=new StringBuilder("RED:");
//        blue=new StringBuilder("BLUE:");
//        red.append(String.format("%s Score:%d\n",chessboardComponent.getGameController().getUserRed().getName(),chessboardComponent.getGameController().getUserRed().getScore()));
//        blue.append(String.format("%s Score:%d\n",chessboardComponent.getGameController().getUserBlue().getName(),chessboardComponent.getGameController().getUserBlue().getScore()));
//        String redOutput=red.toString();
//        String blueOutput=blue.toString();
//        JLabel redLabel=new JLabel(redOutput);
//        JLabel blueLabel=new JLabel(blueOutput);
//        add(redLabel);
//        add(blueLabel);

        add(button);
    }
    private void addSaveButton() {
        JButton button = new JButton("Save");
        button.setLocation(HEIGTH, HEIGTH / 10 + 120);
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
        button.setLocation(HEIGTH, HEIGTH / 10 + 180);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
        add(this.button);
        button.addActionListener(e -> {
            System.out.println("Click load");
            String path = JOptionPane.showInputDialog(this,"Input Path here");
            List<Step> loadsteps=chessboardComponent.getGameController().loadGameFromFile(path);
            if(!Objects.equals(path.substring(path.length() - 4), ".txt")){
                JOptionPane.showMessageDialog(this,"文件类型错误！");
            }else if(loadsteps.get(0).getModel().getGrid().length!=9||loadsteps.get(0).getModel().getGrid()[0].length!=7){
                JOptionPane.showMessageDialog(this,"棋盘大小错误！");
            }else {

                boolean CorrectChessPiece = true;
                int[] allChessPiece = new int[16];
                int i = 1;
                int j;
                while (i <= 9) {
                    j = 1;
                    while (j <= 7) {
                        if (loadsteps.get(0).getModel().getGrid()[i - 1][j - 1].getPiece() != null) {
                            if (loadsteps.get(0).getModel().getGrid()[i - 1][j - 1].getPiece().getOwner().equals(PlayerColor.RED)) {
                                switch (loadsteps.get(0).getModel().getGrid()[i - 1][j - 1].getPiece().getName()) {
                                    case "Elephant":
                                        allChessPiece[7]++;
                                        break;
                                    case "Lion":
                                        allChessPiece[6]++;
                                        break;
                                    case "Tiger":
                                        allChessPiece[5]++;
                                        break;
                                    case "Leopard":
                                        allChessPiece[4]++;
                                        break;
                                    case "Wolf":
                                        allChessPiece[3]++;
                                        break;
                                    case "Dog":
                                        allChessPiece[2]++;
                                        break;
                                    case "Cat":
                                        allChessPiece[1]++;
                                        break;
                                    case "Rat":
                                        allChessPiece[0]++;
                                        break;
                                }
                            } else if (loadsteps.get(0).getModel().getGrid()[i - 1][j - 1].getPiece().getOwner().equals(PlayerColor.BLUE)) {
                                switch (loadsteps.get(0).getModel().getGrid()[i - 1][j - 1].getPiece().getName()) {
                                    case "Elephant":
                                        allChessPiece[15]++;
                                        break;
                                    case "Lion":
                                        allChessPiece[14]++;
                                        break;
                                    case "Tiger":
                                        allChessPiece[13]++;
                                        break;
                                    case "Leopard":
                                        allChessPiece[12]++;
                                        break;
                                    case "Wolf":
                                        allChessPiece[11]++;
                                        break;
                                    case "Dog":
                                        allChessPiece[10]++;
                                        break;
                                    case "Cat":
                                        allChessPiece[9]++;
                                        break;
                                    case "Rat":
                                        allChessPiece[8]++;
                                        break;
                                }
                            }
                        }
                        j++;
                    }
                    i++;
                }
                i = 1;
                while (i <= 16) {
                    if (allChessPiece[i - 1] != 1) {
                        CorrectChessPiece = false;
                    }
                    i++;
                }

                if (!CorrectChessPiece) {
                    JOptionPane.showMessageDialog(this, "棋子错误！");
                }else if(loadsteps.get(loadsteps.size() - 1).getColor()==null) {
                    JOptionPane.showMessageDialog(this, "缺少行棋方！");
                }else {
                    i=1;

                    if(loadsteps.get(0).getModel().calculateDistance(loadsteps.get(0).getSrc(),loadsteps.get(0).getDest())==1) {
                        while (i <= loadsteps.size() - 1) {
                            if (loadsteps.get(i - 1).getModel().getChessPieceAt(loadsteps.get(i).getDest()) == null) {
                                if (!loadsteps.get(i - 1).getModel().isValidMove(loadsteps.get(i).getSrc(), loadsteps.get(i).getDest())) {
                                    break;
                                }
                            } else {
                                if (!loadsteps.get(i - 1).getModel().realcapture(loadsteps.get(i).getSrc(), loadsteps.get(i).getDest())) {
                                    break;
                                }
                            }
                            i++;
                        }
                    }
                    if(i<=loadsteps.size()-1){
                        JOptionPane.showMessageDialog(this, "行棋步骤错误！");
                    }else if (loadsteps.size() > 0) {

                        chessboardComponent.clear(chessboardComponent.getGameController().getModel());

                        //chessboardComponent.clear(chessboardComponent.getGameController().getModel());
                        chessboardComponent.getGameController().getTimer1().cancel();
                        chessboardComponent.getGameController().getTimer1().purge();
                        chessboardComponent.getGameController().timer.setVisible(false);
                        chessboardComponent.getGameController().showCurrentUser1().setVisible(false);
                        chessboardComponent.getGameController().showCurrentUser2().setVisible(false);
                        chessboardComponent.getGameController().showCurrentUser3().setVisible(false);
                        //chessboardComponent.setGameController(new GameController(chessboardComponent, loadsteps.get(loadsteps.size() - 1).getModel(),loadsteps.get(loadsteps.size() - 1).getUserRed(),loadsteps.get(loadsteps.size() - 1).getUserBlue()));
                        GameController g1 = new GameController(chessboardComponent, loadsteps.get(loadsteps.size() - 1).getModel(), loadsteps.get(loadsteps.size() - 1).getUserRed(), loadsteps.get(loadsteps.size() - 1).getUserBlue());

                        g1.setTurns(loadsteps.size());
                        g1.setSteps(loadsteps);
                        //g1.setTime(10);
                        g1.timer.setFont(new Font("楷体", Font.BOLD, 20));
                        g1.timer.setSize(200, 60);
                        g1.timer.setLocation(890,81+420+120);
                        g1.CountDown();
                        this.add(g1.timer);
                        this.add(g1.showCurrentUser1());
                        this.add(g1.showCurrentUser2());
                        this.add(g1.showCurrentUser3());
////                chessboardComponent.getGameController().timer.setVisible(false);

                        clickTime=0;
                    playbackButton();
                    this.button.setText("回放当前棋局");
                    add(this.button);
                        this.repaint();
                    }
                }
            }

            //            boolean CorrectChessPiece=true;
//            int[] allChessPiece=new int[16];
//            int i=1;
//            int j;
//            while(i<=9){
//                j=1;
//                while(j<=7){
//                    if(loadsteps.get(0).getModel().getGrid()[i-1][j-1].getPiece()!=null){
//                        if(loadsteps.get(0).getModel().getGrid()[i-1][j-1].getPiece().getOwner().equals(PlayerColor.RED))
//                        {
//                            switch (loadsteps.get(0).getModel().getGrid()[i - 1][j - 1].getPiece().getName()) {
//                                case "Elephant":
//                                    allChessPiece[7]++;
//                                    break;
//                                case "Lion":
//                                    allChessPiece[6]++;
//                                    break;
//                                case "Tiger":
//                                    allChessPiece[5]++;
//                                    break;
//                                case "Leopard":
//                                    allChessPiece[4]++;
//                                    break;
//                                case "Wolf":
//                                    allChessPiece[3]++;
//                                    break;
//                                case "Dog":
//                                    allChessPiece[2]++;
//                                    break;
//                                case "Cat":
//                                    allChessPiece[1]++;
//                                    break;
//                                case "Rat":
//                                    allChessPiece[0]++;
//                                    break;
//                            }
//                        }else if(loadsteps.get(0).getModel().getGrid()[i-1][j-1].getPiece().getOwner().equals(PlayerColor.BLUE))
//                        {
//                            switch (loadsteps.get(0).getModel().getGrid()[i - 1][j - 1].getPiece().getName()) {
//                                case "Elephant":
//                                    allChessPiece[15]++;
//                                    break;
//                                case "Lion":
//                                    allChessPiece[14]++;
//                                    break;
//                                case "Tiger":
//                                    allChessPiece[13]++;
//                                    break;
//                                case "Leopard":
//                                    allChessPiece[12]++;
//                                    break;
//                                case "Wolf":
//                                    allChessPiece[11]++;
//                                    break;
//                                case "Dog":
//                                    allChessPiece[10]++;
//                                    break;
//                                case "Cat":
//                                    allChessPiece[9]++;
//                                    break;
//                                case "Rat":
//                                    allChessPiece[8]++;
//                                    break;
//                            }
//                        }
//                    }
//                    j++;
//                }
//                i++;
//            }
//            i=1;
//            while(i<=16){
//                if(allChessPiece[i-1]!=1){
//                    CorrectChessPiece=false;
//                }
//                i++;
//            }
//
//
//            if(!CorrectChessPiece){
//                JOptionPane.showMessageDialog(this,"棋子错误！");
//            }else if(!Objects.equals(path.substring(path.length() - 4), ".txt")){
//                JOptionPane.showMessageDialog(this,"文件类型错误！");
//            }else {
//                if (loadsteps.size() > 0) {
//                    chessboardComponent.clear(chessboardComponent.getGameController().getModel());
//                    chessboardComponent.getGameController().getTimer1().cancel();
//                    chessboardComponent.getGameController().getTimer1().purge();
//                    chessboardComponent.getGameController().timer.setVisible(false);
//                    //chessboardComponent.setGameController(new GameController(chessboardComponent, loadsteps.get(loadsteps.size() - 1).getModel(),loadsteps.get(loadsteps.size() - 1).getUserRed(),loadsteps.get(loadsteps.size() - 1).getUserBlue()));
//                    GameController g1 = new GameController(chessboardComponent, loadsteps.get(loadsteps.size() - 1).getModel(), loadsteps.get(loadsteps.size() - 1).getUserRed(), loadsteps.get(loadsteps.size() - 1).getUserBlue());
//
//                    g1.setTurns(loadsteps.size());
//                    g1.setSteps(loadsteps);
//                    //g1.setTime(10);
//                    g1.timer.setFont(new Font("Rockwell", Font.BOLD, 20));
//                    g1.timer.setSize(200, 60);
//                    g1.timer.setLocation(1000, 81 + 420);
//
//                    g1.CountDown();
//                    this.add(g1.timer);
//////                chessboardComponent.getGameController().timer.setVisible(false);
//                    this.repaint();
//                }
//            }
        });
    }
    private void addMuteButton()
    {
        JButton button = new JButton("Mute");
        button.setLocation(HEIGTH, HEIGTH / 10 + 240);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        button.addActionListener((e) -> {
            chessboardComponent.getGameController().getMusic().stop();
            JOptionPane.showMessageDialog(this, "已静音", "提示", JOptionPane.INFORMATION_MESSAGE);
        });

        add(button);

    }

    private void ChangeBG()
    {
        JComboBox<String> changeBG=new JComboBox<String>();
        changeBG.setVisible(true);
        changeBG.setSize(200,60);
        changeBG.setLocation(HEIGTH,HEIGTH/10+300);
        changeBG.addItem("主题一");
        changeBG.addItem("主题二");
        if(changeBG.getSelectedIndex()==1){

        }else{

        }
        add(changeBG);
    }

    private int clickTime=0;
    JButton button = new JButton("回放当前棋局");
    private void playbackButton()
    {
        JButton button = new JButton("回放当前棋局");
        button.setLocation(HEIGTH, HEIGTH / 10 + 360);
        button.setSize(200, 60);
        button.setFont(new Font("楷体", Font.BOLD, 20));
        button.addActionListener((e) -> {
            //button.setText("下一步");

            List<Step> stepList=new ArrayList<>();
            int i=1;
            while(i<=chessboardComponent.getGameController().getSteps().size()){
                stepList.add(chessboardComponent.getGameController().getSteps().get(i-1));
                i++;
            }

            if(clickTime<=stepList.size()-1) {
                chessboardComponent.clear(chessboardComponent.getGameController().getModel());

                //chessboardComponent.clear(chessboardComponent.getGameController().getModel());
                chessboardComponent.getGameController().getTimer1().cancel();
                chessboardComponent.getGameController().getTimer1().purge();
                chessboardComponent.getGameController().timer.setVisible(false);
                chessboardComponent.getGameController().showCurrentUser1().setVisible(false);
                chessboardComponent.getGameController().showCurrentUser2().setVisible(false);
                chessboardComponent.getGameController().showCurrentUser3().setVisible(false);


                GameController g2 = new GameController(chessboardComponent, stepList.get(clickTime).getModel(), stepList.get(clickTime).getUserRed(), stepList.get(clickTime).getUserBlue());


                g2.setTurns(clickTime + 1);
                g2.setSteps(stepList);


                PlayerColor color;
                if(clickTime%2==1) {
                    color = PlayerColor.BLUE;
                }else{
                    color = PlayerColor.RED;
                }
                this.add(g2.showCurrentUser1());

                this.add(g2.showCurrentUser2());
                g2.showCurrentUser1().setText(String.format("当前回合：%s",color));

                this.repaint();

            }else{
                this.button.setText("回放当前棋局");
                JOptionPane.showMessageDialog(this, "已是最新一步！", "提示", JOptionPane.INFORMATION_MESSAGE);
                this.button.repaint();
            }
                clickTime++;
        });

        add(button);


    }
}
