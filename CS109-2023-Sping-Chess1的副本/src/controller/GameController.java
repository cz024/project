package controller;


import listener.GameListener;
import model.*;
import view.CellComponent;
import view.ChessComponent;
import view.ChessboardComponent;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.Timer;

import static model.Constant.CHESSBOARD_COL_SIZE;
import static model.Constant.CHESSBOARD_ROW_SIZE;

/**
 * Controller is the connection between model and view,
 * when a Controller receive a request from a view, the Controller
 * analyzes and then hands over to the model for processing
 * [in this demo the request methods are onPlayerClickCell() and onPlayerClickChessPiece()]
 *
*/

public class GameController extends JComponent implements GameListener,Serializable{
    private List<Step> steps;
    private User userRed;
    private User userBlue;
    private Chessboard model;
    private ChessboardComponent view;
    private PlayerColor currentPlayer;
    private DataInput objectIn;
    public AudioPlayer music;
    public JLabel timer=new JLabel();
    private int time;
    private Timer timer1;
    private final CellComponent[][] gridComponents = new CellComponent[CHESSBOARD_ROW_SIZE.getNum()][CHESSBOARD_COL_SIZE.getNum()];
    private int turns=0;
    private JLabel showCurrentUser1=new JLabel();
    private JLabel showCurrentUser2=new JLabel();
    private JLabel showCurrentUser3=new JLabel();


    public PlayerColor getCurrentPlayer() {
        return currentPlayer;
    }



    public int getTurns() {
        return turns;
    }

    public void setTurns(int turns) {
        this.turns = turns;
    }

    // Record whether there is a selected piece before
    private ChessboardPoint selectedPoint;

