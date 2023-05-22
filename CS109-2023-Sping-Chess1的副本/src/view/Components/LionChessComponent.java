package view.Components;


import model.PlayerColor;
import view.ChessComponent;

import javax.swing.*;
import java.awt.*;

/**
 * This is the equivalent of the ChessPiece class,
 * but this class only cares how to draw Chess on ChessboardComponent
 */
public class LionChessComponent extends ChessComponent {
    //private PlayerColor owner;

    private boolean selected;

    public LionChessComponent(PlayerColor owner, int size) {
        super(owner, size);
        this.owner=owner;
        setSize(size/2, size/2);
        setLocation(0,0);
        setVisible(true);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(!selected) {
            g.drawImage(new ImageIcon("E:\\My life in SUSTech\\学习\\作业\\Java\\project\\CS109-2023-Sping-Chess1的副本(1)\\CS109-2023-Sping-Chess1的副本\\resources\\棋子1.png").getImage(), 0, 0, getWidth(), getHeight(), this);
        }
        else
        {
            g.drawImage(new ImageIcon("E:\\My life in SUSTech\\学习\\作业\\Java\\project\\CS109-2023-Sping-Chess1的副本(1)\\CS109-2023-Sping-Chess1的副本\\resources\\棋子2.png").getImage(), 0, 0, getWidth(), getHeight(), this);

        }
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Font font = new Font("楷体", Font.PLAIN, getWidth() / 2);
        g2.setFont(font);
        g2.setColor(owner.getColor());
        g2.drawString("狮", getWidth() / 4, getHeight() * 5 / 8); // FIXME: Use library to find the correct offset.
        if (isSelected()) { // Highlights the model if selected.
            g.setColor(Color.RED);
            g.drawOval(0, 0, getWidth() , getHeight());
        }
    }
}