package controller;


import listener.GameListener;
import model.*;
import view.CellComponent;
import view.ChessComponent;
import view.ChessboardComponent;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Controller is the connection between model and view,
 * when a Controller receive a request from a view, the Controller
 * analyzes and then hands over to the model for processing
 * [in this demo the request methods are onPlayerClickCell() and onPlayerClickChessPiece()]
 *
*/

public class GameController implements GameListener,Serializable{
    private List<Step> steps;
    private Chessboard model;
    private ChessboardComponent view;
    private PlayerColor currentPlayer;
    private DataInput objectIn;

    public PlayerColor getCurrentPlayer() {
        return currentPlayer;
    }

    private int turns=0;

    public int getTurns() {
        return turns;
    }

    public void setTurns(int turns) {
        this.turns = turns;
    }

    // Record whether there is a selected piece before
    private ChessboardPoint selectedPoint;

    public GameController(ChessboardComponent view, Chessboard model) {
        this.view = view;
        this.model = model;
        this.currentPlayer = PlayerColor.BLUE;
        this.steps=new ArrayList<Step>();

        view.registerController(this);
        initialize();
        view.initiateChessComponent(model);
        view.repaint();
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
    }


    private int win() {
        // TODO: Check the board if there is a winner
        if(model.judgeDens1Cell())
        {
            return 1;//蓝方胜
        }else if(model.judgeDens2Cell())
        {
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
            record(currentPlayer,selectedPoint,point);//记录移动之前的src和dest的状态

            model.moveChessPiece(selectedPoint, point);//移动之后判断有没有进陷阱

            ChessComponent temp=view.removeChessComponentAtGrid(selectedPoint);

            steps.get(turns-1).setSrcComponent(temp);
            view.setChessComponentAtGrid(point, temp);
            swapColor();
            selectedPoint = null;
            view.repaint();

            steps.get(turns-1).setModel(model);


            // TODO: if the chess enter Dens or Traps and so on
            //我们已经写完了^
            int flag=win();
            if(flag==1)
            {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(null, "蓝方胜！", "胜利结算", JOptionPane.INFORMATION_MESSAGE);
                });
            }
            else if(flag==2)
            {
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
            }
        }
        else if (selectedPoint.equals(point))//放下棋子
        {
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


                steps.get(turns-1).setModel(model);


            }
        }

    }
    private void record(PlayerColor color,ChessboardPoint src,ChessboardPoint dest)
    {
        turns++;

        Step step=new Step(color,src,dest);
        //step读不了src和dest上的chesspiece
        //在这里读取model元件的
        step.setSrcpiece(model.getChessPieceAt(src));//存储移动之前双方棋子
        step.setDestpiece(model.getChessPieceAt(dest));


        steps.add(step);
    }
    public void undo()//撤回当前回合step
    {
        if(turns>0)
        {
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
        serializeSteps(steps, "resources/保存的游戏.txt");
    }
    catch (IOException e) {
        e.printStackTrace();
    }


}

    public List<Step> loadGameFromFile(String path) {

        // 尝试反序列化List<Person>对象
        try {
            List<Step> deserializedSteps = deserializeSteps(path);
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
        System.out.println("Serialized data is saved in " + filePath);
    }

    public  List<Step> deserializeSteps(String filePath) throws IOException, ClassNotFoundException {
        FileInputStream fileIn = new FileInputStream(filePath);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        List<Step> steps = (List<Step>) in.readObject();
        in.close();
        fileIn.close();
        return steps;
    }

    public void setView(ChessboardComponent view) {
        this.view = view;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public void setCurrentPlayer(PlayerColor currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

}
