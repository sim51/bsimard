package controllers.cms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;

import models.cms.CMSImage;
import models.cms.CMSPage;

import org.apache.commons.lang.StringUtils;

import play.data.validation.Valid;
import play.db.jpa.Blob;
import play.libs.MimeTypes;
import play.mvc.Controller;
import play.mvc.With;
import controllers.Check;
import controllers.Secure;

/**
 * Admin controller for CMS module.
 */
@With(Secure.class)
@Check("admin")
public class Admin extends Controller {

    /**
     * Display all CMS pages.
     */
    public static void index(String template) {
        if(template == null) {
            if(CMSPage.getAllTemplate().size() > 0) {
                template = CMSPage.getAllTemplate().get(0);
            }
            else{
                template = "Fragment";
            }
        }
		List<CMSPage> pages = CMSPage.getAllByTemplate(template);
		render(pages, template);
	}

    /**
     * Display edit form for a CMS page.
     *
     * @param pageName
     */
	public static void editPage(String pageName) {
		CMSPage page = CMSPage.findById(pageName);
        String template = page.template;
		renderTemplate("@edit", page, template);
	}

    /**
     * Display form to add a CMS page.
     */
	public static void addPage(String template) {
		CMSPage page = new CMSPage();
        page.template = template;
		renderTemplate("@edit", page, template);
	}

    /**
     * Saving a CMS page.
     *
     * @param page
     */
	public static void savePage(@Valid CMSPage page) {
        String template = page.template;
		if (request.params.get("delete") != null) {
			page.delete();
			index(template);
		}
        if (validation.hasErrors()) {
            renderTemplate("@edit", page, template);
        }
        page.updated = new Date();
		page.save();
        flash.success("Saved");

		if (request.params.get("savePage") != null) {
			Frontend.show(page.name);
        }
		editPage(page.name);
	}

    /**
     * Delete a page.
     *
     * @param id
     */
    public static void delete(String id){
        CMSPage page = CMSPage.findById(id);
        notFoundIfNull(page);
        String template = page.template;
        page.delete();
        index(template);
    }

    /**
     * Upload an image for tiny mce.
     *
     * @param data
     * @param title
     */
	public static void upload(File data, String title) {
		CMSImage image = new CMSImage();
		image.name = data.getName();
		if (StringUtils.isEmpty(title))
			image.title = data.getName();
		else
			image.title = title;
		String mimeType = MimeTypes.getContentType(data.getName());
		image.data = new Blob();
		try {
			image.data.set(new FileInputStream(data), mimeType);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		image.save();
		redirectToStatic("/public/tiny_mce/plugins/advimage/image.htm");
	}

    /**
     * Display all image.
     */
    public static void imagelist() {
		List<CMSImage> images = CMSImage.findAll();
		render(images);
	}
}
