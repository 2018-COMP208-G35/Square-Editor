package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import view.helper.Page;

public class Main extends Application {
    private static double MINIMUM_WIDOW_WIDTH = Page.getLineWidthLimit() + 4.0D * Page.getPagePadding();
    private static double MINIMUM_WIDOW_HEIGHT = 600.0D;

    public Main() {
    }

    public void start(Stage primaryStage) throws Exception {
        this.loadFont();
        Parent root = (Parent)FXMLLoader.load(this.getClass().getResource("/fxml/editor.fxml"));
        primaryStage.setMinWidth(MINIMUM_WIDOW_WIDTH);
        primaryStage.setMinHeight(MINIMUM_WIDOW_HEIGHT);
        primaryStage.setTitle("Square Editor");
        primaryStage.setScene(new Scene(root, MINIMUM_WIDOW_WIDTH, 800.0D));
        primaryStage.show();
    }


    private void loadFont() {
        Font.loadFont(this.getClass().getResource("/font/CMU/cmunrm.ttf").toExternalForm(), 17.0D);
        Font.loadFont(this.getClass().getResource("/font/CMU/cmunti.ttf").toExternalForm(), 17.0D);
        Font.loadFont(this.getClass().getResource("/font/CMU/cmunbx.ttf").toExternalForm(), 17.0D);
        Font.loadFont(this.getClass().getResource("/font/CMU/cmunbi.ttf").toExternalForm(), 17.0D);
        Font.loadFont(this.getClass().getResource("/font/CMU/cmunui.ttf").toExternalForm(), 17.0D);
        Font.loadFont(this.getClass().getResource("/font/noto-serif/NotoSerif-Bold.ttf").toExternalForm(), 17.0D);
        Font.loadFont(this.getClass().getResource("/font/noto-serif/NotoSerif-BoldItalic.ttf").toExternalForm(), 17.0D);
        Font.loadFont(this.getClass().getResource("/font/noto-serif/NotoSerif-Italic.ttf").toExternalForm(), 17.0D);
        Font.loadFont(this.getClass().getResource("/font/noto-serif/NotoSerif-Regular.ttf").toExternalForm(), 17.0D);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
