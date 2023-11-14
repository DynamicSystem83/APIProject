package edu.jhu.project;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class TokenController
{
    @Autowired
    private UserService userService;

    @PostMapping("/token")
    public String getToken(@RequestParam("email") final String email, @RequestParam("password") final String password)
	{
		String token = userService.login(email, password);

		if (StringUtils.isEmpty(token))
		{
		   return "no token found";
		}
		return token;
    }
}
