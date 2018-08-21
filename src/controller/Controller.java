package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Toggle;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.PopupFeatures;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import model.ArticleManager;
import model.io.HTMLSaver;
import model.semantic.Semantic;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import view.Caret;
import view.concrete.Author;
import view.concrete.MainTitle;
import view.concrete.Paragraph;
import view.concrete.Quote;
import view.concrete.SectionTitle;
import view.concrete.SubSectionTitle;
import view.concrete.SubSubSectionTitle;
import view.container.Article;
import view.container.Section;
import view.container.TextLine;
import view.container.TextSection;
import view.container.Article.Structure;
import view.helper.CaretHelper;

public class Controller
{
    @FXML
    public MenuItem sectionItem;
    @FXML
    public MenuItem subSectionItem;
    @FXML
    public MenuItem subSubSectionItem;
    @FXML
    public Label currentStructure;
    @FXML
    public Button intelligentSuggest;
    @FXML
    public ScrollPane scrollContainer;
    @FXML
    public Button quoteBtn;
    @FXML
    public Button openFileBtn;
    @FXML
    public Button saveFileBtn;
    @FXML
    public Pane articleContainer;
    @FXML
    public Pane mainPane;
    @FXML
    private ColorPicker titleColorPicker;
    @FXML
    private ColorPicker authoerColorPicker;
    @FXML
    private ColorPicker sectionsColorPicker;
    @FXML
    private ColorPicker paragraphColorPicker;
    @FXML
    private ColorPicker quoteColorPicker;
    @FXML
    private Label status;
    private final FileChooser fileChooser = new FileChooser();
    private Path currentFilePath;
    private ObjectProperty<Article> article = new SimpleObjectProperty();

    public Controller()
    {
    }

    public void initialize()
    {
        article.bind(ArticleManager.getInstance().articleProperty());
        article.addListener(new ChangeListener<Article>()
        {
            public void changed(ObservableValue<? extends Article> observable, Article oldValue, Article newValue)
            {
                article.getValue().getChildren().removeListener(new InvalidationListener()
                {
                    @Override
                    public void invalidated(Observable observable)
                    {
                        intelligentSuggest(observable);

                    }
                });
                articleContainer.getChildren().remove(0);
                articleContainer.getChildren().add(newValue);
                intelligentSuggest(observable);
                newValue.getChildren().addListener(new InvalidationListener()
                {
                    @Override
                    public void invalidated(Observable observable)
                    {
                        intelligentSuggest(observable);
                    }
                });
            }
        });
        articleContainer.getChildren().add(article.getValue());
        setHandlers();
        Platform.runLater(new Runnable()
        {
            public void run()
            {
                ((Node) article.getValue().getChildren().get(0)).requestFocus();
            }
        });
    }

