package com.mobilenamassa.githubapi.model;

public class MainCommit {

	private String sha;
	private Commit commit;
	
	public String getSha() {
		return sha;
	}
	
	public void setSha(String sha) {
		this.sha = sha;
	}

	public Commit getCommit() {
		return commit;
	}

	public void setCommit(Commit commit) {
		this.commit = commit;
	}
	
}