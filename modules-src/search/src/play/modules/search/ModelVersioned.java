package play.modules.search;

import play.db.jpa.GenericModel;

/**
 */
public interface ModelVersioned {

    public <T extends GenericModel> T getLastVersion();
}
