package models.cms;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import play.data.validation.Required;
import play.db.jpa.Blob;
import play.db.jpa.GenericModel;
import play.modules.search.Field;
import play.modules.search.Indexed;

import java.util.Date;

@Entity
@Indexed
@Table(name = "cmsimage")
public class CMSImage extends GenericModel {

    @Id
    @Required
    @Field(sortable = true)
    public String name;

	@Required
    @Field(sortable = true)
	public String title;

    @Field
	@Required
	public Blob data;

    @Field(sortable = true)
    public Date created = new Date();

}
