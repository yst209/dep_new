package dep.model;


public class ProjectBureauEntity {
	private Long id;
	private String projectId;
	private String operatingBureau;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getOperatingBureau() {
		return operatingBureau;
	}
	public void setOperatingBureau(String operatingBureau) {
		this.operatingBureau = operatingBureau;
	}
}