    private void setHandlers()
    {
        quoteBtn.setOnMousePressed(new EventHandler<MouseEvent>()
        {
            public void handle(MouseEvent event)
            {
                article.getValue().createNewSectionAfterCaret(Structure.QUOTE);
            }
        });
        sectionItem.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent event)
            {
                article.getValue().createNewSectionAfterCaret(Structure.SECTION_TITLE);
            }
        });
        subSectionItem.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent event)
            {
                article.getValue().createNewSectionAfterCaret(Structure.SUBSECTION_TITLE);
            }
        });
        subSubSectionItem.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent event)
            {
                article.getValue().createNewSectionAfterCaret(Structure.SUBSUBSECTION_TITLE);
            }
        });
        scrollContainer.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            public void handle(MouseEvent event)
            {
                article.getValue().payAttention();
            }
        });
        CaretHelper.getInstance().getSelectedCaretProperty().addListener(new ChangeListener<Toggle>()
        {
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue)
            {
                Caret caret = CaretHelper.getInstance().getSelectedCaret();
                if (caret != null)
                {
                    Section section = (Section) caret.getParent().getParent();
                    Point2D positionOfCaretInLine = caret.localToParent(caret.getLayoutBounds().getMaxX(), caret.getLayoutBounds().getMaxY());
                    TextLine line = (TextLine) caret.getParent();
                    Point2D positionOfLineInSection = line.localToParent(positionOfCaretInLine);
                    Point2D positionOfLineInArticle = section.localToParent(positionOfLineInSection);
                    System.out.println("Art: " + positionOfLineInArticle);
                    System.out.println("Min " + scrollContainer.getHmin() + "; " + scrollContainer.getVmin());
                    System.out.println("Max " + scrollContainer.getHmax() + "; " + scrollContainer.getVmax());
                    if (section instanceof MainTitle)
                    {
                        currentStructure.setText("Title");
                    } else if (section instanceof Author)
                    {
                        currentStructure.setText("Author");
                    } else if (section instanceof SectionTitle)
                    {
                        currentStructure.setText("Section");
                    } else if (section instanceof SubSectionTitle)
                    {
                        currentStructure.setText("Subsection");
                    } else if (section instanceof SubSubSectionTitle)
                    {
                        currentStructure.setText("Subsubsection");
                    } else if (section instanceof Paragraph)
                    {
                        currentStructure.setText("Paragraph");
                    } else if (section instanceof Quote)
                    {
                        currentStructure.setText("Quote");
                    }
                } else
                {
                    currentStructure.setText("Editing...");
                }

            }
        });
        intelligentSuggest.setOnMousePressed(new EventHandler<MouseEvent>()
        {
            public void handle(MouseEvent event)
            {
                intelligentSuggest.setText("Title");
                article.getValue().createMainTitle();
            }
        });
        ((Article) article.getValue()).getChildren().addListener(this::intelligentSuggest);
    }

    private void intelligentSuggest(Observable observable)
    {
        if (!((Article) article.getValue()).hasMainTitle())
        {
            intelligentSuggest.setText("Title");
            intelligentSuggest.setOnMousePressed(new EventHandler<MouseEvent>()
            {
                public void handle(MouseEvent event)
                {
                    article.getValue().createMainTitle();
                }
            });
        } else if (!((Article) article.getValue()).hasAuthor())
        {
            intelligentSuggest.setText("Author");
            intelligentSuggest.setOnMousePressed(new EventHandler<MouseEvent>()
            {
                public void handle(MouseEvent event)
                {
                    article.getValue().createAuthor();
                }
            });
        } else if (!((Article) article.getValue()).hasSection())
        {
            intelligentSuggest.setText("Section");
            intelligentSuggest.setOnMousePressed(new EventHandler<MouseEvent>()
            {
                public void handle(MouseEvent event)
                {
                    article.getValue().createNewSectionAfterCaret(Structure.SECTION_TITLE);
                }
            });
        } else
        {
            intelligentSuggest.setText("Paragraph");
            intelligentSuggest.setOnMousePressed(new EventHandler<MouseEvent>()
            {
                public void handle(MouseEvent event)
                {
                    article.getValue().createNewSectionAfterCaret(Structure.PARAGRAPH);
                }
            });
        }

    }

    public void setTitleColor(ActionEvent actionEvent)
    {
        ((Article) article.getValue()).paint(Structure.MAIN_TITLE, (Color) titleColorPicker.getValue());
    }

    public void setAuthorColor(ActionEvent actionEvent)
    {
        ((Article) article.getValue()).paint(Structure.AUTHOR, (Color) authoerColorPicker.getValue());
    }

    public void setSectionsColor(ActionEvent actionEvent)
    {
        ((Article) article.getValue()).paint(Structure.SECTION_TITLE, (Color) sectionsColorPicker.getValue());
    }

    public void setParagraphColor(ActionEvent actionEvent)
    {
        ((Article) article.getValue()).paint(Structure.PARAGRAPH, (Color) paragraphColorPicker.getValue());
    }

    public void setQuoteColor(ActionEvent actionEvent)
    {
        ((Article) article.getValue()).paint(Structure.QUOTE, (Color) quoteColorPicker.getValue());
    }

    public void newFile(ActionEvent actionEvent)
    {
        showSaveFileDialog();
    }

    public void openFile(ActionEvent actionEvent)
    {
        boolean saved = showSaveFileDialog();
        if (saved)
        {
            fileChooser.setTitle("Load File");
            File file = fileChooser.showOpenDialog(openFileBtn.getScene().getWindow());
            if (file != null)
            {
                if (file.getName().matches(".+\\.se"))
                {
                    new StringBuilder();

                    try
                    {
                        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                        Document doc = dBuilder.parse(file);
                        doc.getDocumentElement().normalize();
                        Element rootElement = doc.getDocumentElement();
                        NodeList childNodes = rootElement.getChildNodes();
                        getElement(childNodes);
                        System.out.println("---------------------------------------------------------------");
                        reportSuccess("Load File Successful");
                    } catch (ParserConfigurationException | IOException | SAXException var10)
                    {
                        reportFailed("Fail to parse the file " + file.getName());
                    }
                } else
                {
                    reportFailed("Fail to open the file " + file.getName() + " : Illegal File Name");
                }
            }
        }

    }

    public void getElement(NodeList nodeList)
    {
        Article a = (Article) article.getValue();

        for (int i = 0; i < nodeList.getLength(); ++i)
        {
            org.w3c.dom.Node node = nodeList.item(i);
            if (node.getNodeType() == 1)
            {
                if (node.getNodeName().equals("title"))
                {
                    a.createMainTitle();
                    ((TextSection) a.getCaretSection()).loadContentByAdd(node.getTextContent());
                }

                if (node.getNodeName().equals("author"))
                {
                    a.createAuthor();
                    ((TextSection) a.getCaretSection()).loadContentByAdd(node.getTextContent());
                }

                if (node.getNodeName() == "section" | node.getNodeName() == "subsection" | node.getNodeName() == "subsubsection" | node.getNodeName() == "paragraph" | node.getNodeName() == "quote")
                {
                    NodeList childList = node.getChildNodes();

                    for (int j = 0; j < childList.getLength(); ++j)
                    {
                        org.w3c.dom.Node childNode = childList.item(j);
                        checkContent(childNode);
                    }
                }
            }
        }

    }

    private void readSection(org.w3c.dom.Node node)
    {
        Article a = (Article) article.getValue();
        a.createNewSectionAfterCaret(Structure.SECTION_TITLE);
        String nameOfChildNode = node.getNodeName();
    }

    public void checkContent(org.w3c.dom.Node node)
    {
        Article a = (Article) article.getValue();
        String nameOfChildNode = node.getNodeName();
        if (nameOfChildNode.equals("title"))
        {
            org.w3c.dom.Node parentNode = node.getParentNode();
            if (parentNode.getNodeName().equals("section"))
            {
                a.createNewSectionAfterCaret(Structure.SECTION_TITLE);
            } else if (parentNode.getNodeName().equals("subsection"))
            {
                a.createNewSectionAfterCaret(Structure.SUBSECTION_TITLE);
            } else if (parentNode.getNodeName().equals("subsubsection"))
            {
                a.createNewSectionAfterCaret(Structure.SUBSUBSECTION_TITLE);
            }

            NodeList childOfTitle = node.getChildNodes();

            for (int z = 0; z < childOfTitle.getLength(); ++z)
            {
                org.w3c.dom.Node childTitle = childOfTitle.item(z);
                if (childTitle.getNodeType() == 3)
                {
                    System.out.println("*Title of Section: " + childTitle.getTextContent());
                    ((TextSection) a.getCaretSection()).loadContentByAdd(childTitle.getTextContent());
                }
            }
        }

        NodeList childOfSubsubsection;
        int k;
        org.w3c.dom.Node child;
        if (nameOfChildNode.equals("paragraph"))
        {
            a.createNewSectionAfterCaret(Structure.PARAGRAPH);
            System.out.println("*Paragraph: ");
            childOfSubsubsection = node.getChildNodes();

            for (k = 0; k < childOfSubsubsection.getLength(); ++k)
            {
                child = childOfSubsubsection.item(k);
                if (child.getNodeType() == 3)
                {
                    System.out.println("text: " + child.getTextContent());
                    ((TextSection) a.getCaretSection()).loadContentByAdd(child.getTextContent());
                }

                if (child.getNodeType() == 1)
                {
                    if (child.getNodeName().equals("strong"))
                    {
                        ((TextSection) a.getCaretSection()).setCurrentSemantic(Semantic.STRONG);
                    } else if (child.getNodeName().equals("emphasis"))
                    {
                        ((TextSection) a.getCaretSection()).setCurrentSemantic(Semantic.EMPHASIS);
                    }

                    getTagTextValue(child);
                    ((TextSection) a.getCaretSection()).loadContentByAdd(child.getTextContent());
                    ((TextSection) a.getCaretSection()).setCurrentSemantic(Semantic.NORMAL);
                }
            }
        }

        if (node.getNodeName().equals("quote"))
        {
            a.createNewSectionAfterCaret(Structure.QUOTE);
            ((TextSection) a.getCaretSection()).loadContentByAdd(node.getTextContent());
        }

        if (node.getNodeName().equals("section"))
        {
            childOfSubsubsection = node.getChildNodes();

            for (k = 0; k < childOfSubsubsection.getLength(); ++k)
            {
                child = childOfSubsubsection.item(k);
                checkContent(child);
            }
        }

        if (node.getNodeName().equals("subsection"))
        {
            childOfSubsubsection = node.getChildNodes();

            for (k = 0; k < childOfSubsubsection.getLength(); ++k)
            {
                child = childOfSubsubsection.item(k);
                checkContent(child);
            }
        }

        if (node.getNodeName().equals("subsubsection"))
        {
            childOfSubsubsection = node.getChildNodes();

            for (k = 0; k < childOfSubsubsection.getLength(); ++k)
            {
                child = childOfSubsubsection.item(k);
                checkContent(child);
            }
        }

    }

    public String getTagTextValue(org.w3c.dom.Node node)
    {
        return node.getNodeType() == 1 && !node.getNodeName().equals("text") ? node.getTextContent() : "";
    }

    public boolean saveFile()
    {
        fileChooser.setTitle("Save Article");
        String title = ((Article) article.getValue()).getTitle();
        if (title == null)
        {
            title = "MyArticle";
        }

        fileChooser.setInitialFileName(title + ".se");
        File file = fileChooser.showSaveDialog(saveFileBtn.getScene().getWindow());
        if (file != null)
        {
            try
            {
                BufferedWriter out = new BufferedWriter(new FileWriter(file));
                Throwable var4 = null;

                boolean var5;
                try
                {
                    out.write(((Article) article.getValue()).toXML());
                    reportSuccess("File Saved");
                    var5 = true;
                } catch (Throwable var15)
                {
                    var4 = var15;
                    throw var15;
                } finally
                {
                    if (out != null)
                    {
                        if (var4 != null)
                        {
                            try
                            {
                                out.close();
                            } catch (Throwable var14)
                            {
                                var4.addSuppressed(var14);
                            }
                        } else
                        {
                            out.close();
                        }
                    }

                }

                return var5;
            } catch (IOException var17)
            {
                reportFailed("Fail to save file: " + var17.getMessage());
                return false;
            }
        } else
        {
            return false;
        }
    }

    public void exportToHTML(ActionEvent actionEvent)
    {
        fileChooser.setTitle("Export to HTML");
        String title = ((Article) article.getValue()).getTitle();
        if (title == null)
        {
            title = "MyArticle";
        }

        fileChooser.setInitialFileName(title + ".html");
        File file = fileChooser.showSaveDialog(saveFileBtn.getScene().getWindow());
        if (file != null)
        {
            try
            {
                HTMLSaver.getInstance().saveToHTML(((Article) article.getValue()).toXML(), file);
            } catch (TransformerException var5)
            {
                reportFailed("Fail to export: " + var5.getMessage());
            }

            reportSuccess("Export to HTML successful");
        }

    }

    public void viewInBrowser(ActionEvent actionEvent)
    {
        try
        {
            String html = HTMLSaver.getInstance().transformToHTML(((Article) article.getValue()).toXML());
            WebView webView = new WebView();
            Callback<PopupFeatures, WebEngine> popupHandler = (pFeatures) -> {
                Stage stage = new Stage();
                stage.setTitle("Popup");
                WebView popupView = new WebView();
                VBox root = new VBox(new Node[]{popupView});
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
                return popupView.getEngine();
            };
            webView.getEngine().setCreatePopupHandler(popupHandler);
            webView.getEngine().loadContent(html);
            Stage view = new Stage();
            Pane root = new Pane();
            root.getChildren().add(webView);
            Scene scene = new Scene(root, 400.0D, 500.0D);
            view.setTitle("Hello World!");
            view.setScene(scene);
            view.show();
        } catch (TransformerException var8)
        {
            reportFailed("Fail to transform file to HTML: " + var8.getMessage());
            var8.printStackTrace();
        }

    }

    public boolean showSaveFileDialog()
    {
        Dialog<ButtonType> dialog = new Dialog();
        dialog.initOwner(mainPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/fxml/saveFileDialog.fxml"));

        try
        {
            dialog.getDialogPane().setContent((Node) fxmlLoader.load());
        } catch (IOException var8)
        {
            System.out.println("Couldn't load the dialog");
            var8.printStackTrace();
            return false;
        }

        ButtonType SAVE = new ButtonType("Save");
        ButtonType CANCEL = new ButtonType("Cancel");
        ButtonType DONOTSAVE = new ButtonType("Don't Save");
        dialog.getDialogPane().getButtonTypes().add(DONOTSAVE);
        dialog.getDialogPane().getButtonTypes().add(CANCEL);
        dialog.getDialogPane().getButtonTypes().add(SAVE);
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && ((ButtonType) result.get()).equals(SAVE))
        {
            boolean saved = saveFile();
            if (saved)
            {
                ArticleManager.getInstance().newArticle();
                return true;
            } else
            {
                return false;
            }
        } else if (result.isPresent() && ((ButtonType) result.get()).equals(DONOTSAVE))
        {
            System.out.println("Don't save File");
            ArticleManager.getInstance().newArticle();
            return true;
        } else
        {
            System.out.println("Cancel");
            return false;
        }
    }

    public void reportFailed(String message)
    {
        status.setTextFill(Color.RED);
        status.setText(message);
    }

    public void reportSuccess(String message)
    {
        status.setTextFill(Color.GREEN);
        status.setText(message);
    }
}
