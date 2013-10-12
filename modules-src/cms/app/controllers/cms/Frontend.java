package controllers.cms;

import models.cms.CMSFile;
import models.cms.CMSPage;
import play.Play;
import play.mvc.Controller;

import java.util.List;

/**
 * Controller to managed frontend CMS module.
 */
public class Frontend extends Controller {

    /**
     * Display a CMS page.
     *
     * @param pageName
     */
	public static void show(String pageName) {
		CMSPage page = CMSPage.findById(pageName);
		notFoundIfNull(page);
		renderTemplate("/cms/" + page.template + ".html", page);
	}

    /**
     * Display a CMS template object to RSS.
     *
     * @param template
     */
    public static void rss(String template) {
        List<CMSPage> pages = CMSPage.getAllByTemplate(template, Boolean.TRUE);
        String applicationName = Play.configuration.getProperty("application.name");
        response.contentType = "application/rss+xml";
        render("/cms/rss.html", applicationName, template, pages);
    }

    /**
     * Render an CMSFile.
     *
     * @param name
     */
	public static void image(String name) {
		CMSFile image = CMSFile.findById(name);
		renderBinary(image.data.get());
	}

}
