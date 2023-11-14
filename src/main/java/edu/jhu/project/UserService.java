package edu.jhu.project;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Service("userService")
public class UserService
{
    static UserRepository userRepository = new UserRepository();

    public String login(String email, String password)
	{
        Optional<SiteUser> user = userRepository.login(email, password);

        if(user.isPresent()){
            String token = UUID.randomUUID().toString();
            SiteUser custom = user.get();
            custom.setToken(token);
            userRepository.save(custom);

            return token;
        }

        return StringUtils.EMPTY;
    }

    public Optional<User> findByToken(String token)
	{
        Optional<SiteUser> siteUser = userRepository.findByToken(token);
        if (siteUser.isPresent())
		{
            SiteUser tempUser = siteUser.get();
            User user = new User(tempUser.getEmail(), tempUser.getPassword(), true, true, true, true,
                    AuthorityUtils.createAuthorityList("USER"));
            return Optional.of(user);
        }
        return  Optional.empty();
    }

    public SiteUser findById(int id)
	{

        Optional<SiteUser> user = userRepository.findById(id);
        return user.orElse(null);
    }

    public void save(SiteUser user)
	{
        userRepository.save(user);
    }

    public void create(SiteUser user)
	{
        userRepository.create(user);
    }

	public List<SiteUser> getAll()
	{
		return userRepository.getAll();
	}

	public void delete(SiteUser user)
	{
		userRepository.delete(user);
	}

	public List<SiteUser> getListById(List<Integer> userList)
	{
		return userRepository.getListById(userList);
	}
}
