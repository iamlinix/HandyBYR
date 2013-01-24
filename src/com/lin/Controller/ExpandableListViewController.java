package com.lin.Controller;

public class ExpandableListViewController {
	// members
	private String id;
	private String parent;
	private String title;
	private String subtitle;
	private Boolean hasChild;
	private Boolean hasParent;
	private Boolean expanded;
	private Boolean childrenDownloaded;
	private int level;

	public ExpandableListViewController setId(String value) {
		this.id = value;
		return this;
	}

	public String getId() {
		return id;
	}

	public ExpandableListViewController setParent(String value) {
		this.parent = value;
		return this;
	}

	public String getParent() {
		return parent;
	}

	public ExpandableListViewController setTitle(String value) {
		this.title = value;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public ExpandableListViewController setSubtitle(String value) {
		this.subtitle = value;
		return this;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public ExpandableListViewController setHasChild(Boolean value) {
		this.hasChild = value;
		return this;
	}

	public Boolean hasChild() {
		return hasChild;
	}

	public ExpandableListViewController setHasParent(Boolean value) {
		this.hasParent = value;
		return this;
	}

	public Boolean hasParent(Boolean value) {
		return hasParent;
	}

	public ExpandableListViewController setExpanded(Boolean value) {
		this.expanded = value;
		return this;
	}

	public Boolean getExpanded() {
		return expanded;
	}
	
	public ExpandableListViewController setChildrenDownloaded(Boolean value) {
		this.childrenDownloaded = value;
		return this;
	}
	
	public Boolean hasChildrenDownloaded() {
		return childrenDownloaded;
	}

	public ExpandableListViewController setLevel(int value) {
		this.level = value;
		return this;
	}

	public int getLevel() {
		return level;
	}

	public ExpandableListViewController(String id, String parent, String title,
			String subtitle, Boolean hasChild, Boolean hasParent, 
			Boolean childrenDowdloaded, Boolean expanded, int level) {
		super();
		this.id = id;
		this.parent = parent;
		this.title = title;
		this.subtitle = subtitle;
		this.hasChild = hasChild;
		this.hasParent = hasParent;
		this.expanded = expanded;
		this.level = level;
		this.childrenDownloaded = childrenDownloaded;
	}

	public ExpandableListViewController() {
		super();
	}

	public static ExpandableListViewController Builder() {
		return new ExpandableListViewController();
	}
}
