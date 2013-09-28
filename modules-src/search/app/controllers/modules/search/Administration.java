package controllers.modules.search;

import java.util.List;

import play.Play;
import play.libs.Codec;
import play.modules.search.Search;
import play.modules.search.store.ManagedIndex;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.With;
import controllers.Check;
import controllers.Secure;

@With(Secure.class)
@Check("admin")
public class Administration extends Controller {

    public static void index () {
        List<ManagedIndex> indexes = Search.getCurrentStore().listIndexes();
        render(indexes);
    }
    
    public static void optimize (String name) {
        Search.getCurrentStore().optimize(name);
        index();
    }
    
    public static void reindex(String name) {
        Search.getCurrentStore().rebuild(name);
        index();
    }
    
    public static void reopen (String name) {
        Search.getCurrentStore().reopen(name);
        index();
    }
}
