package io.github.miroslavpokorny.blog.model.helper;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateHelper {

    private static final SessionFactory sessionFactory;

    static {
        try {
            Configuration configuration = new Configuration();
            configuration.configure().addResource("mapping/Article.hbm.xml");
            configuration.configure().addResource("mapping/ArticleRating.hbm.xml");
            configuration.configure().addResource("mapping/Category.hbm.xml");
            configuration.configure().addResource("mapping/Comment.hbm.xml");
            configuration.configure().addResource("mapping/Gallery.hbm.xml");
            configuration.configure().addResource("mapping/GalleryItem.hbm.xml");
            configuration.configure().addResource("mapping/User.hbm.xml");
            configuration.configure().addResource("mapping/UserQuestion.hbm.xml");
            configuration.configure().addResource("mapping/UserRole.hbm.xml");
            sessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Init of SessionFactory failed. Exception: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static CloseableSession getSession() throws HibernateException {
        return new CloseableSession(sessionFactory.openSession());
    }
}
