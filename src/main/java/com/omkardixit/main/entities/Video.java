package com.omkardixit.main.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "videos")
public class Video {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String permalink;

	private String title;

	private int visibility;

	private String description;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@OneToMany(mappedBy = "video")
	private List<Like> likes;

	private int views;

	@Column(name = "processing_status")
	private String procesingStatus;

	public Video() {

	}

	public List<Like> getLikes() {
		return likes;
	}

	public void setLikes(List<Like> likes) {
		this.likes = likes;
	}

	public Video(int id, String permalink, String title, int visibility, String description, User user, int views,
			String procesingStatus) {
		this.id = id;
		this.permalink = permalink;
		this.title = title;
		this.visibility = visibility;
		this.description = description;
		this.user = user;
		this.views = views;
		this.procesingStatus = procesingStatus;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPermalink() {
		return permalink;
	}

	public void setPermalink(String permalink) {
		this.permalink = permalink;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getVisibility() {
		return visibility;
	}

	public void setVisibility(int visibility) {
		this.visibility = visibility;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getViews() {
		return views;
	}

	public void setViews(int views) {
		this.views = views;
	}

	public String getProcesingStatus() {
		return procesingStatus;
	}

	public void setProcesingStatus(String procesingStatus) {
		this.procesingStatus = procesingStatus;
	}

	@Override
	public String toString() {
		return "Video [id=" + id + ", permalink=" + permalink + ", title=" + title + ", visibility=" + visibility
				+ ", description=" + description + ", user=" + user + ", views=" + views + ", procesingStatus="
				+ procesingStatus + "]";
	}

}
