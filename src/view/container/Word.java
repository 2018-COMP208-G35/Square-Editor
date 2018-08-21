package view.container;

import java.util.Iterator;
import java.util.LinkedList;
import javafx.scene.Node;

public class Word extends LinkedList<Node> {
    public Word() {
    }

    public double getWordWidth() {
        double totalWidth = 0.0D;

        Node n;
        for(Iterator var3 = this.iterator(); var3.hasNext(); totalWidth += n.getBoundsInLocal().getWidth()) {
            n = (Node)var3.next();
        }

        return totalWidth;
    }

    public String toString() {
        StringBuilder word = new StringBuilder();
        Iterator var2 = this.iterator();

        while(var2.hasNext()) {
            Node n = (Node)var2.next();
            if (n instanceof TextGrid) {
                word.append(((TextGrid)n).getCharacter().getText());
            }
        }

        return word.toString();
    }
}
