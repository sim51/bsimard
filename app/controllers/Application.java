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
        List<CMSPage> projects = CMSPage.getLastests("project", 3);
        render();
    }

    /**
     * Home page.
     */
    public static void blog() {
        CMSPage page = CMSPage.getLastest("blog");
        render("cms/blog.html", page);
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