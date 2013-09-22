package controllers;

import controllers.cms.Admin;
import models.cms.CMSPage;

import java.util.List;

/**
 * Controller that managed principal page of the site.
 */
public class Application extends AbstractController {

    /**
     * Home page.
     */
    public static void index() {
        List<CMSPage> blogs = CMSPage.getLastests("blog", 3);
        render(blogs);
    }

    /**
     * Blog page.
     */
    public static void blog() {
        CMSPage page = CMSPage.getLastest("blog");
        render("cms/blog.html", page);
    }

    /**
     * Search action.
     */
    public static void search(String search) {
        render();
    }

    /**
     * Admin page.
     */
    public static void admin(){
        // check if it's an admin user
        isAdminUser();

        Admin.index("page");
    }

}