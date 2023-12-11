package edu.jhu.project.security;

import edu.jhu.project.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;


public class UserRepository
{
	private List<SiteUser> users = new ArrayList<SiteUser>();
	private final AtomicInteger userIdCounter = new AtomicInteger();

	public UserRepository()
	{
		users.add(new SiteUser(userIdCounter.incrementAndGet(), "jDoe@gmail.com", "pass1"));
		users.add(new SiteUser(userIdCounter.incrementAndGet(), "jSmith@gmail.com", "pass2"));
	}

    public Optional<SiteUser> login(String email, String password)
	{
		for (SiteUser u : users)
		{
			if ( (u.getEmail().equals(email)) && (u.getPassword().equals(password)) )
			{
				return Optional.of(u);
			}
		}
		return Optional.empty();
	}

    public Optional<SiteUser> findByToken(String token)
	{
		if (token != null)
		{
			for (SiteUser u : users)
			{
				if ( (u.getToken() != null) && (u.getToken().equals(token)) )
				{
					return Optional.of(u);
				}
			}
		}
		return Optional.empty();
	}

    public Optional<SiteUser> findById(int id)
	{
		for (SiteUser u : users)
		{
			if (u.getId() == id)
			{
				return Optional.of(u);
			}
		}
		return Optional.empty();
	}

	public Optional<SiteUser> findByEmail(String email)
	{
		for (SiteUser u : users)
		{
			if ( u.getEmail().equals(email))
			{
				return Optional.of(u);
			}
		}
		return Optional.empty();
	}

    public List<SiteUser> getAll()
	{
		return users;
	}

    public void save(SiteUser user)
	{
		for (SiteUser u : users)
		{
			if (u.getId() == user.getId())
			{
				u = user;
				return;
			}
		}
	}

    public void create(SiteUser user)
	{
		user.setId(userIdCounter.incrementAndGet());
		users.add(user);
	}

    public void delete(SiteUser user)
	{
		users.remove(user);
	}

	public List<SiteUser> getListById(List<Integer> userList)
	{
		List<SiteUser> outList = new ArrayList<SiteUser>();
		for (SiteUser u : users)
		{
			if (userList.contains(u.getId()))
			{
				outList.add(u);
			}
		}
		return outList;
	}
}
