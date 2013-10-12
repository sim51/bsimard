package controllers.cms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import models.cms.CMSFile;
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
		List<CMSPage> pages = CMSPage.getAllByTemplate(template, Boolean.FALSE);
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
     * Published the page.
     *
     * @param id
     */
    public static void publish(String id){
        CMSPage page = CMSPage.findById(id);
        notFoundIfNull(page);
        page.published = Boolean.TRUE;
        page.save();
        index(page.template);
    }

    /**
     * UNpublished the page.
     * @param id
     */
    public static void unpublish(String id){
        CMSPage page = CMSPage.findById(id);
        notFoundIfNull(page);
        page.published = Boolean.FALSE;
        page.save();
        index(page.template);
    }

    /**
     * Manager action for filemanager.
     */
    public static void filemanager(){
        String method = params.get("mode");

        // Get information for a file
        if(method.equals("getinfo")) {
            String path =  params.get("path");
            Boolean getSize = params.get("getsize", Boolean.class);

            CMSFile file = CMSFile.findById(path);
            response.contentType = "application/json";
            render("cms/Admin/filemanager/getinfo.html", file);

        }

        // Get information for a folder
        if(method.equals("getfolder")) {
            String path =  params.get("path");
            Boolean getSize = params.get("getsize", Boolean.class);

            List<CMSFile> files = CMSFile.getFolderChildren(path);
            response.contentType = "application/json";
            render("cms/Admin/filemanager/getfolder.html", files);
        }

        // Rename a file (TODO)
        if(method.equals("rename")) {
            String old =  params.get("old");
            String aNew =  params.get("new");

            response.contentType = "application/json";
            render("cms/Admin/filemanager/rename.html");
        }

        // Move a file (TODO)
        if(method.equals("move")) {
            String old =  params.get("old");
            String aNew =  params.get("new");

            response.contentType = "application/json";
            render("cms/Admin/filemanager/move.html");
        }

        // Delete a folder
        if(method.equals("delete")) {
            String path =  params.get("path");

            CMSFile file = CMSFile.findById(path);
            if(file.data != null && file.data.getFile() != null)
                file.data.getFile().delete();
            file.delete();

            response.contentType = "application/json";
            render("cms/Admin/filemanager/delete.html", path);
        }

        // Adding a file (TODO)
        if(method.equals("add")) {
            response.contentType = "application/json";
            render("cms/Admin/filemanager/add.html");
        }

        // Adding a folder (TODO)
        if(method.equals("addFolder")) {
            response.contentType = "application/json";
            render("cms/Admin/filemanager/addfolder.html");
        }

        // Serve the file to the user
        if(method.equals("download")) {
            String path =  params.get("path");
            Frontend.image(path);
        }

    }

}
