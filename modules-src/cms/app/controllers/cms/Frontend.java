package controllers.cms;

import controllers.Secure;
import controllers.Security;
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
        if(page.template.equals("Fragment")){
            notFound();
        }
        if(page.published) {
		    renderTemplate("/cms/" + page.template + ".html", page);
        }
        else {
            boolean hasProfile = Security.check("admin");
            if(hasProfile){
                renderTemplate("/cms/" + page.template + ".html", page);
            }
            else {
                forbidden();
            }
        }
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
        response.contentType = image.data.type();
		renderBinary(image.data.get());
	}

    /**
     * Render an CMSFile.
     */
    public static void image() {
        String name = params.get("name");
        CMSFile image = CMSFile.findById(name);
        response.contentType = image.data.type();
        renderBinary(image.data.get());
    }

}