    public GameController(ChessboardComponent view, Chessboard model,User userRed,User userBlue) {
        this.view = view;
        this.model = model;
        this.currentPlayer = PlayerColor.BLUE;
        this.steps=new ArrayList<Step>();
        this.music=new AudioPlayer("E:\\My life in SUSTech\\学习\\作业\\Java\\project\\CS109-2023-Sping-Chess\\CS109-2023-Sping-Chess\\CS109-2023-Sping-Chess\\music\\Izzamuzzic,Julien Marchal - Shootout (Sped Up).wav");
        this.userBlue=userBlue;
        this.userRed=userRed;
        timer1=new Timer();
        view.registerController(this);
        initialize();
        view.initiateChessComponent(model);
        view.repaint();
        music.play();
    }
    private void initialize() {
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
            }
        }
    }
    // after a valid move swap the player

    private void swapColor() {
        currentPlayer = currentPlayer == PlayerColor.BLUE ? PlayerColor.RED : PlayerColor.BLUE;
        setTime(10);
        showCurrentUser1.setText(String.format("当前回合：%s",currentPlayer));
        showCurrentUser2.setText(String.format("回合数：%d",turns));
        showCurrentUser3.setText("倒计时：");

    }


    public void CountDown(){
            time = 10;
            timer1.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (time > 0) {
                        time--;
                        timer.setText(String.valueOf(time));
                    } else {
                        AIplay();
                    }
                }
            }, 0, 1000);
    }
    private int win() {
        // TODO: Check the board if there is a winner
        if(model.judgeDens1Cell()|findplayerpiece(PlayerColor.RED).size()==0)
        {
            userBlue.setWin(userBlue.getWin()+1);
            userBlue.setScore(userBlue.getScore()+200);
            return 1;//蓝方胜
        }else if(model.judgeDens2Cell()|findplayerpiece(PlayerColor.BLUE).size()==0)
        {
            userRed.setWin(userRed.getWin()+1);
            userRed.setScore(userRed.getScore()+200);
            return 2;//红方胜
        }else{
            return 0;
        }
    }


    // click an empty cell
    @Override
    public void onPlayerClickCell(ChessboardPoint point, CellComponent component) //选中空地判断移动
    {
        if (selectedPoint != null && model.isValidMove(selectedPoint, point)) //可以移动到point
        {
            cancelpainted(findavailablecapture(selectedPoint));
            cancelpainted(findavailablemove(selectedPoint));

            record(currentPlayer,selectedPoint,point);//记录移动之前的src和dest的状态

            model.moveChessPiece(selectedPoint, point);//移动之后判断有没有进陷阱

            ChessComponent temp=view.removeChessComponentAtGrid(selectedPoint);

            steps.get(turns-1).setSrcComponent(temp);
            view.setChessComponentAtGrid(point, temp);
            swapColor();
            selectedPoint = null;
            view.repaint();

            steps.get(turns-1).setModel(model.clone());


            // TODO: if the chess enter Dens or Traps and so on
            //我们已经写完了^
            int flag=win();
            if(flag==1)
            {
//                view.clear(view.getGameController().getModel());
                view.getGameController().getTimer1().cancel();
                view.getGameController().getTimer1().purge();
                view.getGameController().timer.setVisible(false);
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(null, "蓝方胜！", "胜利结算", JOptionPane.INFORMATION_MESSAGE);
                });


            }
            else if(flag==2)
            {
//                view.clear(view.getGameController().getModel());
                view.getGameController().getTimer1().cancel();
                view.getGameController().getTimer1().purge();
                view.getGameController().timer.setVisible(false);
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(null, "红方胜！", "胜利结算", JOptionPane.INFORMATION_MESSAGE);
                });

            }
        }
    }


    // click a cell with a chess
    public void onPlayerClickChessPiece(ChessboardPoint point, view.ChessComponent component) //选中棋子判断捕捉
    {
        if (selectedPoint == null) //selectedpoint是本来选中的点
        {
            if (model.getChessPieceOwner(point).equals(currentPlayer)) {
                selectedPoint = point;

                component.setSelected(true);
                component.repaint();

                print(findavailablemove(selectedPoint));
                print(findavailablecapture(selectedPoint));

                getpainted(findavailablecapture(selectedPoint));
                getpainted(findavailablemove(selectedPoint));

            }
        }
        else if (selectedPoint.equals(point))//放下棋子
        {
            cancelpainted(findavailablecapture(selectedPoint));
            cancelpainted(findavailablemove(selectedPoint));
            selectedPoint = null;

            component.setSelected(false);
            component.repaint();



        }
        // TODO: Implement capture function
        else
        {
            
            boolean flag=model.realcapture(selectedPoint,point);

            if(flag)//移动了那就把这个record留下
            {
                cancelpainted(findavailablecapture(selectedPoint));
                cancelpainted(findavailablemove(selectedPoint));

                record(currentPlayer,selectedPoint,point);

                model.captureChessPiece(selectedPoint,point);
                ChessComponent temp1=view.removeChessComponentAtGrid(point);//拿掉原来占着坑的棋子
                steps.get(turns-1).setDestComponent(temp1);

                ChessComponent temp2=view.removeChessComponentAtGrid(selectedPoint);
                steps.get(turns-1).setSrcComponent(temp2);

                view.setChessComponentAtGrid(point, temp2);

                swapColor();
                selectedPoint = null;
                view.repaint();


                steps.get(turns-1).setModel(model.clone());

                int flag1=win();
                if(flag1==1)
                {
//                    view.clear(view.getGameController().getModel());
                    view.getGameController().getTimer1().cancel();
                    view.getGameController().getTimer1().purge();
                    view.getGameController().timer.setVisible(false);
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(null, "蓝方胜！", "胜利结算", JOptionPane.INFORMATION_MESSAGE);
                    });



                }
                else if(flag1==2)
                {
//                    view.clear(view.getGameController().getModel());
                    view.getGameController().getTimer1().cancel();
                    view.getGameController().getTimer1().purge();
                    view.getGameController().timer.setVisible(false);
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(null, "红方胜！", "胜利结算", JOptionPane.INFORMATION_MESSAGE);
                    });


                }


            }
        }

    }
    private void record(PlayerColor color,ChessboardPoint src,ChessboardPoint dest)
    {
        turns++;

        Step step=new Step(color,src,dest,userRed,userBlue);
        //step读不了src和dest上的chesspiece
        //在这里读取model元件的
        step.setSrcpiece(model.getChessPieceAt(src));//存储移动之前双方棋子
        step.setDestpiece(model.getChessPieceAt(dest));


        steps.add(step);
    }
    public void undo()//撤回当前回合step
    {
//        if(currentPlayer.equals(PlayerColor.RED)){
//
//        if(userRed.getScore()>=100){
//            userRed.setScore(userRed.getScore()-100);
//            if(turns>0) {
//        Step undoStep=steps.get(turns-1);//得到上一步执行之后的结果，也就是这一步的初态
//        //如果移动合理
//        ChessboardPoint src=undoStep.getSrc();
//        ChessboardPoint dest=undoStep.getDest();
//
//        ChessPiece beforesrc=undoStep.getSrcpiece();
//        ChessPiece beforedest=undoStep.getDestpiece();
//
//        model.setChessPiece(src,beforesrc);
//        model.setChessPiece(dest,beforedest);
//
//        view.removeChessComponentAtGrid(dest);//拿掉原来占着坑的棋子
//
//        if(undoStep.getDestComponent()!=null) {
//            view.setChessComponentAtGrid(dest, undoStep.getDestComponent());
//        }
//
//        if(undoStep.getSrcComponent()!=null) {
//            view.setChessComponentAtGrid(src, undoStep.getSrcComponent());
//        }
//        swapColor();
//
//        selectedPoint = null;
//        view.repaint();
//        turns--;
//        steps.remove(undoStep);
//            }
//        }else{
//            JOptionPane.showMessageDialog(this,"蓝方积分不足~");
//            }
//        }else if(currentPlayer.equals(PlayerColor.BLUE)){
//            if(userBlue.getScore()>=100){
//                userBlue.setScore(userBlue.getScore()-100);
                if(turns>0) {
                    Step undoStep=steps.get(turns-1);//得到上一步执行之后的结果，也就是这一步的初态
                    //如果移动合理
                    ChessboardPoint src=undoStep.getSrc();
                    ChessboardPoint dest=undoStep.getDest();

                    ChessPiece beforesrc=undoStep.getSrcpiece();
                    ChessPiece beforedest=undoStep.getDestpiece();

                    model.setChessPiece(src,beforesrc);
                    model.setChessPiece(dest,beforedest);

                    view.removeChessComponentAtGrid(dest);//拿掉原来占着坑的棋子

                    if(undoStep.getDestComponent()!=null) {
                        view.setChessComponentAtGrid(dest, undoStep.getDestComponent());
                    }

                    if(undoStep.getSrcComponent()!=null) {
                        view.setChessComponentAtGrid(src, undoStep.getSrcComponent());
                    }
                    swapColor();

                    selectedPoint = null;
                    view.repaint();
                    turns--;
                    steps.remove(undoStep);
                    setTime(10);
//                }
            }else{
                JOptionPane.showMessageDialog(this,"无路可退");
            }

    }
    public void setModel(Chessboard model) {
        this.model = model;
    }

    public Chessboard getModel() {
        return model;
    }

    public ChessboardComponent getView() {
        return view;
    }


