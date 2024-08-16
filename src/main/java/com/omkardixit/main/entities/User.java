package com.omkardixit.main.entities;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User implements UserDetails {

	private static final long serialVersionUID = -7212754206388986540L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String username;

	private String password;

	@Column(name = "ChannelName")
	private String channelName;

	@OneToMany(mappedBy = "user")
	private List<Video> videos;

	@OneToMany(mappedBy = "user")
	private List<Like> likes;

	@OneToMany(mappedBy = "subscribedTo")
	private List<Subscribe> subscribers;
	
	@OneToMany(mappedBy = "subscriber")
	private List<Subscribe> subscriptions;

	public User() {

	}

	public User(int id, String username, String password, String channelName) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.channelName = channelName;
	}

	public List<Subscribe> getSubscriptions() {
		return subscriptions;
	}

	public void setSubscriptions(List<Subscribe> subscriptions) {
		this.subscriptions = subscriptions;
	}

	public List<Subscribe> getSubscribers() {
		return subscribers;
	}

	public void setSubscribers(List<Subscribe> subscribers) {
		this.subscribers = subscribers;
	}

	public List<Like> getLikes() {
		return likes;
	}

	public void setLikes(List<Like> likes) {
		this.likes = likes;
	}

	public List<Video> getVideos() {
		return videos;
	}

	public void setVideos(List<Video> videos) {
		this.videos = videos;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(() -> "write");
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", channelName=" + channelName
				+ "]";
	}

}
