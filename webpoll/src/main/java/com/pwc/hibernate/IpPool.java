package com.pwc.hibernate;
// Generated Nov 11, 2015 11:11:59 AM by Hibernate Tools 4.0.0

import java.util.Date;

/**
 * IpPool generated by hbm2java
 */
public class IpPool implements java.io.Serializable {

	private Integer id;
	private String ipaddress;
	private Integer vote4picid;
	private Date votedatetime;
	private Integer retired;

	public IpPool() {
	}

	public IpPool(String ipaddress, Integer vote4picid, Date votedatetime, Integer retired) {
		this.ipaddress = ipaddress;
		this.vote4picid = vote4picid;
		this.votedatetime = votedatetime;
		this.retired = retired;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIpaddress() {
		return this.ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public Integer getVote4picid() {
		return this.vote4picid;
	}

	public void setVote4picid(Integer vote4picid) {
		this.vote4picid = vote4picid;
	}

	public Date getVotedatetime() {
		return this.votedatetime;
	}

	public void setVotedatetime(Date votedatetime) {
		this.votedatetime = votedatetime;
	}

	public Integer getRetired() {
		return this.retired;
	}

	public void setRetired(Integer retired) {
		this.retired = retired;
	}

}
