package com.omkardixit.main.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "subscriptions")
public class Subscribe {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne
	@JoinColumn(name = "subscriber_id")
	private User subscriber;

	@ManyToOne
	@JoinColumn(name = "subscribed_to_id")
	private User subscribedTo;

	public Subscribe() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getSubscriber() {
		return subscriber;
	}

	public void setSubscriber(User subscriber) {
		this.subscriber = subscriber;
	}

	public User getSubscribedTo() {
		return subscribedTo;
	}

	public void setSubscribedTo(User subscribedTo) {
		this.subscribedTo = subscribedTo;
	}

}
