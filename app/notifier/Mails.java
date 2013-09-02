/**
 *  This file is part of LogiSima.
 *
 *  LogiSima is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  LogiSima is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with LogiSima.  If not, see <http://www.gnu.org/licenses/>.
 */
package notifier;

import models.User;
import play.Logger;
import play.Play;
import play.mvc.Mailer;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Mailer class.
 *
 * @author bsimard
 */
public class Mails extends Mailer {

    /**
     * Method that send a mail for the contact form.
     *
     * @param author
     * @param message
     * @param email
     * @throws java.util.concurrent.ExecutionException
     *
     * @throws InterruptedException
     */
    public static void contact(String author, String message, String email) throws InterruptedException, ExecutionException {
        Logger.debug("A mail is about to be sent by " + author + "(" + email + ")");
        setSubject("[" + Play.configuration.getProperty("application.name").toUpperCase() + "] %s souhaite vous contacter", author);
        setFrom(Play.configuration.getProperty("application.mail.noreply"));
        setReplyTo(email);
        List<User> admins = User.find("SELECT u FROM User u WHERE isAdmin = true").fetch();
        for (User admin : admins) {
            if (admin.email != null && admin.email.contains("@")) {
                addRecipient(admin.email);
            }
        }
        send(author, message, email);
    }


}
