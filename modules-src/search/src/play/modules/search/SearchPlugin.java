package play.modules.search;

import play.Logger;
import play.Play;
import play.PlayPlugin;
import play.exceptions.UnexpectedException;
import play.modules.search.store.FileExtractor;
import play.mvc.Router;

/**
 * Integrated to Play's lifecycle, SearchPlugin
 * will trap JPA events and drive the Search
 * service.
 * @author jfp
 *
 */
public class SearchPlugin extends PlayPlugin {
    
    @Override
    public void onApplicationStart() {
        Search.init();
        FileExtractor.init();
    }

    @Override
    public void onApplicationStop() {
        try {
            Search.shutdown();
        } catch (Exception e) {
            throw new UnexpectedException (e);
        }
    }

    @Override
    public void onEvent(String message, Object context) {
        Logger.debug("Message: " + message + " | object: " +context.toString());
        if (!message.startsWith("JPASupport"))
            return;
        if (message.equals("JPASupport.objectPersisted") || message.equals("JPASupport.objectUpdated")) {
            Search.index(context);
        } else if (message.equals("JPASupport.objectDeleted")) {
            Search.unIndex(context);
        }
    }
    
}