public void save() {

    List<Step> objectsToSerialize = steps;
    try {
        File directory = new File("data");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        serializeSteps(steps, "data/保存的游戏.txt");
    }
    catch (IOException e) {
        e.printStackTrace();
    }


}

    public List<Step> loadGameFromFile(String path) {

        // 尝试反序列化List<Person>对象
        try {
            List<Step> deserializedSteps = deserializeSteps(path);
//            if(!Objects.equals(path.substring(path.length() - 5), ".txt")){
//
//            }
            setTime(10);
            return deserializedSteps;



        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }


    }


    public  void serializeSteps(List<Step> steps, String filePath) throws IOException {
        FileOutputStream fileOut = new FileOutputStream(filePath);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(steps);
        out.close();
        fileOut.close();
        File file = new File(filePath);
//        String absolutePath = file.getAbsolutePath();
        System.out.println("Serialized data is saved in " + filePath);
//        System.out.println("Serialized data is saved in " + absolutePath);
    }

    public  List<Step> deserializeSteps(String filePath) throws IOException, ClassNotFoundException {
        FileInputStream fileIn = new FileInputStream(filePath);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        List<Step> steps = (List<Step>) in.readObject();
        in.close();
        fileIn.close();
        return steps;
    }

    public AudioPlayer getMusic() {
        return music;
    }

    public void setView(ChessboardComponent view) {
        this.view = view;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public User getUserBlue() {
        return userBlue;
    }

    public User getUserRed() {
        return userRed;
    }

    public void setUserBlue(User userBlue) {
        this.userBlue = userBlue;
    }

    public void setUserRed(User userRed) {
        this.userRed = userRed;
    }

    public void setCurrentPlayer(PlayerColor currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
    public List<ChessboardPoint> findavailablemove(ChessboardPoint point)
    {
        //选中之后在周围显示绿色
        List<ChessboardPoint> availablemove=new ArrayList<ChessboardPoint>();
                int i=0;
                while(i<9){
                    int j=0;
                    while(j<7){
                        ChessboardPoint temp = new ChessboardPoint(i, j);
                        if(getModel().isValidMove(point,temp)&&model.getChessPieceAt(temp)==null&&!temp.equals(point))
                        {
                            availablemove.add(temp);
                        }
                        j++;
                    }
                    i++;
                }
//                print(availablemove);
                return availablemove;
    }
    public List<ChessboardPoint> findavailablecapture(ChessboardPoint point)
    {

        List<ChessboardPoint> availablecapture=new ArrayList<ChessboardPoint>();
        int i=0;
        while(i<9){
            int j=0;
            while(j<7){
                ChessboardPoint temp = new ChessboardPoint(i, j);
                if(model.getChessPieceAt(temp)!=null&&model.realcapture(point,temp)&&!temp.equals(point))
                {
                    availablecapture.add(temp);
                }
                j++;
            }
            i++;
        }
//        print(availablecapture);
        return availablecapture;
    }

    public void print(List<ChessboardPoint> chessboardPoints)
    {
        for(int i=0;i<chessboardPoints.size();i++)
        {
            System.out.println("("+chessboardPoints.get(i).getRow()+","+chessboardPoints.get(i).getCol()+")"+"is available to move!");
        }
    }
    public List<ChessboardPoint> findplayerpiece(PlayerColor playerColor)
    {
        List<ChessboardPoint> chessboardPoints=new ArrayList<ChessboardPoint>();
        for(int i=0;i<9;i++)
        {
            for (int j=0;j<7;j++)
            {
                ChessboardPoint temp=new ChessboardPoint(i,j);
                if(getModel().getChessPieceAt(temp)!=null)
                {
                    if(getModel().getChessPieceAt(temp).getOwner().equals(playerColor))
                    {
                        chessboardPoints.add(temp);
                    }
                }
            }
        }
        return chessboardPoints;
    }

    public void setTime(int time) {
        this.time = time;
    }
    public void AIplay()
    {
        AIplayer ai=new AIplayer(this);
        if(selectedPoint!=null) {
            cancelpainted(findavailablecapture(selectedPoint));
            cancelpainted(findavailablemove(selectedPoint));
            ((ChessComponent) view.getGridComponentAt(selectedPoint).getComponents()[0]).setSelected(false);
            ((ChessComponent) view.getGridComponentAt(selectedPoint).getComponents()[0]).repaint();


//            ChessboardPoint destination=ai.AggresiveMove(selectedPoint);//选到的棋子随机移动
//
//
//            if(this.getModel().getChessPieceAt(destination)!=null)//有东西
//            {
//                this.onPlayerClickChessPiece(destination,(ChessComponent) view.getGridComponentAt(destination).getComponents()[0]);
//            }
//            else
//            {
//                this.onPlayerClickCell(destination,view.getGridComponentAt(destination));
//            }

            selectedPoint = null;
        }
//        else {

            ChessboardPoint selectedPoint = ai.agressiveSelect();//在全棋子中随机选择一个点
            print(findavailablemove(selectedPoint));
            print(findavailablecapture(selectedPoint));
            this.setSelectedPoint(selectedPoint);//设置为选中点

            ChessboardPoint destination = ai.AggresiveMove(selectedPoint);//选到的棋子随机移动

            if (this.getModel().getChessPieceAt(destination) != null)//有东西
            {
                this.onPlayerClickChessPiece(destination, (ChessComponent) view.getGridComponentAt(destination).getComponents()[0]);
            } else {

                this.onPlayerClickCell(destination, view.getGridComponentAt(destination));
            }
//        }
    }

    public void setSelectedPoint(ChessboardPoint selectedPoint) {
        this.selectedPoint = selectedPoint;
    }

    public JLabel showCurrentUser1(){
//        JLabel showCurrentUser=new JLabel();
        showCurrentUser1.setText(String.format("当前回合：%s",currentPlayer));
        showCurrentUser1.setFont(new Font("宋体", Font.PLAIN, 20));
        showCurrentUser1.setSize(400,60);
        showCurrentUser1.setLocation(810,81+420);
        return showCurrentUser1;
    }
    public JLabel showCurrentUser2(){
//        JLabel showCurrentUser=new JLabel();
        showCurrentUser2.setText(String.format("回合数：%d",turns));
        showCurrentUser2.setFont(new Font("宋体", Font.PLAIN, 20));
        showCurrentUser2.setSize(400,60);
        showCurrentUser2.setLocation(810,81+420+60);
        return showCurrentUser2;
    }
    public JLabel showCurrentUser3(){
//        JLabel showCurrentUser=new JLabel();
        showCurrentUser3.setText("倒计时：");
        showCurrentUser3.setFont(new Font("宋体", Font.PLAIN, 20));
        showCurrentUser3.setSize(400,60);
        showCurrentUser3.setLocation(810,81+420+120);
        return showCurrentUser3;
    }
    public void getpainted(List<ChessboardPoint> chessboardPoints)
    {
        for(int i=0;i<chessboardPoints.size();i++)
        {
            view.getGridComponents(chessboardPoints.get(i).getRow(),chessboardPoints.get(i).getCol()).setIsValid(true);
            view.getGridComponents(chessboardPoints.get(i).getRow(),chessboardPoints.get(i).getCol()).repaint();
        }
    }
    public void cancelpainted(List<ChessboardPoint> chessboardPoints)
    {
        for(int i=0;i<chessboardPoints.size();i++)
        {
            view.getGridComponents(chessboardPoints.get(i).getRow(),chessboardPoints.get(i).getCol()).setIsValid(false);
            view.getGridComponents(chessboardPoints.get(i).getRow(),chessboardPoints.get(i).getCol()).repaint();
        }
    }


    public Timer getTimer1() {
        return timer1;
    }

    public List<Step> getSteps() {
        return steps;
    }
}
