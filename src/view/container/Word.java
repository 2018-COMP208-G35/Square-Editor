package view.container;

import javafx.scene.Node;

import java.util.LinkedList;


public class Word extends LinkedList<Node> {

    public Word()
    {
        super();
    }

    public double getWordWidth()
    {
        double totalWidth = 0;
        for (Node n : this)
        {
            totalWidth = totalWidth + n.getBoundsInLocal().getWidth();
        }
        return totalWidth;
    }
}
