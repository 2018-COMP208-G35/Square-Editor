package model;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import view.container.Article;
import view.helper.Theme;

public class ArticleManager
{
    private static ArticleManager ourInstance = new ArticleManager();
    private List<Article> articleList = new ArrayList();
    private ObjectProperty<Article> article = new SimpleObjectProperty(new Article(new Theme("Noto Serif", 40.0D, "Noto Serif", 21.0D, "Noto Serif", 30.0D, 20.0D, 16.0D, "Noto Serif", 16.0D, "Noto Serif", 16.0D)));

    public static ArticleManager getInstance()
    {
        return ourInstance;
    }

    private ArticleManager()
    {
    }

    public Article getArticle()
    {
        return (Article) this.article.get();
    }

    public ObjectProperty<Article> articleProperty()
    {
        return this.article;
    }

    public void newArticle()
    {
        this.articleList.add(this.article.getValue());
        this.article.setValue(new Article(new Theme("Noto Serif", 40.0D, "Noto Serif", 21.0D, "Noto Serif", 30.0D, 20.0D, 16.0D, "Noto Serif", 16.0D, "Noto Serif", 16.0D)));
    }
}
