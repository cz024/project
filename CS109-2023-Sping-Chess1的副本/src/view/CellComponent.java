package view;

import javax.swing.*;
import java.awt.*;

/**
 * This is the equivalent of the Cell class,
 * but this class only cares how to draw Cells on ChessboardComponent
 */

public class CellComponent extends JPanel {
    private Color background;
    private boolean isValid;

    public CellComponent(Color background, Point location, int size) {
        setLayout(new GridLayout(1,1));
        setLocation(location);
        setSize(size, size);
        this.background = background;
        this.isValid = false; // 默认值为 false
    }

    @Override
    public void setBackground(Color background) {
        this.background = background;
    }
    public void setIsValid(boolean isValid) {
        this.isValid = isValid;
        repaint(); // 每次修改 isValid 的值时都要重新绘制
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponents(g);
        g.setColor(background);
        g.fillRect(1, 1, this.getWidth()-1, this.getHeight()-1);

        if (isValid) {
            g.setColor(Color.MAGENTA); // 当 isValid 为 true 时，边框颜色为红色
            g.drawRect(1, 1, this.getWidth()-2, this.getHeight()-2);
        }

        if(background.equals(Color.YELLOW))
        {
            g.drawImage(new ImageIcon("E:\\My life in SUSTech\\学习\\作业\\Java\\project\\CS109-2023-Sping-Chess1的副本(1)\\CS109-2023-Sping-Chess1的副本\\resources\\陷阱.png").getImage(), 0, 0, getWidth(), getHeight(), this);
        }
        else if (background.equals(Color.MAGENTA))
        {
            g.drawImage(new ImageIcon("E:\\My life in SUSTech\\学习\\作业\\Java\\project\\CS109-2023-Sping-Chess1的副本(1)\\CS109-2023-Sping-Chess1的副本\\resources\\兽穴.png").getImage(), 0, 0, getWidth(), getHeight(), this);
        }
        else if(background.equals(Color.LIGHT_GRAY))
        {
            g.drawImage(new ImageIcon("E:\\My life in SUSTech\\学习\\作业\\Java\\project\\CS109-2023-Sping-Chess1的副本(1)\\CS109-2023-Sping-Chess1的副本\\resources\\草地.png").getImage(), 0, 0, getWidth(), getHeight(), this);
        }
        else if(background.equals(Color.CYAN))
        {
            g.drawImage(new ImageIcon("E:\\My life in SUSTech\\学习\\作业\\Java\\project\\CS109-2023-Sping-Chess1的副本(1)\\CS109-2023-Sping-Chess1的副本\\resources\\河流.png").getImage(), 0, 0, getWidth(), getHeight(), this);
        }
    }

}
