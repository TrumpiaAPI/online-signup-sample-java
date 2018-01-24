/* This file is part of Sign-up Page Sample.

The Sign-up Page Sample is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

The Sign-up Page Sample is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with The Sign-up Page Sample.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.trumpia.signup.controller;

import com.trumpia.signup.command.SubscriptionCommand;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class SubscriptionController {

    // This function will get parameters from web page, and proceed mobile number search.
    @RequestMapping(value = "/param", method = RequestMethod.POST)
    public void subscription(HttpServletRequest request) throws Exception {
        // Parameters from web page
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String mobileNum = request.getParameter("mobileNumber");
        SubscriptionCommand command = new SubscriptionCommand();

        command.execute(firstName, lastName, mobileNum);
    }

}
